const { spawn } = require('child_process');
const path = require('path');

const cwd = path.resolve(__dirname, '..');
const isWin = process.platform === 'win32';
const mvnw = path.join(cwd, isWin ? 'mvnw.cmd' : 'mvnw');

const child = spawn(mvnw, ['javafx:run'], {
  cwd,
  stdio: 'inherit',
  shell: isWin,
});

child.on('close', (code) => process.exit(code ?? 0));
