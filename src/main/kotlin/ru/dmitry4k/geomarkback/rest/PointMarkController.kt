package ru.dmitry4k.geomarkback.rest

import ru.dmitry4k.geomarkback.rest.dto.GetMarkRequest
import ru.dmitry4k.geomarkback.rest.dto.PostMarkRequestBody
import io.swagger.annotations.Api
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.dmitry4k.geomarkback.services.geomark.PointRateService
import ru.dmitry4k.geomarkback.services.geomark.dto.GeoPoint
import ru.dmitry4k.geomarkback.services.geomark.dto.GeoRate

@Api(description = "Контроллер оценок на точке")
@RestController
@RequestMapping("/geomark/point")
class PointMarkController(
    val pointRateService: PointRateService
) {
    val logger = LoggerFactory.getLogger(PointMarkController::class.java)!!

    @GetMapping
    fun get(request: GetMarkRequest) {
        logger.info(request.toString())
    }

    @PostMapping
    fun post(@RequestBody requestBody: PostMarkRequestBody) {
        with (requestBody) {
            pointRateService.addRate(GeoRate(rate), GeoPoint(lat, lng))
        }
        logger.info(requestBody.toString())
    }
}