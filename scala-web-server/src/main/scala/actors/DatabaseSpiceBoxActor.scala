package actors

import akka.actor.Actor
import commonsObjects.{StorageClient, Request, Wrapper}
import org.slf4j.LoggerFactory

/**
  * Created by Axel on 22-04-16.
  */
class DatabaseSpiceBoxActor extends Actor{

  // Logger of the Actor.
  val logger =  LoggerFactory.getLogger(getClass)

  val storageClient = new StorageClient()

  def receive = {
                  case wrapper:Wrapper => wrapper.getRequest match {
                                            case Request.readSpiceBoxContent => try{sender ! storageClient.readSpiceBoxContent(wrapper)}
                                                                                catch{case e:Exception => logger.debug(e.getMessage); sender ! null}
                                            case Request.updateSpiceBoxContent => try{
                                                                                    val res = storageClient.updateSpiceBoxContent(wrapper)
                                                                                    if (res.equals("OK")) sender ! true
                                                                                    else{sender ! false; logger.debug(res)}
                                                                                  } catch {case e:Exception => logger.debug(e.getMessage); sender ! false}
                                            case Request.deleteSpiceBoxContent => try{
                                                                                    val res = storageClient.deleteSpiceBoxContent(wrapper)
                                                                                    if(res.equals("OK")) sender ! true
                                                                                    else{sender ! false; logger.debug(res)}
                                                                                  } catch {case e:Exception => logger.debug(e.getMessage); sender ! false}
                                            case _ => logger.debug("Wrapper error with actor DatabaseSpiceBoxActor")
                                          }
                  case "Kill" => context stop self
                  case _ => logger.debug("Message error with actor DatabaseSpiceBoxActor")
                }
}
