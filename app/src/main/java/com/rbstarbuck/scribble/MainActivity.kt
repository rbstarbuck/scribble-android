package com.rbstarbuck.scribble

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.rbstarbuck.scribble.game.GameView
import com.rbstarbuck.scribble.game.GameViewModel

class MainActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val game = remember { GameViewModel() }
            GameView(
                viewModel = game,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}