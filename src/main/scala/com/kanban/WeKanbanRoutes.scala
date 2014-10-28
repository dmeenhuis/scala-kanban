package com.kanban

import com.kanban.views.models.StoriesViewModel

import scala.util.{Failure, Success}
import akka.actor._
import akka.util.Timeout
import com.kanban.WeKanbanProtocol._
import spray.routing._
import spray.http._
import MediaTypes._
import scala.concurrent.duration._

trait WeKanbanRoutes extends HttpService with ActorLogging { actor: Actor =>
  import scala.concurrent.ExecutionContext.Implicits.global
  import akka.pattern.ask
  import akka.pattern.pipe

  implicit val timeout = Timeout(5 seconds)
  val kanbanSystem = context.actorOf(Props[WeKanbanSystem])

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
          complete(html.create("").toString)
        }
      }
    } ~
    path("card" / "save") {
      post {

        formFields('storyNumber, 'title) { (storyNumber, title) =>
          respondWithMediaType(`text/html`) {

            onSuccess(kanbanSystem ? CreateStory(storyNumber, title)) {
              case StoryCreated =>
                complete(html.create("Story created!").toString)
              case StoryCreationFailed(message) =>
                complete(html.create(message).toString)
            }
          }
        }
      }
    } ~
    path("card" / "move") {
      post {
        formFields('storyNumber, 'phase) { (storyNumber, phase) =>
          respondWithMediaType(`text/html`) {

            onSuccess(kanbanSystem ? MoveCard(storyNumber, phase)) {
              case CardMoved(message) =>
                complete(StatusCodes.OK, message)
              case CardMoveFailed(message) =>
                complete(StatusCodes.OK, message)
            }

          }

        }
      }
    } ~
    path("kanban" / "board") {
      get {
        respondWithMediaType(`text/html`) {
          onSuccess(kanbanSystem ? GetAllStories) {
            case AllStories(stories) =>
              complete(html.board(StoriesViewModel(stories)).toString)
          }
        }
      }
    }

  }

}

