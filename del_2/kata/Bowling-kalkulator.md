# Kodekata Bowling-kalkulator

Denne oppgaven er hentet fra [Coding Dojo](http://codingdojo.org/kata/Bowling/). Kataen er ment som en introduksjon til TDD og parprogrammering. Husk rekkefølgen; test først, så kode, så refaktorer.

## Regler:
- Hver kamp med bowling inkluderer ti runder, eller "ruter" for bowleren.
- I hvert rute får bowleren opptil to forsøk på å slå ned alle pinnene.
- Hvis bowleren over to forsøk ikke klarer å slå ned alle pinnene, er poengsummen for den ruten summen av antall pinner som ble slått ned i de to forsøkene.
- Hvis bowleren over to forsøk slår ned alle pinnene, kalles dette en "spare" og poengsummen for ruten er ti pluss antall pinner som ble slått ned på neste forsøk 
  (i neste runde).
- Hvis bowleren på første forsøk i ruten slår ned alle pinnene, kalles dette en "strike". 
  Turen er over, og poengsummen for ruten er ti pluss summen av antall pinner som ble slått ned i de to neste forsøkene.
- Hvis bowleren får en spare eller strike i siste (tiende) rute, får bowleren lov til å kaste en eller to ekstra bonuskuler. 
  Disse bonuskastene tas som en del av samme tur. Hvis bonuskastene slår ned alle pinnene, gjentas ikke prosessen: 
  bonuskastene brukes bare til å beregne poengsummen for den siste ruten.
- Poengsummen for kampen er summen av alle rutesummene.


## Tegnforklaring
- `X` = Strike
- `/` = Spare
- `-` = Miss
- Tall = antall kjegler slått ned

## Anbefalt avhengighet

Følgende avhengighet er anbefalt å legge i `build.gradle.kts`, slik at du får test runner og matchers:

```kotlin
    testImplementation(kotlin("test"))
```

## Test cases
1. Gutter game = bare nuller

   `--------------------` = (score = 0)
2. En kjegle ned i hver rute 

   `11111111111111111111` = (score = 20)
3. Spare i første rute, en kjegle ned i resten

   `9/111111111111111111` = (score = 10 + 1 + 18 = 29)
4. Spare i siste rute, en kjegle ned i resten av rutene og med bonusrull

   `1111111111111111111/1` = (score = 18 + 10 + 1 = 29)
5. Strike i første rute, en kjegle ned i resten

    `X111111111111111111` = (score = 10 + 1 + 1 + 18 = 30)
6. Strike i siste rute, en kjegle ned i resten av rutene og med hver bonuskule
   
    `111111111111111111X11` = (score = 18 + 10 + 1 + 1 = 30)
7. Golden game = strikes i alle ruter
   
    `XXXXXXXXXXXX` = (score = 300)

8. Tilfeldig runde

    `X7/9-X-88/-6XXX81` = (score = 167)
