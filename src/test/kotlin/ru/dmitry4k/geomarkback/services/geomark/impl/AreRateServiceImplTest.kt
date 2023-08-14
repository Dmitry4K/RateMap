package ru.dmitry4k.geomarkback.services.geomark.impl

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import ru.dmitry4k.geomarkback.services.geomark.AreaRateService
import ru.dmitry4k.geomarkback.services.geomark.dto.GeoRate
import javax.validation.ConstraintDeclarationException

@SpringBootTest
class AreRateServiceImplTest(
    @Autowired
    val areaRateService: AreaRateService
) {
    @Test
    fun `Not allowed empty points list`() {
        assertThrows<ConstraintDeclarationException> { areaRateService.addAreaRate(GeoRate(1.0F), listOf()) }
    }
}