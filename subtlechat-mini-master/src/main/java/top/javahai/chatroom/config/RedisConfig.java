package top.javahai.chatroom.config;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import java.io.IOException;

@Slf4j
@Configuration
public class RedisConfig {

    @Bean
    public RedissonClient redisson() {
        RedissonClient client = null;
        try {
            client = Redisson.create(Config.fromYAML(new ClassPathResource("redisson.yml").getInputStream()));
        } catch (IOException e) {
            log.error("init redisson fail:",e);
        }
        return client;
    }

    @Bean
    public RedisMessageListenerContainer container(RedisConnectionFactory factory){
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(factory);
        return container;
    }
}
