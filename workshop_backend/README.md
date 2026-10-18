# Backend workshop

En enkel oppskrift for å lage et typisk API-prosjekt.
Du kan velge om du vil bruke dotnet eller Spring Boot.

## Før du starter

Sjekk at oppsettet ditt er i orden før du går løs på oppgavene. Det tar noen minutter, mest ventetid på nedlastinger.
Har du gjort dette tidligere, er du allerede i gang.

1. **Installer verktøyene** for sporet du har valgt (se tabellen under).
2. **Sjekk versjonen:**

   | .NET | Spring Boot |
   |---|---|
   | `dotnet --list-sdks` skal vise en `10.0.x`.<br>Det er greit å ha eldre versjoner i tillegg. | `java -version` skal vise 21 eller nyere.<br>Det er greit å ha eldre versjoner i tillegg. |

3. **Lag prosjektet fra oppgave 1 og kjør det én gang.** Du trenger bare komme til "Oppsett"-delen, til appen starter.
   Da er alle pakker lastet ned, og du vet at oppsettet virker.
4. **Spring Boot: sjekk Gradle JVM i IntelliJ.** Gå til *Settings → Build, Execution, Deployment → Build Tools → Gradle*
   og sjekk at *Gradle JVM* er versjon 21 eller nyere. Peker den på en eldre JDK, feiler byggingen med uklare feilmeldinger.

Står du fast her, si ifra med en gang – installasjonsproblemer er lettest å løse tidlig.

<details>
  <summary>Vanlige problemer underveis</summary>

**Begge sporene**

- *Porten er i bruk* (`address already in use`, `APPLICATION FAILED TO START`) – du har sannsynligvis appen kjørende fra før,
  f.eks. både fra terminalen og fra editoren. Stopp den gamle.
- *Endringen virker ikke* – start appen på nytt. Spring Boot laster ikke inn endringer av seg selv, og `dotnet watch`
  trenger restart ved enkelte endringer.
- *Rare svar fra databasen etter flere kjøringer* – databasen lagres til fil, så data blir liggende mellom kjøringer.
  Slett databasefila for å starte på nytt (se oppgavene).
- *Windows: `curl` oppfører seg rart* – i Windows PowerShell 5 er `curl` et alias for noe annet. Bruk `curl.exe` eller PowerShell 7.

**Spring Boot**

- *404 på et endepunkt som finnes* – Spring finner bare klasser i samme pakke som hovedklassen (den med `main`), eller i underpakker.
  Sjekk `package`-linjen øverst i fila.
- *Røde imports etter å ha lagt til en avhengighet* – trykk på Gradle-ikonet (elefanten) for å laste ned pakkene.
- *`Cannot find a Java installation on your machine matching...`* – Java-versjonen i `build.gradle.kts` er ikke installert.
  Endre tallet i `JavaLanguageVersion.of(...)` til versjonen du har.
- *Flyway nekter å starte (`Validate failed`, `checksum mismatch`)* – du har endret en migrasjonsfil som allerede er kjørt.
  Lag heller en ny fil (`V3__...sql`), eller slett `testdb.mv.db`.
- *`.http`-filer kan ikke kjøres* – HTTP Client-en i IntelliJ krever abonnement. Bruk curl, Swagger UI eller VS Code (se oppgave 3).

**.NET**

- *Pakker kan ikke installeres (`not compatible with net8.0`)* – prosjektet ble laget med en eldre SDK. Installer .NET 10 og lag prosjektet på nytt.
- *`command not found: dotnet-ef`* – se tipset om PATH i oppgave 2.
- *`The type or namespace name ... could not be found`* – sjekk `namespace`-linjen og at du har riktige `using`-linjer øverst.

</details>

## Oppgaver

