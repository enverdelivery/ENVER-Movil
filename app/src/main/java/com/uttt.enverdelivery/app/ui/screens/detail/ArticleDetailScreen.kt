package com.uttt.enverdelivery.app.ui.screens.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uttt.enverdelivery.app.ui.components.media.EnverImage
import com.uttt.enverdelivery.app.ui.components.media.ImageSource
import com.uttt.enverdelivery.app.ui.screens.home.SampleArticles
import com.uttt.enverdelivery.app.ui.screens.pedidos.PedidosLocalState
import com.uttt.enverdelivery.app.ui.theme.EnverBadgeBg
import com.uttt.enverdelivery.app.ui.theme.EnverBackground
import com.uttt.enverdelivery.app.ui.theme.EnverCardBg
import com.uttt.enverdelivery.app.ui.theme.EnverDarkGreen
import com.uttt.enverdelivery.app.ui.theme.EnverGreen
import com.uttt.enverdelivery.app.ui.theme.EnverTextMuted
import com.uttt.enverdelivery.app.ui.theme.EnverTheme

// Detalle de artículo (FT-02): push full-screen desde la tarjeta de Home.
// Reutiliza exclusivamente los datos del OrderItem existente (SampleArticles).
// "Pedir" (FT-04): registra el artículo como pedido local en memoria (demo
// provisional, sin backend) y navega a la sección Pedidos vía onPedirClick.
// Sin encabezado global ni barra inferior: navegación push.
// Reutiliza exclusivamente los datos del OrderItem existente (SampleArticles);
// "Pedir" NO ejecuta ninguna acción (el flujo real de pedidos llega en un
// slice futuro). Sin encabezado global ni barra inferior: navegación push.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleDetailScreen(
    articleId: String,
    onBackClick: () -> Unit,
    onPedirClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val order = SampleArticles.byId(articleId)
    val scrollState = rememberScrollState()

    Scaffold(
        containerColor = EnverBackground,
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
        ) {
            // Barra superior propia del detalle: botón de volver + título
            TopAppBar(
                title = {
                    Text(
                        text = "Detalle",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .size(38.dp)
                            .background(Color.White, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color(0xFF0F172A)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = EnverBackground)
            )

            if (order == null) {
                // Ruta con id inexistente: estado robusto sin inventar datos
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(120.dp))
                    Text(
                        text = "Artículo no disponible",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = onBackClick,
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = EnverGreen)
                    ) {
                        Text(
                            text = "Volver",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
                return@Scaffold
            }

            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Spacer(modifier = Modifier.height(8.dp))

                // Imagen del artículo (igual que la tarjeta: placeholder si no resId)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .background(Color(0xFFCBD5E1), RoundedCornerShape(24.dp))
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
                        Text(
                            text = order.category,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E293B),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Nombre
                Text(
                    text = order.title,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Badge "100% tuyo" solo cuando corresponda
                if (order.is100PercentYours) {
                    Surface(
                        color = EnverBadgeBg,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "100% tuyo",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = EnverDarkGreen,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                }

                // Costo con el formato actual de OrderItem.cost (sin reformatear)
                Text(
                    text = "COSTO",
                    fontSize = 9.sp,
                    color = EnverTextMuted,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = order.cost,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = EnverGreen
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Datos del pedido existentes: fila omitida si el campo es null
                Surface(
                    color = EnverCardBg,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        order.requesterName?.let { requester ->
                            DetailDataRow(label = "Solicitante", value = requester)
                        }
                        order.rating?.let { rating ->
                            DetailDataRow(label = "Calificación", value = rating)
                        }
                        order.faculty?.let { faculty ->
                            DetailDataRow(label = "Facultad", value = faculty)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Botón principal: registra el pedido local con los datos reales del
                // artículo (dedup por id: el 2.º toque no duplica) y lleva a Pedidos
                Button(
                    onClick = {
                        PedidosLocalState.pedir(order)
                        onPedirClick()
                    },
                // Botón principal: visible en pantalla, SIN acción funcional
                Button(
                    onClick = { },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = EnverGreen),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text(
                        text = "Pedir",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

// Fila etiqueta/valor reutilizada dentro del bloque de datos
@Composable
private fun DetailDataRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            color = EnverTextMuted,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = value,
            fontSize = 11.sp,
            color = Color(0xFF0F172A),
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ArticleDetailScreenPreview() {
    EnverTheme {
        ArticleDetailScreen(articleId = "2", onBackClick = {})
    }
}
