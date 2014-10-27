package com.kanban

import akka.actor._
import com.kanban.WeKanbanProtocol._
import com.kanban.models.Story

object WeKanbanProtocol {
  case class CreateStory(number: String, title: String)

  case class StoryCreated

  case class StoryCreationFailed(message: String)
}


class WeKanbanSystem extends Actor with ActorLogging {

  def receive = {
    case CreateStory(number, title) =>
      log.info(s"Creating new story ($number) with title '$title'")

      val story = Story(number, title)

      story.save() match {
        case Right(x) =>
          log.info(x)
          sender ! StoryCreated
        case Left(x: Throwable) =>
          log.error(x.getMessage, "Error occurred")
          sender ! StoryCreationFailed(x.getMessage)
      }
  }
}

