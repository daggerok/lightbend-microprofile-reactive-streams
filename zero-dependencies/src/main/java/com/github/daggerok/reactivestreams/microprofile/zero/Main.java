package com.github.daggerok.reactivestreams.microprofile.zero;

import org.eclipse.microprofile.reactive.streams.operators.ReactiveStreams;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class Main {
    public static void main(String[] args) {
        ReactiveStreams.of("one", "two", "three", "four", "five")
                       .map("and "::concat)
                       .reduce((s1, s2) -> s1 + " " + s2)
                       .run()
                       .acceptEither(CompletableFuture.supplyAsync(Optional::empty),
                                     optional -> optional.ifPresent(System.out::println));
    }
}
