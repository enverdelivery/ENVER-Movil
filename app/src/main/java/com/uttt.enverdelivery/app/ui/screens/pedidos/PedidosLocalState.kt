package com.uttt.enverdelivery.app.ui.screens.pedidos

import androidx.compose.runtime.mutableStateListOf
import com.uttt.enverdelivery.app.ui.screens.home.OrderItem

// Estado local en memoria (demo provisional FT-04, sin backend ni persistencia):
// pedidos hechos al pulsar "Pedir" en el detalle. Un pedido por artículo (dedup
// por id de artículo) para bloquear duplicados por pulsaciones accidentales.
// Se pierde al reiniciar el proceso — la UI lo declara como demo provisional.
object PedidosLocalState {
    val pedidos = mutableStateListOf<OrderItem>()

    // Idempotente: si el artículo ya fue pedido, no registra nada (devuelve false).
    fun pedir(order: OrderItem): Boolean {
        if (pedidos.any { it.id == order.id }) return false
        pedidos.add(order)
        return true
    }
}
