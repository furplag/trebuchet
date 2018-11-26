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

/**
 * {@link Predicate} now get enable to throw {@link Throwable} .
 *
 * @author furplag
 *
 * @param <T> the type of the input to the predicate
 * @see ThrowablePredicate
 * @see Predicate
 */
@FunctionalInterface
public interface ThrowablePredicate<T> extends ThrowableFunction<T, Boolean>, Predicate<T> {

  /**
   * should never write &quot;ugly&quot; try-catch block to handle {@link Throwable exceptions} in lambda expression .
   *
   * @param <T> the type of the input to the predicate
   * @param <E> anything thrown
   * @param predicate {@link Predicate}, may not be null
   * @param fallen {@link BiPredicate}, or the function that always return {@code false} if this is null
   * @return {@link ThrowablePredicate}
   * @throws NullPointerException if arguments contains null
   */
  static <T, E extends Throwable> ThrowablePredicate<T> of(final Predicate<? super T> predicate, final BiPredicate<? super T, ? super E> fallen) {
    return ThrowableFunction.of(predicate::test, Trebuchet.defaults(fallen)::test)::apply;
  }

  /**
   * should never write &quot;ugly&quot; try-catch block to handle {@link Throwable exceptions} in lambda expression .
   *
   * @param <T> the type of the input to the predicate
   * @param <E> anything thrown
   * @param predicate {@link Predicate}, may not be null
   * @param fallen {@link BiPredicate}, or the function that always return {@code false} if this is null
   * @return {@link ThrowablePredicate}
   * @throws NullPointerException if arguments contains null
   */
  @SuppressWarnings({ "unchecked" })
  static <T, E extends Throwable> ThrowablePredicate<T> of(final Predicate<? super T> predicate, final Predicate<? super T> fallen) {
    return of(predicate, (t, e) -> Trebuchet.defaults(fallen).test((T) t))::apply;
  }

  /**
   * returns the result of {@link #test(Object) predicate.test(T)} if done it normally, or fallen if error occurred .
   *
   * @param <T> the type of the input to the predicate
   * @param predicate {@link Function}, may not be null
   * @param fallen the return value when error has occurred
   * @return the result of {@link #test(Object) predicate.test(T)} if done it normally, or fallen if error occurred
   */
  static <T> boolean orDefault(final T t, final Predicate<? super T> predicate, final boolean fallen) {
    return orElseGet(t, predicate, () -> fallen);
  }

  /**
   * returns the result of {@link #test(Object) predicate.test(T)} if done it normally, or {@link Predicate#test(Object) fallen.test(T)} if error occurred .
   *
   * @param <T> the type of the input to the predicate
   * @param predicate {@link Predicate}, may not be null
   * @param fallen {@link Predicate}, or the function that always return false if this is null
   * @return the result of {@link #test(Object) predicate.test(T)} if done it normally, or {@link Predicate#test(Object) fallen.test(T)} if error occurred
   */
  static <T, E extends Throwable> boolean orElse(final T t, final Predicate<? super T> predicate, final BiPredicate<? super T, E> fallen) {
    return of(predicate, fallen).test(t);
  }

  /**
   * returns the result of {@link #test(Object) predicate.test(T)} if done it normally, or {@link Predicate#test(Object) fallen.test(T)} if error occurred .
   *
   * @param <T> the type of the input to the predicate
   * @param predicate {@link Predicate}, may not be null
   * @param fallen {@link Predicate}, or the predicate that always return false if this is null
   * @return the result of {@link #test(Object) predicate.test(T)} if done it normally, or {@link Predicate#test(Object) fallen.test(T)} if error occurred
   */
  static <T> boolean orElse(final T t, final Predicate<? super T> predicate, final Predicate<? super T> fallen) {
    return orElse(t, predicate, (x, ex) -> Trebuchet.defaults(fallen).test(x));
  }

  /**
   * returns the result of {@link #test(Object) predicate.test(T)} if done it normally, or {@code false} if error occurred .
   *
   * @param <T> the type of the input to the predicate
   * @param predicate {@link Predicate}, may not be null
   * @return the result of {@link #test(Object) predicate.test(T)} if done it normally, or {@code false} if error occurred
   */
  static <T> boolean orNot(final T t, final Predicate<? super T> predicate) {
    return orElseGet(t, predicate, () -> false);
  }

  /**
   * returns the result of {@link #test(Object) predicate.test(T)} if done it normally, or fallen if error occurred .
   *
   * @param <T> the type of the input to the predicate
   * @param predicate {@link Function}, may not be null
   * @param fallen {@link BooleanSupplier}, or the function that always return {@code false} if this is null
   * @return the result of {@link #test(Object) predicate.test(T)} if done it normally, or fallen if error occurred
   */
  static <T> boolean orElseGet(final T t, final Predicate<? super T> predicate, final BooleanSupplier fallen) {
    return ThrowableFunction.orElseGet(t, predicate::test, Objects.requireNonNullElse(fallen, () -> false)::getAsBoolean);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  default boolean test(T t)  {/* @formatter:off */try {return applyOrThrow(t);} catch (Throwable e) {Trebuchet.sneakyThrow(e);} return false;/* @formatter:on */};

}
