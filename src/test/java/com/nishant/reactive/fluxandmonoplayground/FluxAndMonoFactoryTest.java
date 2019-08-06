package com.nishant.reactive.fluxandmonoplayground;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * @author Nishant Nikunja
 */
public class FluxAndMonoFactoryTest {

    //Going to create Flux from ArrayList
    List<String> names = Arrays.asList("Nishant", "Nikunja", "Ricky", "Nick");

    @Test
    public void fluxUsingIterable() {
        Flux<String> namesFlux = Flux.fromIterable(names)
                .log();
        StepVerifier.create(namesFlux)

                .expectNext("Nishant", "Nikunja", "Ricky", "Nick")
                .verifyComplete();
    }

    @Test
    public void fluxUsingArray() {
        String[] names = new String[]{"Nishant", "Nikunja", "Ricky", "Nick"};
        Flux<String> namesFlux = Flux.fromArray(names);
        StepVerifier.create(namesFlux)
                .expectNext("Nishant", "Nikunja", "Ricky", "Nick")
                .verifyComplete();


    }

    @Test
    public void fluxUsingStream() {
        Flux<String> stringFlux = Flux.fromStream(names.stream());
        StepVerifier.create(stringFlux)
                .expectNext("Nishant", "Nikunja", "Ricky", "Nick")
                .verifyComplete();
    }

    @Test
    public void monoUsingJustOrEmpty() {
        Mono<String> stringMono = Mono.justOrEmpty(null);
        StepVerifier.create(stringMono.log())
                .verifyComplete();
        //Since there is nothing in Mono so we cannot expect anything i.e expectNext()
    }

    @Test
    public void monoUsingSupplier() {
        //Lets implement functional interface Supplier
        Supplier<String> stringSupplier = () -> "Nishant";
        // So the above Supplier implementation, whenever you call is going to return string.

        //In general, to get the value from above Supplier implementation we can do the following
        System.out.println(stringSupplier.get());
        //stringSupplier.get() -> this is completely taken care behind the scenes by
        //Mono.fromSupplier

        Mono<String> stringMono = Mono.fromSupplier(stringSupplier);
        StepVerifier.create(stringMono.log())
                .expectNext("Nishant")
                .verifyComplete();
    }

    @Test
    public void fluxUsingRange() {
        //Its going to return values from 1 to 5
        Flux<Integer> integerFlux = Flux.range(1, 5);
        StepVerifier.create(integerFlux.log())
                .expectNext(1,2,3,4,5)
                .verifyComplete();
    }
}
