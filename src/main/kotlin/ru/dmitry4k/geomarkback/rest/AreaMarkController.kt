package ru.dmitry4k.geomarkback.rest

import org.springframework.web.bind.annotation.*
import ru.dmitry4k.geomarkback.rest.dto.PostAreaMarkRequest
import ru.dmitry4k.geomarkback.services.AreaRateService
import ru.dmitry4k.geomarkback.services.dto.GeoPoint

@RestController
@RequestMapping("/geomark/area")
class AreaMarkController(
    val areaRateService: AreaRateService
) {
    @PostMapping
    fun post(@RequestBody request: PostAreaMarkRequest) {
        with(request) {
            areaRateService.addAreaRate(rate, points.map { GeoPoint(it.lat, it.lng, 1.0) })
        }
    }
}