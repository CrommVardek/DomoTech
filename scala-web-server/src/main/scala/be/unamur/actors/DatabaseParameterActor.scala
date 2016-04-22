package be.unamur.actors

import akka.actor.Actor
import be.unamur.commonsObjects.{Request, Wrapper}
import org.slf4j.LoggerFactory

/**
  * Created by Axel on 22-04-16.
  */
class DatabaseParameterActor extends Actor{

  // Logger of the Actor.
  val logger =  LoggerFactory.getLogger(getClass)

  def receive = {
                  case wrapper:Wrapper => wrapper.getRequest match{
                                            case Request.createParameter => ???
                                            case Request.readParameterByKey => ???
                                            case Request.updateParameter => ???
                                            case Request.deleteParameterByKey => ???
                                            case _ => logger.debug("Wrapper error with actor DatabaseParameterActor")
                                          }
                  case _ => logger.debug("Message error with actor DatabaseParameterActor")
                }
}
