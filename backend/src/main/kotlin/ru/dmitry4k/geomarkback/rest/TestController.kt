package ru.dmitry4k.geomarkback.rest

import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.dmitry4k.geomarkback.data.PointsMongoRepository
import ru.dmitry4k.geomarkback.data.dao.GeoPointDao
import ru.dmitry4k.geomarkback.dto.GeoPointRestRequest
import java.util.logging.Logger

val log = Logger.getLogger("testcontroller")
@RestController
@RequestMapping("/api/test")
class TestController(
    val repository: PointsMongoRepository
) {
    @GetMapping
    fun test() : String {
        return "Test!"
    }

    @PutMapping("/send")
    fun sendPoints(@RequestBody requestPoint: GeoPointRestRequest) {
        val point = GeoPointDao().apply {
            mark = 5.0
            count = 10
            point = GeoJsonPoint(requestPoint.lat, requestPoint.lng)
        }
        log.info("Gotten $point")
        repository.save(point)
        log.info("Repository contains points ${repository.findAll().joinToString { it.toString() }}")
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