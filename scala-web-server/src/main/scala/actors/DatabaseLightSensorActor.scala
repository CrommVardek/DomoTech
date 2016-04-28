package actors

import java.net.InetAddress

import akka.actor.Actor
import commonsObjects.{Request, Wrapper, StorageClient}
import org.slf4j.LoggerFactory

/**
  * Created by Axel on 22-04-16.
  */
class DatabaseLightSensorActor extends Actor {

  // Logger of the Actor.
  val logger =  LoggerFactory.getLogger(getClass)

  val storageClient = new StorageClient("test", 8001, InetAddress.getByName("192.168.43.127"))

  def receive = {
    case wrapper:Wrapper => wrapper.getRequest match{
                              case Request.createLightSensorMonitoring => try {
                                                                            val res = storageClient.createLightSensorMonitoring(wrapper)
                                                                            if (res.equals("OK")) sender ! true
                                                                            else {sender ! false; logger.debug(res)}
                                                                          } catch  {case e:Exception => sender ! false}
                              case Request.readLightSensorMonitoringList => try{sender ! storageClient.readLightSensorMonitoringList()}
                                                                            catch{case e:Exception => sender ! List.empty; context stop self}
                            }
    case "Kill" => context stop self
    case _ => logger.debug("Message error with actor DatabaseLightSensorActor")
  }

}
