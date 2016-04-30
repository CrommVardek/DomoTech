package actors

import java.net.InetAddress

import akka.actor.Actor
import commonsObjects.{StorageClient, Request, Wrapper}
import org.slf4j.LoggerFactory


/**
  * Created by Axel on 22-04-16.
  */
class DatabaseTemperatureSensorActor extends Actor{

  // Logger of the Actor.
  val logger =  LoggerFactory.getLogger(getClass)

  //val storageClient = new StorageClient("test", 8001, InetAddress.getByName("192.168.43.127"))
  val storageClient = new StorageClient()

  def receive = {
    case wrapper:Wrapper => wrapper.getRequest match{
                                        case Request.createTemperatureSensorMonitoring => try{
                                            val res = storageClient.createTemperatureSensorMonitoring(wrapper)
                                            if (res.equals("OK")) sender ! true
                                            else{sender ! false; logger.debug(res)}
                                        } catch{case e:Exception => sender ! false; logger.debug(e.getMessage); context stop self}
                                        case Request.readTemperatureSensorMonitoringList => try{
                                            sender ! storageClient.readTemperatureSensorMonitoringList(wrapper); logger.info("Success")}
                                            catch{case e:Exception => sender ! List.empty; logger.info(e.getMessage); context stop self}
                                        case _ => logger.debug("Wrapper error with actor DatabaseTemperatureSensorActor")
                            }
    case "Kill" => context stop self
    case _ => logger.debug("Message error with actor DatabaseTemperatureSensorActor")
  }

}
