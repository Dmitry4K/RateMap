package ru.dmitry4k.geomarkback.extension

import java.awt.Color

fun Color.withAlpha(newAlpha: Int) = Color(red, green, blue, newAlpha)
