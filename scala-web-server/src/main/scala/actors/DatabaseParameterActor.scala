package actors

import akka.actor.Actor
import commonsObjects.{Request, Wrapper, StorageClient}
import org.slf4j.LoggerFactory

/**
  * Created by Axel on 22-04-16.
  */
class DatabaseParameterActor extends Actor{

  // Logger of the Actor.
  val logger =  LoggerFactory.getLogger(getClass)

  val storageClient = new StorageClient()

  def receive = {
                  case wrapper:Wrapper => wrapper.getRequest match{
                                            case Request.createParameter => try{
                                                                              val res = storageClient.createParameter(wrapper)
                                                                              if (res.equals("OK")) sender ! true
                                                                              else{ sender ! false; logger.debug(res)}
                                                                            } catch {case e:Exception => sender ! false}
                                            case Request.readParameterByKey =>  try{sender ! storageClient.readParameterByKey(wrapper)}
                                                                                catch {case e:Exception => sender ! null}
                                            case Request.updateParameter => try{
                                                                              val res = storageClient.updateParameter(wrapper)
                                                                              if (res.equals("OK")) sender ! true
                                                                              else {sender ! false; logger.debug(res)}
                                                                            } catch {case e:Exception => sender ! false}
                                            case Request.deleteParameterByKey => try{
                                                                                  val res = storageClient.deleteParameterByKey(wrapper)
                                                                                  if(res.equals("OK")) sender ! true
                                                                                  else {sender ! false; logger.debug(res)}
                                                                                } catch {case e:Exception => sender ! false}
                                            case _ => logger.debug("Wrapper error with actor DatabaseParameterActor")
                                          }
                  case _ => logger.debug("Message error with actor DatabaseParameterActor")
                }
}
