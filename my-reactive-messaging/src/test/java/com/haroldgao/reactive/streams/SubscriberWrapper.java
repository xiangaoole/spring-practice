package com.haroldgao.reactive.streams;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class SubscriberWrapper<T> implements Subscriber<T> {

    private final DefaultSubscription subscription;

    private final Subscriber<T> subscriber;

    public SubscriberWrapper(DefaultSubscription subscription, Subscriber<T> subscriber) {
        this.subscription = subscription;
        this.subscriber = subscriber;
    }

    @Override
    public void onSubscribe(Subscription s) {
        subscriber.onSubscribe(s);
    }

    @Override
    public void onNext(T t) {
        subscriber.onNext(t);
    }

    @Override
    public void onError(Throwable t) {
        subscriber.onError(t);
    }

    @Override
    public void onComplete() {
        subscriber.onComplete();
    }

    public DefaultSubscription getSubscription() {
        return subscription;
    }

}
