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

import jp.furplag.function.Trebuchet.TriConsumer;

/**
 * {@link BiConsumer} now get enable to throw {@link Throwable} .
 *
 * @author furplag
 *
 * @param <T> the type of the first argument to the operation
 * @param <U> the type of the second argument to the operation
 * @see BiConsumer
 */
@FunctionalInterface
public interface ThrowableBiConsumer<T, U> extends BiConsumer<T, U> {

  /**
   * should never write &quot;ugly&quot; try-catch block to handle {@link Throwable exceptions} in lambda expression .
   *
   * @param <T> the type of the first argument to the operation
   * @param <U> the type of the second argument to the operation
   * @param <E> anything thrown
   * @param consumer {@link BiConsumer}, may not be null
   * @param fallen {@link TriConsumer}, do nothing if this is null
   * @return {@link ThrowableBiConsumer}
   * @throws NullPointerException if {@code consumer} is null
   */
  @SuppressWarnings({ "unchecked" })
  static <T, U, E extends Throwable> ThrowableBiConsumer<T, U> of(final ThrowableBiConsumer<? super T, ? super U> consumer, final TriConsumer<? super T, ? super U, ? super E> fallen) {
    Objects.requireNonNull(consumer);

    return (t, u) -> {/* @formatter:off */try {consumer.accept(t, u);} catch (Throwable e) {Trebuchet.defaults(fallen).accept(t, u, (E) e);}/* @formatter:off */};
  }

  /**
   * should never write &quot;ugly&quot; try-catch block to handle {@link Throwable exceptions} in lambda expression .
   *
   * @param <T> the type of the first argument to the operation
   * @param <U> the type of the second argument to the operation
   * @param consumer {@link BiConsumer}, may not be null
   * @param fallen {@link BiConsumer}, do nothing if this is null
   * @return {@link ThrowableBiConsumer}
   */
  static <T, U> ThrowableBiConsumer<T, U> of(final ThrowableBiConsumer<? super T, ? super U> consumer, final BiConsumer<? super T, ? super U> fallen) {
    return of(consumer, (t, u, e) -> Trebuchet.defaults(fallen).accept(t, u));
  }

  /**
   * {@link #accept(Object, Object) consumer.accept(T, U)} if done it normally, or {@link TriConsumer#accept(Object, Object, Object) fallen.accept(T, U, E)} if error occurred .
   *
   * @param <T> the type of the first argument to the operation
   * @param <U> the type of the second argument to the operation
   * @param <E> anything thrown
   * @param t the value of the first argument to the operation
   * @param u the value of the second argument to the operation
   * @param consumer {@link BiConsumer}, may not be null
   * @param fallen {@link TriConsumer}, do nothing if this is null
   */
  static <T, U, E extends Throwable> void orElse(final T t, final U u, final ThrowableBiConsumer<? super T, ? super U> consumer, final TriConsumer<? super T, ? super U, ? super E> fallen) {
    of(consumer, fallen).accept(t, u);
  }

  /**
   * {@link #accept(Object, Object) consumer.accept(T, U)} if done it normally, or {@link BiConsumer#accept(Object, Object) fallen.accept(T, U)} if error occurred .
   *
   * @param <T> the type of the first argument to the operation
   * @param <U> the type of the second argument to the operation
   * @param t the value of the first argument to the operation
   * @param u the value of the second argument to the operation
   * @param consumer {@link BiConsumer}, may not be null
   * @param fallen {@link BiConsumer}, do nothing if this is null
   */
  static <T, U> void orElse(final T t, final U u, final ThrowableBiConsumer<? super T, ? super U> consumer, final BiConsumer<? super T, ? super U> fallen) {
    orElse(t, u, consumer, (x, y, ex) -> Trebuchet.defaults(fallen).accept(x, y));
  }

  /**
   * mute out any exceptions whether the operation throws it .
   *
   * @param <T> the type of the first argument to the operation
   * @param <U> the type of the second argument to the operation
   * @param t the value of the first argument to the operation
   * @param u the value of the second argument to the operation
   * @param consumer {@link BiConsumer}, may not be null
   */
  static <T, U> void orNot(final T t, final U u, final ThrowableBiConsumer<? super T, ? super U> consumer) {
    orElse(t, u, consumer, (BiConsumer<T, U>) null);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  default void accept(T t, U u) {/* @formatter:off */try {acceptOrThrow(t, u);} catch (Throwable e) {Trebuchet.sneakyThrow(e);}/* @formatter:on */}

  /**
   * performs this operation on the given arguments .
   *
   * @param t the value of the first argument to the operation
   * @param u the value of the second argument to the operation
   * @throws Throwable anything thrown
   */
  void acceptOrThrow(T t, U u) throws Throwable;

  /**
   * {@inheritDoc}
   *
   * @throws NullPointerException if {@code after} is null
   */
  @Override
  default ThrowableBiConsumer<T, U> andThen(BiConsumer<? super T, ? super U> after) {
    Objects.requireNonNull(after);

    return (t, u) -> {/* @formatter:off */try {acceptOrThrow(t, u); after.accept(t, u);} catch (Throwable e) {Trebuchet.sneakyThrow(e);}/* @formatter:on */};
  }
}
