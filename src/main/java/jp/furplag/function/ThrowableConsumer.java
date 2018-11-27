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
import java.util.function.Consumer;

/**
 * {@link Consumer} now get enable to throw {@link Throwable} .
 *
 * @author furplag
 *
 * @param <T> the type of the input to the operation
 * @see Consumer
 */
@FunctionalInterface
public interface ThrowableConsumer<T> extends Consumer<T> {

  /**
   * should never write &quot;ugly&quot; try-catch block to handle {@link Throwable exceptions} in lambda expression .
   *
   * @param <T> the type of the input to the operation
   * @param <E> anything thrown
   * @param consumer {@link Consumer}, may not be null
   * @param fallen {@link BiConsumer}, do nothing if this is null
   * @return {@link ThrowableConsumer}
   * @throws NullPointerException if {@code consumer} is null
   */
  @SuppressWarnings({ "unchecked" })
  static <T, E extends Throwable> ThrowableConsumer<T> of(final ThrowableConsumer<? super T> consumer, final BiConsumer<? super T, ? super E> fallen) {
    Objects.requireNonNull(consumer);

    return (t) -> {/* @formatter:off */try {consumer.accept(t);} catch (Throwable e) {Trebuchet.defaults(fallen).accept(t, (E) e);}/* @formatter:off */};
  }

  /**
   * should never write &quot;ugly&quot; try-catch block to handle {@link Throwable exceptions} in lambda expression .
   *
   * @param <T> the type of the input to the operation
   * @param consumer {@link Consumer}, may not be null
   * @param fallen {@link Consumer}, do nothing if this is null
   * @return {@link ThrowableConsumer}
   */
  static <T> ThrowableConsumer<T> of(final ThrowableConsumer<? super T> consumer, final Consumer<? super T> fallen) {
    return of(consumer, (t, e) -> Objects.requireNonNullElse(fallen, (x) -> {/* do nothing . */}).accept(t));
  }

  /**
   * {@link #accept(Object) consumer.accept(T)} if done it normally, or {@link BiConsumer#accept(Object, Object) fallen.accept(T, E)} if error occurred .
   *
   * @param <T> the type of the input to the operation
   * @param <E> anything thrown
   * @param t the value of the input to the operation
   * @param consumer {@link Consumer}, may not be null
   * @param fallen {@link BiConsumer}, do nothing if this is null
   */
  static <T, E extends Throwable> void orElse(final T t, final ThrowableConsumer<? super T> consumer, final BiConsumer<? super T, ? super E> fallen) {
    of(consumer, fallen).accept(t);
  }

  /**
   * {@link #accept(Object) consumer.accept(T)} if done it normally, or {@link Consumer#accept(Object) fallen.accept(T)} if error occurred .
   *
   * @param <T> the type of the input to the operation
   * @param t the value of the input to the operation
   * @param consumer {@link Consumer}, may not be null
   * @param fallen {@link Consumer}, do nothing if this is null
   */
  static <T> void orElse(final T t, final ThrowableConsumer<? super T> consumer, final Consumer<? super T> fallen) {
    orElse(t, consumer, (x, ex) -> Trebuchet.defaults(fallen).accept(x));
  }

  /**
   * mute out any exceptions whether the operation throws it .
   *
   * @param <T> the type of the input to the operation
   * @param t the value of the input to the operation
   * @param consumer {@link Consumer}, may not be null
   */
  static <T> void orNot(final T t, final ThrowableConsumer<? super T> consumer) {
    orElse(t, consumer, (Consumer<T>) null);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  default void accept(T t) {/* @formatter:off */try {acceptOrThrow(t);} catch (Throwable e) {Trebuchet.sneakyThrow(e);}/* @formatter:on */}

  /**
   * performs this operation on the given arguments.
   *
   * @param t the value of the input to the operation
   * @throws Throwable anything thrown
   */
  void acceptOrThrow(T t) throws Throwable;

  /**
   * {@inheritDoc}
   *
   * @throws NullPointerException if {@code after} is null
   */
  @Override
  default ThrowableConsumer<T> andThen(Consumer<? super T> after) {
    Objects.requireNonNull(after);

    return (t) -> {/* @formatter:off */try {acceptOrThrow(t); after.accept(t);} catch (Throwable e) {Trebuchet.sneakyThrow(e);}/* @formatter:on */};
  }
}
