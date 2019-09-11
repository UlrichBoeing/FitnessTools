package de.ulrich_boeing.basics

class HistoryList<T>(val capacity: Int)  {
    private val arr = Array<Object?>(capacity) { null }
    var added = 0L

    private val addedIndex
        get() = added - 1

    val size : Int
        get() = if (added < capacity) added.toInt() else capacity

    val indices: IntRange
        get() = 0 until size

    fun add(element: T) {
        added++
        val i = (addedIndex % capacity).toInt()
        arr[i] = element as Object
    }

    operator fun get(index: Int): T {
        if (addedIndex < index) {
            return throw IllegalArgumentException("Wrong index value for HistoryList")
        }
        val i = ((addedIndex - index) % capacity).toInt()
        return arr[i] as T
    }
}