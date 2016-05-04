import commonsObjects.{ManagersConfig, SensorReader}
import org.scalatra._
import javax.servlet.ServletContext
import server._


class ScalatraBootstrap extends LifeCycle {

  val sensorReader = new SensorReader().enable()
  val roueEpice = ManagersConfig.getInstance().getRoueEpices
  val managers = ManagersConfig.getInstance()

  override def init(context: ServletContext) {
    context.mount(new HomeServlet, "/*")
    context.mount(new InsideTemperatureServlet, "/rest/insideTemperature")
    context.mount(new InsideLuminosityServlet, "/rest/insideLuminosity")
    context.mount(new SpicesServlet, "/rest/spices")
    context.mount(new SpotsServlet, "/rest/spots")
  }
}
