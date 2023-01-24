# Endepunkt

Vi skal lage en løsning fra scratch! Vi kommer til å bruke Spring Boot sin wizard (Spring Initializr)
som man finner i Intellij for å opprette et Spring Boot prosjekt.

## Oppsett

Start med å opprette et nytt prosjekt ved å velge `File -> New -> Project..`

![nytt-prosjekt](./../img/nytt-prosjekt/nytt-prosjekt.png)

Deretter velg Spring Initializr. Her er det viktig at du velger:

```
Language: Kotlin
Type: Gradle - Kotlin.
```


![valg](./../img/nytt-prosjekt/valg.png)

Klikk igjennom steg-viseren. På neste steg kan man legge til forskjellige avhengigheter til Spring.
Vi kommer til å legge til de vi trenger manuelt senere. 

Det kan ta litt tid for Gradle å laste ned
alle pakkene, men når Intellij og Gradle er ferdige med å laste ned og bygge prosjektet ønsker vi
å kjøre opp løsningen. Det gjør vi ved å gå til Kotlin-fila i mappa `main/kotlin/<package name>` 
og trykker på den grønne pila.

![run](./../img/nytt-prosjekt/run-it.png)

Hvis du får ca samme output som det over så har vi en kjørende Spring Boot applikasjon. 
Den gjør ingenting enda, så la oss legge til litt greier.


## La oss legge til ting!

For at applikasjonen vår skal gjøre noe som helst må vi legge til noen avhengigheter.
Legg til avhengigheten `spring-boot-starter-web` som gir oss mulighenten til å definere REST-endepunkt.

For å legge til nye pakker/avhengigheter i prosjektet gå til `build.gradle.kts` og legg til starter-web pakka
under dependencies:

```kotlin
dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    ...
}
```

## Endepunkt

Åpen Kotlin-fila som inneholder main metoden og lag et endepunkt! 
Dette gjør vi ved å legge til en klasse med en annotasjon:


```kotlin
...
fun main(args: Array<String>) {
    runApplication<MittProsjektApplication>(*args)
}

@RestController
class UserController {

    @GetMapping("user")
    fun user() = "Vegard"
}
```

Standardporten til Spring-boot er 8080. Gå til http://localhost:8080/user for å nå endepunktet.


## Scheduler

Videre kan vi legge til en schedulerer som kjører en jobb. Vi kan fortsette i samme Kotlin-fil, 
eller lage en ny fil med koden under:

```kotlin
...
@Configuration
@EnableScheduling
class JobConfig {

    @Scheduled(fixedDelay = 2000, initialDelay = 500)
    fun letsGo() {
        println("Hei")
    }
}

```

## Videre arbeid

- Lag et HashMap som er en "database" og holder på state
- Send tilbake et objekt som JSON fra REST-endepunktet
- Lage en statemachine som scheduleren oppdaterer underveis