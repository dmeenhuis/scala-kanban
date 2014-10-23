package com.kanban.models

import org.squeryl._

object KanbanSchema extends Schema {
  val stories = table[Story]("STORIES")
}
