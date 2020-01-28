package com.nisum.learnreactivespring.v1;

import com.nisum.learnreactivespring.document.ItemCapped;
import com.nisum.learnreactivespring.repositary.ItemCappedReactiverepositary;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureWebTestClient
@DirtiesContext
public class ItemStreamControllerTest {
    @Autowired
    ItemCappedReactiverepositary itemCappedReactiverepositary;
    @Autowired
    WebTestClient webTestClient;
    @Autowired
    MongoOperations mongoOperations;
    @Before
    public void setup(){

            mongoOperations.dropCollection(ItemCapped.class);
            mongoOperations.createCollection(ItemCapped.class,
                    CollectionOptions.empty().maxDocuments(20).size(50000).capped());
        Flux<ItemCapped> itemcappedflux =Flux.interval(Duration.ofSeconds(1)).
                map(i -> new ItemCapped(null,"random"+i,(100.00+i)))
                .take(5);
        itemCappedReactiverepositary.insert(itemcappedflux).doOnNext(itemCapped -> {
            System.out.println(itemcappedflux);
        }).blockLast();

    }

    @Test
    public void itemstream(){
     Flux<ItemCapped> itemCappedFlux  = webTestClient.get().uri("v1/stream/items").
                exchange().expectStatus().isOk().returnResult(ItemCapped.class).getResponseBody().take(5);
        StepVerifier.create(itemCappedFlux).expectNextCount(2).thenCancel().verify();
    }
}
