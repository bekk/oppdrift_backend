import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
 class PalindromeCheckerTests {

  @Test
  fun `1 'tut' er et palindrom`() {
   val palindromeChecker = PalindromeChecker()
   val result = palindromeChecker.isPalindrome("tut")
   assertTrue(result)
  }

  @Test
  fun `2 'kratt' er ikke et palindrom`() {
   val palindromeChecker = PalindromeChecker()
   val result = palindromeChecker.isPalindrome("kratt")
   assertFalse(result)
  }

  @Test
  fun `3 'Tut' er et palindrom, selv om det er store og små bokstaver`() {
   val palindromeChecker = PalindromeChecker()
   val result = palindromeChecker.isPalindrome("Tut")
   assertTrue(result)
  }

  @Test
  fun `4 'Tut(dot)' er et palindrom, selv om det er store og små bokstaver og punktum`() {
   val palindromeChecker = PalindromeChecker()
   val result = palindromeChecker.isPalindrome("Tut.")
   assertTrue(result)
  }

  @Test
  fun `5 'Agnes i senga' er et palindrom, selv om det inneholder mellomrom`() {
   val palindromeChecker = PalindromeChecker()
   val result = palindromeChecker.isPalindrome("Agnes i senga")
   assertTrue(result)
  }

  @Test
  fun `6 'En ranet Unni sier(kolon) Stas at rom-nissen ga dem et ømt møte med Agnes sin mor(dot) Ta sats, reis inn uten Arne!' er et palindrom`() {
   val palindromeChecker = PalindromeChecker()
   val result =
    palindromeChecker.isPalindrome("En ranet Unni sier: Stas at rom-nissen ga dem et ømt møte med Agnes sin mor. Ta sats, reis inn uten Arne!")
   assertTrue(result)
  }

  @Test
  fun `7 'Denne setningen er ikke et palindrom(dot)' er ikke et palindrom`() {
   val palindromeChecker = PalindromeChecker()
   val result = palindromeChecker.isPalindrome("Denne setningen er ikke et palindrom.")
   assertFalse(result)
  }
 }