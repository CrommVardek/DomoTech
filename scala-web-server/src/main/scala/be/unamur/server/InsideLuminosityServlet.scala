package be.unamur.server

/**
  * Created by Axel on 10-04-16.
  */
class InsideLuminosityServlet extends ScalaWebServerStack {

  get("/") {
    <html>
      <body>
        <h1>InsideLuminosityServlet GET Request</h1>
        This Servlet should handle the requests (GET and POST) regarding the inside luminosity.
      </body>
    </html>
  }

}
