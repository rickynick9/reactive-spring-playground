package com.nishant.reactive.fluxandmonoplayground;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.scheduler.VirtualTimeScheduler;

import java.time.Duration;

/**
 * @author Nishant Nikunja
 */
public class FluxAndMonoCombineTest {

    @Test
    public void combineUsingMerge() {
        Flux<String> flux1 = Flux.just("A","B","C");
        Flux<String> flux2 = Flux.just("D","E","F");

        Flux<String> mergedFlux = Flux.merge(flux1, flux2);

        //I'm going to add a new assert here i.e expectSubscription()
        //subscription is the first thing that happens. When you subscribe its going to make
        // a call to Flux.
        StepVerifier.create(mergedFlux.log())
                .expectSubscription()
                .expectNext("A","B","C","D","E","F")
                .verifyComplete();
    }

    //Now I'm going to introduce some delay so that it takes time for the elements to
    // come out of flux1 and flux2

    @Test
    public void combineUsingMerge_withdelay() {
        Flux<String> flux1 = Flux.just("A","B","C").delayElements(Duration.ofSeconds(1));
        Flux<String> flux2 = Flux.just("D","E","F").delayElements(Duration.ofSeconds(1));

        Flux<String> mergedFlux = Flux.merge(flux1, flux2);

        //I'm going to add a new assert here i.e expectSubscription()
        //subscription is the first thing that happens. When you subscribe its going to make
        // a call to Flux.
        StepVerifier.create(mergedFlux.log())
                .expectSubscription()
                .expectNextCount(6)
                //.expectNext("A","B","C","D","E","F")
                .verifyComplete();
    }

    @Test
    public void combineUsingMerge_withdelay_withvirtualtime() {
        //its going to enable virtual time for this particular test case
        VirtualTimeScheduler.getOrSet();

        Flux<String> flux1 = Flux.just("A","B","C").delayElements(Duration.ofSeconds(1));
        Flux<String> flux2 = Flux.just("D","E","F").delayElements(Duration.ofSeconds(1));

        Flux<String> mergedFlux = Flux.merge(flux1, flux2);

        //we are supplying the Flux withVirtualTime() method which accepts Supplier interface
        // Supplier interface is a Functional interface
        //You have to make this call thenAwait, If you don't then virtual time is
        //not going to work.

        StepVerifier.withVirtualTime(() -> mergedFlux.log())
                .expectSubscription()
                .thenAwait(Duration.ofSeconds(6))
                //because we have 5 elements with 1sec delay
                .expectNextCount(6)
                .verifyComplete();

        //If you run this test case, it is executed in 1 second. It did not take
        // 6 seconds to complete this test case.
    }

    @Test
    public void combineUsingMergeZip() {
        Flux<String> flux1 = Flux.just("A","B","C");
        Flux<String> flux2 = Flux.just("D","E","F");

        Flux<String> mergedFlux = Flux.zip(flux1, flux2, (t1,t2) -> {
            return t1.concat(t2);
        });

        StepVerifier.create(mergedFlux.log())
                .expectSubscription()
                .expectNext("AD", "BE", "CF")
                .verifyComplete();
    }

}
