package com.rbstarbuck.scribble.operation

import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.nio.ByteBuffer

private var operations: List<Operation> = emptyList()

const val DRAWING_COLLECTION = "Drawing"
const val LAYER_COLLECTION = "Layer"
const val STROKE_COLLECTION = "Stroke"

private object Operations {
    private val mutex = Mutex()

    fun editList(task: () -> Unit) {
        GlobalScope.launch {
            mutex.withLock {
                task()
            }
        }
    }
}

internal abstract class Operation {
    protected val firestore = Firebase.firestore

    protected abstract fun execute()

    protected fun finalize(task: Task<Void>) {
        task.addOnCompleteListener {
            Operations.editList {
                while (operations.isNotEmpty()) {
                    operations = operations.subList(1, operations.size)

                    if (operations.isNotEmpty()) {
                        operations.first().execute()
                    }
                }
            }
        }
    }

    fun upload() {
        Operations.editList {
            operations = operations + this
            operations.first().execute()
        }
    }
}

fun packInts(first: Int, second: Int): Long {
    val buffer = ByteBuffer.allocate(8)
    buffer.putInt(first)
    buffer.putInt(second)
    return buffer.long
}

fun unpackInts(packedLong: Long): Pair<Int, Int> {
    val buffer = ByteBuffer.allocate(8)
    buffer.putLong(packedLong)
    buffer.rewind()
    return Pair(buffer.int, buffer.int)
}

fun packFloats(first: Float, second: Float): Long {
    val buffer = ByteBuffer.allocate(8)
    buffer.putFloat(first)
    buffer.putFloat(second)
    return buffer.long
}

fun unpackFloats(packedLong: Long): Pair<Float, Float> {
    val buffer = ByteBuffer.allocate(8)
    buffer.putLong(packedLong)
    buffer.rewind()
    return Pair(buffer.float, buffer.float)
}