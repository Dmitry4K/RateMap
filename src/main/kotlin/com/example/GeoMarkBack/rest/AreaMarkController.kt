package com.example.GeoMarkBack.rest

import io.swagger.annotations.Api
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Api(description = "Контроллер оценок на выделенной области")
@RestController
@RequestMapping("/geomark/area")
class AreaMarkController {
    @GetMapping
    fun get() {

    }

    @PostMapping
    fun post() {

    }
}