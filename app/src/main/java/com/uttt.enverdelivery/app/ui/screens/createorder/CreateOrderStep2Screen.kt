package com.uttt.enverdelivery.app.ui.screens.createorder

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.EditLocation
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.uttt.enverdelivery.app.ui.theme.EnverBadgeBg
import com.uttt.enverdelivery.app.ui.theme.EnverCardBg
import com.uttt.enverdelivery.app.ui.theme.EnverDarkGreen
import com.uttt.enverdelivery.app.ui.theme.EnverGreen
import com.uttt.enverdelivery.app.ui.theme.EnverTextMuted
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

// CENTRO POR DEFECTO DEL MAPA (ajustable al campus correspondiente).
// La cámara salta aquí si aún no hay ubicación del usuario ni punto seleccionado.
private val DefaultCampusCenter = LatLng(25.6517, -100.4062)

private fun formatLatLng(lat: Double, lng: Double): String =
    "Lat %.4f · Lng %.4f".format(Locale.US, lat, lng)

// PASO 2 DE 4: Ubicación. Mapa REAL (Google Maps Compose): mover/zoom/desplazarse,
// tocar para fijar/mover el pin, ubicación actual con permiso y buscador funcional
// (Geocoder del sistema). Sin Scaffold ni barra propia: los aporta MainScaffold.
@Composable
fun CreateOrderStep2Content(
    uiState: CreateOrderUiState,
    onBackClick: () -> Unit,
    onContinueClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED
        )
    }
    var isSearching by remember { mutableStateOf(false) }
    val geocoderResults = remember { mutableStateListOf<Address>() }

    val selected = remember(uiState.selectedLat, uiState.selectedLng) {
        if (uiState.selectedLat != null && uiState.selectedLng != null) {
            LatLng(uiState.selectedLat!!, uiState.selectedLng!!)
        } else null
    }

    val fusedClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            selected ?: DefaultCampusCenter,
            if (selected != null) 17f else 16f
        )
    }

    fun moveToCurrentLocation() {
        try {
            fusedClient.lastLocation.addOnSuccessListener { loc ->
                if (loc != null && uiState.selectedLat == null && uiState.selectedLng == null) {
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(
                        LatLng(loc.latitude, loc.longitude), 16f
                    )
                }
            }
        } catch (_: SecurityException) {
        }
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { grants ->
        val granted = grants[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            grants[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        hasLocationPermission = granted
        if (granted) moveToCurrentLocation()
    }

    LaunchedEffect(hasLocationPermission) {
        if (hasLocationPermission) moveToCurrentLocation()
    }

    fun requestLocationPermission() {
        locationPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    // Búsqueda real por nombre (Geocoder del sistema, sesgada al área visible del mapa)
    fun runLocationSearch(query: String) {
        val trimmed = query.trim()
        if (trimmed.isEmpty()) {
            geocoderResults.clear()
            return
        }
        isSearching = true
        val center = cameraPositionState.position.target
        scope.launch {
            val results = withContext(Dispatchers.IO) {
                try {
                    val geocoder = Geocoder(context, Locale.getDefault())
                    @Suppress("DEPRECATION")
                    geocoder.getFromLocationName(trimmed, 5, center.latitude - 0.02, center.longitude - 0.02, center.latitude + 0.02, center.longitude + 0.02)
                        ?: geocoder.getFromLocationName(trimmed, 5)
                } catch (_: Exception) {
                    null
                }
            }
            isSearching = false
            geocoderResults.clear()
            results
                ?.filter { it.latitude != 0.0 || it.longitude != 0.0 }
                ?.let { geocoderResults.addAll(it) }
        }
    }

    fun selectGeocoderResult(address: Address) {
        val latLng = LatLng(address.latitude, address.longitude)
        uiState.selectedLat = latLng.latitude
        uiState.selectedLng = latLng.longitude
        uiState.selectedAddressLabel = address.getAddressLine(0)
            ?: formatLatLng(latLng.latitude, latLng.longitude)
        geocoderResults.clear()
        scope.launch {
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(latLng, 17f)
            )
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(20.dp)
    ) {
        // --- HEADER SUPERIOR: ATRÁS + INDICADOR PASO 2 DE 4 (reutilizable) ---
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .size(36.dp)
                    .background(Color.White, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Outlined.ArrowBack,
                    contentDescription = "Atrás",
                    tint = Color(0xFF1E293B),
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Atrás",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E293B)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OrderStepProgressHeader(currentStep = 2)

        Spacer(modifier = Modifier.height(16.dp))

        // --- TÍTULO PRINCIPAL ---
        Text(
            text = "¿Dónde te llevamos el auxilio?",
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF0F172A)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Indica tu edificio y aula exacta para que tu compañero\nte encuentre sin rodeos.",
            fontSize = 12.sp,
            color = EnverTextMuted,
            lineHeight = 16.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // --- BUSCADOR DE UBICACIÓN (REAL: Geocoder del sistema) ---
        OutlinedTextField(
            value = uiState.locationSearch,
            onValueChange = { uiState.locationSearch = it },
            placeholder = {
                Text(
                    text = "Buscar lugar cercano (Ej. Biblioteca, Cafetería...)",
                    fontSize = 12.sp,
                    color = Color(0xFF94A3B8)
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = null,
                    tint = EnverGreen
                )
            },
            trailingIcon = {
                if (isSearching) {
                    CircularProgressIndicator(
                        color = EnverGreen,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(18.dp)
                    )
                } else {
                    IconButton(onClick = { runLocationSearch(uiState.locationSearch) }) {
                        Icon(
                            imageVector = Icons.Outlined.Search,
                            contentDescription = "Buscar",
                            tint = EnverDarkGreen,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            },
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color(0xFF0F172A),
                unfocusedTextColor = Color(0xFF0F172A),
                cursorColor = EnverGreen,
                focusedContainerColor = EnverCardBg,
                unfocusedContainerColor = EnverCardBg,
                focusedBorderColor = EnverGreen,
                unfocusedBorderColor = Color(0xFFE2E8F0)
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { runLocationSearch(uiState.locationSearch) }),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        if (geocoderResults.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Surface(
                color = EnverCardBg,
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    geocoderResults.forEach { address ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectGeocoderResult(address) }
                                .padding(horizontal = 14.dp, vertical = 10.dp)
                        ) {
                            Text(
                                text = address.getAddressLine(0) ?: address.featureName ?: "",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF0F172A)
                            )
                            if (!address.thoroughfare.isNullOrBlank() || !address.subThoroughfare.isNullOrBlank() || !address.locality.isNullOrBlank()) {
                                Text(
                                    text = listOfNotNull(address.subThoroughfare, address.thoroughfare, address.locality).joinToString(" · "),
                                    fontSize = 10.sp,
                                    color = EnverTextMuted
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // --- CHIPS DE EDIFICIOS CERCANOS (diseño de la referencia) ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Surface(
                color = EnverGreen,
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Edificio B (Seleccionado)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Surface(
                color = EnverCardBg,
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Text(
                    text = "Edificio A",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1E293B),
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- MAPA REAL E INTERACTIVO (GOOGLE MAPS COMPOSE) ---
        // Permiso de ubicación solicitado al entrar; la cámara salta a la ubicación
        // actual. Toca cualquier punto para colocar/mover el pin (estilo Uber).
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .clip(RoundedCornerShape(24.dp))
                .clickable(enabled = !hasLocationPermission) { requestLocationPermission() }
        ) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(
                    isMyLocationEnabled = hasLocationPermission
                ),
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = false,
                    myLocationButtonEnabled = hasLocationPermission,
                    compassEnabled = false
                ),
                onMapClick = { latLng ->
                    uiState.selectedLat = latLng.latitude
                    uiState.selectedLng = latLng.longitude
                    uiState.selectedAddressLabel = formatLatLng(latLng.latitude, latLng.longitude)
                    geocoderResults.clear()
                }
            ) {
                selected?.let { position ->
                    Marker(
                        state = MarkerState(position = position)
                    )
                }
            }

            Surface(
                color = Color.White.copy(alpha = 0.9f),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(12.dp)
            ) {
                Text(
                    text = "📌 14 rescatistas activos cerca",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = EnverDarkGreen,
                    modifier = Modifier.padding(8.dp)
                )
            }

            // PIN FLOTANTE con la ubicación seleccionada (se mueve con el mapa tocando otra zona)
            Surface(
                color = EnverDarkGreen,
                shape = RoundedCornerShape(20.dp),
                shadowElevation = 6.dp,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 12.dp)
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
                        text = uiState.selectedAddressLabel
                            ?: "Toca el mapa para seleccionar el punto de entrega",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }

        if (!hasLocationPermission) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Permite la ubicación para ver tu posición actual en el mapa.",
                fontSize = 11.sp,
                color = EnverTextMuted
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- CAMPO: DETALLE PARA TU REPARTIDOR (PLACEHOLDER REAL, IGUAL QUE LOGIN/REGISTRO) ---
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Outlined.EditLocation,
                contentDescription = null,
                tint = EnverGreen,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Detalle para tu repartidor",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E293B)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = uiState.locationDetail,
            onValueChange = { uiState.locationDetail = it },
            placeholder = {
                Text(
                    text = "Salón 302 - Fila del fondo junto a la ventana",
                    fontSize = 12.sp,
                    color = Color(0xFF94A3B8)
                )
            },
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color(0xFF0F172A),
                unfocusedTextColor = Color(0xFF0F172A),
                cursorColor = EnverGreen,
                focusedContainerColor = EnverCardBg,
                unfocusedContainerColor = EnverCardBg,
                focusedBorderColor = Color(0xFFCBD5E1),
                unfocusedBorderColor = Color(0xFFE2E8F0)
            ),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(20.dp))

        // --- SECCIÓN: TUS LUGARES HABITUALES (OPCIONES SELECCIONABLES) ---
        Text(
            text = "Tus lugares habituales",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = EnverTextMuted,
            letterSpacing = 0.5.sp
        )
        Spacer(modifier = Modifier.height(8.dp))

        val habitualPlaces = listOf(
            HabitualPlace(
                icon = "🏛️",
                title = "Mi salón habitual (Edif B - 302)",
                subtitle = "Piso 3 · Facultad de Ingeniería"
            )
        )
        val activeHabitual = uiState.selectedHabitualPlace ?: habitualPlaces.first().title

        habitualPlaces.forEach { place ->
            val isSelected = place.title == activeHabitual
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = EnverCardBg
                ),
                border = BorderStroke(
                    1.dp,
                    if (isSelected) EnverGreen else Color(0xFFE2E8F0)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { uiState.selectedHabitualPlace = place.title }
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = EnverBadgeBg,
                        shape = CircleShape,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) { Text(text = place.icon, fontSize = 16.sp) }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = place.title,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E293B)
                        )
                        Text(
                            text = place.subtitle,
                            fontSize = 10.sp,
                            color = EnverTextMuted
                        )
                    }
                    Icon(
                        imageVector = if (isSelected) Icons.Outlined.CheckCircle else Icons.Outlined.RadioButtonUnchecked,
                        contentDescription = null,
                        tint = if (isSelected) EnverGreen else EnverTextMuted,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- BOTÓN CONTINUAR A TIEMPO DE ENTREGA ---
        Button(
            onClick = onContinueClick,
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
                    text = "Continuar a tiempo de entrega",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "→", fontSize = 18.sp, color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(EnverGreen, CircleShape)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Compañeros verificados con matrícula activa",
                fontSize = 10.sp,
                color = EnverTextMuted,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

// Lugar habitual seleccionable del Paso 2
data class HabitualPlace(
    val icon: String,
    val title: String,
    val subtitle: String
)
