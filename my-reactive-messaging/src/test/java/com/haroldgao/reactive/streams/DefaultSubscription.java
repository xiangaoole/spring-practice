package com.haroldgao.reactive.streams;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/**
 * Default implementation of {@link Subscription}.
 *
 * One to one with {@link Subscriber}.
 *
 */
public class DefaultSubscription implements Subscription {

    private boolean canceled = false;

    private final Subscriber<?> subscriber;

    public DefaultSubscription(Subscriber<?> subscriber) {
        this.subscriber = subscriber;
    }

    @Override
    public void request(long n) {

    }

    @Override
    public void cancel() {
        canceled = true;
    }

    public boolean isCanceled() {
        return canceled;
    }
}
