package com.github.daggerok.reactivestreams.microprofile.zeroakka;

import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;
import com.lightbend.microprofile.reactive.streams.akka.AkkaEngine;
import org.eclipse.microprofile.reactive.streams.operators.ReactiveStreams;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        ActorSystem actorSystem = ActorSystem.create();
        Materializer materializer = ActorMaterializer.create(actorSystem);
        AkkaEngine engine = new AkkaEngine(materializer);
        ReactiveStreams.of("one", "two", "three", "four", "five")
                       .map("and "::concat)
                       .reduce((s1, s2) -> s1 + " " + s2)
                       .run(engine)
                       .acceptEither(CompletableFuture.supplyAsync(Optional::empty),
                                     optional -> optional.ifPresent(System.out::println));
        actorSystem.terminate();
    }
}
