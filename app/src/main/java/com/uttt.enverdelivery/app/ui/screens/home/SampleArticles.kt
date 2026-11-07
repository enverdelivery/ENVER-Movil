package com.uttt.enverdelivery.app.ui.screens.home

import com.uttt.enverdelivery.app.R

/**
 * Fuente unica de datos demo para los articulos que Home ya mostraba.
 * Extraida de HomeScreen en FT-02 para que el detalle pueda recuperar un
 * articulo por id sin duplicar la lista. Los datos son EXACTAMENTE los mismos
 * que existian (Checkpoint 4.1, alcance aprobado).
 */
object SampleArticles {
    // Pedidos existentes por sub-filtro (cada espacio se muestra segun el selector activo)
    val mostRequested: List<OrderItem> = listOf(
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

    // Aun no se entregan datos de pedidos recientes: el espacio se conserva vacio
    val recentOrders: List<OrderItem> = emptyList()

    fun byId(id: String): OrderItem? = mostRequested.firstOrNull { it.id == id }
}
