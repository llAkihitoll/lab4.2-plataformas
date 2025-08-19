// Validación simple de URL
package com.example.lab42

// Comprueba que el texto empiece con http:// o https://
fun isLikelyUrl(s: String): Boolean =
    s.startsWith("http://", ignoreCase = true) || s.startsWith("https://", ignoreCase = true)
