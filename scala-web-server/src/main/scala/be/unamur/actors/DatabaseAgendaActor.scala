package be.unamur.actors

import akka.actor.Actor
import be.unamur.commonsObjects.{Request, Wrapper}
import org.slf4j.LoggerFactory

/**
  * Created by Axel on 22-04-16.
  */
class DatabaseAgendaActor extends Actor{

  // Logger of the Actor.
  val logger =  LoggerFactory.getLogger(getClass)


  def receive = {
                  case wrapper:Wrapper => wrapper.getRequest match{
                                            case Request.createAgenda => ???
                                            case Request.readAgendaByDayNumber => ???
                                            case Request.readAgendaByRoomId => ???
                                            case Request.readWeekAgenda => ???
                                            case Request.deleteAgenda => ???
                                            case _ => logger.debug("Wrapper error with actor DatabaseAgendaActor")
                                          }
                  case _ => logger.debug("Message error with actor DatabaseAgendaActor")
                }

}
