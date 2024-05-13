package ru.dmitry4k.geomarkback.rest

import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.dmitry4k.geomarkback.dto.PostAvgMetersPriceRequest
import ru.dmitry4k.geomarkback.service.MarksService
import ru.dmitry4k.geomarkback.service.tile.YandexTileService


@RestController
@RequestMapping("/api/v1/layer")
class LayerController(
    val yandexTileService: YandexTileService,
    val marksService: MarksService
) {
    @GetMapping("/{layerName}/tile/yandex/png")
    fun getTileByTileId(
        @PathVariable layerName: String,
        @RequestParam x: Int,
        @RequestParam y: Int,
        @RequestParam z: Int
    ): ResponseEntity<ByteArray> {
        val tile = yandexTileService.getTile(layerName, x, y, z)
        val headers = HttpHeaders().apply {
            set(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_PNG_VALUE)
        }
        return ResponseEntity.ok()
            .headers(headers)
            .body(tile)
    }

    @GetMapping("/{layerName}/legend/png")
    fun getLayerLegendImage(
        @PathVariable layerName: String,
        @RequestParam width: Int,
        @RequestParam height: Int,
        @RequestParam fontSize: Int
    ): ResponseEntity<ByteArray> {
        val legendPng = yandexTileService.getLegend(layerName, width, height, fontSize)
        val headers = HttpHeaders().apply {
            set(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_PNG_VALUE)
        }
        return ResponseEntity.ok()
            .headers(headers)
            .body(legendPng)
    }

    @PostMapping("/avgMetersPrice/set")
    fun setAvgMeterPrice(@RequestBody request: PostAvgMetersPriceRequest) {
        marksService.saveAvgMeterPrice(request.point, request.avgMeterPrice)
    }
}