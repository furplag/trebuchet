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
import java.util.function.Predicate;

import jp.furplag.function.Trebuchet.TriPredicate;

/**
 * {@link BiPredicate} now get enable to throw {@link Throwable} .
 *
 * @param <T> the type of the first argument to the predicate
 * @param <U> the type of the second argument to the predicate
 * @see ThrowableBiFunction
 * @see BiPredicate
 */
@FunctionalInterface
public interface ThrowableBiPredicate<T, U> extends ThrowableBiFunction<T, U, Boolean>, BiPredicate<T, U> {

  /**
   * should never write &quot;ugly&quot; try-catch block to handle {@link Throwable exceptions} in lambda expression .
   *
   * @param <T> the type of the first argument to the function
   * @param <U> the type of the second argument to the function
   * @param predicate {@link BiPredicate}, may not be null
   * @param fallen {@link BiPredicate}, or the function that always return {@code false} if this is null
   * @return {@link ThrowableBiPredicate}
   */
  static <T, U> ThrowableBiPredicate<T, U> of(final ThrowableBiPredicate<? super T, ? super U> predicate, final BiPredicate<? super T, ? super U> fallen) {
    return of(predicate, (t, u, e) -> Trebuchet.defaults(fallen).test(t, u));
  }

  /**
   * should never write &quot;ugly&quot; try-catch block to handle {@link Throwable exceptions} in lambda expression .
   *
   * @param <T> the type of the first argument to the function
   * @param <U> the type of the second argument to the function
   * @param <E> anything thrown
   * @param predicate {@link BiPredicate}, may not be null
   * @param fallen {@link Predicate}, or the function that always return {@code false} if this is null
   * @return {@link ThrowableBiPredicate}
   */
  @SuppressWarnings({ "unchecked" })
  static <T, U, E extends Throwable> ThrowableBiPredicate<T, U> of(final ThrowableBiPredicate<? super T, ? super U> predicate, final Predicate<? super E> fallen) {
    return of(predicate, (t, u, e) -> Trebuchet.defaults(fallen).test((E) e));
  }

  /**
   * should never write &quot;ugly&quot; try-catch block to handle {@link Throwable exceptions} in lambda expression .
   *
   * @param <T> the type of the first argument to the function
   * @param <U> the type of the second argument to the function
   * @param <E> anything thrown
   * @param predicate {@link BiPredicate}, may not be null
   * @param fallen {@link TriPredicate}, or the function that always return {@code false} if this is null
   * @return {@link ThrowableBiPredicate}
   */
  static <T, U, E extends Throwable> ThrowableBiPredicate<T, U> of(final ThrowableBiPredicate<? super T, ? super U> predicate, final TriPredicate<? super T, ? super U, ? super E> fallen) {
    return (t, u) -> ThrowableBiFunction.of(predicate::test, Objects.requireNonNullElse(fallen, (x, y, ex) -> false)).apply(t, u);
  }

  /**
   * returns the result of {@link #test(Object, Object) predicate.test(T, U)} if done it normally, or fallen if error occurred .
   *
   * @param <T> the type of the first argument to the predicate
   * @param <U> the type of the second argument to the predicate
   * @param t the value of the first argument to the predicate
   * @param u the value of the second argument to the predicate
   * @param predicate {@link BiPredicate}, may not be null
   * @param fallen the return value when error has occurred
   * @return the result of {@link #test(Object, Object) predicate.test(T, U)} if done it normally, or fallen if error occurred
   */
  static <T, U> boolean orDefault(final T t, final U u, final ThrowableBiPredicate<? super T, ? super U> predicate, final boolean fallen) {
    return orElseGet(t, u, predicate, () -> fallen);
  }

  /**
   * returns the result of {@link #test(Object, Object) predicate.test(T, U)} if done it normally, or fallen if error occurred .
   *
   * @param <T> the type of the first argument to the predicate
   * @param <U> the type of the second argument to the predicate
   * @param t the value of the first argument to the predicate
   * @param u the value of the second argument to the predicate
   * @param predicate {@link BiPredicate}, may not be null
   * @param fallen {@link BiPredicate}, or the function that always return {@code false} if this is null
   * @return the result of {@link #test(Object, Object) predicate.test(T, U)} if done it normally, or {@link BiPredicate#test(Object, Object) fallen.test(T, U)} if error occurred
   */
  static <T, U> boolean orElse(final T t, final U u, final ThrowableBiPredicate<? super T, ? super U> predicate, final BiPredicate<? super T, ? super U> fallen) {
    return of(predicate, fallen).test(t, u);
  }

