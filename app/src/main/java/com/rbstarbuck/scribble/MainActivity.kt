package com.rbstarbuck.scribble

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.google.firebase.FirebaseApp
import com.rbstarbuck.scribble.game.GameView
import com.rbstarbuck.scribble.game.GameViewModel

class MainActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)

        setContent {
            val gameViewModel = remember { GameViewModel() }
            GameView(
                viewModel = gameViewModel,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}