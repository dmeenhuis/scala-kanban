package com.kanban

import akka.actor.Actor
import spray.routing._
import spray.http._

trait StaticRoutes extends HttpService with Actor {
  val staticRoutes = {
    get {
      pathPrefix("css") {
        getFromResourceDirectory("css")
      } ~
      pathPrefix("js") {
        getFromResourceDirectory("js")
      }
    }
  }
}
