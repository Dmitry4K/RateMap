package ru.dmitry4k.geomarkback.config

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.MongoTemplate
import ru.dmitry4k.geomarkback.config.properties.RateMapProperties

@EnableConfigurationProperties(RateMapProperties::class)
@Configuration
class MongoConfig(
    val rateMapProperties: RateMapProperties
) {
    @Bean
    fun mongoClient(): MongoClient {
        return MongoClients.create(rateMapProperties.connectionString)
    }

    @Bean
    fun mongoTemplate(): MongoTemplate {
        return MongoTemplate(mongoClient(), "ratemap-mongo-db")
    }
}