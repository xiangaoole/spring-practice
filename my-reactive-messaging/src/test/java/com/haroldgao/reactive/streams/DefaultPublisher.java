package com.haroldgao.reactive.streams;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import java.util.LinkedList;
import java.util.List;

/**
 * Default implementation of {@link Publisher}
 *
 * @param <T> data type to publish
 */
public class DefaultPublisher<T> implements Publisher<T> {

    private List<SubscriberWrapper<? super T>> subscribers = new LinkedList<>();

    @Override
    public void subscribe(Subscriber<? super T> subscriber) {
        DefaultSubscription subscription = new DefaultSubscription(subscriber);
        subscriber.onSubscribe(subscription);
        subscribers.add(new SubscriberWrapper<>(subscription, subscriber));
    }

    public void publish(T data) {
        for (Subscriber<? super T> subscriber : subscribers) {
            SubscriberWrapper subscriberWrapper = (SubscriberWrapper) subscriber;
            if (subscriberWrapper.getSubscription().isCanceled()) {
                System.out.println("not send for canceled: " + data);
                continue;
            }
            subscriber.onNext(data);
        }
    }

    public void error(Throwable t) {
        for (Subscriber<? super T> subscriber : subscribers) {
            subscriber.onError(t);
        }
    }

    public void complete() {
        for (Subscriber<? super T> subscriber : subscribers) {
            subscriber.onComplete();
        }
    }

    public static void main(String[] args) {
        DefaultPublisher<Integer> publisher = new DefaultPublisher<>();
        publisher.subscribe(new DefaultSubscriber<>());
        for (int i = 0; i < 5; i++) {
            publisher.publish(i);
        }
    }
}
