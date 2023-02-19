# Palindrome test

```Kotlin
class palindromeTest {

    private val palindromeChecker: PalindromeChecker = PalindromeChecker()

    @Test
    fun `should tell that "mom" is a palindrome`(){
        //val palindromeChecker = PalindromeChecker()
        val result = palindromeChecker.isPalindrome("mom")
        assertTrue(result)
    }

    @Test
    fun `should tell that "bill" is not a palindrome`(){
        //val palindromeChecker = PalindromeChecker()
        val result = palindromeChecker.isPalindrome("bill")
        assertFalse(result)
    }

    @Test
    fun `should still detect a palindrome even if the casing is off`(){
        //val palindromeChecker = PalindromeChecker()
        //val result = palindromeChecker.isPalindrome("Mom")
        assertTrue(palindromeChecker.isPalindrome("Mom"))
    }

    @Test
    fun `should be able to tell that "Was It A Rat I Saw" is a palindrome`(){
        assertTrue(palindromeChecker.isPalindrome("Was It A Rat I Saw"))
    }

    @Test
    fun `should be able to tell that "Never Odd or Even" is palindrome`(){
        assertTrue(palindromeChecker.isPalindrome("Never Odd or Even"))
    }

}
//https://github.com/MayuRuru/TDD-practise-kotlin/blob/main/src/test/kotlin/palindromeTest.kt
```