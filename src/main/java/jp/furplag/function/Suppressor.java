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

import java.util.Optional;

import jp.furplag.function.Trebuchet.ThrowableBiConsumer;
import jp.furplag.function.Trebuchet.ThrowableBiFunction;
import jp.furplag.function.Trebuchet.ThrowableConsumer;
import jp.furplag.function.Trebuchet.ThrowableFunction;

/**
 * suppressing to raise any exceptions in lambda expression .
 *
 * @author furplag
 *
 */
public interface Suppressor {

  /**
   * returns false when the test throws exception .
   *
   * @param <T> the type of the input to the function
   * @param t the value of the input to the function
   * @param functional {@link ThrowableFunction}
   * @return the result of {@link ThrowableFunction#apply(Object) functional.apply(t)} if done it normally, or false if error occured
   */
  static <T> boolean isCorrect(final T t, final ThrowableFunction<T, Boolean> functional) {
    return Optional.ofNullable(orElse(t, functional, false)).orElse(false);
  }

  /**
   * returns false when the test throws exception .
   *
   * @param <T> the type of the first argument to the function
   * @param <U> the type of the second argument to the function
   * @param t the first function argument
   * @param u the second function argument
   * @param functional {@link ThrowableBiFunction}
   * @return the result of {@link ThrowableBiFunction#apply(Object, Object) functional.apply(t, u)} if done it normally, or false if error occured
   */
  static <T, U> boolean isCorrect(final T t, final U u, final ThrowableBiFunction<T, U, Boolean> functional) {
    return Optional.ofNullable(orElse(t, u, functional, false)).orElse(false);
  }

  /**
   * rollback to original value when the function throws exception .
   *
   * @param <T> the type of the input to the function
   * @param t the value of the input to the function
   * @param functional {@link ThrowableFunction}
   * @return the result of {@link ThrowableFunction#apply(Object) functional.apply(t)} if done it normally, or t if error occured
   * @see jp.furplag.function.Trebuchet.ThrowableFunction
   */
  static <T> T orDefault(final T t, final ThrowableFunction<T, T> functional) {
    return orElse(t, functional, t);
  }

  /**
   * rollback to original value when the function throws exception .
   *
   * @param <T> the type of the first argument to the function
   * @param <U> the type of the second argument to the function
   * @param t the first function argument
   * @param u the second function argument
   * @param functional {@link ThrowableBiFunction}
   * @return the result of {@link ThrowableBiFunction#apply(Object, Object) functional.apply(t, u)} if done it normally, or t if error occured
   * @see jp.furplag.function.Trebuchet.ThrowableBiFunction
   */
  static <T, U> T orDefault(final T t, final U u, final ThrowableBiFunction<T, U, T> functional) {
    return orElse(t, u, functional, t);
  }

  /**
   * fallback to default value when the function throws exception .
   *
   * @param <T> the type of the first argument to the function
   * @param <R> the type of the result of the function
   * @param t the first function argument
   * @param functional {@link ThrowableFunction}
   * @param fallbackDefault default value for fallback
   * @return the result of {@link ThrowableFunction#apply(Object) functional.apply(t)} if done it normally, or fallbackDefault if error occured
   */
  static <T, R> R orElse(final T t, final ThrowableFunction<T, R> functional, final R fallbackDefault) {
    return Trebuchet.orElse(functional, (ex, e) -> fallbackDefault).apply(t);
  }

  /**
   * fallback to default value when the function throws exception .
   *
   * @param <T> the type of the first argument to the function
   * @param <U> the type of the second argument to the function
   * @param <R> the type of the result of the function
   * @param t the first function argument
   * @param u the second function argument
   * @param functional {@link ThrowableBiFunction}
   * @param fallbackDefault default value for fallback
   * @return the result of {@link ThrowableBiFunction#apply(Object, Object) functional.apply(t, u)} if done it normally, or fallbackDefault if error occured
   */
  static <T, U, R> R orElse(final T t, final U u, final ThrowableBiFunction<T, U, R> functional, final R fallbackDefault) {
    return Trebuchet.orElse(functional, (ex, e) -> fallbackDefault).apply(t, u);
  }

  /**
   * mute out any exceptions whether the function throws it .
   *
   * @param <T> the type of the input to the function
   * @param t the value of the input to the function
   * @param functional {@link ThrowableConsumer ThrowableConsumer}
   */
  static <T> void orNot(final T t, final ThrowableConsumer<T> functional) {
    Trebuchet.orElse(functional, (ex, x) -> /* @formatter:off */{/* do nothing . */}/* @formatter:on */).accept(t);
  }

  /**
   * mute out any exceptions whether the function throws it .
   *
   * @param <T> the type of the first argument to the function
   * @param <U> the type of the second argument to the function
   * @param t the first function argument
   * @param u the second function argument
   * @param functional {@link ThrowableBiConsumer}
   */
  static <T, U> void orNot(final T t, final U u, final ThrowableBiConsumer<T, U> functional) {
    Trebuchet.orElse(functional, (ex, x) -> /* @formatter:off */{/* do nothing . */}/* @formatter:on */).accept(t, u);
  }

  /**
   * fallback to null when the function throws exception .
   *
   * @param <T> the type of the input to the function
   * @param <R> the type of the result of the function
   * @param t the value of the input to the function
   * @param functional {@link ThrowableFunction}
   * @return the result of {@link ThrowableFunction#apply(Object) functional.apply(t)} if done it normally, or null if error occured
   * @see jp.furplag.function.Trebuchet.ThrowableFunction
   */
  static <T, R> R orNull(final T t, final ThrowableFunction<T, R> functional) {
    return orElse(t, functional, null);
  }

  /**
   * fallback to null when the function throws exception .
   *
   * @param <T> the type of the first argument to the function
   * @param <U> the type of the second argument to the function
   * @param <R> the type of the result of the function
   * @param t the first function argument
   * @param u the second function argument
   * @param functional {@link ThrowableBiFunction}
   * @return the result of {@link ThrowableBiFunction#apply(Object, Object) functional.apply(t, u)} if done it normally, or null if error occured
   * @see jp.furplag.function.Trebuchet.ThrowableBiFunction
   */
  static <T, U, R> R orNull(final T t, final U u, final ThrowableBiFunction<T, U, R> functional) {
    return orElse(t, u, functional, null);
  }

}
