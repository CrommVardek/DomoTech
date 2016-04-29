package actors

import akka.actor.Actor
import commonsObjects.HeatManager
import org.slf4j.LoggerFactory

/**
  * Created by Axel on 10-04-16.
  */
class HeatManagerActor extends Actor{

  val logger = LoggerFactory.getLogger(getClass)

  //val manager:HeatManager = new HeatManager(1, 0.0)

  private var manager:HeatManager = null

  def receive = {
    case manager:HeatManager => this.manager = manager
    case "Kill" => context stop self; logger.info("Actor Terminated")
    case "Get Manager" => logger.info("Sending manager..."); sender ! manager; logger.info("Manager sent!")
    case message:String =>  {
                              if (message.startsWith("Increase Temperature")){
                                logger.info("Message received = " + message)
                                // Retrieve the integer at the end of the message
                                val degree = message.split(" ").last
                                manager.increaseTemp(degree.toDouble)
                                logger.info("Message treated - Temperature was adjusted.")
                              }
                              else{
                                logger.info("Error: Message not understood... \t"+message)
                              }
                            }
  }
}
