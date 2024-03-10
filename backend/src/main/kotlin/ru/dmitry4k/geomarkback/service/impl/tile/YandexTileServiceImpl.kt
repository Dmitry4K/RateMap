package ru.dmitry4k.geomarkback.service.impl.tile

import org.springframework.stereotype.Service
import ru.dmitry4k.geomarkback.service.tile.YandexTileProvider
import ru.dmitry4k.geomarkback.service.tile.YandexTileService

@Service
class YandexTileServiceImpl(
    private val providers: List<YandexTileProvider>
) : YandexTileService {
    override fun getTile(layerName: String, x: Int, y: Int, z: Int) = providers.first { it.layerName() == layerName }
        .getTile(x, y, z)
}