package com.nishant.reactive.fluxandmonoplayground;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * @author Nishant Nikunja
 */
public class FluxAndmonoTest {

    @Test
    public void fluxTest(){
        //This is a Flux of type String
        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
                .concatWith(Flux.error(new RuntimeException("Exception Occured")))
                .concatWith(Flux.just("After Error"))
                .log();
        //The only way to access the elements from Flux is by subscribing to it.


        stringFlux
                .subscribe(System.out::println,
                        (e) -> System.err.println(e),
                        () -> System.out.println("Completed"));
        //When you subscribe then you are actively attaching a Subscriber which is going to
        // read all the values from Flux. WHen you subscribe thats when the Flux will start
        // emitting the values to the subscriber.
    }

    @Test
    public void fluxTestElements_withoutError() {
        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
                .log();
        StepVerifier.create(stringFlux)
                .expectNext("Spring")
                .expectNext("Spring Boot")
                .expectNext("Reactive Spring")
                .verifyComplete(); // -> Since there are no errors, we are going to verify
                //onComplete
    }

    @Test
    public void fluxTestElements_withError() {
        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
                .concatWith(Flux.error(new RuntimeException("Exception occured")))
                .log();
        StepVerifier.create(stringFlux)
                .expectNext("Spring")
                .expectNext("Spring Boot")
                .expectNext("Reactive Spring")
                .expectError(RuntimeException.class)
                .verify();
                //.verifyComplete(); // -> Since there are no errors, we are going to verify
        //onComplete
    }

    @Test
    public void fluxTestElementsCount_withError() {
        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
                .concatWith(Flux.error(new RuntimeException("Exception occured")))
                .log();
        StepVerifier.create(stringFlux)
                .expectNextCount(3)
                .expectErrorMessage("Exception occured")
                .verify();
        //.verifyComplete(); // -> Since there are no errors, we are going to verify
        //onComplete
    }

    @Test
    public void monoTest() {
        Mono<String> stringMono = Mono.just("Spring").log();
        StepVerifier.create(stringMono)
                .expectNext("Spring")
                .verifyComplete();
    }

    @Test
    public void monoTest_Error() {
        //Since there is just one element in Mono, we don't need to create Mono using
        // Mono.just
        StepVerifier.create(Mono.error(new RuntimeException("Exception occured")).log())
                .expectError(RuntimeException.class)
                .verify();
    }
}
