package com.demo.rabbitmq.consumer;


import com.demo.entity.Student;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class consumer {

    @RabbitListener(queues = "${rabbitmq.queue}")
    public void consumeMessageFromQueue(Student student) {
        System.out.println("Message recieved from queue : " + student);
    }
}
