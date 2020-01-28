package com.nisum.learnreactivespring.handler;

import com.nisum.learnreactivespring.document.Item;
import com.nisum.learnreactivespring.repositary.ItemReactiveRepositary;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureWebTestClient
@DirtiesContext
public class ItemHandlerTest {
    @Autowired
    WebTestClient webTestClient;
    @Autowired
    ItemReactiveRepositary itemReactiveRepositary;

    public List<Item> data(){
        return Arrays.asList(new Item(null,"samsung Tv",230.00)
                ,new Item("ABC","lg tv",700.00));
    }
    @Before
    public void setUp(){
        itemReactiveRepositary.deleteAll().thenMany(Flux.fromIterable(data())).flatMap(itemReactiveRepositary::save)
                .doOnNext(item -> {
                    System.out.println(item);
                }).blockLast();
    }
    public void getAllItems(){
        webTestClient.get().uri("/v1/fun/items").exchange().expectStatus().isOk().expectHeader()
                .contentType(MediaType.APPLICATION_JSON).expectBodyList(Item.class).hasSize(2);
    }
    @Test
    public void getone(){
        webTestClient.get().uri("v1/fun/items/{id}","ABC").exchange().expectStatus().isOk()
                .expectBody().jsonPath("$.price",700.00);
    }
    @Test
    public void notFound(){
        webTestClient.get().uri("v1/fun/items/{id}").exchange().expectStatus().isNotFound();
    }
    @Test
    public void runtimeexeption(){
        webTestClient.get().uri("fun/runtimeexception").exchange()
                .expectStatus().is5xxServerError().expectBody()
                .jsonPath("$.message","Runtime Exception Ocurred");
    }
}
