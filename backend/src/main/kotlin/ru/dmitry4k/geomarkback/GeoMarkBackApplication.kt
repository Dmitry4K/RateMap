package ru.dmitry4k.geomarkback

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.web.servlet.config.annotation.EnableWebMvc

@EnableWebMvc
@EnableConfigurationProperties
@EnableCaching
@SpringBootApplication(exclude = [DataSourceAutoConfiguration::class])
class GeoMarkBackApplication

fun main(args: Array<String>) {
	runApplication<GeoMarkBackApplication>(*args)
}
