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

package jp.furplag.function.suppress;

import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import jp.furplag.function.Suppressor;

/**
 * suppressing to raise any exceptions in lambda expression .
 *
 * @author furplag
 *
 */
public interface SuppressFunction {


  /**
   * fallback to null when the function throws exception .
   *
   * @param <T> the type of the input to the function
   * @param <R> the type of the result of the function
   * @param t the value of the input to the function
   * @param functional {@link Function}
   * @return the result of {@link Function#apply(Object) functional.apply(t)} if done it normally, or null if error occured
   * @see jp.furplag.function.Trebuchet.ThrowableFunction
   */
  static <T, R> R orNull(final T t, final Function<T, R> functional) {
    return Suppressor.orElse(t, functional, null);
  }

  /**
   * fallback to null when the function throws exception .
   *
   * @param <T> the type of the first argument to the function
   * @param t1 the first function argument
   * @param t2 the second function argument
   * @param functional {@link BinaryOperator}
   * @return the result of {@link BinaryOperator#apply(Object, Object) functional.apply(t, u)} if done it normally, or null if error occured
   * @see jp.furplag.function.Trebuchet.ThrowableBinaryOperator
   */
  static <T> T orNull(final T t1, final T t2, final BinaryOperator<T> functional) {
    return Suppressor.orElse(t1, t2, functional, null);
  }

  /**
   * fallback to null when the function throws exception .
   *
   * @param <T> the type of the first argument to the function
   * @param <U> the type of the second argument to the function
   * @param <R> the type of the result of the function
   * @param t the first function argument
   * @param u the second function argument
   * @param functional {@link BiFunction}
   * @return the result of {@link BiFunction#apply(Object, Object) functional.apply(t, u)} if done it normally, or null if error occured
   * @see jp.furplag.function.Trebuchet.ThrowableBiFunction
   */
  static <T, U, R> R orNull(final T t, final U u, final BiFunction<T, U, R> functional) {
    return Suppressor.orElse(t, u, functional, null);
  }

  /**
   * fallback to null when the function throws exception .
   *
   * @param <T> the type of the input to the function
   * @param t the value of the input to the function
   * @param functional {@link UnaryOperator}
   * @return the result of {@link UnaryOperator#apply(Object) functional.apply(t)} if done it normally, or null if error occured
   * @see jp.furplag.function.Trebuchet.ThrowableUnaryOperator
   */
  static <T> T orNull(final T t, final UnaryOperator<T> functional) {
    return Suppressor.orElse(t, functional, null);
  }

}
