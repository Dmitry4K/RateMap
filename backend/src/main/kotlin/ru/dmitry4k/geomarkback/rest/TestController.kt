package ru.dmitry4k.geomarkback.rest

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.dmitry4k.geomarkback.data.PointsMongoRepository
import ru.dmitry4k.geomarkback.data.dao.GeoPointDao

@RestController
@RequestMapping("/api/test")
class TestController(
    val repository: PointsMongoRepository
) {
    @GetMapping
    fun test() : String {
        return "Test!"
    }

    @GetMapping("/near")
    fun getNear(
        @RequestParam("lat") lat: Double,
        @RequestParam("lng") lng: Double,
        @RequestParam("distance") distance: Long
    ): ResponseEntity<List<GeoPointDao>> {
        return ResponseEntity.ok(
            repository.findNear(lng, lat, distance, 0)
        )
    }
}