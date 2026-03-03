# PLPA Graphics IDE

**IntelliJ IDEA:** Run the configuration **"PLPA Graphics IDE (JavaFX)"** (dropdown next to Run, then Run). Do not use the plain "Main" run—it has no JavaFX module path.

**VS Code / Cursor:** Use the launch config **"PLPA Graphics IDE (JavaFX)"** in Run and Debug.

**Terminal:** `./mvnw javafx:run`

If the Scala compile server shows "Unrecognized option: --sun-misc-unsafe-memory-access=allow", use **File → Invalidate Caches → Invalidate and Restart**, or in Settings → Build → Compiler → Scala Compiler clear any custom JVM options.
