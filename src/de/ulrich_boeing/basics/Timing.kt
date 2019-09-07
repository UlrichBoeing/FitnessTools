package de.ulrich_boeing.basics

val timing = Timing()

class Timing {
    private var startTime = 0L
    private var oldMsg = ""

    fun start() {
        oldMsg = "start"
        startTime = System.currentTimeMillis()
    }

    fun get(): Long {
        return System.currentTimeMillis() - startTime
    }

    fun print(msg: String = "") {
        val end = get()

        println("Dauer von $oldMsg bis $msg:  $end")
        oldMsg = msg
        startTime = System.currentTimeMillis()
    }


}