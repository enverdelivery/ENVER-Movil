package com.uttt.enverdelivery.app.ui.components.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.uttt.enverdelivery.app.ui.components.common.EnverGlobalHeader
import com.uttt.enverdelivery.app.ui.screens.createorder.CreateOrderFlowScreen
import com.uttt.enverdelivery.app.ui.screens.createorder.CreateOrderUiState
import com.uttt.enverdelivery.app.ui.screens.home.HomeScreen
import com.uttt.enverdelivery.app.ui.screens.pedidos.PedidosScreen
import com.uttt.enverdelivery.app.ui.theme.EnverBackground
import com.uttt.enverdelivery.app.ui.theme.EnverTextMuted

// Secciones que comparten la barra inferior fija (según los destinos del diseño de Home)
enum class MainSection(val label: String) {
    INICIO("Inicio"),
    NUEVO("Nuevo"),
    PEDIDOS("Pedidos"),
    PERFIL("Perfil");

    companion object {
        fun fromLabel(label: String): MainSection? = entries.firstOrNull { it.label == label }
    }
}

// Contenedor principal de la app: mantiene fijos el encabezado global y la barra
// de navegación inferior, cambiando únicamente el contenido de la sección activa.
// SeccionSolicitada: sección pedida desde fuera (p. ej. "Pedir" en FT-04 marca
// PEDIDOS); se consume una vez vía LaunchedEffect para permitir usos repetidos.
@Composable
fun MainScaffold(
    onArticleClick: (String) -> Unit = {},
    seccionSolicitada: MainSection? = null,
    onSeccionConsumida: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedSection by remember { mutableStateOf(MainSection.INICIO) }

    LaunchedEffect(seccionSolicitada) {
        if (seccionSolicitada != null) {
            selectedSection = seccionSolicitada
            onSeccionConsumida()
        }
    }

    // Hoisted aquí para conservar los datos del flujo "Nuevo pedido" entre pasos
    // y entre cambios de sección
    val createOrderState = remember { CreateOrderUiState() }

    Scaffold(
        modifier = modifier,
        bottomBar = {
            EnverBottomNavigationBar(
                activeTab = selectedSection.label,
                onTabSelected = { label ->
                    MainSection.fromLabel(label)?.let { selectedSection = it }
                }
            )
        },
        containerColor = EnverBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Encabezado global compartido: una sola instancia para todas las secciones
            EnverGlobalHeader()

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f, fill = true)
            ) {
                when (selectedSection) {
                    MainSection.INICIO -> HomeScreen(
                        onEmergencyClick = { selectedSection = MainSection.NUEVO },
                        onArticleClick = onArticleClick,
                        modifier = Modifier.fillMaxSize()
                    )
                    MainSection.NUEVO -> CreateOrderFlowScreen(
                        uiState = createOrderState,
                        modifier = Modifier.fillMaxSize()
                    )
                    MainSection.PEDIDOS -> PedidosScreen(modifier = Modifier.fillMaxSize())
                    MainSection.PERFIL -> SectionPlaceholder(sectionName = MainSection.PERFIL.label, modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }
}

// Placeholder mínimo para secciones cuyo diseño aún no fue proporcionado.
@Composable
private fun SectionPlaceholder(sectionName: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = sectionName,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A)
        )
        Text(
            text = "Pantalla en construcción",
            fontSize = 12.sp,
            color = EnverTextMuted
        )
    }
}
