package ru.dmitry4k.geomarkback.config

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration

@Configuration
class MongoConfig : AbstractMongoClientConfiguration() {
    // TODO Добавить создание 2dsphere индекса
    // TODO Добавить процесс инциализации БД точками
    override fun getDatabaseName(): String {
        return "local"
    }

    override fun mongoClient(): MongoClient {
        return MongoClients.create("mongodb://admin:admin@localhost:27017/")
    }
}