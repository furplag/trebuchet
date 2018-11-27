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
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * {@link Function} now get enable to throw {@link Throwable} .
 *
 * @author furplag
 *
 * @param <T> the type of the input to the function
 * @param <R> the type of the result of the function
 * @see Function
 */
@FunctionalInterface
public interface ThrowableFunction<T, R> extends Function<T, R> {

  /**
   * returns the result of {@link ThrowableFunction#apply(Object) function.apply(T)} if done it normally, or fallen if error occurred .
   *
   * @param <T> the type of the input to the function
   * @param <R> the type of the result of the function
   * @param <U> the type of {@code fallen} of the function
   * @param function {@link Function}, may not be null
   * @param fallen the return value when the function returns null, may not be null
   * @return the result of {@link ThrowableFunction#apply(Object) function.apply(T)} if done it normally, or fallen if error occurred
   */
  static <T, R, U extends R> R applyOrDefault(final T t, final ThrowableFunction<? super T, ? extends R> function, final U fallen) {
    return Objects.requireNonNullElse(orElse(t, function, (e) -> null), Objects.requireNonNull(fallen));
  }

  /**
   * returns a function that always returns its input argument .
   *
   * @param <T> the type of the input and output objects to the function
   * @return a function that always returns its input argument
   */
  static <T> Function<T, T> identity() {
    return t -> t;
  }

  /**
   * should never write &quot;ugly&quot; try-catch block to handle {@link Throwable exceptions} in lambda expression .
   *
   * @param <T> the type of the input to the function
   * @param <R> the type of the result of the function
   * @param <E> anything thrown
   * @param function {@link Function}, may not be null
   * @param fallen {@link BiFunction}, or the function that always return null if this is null
   * @return {@link ThrowableFunction}
   * @throws NullPointerException if {@code function} is null
   */
  @SuppressWarnings({ "unchecked" })
  static <T, R, E extends Throwable> ThrowableFunction<T, R> of(final ThrowableFunction<? super T, ? extends R> function, final BiFunction<? super T, ? super E, ? extends R> fallen) {
    Objects.requireNonNull(function);

    return (t) -> {/* @formatter:off */try {return function.apply(t);} catch (Throwable e) {return Trebuchet.defaults(fallen).apply(t, (E) e);}/* @formatter:off */};
  }

  /**
   * should never write &quot;ugly&quot; try-catch block to handle {@link Throwable exceptions} in lambda expression .
   *
   * @param <T> the type of the input to the function
   * @param <R> the type of the result of the function
   * @param function {@link Function}, may not be null
   * @param fallen {@link Function}, or the function that always return null if this is null
   * @return {@link ThrowableFunction}
   */
  static <T, R> ThrowableFunction<T, R> of(final ThrowableFunction<? super T, ? extends R> function, final Function<? super T, ? extends R> fallen) {
    return of(function, (t, e) -> Trebuchet.defaults(fallen).apply(t));
  }

  /**
   * returns the result of {@link #apply(Object) function.apply(T)} if done it normally, or fallen if error occurred .
   *
   * @param <T> the type of the input to the function
   * @param <R> the type of the result of the function
   * @param <U> the type of {@code fallen} of the function
   * @param t the function argument
   * @param function {@link Function}, may not be null
   * @param fallen the return value when error has occurred
   * @return the result of {@link #apply(Object) function.apply(T)} if done it normally, or fallen if error occurred
   */
  static <T, R, U extends R> R orDefault(final T t, final ThrowableFunction<? super T, ? extends R> function, final U fallen) {
    return orElseGet(t, function, () -> fallen);
  }

  /**
   * returns the result of {@link #apply(Object) function.apply(T)} if done it normally, or {@link BiFunction#apply(Object, Object) fallen.apply(T, E)} if error occurred .
   *
   * @param <T> the type of the input to the function
   * @param <R> the type of the result of the function
   * @param <E> anything thrown
   * @param t the function argument
   * @param function {@link Function}, may not be null
   * @param fallen {@link BiFunction}, or the function that always return null if this is null
   * @return the result of {@link #apply(Object) function.apply(T)} if done it normally, or {@link BiFunction#apply(Object, Object) fallen.apply(T, E)} if error occurred
   */
  static <T, R, E extends Throwable> R orElse(final T t, final ThrowableFunction<? super T, ? extends R> function, final BiFunction<? super T, ? super E, ? extends R> fallen) {
    return of(function, fallen).apply(t);
  }

  /**
   * returns the result of {@link #apply(Object) function.apply(T)} if done it normally, or {@link Function#apply(Object) fallen.apply(T)} if error occurred .
   *
   * @param <T> the type of the input to the function
   * @param <R> the type of the result of the function
   * @param t the function argument
   * @param function {@link Function}, may not be null
   * @param fallen {@link BiFunction}, or the function that always return null if this is null
   * @return the result of {@link #apply(Object) function.apply(T)} if done it normally, or {@link Function#apply(Object) fallen.apply(T)} if error occurred
   */
  static <T, R> R orElse(final T t, final ThrowableFunction<? super T, ? extends R> function, final Function<? super T, ? extends R> fallen) {
    return orElse(t, function, (x, ex) -> Trebuchet.defaults(fallen).apply(x));
  }

  /**
   * returns the result of {@link #apply(Object) function.apply(T)} if done it normally, or {@link Supplier#get() fallen.get()} if error occurred .
   *
   * @param <T> the type of the input to the function
   * @param <R> the type of the result of the function
   * @param t the function argument
   * @param function {@link Function}, may not be null
   * @param fallen {@link Supplier}
   * @return the result of {@link #apply(Object) function.apply(T)} if done it normally, or {@link Supplier#get() fallen.get()} if error occurred
   */
  static <T, R> R orElseGet(final T t, final ThrowableFunction<? super T, ? extends R> function, Supplier<? extends R> fallen) {
    return orElse(t, function, (x) -> Trebuchet.defaults(fallen).get());
  }

  /**
   * returns the result of {@link #apply(Object) function.apply(T)} if done it normally, or {@code null} if error occurred .
   *
   * @param <T> the type of the input to the function
   * @param <R> the type of the result of the function
   * @param t the function argument
   * @param function {@link Function}, may not be null
   * @return the result of {@link #apply(Object) function.apply(T)} if done it normally, or {@code null} if error occurred
   */
  static <T, R> R orNull(final T t, final ThrowableFunction<? super T, ? extends R> function) {
    return orElseGet(t, function, () -> null);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  default <V> ThrowableFunction<T, V> andThen(Function<? super R, ? extends V> after) {
    return (t) -> Trebuchet.defaults(after).apply(apply(t));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  default R apply(T t) {/* @formatter:off */try {return applyOrThrow(t);} catch (Throwable e) {Trebuchet.sneakyThrow(e);} return null;/* @formatter:on */}

  /**
   * Applies this function to the given argument.
   *
   * @param t the function argument
   * @return the function result
   * @throws Throwable anything thrown
   */
  R applyOrThrow(T t) throws Throwable;
}
