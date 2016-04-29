import org.scalatra._
import javax.servlet.ServletContext
import server._


class ScalatraBootstrap extends LifeCycle {

  override def init(context: ServletContext) {
    context.mount(new HomeServlet, "/*")
    context.mount(new InsideTemperatureServlet, "/rest/insideTemperature")
    context.mount(new InsideLuminosityServlet, "/rest/insideLuminosity")
    context.mount(new SpicesServlet, "/rest/spices")
    context.mount(new SpotsServlet, "/rest/spots")
  }
}
