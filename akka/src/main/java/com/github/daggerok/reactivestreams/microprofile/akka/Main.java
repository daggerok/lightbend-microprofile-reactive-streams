package com.github.daggerok.reactivestreams.microprofile.akka;

import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;
import akka.stream.javadsl.Source;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Source<String, NotUsed> source = Source.from(Arrays.asList("one", "two", "three", "four", "five"))
                                               .map("and "::concat)
                                               .reduce((s1, s2) -> s1 + " " + s2);
        ActorSystem actorSystem = ActorSystem.create();
        Materializer materializer = ActorMaterializer.create(actorSystem);
        source.runForeach(System.out::println, materializer);
        actorSystem.terminate();
    }
}
