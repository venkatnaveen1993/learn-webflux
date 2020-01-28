package com.nisum.learnreactivespring.router;

import com.nisum.learnreactivespring.handler.SampleHandlerFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class RouterConfigFunction {
    @Bean
    public RouterFunction<ServerResponse> route(SampleHandlerFunction sampleHandlerFunction){
        return RouterFunctions.route(GET("/functional/flux").and(accept(MediaType.APPLICATION_JSON_UTF8)),sampleHandlerFunction::flux)
                .andRoute(GET("/functional/mono").and(accept(MediaType.APPLICATION_JSON_UTF8)),sampleHandlerFunction::mono);
    }

}
