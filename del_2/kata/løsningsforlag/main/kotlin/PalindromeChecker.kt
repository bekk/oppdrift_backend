class PalindromeChecker {
    fun isPalindrome(text: String): Boolean  {
      val normalized = text.filter { it !in setOf('.', '!', ':', '-', ' ', ',')} .lowercase()
      return normalized == normalized.reversed()
    }
}