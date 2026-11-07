package com.uttt.enverdelivery.app.ui.screens.createorder

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.uttt.enverdelivery.app.ui.theme.EnverBadgeBg
import com.uttt.enverdelivery.app.ui.theme.EnverCardBg
import com.uttt.enverdelivery.app.ui.theme.EnverDarkGreen
import com.uttt.enverdelivery.app.ui.theme.EnverGreen
import com.uttt.enverdelivery.app.ui.theme.EnverTextMuted
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Estado compartido de todo el flujo "Crear pedido" (conserva datos al avanzar/regresar
// entre pasos y entre cambios de sección, mientras la app permanece viva)
class CreateOrderUiState {
    var selectedCategory by mutableStateOf("Comida")
    var orderTitle by mutableStateOf("")
    var specialInstructions by mutableStateOf("")
    var tarifa by mutableStateOf("")
    var photoPath by mutableStateOf<String?>(null)

    // Paso 2: ubicación. Los campos inician vacíos con placeholder (igual que Login/Registro).
    var locationSearch by mutableStateOf("")
    var locationDetail by mutableStateOf("")
    var selectedLat by mutableStateOf<Double?>(null)
    var selectedLng by mutableStateOf<Double?>(null)
    var selectedAddressLabel by mutableStateOf<String?>(null)
    var selectedHabitualPlace by mutableStateOf<String?>(null)

    // Paso 3: tiempo de entrega deseado
    var deliveryTime by mutableStateOf<String?>(null)
}

private const val TOTAL_STEPS = 4

// Contenedor de la sección Nuevo pedido: 4 pasos consecutivos en una sola sección del menú
@Composable
fun CreateOrderFlowScreen(
    uiState: CreateOrderUiState,
    modifier: Modifier = Modifier
) {
    var currentStep by remember { mutableStateOf(1) }

    when (currentStep) {
        1 -> CreateOrderStep1Content(
            uiState = uiState,
            onContinueClick = { currentStep = 2 },
            modifier = modifier
        )
        2 -> CreateOrderStep2Content(
            uiState = uiState,
            onBackClick = { currentStep = 1 },
            onContinueClick = { currentStep = 3 },
            modifier = modifier
        )
        3 -> CreateOrderStep3Content(
            uiState = uiState,
            onBackClick = { currentStep = 2 },
            onContinueClick = { currentStep = 4 },
            modifier = modifier
        )
        4 -> CreateOrderStepPlaceholder(
            stepNumber = 4,
            nextStepNumber = null,
            onBackClick = { currentStep = 3 },
            modifier = modifier
        )
    }
}

@Composable
fun CreateOrderStep1Content(
    uiState: CreateOrderUiState,
    onContinueClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var titleError by remember { mutableStateOf(false) }
    var detailsError by remember { mutableStateOf(false) }
    var tarifaError by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .imePadding()
            .padding(20.dp)
    ) {
        // --- INDICADOR PASO 1 DE 4 (bloque ancho reutilizable; el logo/campus lo aporta el encabezado global) ---
        OrderStepProgressHeader(currentStep = 1)

        Spacer(modifier = Modifier.height(20.dp))

        // --- TÍTULO PRINCIPAL ---
        Text(
            text = "¿Qué necesitas salvar hoy?",
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF0F172A)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // --- SECCIÓN: CATEGORÍA ---
        Text(
            text = "CATEGORÍA",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = EnverTextMuted,
            letterSpacing = 0.5.sp
        )
        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CategoryChip(
                title = "Papelería",
                icon = "📄",
                isSelected = uiState.selectedCategory == "Papelería",
                onClick = { uiState.selectedCategory = "Papelería" },
                modifier = Modifier.weight(1f)
            )
            CategoryChip(
                title = "Comida",
                icon = "🍔",
                isSelected = uiState.selectedCategory == "Comida",
                onClick = { uiState.selectedCategory = "Comida" },
                modifier = Modifier.weight(1f)
            )
            CategoryChip(
                title = "Tecnología",
                icon = "💻",
                isSelected = uiState.selectedCategory == "Tecnología",
                onClick = { uiState.selectedCategory = "Tecnología" },
                modifier = Modifier.weight(1f)
            )
            CategoryChip(
                title = "Otros",
                icon = "📦",
                isSelected = uiState.selectedCategory == "Otros",
                onClick = { uiState.selectedCategory = "Otros" },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // --- CAMPO OBLIGATORIO: TÍTULO DEL PEDIDO (¿QUÉ TE URGE?) ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "¿Qué te urge?",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E293B)
            )
            Text(
                text = "Sé específico · Obligatorio",
                fontSize = 10.sp,
                color = EnverTextMuted
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = uiState.orderTitle,
            onValueChange = {
                uiState.orderTitle = it
                if (it.isNotBlank()) titleError = false
            },
            placeholder = {
                Text(
                    "Ej. Cable cargador tipo C de 2 metros o un sándwich de pollo de la cafetería central...",
                    fontSize = 12.sp,
                    color = Color(0xFF94A3B8)
                )
            },
            isError = titleError,
            supportingText = {
                if (titleError) Text("El título del pedido es obligatorio", color = Color(0xFFDC2626), fontSize = 10.sp)
            },
            shape = RoundedCornerShape(16.dp),
            colors = createOrderInputColors(isError = titleError),
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // --- CAMPO OBLIGATORIO: DETALLES / INSTRUCCIONES ESPECIALES ---
        Text(
            text = "Detalles / Instrucciones especiales",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1E293B)
        )
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = uiState.specialInstructions,
            onValueChange = {
                uiState.specialInstructions = it
                if (it.isNotBlank()) detailsError = false
            },
            placeholder = {
                Text(
                    "Si compras en la cafetería, sin mayonesa por favor",
                    fontSize = 12.sp,
                    color = Color(0xFF94A3B8)
                )
            },
            isError = detailsError,
            supportingText = {
                if (detailsError) Text("Los detalles del pedido son obligatorios", color = Color(0xFFDC2626), fontSize = 10.sp)
            },
            shape = RoundedCornerShape(16.dp),
            colors = createOrderInputColors(isError = detailsError),
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // --- CAMPO OBLIGATORIO: TARIFA (NUMÉRICO, CON $ Y SIN NEGATIVOS) ---
        Text(
            text = "Establece tu tarifa",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1E293B)
        )
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = uiState.tarifa,
            onValueChange = { raw ->
                val sanitized = sanitizeTarifa(raw)
                uiState.tarifa = sanitized
                if (isValidTarifa(sanitized)) tarifaError = false
            },
            placeholder = {
                Text(
                    "Recuerda contemplar el precio del producto",
                    fontSize = 12.sp,
                    color = Color(0xFF94A3B8)
                )
            },
            leadingIcon = {
                Text(
                    text = "$",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B),
                    modifier = Modifier.padding(start = 14.dp)
                )
            },
            isError = tarifaError,
            supportingText = {
                if (tarifaError) Text("Ingresa una tarifa válida mayor a $0.00", color = Color(0xFFDC2626), fontSize = 10.sp)
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            colors = createOrderInputColors(isError = tarifaError),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // --- FOTO O APUNTE DE REFERENCIA (OPCIONAL) ---
        Text(
            text = "Foto o apunte de referencia (opcional)",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1E293B)
        )
        Spacer(modifier = Modifier.height(8.dp))
        OrderPhotoSection(
            photoPath = uiState.photoPath,
            onPhotoTaken = { uiState.photoPath = it },
            onPhotoRemoved = { uiState.photoPath = null }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // --- BOTÓN CONTINUAR A UBICACIÓN (VALIDA CAMPOS OBLIGATORIOS) ---
        Button(
            onClick = {
                val titleOk = uiState.orderTitle.isNotBlank()
                val detailsOk = uiState.specialInstructions.isNotBlank()
                val tarifaOk = isValidTarifa(uiState.tarifa)
                if (titleOk && detailsOk && tarifaOk) {
                    onContinueClick()
                } else {
                    titleError = !titleOk
                    detailsError = !detailsOk
                    tarifaError = !tarifaOk
                }
            },
            shape = RoundedCornerShape(25.dp),
            colors = ButtonDefaults.buttonColors(containerColor = EnverGreen),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Continuar a Ubicación",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "→", fontSize = 18.sp, color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

// --- CHIP DE CATEGORÍA ---
@Composable
private fun CategoryChip(
    title: String,
    icon: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        color = if (isSelected) EnverGreen else EnverCardBg,
        shape = RoundedCornerShape(16.dp),
        border = if (isSelected) null else BorderStroke(1.dp, Color(0xFFE2E8F0)),
        modifier = modifier.height(72.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = icon, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) Color.White else Color(0xFF1E293B)
            )
        }
    }
}

