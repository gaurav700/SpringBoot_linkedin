package com.LinkedIn.ConnectionService.Config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic sendConnectionRequestTopic(){
        return new NewTopic("Send-connection-request-topic", 3, (short) 1);
    }

    @Bean
    public NewTopic acceptConnectionRequestTopic(){
        return new NewTopic("Accept-connection-request-topic", 3, (short) 1);
    }
}
