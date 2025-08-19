package com.example.lab42

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier

class MainActivity : ComponentActivity() {

    // Punto de entrada; monta Compose y maneja el estado hoisted

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HealthyLivingTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Usar remember + mutableStateListOf

                    val items = remember { mutableStateListOf<RecipeItem>() }

                    HealthyLivingApp(
                        items = items,
                        onAddItem = { name, url ->
                            // Validaciones básicas
                            val trimmedName = name.trim()
                            val trimmedUrl = url.trim()
                            require(trimmedName.isNotEmpty()) { "El nombre no puede estar vacío" }
                            require(trimmedUrl.isNotEmpty()) { "El URL no puede estar vacío" }
                            require(isLikelyUrl(trimmedUrl)) { "El URL debe comenzar con http:// o https://" }
                            require(items.none { it.name.equals(trimmedName, ignoreCase = true) }) {
                                "El elemento ya existe en la lista"
                            }
                            items += RecipeItem(name = trimmedName, imageUrl = trimmedUrl)
                        },
                        onRemoveItem = { item ->
                            items.remove(item)
                        }
                    )
                }
            }
        }
    }
}
