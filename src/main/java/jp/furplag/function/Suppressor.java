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

import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * suppressing to raise any exceptions in lambda expression .
 *
 * @author furplag
 *
 */
public interface Suppressor {

  /**
   * fallback to default value when the function throws exception .
   *
   * @param <T> the type of the first argument to the function
   * @param <R> the type of the result of the function
   * @param t the first function argument
   * @param functional {@link Function}
   * @param fallbackDefault default value for fallback
   * @return the result of {@link Function#apply(Object) functional.apply(t)} if done it normally, or fallbackDefault if error occured
   */
  static <T, R> R orElse(final T t, final Function<T, R> functional, final R fallbackDefault) {
    return Trebuchet.orElse(functional, (ex, e) -> fallbackDefault).apply(t);
  }

  /**
   * fallback to default value when the function throws exception .
   *
   * @param <T> the type of the first argument to the function
   * @param t1 the first function argument
   * @param t2 the second function argument
   * @param functional {@link BinaryOperator}
   * @param fallbackDefault default value for fallback
   * @return the result of {@link BinaryOperator#apply(Object, Object) functional.apply(t1, t2)} if done it normally, or fallbackDefault if error occured
   */
  static <T> T orElse(final T t1, final T t2, final BinaryOperator<T> functional, final T fallbackDefault) {
    return Trebuchet.orElse(functional, (ex, e) -> fallbackDefault).apply(t1, t2);
  }

  /**
   * fallback to default value when the function throws exception .
   *
   * @param <T> the type of the first argument to the function
   * @param <U> the type of the second argument to the function
   * @param <R> the type of the result of the function
   * @param t the first function argument
   * @param u the second function argument
   * @param functional {@link BiFunction}
   * @param fallbackDefault default value for fallback
   * @return the result of {@link BiFunction#apply(Object, Object) functional.apply(t, u)} if done it normally, or fallbackDefault if error occured
   */
  static <T, U, R> R orElse(final T t, final U u, final BiFunction<T, U, R> functional, final R fallbackDefault) {
    return Trebuchet.orElse(functional, (ex, e) -> fallbackDefault).apply(t, u);
  }

  /**
   * fallback to default value when the function throws exception .
   *
   * @param <T> the type of the first argument to the function
   * @param t the first function argument
   * @param functional {@link UnaryOperator}
   * @param fallbackDefault default value for fallback
   * @return the result of {@link UnaryOperator#apply(Object) functional.apply(t)} if done it normally, or fallbackDefault if error occured
   */
  static <T> T orElse(final T t, final UnaryOperator<T> functional, final T fallbackDefault) {
    return Trebuchet.orElse(functional, (ex, e) -> fallbackDefault).apply(t);
  }
}
