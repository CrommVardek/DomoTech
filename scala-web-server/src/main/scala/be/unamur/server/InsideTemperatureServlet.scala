package be.unamur.server

import org.scalatra._


/**
  * Created by Axel on 10-04-16.
  */
class InsideTemperatureServlet extends ScalaWebServerStack {

  get("/") {
    <html>
      <body>
        <h1>InsideTemperatureServlet GET Request</h1>
        This servlet should be used to return the current inside temperature of the house/room.
      </body>
    </html>
  }
}
