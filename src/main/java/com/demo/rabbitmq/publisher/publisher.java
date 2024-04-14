package com.demo.rabbitmq.publisher;

import com.demo.entity.Student;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/rabbitmq")
public class publisher {

    @Autowired
    private RabbitTemplate template;

    @Value("${rabbitmq.exchange}")
    private String exchangeName;

    @Value("${rabbitmq.routingKey}")
    private String routingKeyName;

    @PostMapping("/{id}")
    public String bookStudent(@RequestBody Student student, @PathVariable int id) {
        student.setId(id);
        template.convertAndSend(exchangeName, routingKeyName, student);
        return "Success !!";
    }
}
