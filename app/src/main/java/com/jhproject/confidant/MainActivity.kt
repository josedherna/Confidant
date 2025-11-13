package com.jhproject.confidant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import com.jhproject.confidant.ui.mainscreen.MainScreen
import com.jhproject.confidant.ui.theme.ConfidantTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        window.isNavigationBarContrastEnforced = false
        setContent {
            ConfidantTheme {
                MainScreen()
            }
        }
    }
}
