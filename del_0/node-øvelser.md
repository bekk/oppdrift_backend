# En veldig enkel web-server

Vi kan eksperimentere med en enkel web server for å bli kjent med **HTTP**.
Til det bruker vi [Node.js](https://nodejs.org/). Det kan installeres
fra <https://nodejs.org/en/> eller med [Homebrew](https://brew.sh/) på Mac
med `brew install node`.

Åpne et terminalvindu og test om installasjonen er i orden:

```console
node --version
```

## Hello world

I din favoritt teksteditor, lag en ny fil med følgende innhold:

```js
var http = require('http');

http.createServer((request, response) => {
 response.write(`Hello world!`);
 response.end();
}).listen(8080);
```

Lagre den som `server.js`.

Start din nye webserver i konsollen (i samme mappe som du lagret filen) med `node server.js`

Med en nettleser, skriv `http://localhost:8080` i adressefeltet og trykk enter.

## Response header

Vi kan sende med en header til klientene våre. I `server.js`, legg til en linje, så scriptet ser slik ut:

```js
var http = require('http');

http.createServer((request, response) => {
 response.setHeader('Oppdrift', 'Backend');
 response.write(`Hello world!`);
 response.end();
}).listen(8080);
```

Lagre js-filen.
For å oppdatere serveren må den startes på nytt. Trykk `Ctrl+C` og start den på nytt med `node server.js`.

Kall serveren på nytt og se om du finner headeren i responsen (bruk dev tools `F12`).

## HTML Content

For å slippe å starte serveren på nytt manuelt hver gang vi lagrer endringer i js-filen, kan vi starte node i _watch_-modus (fra versjon 18 av Node).
`node  --watch server.js `

For å fortelle klienten at body i meldingen er html, legger vi til en header:

```js
response.setHeader('Content-Type', 'text/html; charset=utf-8');
```

Så kan vi endre linjen der vi skriver til body (husk at dette må skje etter at vi har skrevet ferdig alle headere):

```js
response.write(`<HTML><H1>Hello World!</H1></HTML>`);
```

## Request header

Du kan se headerne i requesten med `console.log(request.rawHeaders);`
Ulike klienter vil sende forskjellige headere.

Kall serveren med ulike klienter:

- I et nytt terminalvindu: `curl http://localhost:8080`. Du kan legge til egne headere med `-H "header-name: header-value"`
- Med en nettleser, skriv `http://localhost:8080` i adressefeltet og trykk enter.
- I [Postman](https://www.postman.com/downloads/), lag en ny tab med samme adressen og klikk «Send». Under «Headers» kan du velge hva du vil sende med.
- I [IntelliJ](https://www.jetbrains.com/idea/) lag en ny http request-fil (*.http) og skriv inn den samme adressen. Du kan legge til headere under adressen med `header-name: header-value`.

## Session

Cookies kan brukes for å implementere en session mellom en server og en nettleser. Om du sender en cookie-header til klienten, vil en nettleser sende den tilbake i neste request.

Du kan sende en cookie med headeren `Set-Cookie`. For eksempel:  `response.setHeader('Set-Cookie', 'Gjende=godt; Max-Age=100000;');`

For å simulere en session, kan vi legge til dette øverst i scriptet:

```js
 const cookieName = 'numberOfRequestInSession';
 const cookieValue = 
  request.headers.cookie &&
  request.headers.cookie.split(';')
   .map(c => c.split('='))
   .filter(c => c[0] = cookieName)
   .map(c => c[1])
   .shift() || 0;
 response.setHeader('set-cookie', `${cookieName}=${parseInt(cookieValue) + 1}`);
```

Prøv deretter å kalle serveren med en nettleser flere ganger. Cookie-verdien ser du både som en request header og i dev-tools i nettleseren.

## Lese fra request

### Path

Vi kan se hvilken url (path) klienten har brukt i `request.url`.
Så vi kan legge til en linje i metoden vår:

```js
const pathAsWords = decodeURIComponent(request.url.replaceAll('/', ' '));
```

og deretter returnere i body:

```js
response.write(`<HTML><H1>Hello ${pathAsWords}!</H1></HTML>`);
```

Kall serveren med for eksempel: `http://localhost:8080/oppdrift/backend`

### Query

_Query parameters_ er den delen av url fra og med `?` til høyre for path. Hvert parameter er separert med `&`. De består av _key_ og _value_ separert med `=`. Samme _key_ kan forekomme flere ganger.

Ved å legge inn `console.log(parseQuery(request.url));` i server-scriptet, kan du inspisere query-parametre du mottar.

Det finnes selvsagt bibliotek for å parse dette, men det kan også gjøres relativt enkelt med litt kode vi kan legge inn i js-filen (vi bruker `decodeURIComponent` av hensyn til [encoding/decoding](https://url.spec.whatwg.org/#string-utf-8-percent-encode) av [spesielle tegn](https://url.spec.whatwg.org/#query-percent-encode-set)):

```js
const parseQuery = function (url) {
    if (!url || !url.includes("?")) return {};
    const query = url.substring(url.lastIndexOf("?") + 1);
    return query
      .split("&")
      .map((q) => q.split("="))
      .reduce((acc, val) => {
        const key = decodeURIComponent(val[0]);
        const value = decodeURIComponent(val[1]);
        if (acc[key]) {
          acc[key].push(value);
        } else {
          acc[key] = [value];
        }
        return acc;
      }, {});
  };
```

Om du kaller serveren med f.eks `http://localhost:8080/?firma=bekk&oppdrift=backend&oppdrift=frontend`

Prøv å få et navn fra query til å returneres i body. Så `http://localhost:8080?name=Brave%20World` returnerer `<HTML>Hello Brave World</HTML>`.

### Body

Om du vil se hva som ligger i request body, kan du gjøre det på denne måten:

```js
const logRequestBody = function(request){
 let body = 'Body: \n\n';
 request.on('data', chunk => body += chunk);
 request.on('end', () => console.log(body));
};
```
