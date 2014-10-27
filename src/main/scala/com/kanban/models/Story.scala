package com.kanban.models

import org.squeryl.PrimitiveTypeMode._
import KanbanSchema._

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
}

