package server

import _root_.akka.actor.{Props, ActorSystem}
import _root_.akka.util.Timeout
import actors.{LightManagerActor, HeatManagerActor}
import commonsObjects.SensorReader
import org.json4s.{DefaultFormats, Formats}
import org.scalatra._
import scalate.ScalateSupport

trait ScalaWebServerStack extends ScalatraServlet with ScalateSupport {

  protected implicit val jsonFormats: Formats = DefaultFormats
  protected implicit val defaultTimeout = Timeout(6000000)

  val actorSystem = ActorSystem("Domotic")



  notFound {
    // remove content type in case it was set through an action
    contentType = null
    // Try to render a ScalateTemplate if no route matched
    findTemplate(requestPath) map { path =>
      contentType = "text/html"
      layoutTemplate(path)
    } orElse serveStaticResource() getOrElse resourceNotFound()
  }


}
