var http = require("http");
const server = function (request, response) {
    response.setHeader("Content-Type", "text/html");
    response.write("<html>Oppdrift</html>");
    response.end();
};
console.info("Listening to http://localhost:8080");
http.createServer(server).listen(8080);
