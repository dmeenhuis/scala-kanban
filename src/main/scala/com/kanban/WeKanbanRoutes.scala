package com.kanban

import com.kanban.models.Story
import spray.routing._
import spray.http._
import MediaTypes._

trait WeKanbanRoutes extends HttpService {
  val dynamicRoutes = {
    path("") {
      get {
        respondWithMediaType(`text/html`) {
          complete(html.index().toString)
        }
      }
    } ~
    path("card" / "create") {
      get {
        respondWithMediaType(`text/html`) {
          complete(html.create().toString)
        }
      }
    } ~
    path("card" / "save") {
      post {

        formFields('storyNumber, 'title) { (storyNumber, title) =>
        //entity(as[Story]) { story =>

          respondWithMediaType(`text/html`) {
            complete(s"Number: $storyNumber, Title: $title")
          }
        }

      }
    }
  }
}
