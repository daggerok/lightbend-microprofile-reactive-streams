package com.github.daggerok.reactivestreams.microprofile.zeroakka;

import akka.Done;
import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.RunnableGraph;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MainTest {

    private ActorSystem system = ActorSystem.create();
    private Materializer mat = ActorMaterializer.create(system);

    @AfterEach
    void tearDown() {
        system.terminate();
    }

    @Test
    void test() throws Exception {
        Source<Integer, NotUsed> source = Source.single(123);
        Source<String, NotUsed> flow = source.map(String::valueOf);
        CompletionStage<String> sink = flow.runWith(Sink.head(), mat);
        String result = sink.toCompletableFuture()
                            .get(1, TimeUnit.SECONDS);
        assertThat(result).isEqualTo("123");
    }

    @Test
    void test_single() throws Exception {
        String result = Source.single(123).map(String::valueOf).runWith(Sink.head(), mat).toCompletableFuture().get(1,
                                                                                                                    TimeUnit.SECONDS);
        assertThat(result).isEqualTo("123");
    }

    @Test
    void test_create() {
        Flow<Integer, String, NotUsed> transformer = Flow.<Integer>create().map(String::valueOf);
        Sink<String, CompletionStage<Done>> consumer = Sink.foreach(System.out::println);
        RunnableGraph<NotUsed> businessLogic = Source.range(1, 5).via(transformer).to(consumer);
        businessLogic.run(mat);
    }
}
