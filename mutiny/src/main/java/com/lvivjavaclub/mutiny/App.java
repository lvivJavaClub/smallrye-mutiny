package com.lvivjavaclub.mutiny;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

import java.time.Duration;
import java.util.List;
import java.util.Random;

public class App {
  public static void main(String[] args) {

    // Example #4
    Multi<Integer> multiLong = Multi.createFrom().items(1, 2, 3, 4)
        .onItem().transform(i -> i * i);
    Uni<List<Integer>> listUni = multiLong.collect().asList();
    List<Integer> indefinitely = listUni.await().indefinitely();
    System.out.println(indefinitely);

    // Example #3
    Multi.createFrom().ticks().every(Duration.ofSeconds(1))
        .onItem().transform(i -> i * i) // map
          .select().when(i -> Uni.createFrom().item(i % 3 == 0)) // filter
          .select().first(3) // limit
        .subscribe().with(
          System.out::println,
          Throwable::printStackTrace,
          () -> System.out.println("end")
        );

    // Example #2
    Multi.createFrom().<Integer>emitter(e -> {
          e.emit(1);
          e.emit(2);
          e.emit(3);
          e.complete();
        })
        .onItem().transform(i -> i * i)
        .onSubscribe().invoke(() -> {
          System.out.println("onSubscribe");
          System.out.println(Thread.currentThread().getName());
        })
        .onCompletion().invoke(() -> System.out.println("onCompletion"))
        .subscribe().with(
          System.out::println,
          Throwable::printStackTrace,
          () -> System.out.println("end")
        );


    // Example #1
    Uni.createFrom().item("qwe")
        .onItem().transform(App::toObject)
        .onFailure().retry().atMost(1)
        .onSubscribe().invoke(() -> System.out.println("onSubscribe"))
        .subscribe()
        .with(
            System.out::println,
            Throwable::printStackTrace
        );
  }

  private static String toObject(String u) {
    // http call
    if (new Random().nextBoolean()) { // if not 200 OK
      System.out.println("throw new RuntimeException");
      throw new RuntimeException("OPS");
    }

    // if 200 OK
    return u.toUpperCase();
  }
}
