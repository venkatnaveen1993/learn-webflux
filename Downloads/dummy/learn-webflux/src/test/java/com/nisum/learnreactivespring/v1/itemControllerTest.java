package com.nisum.learnreactivespring.v1;

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
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureWebTestClient
@DirtiesContext
public class itemControllerTest {
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
            webTestClient.get().uri("/v1/items").exchange().expectStatus().isOk().expectHeader()
                    .contentType(MediaType.APPLICATION_JSON_UTF8).expectBodyList(Item.class).hasSize(2);
    }
    @Test
    public void getone(){
            webTestClient.get().uri("v1/items/{id}","ABC").exchange().expectStatus().isOk()
                    .expectBody().jsonPath("$.price",700.00);
    }
    @Test
    public void notFound(){
            webTestClient.get().uri("v1/items/{id}").exchange().expectStatus().isNotFound();
    }
    @Test
    public void createItem(){
            Item item = new Item(null,"iphone",600.00);
            webTestClient.post().uri("/v1/item").contentType(MediaType.APPLICATION_JSON_UTF8)
                    .body(Mono.just(item),Item.class).exchange().expectStatus().isCreated().expectBody()
                    .jsonPath("$.price".equals(600.00));
    }
    @Test
    public void delete(){
            webTestClient.delete().uri("v1/items/{id}","ABC").accept(MediaType.APPLICATION_JSON_UTF8).exchange()
                    .expectStatus().isOk().expectBody(Void.class);
    }
    @Test
    public void update(){
            double newprice =900.00;
        Item item = new Item(null,"iphone",600.00);

        webTestClient.put().uri("v1/items/{id}","ABC").contentType(MediaType.APPLICATION_JSON_UTF8)
                    .accept(MediaType.APPLICATION_JSON_UTF8).body(Mono.just(item),Item.class).exchange()
                    .expectStatus().isOk().expectBody().jsonPath("$.price".equals(newprice));
    }
    @Test
    public void runtimeException(){
            webTestClient.get().uri("v1/items/runtimeexception").exchange().expectStatus().is5xxServerError()
                    .expectBody(String.class).isEqualTo("Runtime exception occurred");
    }
}
