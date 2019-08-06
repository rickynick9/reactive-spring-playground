package com.nishant.reactive.fluxandmonoplayground;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.scheduler.VirtualTimeScheduler;

import java.time.Duration;

/**
 * @author Nishant Nikunja
 */
public class VirtualTimeTest {

    //without virtual time
    //in order for this test case to complete it took 3 seconds
    // If you have many test cases then its going to take lot of time
    // We have an option not to use machine clock. We can virtualize the clock.


    @Test
    public void testWithoutVirtualtime() {
        //every one second I'm going to have an event emitted
        Flux<Long> longFlux = Flux.interval(Duration.ofSeconds(1))
                .take(3);

        StepVerifier.create(longFlux.log())
                .expectSubscription()
                .expectNext(0L, 1L, 2L)
                .verifyComplete();
    }

    @Test
    public void testWithVirtualTime() {
        //This is the method which is going to enable virtual time.
        //This will make sure its not going to use the clock which is running in the machine.

        VirtualTimeScheduler.getOrSet();
        Flux<Long> longFlux = Flux.interval(Duration.ofSeconds(1))
                .take(3);

        //This is the Supplier interface which doesn't take any input
        // but it will supply with values
        //It could be anything but we are going to supply a Flux
        //If you run the test case it doesn't take 3 seconds to finish the test case
        // because of time virtualization.
        StepVerifier.withVirtualTime(() -> longFlux.log())
                .expectSubscription()
                .thenAwait(Duration.ofSeconds(3))
                .expectNext(0L, 1L, 2L)
                .verifyComplete();

    }

}
