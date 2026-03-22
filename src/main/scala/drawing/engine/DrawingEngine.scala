package drawing.engine

import drawing.model._
import drawing.algorithms._

class DrawingEngine {
  def execute(commands: List[Command]): Either[String, List[Drawable]] = {
    val finalState = commands.foldLeft(EngineState()) {
      (acc, command) => evaluateCommand(command, acc)
    }
    val clipped = finalState.boundingBox match {
      case Some(box) => clipDrawables(finalState.drawables, box)
      case None => finalState.drawables
    }
    Right(clipped)
  }

  def executeWithSteps(commands: List[Command]): Either[String, List[RenderStep]]= {
    var state = EngineState()
    val steps = scala.collection.mutable.ListBuffer[RenderStep]()

    commands.foreach { command =>
      val before =state.drawables
      state = evaluateCommand (command, state)

      val after= state.drawables
      val newDrawables = after.drop(before.length)

      val clippedAll = state.boundingBox match{
        case Some(box) => clipDrawables(after, box)
        case None      => after
      }

      val clippedHighlighted = state.boundingBox match{
        case Some(box) => clipDrawables(newDrawables, box)
        case None      => newDrawables
      }

      steps += RenderStep(command, clippedAll, clippedHighlighted)
    }
    Right(steps.toList)
  }


  private def evaluateCommand(command: Command, state: EngineState): EngineState = {
    command match {
      case BoundingBox(x1, y1, x2, y2) =>
        state.copy(boundingBox = Some(BoundingBox(x1, y1, x2, y2)))
        
      case Line(x1, y1, x2, y2) =>
        val pixels = LineAlgorithm.bresenham(x1, y1, x2, y2, state.currentColor)
        state.copy(drawables = state.drawables ++ pixels)

      case Rectangle(x1, y1, x2, y2) =>
        val pixels =
          LineAlgorithm.bresenham(x1, y1, x2, y1, state.currentColor) ++
          LineAlgorithm.bresenham(x2, y1, x2, y2, state.currentColor) ++
          LineAlgorithm.bresenham(x2, y2, x1, y2, state.currentColor) ++
          LineAlgorithm.bresenham(x1, y2, x1, y1, state.currentColor)

        state.copy(drawables = state.drawables ++ pixels.distinct)

      case Circle(x, y, radius) =>
        val pixels = CircleAlgorithm.midpoint(x, y, radius, state.currentColor)
        state.copy(drawables = state.drawables ++ pixels)

      case TextAt(x, y, text) =>
        val drawable = Text(x, y, text, state.currentColor)
        state.copy(drawables = state.drawables :+ drawable)

      case Draw(color, items) =>
        val previousColor = state.currentColor
        val stateWithColor = state.copy(currentColor = color)
        val finalState = items.foldLeft(stateWithColor) {
          (acc, item) => evaluateCommand(item, acc)
        }
        finalState.copy(currentColor = previousColor)

      case Fill(color, item) =>
        val previousColor = state.currentColor
        val filled = fillShape(item, color)
        state.copy(
          drawables = state.drawables ++ filled,
          currentColor = previousColor
        )
    }
  }

  private def fillShape(cmd: Command, color: Color): List[Pixel] = cmd match {
    case Rectangle(x1, y1, x2, y2) => fillRectangle(x1, y1, x2, y2, color)
    case Circle(cx, cy, r) => fillCircle(cx, cy, r, color)
    case Draw(c, items) => items.flatMap(item => fillShape(item, color))
    case _ => List()
  }

  private def fillRectangle(x1: Int, y1: Int, x2: Int, y2: Int, color:Color): List[Pixel]={
    val minX = Math.min(x1,x2)
    val maxX = Math.max(x1,x2)
    val minY = Math.min(y1,y2)
    val maxY = Math.max(y1,y2)

    (for {
      x <- minX to maxX
      y<- minY to maxY
    } yield Pixel (x,y, color)).toList
  }

  private def fillCircle(cx: Int, cy: Int, r: Int, color:Color): List[Pixel] = {
    (for {
      x <- (cx - r) to (cx + r)
      y <- (cy - r) to (cy + r)
      if (x - cx) * (x - cx) + (y - cy) * (y - cy) <= r * r
    } yield Pixel(x, y, color)).toList
  }

  private def clipDrawables(drawables: List[Drawable], box: BoundingBox): List[Drawable] = {
    drawables.filter {
      case Text(x, y, _, _) =>
        x >= box.x1 && x <= box.x2 && y >= box.y1 && y <= box.y2
      case Pixel(x, y, _) =>
        x >= box.x1 && x <= box.x2 && y >= box.y1 && y <= box.y2
    }
  }
}
