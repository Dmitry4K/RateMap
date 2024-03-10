package ru.dmitry4k.geomarkback.rest

import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.dmitry4k.geomarkback.service.tile.YandexTileService


@RestController
@RequestMapping("/api/v1/layer")
class YandexTileController(
    val yandexTileService: YandexTileService
) {
    @GetMapping("/{layerName}/tile/yandex/png")
    fun getTileByTileId(
        @PathVariable layerName: String,
        @RequestParam x: Int,
        @RequestParam y: Int,
        @RequestParam z: Int
    ): ResponseEntity<ByteArray> {
        val tile = yandexTileService.getTile(layerName, x, y, z)
        val headers = HttpHeaders()
        headers.set(HttpHeaders.CONTENT_TYPE, "image/png")
        return ResponseEntity.ok()
            .headers(headers)
            .body(tile)
    }
}