# Roman numerals

Skriv en test for en romertallkalkulator:

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