package com.nisum.learnreactivespring.monoandfluxplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class FluxAndMonoUsingFactoryMethods {
    List<String> names = Arrays.asList("naveen","raj","ramesh");
    @Test
    public void fluxUsingIterable(){
        Flux<String> stringFlux = Flux.fromIterable(names).log();
        StepVerifier.create(stringFlux).expectNext("naveen","raj","ramesh").verifyComplete();
    }
    @Test
    public void fluxUsingArrays(){
        String[] names = new String[]{"naveen","raj","ramesh"};
        Flux<String> arryFlux = Flux.fromArray(names).log();
        StepVerifier.create(arryFlux).expectNext("naveen","raj","ramesh").verifyComplete();


    }
    @Test
    public void fluxUsingStream(){
        Flux<String> fluxStream = Flux.fromStream(names.stream()).log();
        StepVerifier.create(fluxStream).expectNext("naveen","raj","ramesh").verifyComplete();
    }
      @Test
    public void monoUsingJustorEmpty(){
          Mono<String> mono = Mono.justOrEmpty(null);
          StepVerifier.create(mono).verifyComplete();

      }
      @Test
    public void monoUsingSuppliers(){
          Supplier<String> stringSupplier= ()->"admin";
          Mono<String> stringMono = Mono.fromSupplier(stringSupplier).log();
          StepVerifier.create(stringMono).expectNext("admin").verifyComplete();

    }
    @Test
    public void fluxUsingRange(){
        Flux<Integer> stringRange = Flux.range(1,5).log();
        StepVerifier.create(stringRange).expectNext(1,2,3,4,5).verifyComplete();
    }

}
