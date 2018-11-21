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
import java.util.Objects;
import java.util.stream.Stream;

/**
 * code snippets for some problems when handling java.lang.Throwables in using Stream API .
 *
 * @author furplag
 *
 */
public interface Trebuchet {

  /**
   * represents an operation that accepts two input arguments and returns no result .
   * this is the three-arity specialization of {@link java.util.function.Consumer Consumer} .
   * unlike most other functional interfaces, {@code TriConsumer} is expected to operate via side-effects .
   *
   * @author furplag
   *
   * @param <T> the type of the first argument to the operation
   * @param <U> the type of the second argument to the operation
   * @param <V> the type of the third argument to the operation
   * @see {@link java.util.function.Consumer Consumer}
   */
  @FunctionalInterface
  static interface TriConsumer<T, U, V> {

    /**
     * performs this operation on the given arguments .
     *
     * @param t the first input argument
     * @param u the second input argument
     * @param v the third input argument
     */
    void accept(T t, U u, V v);

    /**
     * returns a composed {@code TriConsumer} that performs, in sequence, this operation followed by the {@code after} operation .
     * if performing either operation throws an exception, it is relayed to the caller of the composed operation .
     * if performing this operation throws an exception, the {@code after} operation will not be performed .
     *
     * @param after the operation to perform after this operation
     * @return a composed {@code TriConsumer} that performs in sequence this operation followed by the {@code after} operation
     */
    default TriConsumer<T, U, V> andThen(TriConsumer<? super T, ? super U, ? super V> after) {
      Objects.requireNonNull(after);

      return (t, u, v) -> {/* @formatter:off */accept(t, u, v); after.accept(t, u, v);/* @formatter:on */};
    }
  }

  /**
   * represents a function that accepts three arguments and produces a result .
   * this is the three-arity specialization of {@link java.util.function.Function Function} .
   *
   * @author furplag
   *
   * @param <T> the type of the first argument to the function
   * @param <U> the type of the second argument to the function
   * @param <V> the type of the third argument to the function
   * @param <R> the type of the result of the function
   * @see {@link java.util.function.Function Function}
   */
  @FunctionalInterface
  static interface TriFunction<T, U, V, R> {

    /**
     * returns a composed function that first applies this function to its input, and then applies the {@code after} function to the result .
     * if evaluation of either function throws an exception, it is relayed to the caller of the composed function .
     *
     * @param <W> the type of output of the {@code after} function, and of the composed function
     * @param after the function to apply after this function is applied
     * @return a composed function that first applies this function and then applies the {@code after} function
     * @throws NullPointerException if after is null
     */
    default <W> TriFunction<T, U, V, W> andThen(java.util.function.Function<? super R, ? extends W> after) {
      Objects.requireNonNull(after);

      return (t, u, v) -> after.apply(apply(t, u, v));
    }

    /**
     * applies this function to the given arguments .
     *
     * @param t the first function argument
     * @param u the second function argument
     * @param v the third function argument
     * @return the function result
     */
    R apply(T t, U u, V v);
  }

  /**
   * represents an operation upon two operands of the same type, producing a result of the same type as the operands .
   * this is a specialization of {@link TriFunction} for the case where the operands and the result are all of the same type .
   *
   * @author furplag
   *
   * @param <T> the type of the operand and result of the operator
   * @see TriFunction
   * @see {@link java.util.function.UnaryOperator UnaryOperator}
   */
  @FunctionalInterface
  public interface TrinaryOperator<T> extends TriFunction<T, T, T, T> {

    /**
     * returns a {@link TrinaryOperator} which returns the greater of three elements according to the specified {@code Comparator} .
     *
     * @param <T> the type of the input arguments of the comparator
     * @param comparator a {@code Comparator} for comparing the three values
     * @return a {@code TrinaryOperator} which returns the greater of its operands, according to the supplied {@code Comparator}
     * @throws NullPointerException if the argument is null
     */
    static <T> TrinaryOperator<T> maxBy(Comparator<? super T> comparator) {
      Objects.requireNonNull(comparator);

      return (a, b, c) -> Stream.of(a, b, c).filter(Objects::nonNull).max(comparator).orElse(null);
    }

