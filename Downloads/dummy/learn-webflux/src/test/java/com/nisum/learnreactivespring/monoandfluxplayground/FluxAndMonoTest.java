package com.nisum.learnreactivespring.monoandfluxplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class FluxAndMonoTest {
    @Test
    public void fluxTest(){
        Flux<String> stringFlux = Flux.just("spring","springboot","reactivespring")
        //        .concatWith(Flux.error(new RuntimeException("Exception occurred")))
                .log();
        stringFlux.subscribe(System.out::println,(e)->System.out.println("eception is"+e));

    }
    @Test
    public void fluxTestElementWithoutError(){
        Flux<String> stringFlux = Flux.just("spring","springboot","reactivespring")
                      .concatWith(Flux.error(new RuntimeException("Exception occurred")))
                .log();
        StepVerifier.create(stringFlux).expectNext("spring","springboot","reactivespring").expectError(RuntimeException.class).verify();
    }
    @Test
    public void fluxTestElemenCounttWitError(){
        Flux<String> stringFlux = Flux.just("spring","springboot","reactivespring")
                .concatWith(Flux.error(new RuntimeException("Exception occurred")))
                .log();
        StepVerifier.create(stringFlux).expectNextCount(3).expectError(RuntimeException.class).verify();
    }
    @Test
    public void monoTest(){
        Mono<String> stringMono = Mono.just("Spring");
        StepVerifier.create(stringMono.log()).expectNext("Spring").verifyComplete();
    }
    @Test
    public void monoTest_Error() {
        StepVerifier.create(Mono.error(new RuntimeException("Exception Occurred")).log())
                .expectError(RuntimeException.class).verify();


    }
}
