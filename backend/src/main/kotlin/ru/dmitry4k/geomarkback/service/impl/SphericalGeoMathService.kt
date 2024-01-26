package ru.dmitry4k.geomarkback.service.impl

import org.springframework.stereotype.Component
import ru.dmitry4k.geomarkback.service.Distance
import ru.dmitry4k.geomarkback.service.GeoMathService
import ru.dmitry4k.geomarkback.service.Mercator

@Component
class SphericalGeoMathService : GeoMathService {
    val sphericalMercatorImpl = SphericalMercatorImpl();
    val sphericalDistanceImpl = SphericalDistanceImpl(SphericalMercatorImpl.RADIUS_MAJOR);
    override fun mercator(): Mercator = sphericalMercatorImpl;

    override fun distance(): Distance = sphericalDistanceImpl;
}