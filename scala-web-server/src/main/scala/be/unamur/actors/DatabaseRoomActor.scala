package be.unamur.actors

import akka.actor.Actor
import be.unamur.commonsObjects.{Request, Wrapper}
import org.slf4j.LoggerFactory

/**
  * Created by Axel on 22-04-16.
  */
class DatabaseRoomActor extends Actor{

  // Logger of the Actor.
  val logger =  LoggerFactory.getLogger(getClass)

  def receive = {
                  case wrapper:Wrapper => wrapper.getRequest match{
                                            case Request.createRoom => ???
                                            case Request.readRoomByName => ???
                                            case Request.readRoomById => ???
                                            case Request.readRoomList => ???
                                            case Request.updateRoom => ???
                                            case Request.deleteRoom => ???
                                            case _ => logger.debug("Wrapper error with actor DatabaseRoomActor")
                                          }
                  case _ => logger.debug("Message error with actor DatabaseRoomActor")
                }
}
