package com.uttt.enverdelivery.app.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uttt.enverdelivery.app.R
import com.uttt.enverdelivery.app.ui.components.media.EnverImage
import com.uttt.enverdelivery.app.ui.components.media.ImageSource
import com.uttt.enverdelivery.app.ui.theme.EnverBadgeBg
import com.uttt.enverdelivery.app.ui.theme.EnverCardBg
import com.uttt.enverdelivery.app.ui.theme.EnverDarkGreen
import com.uttt.enverdelivery.app.ui.theme.EnverGreen
import com.uttt.enverdelivery.app.ui.theme.EnverInputBg
import com.uttt.enverdelivery.app.ui.theme.EnverSubtleGreenBg
import com.uttt.enverdelivery.app.ui.theme.EnverTextMuted

data class OrderItem(
    val id: String,
    val category: String,
    val title: String,
    val cost: String,
    val is100PercentYours: Boolean = false,
    val requesterName: String? = null,
    val rating: String? = null,
    val faculty: String? = null,
    val imageResId: Int? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onEmergencyClick: () -> Unit = {},
    onNavigateToNav: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf("Necesito algo") }
    var selectedFilter by remember { mutableStateOf("Lo más solicitado") }
    val scrollState = rememberScrollState()

    // Pedidos existentes por sub-filtro (cada espacio se muestra según el selector activo)
    val mostRequestedOrders = listOf(
        OrderItem(
            id = "1",
            category = "Calculadora + Útiles",
            title = "Calculadora científica Casio fx-991",
            cost = "$45 MXN",
            imageResId = R.drawable.articulo_calculadora_casio
        ),
        OrderItem(
            id = "2",
            category = "Papelería",
            title = "Cartulina blanca + plumón negro",
            cost = "$40 MXN",
            is100PercentYours = true,
            requesterName = "Alex Martinez",
            rating = "4.9 · 14 rescates",
            faculty = "TICs",
            imageResId = R.drawable.articulo_cartulina_plumon
        )
    )
    // Aún no se entregan datos de pedidos recientes: el espacio se conserva vacío
    val recentOrders = emptyList<OrderItem>()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        // --- HEADER SUPERIOR ---
        // El logo/campus/nivel rescatista proviene del ENCABEZADO GLOBAL compartido
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {

            // SALUDO + INSIGNIA DE USUARIO (Nivel 4 - Rescatista)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = "¡Hola, Mateo! 👋",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                    Text(
                        text = "¿Qué te salva la clase hoy?",
                        fontSize = 13.sp,
                        color = EnverTextMuted
                    )
                }
                Surface(
                    color = EnverBadgeBg,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "⚡",
                            fontSize = 10.sp,
                            color = EnverDarkGreen
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Nivel 4 - Rescatista",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = EnverDarkGreen
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- BOTONES MODO: NECESITO ALGO / QUIERO REPARTIR ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(EnverInputBg, RoundedCornerShape(28.dp))
                    .padding(4.dp)
            ) {
                Button(
                    onClick = { selectedTab = "Necesito algo" },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedTab == "Necesito algo") EnverGreen else Color.Transparent
                    ),
                    shape = RoundedCornerShape(24.dp),
                    elevation = null,
                    modifier = Modifier.weight(1f).height(44.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "✦", fontSize = 12.sp, color = if (selectedTab == "Necesito algo") Color.White else EnverTextMuted)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Necesito algo",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (selectedTab == "Necesito algo") Color.White else EnverTextMuted
                        )
                    }
                }

                Button(
                    onClick = { selectedTab = "Quiero repartir" },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedTab == "Quiero repartir") EnverGreen else Color.Transparent
                    ),
                    shape = RoundedCornerShape(24.dp),
                    elevation = null,
                    modifier = Modifier.weight(1f).height(44.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.ShoppingBag,
                            contentDescription = null,
                            tint = if (selectedTab == "Quiero repartir") Color.White else EnverTextMuted,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Quiero repartir",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (selectedTab == "Quiero repartir") Color.White else EnverTextMuted
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- SUB-FILTRO: LO MÁS SOLICITADO / MÁS RECIENTE (VERDE TENUE) ---
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedFilter == "Lo más solicitado",
                    onClick = { selectedFilter = "Lo más solicitado" },
                    label = { Text("🔥 Lo más solicitado", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) },
                    shape = RoundedCornerShape(20.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = Color.White,
                        selectedContainerColor = EnverSubtleGreenBg,
                        selectedLabelColor = EnverDarkGreen,
                        labelColor = EnverTextMuted
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = selectedFilter == "Lo más solicitado",
                        borderColor = Color(0xFFE2E8F0),
                        selectedBorderColor = EnverGreen
                    )
                )
                FilterChip(
                    selected = selectedFilter == "Más reciente",
                    onClick = { selectedFilter = "Más reciente" },
                    label = { Text("🕒 Más reciente", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) },
                    shape = RoundedCornerShape(20.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = Color.White,
                        selectedContainerColor = EnverSubtleGreenBg,
                        selectedLabelColor = EnverDarkGreen,
                        labelColor = EnverTextMuted
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = selectedFilter == "Más reciente",
                        borderColor = Color(0xFFE2E8F0),
                        selectedBorderColor = EnverGreen
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // --- BLOQUE DE PEDIDOS (APILADOS; el espacio es el mismo para ambos filtros) ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val visibleOrders = if (selectedFilter == "Más reciente") recentOrders else mostRequestedOrders
            if (visibleOrders.isEmpty()) {
                Text(
                    text = if (selectedFilter == "Más reciente") "No hay pedidos recientes" else "No hay pedidos",
                    fontSize = 12.sp,
                    color = EnverTextMuted,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    textAlign = TextAlign.Center
                )
            } else {
                visibleOrders.forEach { item ->
                    OrderCardVerticalFullWidth(order = item)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // --- BANNER DE EMERGENCIA ULTRA EXPRESS ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .background(
                    color = Color(0xFF047857),
                    shape = RoundedCornerShape(24.dp)
                )
                .padding(20.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = Color(0xFF065F46),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "⚡", fontSize = 10.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "ULTRA EXPRESS",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    Text(
                        text = "Entrega prom: 12 min",
                        fontSize = 11.sp,
                        color = Color(0xFFA7F3D0)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "¿Emergencia en plena\nclase?",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    lineHeight = 26.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Lanzamos tu aviso a +180 compañeros\nque están caminando por tu edificio.",
                    fontSize = 12.sp,
                    color = Color(0xFFD1FAE5),
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onEmergencyClick,
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    modifier = Modifier.height(44.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "⚡", fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Pedir auxilio",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF047857)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // --- SECCIÓN MIS PEDIDOS ACTIVOS ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Mis pedidos activos",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .background(EnverGreen, CircleShape)
                )
            }

            Text(
                text = "+14 activos",
                fontSize = 12.sp,
                color = EnverTextMuted,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- ESTADO DE PEDIDOS ACTIVOS (el espacio se conserva aunque esté vacío) ---
        Surface(
            color = Color.White,
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Text(
                text = "No hay pedidos",
                fontSize = 12.sp,
                color = EnverTextMuted,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 18.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // --- BANNER VERIFICACIÓN DE ESTUDIANTES ---
        Surface(
            color = Color.White,
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.VerifiedUser,
                    contentDescription = null,
                    tint = EnverDarkGreen,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "100% Estudiantes Verificados",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B)
                    )
                    Text(
                        text = "Solo usuarios con correo @tec.mx o credencial oficial.",
                        fontSize = 10.sp,
                        color = EnverTextMuted
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

// --- TARJETA VERTICAL DE ANCHO COMPLETO (APILADA) ---
@Composable
fun OrderCardVerticalFullWidth(order: OrderItem) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = EnverCardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Mitad Superior: Imagen del artículo
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(Color(0xFFCBD5E1))
            ) {
                order.imageResId?.let { resId ->
                    EnverImage(
                        source = ImageSource.Local(resId),
                        contentDescription = order.title,
                        modifier = Modifier.matchParentSize()
                    )
                }
                Surface(
                    color = Color.White.copy(alpha = 0.9f),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .padding(12.dp)
                        .align(Alignment.TopEnd)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "🏷️", fontSize = 10.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = order.category,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E293B)
                        )
                    }
                }
            }

            // Contenido inferior
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "ARTÍCULOS SOLICITADOS",
                    fontSize = 9.sp,
                    color = EnverTextMuted,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = order.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (order.is100PercentYours) "TU GANANCIA TOTAL" else "COSTO",
                            fontSize = 9.sp,
                            color = EnverTextMuted,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = order.cost,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = EnverGreen
                        )
                    }

                    Button(
                        onClick = { },
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = EnverGreen),
                        modifier = Modifier.height(38.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) {
                        Text(
                            text = "Ver detalles →",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                if (order.is100PercentYours) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        color = EnverBadgeBg,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "✔ 100% tuya - Sin comisión de app",
                            fontSize = 9.sp,
                            color = EnverDarkGreen,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}
