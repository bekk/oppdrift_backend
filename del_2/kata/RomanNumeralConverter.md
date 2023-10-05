# Roman numerals

Dette er en kata for å trene på TDD og parprogrammering. Kataen er ment som en introduksjon til TDD og parprogrammering. Husk rekkefølgen; test først, så kode, så refaktorer.

Skriv en test for en romertallkalkulator. Her må reglene utledes fra spesifikasjonen lenger ned. Lykke til!

## Anbefalt avhengighet

Følgende avhengighet er anbefalt å legge i `build.gradle.kts`, slik at du får test runner og matchers:

```kotlin
    testImplementation(kotlin("test"))
```

## Test

```Kotlin
class RomanNumeralConverterTest {
    @Test
    fun convertFromArabic() {
        assertEquals("", RomanNumeralConverter.fromArabic(0))
    }
}
```

## Implementasjon

```Kotlin
object RomanNumeralConverter {
    fun fromArabic(arabic: Int): String
}
```

## Spesifikasjon

1. 0 ➡︎ ""
2. 1 ➡︎ "I"
3. 2 ➡︎ "II"
4. 4 ➡︎ "IV"
5. 5 ➡︎ "V"
6. 6 ➡︎ "VI"
7. 10 ➡︎ "X"
8. 9 ➡︎ "IX"
9. 11 ➡︎ "XI"
10. 500 ➡︎ "D"
11. 100 ➡︎ "C"
12. 2019 ➡︎ "MMXIX"
13. 3000 ➡︎ "MMM"
14. 65 ➡︎ "LV"
15. 1379 ➡︎ "MCCCLXXIX"

Ekstra: Lag en `RomanNumeralParser`
