# Fizz Buzz

Dette er en enkel Kata med det enkle FizzBuzz-problemet. Kataen er ment som en introduksjon til TDD og parprogrammering. Husk rekkefølgen; test først, så kode, så refaktorer.
Reglene er som følger:

- For hvert tall som er delelig med 3, skriv ut "Fizz"
- For hvert tall som er delelig med 5, skriv ut "Buzz"
- For hvert tall som er delelig med 3 og 5, skriv ut "Fizzbuzz"
- For alle andre tall, skriv ut tallet

https://en.wikipedia.org/wiki/Fizz_buzz

Under har du noen ferdigskrevne tester som du kan bruke for å komme i gang. Hvis du følger stram TDD metodikk og kun skriver kode som oppfyller en og en av testene under, så vil du ikke nødvendigvis oppfylle alle reglene over. Ser du hvilken?

## Nødvendige avhengigheter

For at koden under skal kjøre, må du ha følgende avhengigheter i `build.gradle.kts`:

```kotlin
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
```

## Testkode

Aktiver en og en av testene under ved å fjerne `//` foran annotasjonene. Det er så klart anbefalt å gjøre det i rekkefølge, og du kan selvfølgelig skrive dine egne tester - bare husk å skriv testen først i god TDD ånd. :)

```Kotlin
class FizzbuzzTest {
    @Test
    fun `1 should receive number and return number`() {
        val result = fizzBuzz(1)
        assertEquals("1", result)
    }

    //@Test
    fun `2 should return Fizz if number is multiple of three`() {
        val result = fizzBuzz(3)
        assertEquals("Fizz", result)
    }

    //@Test
    fun `3 should return Buzz if number is multiple of five`() {
        val result = fizzBuzz(5)
        assertEquals("Buzz", result)
    }

    //@ParameterizedTest
    @CsvSource(
        "15",
        "30"
    )
    fun `4 should return Fizzbuzz if number is multiple of three and five`(number: Int) {
        val result = fizzBuzz(number)
        assertEquals("Fizzbuzz", result)
    }
}
//https://github.com/MayuRuru/TDD-practise-kotlin/blob/main/src/test/kotlin/fizzbuzzTest.kt
```
