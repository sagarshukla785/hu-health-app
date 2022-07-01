package com.example.healthcare.healthcare.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {
    @Bean
    public NewTopic newTopicPatient(){
        return TopicBuilder.name("send-email-doctor").build();
    }

    @Bean
    public NewTopic newTopicDoctor(){
        return TopicBuilder.name("send-email-patient").build();
    }
}
