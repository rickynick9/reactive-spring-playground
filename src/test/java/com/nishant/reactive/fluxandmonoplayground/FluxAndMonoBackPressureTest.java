package com.nishant.reactive.fluxandmonoplayground;

import org.junit.Test;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
 * @author Nishant Nikunja
 */
public class FluxAndMonoBackPressureTest {

    @Test
    public void backPressureTest() {
        Flux<Integer> finiteFlux = Flux.range(1,10)
                .log();

        //I want the subscriber to take control. Subscriber here is StepVerifier.
        //You cannot do verifyComplete

        StepVerifier.create(finiteFlux)
                .expectSubscription()
                .thenRequest(1)
                .expectNext(1)
                .thenRequest(1)
                .expectNext(2)
                .thenCancel()
                .verify();
    }

    @Test
    public void backPressure(){
        Flux<Integer> finiteFlux = Flux.range(1,10)
                .log();
        finiteFlux.subscribe(
                (element) -> System.out.println("Element is : "+element),
                (e) -> System.err.println("Exception : "+e.getMessage()),
                () -> System.out.println("Done"),
                (subscription -> subscription.request(2))
        );
        //'Done' will be executed when there is onComplete event. When the publishing is camcelled
        //then onComplete event will not be triggered.
        //after 2 elements are emitted, Flux is done with its job.
    }

    @Test
    public void backPressure_Cancel(){
        Flux<Integer> finiteFlux = Flux.range(1,10)
                .log();
        finiteFlux.subscribe(
                (element) -> System.out.println("Element is : "+element),
                (e) -> System.err.println("Exception : "+e.getMessage()),
                () -> System.out.println("Done"),
                (subscription -> subscription.cancel())
        );

        // subscription is an interface which has request and cancel method using which
        // we can control the data flow.
        //In this case nothing will be printed in console, immediately after subscribe
        // we are cancelling the subscription.
    }

    @Test
    public void customizeBackPressure() {
        Flux<Integer> finiteFlux = Flux.range(1,10)
                .log();

        finiteFlux.subscribe(new BaseSubscriber<Integer>() {
            @Override
            protected void hookOnNext(Integer value) {
                request(1);
                System.out.println("Value received is : "+value);
                if (value == 4) {
                    cancel();
                }
                //super.hookOnNext(value);
            }
        });

        //this implementation is about taking more control from Publisher and how the data
        //should flow from Publisher
    }
}
