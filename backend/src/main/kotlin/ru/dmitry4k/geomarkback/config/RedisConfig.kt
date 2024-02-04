package ru.dmitry4k.geomarkback.config

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.cache.CacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisPassword
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory
import ru.dmitry4k.geomarkback.config.properties.CacheProperties
import java.time.Duration

@EnableConfigurationProperties(CacheProperties::class)
@Configuration
class RedisConfig {
    @Bean
    fun cacheManager(
        redisConnectionFactory: RedisConnectionFactory,
        cacheProperties: CacheProperties
    ): CacheManager {
        val default = RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofMinutes(5))
        return RedisCacheManager.builder(redisConnectionFactory)
            .cacheDefaults(default)
            .withInitialCacheConfigurations(initCaches(cacheProperties))
            .build()
            .also { it.initializeCaches() }
    }

    @Bean
    fun jedisConnectionFactory(cacheProperties: CacheProperties): RedisConnectionFactory {
        val clientConfig = JedisClientConfiguration.builder()
            .useSsl()
            .build()
        val redisConfig = RedisStandaloneConfiguration().apply {
            hostName = cacheProperties.connection.host
            port = cacheProperties.connection.port
            password = RedisPassword.of(cacheProperties.connection.pass)
        }
        return JedisConnectionFactory(redisConfig, clientConfig)
    }

    fun initCaches(cacheProperties: CacheProperties): Map<String, RedisCacheConfiguration> {
        return cacheProperties.collections
            .mapValues { RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(it.value.ttl))
            }
    }
}