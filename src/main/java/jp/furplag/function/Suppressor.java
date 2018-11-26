/**
 * Copyright (C) 2018+ furplag (https://github.com/furplag)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jp.furplag.function;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import jp.furplag.function.Trebuchet.TriConsumer;
import jp.furplag.function.Trebuchet.TriFunction;

public interface Suppressor {

  static <T> void muted(final T t, final ThrowableConsumer<? super T> consumer) {
    otherwise(t, consumer, Trebuchet.defaults((Consumer<?>) null));
  }

  static <T, U> void muted(final T t, final U u, final ThrowableBiConsumer<? super T, ? super U> consumer) {
    otherwise(t, u, consumer, Trebuchet.defaults((BiConsumer<?, ?>) null));
  }

  static <T, U, V> void muted(final T t, final U u, final V v, final ThrowableTriConsumer<? super T, ? super U, ? super V> consumer) {
    otherwise(t, u, v, consumer, Trebuchet.defaults((TriConsumer<?, ?, ?>) null));
  }

  static <T, R, U extends R> R orDefault(final T t, final ThrowableFunction<? super T, ? extends R> function, final U fallen) {
    return ThrowableFunction.orDefault(t, function, fallen);
  }

  static <T, U, R, V extends R> R orDefault(final T t, final U u, final ThrowableBiFunction<? super T, ? super U, ? extends R> function, final V fallen) {
    return ThrowableBiFunction.orDefault(t, u, function, fallen);
  }

  static <T, U, V, R, W extends R> R orDefault(final T t, final U u, final V v, final ThrowableTriFunction<? super T, ? super U, ? super V, ? extends R> function, final W fallen) {
    return ThrowableTriFunction.orDefault(t, u, v, function, fallen);
  }

  static <T, R, E extends Throwable> R orElse(final T t, final ThrowableFunction<? super T, ? extends R> function, final BiFunction<? super T, ? super E, ? extends R> fallen) {
    return ThrowableFunction.orElse(t, function, fallen);
  }

  static <T, R> R orElse(final T t, final ThrowableFunction<? super T, ? extends R> function, final Function<? super T, ? extends R> fallen) {
    return ThrowableFunction.orElse(t, function, fallen);
  }

  static <T, U, R> R orElse(final T t, final U u, final ThrowableBiFunction<? super T, ? super U, ? extends R> function, final BiFunction<? super T, ? super U, ? extends R> fallen) {
    return ThrowableBiFunction.orElse(t, u, function, fallen);
  }

  static <T, U, R, E extends Throwable> R orElse(final T t, final U u, final ThrowableBiFunction<? super T, ? super U, ? extends R> function, final TriFunction<? super T, ? super U, ? super E, ? extends R> fallen) {
    return ThrowableBiFunction.orElse(t, u, function, fallen);
  }

  static <T, U, V, R, E extends Throwable> R orElse(final T t, final U u, final V v, final ThrowableTriFunction<? super T, ? super U, ? super V, ? extends R> function, final Function<? super E, ? extends R> fallen) {
    return ThrowableTriFunction.orElse(t, u, v, function, fallen);
  }

  static <T, U, V, R> R orElse(final T t, final U u, final V v, final ThrowableTriFunction<? super T, ? super U, ? super V, ? extends R> function, final TriFunction<? super T, ? super U, ? super V, ? extends R> fallen) {
    return ThrowableTriFunction.orElse(t, u, v, function, fallen);
  }

  static <T, R, U extends R> R orElseGet(final T t, final ThrowableFunction<? super T, ? extends R> function, final Supplier<U> fallen) {
    return ThrowableFunction.orElseGet(t, function, fallen);
  }

  static <T, U, R, V extends R> R orElseGet(final T t, final U u, final ThrowableBiFunction<? super T, ? super U, ? extends R> function, Supplier<V> fallen) {
    return ThrowableBiFunction.orElseGet(t, u, function, fallen);
  }

  static <T, U, V, R, W extends R> R orElseGet(final T t, final U u, final V v, final ThrowableTriFunction<? super T, ? super U, ? super V, ? extends R> function, final Supplier<W> fallen) {
    return ThrowableTriFunction.orElseGet(t, u, v, function, fallen);
  }

  static <T> boolean orNot(final T t, final ThrowablePredicate<? super T> predicate) {
    return Objects.requireNonNullElse(orNull(t, predicate), false);
  }

  static <T, U> boolean orNot(final T t, final U u, final ThrowableBiPredicate<? super T, ? super U> predicate) {
    return Objects.requireNonNullElse(orNull(t, u, predicate), false);
  }

  static <T, U, V> boolean orNot(final T t, final U u, final V v, final ThrowableTriPredicate<? super T, ? super U, ? super V> predicate) {
    return Objects.requireNonNullElse(orNull(t, u, v, predicate), false);
  }

  static <T, R, U extends R> R orNull(final T t, final ThrowableFunction<? super T, ? extends R> function) {
    return ThrowableFunction.orElse(t, function, (x) -> null);
  }

  static <T, U, R, V extends R> R orNull(final T t, final U u, final ThrowableBiFunction<? super T, ? super U, ? extends R> function) {
    return ThrowableBiFunction.orElse(t, u, function, (x, y) -> null);
  }

  static <T, U, V, R, W extends R> R orNull(final T t, final U u, final V v, final ThrowableTriFunction<? super T, ? super U, ? super V, ? extends R> function) {
    return ThrowableTriFunction.orElse(t, u, v, function, (x, y, z) -> null);
  }

  static <T, E extends Throwable> void otherwise(final T t, final ThrowableConsumer<? super T> consumer, final BiConsumer<? super T, ? super E> fallen) {
    ThrowableConsumer.orElse(t, consumer, fallen);
  }

  static <T> void otherwise(final T t, final ThrowableConsumer<? super T> consumer, final Consumer<? super T> fallen) {
    ThrowableConsumer.orElse(t, consumer, fallen);
  }

  static <T, U> void otherwise(final T t, final U u, final ThrowableBiConsumer<? super T, ? super U> consumer, final BiConsumer<? super T, ? super U> fallen) {
    ThrowableBiConsumer.orElse(t, u, consumer, fallen);
  }

  static <T, U, E extends Throwable> void otherwise(final T t, final U u, final ThrowableBiConsumer<? super T, ? super U> consumer, final TriConsumer<? super T, ? super U, ? super E> fallen) {
    ThrowableBiConsumer.orElse(t, u, consumer, fallen);
  }

  static <T, U, V, E extends Throwable> void otherwise(final T t, final U u, final V v, final ThrowableTriConsumer<? super T, ? super U, ? super V> consumer, final Consumer<? super E> fallen) {
    ThrowableTriConsumer.orElse(t, u, v, consumer, fallen);
  }

  static <T, U, V> void otherwise(final T t, final U u, final V v, final ThrowableTriConsumer<? super T, ? super U, ? super V> consumer, final TriConsumer<? super T, ? super U, ? super V> fallen) {
    ThrowableTriConsumer.orElse(t, u, v, consumer, fallen);
  }
}
