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
import java.util.function.Function;
import java.util.function.Supplier;

import jp.furplag.function.Trebuchet.TriFunction;

/**
 * {@link TriFunction} now get enable to throw {@link Throwable} .
 *
 * @author furplag
 *
 * @param <T> the type of the first argument to the function
 * @param <U> the type of the second argument to the function
 * @param <V> the type of the third argument to the function
 * @see TriFunction
 */
@FunctionalInterface
public interface ThrowableTriFunction<T, U, V, R>  extends TriFunction<T, U, V, R> {

  /**
   * returns the result of {@link #apply(Object, Object, Object) function.apply(T, U, V)} if done it normally, or fallen if error occurred .
   *
   * @param <T> the type of the first argument to the function
   * @param <U> the type of the second argument to the function
   * @param <V> the type of the third argument to the function
   * @param <R> the type of the result of the function
   * @param <W> the type of {@code fallen} of the function
   * @param t the value of the first argument to the function
   * @param u the value of the second argument to the function
   * @param v the value of the third argument to the function
   * @param function {@link Function}, may not be null
   * @param fallen the return value when the function returns null, may not be null
   * @return the result of {@link #apply(Object, Object, Object) function.apply(T, U, V)} if done it normally, or fallen if error occurred
   */
  static <T, U, V, R, W extends R> R applyOrDefault(final T t, final U u, final V v, final ThrowableTriFunction<? super T, ? super U, ? super V, ? extends R> function, final W fallen) {
    return Objects.requireNonNullElse(orElse(t, u, v, function, (ex) -> null), Objects.requireNonNull(fallen));
  }

  /**
   * should never write &quot;ugly&quot; try-catch block to handle {@link Throwable exceptions} in lambda expression .
   *
   * @param <T> the type of the first argument to the function
   * @param <U> the type of the second argument to the function
   * @param <V> the type of the third argument to the function
   * @param <R> the type of the result of the function
   * @param function {@link TriFunction}, may not be null
   * @param fallen {@link Function}, or the function that always return {@code null} if this is null
   * @return {@link ThrowableTriFunction}
   * @throws NullPointerException if {@code function} is null
   */
  @SuppressWarnings({ "unchecked" })
  static <T, U, V, R, E extends Throwable> ThrowableTriFunction<T, U, V, R> of(final ThrowableTriFunction<? super T, ? super U, ? super V, ? extends R> function, final Function<? super E, ? extends R> fallen) {
    Objects.requireNonNull(function);

    return (t, u, v) -> {/* @formatter:off */try {return function.apply(t, u, v);} catch (Throwable e) {return Trebuchet.defaults(fallen).apply((E) e);}/* @formatter:off */};
  }

  /**
   * should never write &quot;ugly&quot; try-catch block for {@link Throwable} (s) in stream .
   *
   * @param <T> the type of the first argument to the function
   * @param <U> the type of the second argument to the function
   * @param <V> the type of the third argument to the function
   * @param <R> the type of the result of the function
   * @param function {@link TriFunction}, may not be null
   * @param fallen {@link TriFunction}, or the function that always return {@code null} if this is null
   * @return {@link ThrowableTriFunction}
   * @throws NullPointerException if {@code function} is null
   */
  static <T, U, V, R> ThrowableTriFunction<T, U, V, R> of(final ThrowableTriFunction<? super T, ? super U, ? super V, ? extends R> function, final TriFunction<? super T, ? super U, ? super V, ? extends R> fallen) {
    Objects.requireNonNull(function);

    return (t, u, v) -> {/* @formatter:off */try {return function.apply(t, u, v);} catch (Throwable e) {return Trebuchet.defaults(fallen).apply(t, u, v);}/* @formatter:off */};
  }

  /**
   * returns the result of {@link #apply(Object, Object, Object) function.apply(T, U, V)} if done it normally, or fallen if error occurred .
   *
   * @param <T> the type of the first argument to the function
   * @param <U> the type of the second argument to the function
   * @param <V> the type of the third argument to the function
   * @param <R> the type of the result of the function
   * @param t the value of the first argument to the function
   * @param u the value of the second argument to the function
   * @param v the value of the third argument to the function
   * @param function {@link TriFunction}, may not be null
   * @param fallen the return value when error has occurred
   * @return the result of {@link #apply(Object, Object, Object) function.apply(T, U, V)} if done it normally, or fallen if error occurred
   */
  static <T, U, V, R, W extends R> R orDefault(final T t, final U u, final V v, final ThrowableTriFunction<? super T, ? super U, ? super V, ? extends R> function, final W fallen) {
    return orElseGet(t, u, v, function, () -> fallen);
  }

