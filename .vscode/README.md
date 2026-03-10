# Run PLPA Graphics IDE (Cursor / VS Code)

## Using the Run button ▶ (recommended)

1. Open **Run and Debug** (Ctrl+Shift+D / Cmd+Shift+D).
2. In the dropdown, select **"PLPA Graphics IDE (Run with Maven)"**.
3. Press **Run** (▶) or **F5**.

This configuration runs `./mvnw javafx:run` for you, so the app starts the same way as in the terminal and does not crash on macOS.

## Other ways to run

- **Terminal** → **Run Task…** → **Run PLPA Graphics IDE**
- From a terminal: `./mvnw javafx:run`

## Java runner (alternative)

The **"PLPA Graphics IDE (JavaFX)"** config uses the Java extension runner. It can crash on macOS (NSTrackingRectTag). If you want to try it:

1. Set `JAVA_HOME` to your JDK 17 (e.g. on macOS: `export JAVA_HOME=$(/usr/libexec/java_home -v 17)`).
2. Select **"PLPA Graphics IDE (JavaFX)"** and press Run.

The same setup applies to both **Cursor** and **VS Code**.
