package be.unamur.server

/**
  * Created by Axel on 10-04-16.
  */
class OutsideTemperatureServlet extends ScalaWebServerStack {

  get("/") {
    <html>
      <body>
        <h1>OutsideTemperatureServlet GET Request</h1>
        This Servlet should be used to handle requests (GET and POST) regarding the outside temperature.\n
        There should only be GET requests that will return the outside temperature via a Web Service API.
      </body>
    </html>
  }

}
