package cz.ackee.testtask

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Surface
import cz.ackee.testtask.core.presentation.ui.theme.TestTaskTheme
import cz.ackee.testtask.navigation.Navigation

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestTaskTheme {
                Surface {
                    Navigation()
                }
            }
        }
    }
}
