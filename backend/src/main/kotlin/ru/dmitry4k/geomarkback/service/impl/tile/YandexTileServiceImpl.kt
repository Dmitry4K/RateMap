package ru.dmitry4k.geomarkback.service.impl.tile

import org.springframework.stereotype.Service
import ru.dmitry4k.geomarkback.service.tile.YandexTileProvider
import ru.dmitry4k.geomarkback.service.tile.YandexTileService

@Service
class YandexTileServiceImpl(
    private val providers: List<YandexTileProvider>
) : YandexTileService {

    //@Cacheable(cacheNames = ["yandex-tiles"])
    override fun getTile(layerName: String, x: Int, y: Int, z: Int) = resolveLayerProvider(layerName)
        .getTile(x, y, z)

    override fun getLegend(layerName: String, width: Int, height: Int, fontSize: Int): ByteArray = resolveLayerProvider(layerName)
        .getLegend(width, height, fontSize)

    private fun resolveLayerProvider(layerName: String) = providers.first { it.layerName() == layerName }
}