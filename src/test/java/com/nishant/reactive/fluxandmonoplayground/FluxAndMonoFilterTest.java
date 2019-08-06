package com.nishant.reactive.fluxandmonoplayground;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author Nishant Nikunja
 */
public class FluxAndMonoFilterTest {

    List<String> names = Arrays.asList("Nishant", "Nikunja", "Ricky", "Nick");


    @Test
    public void filterTest() {
         //create Flux from the above list
        Flux<String> stringFlux = Flux.fromIterable(names)
                .filter(s->s.length() > 6) // Ricky is filtered out
                .log();

        StepVerifier.create(stringFlux)
                .expectNext("Nishant", "Nikunja")
                .verifyComplete();
    }
}
