package drawing.engine

import drawing.model._

case class EngineState (
  boundingBox: Option[BoundingBox] = None,
  currentColor: Color = Black,
  drawables: List[Drawable] = List()
)
