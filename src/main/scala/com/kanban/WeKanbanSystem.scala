package com.kanban

import akka.actor._
import com.kanban.WeKanbanProtocol._
import com.kanban.models._

object WeKanbanProtocol {
  case class CreateStory(number: String, title: String)

  case object StoryCreated

  case class StoryCreationFailed(message: String)

  case object GetAllStories
  
  case class AllStories(stories: Traversable[Story])
  
  case class FindStoriesByPhase(phase: String)

  case class StoriesByPhase(stories: Traversable[Story])

  case class MoveCard(number: String, phase: String)

  case class CardMoved(message: String)
  case class CardMoveFailed(message: String)
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

    case GetAllStories =>
      log.info("Getting all stories")

      sender ! AllStories(Story.all())
      
    case FindStoriesByPhase(phase) =>
      log.info(s"Finding stories with phase '$phase'")

      val stories = Story.findAllStoriesByPhase(phase)

      sender ! StoriesByPhase(stories)

    case MoveCard(number, phase) =>
      log.info(s"Moving card '$number' to phase '$phase'")

      val story = Story.findByNumber(number)

      story.moveTo(phase) match {
        case Right(x) =>
          log.info(x)
          sender ! CardMoved(x)
        case Left(x: Throwable) =>
          log.error(x.getMessage, "Error moving card")
          sender ! CardMoveFailed(x.getMessage)
      }

      sender ! CardMoved
  }
}

