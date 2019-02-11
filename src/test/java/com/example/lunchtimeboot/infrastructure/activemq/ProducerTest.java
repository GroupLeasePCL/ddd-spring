package com.example.lunchtimeboot.infrastructure.activemq;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProducerTest {

    @MockBean
    private JmsTemplate jmsTemplate;

    @Autowired
    private Producer producer;

//    @Before
//    public void setUp() {
//        producer = new Producer(jmsTemplate);
//    }

    @Test
    public void publish() {
        /* Given */
        String destinationName = "somewhere";
        String message = "hello";

        /* Setup */
        jmsTemplate.setReceiveTimeout(1000);

        /* When */
        producer.publish(destinationName, message);

        /* Then */
//        assertThat(jmsTemplate.receiveAndConvert())
        Mockito.verify(jmsTemplate, Mockito.times(1))
                .convertAndSend(destinationName, message);
    }
}
