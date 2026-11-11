package com.uttt.enverdelivery.app.ui.screens.auth

// Validación local PROVISIONAL (FT-06): sólo vacíos y formatos básicos.
// Las reglas definitivas las definirá el contrato del backend (pendiente);
// aquí no se imponen restricciones de dominio inventadas.
object ValidacionEntrada {
    private val emailRegex = Regex("^[A-Za-z0-9+._-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    private val telefonoRegex = Regex("^[0-9]{8,15}$")

    fun noVacio(valor: String): Boolean = valor.isNotBlank()

    fun esEmailValido(email: String): Boolean =
        email.isNotBlank() && emailRegex.matches(email.trim())

    // 8-15 dígitos (posibles espacios/guiones intermedios ignorados); regla
    // provisional: se ajustará a la política del backend cuando exista.
    fun esTelefonoValido(telefono: String): Boolean {
        val limpio = telefono.trim().replace(Regex("[\\s-]"), "")
        return telefonoRegex.matches(limpio)
    }

    // Nivel visual 0..4 (provisional, sólo indicación al usuario; la validación
    // final de contraseña corresponde al servidor). 0 = vacía.
    fun evaluarNivelDeSeguridad(password: String): Int {
        if (password.isEmpty()) return 0
        var nivel = 0
        if (password.length >= 8) nivel++
        if (password.length >= 12) nivel++
        if (password.any { it.isDigit() }) nivel++
        if (password.any { it.isLetterOrDigit().not() } && password.any { it.isLetter() }) nivel++
        return nivel
    }

    fun etiquetaDeNivel(nivel: Int): String = when (nivel) {
        0 -> "Vacía"
        1 -> "Baja"
        2 -> "Media"
        3 -> "Alta"
        else -> "Muy alta"
    }
}
