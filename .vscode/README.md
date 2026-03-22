# Run PLPA Graphics IDE (Cursor / VS Code)

## Run button ▶ with Java runner

1. **Restart Cursor** (or VS Code) so it uses your system JDK instead of the embedded JRE.
2. Open **Run and Debug** (Ctrl+Shift+D / Cmd+Shift+D).
3. Select **"PLPA Graphics IDE (JavaFX)"**.
4. Press **Run** (▶) or **F5**.

The embedded JRE in the Red Hat Java extension has been disabled on this machine, and the config points to your JDK at `/Library/Java/JavaVirtualMachines/temurin-25.jdk/Contents/Home`, so the runner should start without the macOS JavaFX crash.

## If the runner still crashes

- Use **"PLPA Graphics IDE (Run with Maven)"** (runs `./mvnw javafx:run`), or  
- **Terminal** → **Run Task…** → **Run PLPA Graphics IDE**, or  
- From a terminal: `./mvnw javafx:run`

## Re-running the fix on another machine

To disable the embedded JRE so the Run button uses the system JDK:

```bash
./.vscode/disable-embedded-jre.sh
```

Then restart the editor. The script supports both Cursor (`~/.cursor/extensions`) and VS Code (`~/.vscode/extensions`).
