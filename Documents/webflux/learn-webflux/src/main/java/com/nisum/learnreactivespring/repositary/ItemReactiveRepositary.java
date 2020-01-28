package com.nisum.learnreactivespring.repositary;

import com.nisum.learnreactivespring.document.Item;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import reactor.core.publisher.Flux;

@EnableReactiveMongoRepositories

public interface ItemReactiveRepositary extends ReactiveMongoRepository<Item,String> {
    Flux<Item>  findByDescription(String description);
}
