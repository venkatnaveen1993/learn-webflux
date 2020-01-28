package com.nisum.learnreactivespring.monoandfluxplayground;

import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxAndMonoBackPressure {
    @Test
    public void backPressureTest(){
        Flux<Integer> finiteflux =Flux.range(1,10).log();
        StepVerifier.create(finiteflux).expectSubscription()
                .thenRequest(1).expectNext(1).thenRequest(2).expectNext(2).thenCancel().verify();
    }
    @Test
    public void backPressure() {
        Flux<Integer> finiteflux = Flux.range(1, 10).log();
        finiteflux.subscribe((ele)->System.out.println("element is"+ele)
                ,(e)->System.err.println("exception"+e)
                ,()->System.out.println("Done")
                ,(subscription -> subscription.request(2)
        ) );
    }
    @Test
    public void backPressurecancel() {
        Flux<Integer> finiteflux = Flux.range(1, 10).log();
        finiteflux.subscribe((ele)->System.out.println("element is"+ele)
                ,(e)->System.err.println("exception"+e)
                ,()->System.out.println("Done")
                ,(subscription -> subscription.cancel()
                ) );
    }
    @Test
    public void customized_backPressure() {
        Flux<Integer> finiteflux = Flux.range(1, 10).log();
        finiteflux.subscribe(new BaseSubscriber<Integer>() {
            @Override
            protected void hookOnNext(Integer value) {
                 request(1);
                 System.out.println("value recieved"+value);
                 if (value==4){
                     cancel();
                 }

            }
        });
    }
}
