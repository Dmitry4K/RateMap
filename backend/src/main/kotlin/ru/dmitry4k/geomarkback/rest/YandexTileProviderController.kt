package ru.dmitry4k.geomarkback.rest

import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.dmitry4k.geomarkback.service.YandexTileService


@RestController
@RequestMapping("/api/tile/yandex/png")
class YandexTileProviderController(
    val tileRenderer: YandexTileService
) {
    @GetMapping
    fun getTileByTileId(
        @RequestParam x: Int,
        @RequestParam y: Int,
        @RequestParam z: Int
    ): ResponseEntity<ByteArray> {
        val tile = tileRenderer.getTile(x, y, z)
        val headers = HttpHeaders()
        headers.set(HttpHeaders.CONTENT_TYPE, "image/png")
        return ResponseEntity.ok()
            .headers(headers)
            .body(tile)
    }
}