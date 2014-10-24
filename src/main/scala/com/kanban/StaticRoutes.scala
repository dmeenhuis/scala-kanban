package com.kanban

import spray.routing._
import spray.http._

trait StaticRoutes extends HttpService {
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
