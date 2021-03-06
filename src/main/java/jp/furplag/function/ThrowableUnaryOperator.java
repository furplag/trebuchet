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

import java.util.function.UnaryOperator;

/**
 * {@link UnaryOperator} now get enable to throw {@link Throwable} .
 *
 * @author furplag
 *
 * @param <T> the type of the operand and result of the operator
 * @see ThrowableFunction
 * @see UnaryOperator
 */
@FunctionalInterface
public interface ThrowableUnaryOperator<T> extends ThrowableFunction<T, T>, UnaryOperator<T> {

  /**
   * returns a unary operator that always returns its input argument .
   *
   * @param <T> the type of the input and output of the operator
   * @return a unary operator that always returns its input argument
   */
  static <T> ThrowableUnaryOperator<T> identity() {
    return (t) -> t;
  }
}
