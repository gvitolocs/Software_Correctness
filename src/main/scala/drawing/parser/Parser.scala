package drawing.parser

import drawing.model._

object Parser {
  def parseInput(input: String): Either[String, List[Command]] = {
    val lowerCaseInput = input.toLowerCase
    // get arguments and execute the corresponding command
    Left("Invalid command")
  }
  
  def parseColor(color: String): Either[String, Color] = {
    color match {
      case "black" => Right(Black)
      case "red" => Right(Red)
      case "green" => Right(Green)
      case "blue" => Right(Blue)
      case _ => Left("Invalid color")
    }
  }
}
