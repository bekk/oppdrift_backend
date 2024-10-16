const webSocket = require("ws");
const http = require("http");

const port = 8080;

const serveHandler = require("serve-handler");

const staticFiles = { public: "./static/" };

const apiServer = http.createServer((req, res) => {
  if (req.url.startsWith("/api/")) {
    const msg = decodeURIComponent(req.url.substring(5));
    res.write(`Message received: ${msg}`);
    sendToSockets(`From api (${req.headers['x-forwarded-for']}): ${msg}`);
    res.end();
  } else {
    serveHandler(req, res, staticFiles);
  }
});

const socketServer = new webSocket.Server({ server: apiServer });

let sockets = [];

socketServer.on("connection", (socket, req) => {
  sockets.push(socket);

  const ip = req.headers['x-forwarded-for'];

  socket.send(`Welcome to the chat ${ip}.`);

  socket.on("message", async (msg, isBinary) => {
    const txt = isBinary ? msg : msg.toString();
    sendToSockets(`${ip}: ${txt}`);
  });

  socket.on("close", () => (sockets = sockets.filter((s) => s !== socket)));

  socket.onerror = (e) => console.error(`Some error: ${e}`);
});

function sendToSockets(txt) {
  sockets.forEach((s) => s.send(txt));
}

console.info(`Listening to http://localhost:${port}/`);

apiServer.listen(port);
