package com.nisum.learnreactivespring.handler;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureWebTestClient
public class SampleHandlerFunctionTest {
    @Autowired
    WebTestClient webtest;
    @Test
    public  void Flux_apporach1(){
        Flux<Integer> integerStream = webtest.get().uri("/functional/flux").accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange().expectStatus().isOk().returnResult(Integer.class).getResponseBody();
        StepVerifier.create(integerStream).expectSubscription()
                .expectNext(1).expectNext(2).expectNext(3).expectNext(4).verifyComplete();
    }
//    @Test
//    public void monoTest() {
//        Integer expectedvalue = new Integer(1);
//        webtest.get().uri("/functional/mono").accept(MediaType.APPLICATION_JSON_UTF8).exchange().expectStatus().isOk()
//                .expectBody(Integer.class).consumeWith((response) -> {
//                    assertEquals(expectedvalue, response.getResponseBody());
//                }
//
//        );
//    }
}
