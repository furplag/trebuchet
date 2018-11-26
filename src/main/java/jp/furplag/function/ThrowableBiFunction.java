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

import jp.furplag.function.Trebuchet.TriFunction;

/**
 * {@link BiFunction} now get enable to throw {@link Throwable} .
 *
 * @author furplag
 *
 * @param <T> the type of the first argument to the function
 * @param <U> the type of the second argument to the function
 * @param <R> the type of the result of the function
 * @see BiFunction
 */
@FunctionalInterface
public interface ThrowableBiFunction<T, U, R> extends BiFunction<T, U, R> {

  /**
   * returns the result of {@link #apply(Object, Object) function.apply(T, U)} if done it normally, or {@code fallen} if error occurred .
   *
   * @param <T> the type of the first argument to the function
   * @param <U> the type of the second argument to the function
   * @param <R> the type of the result of the function
   * @param <V> the type of {@code fallen} of the function
   * @param t the value of the first argument to the function
   * @param u the value of the second argument to the function
   * @param function {@link Function}, may not be null
   * @param fallen the return value when the function returns null, may not be null
   * @return the result of {@link ThrowableBiFunction#apply(Object, Object) function.apply(T, U)} if done it normally, or {@code fallen} if error occurred
   */
  static <T, U, R, V extends R> R applyOrDefault(final T t, final U u, final BiFunction<? super T, ? super U, ? extends R> function, final V fallen) {
    return Objects.requireNonNullElse(orElse(t, u, function, (ex) -> null), Objects.requireNonNull(fallen));
  }

  /**
   * should never write &quot;ugly&quot; try-catch block to handle {@link Throwable exceptions} in lambda expression .
   *
   * @param <T> the type of the first argument to the function
   * @param <U> the type of the second argument to the function
   * @param <R> the type of the result of the function
   * @param function {@link BiFunction}, may not be null
   * @param fallen {@link BiFunction}, or the function that always return {@code null} if this is null
   * @return {@link ThrowableBiFunction}
   */
  static <T, U, R> ThrowableBiFunction<T, U, R> of(final BiFunction<? super T, ? super U, ? extends R> function, final BiFunction<? super T, ? super U, ? extends R> fallen) {
    return of(function, (t, u, e) -> Trebuchet.defaults(fallen).apply(t, u));
  }

  /**
   * should never write &quot;ugly&quot; try-catch block to handle {@link Throwable exceptions} in lambda expression .
   *
   * @param <T> the type of the first argument to the function
   * @param <U> the type of the second argument to the function
   * @param <R> the type of the result of the function
   * @param <E> anything thrown
   * @param function {@link BiFunction}, may not be null
   * @param fallen {@link TriFunction}, or the function that always return {@code null} if this is null
   * @return {@link ThrowableBiFunction}
   */
  @SuppressWarnings({ "unchecked" })
  static <T, U, R, E extends Throwable> ThrowableBiFunction<T, U, R> of(final BiFunction<? super T, ? super U, ? extends R> function, final Function<? super E, ? extends R> fallen) {
    return of(function, (t, u, e) -> Trebuchet.defaults(fallen).apply((E) e));
  }

  /**
   * should never write &quot;ugly&quot; try-catch block to handle {@link Throwable exceptions} in lambda expression .
   *
   * @param <T> the type of the first argument to the function
   * @param <U> the type of the second argument to the function
   * @param <R> the type of the result of the function
   * @param <E> anything thrown
   * @param function {@link BiFunction}, may not be null
   * @param fallen {@link TriFunction}, or the function that always return {@code null} if this is null
   * @return {@link ThrowableBiFunction}
   * @throws NullPointerException if {@code function} is null
   */
  @SuppressWarnings({ "unchecked" })
  static <T, U, R, E extends Throwable> ThrowableBiFunction<T, U, R> of(final BiFunction<? super T, ? super U, ? extends R> function, final TriFunction<? super T, ? super U, ? super E, ? extends R> fallen) {
    Objects.requireNonNull(function);

    return (t, u) -> {/* @formatter:off */try {return function.apply(t, u);} catch (Throwable e) {return Trebuchet.defaults(fallen).apply(t, u, (E) e);}/* @formatter:off */};
  }

  /**
   * returns the result of {@link #apply(Object, Object) function.apply(T, U)} if done it normally, or fallen if error occurred .
   *
   * @param <T> the type of the first argument to the function
   * @param <U> the type of the second argument to the function
   * @param <R> the type of the result of the function
   * @param <V> the type of {@code fallen} of the function
   * @param t the value of the first argument to the function
   * @param u the value of the second argument to the function
   * @param function {@link Function}, may not be null
   * @param fallen the return value when error has occurred
   * @return the result of {@link ThrowableBiFunction#apply(Object, Object) function.apply(T, U)} if done it normally, or fallen if error occurred
   */
  static <T, U, R, V extends R> R orDefault(final T t, final U u, final BiFunction<? super T, ? super U, ? extends R> function, final V fallen) {
    return orElseGet(t, u, function, () -> fallen);
  }

