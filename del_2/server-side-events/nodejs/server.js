const serveHandler = require("serve-handler");
const http = require("http");

const staticFiles = { public: "./static/" };

const apiServer = http.createServer((request, response) => {
  if (request.url.startsWith("/api/eventSource")) {
    const newClient = {
      id: Date.now(),
      response
    };
    clients.push(newClient);
    request.on("close", () => {
      console.log(`${newClient.id} connection closed`);
      clients = clients.filter(c => c.id !== newClient.id);
    });
    response.setHeader("Content-Type", "text/event-stream");
    response.setHeader("Connection", "keep-alive");
    response.setHeader("Cache-Control", "no-cache");
    response.write(`data: Welcome!\n\n`);
  } else if(request.url.startsWith("/api/newMessage")) {
      let body = `${request.headers["x-forwarded-for"]}: `;
      request.on('data', chunk => body += chunk);
      request.on('end', () => {
        clients.forEach(client => {
          client.response.write(`data: ${body}\n\n`)
        })
      });
    }
  else
  {
    serveHandler(request, response, staticFiles);
  }
});

let clients = [];

console.info("Listening to http://localhost:8080/");

apiServer.listen(8080);
