package com.uttt.enverdelivery.app.data

// Punto de integración de autenticación (FT-06). El contrato real de AWS AÚN
// NO existe: aquí NO se inventan endpoints, cuerpos, tokens ni sesiones.
// Cuando el contrato llegue, se implementa esta interfaz con el cliente HTTP
// real (DTOs según contrato) sin cambiar la UI.
sealed class ResultadoAutenticacion {
    data object Exito : ResultadoAutenticacion()
    data class Error(val mensaje: String) : ResultadoAutenticacion()
}

interface AuthRepository {
    suspend fun iniciarSesion(correo: String, contrasena: String): ResultadoAutenticacion
    suspend fun registrarCuenta(
        nombreCompleto: String,
        correo: String,
        telefono: String,
        contrasena: String
    ): ResultadoAutenticacion
}

// Estado actual: sin baseUrl/endpoints confirmados → las operaciones devuelven
// error honesto; la UI NO debe interpretarlo como éxito ni navegar por él.
class AuthRepositoryPendienteDeContrato : AuthRepository {
    override suspend fun iniciarSesion(correo: String, contrasena: String): ResultadoAutenticacion =
        ResultadoAutenticacion.Error(
            "La integración con el servidor todavía no está disponible."
        )

    override suspend fun registrarCuenta(
        nombreCompleto: String,
        correo: String,
        telefono: String,
        contrasena: String
    ): ResultadoAutenticacion = ResultadoAutenticacion.Error(
        "La creación de cuentas se habilitará cuando se integre el servidor."
    )
}
