package com.nishant.reactive.fluxandmonoplayground;

import org.junit.Test;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

import java.time.Duration;

/**
 * @author Nishant Nikunja
 */
public class ColdAndHotPublisherTest {

    @Test
    public void coldPublisherTest() throws InterruptedException {
        //create a sting Flux and then delay each element by 1 second
        Flux<String> stringFlux = Flux.just("A", "B", "C", "D", "E", "F")
                .delayElements(Duration.ofSeconds(1));

        //how data will flow if there are multiple subscribers ?
        stringFlux.subscribe(s -> System.out.println("subscriber 1 : "+s));

        Thread.sleep(1000);

        stringFlux.subscribe(s -> System.out.println("subscriber 2 : "+s));

        Thread.sleep(6000);

        //every time a new subscriber is assigned, Flux emits the value from the begininning.
        // This is called cold Publisher.
        //Hot publisher is opposite to the cold publisher, it is not going to emit the value
        // from the begininning if any new subscriber gets added to the Flux.
    }

    @Test
    public void hotPublisherTest() throws InterruptedException {
        //create a sting Flux and then delay each element by 1 second
        Flux<String> stringFlux = Flux.just("A", "B", "C", "D", "E", "F")
                .delayElements(Duration.ofSeconds(1));

        ConnectableFlux<String> connectableFlux = stringFlux.publish();
        connectableFlux.connect();
        //This is a Hot publisher. Now you can add any number of subscriber to this.

        connectableFlux.subscribe(s-> System.out.println("Subscriber 1 : "+s));

        Thread.sleep(3000);

        connectableFlux.subscribe(s-> System.out.println("Subscriber 2 : "+s));

        Thread.sleep(4000);
    }

}
