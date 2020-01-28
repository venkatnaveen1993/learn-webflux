package com.nisum.learnreactivespring.repositary;

import com.nisum.learnreactivespring.document.Item;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

@DataMongoTest
@RunWith(SpringRunner.class)
@DirtiesContext
public class ItemReactiveRepositaryTest {
    @Autowired
    ItemReactiveRepositary itemReactiveRepositary;

    List<Item> itemList = Arrays.asList(new Item(null, "SamsungTv", 400.00)
            , new Item(null, "Lg Tv", 500.00)
            , new Item("ABC", "mi Tv", 600.00));


    @Before
    public void setUp() {
        itemReactiveRepositary.deleteAll().thenMany(Flux.fromIterable(itemList)).flatMap(itemReactiveRepositary::save)
                .doOnNext((item) ->
                        System.out.println("inserted item" + item)).blockLast();

    }

    @Test
    public void getAllItems() {
        StepVerifier.create(itemReactiveRepositary.findAll()).expectSubscription().expectNextCount(0).verifyComplete();
    }


    @Test
    public void getItemById() {
        StepVerifier.create(itemReactiveRepositary.findById("ABC")).expectSubscription().expectNextMatches((item) -> {
                    return item.getDescription().equals("mi TV");
                }
        ).verifyComplete();
    }


    @Test
    public void findByDescription() {
        StepVerifier.create(itemReactiveRepositary.findByDescription("mi tv")).expectSubscription().expectNextCount(1).verifyComplete();
    }

    @Test
    public void saveItem() {
        Item item = new Item(null, "google home mini", 700.00);
        Mono<Item> saveditem = itemReactiveRepositary.save(item);
        StepVerifier.create(saveditem).expectSubscription().expectNextMatches((item1) -> {
            return item.getId() != null && item.getDescription().equals("google home mini");
        }).verifyComplete();
    }

    @Test
    public void updateItem() {
        double newprice = 520.00;
     Flux<Item> updatedItem=   itemReactiveRepositary.findByDescription("mi tv").map(item -> {
            item.setPrice(newprice);
            return item;
        })
                .flatMap(item -> {
                    return itemReactiveRepositary.save(item);

                });
 StepVerifier.create(updatedItem).expectSubscription().expectNextMatches(item -> item.getPrice()==520.00).verifyComplete();
    }

    @Test
    public void deleteById(){
        Mono<Void> deletebyid  = itemReactiveRepositary.findById("ABC").map(Item::getId).flatMap((id)->{
            return itemReactiveRepositary.deleteById(id);
        });
        StepVerifier.create(deletebyid).expectSubscription().expectNextCount(2).verifyComplete();
    }


}
