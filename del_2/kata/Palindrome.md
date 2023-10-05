# Palindrome test

Dette er en kata for å trene på TDD og parprogrammering. Kataen er ment som en introduksjon til TDD og parprogrammering. Husk rekkefølgen; test først, så kode, så refaktorer.

Reglene er som følger:

- Et palindrom er et ord, setninger eller tall som er likt skrevet baklengs som forlengs
- For eksempel er "tut" et palindrom, mens "kratt" ikke er det
- Store og små bokstaver har ikke noe å si, ei heller mellomrom eller andre tegn
- Eksempel på setninger kan være "Agnes i senga" eller "En ranet Unni sier: Stas at rom-nissen ga dem et ømt møte med Agnes sin mor. Ta sats, reis inn uten Arne!". Denne setningen er ikke et palindrom.


## Nødvendige avhengigheter

For at koden under skal kjøre, må du ha følgende avhengigheter i `build.gradle.kts`:

```kotlin
    testImplementation(kotlin("test"))
```

## Testkode

Aktiver en og en av testene under ved å fjerne `//` foran annotasjonene. Det er så klart anbefalt å gjøre det i rekkefølge, og du kan selvfølgelig skrive dine egne tester - bare husk å skriv testen først i god TDD ånd. :)

```Kotlin
class PalindromeTest {

    @Test
    fun `1. 'tut' er et palindrom`(){
        val palindromeChecker = PalindromeChecker()
        val result = palindromeChecker.isPalindrome("tut")
        assertTrue(result)
    }

    //@Test
    fun `2. 'kratt' er ikke et palindrom`(){
        val palindromeChecker = PalindromeChecker()
        val result = palindromeChecker.isPalindrome("kratt")
        assertFalse(result)
    }

    //@Test
    fun `3. 'Tut' er et palindrom, selv om det er store og små bokstaver`(){
        val palindromeChecker = PalindromeChecker()
        val result = palindromeChecker.isPalindrome("Tut")
        assertTrue(result)
    }

    //@Test
    fun `4, 'Tut.' er et palindrom, selv om det er store og små bokstaver og punktum`(){
        val palindromeChecker = PalindromeChecker()
        val result = palindromeChecker.isPalindrome("Tut.")
        assertTrue(result)
    }

    //@Test
    fun `5. 'Agnes i senga' er et palindrom, selv om det inneholder mellomrom`(){
        val palindromeChecker = PalindromeChecker()
        val result = palindromeChecker.isPalindrome("Agnes i senga")
        assertTrue(result)
    }

    //@Test
    fun `6. 'En ranet Unni sier: Stas at rom-nissen ga dem et ømt møte med Agnes sin mor. Ta sats, reis inn uten Arne!' er et palindrom`(){
        val palindromeChecker = PalindromeChecker()
        val result = palindromeChecker.isPalindrome("En ranet Unni sier: Stas at rom-nissen ga dem et ømt møte med Agnes sin mor. Ta sats, reis inn uten Arne!")
        assertTrue(result) 
    }

    //@Test
    fun `7. 'Denne setningen er ikke et palindrom.' er ikke et palindrom`(){
        val palindromeChecker = PalindromeChecker()
        val result = palindromeChecker.isPalindrome("Denne setningen er ikke et palindrom.")
        assertTrue(result)
    }
}
//https://github.com/MayuRuru/TDD-practise-kotlin/blob/main/src/test/kotlin/palindromeTest.kt
```
