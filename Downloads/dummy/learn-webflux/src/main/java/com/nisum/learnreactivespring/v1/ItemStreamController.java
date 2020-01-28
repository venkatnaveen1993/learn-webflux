package com.nisum.learnreactivespring.v1;

import com.nisum.learnreactivespring.document.ItemCapped;
import com.nisum.learnreactivespring.repositary.ItemCappedReactiverepositary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class ItemStreamController {
    @Autowired
    ItemCappedReactiverepositary itemCappedReactiverepositary;
    @GetMapping(value = "/v1/stream/items",produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<ItemCapped> getItemstream(){
        return itemCappedReactiverepositary.findItemBy();
    }

}
