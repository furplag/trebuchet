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

import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Stream;

import jp.furplag.function.Trebuchet.TrinaryOperator;

/**
 * {@link TrinaryOperator} now get enable to throw {@link Throwable} .
 *
 * @author furplag
 *
 * @param <T> the type of the operand and result of the operator
 * @see TrinaryOperator
 * @see ThrowableTriFunction
 */
@FunctionalInterface
public interface ThrowableTrinaryOperator<T> extends TrinaryOperator<T>, ThrowableTriFunction<T, T, T, T> {

  /**
   * returns a {@link TrinaryOperator} which returns the greater of three elements according to the specified {@code Comparator} .
   *
   * @param <T> the type of the input arguments of the comparator
   * @param comparator a {@code Comparator} for comparing the three values
   * @return a {@code TrinaryOperator} which returns the greater of its operands, according to the supplied {@code Comparator}
   * @throws NullPointerException if the argument is null
   */
  static <T> ThrowableTrinaryOperator<T> maxBy(Comparator<? super T> comparator) {
    Objects.requireNonNull(comparator);

    return (a, b, c) -> stream(a, b, c).max(comparator).orElse(null);
  }

  /**
   * returns a {@link TrinaryOperator} which returns the lesser of three elements according to the specified {@code Comparator} .
   *
   * @param <T> the type of the input arguments of the comparator
   * @param comparator a {@code Comparator} for comparing the three values
   * @return a {@code TrinaryOperator} which returns the lesser of its operands, according to the supplied {@code Comparator}
   * @throws NullPointerException if the argument is null
   */
  static <T> ThrowableTrinaryOperator<T> minBy(Comparator<? super T> comparator) {
    Objects.requireNonNull(comparator);

    return (a, b, c) -> stream(a, b, c).min(comparator).orElse(null);
  }

  /**
   * shorthand for {@code Stream.of(x0, x1, x2).filter((t) -> Objects.nonNull(t))} .
   *
   * @param <T> the type of the operand and result of the operator
   * @param ts the value of the operand of the operator
   * @return {@link Stream}
   */
  @SafeVarargs
  private static <T> Stream<T> stream(T... ts) {
    return Arrays.stream(ts).filter(Objects::nonNull);
  }
}
