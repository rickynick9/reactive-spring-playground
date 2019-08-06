package com.nishant.reactive.fluxandmonoplayground;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static reactor.core.scheduler.Schedulers.parallel;

/**
 * @author Nishant Nikunja
 */
public class FluxAndMonoTransformTest {

    List<String> names = Arrays.asList("Nishant", "Nikunja", "Ricky", "Nick");

    @Test
    public void transformUsingMap() {
        Flux<String> stringFlux = Flux.fromIterable(names)
                .map(s -> s.toUpperCase())
                .log();

        StepVerifier.create(stringFlux)
                .expectNext("NISHANT", "NIKUNJA", "RICKY", "NICK")
                .verifyComplete();
    }

    @Test
    public void transformUsingMap_Length() {
        Flux<Integer> stringFlux = Flux.fromIterable(names)
                .map(s->s.length())
                .log();

        StepVerifier.create(stringFlux)
                .expectNext(7,7,5,4)
                .verifyComplete();

    }

    //lets filter names based on length and then map/transform to uppercase

    @Test
    public void transformUsingMap_Filter() {
        Flux<String> stringFlux = Flux.fromIterable(names)
                .filter(s->s.length()>5)
                .map(s->s.toUpperCase())
                .log();

        StepVerifier.create(stringFlux)
                .expectNext("NISHANT", "NIKUNJA")
                .verifyComplete();
    }

    @Test
    public void transformUsingFlatMap() {
        Flux<String> stringFlux = Flux.fromIterable(Arrays.asList("A","B","C","D","E","F"))
                .flatMap(s -> {
                    //lets assume that its a DB call
                    return Flux.fromIterable(convertToList(s));
                })
                .log();

        StepVerifier.create(stringFlux)
                .expectNextCount(12)
                .verifyComplete();

    }

    @Test
    public void transformUsingFlatMap_Parallel() {
        Flux<String> stringFlux = Flux.fromIterable(Arrays.asList("A","B","C","D","E","F"))
                .window(2) //its going to wait for 2 elements before passing to flatMap
                //earlier Flux was passing element one by one. Noe elements will be passed
                //in pair. So it will be passed as Flux<Flux<String>>
                .flatMap(s-> {
                    return s.map(this::convertToList).subscribeOn(parallel()); //Flux<List<String>>
                })
                .flatMap(s1-> Flux.fromIterable(s1)) //Flux<String>
                .log();

        StepVerifier.create(stringFlux)
                .expectNextCount(12)
                .verifyComplete();

    }


    @Test
    public void transformUsingFlatMap_parallel_maintain_order() {
        Flux<String> stringFlux = Flux.fromIterable(Arrays.asList("A","B","C","D","E","F"))
                .window(2) //its going to wait for 2 elements before passing to flatMap
                //earlier Flux was passing element one by one. Noe elements will be passed
                //in pair. So it will be passed as Flux<Flux<String>>
                .flatMapSequential(s-> {
                    return s.map(this::convertToList).subscribeOn(parallel()); //Flux<List<String>>
                })
                .flatMap(s1-> Flux.fromIterable(s1)) //Flux<String>
                .log();

        StepVerifier.create(stringFlux)
                .expectNextCount(12)
                .verifyComplete();

    }


    private List<String> convertToList(String s) {
        try {

            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return Arrays.asList(s, "New Value");
    }


}
