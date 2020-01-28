package com.nisum.learnreactivespring.router;

import com.nisum.learnreactivespring.handler.ItemHandlerFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;


@Configuration
public class ItemRouet {
    @Bean
    public RouterFunction<ServerResponse> itemRoute(ItemHandlerFunction itemHandlerFunction) {
        return RouterFunctions.route(GET("/v1/fun/items")
                .and(accept(MediaType.APPLICATION_JSON)), itemHandlerFunction::getallitems)
                .andRoute(GET("/v1/fun/items/{id}")
                        .and(accept(MediaType.APPLICATION_JSON)), itemHandlerFunction::getoneitem)
                .andRoute(POST("/v1/fun/items")
                        .and(accept(MediaType.APPLICATION_JSON)), itemHandlerFunction::createitem)
                .andRoute(POST("/v1/fun/{id}")
                        .and(accept(MediaType.APPLICATION_JSON)), itemHandlerFunction::deleteitem)
                .andRoute(POST("/v1/fun/{id}")
                        .and(accept(MediaType.APPLICATION_JSON)), itemHandlerFunction::updateitem);
    }

    @Bean
    public RouterFunction<ServerResponse> errorRoute(ItemHandlerFunction itemHandlerFunction1) {
        return RouterFunctions.route(GET("/FUN/RUNTIMEEXCEPTION")
                .and(accept(MediaType.APPLICATION_JSON)), itemHandlerFunction1::itemexception);
    }
    @Bean
    public RouterFunction<ServerResponse> itemstreamRoute(ItemHandlerFunction itemHandlerFunction1) {
        return RouterFunctions.route(GET("/v1/fun/stream/items")
                .and(accept(MediaType.APPLICATION_JSON)), itemHandlerFunction1::itemStream);
    }
}
