package ru.dmitry4k.geomarkback.data.dao

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "marks")
class GeoPointDao {
    @Id
    var id: String? = null
    var name: String? = null
}