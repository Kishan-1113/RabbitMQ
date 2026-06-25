package com.example.rabbit_config.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.rabbit_config.Publisher.Publisher;

@RestController
@RequestMapping("/queue")
public class MessageController {

    private Publisher publisher;

    MessageController(Publisher publisher) {
        this.publisher = publisher;
    }

    @GetMapping("/{message}")
    public ResponseEntity<?> sendMessage(@PathVariable String message) {
        try {
            publisher.publisher1(message);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occuered while publishing to server queue !");
        }
        return ResponseEntity.ok("Published : " + message);
    }

    // @GetMapping("/response")
    // public ResponseEntity<?> getMethodName() {
    // return ResponseEntity.ok(consumer.consumer1(null));
    // }

}
