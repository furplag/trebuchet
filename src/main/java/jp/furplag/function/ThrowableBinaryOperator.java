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

import java.util.Comparator;
import java.util.function.BinaryOperator;

/**
 * {@link BinaryOperator} now get enable to throw {@link Throwable} .
 *
 * @author furplag
 *
 * @param <T> the type of the operand and result of the operator
 * @see ThrowableBiFunction
 * @see BinaryOperator
 */
@FunctionalInterface
public interface ThrowableBinaryOperator<T> extends ThrowableBiFunction<T, T, T>, BinaryOperator<T> {

  /**
   * returns a {@link BinaryOperator} which returns the greater of two elements according to the specified {@code Comparator} .
   *
   * @param <T> the type of the input arguments of the comparator
   * @param comparator a {@code Comparator} for comparing the two values
   * @return a {@code BinaryOperator} which returns the greater of its operands, according to the supplied {@code Comparator}
   * @throws NullPointerException if the argument is null
   */
  static <T> ThrowableBinaryOperator<T> maxBy(Comparator<? super T> comparator) {
    return (a, b) -> comparator.compare(a, b) >= 0 ? a : b;
  }

  /**
   * returns a {@link BinaryOperator} which returns the lesser of two elements according to the specified {@code Comparator} .
   *
   * @param <T> the type of the input arguments of the comparator
   * @param comparator a {@code Comparator} for comparing the two values
   * @return a {@code BinaryOperator} which returns the lesser of its operands, according to the supplied {@code Comparator}
   * @throws NullPointerException if the argument is null
   */
  static <T> ThrowableBinaryOperator<T> minBy(Comparator<? super T> comparator) {
    return (a, b) -> comparator.compare(a, b) <= 0 ? a : b;
  }
}
