package actors

import akka.actor.Actor
import commonsObjects.{Request, Wrapper, StorageClient}
import org.slf4j.LoggerFactory

/**
  * Created by Axel on 22-04-16.
  */
class DatabaseAgendaActor extends Actor{

  // Logger of the Actor.
  val logger =  LoggerFactory.getLogger(getClass)

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
