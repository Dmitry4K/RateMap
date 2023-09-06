package ru.dmitry4k.geomarkback.config

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration
import ru.dmitry4k.geomarkback.data.dao.GeoPointDao
import ru.dmitry4k.geomarkback.data.repository.MongoRepository

@Configuration
class MongoConfig : AbstractMongoClientConfiguration() {
    override fun getDatabaseName(): String {
        return "local"
    }

    override fun mongoClient(): MongoClient {
        return MongoClients.create("mongodb://admin:admin@localhost:27017/")
    }

    @Bean
    fun runner(repository: MongoRepository) = CommandLineRunner {
        args -> repository.save(GeoPointDao().apply { name = "123" })
    }
}