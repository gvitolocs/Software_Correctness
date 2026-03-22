package drawing.parser

import drawing.model._

/**
 * Parser for the graphics language (s-expressions).
 * Produces a list of Command for DrawingEngine. First command must be BOUNDING-BOX.
 */
object Parser {

  def parseInput(input: String): Either[String, List[Command]] = {
    val tokens = tokenize(input)
    parseCommands(tokens)
  }

  /** Public for use from GraphicsLogic; supports black, red, green, blue. */
  def parseColor(color: String): Either[String, Color] = {
    color.toLowerCase match {
      case "black"  => Right(Black)
      case "red"    => Right(Red)
      case "green"  => Right(Green)
      case "blue"   => Right(Blue)
      case _        => Left("Invalid color")
    }
  }

  /** Split input into tokens: parentheses and symbols separated by whitespace. */
  private def tokenize(input: String): List[String] =
    input
      .replace("(", " ( ")
      .replace(")", " ) ")
      .trim
      .split("\\s+")
      .toList
      .filter(_.nonEmpty)

  /** Parse all commands from the token list; order is preserved. */
  private def parseCommands(tokens: List[String]): Either[String, List[Command]] = {
    def loop(ts: List[String], acc: List[Command]): Either[String, List[Command]] =
      ts match {
        case Nil => Right(acc.reverse)
        case _   =>
          parseCommand(ts).flatMap { case (cmd, rest) =>
            loop(rest, cmd :: acc)
          }
      }
    loop(tokens, Nil)
  }

  /** Parse a single command and return it plus the remaining tokens. */
  private def parseCommand(tokens: List[String]): Either[String, (Command, List[String])] =
    tokens match {

      case "(" :: "BOUNDING-BOX" :: rest =>
        for {
          p1 <- parsePoint(rest)
          (x1, y1, r1) = p1
          p2 <- parsePoint(r1)
          (x2, y2, r2) = p2
          tail <- expect(")", r2)
        } yield (BoundingBox(x1, y1, x2, y2), tail)

      case "(" :: "LINE" :: rest =>
        for {
          p1 <- parsePoint(rest)
          (x1, y1, r1) = p1
          p2 <- parsePoint(r1)
          (x2, y2, r2) = p2
          tail <- expect(")", r2)
        } yield (Line(x1, y1, x2, y2), tail)

      case "(" :: "RECTANGLE" :: rest =>
        for {
          p1 <- parsePoint(rest)
          (x1, y1, r1) = p1
          p2 <- parsePoint(r1)
          (x2, y2, r2) = p2
          tail <- expect(")", r2)
        } yield (Rectangle(x1, y1, x2, y2), tail)

      case "(" :: "CIRCLE" :: "(" :: x :: y :: ")" :: r :: ")" :: tail =>
        Right((Circle(x.toInt, y.toInt, r.toInt), tail))

      case "(" :: "TEXT-AT" :: "(" :: x :: y :: ")" :: rest =>
        val (textTokens, afterText) = rest.span(_ != ")")
        val text = textTokens.mkString(" ").stripPrefix("\"").stripSuffix("\"")
        afterText match {
          case ")" :: tail => Right((TextAt(x.toInt, y.toInt, text), tail))
          case _           => Left("Invalid TEXT-AT format")
        }

      case "(" :: "DRAW" :: color :: rest =>
        for {
          parsedColor <- parseColor(color)
          nested      <- parseNestedCommands(rest)
          (items, after) = nested
          tail <- expect(")", after)
        } yield (Draw(parsedColor, items), tail)

      case "(" :: "FILL" :: color :: rest =>
        for {
          parsedColor <- parseColor(color)
          parsedCmd   <- parseCommand(rest)
          (cmd, r1)   = parsedCmd
          tail <- expect(")", r1)
        } yield (Fill(parsedColor, cmd), tail)

      case _ =>
        Left(s"Unknown command starting at: ${tokens.take(5).mkString(" ")}")
    }

  /** Parse a point (x y) and return (x, y, remaining tokens). */
  private def parsePoint(tokens: List[String]): Either[String, (Int, Int, List[String])] =
    tokens match {
      case "(" :: x :: y :: ")" :: rest => Right((x.toInt, y.toInt, rest))
      case _                             => Left("Invalid point format")
    }

  /** Require the next token to be the given one; return the rest or an error. */
  private def expect(token: String, tokens: List[String]): Either[String, List[String]] =
    tokens match {
      case t :: rest if t == token => Right(rest)
      case _                       => Left(s"Expected '$token'")
    }

  /** Parse nested commands until closing ")" (e.g. inside DRAW). */
  private def parseNestedCommands(tokens: List[String]): Either[String, (List[Command], List[String])] = {
    def loop(ts: List[String], acc: List[Command]): Either[String, (List[Command], List[String])] =
      ts match {
        case ")" :: _  => Right((acc.reverse, ts))
        case Nil       => Left("Unexpected EOF inside DRAW or FILL")
        case _         =>
          parseCommand(ts).flatMap { case (cmd, rest) =>
            loop(rest, cmd :: acc)
          }
      }
    loop(tokens, Nil)
  }
}
