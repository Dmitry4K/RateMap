package ru.dmitry4k.geomarkback.rest

import io.swagger.annotations.Api
import org.springframework.web.bind.annotation.*
import ru.dmitry4k.geomarkback.rest.dto.PostAreaMarkRequest
import ru.dmitry4k.geomarkback.services.geomark.AreaRateService
import ru.dmitry4k.geomarkback.services.geomark.dto.GeoPoint

@Api(description = "Контроллер оценок на выделенной области")
@RestController
@RequestMapping("/geomark/area")
class AreaMarkController(
    val areaRateService: AreaRateService
) {
    @PostMapping
    fun post(@RequestBody request: PostAreaMarkRequest) {
        with(request) {
            areaRateService.addAreaRate(rate, points.map { GeoPoint(it.lat, it.lng) })
        }
    }
}