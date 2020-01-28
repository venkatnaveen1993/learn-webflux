package com.nisum.learnreactivespring.v1;

import com.nisum.learnreactivespring.document.Item;
import com.nisum.learnreactivespring.repositary.ItemReactiveRepositary;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class ItemController {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String>handleRuntimeException(RuntimeException ex){
        log.error("exception at runtime:{}"+ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    @Autowired
    ItemReactiveRepositary itemReactiveRepositary;



    @GetMapping("/v1/items")
    public Flux<Item> getAllItems(){
        return  itemReactiveRepositary.findAll();
    }

    @GetMapping("/v1/items/runtimexception")
    public Flux<Item> runtimeexception(){
        return  itemReactiveRepositary.findAll().concatWith(Mono.error(new RuntimeException("exception occurred")));
    }
    @GetMapping("/v1/items/{id}")
    public Mono<ResponseEntity<Item>> getoneitem(@PathVariable String id){
        return itemReactiveRepositary.findById(id).map((item)->
             new ResponseEntity<>(item, HttpStatus.OK)
        ).defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
}

    @PostMapping("v1/items")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Item> createItem(@RequestBody Item item){
        return itemReactiveRepositary.save(item);

}

@DeleteMapping("v1/items/{id}")
    public Mono<Void> delete(@PathVariable String id){
        return itemReactiveRepositary.deleteById(id);
}
@PutMapping("v1/items/{id")
    public Mono<ResponseEntity<Item>> update(@PathVariable String id,@RequestBody Item item){
        return itemReactiveRepositary.findById(id).flatMap(cuurentitem ->{
            cuurentitem.setPrice(item.getPrice());
            cuurentitem.setDescription(item.getDescription());
            return itemReactiveRepositary.save(cuurentitem);
        }).map(updateditem->new ResponseEntity<>(updateditem,HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
}

}

