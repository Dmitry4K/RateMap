package ru.dmitry4k.geomarkback.rest

import ru.dmitry4k.geomarkback.rest.dto.PostAreaMarkRequest
import ru.dmitry4k.geomarkback.rest.dto.PutAreaMarkRequest
import io.swagger.annotations.Api
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import ru.dmitry4k.geomarkback.services.geomark.AreaRateService
import ru.dmitry4k.geomarkback.services.geomark.dto.GeoPoint
import ru.dmitry4k.geomarkback.services.geomark.dto.GeoRate

@Api(description = "Контроллер оценок на выделенной области")
@RestController
@RequestMapping("/geomark/area")
class AreaMarkController(
    val areaRateService: AreaRateService
) {
    @PostMapping
    fun post(@RequestBody request: PostAreaMarkRequest) {
        with(request) {
            areaRateService.addAreaRate(GeoRate(rate), points.map { GeoPoint(it.lat, it.lng) })
        }
    }
}