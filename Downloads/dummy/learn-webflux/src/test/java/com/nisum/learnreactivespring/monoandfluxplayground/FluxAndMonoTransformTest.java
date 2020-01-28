package com.nisum.learnreactivespring.monoandfluxplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.ParallelFlux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

public class FluxAndMonoTransformTest {
    List<String> names = Arrays.asList("naveen", "raj", "ramesh");

    @Test
    public void transformUsingMap() {
        Flux<String> namesflux = Flux.fromIterable(names).map(s -> s.toUpperCase()).log();
        StepVerifier.create(namesflux).expectNext("NAVEEN", "RAJ", "RAMESH").verifyComplete();
    }

    @Test
    public void TrasformfromUsingMap_length() {
        Flux<Integer> namesflux = Flux.fromIterable(names).map(s -> s.length()).log();
        StepVerifier.create(namesflux).expectNext(6, 3, 6).verifyComplete();
    }

    @Test
    public void transformUsingMap_repeat() {
        Flux<Integer> namerepeat = Flux.fromIterable(names).map(s -> s.length()).repeat(1).log();
        StepVerifier.create(namerepeat).expectNext(6, 3, 6, 6, 3, 6).verifyComplete();
    }

    @Test
    public void trasformUsingMap_Filter() {
        Flux<String> namesfilter = Flux.fromIterable(names).filter(s -> s.length() < 4).map(s -> s.toUpperCase()).log();
        StepVerifier.create(namesfilter).expectNext("RAJ").verifyComplete();
    }

    @Test
    public void TransformUsingFlatmap() {
        Flux<String> stringFlux = Flux.fromIterable(Arrays.asList("A", "B", "C", "D", "E", "F"))
                .flatMap(s -> {
                    return Flux.fromIterable(convertToList(s));
                        }
                ).log();
        StepVerifier.create(stringFlux).expectNextCount(12).verifyComplete();

    }

    private List<String> convertToList(String s) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Arrays.asList(s,"new value");

    }



//    @Test
//    public void TransforUsing_parallel(){
//        Flux<String> stringFlux = Flux.fromIterable(Arrays.asList("A","B","C","D","E","F"))
//                .window(2).flatMap((s->{
//                            s.map(this::convertToList).subscribeOn();
//                        }
//                        )
//
//        .flatMap(s->)
//    }

}
