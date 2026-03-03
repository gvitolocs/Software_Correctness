package drawing.engine

import drawing.model._

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
        state.copy(drawables = state.drawables ++ pixels)

      case Circle(x, y, radius) =>
        var pixels = CircleAlgorithm.midpoint(x, y, radius, state.currentColor)
        state.copy(drawables = state.drawables ++ pixels)

      case TextAt(x, y, text) =>
        val drawable = Text(x, y, text, state.currentColor)
        state.copy(drawables = state.drawables :+ drawable)

      case Draw(color, items) =>
        state.copy(currentColor = color)
        val finalState = items.foldLeft(state) {
          (acc, item) => evaluateCommand(item, acc)
        }
        finalState.copy(currentColor = Black)

      case Fill(color, item) =>
        state.copy(currentColor = color)
        val finalState = evaluateCommand(item, state)
        finalState.copy(currentColor = Black)
    }
  }

  private def clipDrawables(drawables: List[Drawable], box: BoundingBox): List[Drawable] = {
    drawables.filter {
      case Text(x, y, _, _) =>
        x >= box.x1 && x <= box.x2 &&
        y >= box.y1 && y <= box.y2
      case Pixel(x, y, _) =>
        x >= box.x1 && x <= box.x2 &&
        y >= box.y1 && y <= box.y2
    }
  }
}
