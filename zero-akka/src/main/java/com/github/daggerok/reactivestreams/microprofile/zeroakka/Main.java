package com.github.daggerok.reactivestreams.microprofile.zeroakka;

import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;

import java.util.concurrent.CompletionStage;

public class Main {
    public static void main(String[] args) {
        ActorSystem actorSystem = ActorSystem.create();
        Materializer materializer = ActorMaterializer.create(actorSystem);
        CompletionStage<String> s = Source.single(1)
                                          .map(String::valueOf)
                                          .runWith(Sink.head(), materializer);
        s.thenAccept(System.out::println);
        actorSystem.terminate();
    }
}