<table>
    <thead>
    <tr>
        <th>Lag et rest-api i Dotnet</th>
        <th>Lag et rest-api i Spring Boot</th>
    </tr>
    </thead>
    <tr>
        <td>
            Du kan bruke <a href="https://code.visualstudio.com/">VsCode</a> 
            eller en hvilken som helst annen teksteditor.
            Mange foretrekker <a href="https://www.jetbrains.com/rider/">Rider</a> 
            til c#-programmering.
            Du må også installere <a href="https://dotnet.microsoft.com/en-us/download">
            .NET 10 SDK herfra</a>.
            <p>
                <img src="../img/mac_logo.png" /> <code>brew install --cask dotnet-sdk</code>
            </p>
            <p>
                <img src="../img/win_logo.png" /> <code>winget install Microsoft.DotNet.SDK.10</code>
            </p>
        </td>
        <td>
            Det anbefales å bruke 
            <a href="https://www.jetbrains.com/idea/">IntelliJ</a> til oppgavene.
            Den kan installeres fra https://www.jetbrains.com/idea/download/ eller
            med Homebrew eller Winget. Gratisversjonen holder til oppgavene, inkludert Spring Boot-wizarden.
            (Siden versjon 2025.3 finnes det bare én IntelliJ IDEA. Ultimate-funksjonene låses opp med abonnement.)
            Du trenger også en JDK (versjon 21 eller nyere), som IntelliJ kan laste ned for deg.
            <p>
                <img src="../img/mac_logo.png" /> <code>brew install --cask intellij-idea</code>
            </p>
            <p>
                <img src="../img/win_logo.png" /> <code>winget install JetBrains.IntelliJIDEA.Ultimate</code>
            </p>
        </td>
    </tr>
    <tr>
        <td>1: <a href="../sommer/dotnet/nytt-prosjekt.md">Lag et nytt prosjekt i ASP.net</a>.</td>
        <td>1: <a href="../del_0/nytt-prosjekt.md">Lag et nytt prosjekt i Kotlin og Spring Boot</a>.</td>
    </tr>
    <tr>
        <td>2: <a href="../sommer/dotnet/database-integrasjon.md">Databaseintegrasjon</a>.</td>
        <td>2: <a href="../del_1/database-integrasjon.md">Databaseintegrasjon</a>.</td>
    </tr>
    <tr>
        <td>3: <a href="../sommer/dotnet/aspnet-REST.md">REST</a>.</td>
        <td>3: <a href="../del_2/Spring-REST.md">REST</a>.</td>
    </tr>
</table>

## Veien videre

Har du kommet gjennom oppgavene, har du laget et API med database og CRUD-endepunkter – grunnmuren i de fleste backend-applikasjoner.
Under er konsepter du vil møte i ekte prosjekter. Det er ikke oppgaver med fasit, men stikkord og retning
for hva det kan være lurt å utforske videre. Navnene i parentes er det du kan søke etter for Spring Boot og .NET.

### Slik kommer du i gang

- **Bygg videre på ditt eget prosjekt.** Alt under kan legges til i API-et du allerede har laget. Det er mye lettere å lære
  noe nytt når resten av koden er kjent.
- **Ta ett konsept om gangen, og start smått.** Få én ting til å virke for ett endepunkt før du gjør det for alle.
  Hvert punkt under har et forslag til et første steg.
