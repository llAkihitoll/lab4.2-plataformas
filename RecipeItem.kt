package com.example.lab42

import java.util.UUID

// Representa un registro en la lista
data class RecipeItem(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val imageUrl: String
)
