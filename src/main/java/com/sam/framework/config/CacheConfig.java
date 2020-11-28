package com.sam.framework.config;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerUtils;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * redis配置@Cacheable缓存
 */
@Slf4j
@EnableCaching
@Configuration
@EnableConfigurationProperties(CacheProperties.class)
public class CacheConfig extends CachingConfigurerSupport {

    private final RedisProperties properties;

    private final CacheProperties cacheProperties;

    @Autowired
    public CacheConfig(RedisProperties properties, CacheProperties cacheProperties) {
        this.cacheProperties = cacheProperties;
        this.properties = properties;
    }

    /**
     * 自定义缓存的可以生成策略, 没有指定key的缓存都使用这个生成.
     *
     * @return
     */
    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        //  设置自动key的生成规则，配置spring boot的注解，进行方法级别的缓存
        // 使用：进行分割，可以很多显示出层级关系
        return (target, method, params) -> {
            StringBuilder sb = new StringBuilder();
            sb.append(target.getClass().getName());
            sb.append(".");
            sb.append(method.getName());
            sb.append("[");
            for (Object obj : params) {
                sb.append(obj);
            }
            sb.append("]");
            return sb.toString();
        };
    }

    /**
     * 创建ehCacheCacheManager, ehcache 缓存
     */
    @Bean
    public EhCacheCacheManager ehCacheCacheManager() {
        Resource p = this.cacheProperties.getEhcache().getConfig();
        Resource location = this.cacheProperties
                .resolveConfigLocation(p);
        return new EhCacheCacheManager(EhCacheManagerUtils.buildCacheManager(location));
    }

    /**
     * cacheManager名字
     */
    public interface CacheManagerNames {
        /**
         * redis
         */
        String REDIS_CACHE_MANAGER = "redisCacheManager";

        /**
         * ehCache
         */
        String EHCACHE_CACHE_MAANGER = "ehCacheCacheManager";
    }


    /**
     * 缓存名，名称暗示了缓存时长 注意： 如果添加了新的缓存名，需要同时在下面的RedisCacheCustomizer#RedisCacheCustomizer里配置名称对应的缓存时长
     * ，时长为0代表永不过期；缓存名最好公司内部唯一，因为可能多个项目共用一个redis。
     */
    public interface RedisCacheNames {
        /**
         * 15分钟缓存组
         */
        String CACHE_15MINS = "mycache:cache:15m";
        /**
         * 30分钟缓存组
         */
        String CACHE_30MINS = "mycache:cache:30m";
        /**
         * 60分钟缓存组
         */
        String CACHE_60MINS = "mycache:cache:60m";
        /**
         * 180分钟缓存组
         */
        String CACHE_180MINS = "mycache:cache:180m";
    }

    /**
     * ehcache缓存名
     * 需要在ehcache.xml配置文件配置对应的缓存. 注意name保持一致.
     */
    public interface EhCacheNames {
        String CACHE_10MINS = "mycache:cache:10m";

        String CACHE_20MINS = "mycache:cache:20m";

        String CACHE_30MINS = "mycache:cache:30m";
    }


    /**
     * 缓存配置管理器
     * 这里设定 jackson2JsonRedisSerializer , 解决保存的值显示为乱码
     */
    @Bean
    @Primary
    public RedisCacheManager redisCacheManager(RedisConnectionFactory connectionFactory) {
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = getObjectJackson2JsonRedisSerializer();
        // 配置redisTemplate
        RedisSerializer<String> stringSerializer = new StringRedisSerializer();

        // 生成一个默认配置，通过config对象即可对缓存进行自定义配置
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
        // 设置缓存的默认过期时间，也是使用Duration设置, 设置为30分钟
        config = config.entryTtl(Duration.ofMinutes(30))
                // 不缓存空值
                .disableCachingNullValues();

        // 设置一个初始化的缓存空间set集合, 就是注解@Cacheable(value = "my-redis-cache2")中的value值,
        Set<String> cacheNames = new HashSet<>();
        cacheNames.add(RedisCacheNames.CACHE_15MINS);
        cacheNames.add(RedisCacheNames.CACHE_30MINS);
        cacheNames.add(RedisCacheNames.CACHE_60MINS);
        cacheNames.add(RedisCacheNames.CACHE_180MINS);

        // 对每个缓存空间应用不同的配置
        Map<String, RedisCacheConfiguration> configMap = new HashMap<>(16);
        configMap.put(RedisCacheNames.CACHE_15MINS, config.entryTtl(Duration.ofMinutes(15))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(stringSerializer))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer)));
        configMap.put(RedisCacheNames.CACHE_30MINS, config.entryTtl(Duration.ofMinutes(30))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(stringSerializer))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer)));
        configMap.put(RedisCacheNames.CACHE_60MINS, config.entryTtl(Duration.ofMinutes(60))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(stringSerializer))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer)));
        configMap.put(RedisCacheNames.CACHE_180MINS, config.entryTtl(Duration.ofMinutes(180))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(stringSerializer))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer)));

        log.info("....");
        // 配置序列化（解决乱码的问题）
        // 生成一个默认配置，通过config对象即可对缓存进行自定义配置
        // 配置序列化
        RedisCacheConfiguration redisCacheConfiguration = config
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(stringSerializer))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer));
        return RedisCacheManager.builder(connectionFactory)
                .initialCacheNames(cacheNames)
                .withInitialCacheConfigurations(configMap)
                .cacheDefaults(redisCacheConfiguration)
                .transactionAware()
                .build();
    }


    private Jackson2JsonRedisSerializer<Object> getObjectJackson2JsonRedisSerializer() {
        //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值（默认使用JDK的序列化方式）
        Jackson2JsonRedisSerializer jacksonSeial = new Jackson2JsonRedisSerializer(Object.class);

        ObjectMapper om = new ObjectMapper();
        // 指定要序列化的域，field,get和set,以及修饰符范围，ANY是都有包括private和public
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 指定序列化输入的类型，类必须是非final修饰的，final修饰的类，比如String,Integer等会跑出异常
//        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);//过时
        om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        //配置全局localdatetime解析器
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
        om.registerModule(javaTimeModule);
        jacksonSeial.setObjectMapper(om);

        return jacksonSeial;
    }

    /**
     * LocalDateTime序列化
     */
    public class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {
        @Override
        public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(value.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
    }

    /**
     * LocalDateTime反序列化
     */
    public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
        @Override
        public LocalDateTime deserialize(JsonParser p, DeserializationContext deserializationContext) throws IOException {
            return LocalDateTime.parse(p.getValueAsString(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
    }

    /**
     * 获取缓存连接
     *
     * @return
     */
    /*@Bean
    public RedisConnectionFactory getConnectionFactory() {
        //单机模式
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(properties.getHost());
        configuration.setPort(properties.getPort());
        configuration.setDatabase(properties.getDatabase());
        configuration.setPassword(RedisPassword.of(properties.getPassword()));
        //哨兵模式
        //RedisSentinelConfiguration configuration1 = new RedisSentinelConfiguration();
        //集群模式
        //RedisClusterConfiguration configuration2 = new RedisClusterConfiguration();
        //factory.setShareNativeConnection(false);//是否允许多个线程操作共用同一个缓存连接，默认true，false时每个操作都将开辟新的连接
        return new LettuceConnectionFactory(configuration, getPoolConfig());
    }*/

    /**
     * 获取缓存连接池
     *
     * @return
     */
    /*@Bean
    public LettucePoolingClientConfiguration getPoolConfig() {
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxTotal(properties.getMaxActive());
        config.setMaxWaitMillis(Long.parseLong(properties.getMaxWait().contains("ms") ? properties.getMaxWait().replaceAll("ms", "") : properties.getMaxWait()));
        config.setMaxIdle(properties.getMaxIdle());
        config.setMinIdle(properties.getMinIdle());
        return LettucePoolingClientConfiguration.builder()
                .poolConfig(config)
                .commandTimeout(Duration.ofMillis(Long.parseLong(properties.getTimeout().contains("ms") ? properties.getTimeout().replaceAll("ms", "") : properties.getTimeout())))
                .shutdownTimeout(Duration.ofMillis(Long.parseLong(properties.getShutdown().contains("ms") ? properties.getShutdown().replaceAll("ms", "") : properties.getShutdown())))
                .build();
    }*/
    @Override
    @Bean
    public CacheErrorHandler errorHandler() {
        // 异常处理，当Redis发生异常时，打印日志，但是程序正常走
        log.info("初始化 -> [{}]", "Redis CacheErrorHandler");
        return new CacheErrorHandler() {
            @Override
            public void handleCacheGetError(RuntimeException e, Cache cache, Object key) {
                log.error("Redis occur handleCacheGetError：key -> [{}]", key, e);
            }

            @Override
            public void handleCachePutError(RuntimeException e, Cache cache, Object key, Object value) {
                log.error("Redis occur handleCachePutError：key -> [{}]；value -> [{}]", key, value, e);
            }

            @Override
            public void handleCacheEvictError(RuntimeException e, Cache cache, Object key) {
                log.error("Redis occur handleCacheEvictError：key -> [{}]", key, e);
            }

            @Override
            public void handleCacheClearError(RuntimeException e, Cache cache) {
                log.error("Redis occur handleCacheClearError：", e);
            }
        };
    }


}
