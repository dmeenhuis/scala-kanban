package com.kanban.models

import org.squeryl.PrimitiveTypeMode._
import KanbanSchema._
import spray.httpx.unmarshalling._
import spray.util._
import spray.http._
import spray.http.HttpEntity
import spray.httpx.unmarshalling.Unmarshaller

case class Story(number: String, title: String, phase: String) {

  private[this] def validate() = {
    if (number.isEmpty || title.isEmpty)
      throw new ValidationException("Both number and title are required")

    if (stories.where(x => x.number === number).nonEmpty) {
      throw new ValidationException("The story number is not unique")
    }
  }

  def save(): Either[Throwable, String] = {
    tx {
      try {
        validate()
        stories.insert(this)
        Right("Story is created successfully.")
      } catch {
        case exception: Throwable => Left(exception)
      }
    }
  }
}

object Story {
  def apply(number: String, title: String) =
    new Story(number, title, "ready")

  /*implicit val StoryUnmarshaller =
    Unmarshaller[Story](MediaTypes.`application/x-www-form-urlencoded`) {
      case HttpEntity.NonEmpty(contentType, data) =>

        val Array(_, name, first, age) =
          data.asString.split(":,".toCharArray).map(_.trim)

        println(data)

        Story("", "")
    }*/
}

