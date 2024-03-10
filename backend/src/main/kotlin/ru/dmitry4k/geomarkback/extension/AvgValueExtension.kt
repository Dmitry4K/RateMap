package ru.dmitry4k.geomarkback.extension

import ru.dmitry4k.geomarkback.dto.AvgValue

fun AvgValue.merge(other: AvgValue){
    val newCount = count + other.count
    this.value = if (newCount <= 0) -1.0 else
        count.toDouble() / newCount.toDouble() * value + other.count.toDouble() / newCount.toDouble() * other.value
    this.count = newCount
}
