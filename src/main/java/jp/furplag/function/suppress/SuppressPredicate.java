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

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

import jp.furplag.function.Suppressor;

/**
 * suppressing to raise any exceptions in lambda expression .
 *
 * @author furplag
 *
 */
public interface SuppressPredicate {

  /**
   * returns false when the test throws exception .
   *
   * @param <T> the type of the input to the function
   * @param t the value of the input to the function
   * @param functional {@link Function}
   * @return the result of {@link Function#apply(Object) functional.apply(t)} if done it normally, or false if error occured
   * @see jp.furplag.function.Trebuchet.ThrowableFunction
   */
  static <T> boolean isCorrect(final T t, final Function<T, Boolean> functional) {
    return Optional.ofNullable(Suppressor.orElse(t, functional, false)).orElse(false);
  }

  /**
   * returns false when the test throws exception .
   *
   * @param <T> the type of the input to the function
   * @param t the value of the input to the function
   * @param functional {@link Predicate}
   * @return the result of {@link Predicate#test(Object) functional.test(t)} if done it normally, or false if error occured
   * @see jp.furplag.function.Trebuchet.ThrowablePredicate
   */
  static <T> boolean isCorrect(final T t, final Predicate<T> functional) {
    return Optional.ofNullable(Suppressor.orElse(t, functional::test, false)).orElse(false);
  }

  /**
   * returns false when the test throws exception .
   *
   * @param <T> the type of the first argument to the function
   * @param <U> the type of the second argument to the function
   * @param t the first function argument
   * @param u the second function argument
   * @param functional {@link BiFunction}
   * @return the result of {@link BiFunction#apply(Object, Object) functional.apply(t, u)} if done it normally, or false if error occured
   * @see jp.furplag.function.Trebuchet.ThrowableBiFunction
   */
  static <T, U> boolean isCorrect(final T t, final U u, final BiFunction<T, U, Boolean> functional) {
    return Optional.ofNullable(Suppressor.orElse(t, u, functional, false)).orElse(false);
  }

  /**
   * returns false when the test throws exception .
   *
   * @param <T> the type of the first argument to the function
   * @param <U> the type of the second argument to the function
   * @param t the first function argument
   * @param u the second function argument
   * @param functional {@link BiPredicate}
   * @return the result of {@link BiPredicate#test(Object, Object) functional.test(t, u)} if done it normally, or false if error occured
   * @see jp.furplag.function.Trebuchet.ThrowableFunction
   */
  static <T, U> boolean isCorrect(final T t, final U u, final BiPredicate<T, U> functional) {
    return Optional.ofNullable(Suppressor.orElse(t, u, functional::test, false)).orElse(false);
  }

}
