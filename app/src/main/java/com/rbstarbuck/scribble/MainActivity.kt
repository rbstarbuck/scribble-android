package com.rbstarbuck.scribble

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.rbstarbuck.scribble.game.GameView
import com.rbstarbuck.scribble.game.GameViewModel

class MainActivity: ComponentActivity() {
    private val game = GameViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            GameView(
                viewModel = game,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}