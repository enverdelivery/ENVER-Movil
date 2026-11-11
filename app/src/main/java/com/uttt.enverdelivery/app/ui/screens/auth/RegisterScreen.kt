package com.uttt.enverdelivery.app.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.platform.LocalContext
import com.uttt.enverdelivery.app.data.AuthRepositoryPendienteDeContrato
import kotlinx.coroutines.launch
import com.uttt.enverdelivery.app.data.ResultadoAutenticacion
import com.uttt.enverdelivery.app.ui.theme.EnverBackground
import com.uttt.enverdelivery.app.ui.theme.EnverBadgeBg
import com.uttt.enverdelivery.app.ui.theme.EnverDarkGreen
import com.uttt.enverdelivery.app.ui.theme.EnverGreen
import com.uttt.enverdelivery.app.ui.theme.EnverInputBg
import com.uttt.enverdelivery.app.ui.theme.EnverTextMuted

class Country(val label: String, val emoji: String, val dialCode: String)

// Aviso de privacidad oficial del proyecto (Checkpoint 4.7): única URL permitida.
const val AVISO_PRIVACIDAD_URL = "https://enverdelivery.github.io/AvisoDePrivacidad/"

private val COUNTRIES = listOf(
    Country("México", "🇲🇽", "+52"),
    Country("Estados Unidos", "🇺🇸", "+1"),
    Country("Canadá", "🇨🇦", "+1"),
    Country("Brasil", "🇧🇷", "+55"),
    Country("Argentina", "🇦🇷", "+54"),
    Country("Colombia", "🇨🇴", "+57"),
    Country("España", "🇪🇸", "+34"),
    Country("Reino Unido", "🇬🇧", "+44"),
    Country("Alemania", "🇩🇪", "+49"),
    Country("Francia", "🇫🇷", "+33")
)

