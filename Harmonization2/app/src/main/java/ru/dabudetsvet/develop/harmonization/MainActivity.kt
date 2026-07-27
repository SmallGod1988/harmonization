package ru.dabudetsvet.develop.harmonization

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.dabudetsvet.develop.harmonization.ui.navigation.HarmonizationNavHost
import ru.dabudetsvet.develop.harmonization.ui.theme.HarmonizationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HarmonizationTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    HarmonizationNavHost()
                }
            }
        }
    }
}
