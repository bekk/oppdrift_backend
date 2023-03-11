
Sjekk at applikasjonen svarer på port 8080 når du kjører opp løsningen fra forrige gang.

Du kan også ta utgangspunkt i [eksempel prosjektet](https://github.com/veiset/kotlin-spring-flyway-rest-example).


## Actuator

```kotlin
dependencies {
    ...
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    ...
}
```

Restart appen (husk å trykk på elefanten for å installere nye avhengigheter). 
Når appen starter opp sjekk at du får svar på: http://localhost:8080/actuator/health

## Dockerfile

Vi trenger en Dockerfile som beskriver hvordan applikasjonen vår skal bygges og pakkes sammen til et docker image.

```Dockerfile
FROM gradle:7 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon

FROM eclipse-temurin:17
EXPOSE 8080:8080
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/backend.jar
ENTRYPOINT ["java","-jar","/app/backend.jar"]
```

## Fly.io

Registrer en konto med [fly.io](https://fly.io). Veldig enkel signup ved å bruke GitHub kontoen din.

![fly.io signup](../img/flyio/fly-signup.png)

Følg guiden på fly.io for å installere Fly sitt toolkit (flyctl).


## Deploy til Fly.io

Gå til mappen med prosjektet (eller trykk på terminal-knappen i intellij).

Logg inn og deploy med flyctl

```bash
flyctl auth login
flyctl launch
```

Gå til `https://<generert-navn>.fly.dev/api/users`