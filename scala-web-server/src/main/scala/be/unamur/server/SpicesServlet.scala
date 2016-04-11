package be.unamur.server

/**
  * Created by Axel on 10-04-16.
  */
class SpicesServlet extends ScalaWebServerStack {

  get("/") {
    <html>
      <body>
        <h1>SpicesServlet GET Request</h1>
        This Servlet should be used to handle requests (GET and POST) regarding the spices.\n
      </body>
    </html>
  }

}
