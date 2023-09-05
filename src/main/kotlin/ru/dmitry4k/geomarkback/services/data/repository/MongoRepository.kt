package ru.dmitry4k.geomarkback.services.data.repository

import org.springframework.data.mongodb.repository.MongoRepository
import ru.dmitry4k.geomarkback.services.data.dao.GeoPointDao

interface MongoRepository : MongoRepository<GeoPointDao, String>