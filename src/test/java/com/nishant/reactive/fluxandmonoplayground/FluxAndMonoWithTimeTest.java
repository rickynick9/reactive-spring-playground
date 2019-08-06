package com.nishant.reactive.fluxandmonoplayground;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

/**
 * @author Nishant Nikunja
 */
public class FluxAndMonoWithTimeTest {

    @Test
    public void infiniteSequence() throws InterruptedException {
        Flux<Long> infiniteFlux = Flux.interval(Duration.ofMillis(200))
                .log();

        // every 200ms its going to generate the value.
        //The values are going to start from 0...

        infiniteFlux.subscribe((ele) -> {
            System.out.println("Value is : "+ele);
        });

        Thread.sleep(3000);
    }


    @Test
    public void infiniteSequence_test() throws InterruptedException {
        Flux<Long> finiteFlux = Flux.interval(Duration.ofMillis(200))
                .take(3)
                .log();

        //its going to emit 3 elements and then its going to come out of
        //finite flux.

        StepVerifier.create(finiteFlux)
                .expectSubscription()
                .expectNext(0L,1L,2L)
                .verifyComplete();
        //verifyComplete is like subscribe call and its going to wait until all these
        //events are published from the Flux.
    }


    @Test
    public void infiniteSequence_test_map() throws InterruptedException {
        Flux<Integer> finiteFlux = Flux.interval(Duration.ofMillis(200))
                .map(l -> l.intValue()) //map Long to Integer
                .take(3)
                .log();

         StepVerifier.create(finiteFlux)
                .expectSubscription()
                .expectNext(0,1,2)
                .verifyComplete();
    }

    @Test
    public void infiniteSequence_test_map_withdelay() throws InterruptedException {
        Flux<Integer> finiteFlux = Flux.interval(Duration.ofMillis(200))
                .delayElements(Duration.ofSeconds(1))
                .map(l -> l.intValue()) //map Long to Integer
                .take(3)
                .log();

        //its going to emit 3 elements and then its going to come out of
        //finite flux.

        StepVerifier.create(finiteFlux)
                .expectSubscription()
                .expectNext(0,1,2)
                .verifyComplete();
        //verifyComplete is like subscribe call and its going to wait until all these
        //events are published from the Flux.
    }
}