    /**
     * returns a {@link TrinaryOperator} which returns the lesser of three elements according to the specified {@code Comparator} .
     *
     * @param <T> the type of the input arguments of the comparator
     * @param comparator a {@code Comparator} for comparing the three values
     * @return a {@code TrinaryOperator} which returns the lesser of its operands, according to the supplied {@code Comparator}
     * @throws NullPointerException if the argument is null
     */
    static <T> TrinaryOperator<T> minBy(Comparator<? super T> comparator) {
      Objects.requireNonNull(comparator);

      return (a, b, c) -> Stream.of(a, b, c).filter(Objects::nonNull).min(comparator).orElse(null);
    }
  }

  /**
   * represents a predicate (boolean-valued function) of three arguments .
   * this is the three-arity specialization of {@link java.util.function.Predicate Predicate} .
   *
   * @author furplag
   *
   * @param <T> the type of the first argument to the predicate
   * @param <U> the type of the second argument to the predicate
   * @param <V> the type of the third argument to the predicate
   * @see TriFunction
   * @see {@link java.util.function.Predicate Predicate}
   */
  @FunctionalInterface
  public interface TriPredicate<T, U, V> extends TriFunction<T, U, V, Boolean> {

    /**
     * returns a composed predicate that represents a short-circuiting logical AND of this predicate and another .
     * when evaluating the composed predicate, if this predicate is {@code false}, then the {@code other} predicate is not evaluated .
     *
     * <p>any exceptions thrown during evaluation of either predicate are relayed to the caller;
     * if evaluation of this predicate throws an exception, the {@code other} predicate will not be evaluated .</p>
     *
     * @param other a predicate that will be logically-ANDed with this predicate
     * @return a composed predicate that represents the short-circuiting logical AND of this predicate and the {@code other} predicate
     * @throws NullPointerException if other is null
     */
    default TriPredicate<T, U, V> and(TriPredicate<? super T, ? super U, ? super V> other) {
      Objects.requireNonNull(other);

      return (t, u, v) -> test(t, u, v) && other.test(t, u, v);
    }

    /**
     * returns a predicate that represents the logical negation of this predicate .
     *
     * @return a predicate that represents the logical negation of this predicate
     */
    default TriPredicate<T, U, V> negate() {
      return (t, u, v) -> !test(t, u, v);
    }

    /**
     * returns a composed predicate that represents a short-circuiting logical OR of this predicate and another .
     * when evaluating the composed predicate, if this predicate is {@code true}, then the {@code other} predicate is not evaluated .
     *
     * <p>any exceptions thrown during evaluation of either predicate are relayed to the caller;
     * if evaluation of this predicate throws an exception, the {@code other} predicate will not be evaluated .</p>
     *
     * @param other a predicate that will be logically-ORed with this predicate
     * @return a composed predicate that represents the short-circuiting logical OR of this predicate and the {@code other} predicate
     * @throws NullPointerException if other is null
     */
    default TriPredicate<T, U, V> or(TriPredicate<? super T, ? super U, ? super V> other) {
      Objects.requireNonNull(other);

      return (t, u, v) -> test(t, u, v) || other.test(t, u, v);
    }

    /**
     * evaluates this predicate on the given argument .
     *
     * @param t the first input argument
     * @param u the second input argument
     * @param v the third input argument
     * @return {@code true} if the input argument matches the predicate, otherwise {@code false}
     */
    default boolean test(T t, U u, V v) {/* @formatter:off */return apply(t, u, v);/* @formatter:on */}
  }

  /**
   * the fork of {@code lombok.Lombok.sneakyThrow(Throwable)} .
   *
   * @param <E> anything thrown
   * @param ex anything thrown
   * @throws E anything thrown
   */
  @SuppressWarnings({ "unchecked" })
  static <E extends Throwable> void sneakyThrow(final Throwable ex) throws E {
    throw (E) (ex == null ? new IllegalArgumentException("hmm, no way call me with null .") : ex);
  }
}
