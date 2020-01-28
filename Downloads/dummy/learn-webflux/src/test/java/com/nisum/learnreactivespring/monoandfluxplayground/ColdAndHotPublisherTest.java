package com.nisum.learnreactivespring.monoandfluxplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class ColdAndHotPublisherTest {
    @Test
    public void coldPublisherTest() throws InterruptedException {
        Flux<String> stringFlux = Flux.just("A","B","c","D","E","F")
                .delayElements(Duration.ofSeconds(1))
                .log();
        stringFlux.subscribe(s->System.out.println("Subscriber1"+s));
        Thread.sleep(2000);
        stringFlux.subscribe(s->System.out.println("Subscriber2"+s));
        Thread.sleep(4000);



    }
    @Test
    public void hotPublisher() throws InterruptedException {
        Flux<String> stringFlux = Flux.just("A","B","c","D","E","F")
                .delayElements(Duration.ofSeconds(1))
                .log();
        ConnectableFlux<String> connectableFlux = stringFlux.publish();
        connectableFlux.subscribe(s->System.out.println("Subscriber1"+s));
        Thread.sleep(2000);
        connectableFlux.subscribe(s->System.out.println("Subscriber2"+s));
        Thread.sleep(4000);

    }
}
