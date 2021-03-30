package com.haroldgao.reactive.streams;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/**
 * Default implementation of {@link Subscriber}
 *
 * @param <T> data type to accept
 */
public class DefaultSubscriber<T> implements Subscriber<T> {

    private static final int MAX_DEMAND = 3;

    private Subscription subscription;

    private int demand = 0;

    @Override
    public void onSubscribe(Subscription s) {
        this.subscription = s;
    }

    @Override
    public void onNext(T t) {
        System.out.println("get next: " + (t == null ? "null" : t.toString()));
        demand++;
        if (demand >= MAX_DEMAND) {
            subscription.cancel();
        }
    }

    @Override
    public void onError(Throwable t) {
        System.out.println("get error: " + t.toString());
    }

    @Override
    public void onComplete() {
        System.out.println("complete");
    }
}
