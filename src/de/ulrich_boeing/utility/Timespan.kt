package de.ulrich_boeing.utility

import java.lang.RuntimeException

/**
 * Messung einer Zeitspanne
 * Ergebnisse werden in einer Liste gespeichert
 */
class Timespan(val name: String = "timespan", var unit: Unit = Unit.MILLI) {
    /**
     * Einheit für Zeitspannen
     * MILLI ist eine tausendstel Sekunde
     * MICRO ist eine millionstel Sekunde (Abkürzung µ wird englisch muh gesprochen),
     * NANO ist eine milliardstel Sekunde
     */
    enum class Unit(private val divisor: Long, val abbreviation: String) {
        MILLI(1_000_000L, "ms"),
        MICRO(1000L, "µs"),
        NANO(1L, "ns");

        /**
         * Konvertieren von Nanosekunden in die aktuelle Einheit
         */
        fun convert(nano: Long) = nano / divisor
    }

    /**
     * Liste für alle gemessenen Zeitspannen
     */
    private val list by lazy { mutableListOf<Long>() }
    private var startTime = 0L

    val isRunning: Boolean
        get() = (startTime != 0L)

    val lastTime: Long
    get() =
        if (list.isEmpty())
            time
        else
            unit.convert(list.last())

    /**
     * Die seit start() vergangene Zeit in Nanosekunden,
     * Liefert 0, wenn Timespan noch nicht gestartet wurde
     */
    val nano: Long
        get() = if (isRunning) (System.nanoTime() - startTime) else 0L

    /**
     * Die seit start() vergangene Zeit in der aktuellen Zeiteinheit
     */
    val time = unit.convert(nano)

    val milli: Long
        get() = Unit.MILLI.convert(nano)

    val micro: Long
        get() = Unit.MICRO.convert(nano)

    /**
     * Startet die Zeitmessung
     * Soll die Zeitmessung direkt gestartet werden: val timespan = Timespan().start()
     */
    fun start(): Timespan {
        if (isRunning)
            throw RuntimeException("Timespan: start() is called while running.")
        startTime = System.nanoTime()
        return this
    }

    /**
     * Beendet die Zeitmessung und speichert die Zeit,
     * Zeitmessung muss gestartet worden sein.
     */
    fun end(): Timespan {
        if (!isRunning)
            throw RuntimeException("Timespan: start before calling end.")
        list.add(nano)
        startTime = 0L
        return this
    }

    fun println() = println(this)

    override fun toString(): String {
        val abbr = unit.abbreviation
        return when {
            isRunning -> "$name is running: $time$abbr"
            list.size == 1 -> "$name no.1: $lastTime$abbr"
            list.isNotEmpty() -> "$name no.${list.size}: ${unit.convert(list.last())}$abbr - mean: ${mean()}$abbr"
            else -> "$name is just created"
        }
    }

    /**
     * Liefert den Mittelwert der bisherigen Zeitmessungen
     */
    fun mean() = unit.convert(list.sum() / list.size)
}
