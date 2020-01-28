package com.nisum.learnreactivespring.controller;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import javax.xml.ws.WebFault;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
@RunWith(SpringRunner.class)
@WebFluxTest
public class FluxAndMonoControllerTest {
    @Autowired
    WebTestClient webTestClient;
    @Test
    public  void Flux_apporach1(){
        Flux<Integer> integerStream = webTestClient.get().uri("/flux").accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange().expectStatus().isOk().returnResult(Integer.class).getResponseBody();
        StepVerifier.create(integerStream).expectSubscription()
                .expectNext(1).expectNext(2).expectNext(3).expectNext(4).verifyComplete();
    }
    @Test
    public void Flux_apporach2(){
        webTestClient.get().uri("/flux").accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange().expectStatus().isOk().expectHeader()
                .contentType(MediaType.APPLICATION_JSON_UTF8).expectBodyList(Integer.class).hasSize(4);
    }
    @Test
    public void flux_apporach3(){
        List<Integer> expectedIntegerList = Arrays.asList(1,2,3,4);
        EntityExchangeResult<List<Integer>> entityexchane = webTestClient.get().uri("/flux").accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange().expectStatus().isOk().expectBodyList(Integer.class).returnResult();
        assertEquals(expectedIntegerList,entityexchane.getResponseBody());
    }
//    @Test
//    public void flux_apporach4(){
//        List<Integer> expectedIntegerList = Arrays.asList(1,2,3,4);
//        EntityExchangeResult<List<Integer>> entityexchane = webTestClient.get().uri("/flux").accept(MediaType.APPLICATION_JSON_UTF8)
//                .exchange().expectStatus().isOk().expectBodyList(Integer.class).consumeWith((response)->{
//                assertEquals(expectedIntegerList,response.getResponseBody());
//                });
//    }
    @Test
    public void fluxStream() {
        Flux<Long> longstream = webTestClient.get().uri("/fluxstreaminfinite").accept(MediaType.APPLICATION_STREAM_JSON)
                .exchange().expectStatus().isOk().returnResult(long.class).getResponseBody();
        StepVerifier.create(longstream).expectSubscription().expectNext(0L).expectNext(1L).thenCancel().verify();
    }
    @Test
    public void monoTest(){
        Integer expectedvalue = new Integer(1);
        webTestClient.get().uri("/mono").accept(MediaType.APPLICATION_JSON_UTF8).exchange().expectStatus().isOk()
                .expectBody(Integer.class).consumeWith((response)->{
                    assertEquals(expectedvalue,response.getResponseBody());
                }

                );
    }
}