@Composable
fun RegisterScreen(
    onBackClick: () -> Unit = {},
    onSubmitClick: () -> Unit = {},
    onLoginRedirectClick: () -> Unit = {}
) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var termsAccepted by remember { mutableStateOf(false) }
    // FT-06: indicador dinámico + estado honesto de integración pendiente.
    val nivelSeguridad = ValidacionEntrada.evaluarNivelDeSeguridad(password)
    var mensajeEstado by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val authRepository = remember { AuthRepositoryPendienteDeContrato() }
    val contexto = LocalContext.current

    var selectedCountry by remember { mutableStateOf(COUNTRIES.first()) }
    var countryMenuExpanded by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(EnverBackground)
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Spacer(modifier = Modifier.height(16.dp))


                // --- HEADER: BOTÓN ATRÁS, PASO Y AYUDA ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .size(38.dp)
                            .background(Color.White, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = "Regresar",
                            tint = Color(0xFF1E293B),
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Indicador de Paso
                    Surface(
                        color = Color(0xFFE2E8F0),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .background(EnverGreen, CircleShape)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Paso 1 de 2 · Datos Básicos",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF334155)
                            )
                        }
                    }

                    IconButton(
                        onClick = { /* Acción de Ayuda */ },
                        modifier = Modifier
                            .size(38.dp)
                            .background(Color.White, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.HelpOutline,
                            contentDescription = "Ayuda",
                            tint = Color(0xFF1E293B),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // --- TÍTULO Y SUBTÍTULO ---
                Text(
                    text = "Registro de Estudiante",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF0F172A)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Tu cuenta te permite solicitar pedidos\ninmediatamente dentro del campus.",
                    fontSize = 12.sp,
                    color = EnverTextMuted,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                // --- CAMPO 1: NOMBRE COMPLETO ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.PersonOutline,
                            contentDescription = null,
                            tint = EnverGreen,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Nombre completo",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = Color(0xFF1E293B)
                        )
                    }
                    Text(
                        text = "Como figura en tu credencial",
                        fontSize = 10.sp,
                        color = EnverTextMuted
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    placeholder = { Text("ej. Mateo Alarcón", color = Color(0xFF94A3B8), fontSize = 13.sp) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Badge,
                            contentDescription = null,
                            tint = EnverTextMuted
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = EnverInputBg,
                        unfocusedContainerColor = EnverInputBg,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(14.dp))

                // --- CAMPO 2: CORREO INSTITUCIONAL ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.Email,
                            contentDescription = null,
                            tint = EnverGreen,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Correo institucional",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = Color(0xFF1E293B)
                        )
                    }
                    // Badge de validación
                    Surface(
                        color = EnverBadgeBg,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "✔", fontSize = 9.sp, color = EnverDarkGreen)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Se validará matrícula",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = EnverDarkGreen
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = { Text("ej. mateo.alarcon@campus.edu", color = Color(0xFF94A3B8), fontSize = 13.sp) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.MailOutline,
                            contentDescription = null,
                            tint = EnverTextMuted
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = EnverInputBg,
                        unfocusedContainerColor = EnverInputBg,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "🔒 Te enviaremos un pin rápido de activación universitaria.",
                    fontSize = 10.sp,
                    color = EnverTextMuted
                )

                Spacer(modifier = Modifier.height(14.dp))

                // --- CAMPO 3: WHATSAPP / TELÉFONO MÓVIL ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.PhoneAndroid,
                            contentDescription = null,
                            tint = EnverGreen,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "WhatsApp / Teléfono móvil",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = Color(0xFF1E293B)
                        )
                    }
                    Text(
                        text = "Avisos de entrega",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFD97706) // Tono ámbar/dorado de la imagen
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Selector de país con menú desplegable
                    Box {
                        Surface(
                            color = EnverInputBg,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .width(85.dp)
                                .height(56.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .clickable { countryMenuExpanded = true }
                                    .padding(horizontal = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(text = selectedCountry.emoji, fontSize = 14.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = selectedCountry.dialCode, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                                Text(text = " ▾", fontSize = 10.sp, color = EnverTextMuted)
                            }
                        }
                        DropdownMenu(
                            expanded = countryMenuExpanded,
                            onDismissRequest = { countryMenuExpanded = false }
                        ) {
                            COUNTRIES.forEach { country ->
                                DropdownMenuItem(
                                    text = {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(text = country.emoji, fontSize = 16.sp)
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = "${country.dialCode} ${country.label}",
                                                fontSize = 13.sp,
                                                color = Color(0xFF1E293B)
                                            )
                                        }
                                    },
                                    onClick = {
                                        selectedCountry = country
                                        countryMenuExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        placeholder = { Text("55 1234 5678", color = Color(0xFF94A3B8), fontSize = 13.sp) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.ChatBubbleOutline,
                                contentDescription = null,
                                tint = EnverTextMuted,
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color(0xFF0F172A),
                            unfocusedTextColor = Color(0xFF0F172A),
                            cursorColor = EnverGreen,
                            focusedContainerColor = EnverInputBg,
                            unfocusedContainerColor = EnverInputBg,
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // --- CAMPO 4: CONTRASEÑA SEGURA ---
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Lock,
                        contentDescription = null,
                        tint = EnverGreen,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Contraseña segura",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = Color(0xFF1E293B)
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = { Text("🔑 Mínimo 8 caracteres", color = Color(0xFF94A3B8), fontSize = 13.sp) },
                    trailingIcon = {
                        IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                            Icon(
                                imageVector = if (isPasswordVisible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
                                contentDescription = null,
                                tint = EnverTextMuted
                            )
                        }
                    },
                    visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = EnverInputBg,
                        unfocusedContainerColor = EnverInputBg,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Medidor de seguridad de contraseña (4 barritas, dinámico FT-06)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Nivel de seguridad", fontSize = 10.sp, color = EnverTextMuted, fontWeight = FontWeight.SemiBold)
                    Text(
                        text = ValidacionEntrada.etiquetaDeNivel(nivelSeguridad),
                        fontSize = 10.sp,
                        color = if (nivelSeguridad >= 3) EnverDarkGreen else Color(0xFFD97706),
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    repeat(4) { barra ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(4.dp)
                                .background(
                                    if (barra < nivelSeguridad) EnverGreen else Color(0xFFE2E8F0),
                                    RoundedCornerShape(2.dp)
                                )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // --- CHECKBOX DE TÉRMINOS Y CONDICIONES ---
                Row(
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(
                        checked = termsAccepted,
                        onCheckedChange = { termsAccepted = it },
                        colors = CheckboxDefaults.colors(checkedColor = EnverGreen),
                        modifier = Modifier.size(20.dp).padding(end = 4.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Acepto los Términos y Condiciones y el Código de Honor de convivencia y auxilio estudiantil en el campus.",
                            fontSize = 11.sp,
                            color = Color(0xFF334155),
                            lineHeight = 14.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        // FT-06: sólo ABRE el aviso en el navegador (consultar);
                        // la aceptación es la casilla, no la lectura de la página.
                        Text(
                            text = "🌐 Consultar Aviso de Privacidad Estudiantil",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = EnverDarkGreen,
                            modifier = Modifier.clickable {
                                val intento = android.content.Intent(
                                    android.content.Intent.ACTION_VIEW,
                                    android.net.Uri.parse(AVISO_PRIVACIDAD_URL)
                                )
                                contexto.startActivity(
                                    android.content.Intent.createChooser(
                                        intento,
                                        "Abrir Aviso de Privacidad"
                                    )
                                )
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // --- BOTÓN PRINCIPAL Y FOOTER INFERIOR ---
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        mensajeEstado = when {
                            !ValidacionEntrada.noVacio(fullName) ->
                                "Escribe tu nombre completo."
                            !ValidacionEntrada.esEmailValido(email) ->
                                "Escribe un correo electrónico válido."
                            !ValidacionEntrada.esTelefonoValido(phone) ->
                                "Escribe un teléfono móvil válido (8 a 15 dígitos)."
                            !ValidacionEntrada.noVacio(password) ->
                                "Escribe una contraseña."
                            !termsAccepted ->
                                "Debes aceptar el Aviso de Privacidad y los Términos para crear tu cuenta."
                            else -> ""
                        }
                        if (mensajeEstado.isEmpty()) {
                            // Registro localmente válido; el resultado real lo dará el
                            // servidor (pendiente de contrato): hoy el repositorio
                            // devuelve el aviso honesto y NO se navega a éxito simulado.
                            scope.launch {
                                val resultado = authRepository.registrarCuenta(
                                    fullName.trim(),
                                    email.trim(),
                                    phone.trim(),
                                    password
                                )
                                if (resultado is ResultadoAutenticacion.Error) {
                                    mensajeEstado = resultado.mensaje
                                } else {
                                    // Exito real (futuro): se navegará vía onSubmitClick.
                                    mensajeEstado = ""
                                    onSubmitClick()
                                }
                            }
                        }
                    },
                    shape = RoundedCornerShape(25.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = EnverGreen),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(
                        text = "Crear mi Cuenta Universitaria",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                // Mensaje de estado honesto (validación local / integración pendiente)
                if (mensajeEstado.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = mensajeEstado,
                        fontSize = 11.sp,
                        color = Color(0xFFDC2626),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "¿Ya tienes tu cuenta ENVER activa? ",
                        fontSize = 12.sp,
                        color = EnverTextMuted
                    )
                    TextButton(
                        onClick = onLoginRedirectClick,
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = "Inicia Sesión",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = EnverDarkGreen
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegisterScreenPreview() {
    RegisterScreen()
}
