package ru.dmitry4k.geomarkback.service.tile

interface TileModifier<T, K> {
    fun modify(matrix: MutableList<MutableList<T>>): MutableList<MutableList<K>>
}