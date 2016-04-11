import be.unamur.server._
import org.scalatra._
import javax.servlet.ServletContext

class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext) {
    context.mount(new HomeServlet, "/*")
    context.mount(new InsideTemperatureServlet, "/rest/insideTemperature")
    context.mount(new InsideLuminosityServlet, "/rest/insideLuminosity")
    context.mount(new OutsideTemperatureServlet, "/rest/outsideTemperature")
    context.mount(new SpicesServlet, "/rest/spices")
  }
}
