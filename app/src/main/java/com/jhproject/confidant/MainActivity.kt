package com.jhproject.confidant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import com.jhproject.confidant.ui.mainscreen.MainScreen
import com.jhproject.confidant.ui.theme.ConfidantTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        window.isNavigationBarContrastEnforced = false
        setContent {
            ConfidantTheme {
                val windowSizeClass = calculateWindowSizeClass(this)
                MainScreen(windowSizeClass)
            }
        }
    }
}
