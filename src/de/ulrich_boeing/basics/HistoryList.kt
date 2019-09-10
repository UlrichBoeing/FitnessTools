package de.ulrich_boeing.basics

class HistoryList<T>(val maxSize: Int)  {
    private val arr = Array<Object?>(maxSize) { null }
    var added = 0L

    private val addedIndex
        get() = added - 1

    val size : Int
        get() = if (added < maxSize) added.toInt() else maxSize

    val indices: IntRange
        get() = 0 until size

    fun add(element: T) {
        added++
        val i = (addedIndex % maxSize).toInt()
        arr[i] = element as Object
    }

    operator fun get(index: Int): T {
        if (addedIndex < index) {
            return throw IllegalArgumentException("Wrong index value for HistoryList")
        }
        val i = ((addedIndex - index) % maxSize).toInt()
        return arr[i] as T
    }
}