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
import java.util.function.Consumer;

import jp.furplag.function.Trebuchet.TriConsumer;

/**
 * {@link TriConsumer} now get enable to throw {@link Throwable} .
 *
 * @author furplag
 *
 * @param <T> the type of the first argument to the operation
 * @param <U> the type of the second argument to the operation
 * @param <V> the type of the third argument to the operation
 * @see TriConsumer
 */
@FunctionalInterface
public interface ThrowableTriConsumer<T, U, V> extends TriConsumer<T, U, V> {

  /**
   * should never write &quot;ugly&quot; try-catch block to handle {@link Throwable exceptions} in lambda expression .
   *
   * @param <T> the type of the first argument to the operation
   * @param <U> the type of the second argument to the operation
   * @param <V> the type of the third argument to the operation
   * @param consumer {@link TriConsumer}, may not be null
   * @param fallen {@link TriConsumer}, do nothing if this is null
   * @return {@link ThrowableTriConsumer}
   * @throws NullPointerException if {@code consumer} is null
   */
  static <T, U, V> ThrowableTriConsumer<T, U, V> of(final ThrowableTriConsumer<? super T, ? super U, ? super V> consumer, final TriConsumer<? super T, ? super U, ? super V> fallen) {
    Objects.requireNonNull(consumer);

    return (t, u, v) -> {/* @formatter:off */try {consumer.accept(t, u, v);} catch (Throwable e) {Trebuchet.defaults(fallen).accept(t, u, v);}/* @formatter:off */};
  }

  /**
   * should never write &quot;ugly&quot; try-catch block to handle {@link Throwable exceptions} in lambda expression .
   *
   * @param <T> the type of the first argument to the operation
   * @param <U> the type of the second argument to the operation
   * @param <V> the type of the third argument to the operation
   * @param <E> anything thrown
   * @param consumer {@link TriConsumer}, may not be null
   * @param fallen {@link Consumer}, do nothing if this is null
   * @return {@link ThrowableTriConsumer}
   * @throws NullPointerException if {@code consumer} is null
   */
  @SuppressWarnings({ "unchecked" })
  static <T, U, V, E extends Throwable> ThrowableTriConsumer<T, U, V> of(final ThrowableTriConsumer<? super T, ? super U, ? super V> consumer, final Consumer<? super E> fallen) {
    Objects.requireNonNull(consumer);

    return (t, u, v) -> {/* @formatter:off */try {consumer.accept(t, u, v);} catch (Throwable e) {Trebuchet.defaults(fallen).accept((E) e);}/* @formatter:off */};
  }

  /**
   * {@link #accept(Object, Object, Object) function.accept(T, U, V)} if done it normally, or {@link TriConsumer#accept(Object, Object, Object) fallen.accept(T, U, V)} if error occurred .
   *
   * @param <T> the type of the first argument to the operation
   * @param <U> the type of the second argument to the operation
   * @param <V> the type of the third argument to the operation
   * @param t the value of the first argument to the operation
   * @param u the value of the second argument to the operation
   * @param v the value of the third argument to the operation
   * @param consumer {@link TriConsumer}, may not be null
   * @param fallen {@link TriConsumer}, do nothing if this is null
   */
  static <T, U, V> void orElse(final T t, final U u, final V v, final ThrowableTriConsumer<? super T, ? super U, ? super V> consumer, final TriConsumer<? super T, ? super U, ? super V> fallen) {
    of(consumer, fallen).accept(t, u, v);
  }

  /**
   * {@link #accept(Object, Object, Object) function.accept(T, U, V)} if done it normally, or {@link Consumer#accept(Object) fallen.accept(E)} if error occurred .
   *
   * @param <T> the type of the first argument to the operation
   * @param <U> the type of the second argument to the operation
   * @param <V> the type of the third argument to the operation
   * @param <E> anything thrown
   * @param t the value of the first argument to the operation
   * @param u the value of the second argument to the operation
   * @param v the value of the third argument to the operation
   * @param consumer {@link TriConsumer}, may not be null
   * @param fallen {@link Consumer}, may not be null
   */
  static <T, U, V, E extends Throwable> void orElse(final T t, final U u, final V v, final ThrowableTriConsumer<? super T, ? super U, ? super V> consumer, final Consumer<? super E> fallen) {
    of(consumer, fallen).accept(t, u, v);
  }

  /**
   * mute out any exceptions whether the operation throws it .
   *
   * @param <T> the type of the first argument to the operation
   * @param <U> the type of the second argument to the operation
   * @param <V> the type of the third argument to the operation
   * @param t the value of the first argument to the operation
   * @param u the value of the second argument to the operation
   * @param v the value of the third argument to the operation
   * @param consumer {@link TriConsumer}, may not be null
   */
  static <T, U, V> void orNot(final T t, final U u, final V v, final ThrowableTriConsumer<? super T, ? super U, ? super V> consumer) {
    of(consumer, (TriConsumer<T, U, V>) null).accept(t, u, v);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  default void accept(T t, U u, V v) {/* @formatter:off */try {acceptOrThrow(t, u, v);} catch (Throwable e) {Trebuchet.sneakyThrow(e);}/* @formatter:on */}

  /**
   * performs this operation on the given arguments .
   *
   * @param t the value of the first argument to the operation
   * @param u the value of the second argument to the operation
   * @param v the value of the third argument to the operation
   * @throws Throwable anything thrown
   */
  void acceptOrThrow(T t, U u, V v) throws Throwable;

  /**
   * {@inheritDoc}
   *
   * @throws NullPointerException if {@code after} is null
   */
  @Override
  default ThrowableTriConsumer<T, U, V> andThen(TriConsumer<? super T, ? super U, ? super V> after) {
    return (t, u, v) -> {/* @formatter:off */accept(t, u, v); Trebuchet.defaults(after).accept(t, u, v);/* @formatter:on */};
  }
}
