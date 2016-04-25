package server

import _root_.akka.actor.{Props, ActorSystem, Actor}
import actors.DatabaseActionActor
import commonsObjects.{Request, Wrapper, Action}


import scala.concurrent.Await
import akka.pattern.ask
import scala.concurrent.duration._



class HomeServlet extends ScalaWebServerStack {

  get("/") {

    implicit val system = ActorSystem("Test")

    val myActor = system.actorOf(Props[DatabaseActionActor], "test1")

    val myAction = new Action()
    val myWrapper = new Wrapper(Request.createAction, myAction)

    def futureResult = ask(myActor, myWrapper)
    val res = Await.result(futureResult, 15.seconds).asInstanceOf[Boolean]

    if (res) {
      println("success")
    }
    else {
      println("failure")
    }




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
