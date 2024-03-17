# Program for Oppdrift Backend tredje økt

Innholdet i denne økten består i hovedak av to deler; API-design og testing.

[Presentasjon](https://github.com/bekk/oppdrift_backend/blob/main/del_2/Kurspresentasjon_2.pptx)

## API-design

API (_Application Programming Interface_) er utvilsomt et sentralt begrep for
backend-programmering. Vi bruker gjerne også «API» i sammenhenger der vi mener 
grensesnitt eller kontaktflate. Også klasser eller moduler har et API; de delene
som er eksponert ut.

Et API kan gjerne ses på som en kontrakt mellom tilbyder av grensesnittet og
konsumentene av det.

Hvordan vi designer et API er gjerne avhengig av om
- det er eksponert ut på nettet eller ikke
- om du kjenner alle konsumentene
- om det kun er beregnet for din egen front-end.


### BFF

_Backend for frontend_ er et pattern der man har et dedikert API for hver enkelt
type front-end. For eksmepel kan det være et eget API for mobile klienter.
Da kan man ha spesifikke endepunkt og schema for det.

Om klienten er din egen front-end er det ikke uvanlig å generere klient-kode 
fra API-et.

- [Backend for frontend](https://learn.microsoft.com/en-us/azure/architecture/patterns/backends-for-frontends)

### Dokumentasjon

En utfordring (særlig når du ikke kjenner konsumentene dine) er at det er et behov
for dokumentasjon av grensesnittet på et vis. Det er også et behov for å håndtere
endringer i kontrakten. Som utviklere er vi ofte ikke så glad i å skrive dokumentasjon
utenfor koden, da det er problematisk å holde den ved like når koden endrer seg,
og det er vanskelig å vite hvor mye dokumentasjon som er nyttig og tilstrekkelig.

### Json:api

Dette er et forsøk på å forenkle bruken av et API ved å standardisere formatet 
på json-meldingene i API-et, og parametre som API-et tar inn.

Det har et spesifikt format for 
- feilmeldinger
- koblinger mellom ulike ressurser på serveren
- paginering
- filtrering
- sortering

Det er vanskelig å finne eksempler der formatet er fulgt 100%, men det har 
allikevel nytte som en inspirasjon.

- [Dokumentasjon](https://jsonapi.org/)
- [Eksempel på et bibliotek](https://github.com/MarkoMilos/jsonapi)
- [Eksempel på bruk](https://occapi.uni-foundation.eu/occapi/v1/)
- [Eksempel på delvis bruk](https://api.tvmaze.com/shows/431)
- [HAL (en elternativ standard)](https://stateless.group/hal_specification.html)

### OpenAPI

Også (eller egentlig tidligere) kjent som Swagger.

Dette er et standardisert format for å beskrive et HTTP-basert API;
endepunkter, input-parameter, returverdier, forventede result-koder og annen beskrivelse.
Beskrivelsen er i Json eller Yaml, men den vises som regel i Swagger, som er en tjeneste
for å lese, bla i og teste API-et.

Den vanligste tilnærmingen er at dokumentasjonen genereres av annotasjoner på endepunktene
inline dokumentasjon (Jdoc eller tilsvarende) og vises med Swagger-middleware.

- [Wikipedia](https://en.wikipedia.org/wiki/OpenAPI_Specification)
- [😻](https://catfact.ninja)
- [Eksempel fra SVV](https://nvdbrapportapi.atlas.vegvesen.no/swagger-ui/)
- [Barnehagefakta](987117575)
- [Eksempel på bruk i Kotlin](https://www.baeldung.com/kotlin/swagger-spring-rest-api)

### Endringshåndtering

Når du gjør endringer i API-et som bryter kontrakten (breaking change), må dette
kunne kommuniseres til konsumentene på et vis.

Slike endringer kan være 

- endringer i formatet på innholdet
- endring i typen
- fjerning av deler av et API.

Måter å versjonere kan være

- URI-versjonering: at deler av path inneholder et versjonsnummer.
- Custom Request Header: At klientene ber om en spesifikk versjon med en custom header.
- Accept header: At klienene bruker content negotiation for å spesifisere hvilken versjon de vil ha

- [How to](https://restfulapi.net/versioning/)

### Pact

Pact er et forsøk på å invertere avhengighetsforholdet mellom konsument og tilbyder.
Det kalles «consumer driven contract».

Tilbyder genererer en beskrivelse av sitt API som sendes til en _broker_, en 
server som er tilgjengelig for konsumentene.

Konsumentene lager integrasjonstester som beskriver sin bruk av API-et; sitt behov
og sine forventinger. Disse kjører mot broker, som simulerer API-et. De kan senere
spille av disse mot det egentlige API-et for å verifisere at de gir forventet svar.

Når konsumenten gjør en endring, kjøres alle disse testene mot det nye API-et.
Om ingen av dem klager, betyr det at API-et i praksis ikke er endret. Ingenting som
er i bruk er brukket. Da trengs heller ikke et nytt versjonsnummer.

- [Pact](https://pact.io/)
- [Pactflow](https://pactflow.io/)

### Caching

Caching er en teknikk som brukes for å forbedre ytelsen ved aksessering av data.
Når tilgangen kan være kostbar (for eksempel pga kompliserte spørringer eller nettverkskall)
og skal aksesseres ofte, er det hensiktsmessig å mellomlagre dataene.

Et slikt mellomlager kalles en cache.

Med en cache vil framtidig aksess av dataene gå vesentlig raskere. Men det er svært
krevende å få caching til korrekt, og det kan være en kilde til mye gufne feil.

Caching kan skje i forkant av første aksessering, eller lazy; nå første konsument gjør at
det hentes. Den siste strategien kalles **read-through**.

Dersom konsumentene også skal gjøre endring på verdien, kan skriving skje til cachen. Dette kalles
**write-through**.

Alternativt kan skriving til datakilden føre til at cachen invalideres. Da kan den
**flushes**.

Om du bare har én instans av serveren, kan cachen holdes i minnet. Dette er enklest og raskest.
Dersom det finnes flere instanser, trengs det antagelig en **distribuert cache**.
Man kan også bruke en **CDN** (Content Delivery Network) for å cache statiske filer. Det er
servere som er spesiallaget for nettopp det formålet.

Man må også ta stilling til hvor lenge cachen skal leve for å unngå at den blir 
utdatert (**stale**). En kort levetid kan ha stor effekt i et system med mye trafikk.

HTTP har caching innebygget. Det styres i hovedsak med headeren `Cache-Control`.
En cache kan være `public` (felles for alle besøkende) eller `private` (alle har sin egen
[i nettleseren]). Selv om ikke en header er gitt, kan resultatet caches allikevel (f.eks om
det er lenge siden det ble endret). Dette kalles **heuristic cahing**.
`Vary`-headeren kan bestemme hva som skal brukes som nøkkel i cahen.
`Etag`-headeren kan sendes fra server som en slags versjonering. Klienten kan spørre 
om ressursen er endret med `If-None-Match`.

For å ikke cache resultatet kan server sende med `Cache-Control: no-cache` eller
`Cache-Control: no-store`.

Et vanlig triks for å overstyre cachen er å legge med et request-parameter som endres 
med nye versjoner. Dette kalles **Cache busting**.

- [Eksempel](./caching/nodejs/server.js)
- [MDN](https://developer.mozilla.org/en-US/docs/Web/HTTP/Caching)
- [RFC9111](https://httpwg.org/specs/rfc9111.html)
- [Caching tutorial](https://www.mnot.net/cache_docs/)
- [Caching strategies](https://codeahoy.com/2017/08/11/caching-strategies-and-how-to-choose-the-right-one/)
- [Et eksempel på at caching skaper økt kompleksitet, og at også det kan gå galt](https://www.kode24.no/artikkel/sann-fiksa-vg-utviklerne-valgtrobbelet-det-skar-seg-sa-voldsomt/80202419)

## Protokoller

Det finnes flere protokoller (og konvensjoner) man kan vurdere, avhengig av bruksområdet.

### SOAP

_Simple Object Access Protocol_ er en protokoll som ikke er så mye i bruk i moderne
løsninger. Den er basert på XML, og definerer i detalj kontrakten mellom konsument
og tilbyder. Både endepunkter og schema for parametre og resultat, samt feilhåndtering (faults).

SOAP-API har ofte et discovery-endepunkt som kan gi en beskrivelse av endepunktet
i WSDL-format (Web Services Description Language). Det finnes mange verktøy som kan
lese dette og generere all kode du trenger for å definere en klient (inkludert modeller).

- [Wikipedia: SOAP](https://en.wikipedia.org/wiki/SOAP)
- [Wikpedia: WSDL](https://en.wikipedia.org/wiki/Web_Services_Description_Language)
- [Eksempel](https://apps.learnwebservices.com/services/hello?WSDL)
- [SoapUI er det beste verktøyet for å jobbe med SOAP](https://www.soapui.org/)

### gRPC

gRPC er en protokoll som er utviklet av Google. Den bruker et binært format i transporten
mellom server og klient. Det er basert på HTTP/2, der den utnytter frames til å sende «trailing headers».

Det binære formatet kalles **Protocol Buffers**. Modellen defineres i såkalte proto-filer, og 
klient/server-kode kan genereres fra disse.

Det kan ikke brukes direkte i nettlesere, siden fetch-apiene ikke har implementert funksjonaliteten
fra HTTP/2 som brukes. Det er laget en workaround (gRPC Web) som baker inn trailing headers i 
body på meldingen. Det er avhengig av at serveren forstår dette, eller at det er satt opp en
proxy.

- [Introduksjon](https://grpc.io/docs/what-is-grpc/introduction/)
- [Wikipedia](https://en.wikipedia.org/wiki/GRPC)
- [gRPC web](https://grpc.io/docs/platforms/web/)

### Push- og duplex-protokoller

Ordinær HTTP-protokoll innebærer typisk at klienten tar initiativ til kommunikasjonen,
og at server svarer på henvendelsen. Dette kalles gjerne **pull**- eller **polling**-kommunikasjon.

Dersom klienten trenger å ha oppdatert informasjon til en hver tid (for eksempel i et 
status-dashboard eller lignende) må den spørre serveren hyppig om endringer.
Dette kan være problematisk for ytelsen om det er mange brukere av systemet som
gjør disse.

En annen tilnærming er å la serveren gi beskjed om det har skjedd endringer i dataene,
og sende disse til de klientene som har ytret interesse for dem.

- [Wikipedia](https://en.wikipedia.org/wiki/Push_technology)
- [ngrok](https://ngrok.com/)

### Web hook

Dette er mest aktuelt i en server-til-server-kontekst. Når noe skjer kalles et 
på forhånd definert endepunkt.

### Long polling

Dette utnytter en vanlig http-request, der serveren holder på den i stedet for å svare.
Når noe skjer sendes responsen tilbake.

Dette krever ikke at klienten kjenner andre protokoller, men kan være belastende for serveren
når det er mye trafikk. Noen klient-rammeverk bruker dette som en fallback-strategi om klienten 
ikke kan bruke noe mer moderne.

### Push-API/web-push

Dette er et klient-api, der en background-worker kan ta i mot meldinger fra serveren.

Da kan også klienten motta meldinger selv om ikke websiden er aktiv eller åpnet. Bruker
må akseptere å motta slike varsler for at det skal fungere.

- [Eksempel](./push-api)
- [MDN: Push API](https://developer.mozilla.org/en-US/docs/Web/API/Push_API)
- [W3C Working draft: Push API](https://www.w3.org/TR/push-api/)
- [Se alle service-workers i Chrome](chrome://serviceworker-internals/?devtools)

### Server Side Events

Dette er et klient-API for å motta push-meldinger.

- [Eksempel](./server-side-events)
- [En forklaring på Server side events](https://www.neerajsidhaye.com/posts/sse/sse/)

### Web Socker

Dette er en toveis-protokoll, der både klienten og server kan sende meldinger.
Klienten registrerer seg ved å sende en melding til server med header `Upgrade: websocket`.
Server svarer med `101`.

- [Eksempel](./websocket)
- [Wikipedia: WebSocket](https://en.wikipedia.org/wiki/WebSocket)
- [RFC6455](https://datatracker.ietf.org/doc/html/rfc6455)

### Data-API

Du bør aldri gi konsumenter direkte tilgang til databasen.
Da kortsluttes forretningslogikken, og det er vanskelig å ha kontroll på ytelsen.

Det finnes derimot dedikerte protokoller som kan brukes til å gjøre dataene tilgjengelige.
Det gjør det mulig for konsumentene å betjenes seg selv, og det er klart avgrenset
og veldefinert hvilke data som er tilgjengelig.

#### [OData](https://www.odata.org)

Dette er et API fra Microsoft. Det er REST-basert.

#### [GraphQL](https://graphql.org/)

Dette er laget av Facebook.
Det består av typedefinisjoner og har et spørrespråk.

Det er JSON-basert.

### REST

«REST» står for _representational state transfer_. Det handler, kort fortalt, om å bruke HTTP-standarden
slik den var ment. 

Man kan beskrive det i fire nivåer:

- **HTTP**: Et minstekrav er at HTTP brukes i kommunikasjonen.
- **Resources**: API-et beskriver ressurser. Dette er i motsetning til et RPC-type API, der metoder eksponeres for konsumentene. En url referer til en ressurs (eller et sett av ressurser).
- **HTTP verb**: Metodene `GET`, `POST`, `PUT`, `DELETE` og `PATCH` brukes for å indikere hvilken operasjon som skal utføres på ressursen.
- **Hypermedia controls**: Linker mellom ressurser kan gjøre det mulig for en konsument å navigere mellom ressurser. 

Man kan bruke query-parametre for å implementere søk/filtrering, paginering og sortering.

- [Wikipedia](https://en.wikipedia.org/wiki/Representational_state_transfer)
- [Maturity model](https://martinfowler.com/articles/richardsonMaturityModel.html)
- [Resource naming](https://restfulapi.net/resource-naming/)
- [MDN: HTTP methods](https://developer.mozilla.org/en-US/docs/Web/HTTP/Methods)
- [Method definitions](https://httpwg.org/specs/rfc9110.html#rfc.section.9.3)
- [En streng definisjon (Roy T. Fielding)](https://roy.gbiv.com/untangled/2008/rest-apis-must-be-hypertext-driven)
- [Pragamatic REST](https://www.ben-morris.com/pragmatic-rest-apis-without-hypermedia-and-hateoas/)
- [The REST of the ten commandments](https://hackernoon.com/the-rest-of-the-10-commandments?ref=hackernoon.com)
- [HATEOAS](https://en.wikipedia.org/wiki/HATEOAS)
- [JSON Patch; en standard for patch-meldinger](https://jsonpatch.com/)

## Testing

### Testtyper

Det finnes mange typer testing. Et forsøk på en [liste finnes her](https://glossary.istqb.org/en_US/search).

- **Akseptansetesting**: Valigvis en manuell test der kunden skal godkjenne en leveranse. Om det gjøres før produksjonsetting vil det blokkere utrullingsflyten.
- **Systemtest**: Test av et system, vanligvis isolert fra integrasjoner.
- **Regresjonstest**: En test av all funksjonalitet for å luke ut følgefeil av nye endringer. Det er egentlig bare realistisk å gjøre med automatisk testing.
- **A/B-test**: En metode for å teste ut funksjonalitet ved å sende ulike versjoner av koden til brukerne i produksjon. For eksempel kan 10% av brukerne få en versjon som har en liten endring fra resten. Ved å måle brukernes atferd, er det mulig å anslå om endringen fungerer bedre enn originalen. Dette krever automatisk utrulling, god overvåking av bruksdata og en viss mengde brukere.
- **Sikkerhetstest/PEN-test**: Dette er en test for å se om det er mulig å komme rundt systemets sikkerhetsmekanismer. Det gjøres ofte manuelt, men ved å ta i bruk verktøy for å utnytte kjente sikkerhetshull. Det kan enten skje som en «black-box»-test der testerne ikke kjenner til hvordan systemet er bugget, eller ved en analyse av kildekoden.
- **UU-test/compliance-test**: Dette er tester som ser etter brudd på spesifikke regelverk. For UU (tilgjengelighet) finnes det verktøy som gjør testingen enklere.

### Manuell testing

Dette er testing av løsningen som gjøres manuelt, ved å simulere brukere og følge test-script.
Utviklere er notorisk dårlige til å finne feil i egen kode, så litt av hensikten er at det er
noen som ikke kjenner til hvordan funksjonaliteten er utviklet som kan avsløre svakheter.

- [En oppsummering av manuell testing](https://www.simplilearn.com/manual-testing-article)
- [Dan North: We need to talk about testing](https://dannorth.net/we-need-to-talk-about-testing/)

