package com.uttt.enverdelivery.app.ui.components.media

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.uttt.enverdelivery.app.ui.theme.EnverTextMuted

@Composable
fun EnverImage(
    source: ImageSource,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    when (source) {
        is ImageSource.Local -> {
            Image(
                painter = painterResource(id = source.resId),
                contentDescription = contentDescription,
                contentScale = contentScale,
                modifier = modifier
            )
        }
        is ImageSource.Remote -> {
            // Etapa actual sin carga remota: placeholder en un contenedor definido.
            // Cuando llegue la fuente en la nube solo cambia esta rama (p. ej. un
            // loader asíncrono), sin reescribir los composables que usan EnverImage.
            Box(
                modifier = modifier.background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Imagen no disponible",
                    color = EnverTextMuted
                )
            }
        }
    }
}
