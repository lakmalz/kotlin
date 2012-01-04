package std.template.html

import std.*
import std.template.*
import std.template.io.*
import std.io.*
import std.util.*
import std.test.*
import java.util.*

val justBody = body {
  +"Hello world"
}

fun result(args : List<String>) =
html {
  head {
    title {+"XML encoding with Kotlin"}
  }
  body {
    h1 {+"XML encoding with Kotlin"}
    p {+"this format can be used as an alternative markup to XML"}

    // an element with attributes and text content
    a(href = "http://jetbrains.com/kotlin") {+"Kotlin"}

    // mixed content
    p {
      +"This is some"
      b {+"mixed"}
      +"text. For more see the"
      a(href = "http://jetbrains.com/kotlin") {+"Kotlin"}
      +"project"
    }
    p {+"some text"}

    // content generated by
    p {
      for (arg in args)
      +arg
    }
  }
}

/** Create a bad element which doesn't have a class object create() method */
class BadElem() : BodyTag("bad") {
}

/** Creates a bad body to test out badly defined elements */
class BadBody() : BodyTag("badBody") {
  fun bad(init : BadElem.()-> Unit) = initTag(init)
}

fun badBody(init: BadBody.()-> Unit): BadBody {
  val elem = BadBody()
  elem.init()
  return elem
}

class TemplateHtmlTest() : TestSupport() {

  fun testJustBody() {
    assertEquals("<body>Hello world<body>", justBody.toString())
  }

  fun testEmbeddedSimpleBody() {
    val e = body {
      +"body with text"
    }
    assertEquals("<body>body with text<body>", e.toString())
  }

  fun testEmbeddedBodyWithNestedBold() {
    val e = body {
      b{
        +"this is bold"
      }
    }
    assertEquals("<body><b>this is bold<b><body>", e.toString())
  }

  fun testEmbeddedBodyWithNestedLinks() {
    val e = body {
      a("http://jetbrains.com/kotlin") {
        +"link text"
      }
    }
    assertEquals("<body><a href=\"http://jetbrains.com/kotlin\">link text<a><body>", e.toString())
  }

  fun testHtmlFunction() {
    val e = result(arrayList("a", "b", "c"))
    assertEquals("""<html><head><title>XML encoding with Kotlin<title><head><body><h1>XML encoding with Kotlin<h1><p>this format can be used as an alternative markup to XML<p><a href="http://jetbrains.com/kotlin">Kotlin<a><b>mixed<b><a href="http://jetbrains.com/kotlin">Kotlin<a><p>This is sometext. For more see theproject<p><p>some text<p><p>abc<p><body><html>""", e.toString())
  }

  fun testEmbeddedFunction() {
    val e = html {
      head {
        title {+"XML encoding with Kotlin"}
      }
      body {
        a("http://jetbrains.com/kotlin")
      }
    }
    assertEquals("<html><head><title>XML encoding with Kotlin<title><head><body><a href=\"http://jetbrains.com/kotlin\"/><body><html>", e.toString())
  }

  fun testBadlyDefinedElement() {
    failsWith<UnsupportedOperationException>{
      val e = badBody {
        bad{
          +"Bad Element Text"
        }
      }
      println("bad body: $e")
    }
  }
}