package ru.dmitry4k.geomarkback.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.MongoTemplate
import ru.dmitry4k.geomarkback.config.properties.RateMapProperties
import ru.dmitry4k.geomarkback.service.RateMapPointProvider
import ru.dmitry4k.geomarkback.service.impl.RateMapMongoProviderImpl

@Configuration
class PointsProvidersConfig(
    val mongoTemplate: MongoTemplate,
    val rateMapProperties: RateMapProperties
) {
    @Bean
    fun providers(): List<RateMapPointProvider> {
        val providers = mutableListOf<RateMapPointProvider>()
        for (area in rateMapProperties.areas) {
            for (layers in rateMapProperties.layers) {
                providers.add(
                    RateMapMongoProviderImpl(area.key, area.key+"_"+layers.key, layers.value.searchDistance, mongoTemplate)
                )
            }
        }
        return providers
    }
}