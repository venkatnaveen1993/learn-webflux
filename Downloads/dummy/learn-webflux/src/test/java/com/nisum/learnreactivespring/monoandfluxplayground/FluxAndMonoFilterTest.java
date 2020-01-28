package com.nisum.learnreactivespring.monoandfluxplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

public class FluxAndMonoFilterTest {
    List<String> names = Arrays.asList("naveen","raj","ramesh");
    @Test
    public void filterTest(){
        Flux<String> namesflux = Flux.fromIterable(names)
                .filter(s -> s.startsWith("r")).log();
        StepVerifier.create(namesflux).expectNext("raj","ramesh").verifyComplete();
    }
    @Test
    public void filtertLength(){
        Flux<String> namelength = Flux.fromIterable(names).filter(s -> s.length()<4).log();
        StepVerifier.create(namelength).expectNext("raj").verifyComplete();
    }
}
