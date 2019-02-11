package com.example.lunchtimeboot.infrastructure.activemq;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class Producer {
    private JmsTemplate jmsTemplate;

    public Producer(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void publish(String destinationName, String message) {
        jmsTemplate.convertAndSend(destinationName, message);
    }
}
