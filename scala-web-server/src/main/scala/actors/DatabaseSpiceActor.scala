package actors

import akka.actor.Actor
import commonsObjects.{StorageClient, Request, Wrapper}
import org.slf4j.LoggerFactory

/**
  * Created by Axel on 22-04-16.
  */
class DatabaseSpiceActor extends Actor {

  // Logger of the Actor.
  val logger =  LoggerFactory.getLogger(getClass)

  val storageClient = new StorageClient()

  /**
    * Receive wrapper object representing the request to the database.
    *
    * @return ???
    */
  def receive = {
                  case wrapper:Wrapper => wrapper.getRequest match {
                                            case Request.createSpice  => try{
                                                                            val res = storageClient.createSpice(wrapper)
                                                                            if(res.equals("OK")) sender ! true
                                                                            else{sender ! false; logger.debug(res)}
                                                                          } catch{case e:Exception => logger.debug(e.getMessage); sender ! false }
                                            case Request.readSpiceById => try{
                                                                            sender ! storageClient.readSpiceById(wrapper)
                                                                          } catch {case e:Exception => logger.debug(e.getMessage); sender ! null }
                                            case Request.readSpiceByName => try{
                                                                              sender ! storageClient.readSpiceByName(wrapper)
                                                                            } catch {case e:Exception => logger.debug(e.getMessage); sender ! null }
                                            case Request.readSpiceList => try{
                                                                              sender ! storageClient.readSpiceList()
                                                                          } catch {case e:Exception =>logger.debug(e.getMessage); sender ! List.empty}
                                            case Request.updateSpice => try{
                                                                            val res = storageClient.updateSpice(wrapper)
                                                                            if (res.equals("OK")) sender ! true
                                                                            else{sender ! false; logger.debug(res)}
                                                                          } catch{case e:Exception => logger.debug(e.getMessage); sender ! false}
                                            case Request.deleteSpice => try{
                                                                          val res = storageClient.deleteSpice(wrapper)
                                                                          if(res.equals("OK")) sender ! true
                                                                          else{sender ! false; logger.debug(res)}
                                                                        } catch{case e:Exception => logger.debug(e.getMessage); sender ! false}
                                            case _ => logger.debug("Wrapper error with DatabaseSpiceActor")
                                          }
                  case _ => logger.debug("Message error with DatabaseSpiceActor.")
                }
}
