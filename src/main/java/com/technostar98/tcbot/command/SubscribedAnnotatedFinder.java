package com.technostar98.tcbot.command;

import api.filter.event.Event;
import api.filter.event.SubscribedEvent;
import com.google.common.base.Objects;
import com.google.common.base.Throwables;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.reflect.TypeToken;
import com.google.common.util.concurrent.UncheckedExecutionException;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Google Code & Bret 'Horfius' Dusseault
 * @version 1.0
 */
public class SubscribedAnnotatedFinder implements SubscriberFindingStrategy {

    /**
     * A thread-safe cache that contains the mapping from each class to all methods in that class and
     * all super-classes, that are annotated with {@code SubscribedEvent}. The cache is shared across all
     * instances of this class; this greatly improves performance if multiple EventBus instances are
     * created and objects of the same class are registered on all of them.
     */
    private static final LoadingCache<Class<? extends Event>, ImmutableList<Method>> subscriberMethodsCache =
        CacheBuilder.newBuilder()
            .weakKeys()
            .build(new CacheLoader<Class<? extends Event>, ImmutableList<Method>>() {
                @Override
                public ImmutableList<Method> load(Class<? extends Event> concreteClass) throws Exception {
                    return getAnnotatedMethodsInternal(concreteClass);
                }
            });


    private static ImmutableList<Method> getAnnotatedMethods(Class<? extends Event> clazz) {
        try {
            return subscriberMethodsCache.getUnchecked(clazz);
        } catch (UncheckedExecutionException e) {
            throw Throwables.propagate(e.getCause());
        }
    }

    private static final class MethodIdentifier {
        private final String name;
        private final List<Class<?>> parameterTypes;

        MethodIdentifier(Method method) {
            this.name = method.getName();
            this.parameterTypes = Arrays.asList(method.getParameterTypes());
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(name, parameterTypes);
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof MethodIdentifier) {
                MethodIdentifier ident = (MethodIdentifier) o;
                return name.equals(ident.name) && parameterTypes.equals(ident.parameterTypes);
            }
            return false;
        }
    }

    private static ImmutableList<Method> getAnnotatedMethodsInternal(Class<? extends Event> clazz) {
        Set<? extends Class<?>> supers = TypeToken.of(clazz).getTypes().rawTypes();
        Map<MethodIdentifier, Method> identifiers = Maps.newHashMap();
        for (Class<?> superClazz : supers) {
            for (Method superClazzMethod : superClazz.getMethods()) {
                if (superClazzMethod.isAnnotationPresent(SubscribedEvent.class)
                        && !superClazzMethod.isBridge()) {
                    Class<?>[] parameterTypes = superClazzMethod.getParameterTypes();
                    if (parameterTypes.length != 1 || (parameterTypes.length > 0 &&!Event.class.isAssignableFrom(parameterTypes[0]))) {
                        throw new IllegalArgumentException("Method " + superClazzMethod
                                + " has @SubscribedEvent annotation, but requires " + parameterTypes.length
                                + " arguments.  Event subscriber methods must require a single argument.");
                    }

                    MethodIdentifier ident = new MethodIdentifier(superClazzMethod);
                    if (!identifiers.containsKey(ident)) {
                        identifiers.put(ident, superClazzMethod);
                    }
                }
            }
        }
        return ImmutableList.copyOf(identifiers.values());
    }

    /**
     * Creates an {@code EventSubscriber} for subsequently calling {@code method} on
     * {@code listener}.
     * Selects an EventSubscriber implementation based on the annotations on
     * {@code method}.
     *
     * @param listener  object bearing the event subscriber method.
     * @param method  the event subscriber method to wrap in an EventSubscriber.
     * @return an EventSubscriber that will call {@code method} on {@code listener}
     *         when invoked.
     */
    private static EventSubscriber makeSubscriber(Object listener, Method method) {
        return new EventSubscriber(listener, method);
    }

    @Override
    public Multimap<Class<? extends Event>, EventSubscriber> findAllSubscribers(Object listener) {
        Multimap<Class<? extends Event>, EventSubscriber> methodsInListener = HashMultimap.create();
        Class<?> clazz = listener.getClass();
        for (Method method : getAnnotatedMethods((Class<? extends Event>)clazz)) {
            Class<?>[] parameterTypes = method.getParameterTypes();
            Class<? extends Event> eventType = (Class<? extends Event>)parameterTypes[0];
            EventSubscriber subscriber = makeSubscriber(listener, method);
            methodsInListener.put(eventType, subscriber);
        }
        return methodsInListener;
    }
}
