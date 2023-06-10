package com.example.GeoMarkBack.rest

import com.example.GeoMarkBack.rest.dto.GetMarkRequest
import com.example.GeoMarkBack.rest.dto.PostMarkRequestBody
import io.swagger.annotations.Api
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*

@Api(description = "Контроллер оценок на точке")
@RestController
@RequestMapping("/geomark/point")
class PointMarkController {
    val logger = LoggerFactory.getLogger(PointMarkController::class.java)!!

    @GetMapping
    fun get(request: GetMarkRequest) {
        logger.info(request.toString())
    }

    @PostMapping
    fun post(@RequestBody requestBody: PostMarkRequestBody) {
        logger.info(requestBody.toString())
    }
}