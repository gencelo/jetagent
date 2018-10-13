package com.oguzhan.jetagent;

import com.oguzhan.jetagent.service.PostService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JetagentApplication {

    public static void main(String[] args) {
        SpringApplication.run(JetagentApplication.class, args);
        PostService postService = new PostService();
    }
}
