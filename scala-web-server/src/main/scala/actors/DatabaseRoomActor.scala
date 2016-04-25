package actors

import akka.actor.Actor
import commonsObjects.{Request, Wrapper, StorageClient}
import org.slf4j.LoggerFactory

/**
  * Created by Axel on 22-04-16.
  */
class DatabaseRoomActor extends Actor{

  // Logger of the Actor.
  val logger =  LoggerFactory.getLogger(getClass)

  val storageClient = new StorageClient()

  def receive = {
                  case wrapper:Wrapper => wrapper.getRequest match{
                                            case Request.createRoom => try{
                                                                        val res = storageClient.createRoom(wrapper)
                                                                        if (res.equals("OK")) sender ! true
                                                                        else { sender ! false; logger.debug(res)}
                                                                      } catch {case e:Exception => sender ! false}
                                            case Request.readRoomByName => try{sender ! storageClient.readRoomByName(wrapper)}
                                                                            catch {case e:Exception => sender ! null}
                                            case Request.readRoomById =>  try{ sender ! storageClient.readRoomById(wrapper)}
                                                                          catch{case e:Exception => sender ! null}
                                            case Request.readRoomList =>  try{sender ! storageClient.readRoomList()}
                                                                          catch{case e:Exception => sender ! List.empty}
                                            case Request.updateRoom => try{
                                                                            val res = storageClient.updateRoom(wrapper)
                                                                            if(res.equals("OK")) sender ! true
                                                                            else {sender ! false; logger.debug(res)}
                                                                      } catch {case e:Exception => sender ! false}
                                            case Request.deleteRoom => try{
                                                                          val res = storageClient.deleteRoom(wrapper)
                                                                          if (res.equals("OK")) sender ! true
                                                                          else {sender ! false; logger.debug(res)}
                                                                        } catch {case e:Exception => sender ! false}
                                            case _ => logger.debug("Wrapper error with actor DatabaseRoomActor")
                                          }
                  case _ => logger.debug("Message error with actor DatabaseRoomActor")
                }
}