// --- FOTO DEL PRODUCTO: PERMISO DE CÁMARA + CÁMARA REAL + VISTA PREVIA (OPCIONAL) ---
@Composable
private fun OrderPhotoSection(
    photoPath: String?,
    onPhotoTaken: (String) -> Unit,
    onPhotoRemoved: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED
        )
    }
    var permissionDenied by remember { mutableStateOf(false) }
    var pendingPhotoFile by remember { mutableStateOf<File?>(null) }
    var photoBitmap by remember(photoPath) {
        mutableStateOf(photoPath?.let { decodeSampledBitmap(File(it)) })
    }

    val takePictureLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        val file = pendingPhotoFile
        if (success && file != null && file.exists()) {
            onPhotoTaken(file.absolutePath)
        }
        pendingPhotoFile = null
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasCameraPermission = granted
        permissionDenied = !granted
        if (granted) {
            pendingPhotoFile?.let { file ->
                launchTakePicture(file, context, takePictureLauncher)
            }
        }
    }

    fun requestAndLaunchCamera() {
        val photoFile = File(context.cacheDir, "order_photos")
            .apply { mkdirs() }
            .let { dir ->
                File(dir, "pedido_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())}.jpg")
            }
        pendingPhotoFile = photoFile
        if (hasCameraPermission) {
            takePictureLauncher.launch(
                FileProvider.getUriForFile(context, "com.uttt.enverdelivery.app.fileprovider", photoFile)
            )
        } else {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = EnverCardBg),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val captured = photoBitmap

            if (captured != null) {
                // --- VISTA PREVIA DE LA FOTOGRAFÍA REALMENTE TOMADA ---
                Image(
                    bitmap = captured.asImageBitmap(),
                    contentDescription = "Foto del pedido",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(14.dp))
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Vista previa de tu foto",
                    fontSize = 11.sp,
                    color = EnverTextMuted
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = {
                            onPhotoRemoved()
                        },
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Icon(Icons.Outlined.DeleteOutline, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Quitar", fontSize = 11.sp)
                    }
                    Button(
                        onClick = { requestAndLaunchCamera() },
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = EnverGreen)
                    ) {
                        Icon(
                            Icons.Outlined.Refresh,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Tomar otra", fontSize = 11.sp, color = Color.White)
                    }
                }
            } else {
                Surface(
                    color = EnverBadgeBg,
                    shape = CircleShape,
                    modifier = Modifier
                        .size(48.dp)
                        .clickable { requestAndLaunchCamera() }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Outlined.CameraAlt,
                            contentDescription = "Tomar foto",
                            tint = EnverDarkGreen,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Tomar foto o subir captura",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )
                Text(
                    text = if (permissionDenied)
                        "Permiso de cámara denegado: puedes continuar sin fotografía."
                    else
                        "Ayuda a que traigan el conector o modelo exacto",
                    fontSize = 11.sp,
                    color = EnverTextMuted,
                    textAlign = TextAlign.Center
                )

                if (permissionDenied) {
                    Spacer(modifier = Modifier.height(8.dp))
                    TextButton(onClick = { permissionLauncher.launch(Manifest.permission.CAMERA) }) {
                        Text("Volver a intentar", fontSize = 11.sp, color = EnverDarkGreen)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Chips de ayuda rápida (Apuntes, Conector, Menú)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SuggestionChip(
                        onClick = { },
                        label = { Text("Apuntes", fontSize = 11.sp) },
                        shape = RoundedCornerShape(12.dp)
                    )
                    SuggestionChip(
                        onClick = { },
                        label = { Text("Conector", fontSize = 11.sp) },
                        shape = RoundedCornerShape(12.dp)
                    )
                    SuggestionChip(
                        onClick = { },
                        label = { Text("Menú", fontSize = 11.sp) },
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }
        }
    }
}

private fun launchTakePicture(
    file: File,
    context: android.content.Context,
    launcher: androidx.activity.result.ActivityResultLauncher<Uri>
) {
    launcher.launch(FileProvider.getUriForFile(context, "com.uttt.enverdelivery.app.fileprovider", file))
}

private fun decodeSampledBitmap(file: File): Bitmap? {
    if (!file.exists()) return null
    val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
    BitmapFactory.decodeFile(file.absolutePath, bounds)
    var sampleSize = 1
    while (bounds.outWidth / sampleSize > 1280) sampleSize *= 2
    val options = BitmapFactory.Options().apply { inSampleSize = sampleSize }
    return BitmapFactory.decodeFile(file.absolutePath, options)
}

// Sanitización de la tarifa: solo dígitos + un punto decimal (máx 2 decimales),
// sin negativos ni caracteres arbitrarios.
private fun sanitizeTarifa(raw: String): String {
    val cleaned = StringBuilder()
    var decimalStarted = false
    var decimalCount = 0
    for (c in raw) {
        when {
            c.isDigit() -> {
                if (!decimalStarted || decimalCount < 2) {
                    cleaned.append(c)
                    if (decimalStarted) decimalCount++
                }
            }
            c == '.' && !decimalStarted && cleaned.isNotEmpty() -> {
                decimalStarted = true
                cleaned.append('.')
            }
        }
    }
    return cleaned.toString()
}

private fun isValidTarifa(value: String): Boolean {
    val amount = value.toDoubleOrNull() ?: return false
    return amount > 0.0
}

@Composable
private fun createOrderInputColors(isError: Boolean) = OutlinedTextFieldDefaults.colors(
    focusedTextColor = Color(0xFF0F172A),
    unfocusedTextColor = Color(0xFF0F172A),
    cursorColor = EnverGreen,
    focusedContainerColor = EnverCardBg,
    unfocusedContainerColor = EnverCardBg,
    focusedBorderColor = if (isError) Color(0xFFDC2626) else Color(0xFFCBD5E1),
    unfocusedBorderColor = if (isError) Color(0xFFDC2626) else Color(0xFFE2E8F0)
)

// --- INDICADOR DE PROGRESO REUTILIZADO EN LOS 4 PASOS ---
// Bloque coherente: etiqueta "PASO N DE 4" + barra a todo el ancho disponible
// (respetando los márgenes laterales normales del contenido).
@Composable
fun OrderStepProgressHeader(currentStep: Int, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "PASO $currentStep DE $TOTAL_STEPS",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = EnverGreen
        )
        Spacer(modifier = Modifier.height(6.dp))
        StepProgressBar(currentStep = currentStep)
    }
}

