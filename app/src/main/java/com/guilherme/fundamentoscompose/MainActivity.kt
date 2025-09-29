package com.guilherme.fundamentoscompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import com.guilherme.fundamentoscompose.screens.GamesScreen
import com.guilherme.fundamentoscompose.ui.theme.FundamentosjetpackcomposelistaslazyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FundamentosjetpackcomposelistaslazyTheme {
                // Você pode usar direto o MaterialTheme se preferir
                MaterialTheme {
                    GamesScreen()
                }
            }
        }
    }
}
