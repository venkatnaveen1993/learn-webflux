package com.nisum.learnreactivespring.monoandfluxplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

public class FluxAndMonoTimeTest {
    @Test
    public void infiniteSequence() throws InterruptedException {
        Flux<Long> infinitesequence = Flux.interval(Duration.ofMillis(100))
                .log();
        infinitesequence.subscribe(
                (element)->System.out.println("value is"+element));
                Thread.sleep(3000);


    }
    @Test
    public void infiniteSequenceTest() throws InterruptedException {
        Flux<Long> infinitesequence = Flux.interval(Duration.ofMillis(100))
                .take(3)
                .log();
        StepVerifier.create(infinitesequence).expectSubscription().expectNext(0L,1L,2L).verifyComplete();


    }
    @Test
    public void infiniteSequencemap() throws InterruptedException {
        Flux<Integer> infinitesequence = Flux.interval(Duration.ofMillis(100))
                .map(l->new Integer(l.intValue()))
                .take(3)
                .log();
        StepVerifier.create(infinitesequence).expectSubscription().expectNext(0,1,2).verifyComplete();


    }
    @Test
    public void infiniteSequencemapDelay() throws InterruptedException {
        Flux<Integer> infinitesequence = Flux.interval(Duration.ofMillis(100))
                .delayElements(Duration.ofSeconds(1))
                .map(l->new Integer(l.intValue()))
                .take(3)
                .log();
        StepVerifier.create(infinitesequence).expectSubscription().expectNext(0,1,2).verifyComplete();


    }
}
