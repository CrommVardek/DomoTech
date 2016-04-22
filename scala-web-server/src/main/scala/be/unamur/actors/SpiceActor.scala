package be.unamur.actors

import akka.actor.Actor

import org.slf4j.LoggerFactory

/**
  * Created by Axel on 16-04-16.
  */
class SpiceActor extends Actor{

  val logger =  LoggerFactory.getLogger(getClass)

  def receive = {
    case "Get spices list" => sender ! ""//SpiceData.all
    case "Kill" => context stop self
    case message:String => logger.info("Error: Message not understood...\t"+message)
  }
}
