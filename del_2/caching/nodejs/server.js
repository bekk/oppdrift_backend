const http = require("http");
const maxAge = 1000;
const port = 8080;
const server = (request, response) => {
    console.info(`Request: ${request.method} ${request.url}`)
    response.setHeader("Content-Type", "text/html");
    response.setHeader("Cache-Control", `public,max-age=${maxAge}`)
    response.write("<html>");
    response.write("<h1>Test av cache.</h1>");
    response.write(`<p>Servertid: <time>${new Date().toLocaleString('no')}</time>.</p>`);
    response.write(`<p>Klienttid: <time><script>document.write(new Date().toLocaleString('no'));</script></time></p>`);
    response.write(`<a href='/'>self</a>.`);
    response.write("</html>");
    response.end()
};
console.info(`Listening to http://localhost:${port}`);
http.createServer(server).listen(port);
