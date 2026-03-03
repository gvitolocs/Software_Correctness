package plpa.graphics

/**
 * Interpreter for the graphics language. Coursework: all drawing state (bounding box,
 * colours, etc.) must live here in Scala. The program string from the editor is passed
 * directly and must not be modified (e.g. do not split into separate commands before
 * passing to the interpreter).
 */
object GraphicsLogic {

  /**
   * Interpret the program string (exactly as from the editor).
   * Return "ERR\nmsg1\nmsg2\n..." on any error; otherwise return
   * "OK\n" + drawing data (e.g. "BOX x1 y1 x2 y2\n" then LINE/CIRCLE/RECTANGLE/TEXT lines
   * with optional colour). The Java GUI uses this to show errors or to pass data to
   * DrawingCanvas (Java must not hold state).
   * Tip: split into lines, trim, skip empty; first line must be (BOUNDING-BOX (x1 y1) (x2 y2)).
   * Then process (DRAW c g1 g2 ...), (FILL c g), or standalone shapes; default colour black.
   * Coursework: objects must be drawn in the order specified; output your shape list in that order.
   */
  def interpret(program: String): String = {
    // TODO: split into lines, parse each line, collect errors and shapes
    // TODO: if errors non-empty return "ERR\n" + errors.mkString("\n")
    // TODO: else return "OK\n" + "BOX ..." + "\n" + shapes.mkString("\n")
    ???
  }

  /**
   * Parse (BOUNDING-BOX (x1 y1) (x2 y2)). Must be the first command (coursework).
   * Tip: regex like """\(BOUNDING-BOX\s+\(([\d.]+)\s+([\d.]+)\)\s+\(([\d.]+)\s+([\d.]+)\)\)""".r
   * or split on spaces/parens and match.
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
   * Parse (RECTANGLE (x1 y1) (x2 y2)) — bottom-left (x1,y1) to top-right (x2,y2) (coursework).
   * Return e.g. Some((x1, y1, x2, y2)) for the rectangle.
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
   * Parse (DRAW c g1 g2 g3 ...): draw g1, g2, g3 in colour c. c can be a colour name or
   * representation; g1, g2, g3 are nested commands like (LINE ...), (CIRCLE ...), etc.
   * Tip: parse c, then recursively parse each g_i and emit shape lines with colour c.
   * Outside DRAW the default colour is black.
   */
  private def parseDraw(line: String): Option[(String, List[String])] = {
    // TODO: implement (e.g. colour + list of shape data strings)
    ???
  }

  /**
   * Parse (FILL c g): fill object g with colour c.
   * Tip: parse c and g (one shape), then emit a “fill” variant of that shape with colour c.
   */
  private def parseFill(line: String): Option[(String, String)] = {
    // TODO: implement (e.g. colour + shape data string)
    ???
  }
}
