package ru.dmitry4k.geomarkback.data.repository

import org.springframework.data.mongodb.repository.MongoRepository
import ru.dmitry4k.geomarkback.data.dao.GeoPointDao

interface MongoRepository : MongoRepository<GeoPointDao, String>