@Composable
fun StepProgressBar(currentStep: Int, modifier: Modifier = Modifier) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        for (step in 1..TOTAL_STEPS) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(6.dp)
                    .background(
                        color = if (step <= currentStep) EnverGreen else Color(0xFFE2E8F0),
                        shape = RoundedCornerShape(3.dp)
                    )
            )
        }
    }
}

// --- PASOS 2 A 4: PLACEHOLDERS CONECTADOS AL FLUJO (diseño pendiente de referencia) ---
@Composable
private fun CreateOrderStepPlaceholder(
    stepNumber: Int,
    nextStepNumber: Int?,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        TextButton(onClick = onBackClick, contentPadding = PaddingValues(0.dp)) {
            Text("← Regresar", fontSize = 12.sp, color = EnverDarkGreen)
        }
        Spacer(modifier = Modifier.height(8.dp))
        OrderStepProgressHeader(currentStep = stepNumber)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 120.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Paso $stepNumber en construcción",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "El diseño de este paso se integrará al entregar el código de referencia. Tus datos del paso anterior se conservan.",
                fontSize = 12.sp,
                color = EnverTextMuted,
                textAlign = TextAlign.Center
            )
            if (nextStepNumber != null) {
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = {},
                    enabled = false,
                    shape = RoundedCornerShape(25.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EnverGreen,
                        disabledContainerColor = EnverTextMuted.copy(alpha = 0.3f)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Text(
                        text = "Continuar",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}
