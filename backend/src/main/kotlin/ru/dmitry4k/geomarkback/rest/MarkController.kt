package ru.dmitry4k.geomarkback.rest

import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*
import ru.dmitry4k.geomarkback.data.dao.GeoPointDao
import ru.dmitry4k.geomarkback.dto.PostMarkRequestBody
import ru.dmitry4k.geomarkback.service.MarksService

@RestController
@RequestMapping("/api/v1/mark")
class MarkController(
    val marksService: MarksService
) {
    @PutMapping
    fun putMark(@Valid @RequestBody request: PostMarkRequestBody) {
        with(request) {
            marksService.saveMark(mark, polygon)
        }
    }

    @GetMapping
    fun getMarks(@RequestParam lat: Double, @RequestParam lng: Double, @RequestParam radius: Long): List<GeoPointDao> {
        return marksService.getMarks(lat, lng, radius).points
    }
}