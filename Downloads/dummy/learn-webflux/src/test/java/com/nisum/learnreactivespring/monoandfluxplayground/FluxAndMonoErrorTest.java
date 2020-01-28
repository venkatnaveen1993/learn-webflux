package com.nisum.learnreactivespring.monoandfluxplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

public class FluxAndMonoErrorTest {
    @Test
    public void fluxErrorHandling(){
        Flux<String> stringFlux = Flux.just("A","B","c")
                .concatWith(Flux.error(new RuntimeException("Exception Ocurred")))
                .concatWith(Flux.just("D"))
                .log();
        StepVerifier.create(stringFlux).expectSubscription()
                .expectNext("A","B","c").expectError(RuntimeException.class).verify();
    }
    @Test
    public void fluxErrorHandling_resume(){
        Flux<String> stringFlux = Flux.just("A","B","c")
                .concatWith(Flux.error(new RuntimeException("Exception Ocurred")))
                .concatWith(Flux.just("D"))
                .onErrorResume((e)->{
                    System.out.println("Exception is"+e);
                    return Flux.just("default","default2");
                        }
                        );
        StepVerifier.create(stringFlux.log()).expectSubscription()
                .expectNext("A","B","c").expectNext("default","default2").verifyComplete();
    }
    @Test
    public void fluxErrorHandling_ErrorRturn(){
        Flux<String> stringFlux = Flux.just("A","B","c")
                .concatWith(Flux.error(new RuntimeException("Exception Ocurred")))
                .concatWith(Flux.just("D"))
                .onErrorReturn("default")
                ;
        StepVerifier.create(stringFlux.log()).expectSubscription()
                .expectNext("A","B","c").expectNext("default").verifyComplete();
    }
    @Test
    public void fluxErrorHandling_ErrorMap(){
        Flux<String> stringFlux = Flux.just("A","B","c")
                .concatWith(Flux.error(new RuntimeException("Exception Ocurred")))
                .concatWith(Flux.just("D"))
                .onErrorMap((e)->new CustomException(e));

        StepVerifier.create(stringFlux.log()).expectSubscription()
                .expectNext("A","B","c").expectError(CustomException.class).verify();
    }
    @Test
    public void fluxErrorHandling_ErrorMapRetry(){
        Flux<String> stringFlux = Flux.just("A","B","c")
                .concatWith(Flux.error(new RuntimeException("Exception Ocurred")))
                .concatWith(Flux.just("D"))
                .onErrorMap((e)->new CustomException(e))
                .retry(2);

        StepVerifier.create(stringFlux.log()).expectSubscription()
                .expectNext("A","B","c").expectNext("A","B","c").expectNext("A","B","c").expectError(CustomException.class).verify();
    }
    //this Test case fail because illegal exception
//    @Test
//    public void fluxErrorHandling_ErrorMapRetry_backoff(){
//        Flux<String> stringFlux = Flux.just("A","B","c")
//                .concatWith(Flux.error(new RuntimeException("Exception Ocurred")))
//                .concatWith(Flux.just("D"))
//                .onErrorMap((e)->new CustomException(e))
//                .retryBackoff(2, Duration.ofSeconds(5));
//
//        StepVerifier.create(stringFlux.log()).expectSubscription()
//                .expectNext("A","B","c").expectNext("A","B","c").expectNext("A","B","c").expectError(CustomException.class).verify();
//
//}
    @Test
    public void fluxErrorHandling_ErrorMapRetry_backoffpass(){
        Flux<String> stringFlux = Flux.just("A","B","c")
                .concatWith(Flux.error(new RuntimeException("Exception Ocurred")))
                .concatWith(Flux.just("D"))
                .onErrorMap((e)->new CustomException(e))
                .retryBackoff(2, Duration.ofSeconds(5));

        StepVerifier.create(stringFlux.log()).expectSubscription()
                .expectNext("A","B","c").expectNext("A","B","c").expectNext("A","B","c").expectError(IllegalStateException.class).verify();

    }
}
