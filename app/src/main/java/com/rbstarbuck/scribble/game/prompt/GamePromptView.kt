package com.rbstarbuck.scribble.game.prompt

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rbstarbuck.scribble.R
import org.koin.java.KoinJavaComponent.inject

@Composable
fun GamePromptView(modifier: Modifier = Modifier) {
    val viewModel: GamePromptViewModel by inject(GamePromptViewModel::class.java)

    Column(modifier) {
        Spacer(Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                imageVector = ImageVector.vectorResource(R.drawable.back),
                contentDescription = stringResource(R.string.back),
                modifier = Modifier
                    .size(36.dp)
            )

            Text(
                text = "Watermelon",
                fontSize = 30.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )

            Spacer(Modifier.width(36.dp))
        }

        Spacer(Modifier.weight(1f))
    }
}