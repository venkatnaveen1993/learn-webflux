package com.nisum.learnreactivespring.initialize;

import com.nisum.learnreactivespring.document.Item;
import com.nisum.learnreactivespring.document.ItemCapped;
import com.nisum.learnreactivespring.repositary.ItemCappedReactiverepositary;
import com.nisum.learnreactivespring.repositary.ItemReactiveRepositary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Component
public class ItemDataInitialie implements CommandLineRunner {
@Autowired
    ItemReactiveRepositary itemReactiveRepositary;
@Autowired
    MongoOperations mongoOperations;
@Autowired
    ItemCappedReactiverepositary itemCappedReactiverepositary;
    @Override
    public void run(String... args) throws Exception {
        initialiseDataSetup();
        createCappedCollections();
        dataSetupForCappedColection();
    }
    private void createCappedCollections(){
        mongoOperations.dropCollection(ItemCapped.class);
        mongoOperations.createCollection(ItemCapped.class, CollectionOptions.empty().maxDocuments(20).size(50000).capped());
    }
public List<Item> data(){
   return Arrays.asList(new Item("null","samsung Tv",230.00)
            ,new Item(null,"lg tv",700.00));
}
    private void initialiseDataSetup() {
        itemReactiveRepositary.deleteAll().thenMany(Flux.fromIterable(data()))
                .flatMap(itemReactiveRepositary::save).thenMany(itemReactiveRepositary.findAll())
                .subscribe(item -> {
                    System.out.println(item);
                });
    }
    public void dataSetupForCappedColection()
    {
        Flux<ItemCapped> itemcappedflux =Flux.interval(Duration.ofSeconds(1)).map(i -> new ItemCapped(null,"random"+i,(100.00+i)));
        itemCappedReactiverepositary.insert(itemcappedflux).subscribe(item->{
            System.out.println(itemcappedflux);
        });
    }
}
