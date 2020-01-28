package com.nisum.learnreactivespring.handler;

import com.nisum.learnreactivespring.document.Item;
import com.nisum.learnreactivespring.document.ItemCapped;
import com.nisum.learnreactivespring.repositary.ItemCappedReactiverepositary;
import com.nisum.learnreactivespring.repositary.ItemReactiveRepositary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;

@Component
public class ItemHandlerFunction {
static Mono<ServerResponse> notfound =ServerResponse.notFound().build();
    @Autowired
    ItemReactiveRepositary itemReactiveRepositary;
    @Autowired
    ItemCappedReactiverepositary itemCappedReactiverepositary;
    public Mono<ServerResponse> getallitems(ServerRequest serverRequest){
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(itemReactiveRepositary.findAll(), Item.class);
    }
    @Autowired
    public Mono<ServerResponse> getoneitem(ServerRequest serverRequest){
       String id = serverRequest.pathVariable("id");
      Mono<Item> one = itemReactiveRepositary.findById(id);
      return one.flatMap(item ->
          ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(fromObject(item)).switchIfEmpty(notfound)
      );
    }
    @Autowired
    public Mono<ServerResponse> createitem(ServerRequest serverRequest){
        Mono<Item> itemCreated =serverRequest.bodyToMono(Item.class);
        return itemCreated.flatMap(item -> ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON).body(itemReactiveRepositary.save(item),Item.class));
    }
    @Autowired

    public Mono<ServerResponse> deleteitem(ServerRequest serverRequest){
      String id =  serverRequest.pathVariable("id");
     Mono<Void>deleteid = itemReactiveRepositary.deleteById(id);
     return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(deleteid,Item.class);

    }
    @Autowired
    public Mono<ServerResponse> updateitem(ServerRequest serverRequest){
        String id1 = serverRequest.pathVariable("id");
       Mono<Item>monoitem= serverRequest.bodyToMono(Item.class).flatMap(item -> {
        Mono<Item>mono=    itemReactiveRepositary.findById(id1).flatMap(currentitem->{
                currentitem.setDescription(item.getDescription());
                currentitem.setPrice(item.getPrice());
                return itemReactiveRepositary.save(currentitem);
            });
        return mono;

        });
       return monoitem.flatMap(item -> ServerResponse.ok()
               .contentType(MediaType.APPLICATION_JSON).body(fromObject(item)).switchIfEmpty(notfound));

    }
    public Mono<ServerResponse> itemexception(ServerRequest serverRequest){
        throw new RuntimeException("Runtime Exception occurred");
    }
    public Mono<ServerResponse> itemStream(ServerRequest serverRequest){
        return  ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(itemCappedReactiverepositary.findItemBy(), ItemCapped.class);
    }

}
