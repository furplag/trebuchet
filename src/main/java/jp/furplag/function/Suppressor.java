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

import jp.furplag.function.Trebuchet.ThrowableBiConsumer;
import jp.furplag.function.Trebuchet.ThrowableBiFunction;
import jp.furplag.function.Trebuchet.ThrowableConsumer;
import jp.furplag.function.Trebuchet.ThrowableFunction;
import jp.furplag.function.Trebuchet.ThrowableOperator;


/**
 * code snippets for some problems when handling java.lang.Throwables in using Stream API .
 *
 * @author furplag
 *
 */
public interface Suppressor {

  /**
   * rollback to original value when the function throws exception .
   *
   * @param <T> the type of the input to the function
   * @param t the value of the input to the function
   * @param functional {@link ThrowableOperator}
   * @return the result of {@link ThrowableOperator#apply(Object) functional.apply(t)} if done it normally, or t if error occured
   */
  static <T> T nope(final T t, final ThrowableOperator<T> functional) {
    return Trebuchet.orElse(functional, (ex, x) -> x).apply(t);
  }

  /**
   * fallback to null when the function throws exception .
   *
   * @param <T> the type of the input to the function
   * @param <R> the type of the result of the function
   * @param t the value of the input to the function
   * @param functional {@link ThrowableFunction}
   * @return the result of {@link ThrowableFunction#apply(Object) functional.apply(t)} if done it normally, or null if error occured
   */
  static <T, R> R orNull(final T t, final ThrowableFunction<T, R> functional) {
    return Trebuchet.orElse(functional, (ex, x) -> null).apply(t);
  }

  /**
   * fallback to null when the function throws exception .
   *
   * @param <T> the type of the first argument to the function
   * @param <U> the type of the second argument to the function
   * @param <R> the type of the result of the function
   * @param t the first function argument
   * @param u the second function argument
   * @param functional {@link ThrowableFunction}
   * @return the result of {@link ThrowableBiFunction#apply(Object, Object) functional.apply(t, u)} if done it normally, or null if error occured
   */
  static <T, U, R> R orNull(final T t, final U u, final ThrowableBiFunction<T, U, R> functional) {
    return Trebuchet.orElse(functional, (ex, x) -> null).apply(t, u);
  }

  /**
   * mute out any exceptions whether the function throws it .
   *
   * @param <T> the type of the input to the function
   * @param t the value of the input to the function
   * @param functional {@link ThrowableConsumer}
   */
  static <T> void suppress(final T t, final ThrowableConsumer<T> functional) {
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
  static <T, U> void suppress(final T t, final U u, final ThrowableBiConsumer<T, U> functional) {
    Trebuchet.orElse(functional, (ex, x) -> /* @formatter:off */{/* do nothing . */}/* @formatter:on */).accept(t, u);
  }
}
