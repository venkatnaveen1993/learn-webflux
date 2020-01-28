package com.nisum.learnreactivespring.monoandfluxplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

public class FluxAndMonoCombineTest {
    @Test
    public void combineUsingMerge(){
        Flux<String> flux1 = Flux.just("A","B","C");
        Flux<String> flux2 = Flux.just("D","E","F");
        Flux<String> mergedstring = Flux.merge(flux1,flux2);
        StepVerifier.create(mergedstring.log()).expectSubscription().expectNext("A","B","C","D","E","F").verifyComplete();
     }
    @Test
    public void combineUsingMergeWithDelay(){
        Flux<String> flux1 = Flux.just("A","B","C").delayElements(Duration.ofSeconds(1));
        Flux<String> flux2 = Flux.just("D","E","F").delayElements(Duration.ofSeconds(1));
        Flux<String> mergedstring = Flux.merge(flux1,flux2);
        StepVerifier.create(mergedstring.log()).expectSubscription().expectNextCount(6).verifyComplete();
    }
    @Test
    public void combineUsingConcatWithDelay(){
        Flux<String> flux1 = Flux.just("A","B","C").delayElements(Duration.ofSeconds(1));
        Flux<String> flux2 = Flux.just("D","E","F").delayElements(Duration.ofSeconds(1));
        Flux<String> mergedstring = Flux.concat(flux1,flux2);
        StepVerifier.create(mergedstring.log()).expectSubscription().expectNext("A","B","C","D","E","F").verifyComplete();
    }
    @Test
    public void combineUsingZip(){
        Flux<String> flux1 = Flux.just("A","B","C");
        Flux<String> flux2 = Flux.just("D","E","F");
        Flux<String> mergedstring = Flux.zip(flux1,flux2,(t1,t2)->{
            return t1.concat(t2);
                }
                );
        StepVerifier.create(mergedstring.log()).expectSubscription().expectNext("AD","BE","CF").verifyComplete();
    }
}
