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
import java.util.function.BiPredicate;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Predicate;

import jp.furplag.function.Trebuchet.TriFunction;
import jp.furplag.function.Trebuchet.TriPredicate;


/**
 * {@link TriPredicate} now get enable to throw {@link Throwable} .
 *
 * @author furplag
 *
 * @param <T> the type of the first argument to the predicate
 * @param <U> the type of the second argument to the predicate
 * @param <V> the type of the third argument to the predicate
 * @see TriPredicate
 * @see TriFunction
 */
@FunctionalInterface
public interface ThrowableTriPredicate<T, U, V> extends TriPredicate<T, U, V>, ThrowableTriFunction<T, U, V, Boolean> {

  /**
   * should never write &quot;ugly&quot; try-catch block to handle {@link Throwable exceptions} in lambda expression .
   *
   * @param <T> the type of the first argument to the predicate
   * @param <U> the type of the second argument to the predicate
   * @param <V> the type of the third argument to the predicate
   * @param predicate {@link TriPredicate}, may not be null
   * @param fallen {@link Predicate}, or the predicate that always return {@code null} if this is null
   * @return {@link ThrowableTriPredicate}
   */
  static <T, U, V, E extends Throwable> ThrowableTriPredicate<T, U, V> of(final ThrowableTriPredicate<? super T, ? super U, ? super V> predicate, final Predicate<? super E> fallen) {
    return (t, u, v) -> ThrowableTriFunction.of(predicate, Trebuchet.defaults(fallen)::test).apply(t, u, v);
  }

  /**
   * should never write &quot;ugly&quot; try-catch block to handle {@link Throwable exceptions} in lambda expression .
   *
   * @param <T> the type of the first argument to the predicate
   * @param <U> the type of the second argument to the predicate
   * @param <V> the type of the third argument to the predicate
   * @param predicate {@link TriPredicate}, may not be null
   * @param fallen {@link TriPredicate}, or the predicate that always return {@code null} if this is null
   * @return {@link ThrowableTriPredicate}
   */
  static <T, U, V> ThrowableTriPredicate<T, U, V> of(final ThrowableTriPredicate<? super T, ? super U, ? super V> predicate, final TriPredicate<? super T, ? super U, ? super V> fallen) {
    return (t, u, v) -> ThrowableTriFunction.of(predicate, Objects.requireNonNullElse(fallen, (x, y, z) -> false)).apply(t, u, v);
  }

  /**
   * returns the result of {@link #test(Object, Object, Object) predicate.test(T, U, V)} if done it normally, or fallen if error occurred .
   *
   * @param <T> the type of the first argument to the predicate
   * @param <U> the type of the second argument to the predicate
   * @param <V> the type of the third argument to the predicate
   * @param t the value of the first argument to the predicate
   * @param u the value of the second argument to the predicate
   * @param v the value of the third argument to the predicate
   * @param predicate {@link BiPredicate}, may not be null
   * @param fallen the return value when error has occurred
   * @return the result of {@link #test(Object, Object, Object) predicate.test(T, U, V)} if done it normally, or fallen if error occurred
   */
  static <T, U, V> boolean orDefault(final T t, final U u, final V v, final ThrowableTriPredicate<? super T, ? super U, ? super V> predicate, final boolean fallen) {
    return orElseGet(t, u, v, predicate, () -> fallen);
  }

  /**
   * returns the result of {@link #test(Object, Object, Object) predicate.test(T, U, V)} if done it normally, or {@link BooleanSupplier#getAsBoolean() fallen.getAsBoolean()} if error occurred .
   *
   * @param <T> the type of the first argument to the predicate
   * @param <U> the type of the second argument to the predicate
   * @param <V> the type of the third argument to the predicate
   * @param t the value of the first argument to the predicate
   * @param u the value of the second argument to the predicate
   * @param v the value of the third argument to the predicate
   * @param predicate {@link BiPredicate}, may not be null
   * @param fallen {@link BooleanSupplier}, or the function that always return {@code false} if this is null
   * @return the result of {@link #test(Object, Object, Object) predicate.test(T, U, V)} if done it normally, or {@link BooleanSupplier#getAsBoolean() fallen.getAsBoolean()} if error occurred
   */
  static <T, U, V> boolean orElseGet(final T t, final U u, final V v, final ThrowableTriPredicate<? super T, ? super U, ? super V> predicate, final BooleanSupplier fallen) {
    return ThrowableTriFunction.orElseGet(t, u, v, predicate::test, Objects.requireNonNullElse(fallen, () -> false)::getAsBoolean);
  }

  /**
   * returns the result of {@link #test(Object, Object, Object) predicate.test(T, U, V)} if done it normally, or {@code false} if error occurred .
   *
   * @param <T> the type of the first argument to the predicate
   * @param <U> the type of the second argument to the predicate
   * @param t the value of the first argument to the predicate
   * @param u the value of the second argument to the predicate
   * @param predicate {@link BiPredicate}, may not be null
   * @return the result of {@link #test(Object, Object, Object) predicate.test(T, U, V)} if done it normally, or {@code false} if error occurred
   */
  static <T, U, V> boolean orNot(final T t, final U u, final V v, final ThrowableTriPredicate<? super T, ? super U, ? super V> predicate) {
    return orElseGet(t, u, v, predicate, () -> false);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  default ThrowableTriPredicate<T, U, V> and(TriPredicate<? super T, ? super U, ? super V> other) {
    return (t, u, v) -> test(t, u, v) && Objects.requireNonNullElse(Trebuchet.defaults(other).apply(t, u, v), false);
  }

  /**
   * Throws {@link UnsupportedOperationException} .
   *
   * @return never
   * @throws UnsupportedOperationException as this operation is not supported
   */
  @Override
  default <W> ThrowableTriFunction<T, U, V, W> andThen(Function<? super Boolean, ? extends W> after) {
    throw new UnsupportedOperationException();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  default ThrowableTriPredicate<T, U, V> negate() {
    return (t, u, v) -> !test(t, u, v);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  default ThrowableTriPredicate<T, U, V> or(TriPredicate<? super T, ? super U, ? super V> other) {
    return (t, u, v) -> test(t, u, v) || Objects.requireNonNullElse(Trebuchet.defaults(other).apply(t, u, v), false);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  default boolean test(T t, U u, V v) {/* @formatter:off */return apply(t, u, v);/* @formatter:on */}
}
