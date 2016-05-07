package actors

import java.net.InetAddress

import akka.actor.Actor
import commonsObjects.{Request, Wrapper, StorageClient}
import org.slf4j.LoggerFactory

/**
  * Actor to handle database interactions related to the Agenda.
  * All messages must come in a Wrapper.
  *
  * @see commonsObjects.Agenda
  * @see commonsObjects.Wrapper
  *
  * Created by Axel on 22-04-16.
  */
class DatabaseAgendaActor extends Actor{

  // Logger of the Actor.
  val logger =  LoggerFactory.getLogger(getClass)
  // Interface to the database.
  //  val storageClient = new StorageClient("test", 8001, InetAddress.getByName("192.168.43.127"))
  val storageClient = new StorageClient()

  def receive = {
                  case wrapper:Wrapper => wrapper.getRequest match{
                                            case Request.createAgenda =>  try{
                                                                              val res = storageClient.createAgenda(wrapper)
                                                                              if (res.equals("OK")){sender ! true}
                                                                              else{sender ! false; logger.debug(res)}
                                                                          } catch {case e:Exception => sender ! false}
                                            case Request.readAgendaByDayNumber => try{sender ! storageClient.readAgendaByDayNumber(wrapper)}
                                                                                  catch {case e:Exception => sender ! List.empty}
                                            case Request.readAgendaByRoomId => try{ sender ! storageClient.readAgendaByRoomId(wrapper)}
                                                                                catch{case e:Exception => sender ! List.empty}
                                            case Request.readWeekAgenda =>  try{sender ! storageClient.readWeekAgenda()}
                                                                            catch {case e:Exception => sender ! List.empty}
                                            case Request.deleteAgenda => try{
                                                                            val res = storageClient.deleteAgenda(wrapper)
                                                                            if (res.equals("OK")){sender ! true}
                                                                            else {sender ! false; logger.debug(res)}
                                                                          } catch {case e:Exception => sender ! false}
                                            case _ => logger.debug("Wrapper error with actor DatabaseAgendaActor")
                                          }
                  case "Kill" => context stop self
                  case _ => logger.debug("Message error with actor DatabaseAgendaActor")
                }
}
