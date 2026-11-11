package com.uttt.enverdelivery.app.ui.screens.pedidos

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
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uttt.enverdelivery.app.ui.components.media.EnverImage
import com.uttt.enverdelivery.app.ui.components.media.ImageSource
import com.uttt.enverdelivery.app.ui.screens.home.OrderItem
import com.uttt.enverdelivery.app.ui.theme.EnverBadgeBg
import com.uttt.enverdelivery.app.ui.theme.EnverCardBg
import com.uttt.enverdelivery.app.ui.theme.EnverDarkGreen
import com.uttt.enverdelivery.app.ui.theme.EnverTextMuted

// Sección "Pedidos" del MainScaffold (FT-03 estado vacío honesto).
// FT-04: mientras no existan pedidos reales se conserva el estado vacío;
// si hay pedidos registrados localmente (demo provisional), se muestran en lista.
@Composable
fun PedidosScreen(modifier: Modifier = Modifier) {
    if (PedidosLocalState.pedidos.isEmpty()) {
        PedidosEstadoVacio(modifier = modifier)
    } else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Mis pedidos",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A)
            )

            Spacer(modifier = Modifier.height(16.dp))

                PedidosLocalState.pedidos.forEach { pedido ->
                    PedidoRow(pedido = pedido)
                    Spacer(modifier = Modifier.height(10.dp))
                }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Pedido registrado localmente (demo provisional, sin conexión a servidor).",
                fontSize = 11.sp,
                color = EnverTextMuted,
                modifier = Modifier.padding(bottom = 24.dp)
            )
        }
    }
}

// Fila de pedido con los datos reales del artículo pedido (sin pedidos inventados).
@Composable
private fun PedidoRow(pedido: OrderItem) {
    Surface(
        color = EnverCardBg,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(Color(0xFFCBD5E1), RoundedCornerShape(12.dp))
            ) {
                pedido.imageResId?.let { resId ->
                    EnverImage(
                        source = ImageSource.Local(resId),
                        contentDescription = pedido.title,
                        modifier = Modifier.matchParentSize()
                    )
                }
            }

            Spacer(modifier = Modifier.size(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = pedido.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF0F172A)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Solicitado",
                    fontSize = 11.sp,
                    color = EnverTextMuted
                )
            }

            Text(
                text = pedido.cost,
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold,
                color = EnverDarkGreen
            )
        }
    }
}

// Estado vacío original (FT-03), sin cambios respecto a lo aprobado.
@Composable
private fun PedidosEstadoVacio(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Mis pedidos",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                color = EnverCardBg,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp, horizontal = 16.dp)
                ) {
                    Surface(
                        color = EnverBadgeBg,
                        shape = CircleShape
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Receipt,
                            contentDescription = null,
                            tint = EnverDarkGreen,
                            modifier = Modifier
                                .padding(14.dp)
                                .size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Todavía no tienes pedidos",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF0F172A)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Cuando solicites un artículo aparecerá aquí.",
                        fontSize = 12.sp,
                        color = EnverTextMuted,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                }
            }
        }
    }
}
