package be.unamur.server

import _root_.akka.actor.{Props, ActorSystem, Actor}
//import be.unamur.commonsObjects.LuminosityData


class HomeServlet extends ScalaWebServerStack {

  get("/") {

    implicit val system = ActorSystem("Test")
    //val myActor = system.actorOf(Props[ActorTest], "testActor")
    //myActor ! "Test"
    <html>
      <body>
        <h1>Welcome</h1>
        Welcome to the Home Automation Web Page
      </body>
    </html>


  }

}
