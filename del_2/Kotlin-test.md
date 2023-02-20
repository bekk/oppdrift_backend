# Testing i Kotlin

![opprett-nytt-prosjekt](./../img/test-prosjekt/nytt-test-prosjekt.png)

![opprett-fil](./../img/test-prosjekt/nyfil.png)

```kotlin
class MinKuleGreie {
    fun skrik(tekst: String) = tekst.uppercase()
}
```


![generer-test](./../img/test-prosjekt/generate-test.png)

![ny-test](./../img/test-prosjekt/generate-test-menu.png)


![kjoer-test](./../img/test-prosjekt/test-result.png)

## kotlin.test / Jupiter / JUnit


```kotlin
import kotlin.test.Test
import kotlin.test.assertEquals

internal class MinKuleGreieTest {

    @Test
    fun test_skrik_er_uppercase() {
        val expected = "HEI"
        assertEquals(expected, MinKuleGreie().skrik("hei"))
    }
}
```

Les mer på: https://kotlinlang.org/docs/jvm-test-using-junit.html

## Spek
https://www.spekframework.org/

## Mockk
https://mockk.io/

