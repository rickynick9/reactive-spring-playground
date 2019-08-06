package com.nishant.reactive.fluxandmonoplayground;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

/**
 * @author Nishant Nikunja
 */
public class FluxAndMonoErrorTest {
    @Test
    public void fluxErrorHandling_onerror_map_with_retry() {
        Flux<String> stringFlux = Flux.just("A","B","C")
                .concatWith(Flux.error(new RuntimeException("Exception occured")))
                .concatWith(Flux.just("D"))
                .onErrorMap(e -> new CustomException(e))
                //.retry(2)
                .retryBackoff(2, Duration.ofSeconds(5))
                .log();

        StepVerifier.create(stringFlux)
                .expectSubscription()
                .expectNext("A","B","C")
                .expectNext("A","B","C")
                .expectNext("A","B","C") //we are retrying it 2 time so we have
                // multiple expectNext
                //.expectError(CustomException.class)
                //If you look at the logs, retry is exhausted. And we get  IllegalStateException
                // instead CustomException
                .expectError(IllegalStateException.class)
                .verify();
    }


    @Test
    public void fluxErrorHandling() {
        Flux<String> stringFlux = Flux.just("A","B","C")
                .concatWith(Flux.error(new RuntimeException("Exception occured")))
                .concatWith(Flux.just("D"))
                .onErrorResume((e) -> { //this block gets executed when there is some error
                    System.out.println("Exception is : "+e);
                    return Flux.just("default", "default1");
                })
                .log();

        StepVerifier.create(stringFlux)
                .expectSubscription()
                .expectNext("A","B","C")

                //.expectError(RuntimeException.class)
                .expectNext("default","default1")
                .verifyComplete();
    }

    @Test
    public void fluxErrorHandling_onerror_return() {
        Flux<String> stringFlux = Flux.just("A","B","C")
                .concatWith(Flux.error(new RuntimeException("Exception occured")))
                .concatWith(Flux.just("D"))
                .onErrorReturn("default")
                .log();

        StepVerifier.create(stringFlux)
                .expectSubscription()
                .expectNext("A","B","C","default")
                .verifyComplete();
    }

    @Test
    public void fluxErrorHandling_onerror_map() {
        Flux<String> stringFlux = Flux.just("A","B","C")
                .concatWith(Flux.error(new RuntimeException("Exception occured")))
                .concatWith(Flux.just("D"))
                .onErrorMap(e -> new CustomException(e))
                .log();

        StepVerifier.create(stringFlux)
                .expectSubscription()
                .expectNext("A","B","C")
                .expectError(CustomException.class)
                .verify();
    }

}
