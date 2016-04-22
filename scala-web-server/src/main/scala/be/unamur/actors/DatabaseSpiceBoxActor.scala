package be.unamur.actors

import akka.actor.Actor
import be.unamur.commonsObjects.{Request, Wrapper}
import org.slf4j.LoggerFactory

/**
  * Created by Axel on 22-04-16.
  */
class DatabaseSpiceBoxActor extends Actor{

  // Logger of the Actor.
  val logger =  LoggerFactory.getLogger(getClass)

  def receive = {
                  case wrapper:Wrapper => wrapper.getRequest match {
                                            case Request.readSpiceBoxContent => ???
                                            case Request.updateSpiceBoxContent => ???
                                            case Request.deleteSpiceBoxContent => ???
                                            case _ => logger.debug("Wrapper error with actor DatabaseSpiceBoxActor")
                                          }
                  case _ => logger.debug("Message error with actor DatabaseSpiceBoxActor")
                }
}
