package ru.dmitry4k.geomarkback

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.servlet.config.annotation.EnableWebMvc

@EnableWebMvc
@SpringBootApplication
class GeoMarkBackApplication

fun main(args: Array<String>) {
	runApplication<GeoMarkBackApplication>(*args)
}
