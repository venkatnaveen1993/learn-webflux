package com.nisum.learnreactivespring.repositary;

import com.nisum.learnreactivespring.document.ItemCapped;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import reactor.core.publisher.Flux;

public interface ItemCappedReactiverepositary extends ReactiveMongoRepository<ItemCapped,String> {
    @Tailable
    Flux<ItemCapped> findItemBy();
}
