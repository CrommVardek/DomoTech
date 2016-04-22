package be.unamur.actors

import akka.actor.Actor
import be.unamur.commonsObjects.{StorageClient, Request, Wrapper}
import org.slf4j.LoggerFactory

/**
  * Created by Axel on 22-04-16.
  */
class DatabaseActionActor extends Actor{

  // Logger of the Actor.
  val logger =  LoggerFactory.getLogger(getClass)

  val storageClient = new StorageClient()


  def receive = {
                  case wrapper:Wrapper => wrapper.getRequest match{
                                            case Request.createAction =>  {
                                                                            try{
                                                                              val res = storageClient.createAction(wrapper)
                                                                              if (res.equals("OK")){sender ! true}
                                                                              else{
                                                                                sender ! false
                                                                                logger.debug(res)
                                                                              }
                                                                            } catch{case e:Exception => sender ! false}
                                                                          }
                                            case Request.readActionById => {}
                                            case Request.readActionByName => ???
                                            case Request.readActionList => ???
                                            case Request.updateAction => ???
                                            case Request.deleteAction => ???
                                          }
                  case _ => logger.debug("Message error with actor DatabaseActionActor")
                }

}