  /**
   * returns the result of {@link #apply(Object, Object, Object) function.apply(T, U, V)} if done it normally, or {@link Function#apply(Object) fallen.apply(E)} if error occurred .
   *
   * @param <T> the type of the first argument to the function
   * @param <U> the type of the second argument to the function
   * @param <V> the type of the third argument to the function
   * @param <R> the type of the result of the function
   * @param <E> anything thrown
   * @param t the value of the first argument to the function
   * @param u the value of the second argument to the function
   * @param v the value of the third argument to the function
   * @param function {@link TriFunction}, may not be null
   * @param fallen {@link Function}, or the function that always return {@code null} if this is null
   * @return the result of {@link #apply(Object, Object, Object) function.apply(T, U, V)} if done it normally, or {@link Function#apply(Object) fallen.apply(E)} if error occurred
   */
  static <T, U, V, R, E extends Throwable> R orElse(final T t, final U u, final V v, final ThrowableTriFunction<? super T, ? super U, ? super V, ? extends R> function, final Function<? super E, ? extends R> fallen) {
    return of(function, fallen).apply(t, u, v);
  }

  /**
   * returns the result of {@link #apply(Object, Object, Object) function.apply(T, U, V)} if done it normally, or {@link TriFunction#apply(Object, Object, Object) fallen.apply(T, U, V)} if error occurred .
   *
   * @param <T> the type of the first argument to the function
   * @param <U> the type of the second argument to the function
   * @param <V> the type of the third argument to the function
   * @param <R> the type of the result of the function
   * @param t the value of the first argument to the function
   * @param u the value of the second argument to the function
   * @param v the value of the third argument to the function
   * @param function {@link TriFunction}, may not be null
   * @param fallen {@link TriFunction}, or the function that always return {@code null} if this is null
   * @return the result of {@link #apply(Object, Object, Object) function.apply(T, U, V)} if done it normally, or {@link TriFunction#apply(Object, Object, Object) fallen.apply(T, U, V)} if error occurred
   */
  static <T, U, V, R> R orElse(final T t, final U u, final V v, final ThrowableTriFunction<? super T, ? super U, ? super V, ? extends R> function, final TriFunction<? super T, ? super U, ? super V, ? extends R> fallen) {
    return of(function, fallen).apply(t, u, v);
  }

  /**
   * returns the result of {@link #apply(Object, Object, Object) function.apply(T, U, V)} if done it normally, or {@link Supplier#get() fallen.get()} if error occurred .
   *
   * @param <T> the type of the first argument to the function
   * @param <U> the type of the second argument to the function
   * @param <V> the type of the third argument to the function
   * @param <R> the type of the result of the function
   * @param t the value of the first argument to the function
   * @param u the value of the second argument to the function
   * @param v the value of the third argument to the function
   * @param function {@link TriFunction}, may not be null
   * @param fallen {@link Supplier}, or the function that always return {@code null} if this is null
   * @return the result of {@link #apply(Object, Object, Object) function.apply(T, U, V)} if done it normally, or {@link Supplier#get() fallen.get()} if error occurred
   */
  static <T, U, V, R> R orElseGet(final T t, final U u, final V v, final ThrowableTriFunction<? super T, ? super U, ? super V, ? extends R> function, final Supplier<? extends R> fallen) {
    return of(function, (ex) -> Trebuchet.defaults(fallen).get()).apply(t, u, v);
  }

  /**
   * returns the result of {@link #apply(Object, Object, Object) function.apply(T, U, V)} if done it normally, or {@code null} if error occurred .
   *
   * @param <T> the type of the first argument to the function
   * @param <U> the type of the second argument to the function
   * @param <V> the type of the third argument to the function
   * @param <R> the type of the result of the function
   * @param t the value of the first argument to the function
   * @param u the value of the second argument to the function
   * @param v the value of the third argument to the function
   * @param function {@link TriFunction}, may not be null
   * @return the result of {@link #apply(Object, Object, Object) function.apply(T, U, V)} if done it normally, or {@code null} if error occurred
   */
  static <T, U, V, R> R orNull(final T t, final U u, final V v, final ThrowableTriFunction<? super T, ? super U, ? super V, ? extends R> function) {
    return orElseGet(t, u, v, function, () -> null);
  }

  /**
   * returns a composed function that first applies this function to its input, and then applies the {@code after} function to the result .
   * if evaluation of either function throws an exception, it is relayed to the caller of the composed function.
   *
   * @param <W> the type of output of the {@code after} function, and of the composed function
   * @param after the function to apply after this function is applied
   * @return a composed function that first applies this function and then applies the {@code after} function
   * @throws NullPointerException if after is null
   */
  default <W> ThrowableTriFunction<T, U, V, W> andThen(Function<? super R, ? extends W> after) {
    return (t, u, v) -> Trebuchet.defaults(after).apply(apply(t, u, v));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  default R apply(T t, U u, V v) {/* @formatter:off */try {return applyOrThrow(t, u, v);} catch (Throwable e) {Trebuchet.sneakyThrow(e);} return null;/* @formatter:on */}

  /**
   * applies this function to the given argument .
   *
   * @param t the value of the first argument to the function
   * @param u the value of the second argument to the function
   * @param v the value of the third argument to the function
   * @return the function result
   * @throws Throwable anything thrown
   */
  R applyOrThrow(T t, U u, V v) throws Throwable;
}
