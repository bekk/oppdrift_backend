Sommerfaggruppe backend
===

![refleksjon.png](../img/refleksjon.png)

## Hva er backend?

```mermaid
mindmap
  root((Backend))
    API og kommunikasjon
      REST
        HTTP-metoder
        Statuskoder
        Ressursmodellering
        HATEOAS
      GraphQL
        Queries og mutations
        Schema-definisjon
        N+1-problemet
      gRPC
        Protocol Buffers
        Streaming
      WebSockets
        Sanntidskommunikasjon
        Heartbeat og reconnect
      API-versjonering
      Feilhåndtering
      Resilience
        Rate limiting
        Retry policy
        Circuit Breaker
      OpenAPI og Swagger
    Autentisering og autorisasjon
      OAuth 2.0
      OpenID Connect
      JWT
      API-nøkler
      RBAC
      Sessions og cookies
    Databaser
      Relasjonsdatabaser
        PostgreSQL
        Normalisering
        Transaksjoner og ACID
        Indekser og ytelse
      NoSQL
        Dokumentdatabaser
        Key-value
        Kolonnebaserte
        Grafbaser
      ORM og query builders
      Migrasjoner
      Connection pooling
      CAP-teoremet
    Arkitektur og design
      Monolitt vs mikrotjenester
      Domenemodellering og DDD
      Event-drevet arkitektur
      Meldingskøer
        Kafka
        RabbitMQ
      CQRS og Event Sourcing
      Caching
        In-memory
        CDN-caching
        Cache-invalidering
        HTTP-caching
      Bakgrunnsjobber
      Dependency Injection
    Testing
      Enhetstesting
      Integrasjonstesting
      Kontraktstesting
      Testdatabaser og fixtures
      Mocking og stubbing
      Testdekning
      Property-based testing
    Drift og produksjon
      Docker og containers
      Kubernetes
      CI/CD-pipelines
      Logging
      Metrics og overvåking
      Distribuert tracing
      Alerting
      Feature flags
    Sikkerhet
      Input-validering
      OWASP Top 10
      CORS
      TLS og HTTPS
      Dependency scanning
      Secrets management
```

## Workshop

Installasjon er bseskrevet her: [installering](../installering.md)

### Lag et rest-api i Spring Boot

Du kan bruke IntelliJ for å gjøre disse oppgavene. 

1. [Lag et nytt prosjekt i Kotlin og Spring Boot](../del_0/nytt-prosjekt.md)
2. [Databaseintegrasjon](../del_1/database-integrasjon.md)
3. [REST](../del_2/Spring-REST.md)

### Lag et rest-api i Dotnet

Du må ha dotnet SDK installert for å gjøre disse oppgavene 

1. [Nytt prosjekt](dotnet/nytt-prosjekt.md)
2. [Databaseintegrasjon](dotnet/database-integrasjon.md)
3. [REST](dotnet/aspnet-REST.md)