- **Bruk git.** Lag en ny branch for hvert konsept du prøver. Blir det rot, kan du alltid gå tilbake.
- **Les den offisielle dokumentasjonen.** Den er god for begge plattformene, og er ofte bedre enn tilfeldige bloggposter:
  - Spring: [spring.io/guides](https://spring.io/guides) (korte, praktiske guider) og [Spring Boot-dokumentasjonen](https://docs.spring.io/spring-boot/)
  - .NET: [ASP.NET Core-dokumentasjonen på Microsoft Learn](https://learn.microsoft.com/aspnet/core/)
- **Bruk gjerne AI-assistenter** til å forklare og foreslå kode, men sørg for at du forstår hva koden gjør før du tar den i bruk.

> [!TIP]
> Usikker på hvor du skal begynne? Denne rekkefølgen bygger naturlig på hverandre:
> 1. Validering og feilhåndtering – gjør API-et ditt hyggeligere å bruke.
> 2. Integrasjonstester – så du trygt kan endre koden videre.
> 3. PostgreSQL i Docker – en ekte database, som i de fleste prosjekter.
> 4. Autentisering – beskytt endepunktene som endrer data.
> 5. Helsesjekker og logging – så du kan se hva applikasjonen gjør når den kjører.

### Gjør API-et mer robust

- **Validering** – I dag kan man opprette en bruker med tomt brukernavn eller en tittel på 1000 tegn. Validering betyr å
  avvise ugyldig input med en tydelig `400 Bad Request` som forteller hva som er feil, før det havner i databasen.
  (Spring: `spring-boot-starter-validation`, `@NotBlank`, `@Size`, `@Valid`. .NET: `[Required]`, `[StringLength]`.)

  *Kom i gang:* Gjør `username` påkrevd i `User`, og send en POST med tomt brukernavn. Hva får du tilbake før og etter?

- **Feilhåndtering** – Når noe går galt i dag, får klienten ofte en `500` med en stacktrace som avslører detaljer om koden.
  Med sentral feilhåndtering samler du logikken for å gjøre exceptions om til ryddige feilmeldinger på ett sted.
  Se etter *Problem Details* (RFC 9457), et standardformat for feilmeldinger i JSON.
  (Spring: `@ControllerAdvice`, `ProblemDetail`. .NET: `AddProblemDetails()`, `UseExceptionHandler()`.)

  *Kom i gang:* Prøv å legge til en bruker med en id som allerede finnes, og se hva som skjer. Få det til å gi `409 Conflict` i stedet.

- **DTO-er vs. entiteter** – Nå bruker vi den samme `User`-klassen både i databasen og i API-et. Det betyr at hver endring i
  tabellen også endrer API-et, og at klienten må sende inn id-en. Ved å lage egne klasser for det som går inn og ut av API-et
  (*Data Transfer Objects*) kan de to utvikle seg hver for seg.

  *Kom i gang:* Lag en `CreateUserRequest` uten id, og la databasen lage id-en ved opprettelse.

- **Paginering, filtrering og sortering** – `GET /api/users` returnerer alle brukerne. Med 100 000 brukere blir det tregt.
  Vanlig praksis er å la klienten be om en side om gangen, og filtrere og sortere med query-parametere:
  `GET /api/users?page=2&size=20&sort=username`.

  *Kom i gang:* Legg til `page` og `size` som query-parametere, og bruk `LIMIT`/`OFFSET` i SQL (eller `Skip`/`Take` i EF Core).

- **Konfigurasjon og miljøer** – Tilkoblingsstrengen til databasen står nå i koden eller i en konfigurasjonsfil. I et ekte prosjekt
  har man ulike innstillinger for utvikling, test og produksjon, og passord og nøkler skal aldri inn i git.
  (Spring: profiler, `application-dev.properties`. .NET: `appsettings.Development.json`, *user secrets*. Begge: miljøvariabler.)

  *Kom i gang:* Flytt tilkoblingsstrengen til konfigurasjonen, og overstyr den med en miljøvariabel når du starter appen.

### Testing

Tester gjør det trygt å endre koden. Uten tester må du klikke deg gjennom alt manuelt etter hver endring.

- **Enhetstester** – Tester av en enkelt klasse eller funksjon, isolert fra database og nettverk. De er raske, og fine for logikk.
  (JUnit og kotlin-test, xUnit.) Se også [Testing i Kotlin](../del_2/Kotlin-test.md).

  *Kom i gang:* Skriv tester for state-maskinen fra del 1. Den har ingen avhengigheter og passer perfekt.

- **Integrasjonstester** – Tester som starter hele applikasjonen og kaller endepunktene på ekte, med database. De fanger feil
  som enhetstester ikke ser, f.eks. feil i SQL eller rutingen.
  (Spring: `@SpringBootTest`, `MockMvc`. .NET: `WebApplicationFactory` fra `Microsoft.AspNetCore.Mvc.Testing`.)

  *Kom i gang:* Skriv én test som kaller `GET /api/users` og sjekker at du får `200 OK` og to brukere.
  Gjør så det samme for alle kallene i `.http`-fila.

- **Testcontainers** – Starter en ekte database i Docker mens testene kjører, og fjerner den etterpå. Da tester du mot samme
  database som i produksjon, i stedet for H2 eller SQLite. Se [testcontainers.com](https://testcontainers.com/).

  *Kom i gang:* Når du har PostgreSQL i Docker (se under), la integrasjonstestene bruke Testcontainers.

### Database og infrastruktur

- **PostgreSQL i Docker** – H2 og SQLite er fine til å lære, men de fleste prosjekter bruker en databaseserver som PostgreSQL.
  Med Docker kan du kjøre en slik database lokalt uten å installere den. Se [Docker-jukselappen](../del_1/Docker_Jukselapp.md).

  *Kom i gang:* Start PostgreSQL med én `docker run`-kommando (eller `docker compose`), bytt tilkoblingsstrengen og kjør migrasjonene.
  Virker alt fortsatt? Du vil kanskje oppdage små forskjeller i SQL-dialekten.

- **Relasjoner** – Ekte data henger sammen: en bruker har mange meldinger, en ordre har mange varer. Her lærer du om
  fremmednøkler, `JOIN` og *N+1-problemet* (når én liste med data fører til hundrevis av databasekall).

  *Kom i gang:* Legg til en tabell `Messages` som tilhører en bruker, og et endepunkt `GET /api/users/{id}/messages`.

- **Containerisering** – En `Dockerfile` pakker API-et ditt i et image som kan kjøres likt overalt: på din maskin, i testmiljøet og i skyen.

  *Kom i gang:* Lag en `Dockerfile` for API-et, og en `docker-compose.yml` som starter både API-et og databasen med én kommando.

- **Caching** – Hvis de samme dataene hentes om og om igjen (f.eks. pokemoner fra PokéAPI), kan du lagre svaret en stund
  i stedet for å spørre hver gang. Det gjør API-et raskere og sparer den andre tjenesten. Se [caching-eksemplene](../del_2/caching).
  (Spring: `@Cacheable`. .NET: `IMemoryCache`, `HybridCache`.)

  *Kom i gang:* Cache svaret fra PokéAPI i noen minutter, og mål forskjellen i responstid.

### Integrasjoner

De fleste backend-systemer snakker med andre systemer. Det er her mye kan gå galt.

- **Robuste kall mot andre API-er** – Hva skjer med `/poke/{name}` hvis PokéAPI er nede eller svarer veldig tregt?
  Robuste integrasjoner bruker timeouts, prøver på nytt ved midlertidige feil (*retries*), og slutter å prøve en stund hvis
  tjenesten er helt nede (*circuit breaker*). Se etter *resilience*.
  (Spring: Resilience4j. .NET: `Microsoft.Extensions.Http.Resilience`.)

  *Kom i gang:* Sett en timeout på kallet mot PokéAPI, og returner en fornuftig feilmelding (f.eks. `404` for en pokemon som ikke finnes,
  `503` hvis PokéAPI er nede) i stedet for `500`.

- **Asynkron meldingsutveksling** – I stedet for at systemer kaller hverandre direkte, sender de meldinger via en kø
  (RabbitMQ, Kafka, Azure Service Bus). Avsenderen trenger ikke vente på svar, og mottakeren kan behandle meldingene når den har tid.

  *Kom i gang:* Kjør RabbitMQ i Docker. La API-et legge en melding på en kø når en bruker opprettes, og la bakgrunnsjobben fra del 1 lese dem.

- **Webhooks** – Når et annet system vil si ifra til deg om at noe har skjedd (f.eks. en betaling er gjennomført), kaller det et
  endepunkt hos deg. Det er det motsatte av at du spør dem hele tiden.

  *Kom i gang:* Lag et `POST /webhooks/...`-endepunkt, og test det med en tjeneste som [webhook.site](https://webhook.site/) eller med `.http`-fila.

- **Sanntid** – Noen ganger skal klienten få beskjed med en gang noe endrer seg, f.eks. i en chat eller et dashboard.
  Se [WebSocket](../del_2/websocket) og [Server-Sent Events](../del_2/server-side-events).

- **API-first med OpenAPI** – Dere har generert API-dokumentasjon fra koden. Mange team gjør det motsatte: de blir enige om
  kontrakten (OpenAPI-fila) først, og genererer kode eller klienter fra den. Da kan frontend og backend jobbe parallelt.

  *Kom i gang:* Last ned OpenAPI-fila fra API-et ditt, og generer en klient fra den med f.eks. OpenAPI Generator.

### Sikkerhet

- **Autentisering og autorisasjon** – *Autentisering* er å finne ut hvem brukeren er (innlogging). *Autorisasjon* er å finne ut
  hva brukeren har lov til. De fleste API-er bruker *tokens* (ofte JWT) og standardene OAuth2 og OpenID Connect.
  Start med å forstå begrepene før du skriver kode – det sparer mye forvirring.
  (Spring Security. ASP.NET Core Authentication og Authorization.)

  *Kom i gang:* La `GET` være åpent for alle, men krev innlogging for `POST`, `PUT` og `DELETE`. Start gjerne med et enkelt oppsett
  (Basic Auth eller API-nøkkel) for å forstå flyten, før du går over til tokens.

- **OWASP Top 10** – En liste over de vanligste sikkerhetshullene i webapplikasjoner. Ett av dem har du allerede unngått:
  SQL-injection, ved å bruke parametere i stedet for å lime verdier inn i SQL-strengen. Se [owasp.org/Top10](https://owasp.org/Top10/).

  *Kom i gang:* Les gjennom listen og vurder hvert punkt opp mot ditt eget API.

- **CORS** – Nettleseren stopper som standard JavaScript fra å kalle et API på en annen adresse eller port. CORS er måten
  API-et sier fra om hvilke nettsider som har lov. Dette er ofte det første som stopper deg når du kobler på en frontend.

  *Kom i gang:* Lag en enkel HTML-side som kaller API-et med `fetch`, se feilmeldingen i nettleserkonsollen, og fiks det med CORS-oppsett.

### Observerbarhet

Når applikasjonen kjører i produksjon, kan du ikke sette breakpoints. Da trenger du andre måter å se hva som skjer.

- **Strukturert logging** – Logg med felter (`userId=3`) i stedet for fritekst, slik at loggene kan søkes i og filtreres.
  Lær også loggnivåene (`DEBUG`, `INFO`, `WARN`, `ERROR`) og når hvert av dem skal brukes. (SLF4J/Logback, `ILogger`.)

  *Kom i gang:* Erstatt `println`/`Console.WriteLine` med en logger, og logg hver gang en bruker opprettes eller slettes.

- **Helsesjekker** – Et endepunkt som forteller om applikasjonen er oppe og får kontakt med databasen. Brukes av plattformen
  appen kjører på for å vite om den skal startes på nytt eller få trafikk.
  (Spring Boot Actuator, `/actuator/health`. .NET: `AddHealthChecks()`.)

  *Kom i gang:* Legg til en helsesjekk, stopp databasen, og se at svaret endrer seg.

- **Metrikker og tracing** – *Metrikker* er tall over tid: hvor mange requests, hvor lang tid de tar, hvor mange som feiler.
  *Tracing* lar deg følge én request gjennom flere tjenester og se hvor tiden går. Standarden for begge er
  [OpenTelemetry](https://opentelemetry.io/docs/).

  *Kom i gang:* Legg til OpenTelemetry og vis dataene lokalt, f.eks. med .NET Aspire-dashboardet eller Grafana i Docker.

### Levering

- **CI/CD** – *Continuous Integration* betyr at koden bygges og testene kjøres automatisk hver gang noen pusher.
  *Continuous Delivery/Deployment* betyr at den også leveres ut automatisk når alt er grønt.
  Se [GitHub Actions](https://docs.github.com/actions).

  *Kom i gang:* Legg prosjektet på GitHub og lag en workflow som bygger og kjører testene ved hver push.

- **Deploy til skyen** – Det er motiverende å få API-et ut på en adresse andre kan nå. Det finnes mange tjenester for dette
  (Fly.io, Azure App Service, Google Cloud Run m.fl.). Se [deploy med Fly](../del_3/deploy-fly.md).

  *Kom i gang:* Deploy API-et med en database, og del adressen med noen.

### API-design

- **REST i dybden** – Lær om *idempotens* (hvorfor `PUT` og `DELETE` tåler å bli sendt flere ganger, men ikke `POST`),
  hvilke statuskoder som passer når, og hvordan man navngir ressurser og URL-er.

  *Kom i gang:* Gå gjennom endepunktene dine og vurder: Returnerer de riktige statuskoder? Hva skjer om samme request sendes to ganger?

- **Versjonering** – Når andre har begynt å bruke API-et ditt, kan du ikke endre det fritt uten å ødelegge for dem.
  Vanlige løsninger er versjon i URL-en (`/v1/`, `/v2/`) eller i en header.

  *Kom i gang:* Lag en `v2` av ett endepunkt med en endring som ikke er bakoverkompatibel, og behold `v1`.

- **Alternativer til REST** – REST er vanligst, men ikke alltid best. *GraphQL* lar klienten velge nøyaktig hvilke data den vil ha,
  og [gRPC](../del_2/gRPC.md) er raskt og typesikkert for kommunikasjon mellom tjenester.

  *Kom i gang:* Les om når hvert av dem passer, og prøv gRPC-eksempelet.
