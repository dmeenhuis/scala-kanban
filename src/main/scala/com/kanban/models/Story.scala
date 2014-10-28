package com.kanban.models

import org.squeryl.PrimitiveTypeMode._
import KanbanSchema._

case class Story(number: String, title: String, phase: String) {

  private def phaseLimits = Map("ready" -> Some(3), "dev" -> Some(2), "test" -> Some(2), "deploy" -> None)

  private[this] def validate() = {
    if (number.isEmpty || title.isEmpty)
      throw new ValidationException("Both number and title are required")

    if (stories.where(x => x.number === number).nonEmpty) {
      throw new ValidationException("The story number is not unique")
    }
  }

  private[this] def validateLimit(phase: String) = {
    val currentSize: Long = from(stories)(s => where(s.phase === phase) compute count)

    if(currentSize == phaseLimits(phase).getOrElse(-1)) {
      throw new ValidationException("You cannot exceed the limit set for the phase.")
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

  def moveTo(phase: String): Either[Throwable, String] = {
    tx {
      try {
        validateLimit(phase)

        update(stories)(s =>
          where(s.number === this.number)
          set(s.phase := phase)
        )

        Right(s"Card ${this.number} is moved to $phase phase successfully.")
      } catch {
        case exception: Throwable => Left(exception)
      }
    }
  }

}

object Story {
  def apply(number: String, title: String) =
    new Story(number, title, "ready")

  def findAllStoriesByPhase(phase: String) : Traversable[Story] = {
    tx { from(stories)(x => where(x.phase === phase) select x) map(x => x) }.toList
  }

  def findByNumber(number: String) = tx {
    stories.where(s => s.number === number).single
  }

  def all() : Traversable[Story] = {
    tx {
      from(stories)(x => select(x)) map (x => x)
    }.toList
  }
}

