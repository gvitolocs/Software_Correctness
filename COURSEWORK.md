# Coursework checklist (from Coursework.pdf)

## Commands to support
- `(BOUNDING-BOX (x1 y1) (x2 y2))` — must be first; drawing outside is clipped.
- `(LINE (x1 y1) (x2 y2))` — use **Bresenham’s line algorithm**.
- `(RECTANGLE (x1 y1) (x2 y2))` — bottom-left to top-right.
- `(CIRCLE (x1 y1) r)` — use **Midpoint circle algorithm**.
- `(TEXT-AT (x1 y1) t)` — use built-in text rendering.
- `(DRAW c g1 g2 g3 ...)` — draw g1, g2, g3 in colour c; default outside is black.
- `(FILL c g)` — fill object g with colour c.

## Rules
- **Scala:** all drawing state (bounding box, colours, etc.); interpret program string as-is (do not modify before passing).
- **Java:** GUI only; no drawing state; display what Scala sends; grid on the left; optionally highlight current object.
- Draw in **order**; coordinates Euclidean, (0,0) bottom-left.

## Where to implement
- **GraphicsLogic.scala** — parsing, errors, output format for Java (see comments + tips in the file).
- **DrawingCanvas.java** — grid, coordinate conversion, Bresenham/Midpoint/text, clipping (see comments + tips in the file).

## Demo
- One bar chart and one pie chart with labels; pie chart: 5 segments.
