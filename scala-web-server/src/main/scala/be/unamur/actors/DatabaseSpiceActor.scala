package be.unamur.actors

import akka.actor.Actor
import be.unamur.commonsObjects.Wrapper
import be.unamur.commonsObjects.Request
import org.slf4j.LoggerFactory

/**
  * Created by Axel on 22-04-16.
  */
class DatabaseSpiceActor extends Actor {

  // Logger of the Actor.
  val logger =  LoggerFactory.getLogger(getClass)

  /**
    * Receive wrapper object representing the request to the database.
    *
    * @return ???
    */
  def receive = {
                  case wrapper:Wrapper => wrapper.getRequest match {
                                            case Request.createSpice  => ???
                                            case Request.readSpiceById => ???
                                            case Request.readSpiceByName => ???
                                            case Request.readSpiceList => ???
                                            case Request.updateSpice => ???
                                            case Request.deleteSpice => ???
                                            case _ => logger.debug("Wrapper error with DatabaseSpiceActor")
                                          }
                  case _ => logger.debug("Message error with DatabaseSpiceActor.")
                }
}
