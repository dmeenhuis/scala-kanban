package com.kanban.views.models

import com.kanban.models.Story

case class StoriesViewModel(stories: Traversable[Story]) {
  def storiesByPhase(phase: String) = {
    stories.filter(x => x.phase == phase)
  }
}