  /**
   * returns the result of {@link #apply(Object, Object) function.apply(T, U)} if done it normally, or {@link BiFunction#apply(Object, Object) fallen.apply(T, U)} if error occurred .
   *
   * @param <T> the type of the first argument to the function
   * @param <U> the type of the second argument to the function
   * @param <R> the type of the result of the function
   * @param <E> anything thrown
   * @param t the value of the first argument to the function
   * @param u the value of the second argument to the function
   * @param function {@link BiFunction}, may not be null
   * @param fallen {@link BiFunction}, or the function that always return {@code null} if this is null
   * @return the result of {@link #apply(Object, Object) function.apply(T, U)} if done it normally, or {@link BiFunction#apply(Object, Object) fallen.apply(T, U)} if error occurred
   */
  static <T, U, R, E extends Throwable> R orElse(final T t, final U u, final BiFunction<? super T, ? super U, ? extends R> function, final BiFunction<? super T, ? super U, ? extends R> fallen) {
    return of(function, fallen).apply(t, u);
  }

  /**
   * returns the result of {@link #apply(Object, Object) function.apply(T, U)} if done it normally, or {@link Function#apply(Object) fallen.apply(E)} if error occurred .
   *
   * @param <T> the type of the first argument to the function
   * @param <U> the type of the second argument to the function
   * @param <R> the type of the result of the function
   * @param <E> anything thrown
   * @param t the value of the first argument to the function
   * @param u the value of the second argument to the function
   * @param function {@link BiFunction}, may not be null
   * @param fallen {@link TriFunction}, or the function that always return {@code null} if this is null
   * @return the result of {@link #apply(Object, Object) function.apply(T, U)} if done it normally, or {@link Function#apply(Object) fallen.apply(E)} if error occurred
   */
  static <T, U, R, E extends Throwable> R orElse(final T t, final U u, final BiFunction<? super T, ? super U, ? extends R> function, final Function<? super E, ? extends R> fallen) {
    return of(function, fallen).apply(t, u);
  }

  /**
   * returns the result of {@link #apply(Object, Object) function.apply(T, U)} if done it normally, or {@link TriFunction#apply(Object, Object, Object) fallen.apply(T, U, E)} if error occurred .
   *
   * @param <T> the type of the first argument to the function
   * @param <U> the type of the second argument to the function
   * @param <R> the type of the result of the function
   * @param <E> anything thrown
   * @param t the value of the first argument to the function
   * @param u the value of the second argument to the function
   * @param function {@link BiFunction}, may not be null
   * @param fallen {@link TriFunction}, or the function that always return {@code null} if this is null
   * @return the result of {@link #apply(Object, Object) function.apply(T, U)} if done it normally, or {@link TriFunction#apply(Object, Object, Object) fallen.apply(T, U, E)} if error occurred
   */
  static <T, U, R, E extends Throwable> R orElse(final T t, final U u, final BiFunction<? super T, ? super U, ? extends R> function, final TriFunction<? super T, ? super U, ? super E, ? extends R> fallen) {
    return of(function, fallen).apply(t, u);
  }

  /**
   * returns the result of {@link #apply(Object, Object) function.apply(T, U)} if done it normally, or {@link Supplier#get() fallen.get()} if error occurred .
   *
   * @param <T> the type of the first argument to the function
   * @param <U> the type of the second argument to the function
   * @param <R> the type of the result of the function
   * @param t the value of the first argument to the function
   * @param u the value of the second argument to the function
   * @param function {@link Function}, may not be null
   * @param fallen {@link Supplier}, or the function that always return {@code null} if this is null
   * @return the result of {@link #apply(Object, Object) function.apply(T, U)} if done it normally, or {@link Supplier#get() fallen.get()} if error occurred
   */
  static <T, U, R> R orElseGet(final T t, final U u, final BiFunction<? super T, U, ? extends R> function, Supplier<? extends R> fallen) {
    return orElse(t, u, function, (x, y, ex) -> Objects.requireNonNullElse(fallen, () -> null).get());
  }

  /**
   * returns the result of {@link #apply(Object, Object) function.apply(T, U)} if done it normally, or {@code null} if error occurred .
   *
   * @param <T> the type of the first argument to the function
   * @param <U> the type of the second argument to the function
   * @param <R> the type of the result of the function
   * @param t the value of the first argument to the function
   * @param u the value of the second argument to the function
   * @param function {@link Function}, may not be null
   * @return the result of {@link #apply(Object, Object) function.apply(T, U)} if done it normally, or {@code null} if error occurred
   */
  static <T, U, R> R orNull(final T t, final U u, final BiFunction<? super T, U, ? extends R> function) {
    return orElseGet(t, u, function, () -> null);
  }

  /**
   * {@inheritDoc}
   *
   * @throws NullPointerException if {@code after} is null
   */
  @Override
  default <V> BiFunction<T, U, V> andThen(Function<? super R, ? extends V> after) {
    Objects.requireNonNull(after);

    return (t, u) -> {/* @formatter:off */try {return after.apply(applyOrThrow(t, u));} catch (Throwable e) {Trebuchet.sneakyThrow(e);} return null;/* @formatter:on */};
  }

  /**
   * {@inheritDoc}
   */
  @Override
  default R apply(T t, U u) {/* @formatter:off */try {return applyOrThrow(t, u);} catch (Throwable e) {Trebuchet.sneakyThrow(e);} return null;/* @formatter:on */}

  /**
   * applies this function to the given argument .
   *
   * @param t the value of the first argument to the function
   * @param u the value of the second argument to the function
   * @return the function result
   * @throws Throwable anything thrown
   */
  R applyOrThrow(T t, U u) throws Throwable;
}
