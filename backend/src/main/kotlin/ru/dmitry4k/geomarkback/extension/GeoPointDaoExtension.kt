package ru.dmitry4k.geomarkback.extension

import ru.dmitry4k.geomarkback.data.dao.GeoPointDao

fun GeoPointDao.updateMark(newMark: Double) {
    mark = calculateNewMark(count!!, mark!!, newMark)
    count = count!! + 1
}

private fun calculateNewMark(oldCount: Long, oldMark: Double, newMark: Double): Double {
    val newCount = oldCount + 1
    return if (oldCount == 0L) newMark else oldMark * (oldCount.toDouble() / newCount.toDouble()) + newMark / newCount.toDouble()
}
