package actors

import java.net.InetAddress

import akka.actor.Actor
import commonsObjects.{Request, Wrapper, StorageClient}
import org.slf4j.LoggerFactory

/**
  * Created by Axel on 22-04-16.
  */
class DatabaseActionActor extends Actor{

  // Logger of the Actor.
  val logger =  LoggerFactory.getLogger(getClass)

  val storageClient = new StorageClient("test", 8001, InetAddress.getByName("192.168.43.127"))


  def receive = {
                  case wrapper:Wrapper => wrapper.getRequest match{
                                            case Request.createAction => try{
                                                                              logger.info("In")
                                                                              logger.info(wrapper.getRequest.toString)
                                                                              logger.info(wrapper.getContainer.getClass.toString)
                                                                              val res = storageClient.createAction(wrapper)
                                                                              logger.info("res= " + res)
                                                                              if (res.equals("OK")){sender ! true}
                                                                              else{
                                                                                sender ! false
                                                                                logger.debug(res)
                                                                              }
                                                                            } catch{case e:Exception => logger.info(e.getMessage); sender ! false}
                                            case Request.readActionById =>  try{
                                                                                sender ! storageClient.readActionById(wrapper)
                                                                              } catch{case e:Exception => sender ! null}
                                            case Request.readActionByName => try{
                                                                                  sender ! storageClient.readActionByName(wrapper)
                                                                              } catch{case e:Exception => sender ! null}
                                            case Request.readActionList =>  try{sender ! storageClient.readActionList()}
                                                                            catch {case e:Exception => sender ! List.empty}
                                            case Request.updateAction => try{
                                                                              val res = storageClient.updateAction(wrapper)
                                                                              if (res.equals("OK")) {sender ! true}
                                                                              else{sender ! false; logger.debug(res)}
                                                                          } catch {case e:Exception => sender ! false}
                                            case Request.deleteAction => try{
                                                                            val res = storageClient.deleteAction(wrapper)
                                                                            if (res.equals("OK")){sender ! true}
                                                                            else {sender ! false; logger.debug(res)}
                                                                          } catch {case e:Exception => sender ! false;}
                                            case _ => logger.debug("Wrapper error with actor DatabaseActionActor")
                                          }
                  case _ => logger.debug("Message error with actor DatabaseActionActor")
                }
}
