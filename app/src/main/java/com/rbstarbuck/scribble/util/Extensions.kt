package com.rbstarbuck.scribble.util

import android.content.Context
import android.util.DisplayMetrics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Float.pxToDp(context: Context): Dp =
    (this /(context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT))
        .dp

fun Dp.dpToPx(context: Context): Float = (this.value * context.resources.displayMetrics.density)

private val chars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
fun generateKey(size: Int = 32) = Array(size) { chars.random() }.joinToString("")