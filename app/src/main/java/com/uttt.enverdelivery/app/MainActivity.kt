package com.uttt.enverdelivery.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.uttt.enverdelivery.app.navigation.AppNavHost
import com.uttt.enverdelivery.app.ui.theme.EnverTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EnverTheme {
                AppNavHost()
            }
        }
    }
}
