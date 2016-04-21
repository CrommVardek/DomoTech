package be.unamur.server

import _root_.akka.actor.ActorSystem
import _root_.akka.util.Timeout
import org.json4s.{DefaultFormats, Formats}
import org.scalatra._
import scalate.ScalateSupport


trait ScalaWebServerStack extends ScalatraServlet with ScalateSupport {

  val actorSystem = ActorSystem("Domotic")
  //
  protected implicit val jsonFormats: Formats = DefaultFormats

  protected implicit val defaultTimeout = Timeout(3)

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
