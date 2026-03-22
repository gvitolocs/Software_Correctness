#!/bin/bash
# Disable the Red Hat Java extension's embedded JRE so Cursor/VS Code uses your system JDK
# (fixes JavaFX NSTrackingRectTag crash on macOS when using the Run button)
# Run once, then restart Cursor. To re-enable: rename jre.disabled back to jre.

set -e
EXT_DIR="${HOME}/.cursor/extensions"
if [ ! -d "$EXT_DIR" ]; then
  EXT_DIR="${HOME}/.vscode/extensions"
fi
for dir in "$EXT_DIR"/redhat.java-*/; do
  [ -d "$dir" ] || continue
  if [ -d "$dir/jre" ]; then
    mv "$dir/jre" "$dir/jre.disabled"
    echo "Disabled embedded JRE in $dir"
    echo "Restart Cursor (or VS Code) so the runner uses your system JDK."
  elif [ -d "$dir/jre.disabled" ]; then
    echo "Embedded JRE already disabled in $dir"
  fi
done
