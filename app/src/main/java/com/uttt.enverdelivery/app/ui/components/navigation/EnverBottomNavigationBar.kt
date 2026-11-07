package com.uttt.enverdelivery.app.ui.components.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.uttt.enverdelivery.app.ui.theme.EnverGreen
import com.uttt.enverdelivery.app.ui.theme.EnverTextMuted

// --- BARRA DE NAVEGACIÓN INFERIOR (SOLO ICONOS, MISMO TAMAÑO Y FORMA) ---
@Composable
fun EnverBottomNavigationBar(
    activeTab: String,
    onTabSelected: (String) -> Unit
) {
    Surface(
        color = Color.White,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Inicio (Solo Icono)
            IconButton(onClick = { onTabSelected("Inicio") }) {
                Icon(
                    imageVector = Icons.Outlined.Home,
                    contentDescription = "Inicio",
                    tint = if (activeTab == "Inicio") EnverGreen else EnverTextMuted,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Botón central flotante de Agregar (+)
            FloatingActionButton(
                onClick = { onTabSelected("Nuevo") },
                containerColor = EnverGreen,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.size(44.dp)
            ) {
                Icon(imageVector = Icons.Outlined.Add, contentDescription = "Agregar")
            }

            // Pedidos (Solo Icono)
            IconButton(onClick = { onTabSelected("Pedidos") }) {
                Icon(
                    imageVector = Icons.Outlined.Receipt,
                    contentDescription = "Pedidos",
                    tint = if (activeTab == "Pedidos") EnverGreen else EnverTextMuted,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Perfil (Solo Icono)
            IconButton(onClick = { onTabSelected("Perfil") }) {
                Icon(
                    imageVector = Icons.Outlined.PersonOutline,
                    contentDescription = "Perfil",
                    tint = if (activeTab == "Perfil") EnverGreen else EnverTextMuted,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
