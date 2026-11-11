package com.uttt.enverdelivery.app

import com.uttt.enverdelivery.app.ui.screens.auth.ValidacionEntrada
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ValidacionEntradaTest {

    // --- Correo ---
    @Test
    fun email_valido_acepta_formatos_basicos() {
        assertTrue(ValidacionEntrada.esEmailValido("usuario@enver.mx"))
        assertTrue(ValidacionEntrada.esEmailValido("a.b+c@sub.dominio.org"))
    }

    @Test
    fun email_invalido_rechaza_formatos_incompletos() {
        assertFalse(ValidacionEntrada.esEmailValido("sin-arroba.com"))
        assertFalse(ValidacionEntrada.esEmailValido("sin@dominio"))
        assertFalse(ValidacionEntrada.esEmailValido(""))
        assertFalse(ValidacionEntrada.esEmailValido("   "))
    }

    // --- Teléfono (regla provisional 8-15 dígitos) ---
    @Test
    fun telefono_valido_acepta_10_digitos_con_separadores() {
        assertTrue(ValidacionEntrada.esTelefonoValido("5512345678"))
        assertTrue(ValidacionEntrada.esTelefonoValido("55-1234 5678"))
    }

    @Test
    fun telefono_invalido_rechaza_largos_y_letras() {
        assertFalse(ValidacionEntrada.esTelefonoValido("1234567"))
        assertFalse(ValidacionEntrada.esTelefonoValido("1234567890123456"))
        assertFalse(ValidacionEntrada.esTelefonoValido("55abc123"))
        assertFalse(ValidacionEntrada.esTelefonoValido(""))
    }

    // --- Contraseña: nivel visual (indicador; validación final = servidor) ---
    @Test
    fun nivel_0_con_contrasena_vacia() {
        assertEquals(0, ValidacionEntrada.evaluarNivelDeSeguridad(""))
    }

    @Test
    fun nivel_crece_con_longitud_variedad() {
        assertTrue(ValidacionEntrada.evaluarNivelDeSeguridad("abc") <
            ValidacionEntrada.evaluarNivelDeSeguridad("Abcdef1!"))
        assertEquals(4, ValidacionEntrada.evaluarNivelDeSeguridad("Abcdefgh1234!"))
    }

    @Test
    fun etiquetas_corresponden_al_nivel() {
        assertEquals("Vacía", ValidacionEntrada.etiquetaDeNivel(0))
        assertEquals("Media", ValidacionEntrada.etiquetaDeNivel(2))
        assertEquals("Muy alta", ValidacionEntrada.etiquetaDeNivel(4))
    }

    // --- Campo vacío ---
    @Test
    fun noVacio_rechaza_blancos() {
        assertFalse(ValidacionEntrada.noVacio("   "))
        assertTrue(ValidacionEntrada.noVacio("Nombre"))
    }
}
