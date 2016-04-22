import _root_.akka.actor.ActorSystem
import be.unamur.server._
import org.scalatra._
import javax.servlet.ServletContext

class ScalatraBootstrap extends LifeCycle {

  // Get a handle to an ActorSystem and a reference to one of your actors
  val system = ActorSystem()
  //val myActor = system.actorOf(Props[ActorTest])


  override def init(context: ServletContext) {
    context.mount(new HomeServlet, "/*")
    context.mount(new InsideTemperatureServlet, "/rest/insideTemperature")
    context.mount(new InsideLuminosityServlet, "/rest/insideLuminosity")
    context.mount(new SpicesServlet, "/rest/spices")
    context.mount(new SpotsServlet, "/rest/spots")
  }
}