  /**
   * returns the result of {@link #test(Object, Object) predicate.test(T, U)} if done it normally, or fallen if error occurred .
   *
   * @param <T> the type of the first argument to the predicate
   * @param <U> the type of the second argument to the predicate
   * @param t the value of the first argument to the predicate
   * @param u the value of the second argument to the predicate
   * @param predicate {@link BiPredicate}, may not be null
   * @param fallen {@link Predicate}, or the function that always return {@code false} if this is null
   * @return the result of {@link #test(Object, Object) predicate.test(T, U)} if done it normally, or {@link Predicate#test(Object) fallen.test(E)} if error occurred
   */
  static <T, U, E extends Throwable> boolean orElse(final T t, final U u, final ThrowableBiPredicate<? super T, ? super U> predicate, final Predicate<? super E> fallen) {
    return of(predicate, fallen).test(t, u);
  }

  /**
   * returns the result of {@link #test(Object, Object) predicate.test(T, U)} if done it normally, or {@link TriPredicate#test(Object, Object, Object) fallen.test(T, U, E)} if error occurred .
   *
   * @param <T> the type of the first argument to the predicate
   * @param <U> the type of the second argument to the predicate
   * @param t the value of the first argument to the predicate
   * @param u the value of the second argument to the predicate
   * @param predicate {@link BiPredicate}, may not be null
   * @param fallen {@link TriPredicate}, or the function that always return {@code false} if this is null
   * @return the result of {@link #test(Object, Object) predicate.test(T, U)} if done it normally, or {@link TriPredicate#test(Object, Object, Object) fallen.test(T, U, E)} if error occurred
   */
  static <T, U, E extends Throwable> boolean orElse(final T t, final U u, final ThrowableBiPredicate<? super T, ? super U> predicate, final TriPredicate<? super T, ? super U, ? super E> fallen) {
    return of(predicate, fallen).test(t, u);
  }

  /**
   * returns the result of {@link #test(Object, Object) predicate.test(T, U)} if done it normally, or {@link BooleanSupplier#getAsBoolean() fallen.getAsBoolean()} if error occurred .
   *
   * @param <T> the type of the first argument to the predicate
   * @param <U> the type of the second argument to the predicate
   * @param t the value of the first argument to the predicate
   * @param u the value of the second argument to the predicate
   * @param predicate {@link BiPredicate}, may not be null
   * @param fallen {@link BooleanSupplier}, or the function that always return {@code false} if this is null
   * @return the result of {@link #test(Object, Object) predicate.test(T, U)} if done it normally, or {@link BooleanSupplier#getAsBoolean() fallen.getAsBoolean()} if error occurred
   */
  static <T, U> boolean orElseGet(final T t, final U u, final ThrowableBiPredicate<? super T, ? super U> predicate, final BooleanSupplier fallen) {
    return ThrowableBiFunction.orElseGet(t, u, predicate::test, Objects.requireNonNullElse(fallen, () -> false)::getAsBoolean);
  }

  /**
   * returns the result of {@link #test(Object, Object) predicate.test(T, U)} if done it normally, or {@code false} if error occurred .
   *
   * @param <T> the type of the first argument to the predicate
   * @param <U> the type of the second argument to the predicate
   * @param t the value of the first argument to the predicate
   * @param u the value of the second argument to the predicate
   * @param predicate {@link BiPredicate}, may not be null
   * @return the result of {@link #test(Object, Object) predicate.test(T, U)} if done it normally, or {@code false} if error occurred
   */
  static <T, U> boolean orNot(final T t, final U u, final ThrowableBiPredicate<? super T, ? super U> predicate) {
    return orElseGet(t, u, predicate, () -> false);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  default boolean test(T t, U u) {/* @formatter:off */return apply(t, u);/* @formatter:on */}

  /**
   * {@inheritDoc}
   */
  @Override
  default ThrowableBiPredicate<T, U> negate() {
    return (t, u) -> !test(t, u);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  default ThrowableBiPredicate<T, U> and(BiPredicate<? super T, ? super U> other) {
    Objects.requireNonNull(other);

    return (t, u) -> test(t, u) && other.test(t, u);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  default ThrowableBiPredicate<T, U> or(BiPredicate<? super T, ? super U> other) {
    Objects.requireNonNull(other);

    return (t, u) -> test(t, u) || other.test(t, u);
  }
}
