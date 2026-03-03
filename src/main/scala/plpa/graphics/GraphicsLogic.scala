package plpa.graphics

/**
 * Interpreter for the graphics language. All drawing state (bounding box, colours,
 * list of shapes) must live here in Scala. The program text from the editor is passed
 * as-is; do not change it before calling interpret.
 */
object GraphicsLogic {

  /**
   * Interpret the whole program string. If there are errors, return "ERR\n" followed by
   * one error message per line. Otherwise return "OK\n" followed by the drawing data:
   * first line "BOX x1 y1 x2 y2", then one line per shape (LINE, CIRCLE, RECTANGLE, TEXT, etc.)
   * with optional colour. The Java GUI shows errors in the error box and passes the part
   * after "OK\n" to the canvas. The first line of the program must be (BOUNDING-BOX (x1 y1) (x2 y2)).
   * Then you have (DRAW c g1 g2 ...), (FILL c g), or single shapes. Default colour is black.
   * Output shapes in the same order they appear in the program.
   */
  def interpret(program: String): String = {
    // TODO: split into lines, parse each line, collect errors and shapes
    // TODO: if errors non-empty return "ERR\n" + errors.mkString("\n")
    // TODO: else return "OK\n" + "BOX ..." + "\n" + shapes.mkString("\n")
    ???
  }

  /**
   * Parse a line like (BOUNDING-BOX (x1 y1) (x2 y2)). Return the four numbers.
   * This must be the first command. You can use a regex, or split by spaces and
   * parentheses and then pattern match.
   */
  private def parseBoundingBox(line: String): Option[(Double, Double, Double, Double)] = {
    // TODO: implement
    ???
  }

  /**
   * Parse (CIRCLE (x y) r). Tip: same style as above with regex or parser.
   */
  private def parseCircle(line: String): Option[(Double, Double, Double)] = {
    // TODO: implement
    ???
  }

  /**
   * Parse (LINE (x1 y1) (x2 y2)).
   */
  private def parseLine(line: String): Option[(Double, Double, Double, Double)] = {
    // TODO: implement
    ???
  }

  /**
   * Parse (RECTANGLE (x1 y1) (x2 y2)). (x1,y1) is bottom-left, (x2,y2) is top-right.
   * Return the four numbers.
   */
  private def parseRectangle(line: String): Option[(Double, Double, Double, Double)] = {
    // TODO: implement
    ???
  }

  /**
   * Parse (TEXT-AT (x y) t) where t is a quoted string or a single word.
   * Tip: two regexes: one for "([^"]*)" and one for (\S+).
   */
  private def parseTextAt(line: String): Option[(Double, Double, String)] = {
    // TODO: implement
    ???
  }

  /**
   * Parse (DRAW c g1 g2 ...): draw shapes g1, g2, ... in colour c. c is a colour name or
   * value; g1, g2 are nested commands like (LINE ...), (CIRCLE ...). Parse the colour,
   * then parse each shape and output a shape line with that colour attached.
   */
  private def parseDraw(line: String): Option[(String, List[String])] = {
    // TODO: implement (e.g. colour + list of shape data strings)
    ???
  }

  /**
   * Parse (FILL c g): fill one shape g with colour c.
   * Parse the colour and the shape, then output a filled version of that shape with colour c.
   */
  private def parseFill(line: String): Option[(String, String)] = {
    // TODO: implement (e.g. colour + shape data string)
    ???
  }
}
