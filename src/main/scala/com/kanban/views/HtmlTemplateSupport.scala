package com.kanban.views

import play.twirl.api._

case class HtmlTemplate(templateFile: String, content: Content) {
  lazy val merged: String = {
    val template = io.Source.fromInputStream(classOf[HtmlTemplate].getClassLoader.getResourceAsStream(templateFile), "utf-8").mkString

    template.replace(HtmlTemplate.ContentPlaceHolder, content.body)
  }
}

object HtmlTemplate {
  val ContentPlaceHolder = "###TWIRL-CONTENT###"
}

trait HtmlTemplateSupport {
  import spray.httpx.marshalling._
  import spray.http.MediaTypes._

  implicit val htmlTemplateMarshaller: Marshaller[HtmlTemplate] =
    Marshaller.delegate[HtmlTemplate, String](`text/html`)(_.merged)
}

object HtmlTemplateSupport extends HtmlTemplateSupport
