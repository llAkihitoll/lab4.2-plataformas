// -----------------------------------------------
// TÍTULO: UI principal (Compose) sin KeyboardOptions
// PASOS:
// 1) Formulario (TextFields) simples
// 2) Lista con LazyColumn
// 3) Carga de imágenes con Coil (AsyncImage)
// 4) Diálogo de confirmación para eliminar
// -----------------------------------------------
package com.example.lab42

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage  // <- Coil 2.x; si usas Coil 3 cambia a: import coil3.compose.AsyncImage

// -----------------------------------------------
// TÍTULO: Composable principal
// FUNCIÓN: Recibe la lista y callbacks (state hoisting)
// -----------------------------------------------
@Composable
fun HealthyLivingApp(
    items: List<RecipeItem>,
    onAddItem: (name: String, url: String) -> Unit,
    onRemoveItem: (RecipeItem) -> Unit
) {
    // -----------------------------------------------
    // TÍTULO: Estados locales
    // -----------------------------------------------
    var name by rememberSaveable { mutableStateOf("") }
    var url by rememberSaveable { mutableStateOf("") }
    var pendingDelete by remember { mutableStateOf<RecipeItem?>(null) }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    // -----------------------------------------------
    // TÍTULO: Contenedor principal
    // -----------------------------------------------
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // Encabezado
        Text(text = "Healthy Living — Recetas", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Ingresa el nombre y el URL de la imagen. Pulsa Agregar.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.height(16.dp))

        // -----------------------------------------------
        // TÍTULO: Campo de nombre (sin KeyboardOptions)
        // -----------------------------------------------
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre de la receta") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        // -----------------------------------------------
        // TÍTULO: Campo de URL (sin KeyboardOptions)
        // -----------------------------------------------
        OutlinedTextField(
            value = url,
            onValueChange = { url = it },
            label = { Text("URL de la imagen (http/https)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        // -----------------------------------------------
        // TÍTULO: Botón Agregar
        // -----------------------------------------------
        Button(
            onClick = {
                try {
                    onAddItem(name, url)
                    name = ""
                    url = ""
                    errorMsg = null
                } catch (e: IllegalArgumentException) {
                    errorMsg = e.message ?: "Error de validación"
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) { Text("Agregar") }

        if (errorMsg != null) {
            Spacer(Modifier.height(8.dp))
            AssistChip(
                onClick = { errorMsg = null },
                label = { Text(errorMsg!!) }
            )
        }

        Spacer(Modifier.height(16.dp))

        // -----------------------------------------------
        // TÍTULO: Lista de recetas
        // -----------------------------------------------
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            items(items, key = { it.id }) { item ->
                RecipeCard(item = item, onClick = { pendingDelete = item })
            }
        }
    }

    // -----------------------------------------------
    // TÍTULO: Diálogo de confirmación de eliminación
    // -----------------------------------------------
    pendingDelete?.let { item ->
        AlertDialog(
            onDismissRequest = { pendingDelete = null },
            title = { Text("Eliminar receta") },
            text = { Text("¿Deseas eliminar \"${item.name}\" de la lista?") },
            confirmButton = {
                TextButton(onClick = {
                    onRemoveItem(item)
                    pendingDelete = null
                }) { Text("Eliminar") }
            },
            dismissButton = {
                TextButton(onClick = { pendingDelete = null }) { Text("Cancelar") }
            }
        )
    }
}

// -----------------------------------------------
// TÍTULO: Card de receta (imagen + texto)
// -----------------------------------------------
@Composable
fun RecipeCard(
    item: RecipeItem,
    onClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen con Coil
            AsyncImage(
                model = item.imageUrl,
                contentDescription = "Imagen de ${item.name}",
                modifier = Modifier
                    .size(72.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceContainerHighest,
                        shape = RoundedCornerShape(12.dp)
                    ),
                placeholder = ColorPainter(Color(0xFFE0E0E0)),
                error = ColorPainter(Color(0xFFFFCDD2))
            )

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = item.name, style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(4.dp))
                Text(
                    text = item.imageUrl,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
