package com.uttt.enverdelivery.app.ui.components.media

import com.uttt.enverdelivery.app.R

sealed interface ImageSource {
    data class Local(val resId: Int) : ImageSource
    data class Remote(val url: String) : ImageSource

    companion object {
        val ORDER_CALCULADORA_CASIO = Local(R.drawable.articulo_calculadora_casio)
        val ORDER_CARTULINA_PLUMON = Local(R.drawable.articulo_cartulina_plumon)
    }
}
