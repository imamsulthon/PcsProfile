package com.mamsky.pcsprofile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.mamsky.pcsprofile.navigation.MyNavigationHost
import com.mamsky.pcsprofile.ui.theme.PcsProfileTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PcsProfileTheme {
                MyNavigationHost(navController = rememberNavController())
            }
        }
    }

}