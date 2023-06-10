package com.example.GeoMarkBack.rest

import com.example.GeoMarkBack.rest.dto.GetMarkRequest
import com.example.GeoMarkBack.rest.dto.PostMarkRequestBody
import io.swagger.annotations.Api
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

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