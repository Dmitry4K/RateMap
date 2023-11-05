package ru.dmitry4k.geomarkback.rest

import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*
import ru.dmitry4k.geomarkback.rest.dto.PostMarkRequestBody
import ru.dmitry4k.geomarkback.service.MarksService

@RestController
@RequestMapping("/mark")
class MarkController(
    val marksService: MarksService
) {
    @PutMapping
    fun putMark(@Valid @RequestBody request: PostMarkRequestBody) {
        with(request) {
            marksService.saveMark(mark, lat, lng, depth)
        }
    }
}