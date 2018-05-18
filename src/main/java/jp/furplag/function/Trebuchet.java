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

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * code snippets for some problems when handling java.lang.Throwables in using Stream API .
 *
 * @author furplag
 *
 */
public interface Trebuchet {

  /**
   * {@link java.util.function.BiConsumer BiConsumer} now get enable to throw {@link Throwable} .
   *
   * @author furplag
   *
   * @param <T> the type of the first argument to the function
   * @param <U> the type of the second argument to the function
   * @see java.util.function.BiConsumer
   */
  @FunctionalInterface
  public interface ThrowableBiConsumer<T, U> extends BiConsumer<T, U> {

    /**
     * {@inheritDoc}
     */
    @Override
    default void accept(T t, U u) {/* @formatter:off */try {accept0(t, u);} catch (Throwable ex) {sneakyThrow(ex);}/* @formatter:on */}

    /**
     * Performs this operation on the given arguments.
     *
     * @param t the first input argument
     * @param u the second input argument
     * @throws Throwable anything thrown
     */
    void accept0(T t, U u) throws Throwable;

    /**
     * should never write "ugly" try-catch block for {@link Throwable} (s) in stream .
     *
     * @param <T> the type of the first argument to the function
     * @param <U> the type of the second argument to the function
     * @param <E> anything thrown
     * @param functional {@link ThrowableBiConsumer}
     * @param fallen {@link BiConsumer}
     * @return {@link ThrowableBiConsumer#accept(Object, Object) functional.accept(T, U)} if done it normally, or {@link BiConsumer#accept(Object, Object) fallen.accept(E, T)} if error occured
     */
    @SuppressWarnings({ "unchecked" })
    private static <T, U, E extends Throwable> BiConsumer<T, U> orElse(final ThrowableBiConsumer<T, U> functional, final BiConsumer<E, T> fallen) {
      return (t, u) -> {/* @formatter:off */try {functional.accept(t, u);} catch (Throwable ex) {fallen.accept((E) ex, t);}/* @formatter:off */};
    }
  }

  /**
   * {@link java.util.function.BiFunction BiFunction} now get enable to throw {@link Throwable} .
   *
   * @author furplag
   *
   * @param <T> the type of the first argument to the function
   * @param <U> the type of the second argument to the function
   * @param <R> the type of the result of the function
   * @see java.util.function.BiFunction
   */
  @FunctionalInterface
  public interface ThrowableBiFunction<T, U, R> extends BiFunction<T, U, R> {

    /**
     * {@inheritDoc}
     */
    @Override
    default R apply(T t, U u) {/* @formatter:off */try {return apply0(t, u);} catch (Throwable ex) {sneakyThrow(ex);} return null;/* @formatter:on */}

    /**
     * Applies this function to the given arguments.
     *
     * @param t the first function argument
     * @param u the second function argument
     * @return the function result
     * @throws Throwable anything thrown
     */
    R apply0(T t, U u) throws Throwable;

    /**
     * should never write "ugly" try-catch block for {@link Throwable} (s) in stream .
     *
     * @param <T> the type of the first argument to the function
     * @param <U> the type of the second argument to the function
     * @param <R> the type of the result of the function
     * @param <E> anything thrown
     * @param functional {@link ThrowableBiFunction}
     * @param fallen {@link BiFunction}
     * @return {@link ThrowableBiFunction#apply(Object, Object) functional.apply(T, U)} if done it normally, or {@link BiFunction#apply(Object, Object) fallen.apply(E, T)} if error occured
     */
    @SuppressWarnings({ "unchecked" })
    private static <T, U, R, E extends Throwable> BiFunction<T, U, R> orElse(final ThrowableBiFunction<T, U, R> functional, final BiFunction<E, T, R> fallen) {
      return (t, u) -> {/* @formatter:off */try {return functional.apply(t, u);} catch (Throwable ex) {return fallen.apply((E) ex, t);}/* @formatter:off */};
    }
  }

  /**
   * {@link java.util.function.BinaryOperator BinaryOperator} now get enable to throw {@link Throwable} .
   *
   * @author furplag
   *
   * @param <T> the type of the first argument to the function
   * @see java.util.function.BinaryOperator
   * @see ThrowableBiFunction
   */
  @FunctionalInterface
  public interface ThrowableBinaryOperator<T> extends BinaryOperator<T>, ThrowableBiFunction<T, T, T> {

    /**
     * should never write "ugly" try-catch block for {@link Throwable} (s) in stream .
     *
     * @param <T> the type of the first argument to the function
     * @param <E> anything thrown
     * @param functional {@link ThrowableBinaryOperator}
     * @param fallen {@link BiFunction}
     * @return {@link ThrowableBinaryOperator#apply(Object, Object) functional.apply(T, U)} if done it normally, or {@link BiFunction#apply(Object, Object) fallen.apply(E, T)} if error occured
     */
    @SuppressWarnings({ "unchecked" })
    private static <T, E extends Throwable> BinaryOperator<T> orElse(final ThrowableBinaryOperator<T> functional, final BiFunction<E, T, T> fallen) {
      return (t1, t2) -> {/* @formatter:off */try {return functional.apply(t1, t2);} catch (Throwable ex) {return fallen.apply((E) ex, t1);}/* @formatter:off */};
    }
  }

  /**
   * {@link java.util.function.Consumer Consumer} now get enable to throw {@link Throwable} .
   *
   * @author furplag
   *
   * @param <T> the type of the input to the function
   * @see java.util.function.Consumer
   */
  @FunctionalInterface
  public interface ThrowableConsumer<T> extends Consumer<T> {

    /**
     * {@inheritDoc}
     */
    @Override
    default void accept(T t) {/* @formatter:off */try {accept0(t);} catch (Throwable ex) {sneakyThrow(ex);}/* @formatter:on */}

    /**
     * Performs this operation on the given argument.
     *
     * @param t the input argument
     * @throws Throwable anything thrown
     */
    void accept0(T t) throws Throwable;

    /**
     * should never write "ugly" try-catch block for {@link Throwable} (s) in stream .
     *
     * @param <T> the type of the input to the function
     * @param <E> anything thrown
     * @param functional {@link ThrowableConsumer}
     * @param fallen {@link BiConsumer}
     * @return {@link ThrowableConsumer#accept(Object) functional.accept(T)} if done it normally, or {@link BiConsumer#accept(Object, Object) fallen.accept(E, T)} if error occured
     */
    @SuppressWarnings({ "unchecked" })
    private static <T, E extends Throwable> Consumer<T> orElse(final ThrowableConsumer<T> functional, final BiConsumer<E, T> fallen) {
      return (t) -> {/* @formatter:off */try {functional.accept(t);} catch (Throwable ex) {fallen.accept((E) ex, t);}/* @formatter:off */};
    }
  }

  /**
   * {@link java.util.function.Function Function} now get enable to throw {@link Throwable} .
   *
   * @author furplag
   *
   * @param <T> the type of the input to the function
   * @param <R> the type of the result of the function
   * @see java.util.function.Function
   */
  @FunctionalInterface
  public interface ThrowableFunction<T, R> extends Function<T, R> {

    /**
     * {@inheritDoc}
     */
    @Override
    default R apply(T t) {/* @formatter:off */try {return apply0(t);} catch (Throwable ex) {sneakyThrow(ex);} return null;/* @formatter:on */}

    /**
     * Applies this function to the given arguments.
     *
     * @param t the first function argument
     * @return the function result
     * @throws Throwable anything thrown
     */
    R apply0(T t) throws Throwable;

    /**
     * should never write "ugly" try-catch block for {@link Throwable} (s) in stream .
     *
     * @param <T> the type of the input to the function
     * @param <R> the type of the result of the function
     * @param <E> anything thrown
     * @param functional {@link ThrowableFunction}
     * @param fallen {@link BiFunction}
     * @return {@link ThrowableFunction#apply(Object) functional.apply(T)} if done it normally, or {@link BiFunction#apply(Object, Object) fallen.apply(E, T)} if error occured
     */
    @SuppressWarnings({ "unchecked" })
    private static <T, R, E extends Throwable> Function<T, R> orElse(final ThrowableFunction<T, R> functional, final BiFunction<E, T, R> fallen) {
      return (t) -> {/* @formatter:off */try {return functional.apply(t);} catch (Throwable ex) {return fallen.apply((E) ex, t);}/* @formatter:off */};
    }
  }

  /**
   * {@link java.util.function.Function Function} now get enable to throw {@link Throwable} .
   *
   * @author furplag
   *
   * @param <T> the type of the input to the function
   * @see java.util.function.Function
   */
  @FunctionalInterface
  public interface ThrowablePredicate<T> extends Predicate<T> {

    /**
     * {@inheritDoc}
     */
    @Override
    default boolean test(T t) {/* @formatter:off */try {return test0(t);} catch (Throwable ex) {sneakyThrow(ex);} return false;/* @formatter:on */}

    /**
     * Evaluates this predicate on the given argument.
     *
     * @param t the input argument
     * @return {@code true} if the input argument matches the predicate,
     * otherwise {@code false}
     */
    boolean test0(T t) throws Throwable;

    /**
     * should never write "ugly" try-catch block for {@link Throwable} (s) in stream .
     *
     * @param <T> the type of the input to the function
     * @param <E> anything thrown
     * @param functional {@link ThrowablePredicate}
     * @param fallen {@link BiPredicate}
     * @return {@link ThrowablePredicate#test(Object) functional.test(T)} if done it normally, or {@link BiPredicate#apply(Object, Object) fallen.test(E, T)} if error occured
     */
    @SuppressWarnings({ "unchecked" })
    private static <T, E extends Throwable> Predicate<T> orElse(final ThrowablePredicate<T> functional, final BiPredicate<E, T> fallen) {
      return (t) -> {/* @formatter:off */try {return functional.test(t);} catch (Throwable ex) {return fallen.test((E) ex, t);}/* @formatter:off */};
    }
  }

  /**
   * {@link java.util.function.Function Function} now get enable to throw {@link Throwable} .
   *
   * @author furplag
   *
   * @param <T> the type of the input to the function
   * @see java.util.function.Function
   */
  @FunctionalInterface
  public interface ThrowableBiPredicate<T, U> extends BiPredicate<T, U> {

    /**
     * {@inheritDoc}
     */
    @Override
    default boolean test(T t, U u) {/* @formatter:off */try {return test0(t, u);} catch (Throwable ex) {sneakyThrow(ex);} return false;/* @formatter:on */}

    /**
     * Evaluates this predicate on the given arguments.
     *
     * @param t the first input argument
     * @param u the second input argument
     * @return {@code true} if the input arguments match the predicate,
     * otherwise {@code false}
     */
    boolean test0(T t, U u) throws Throwable;

    /**
     * should never write "ugly" try-catch block for {@link Throwable} (s) in stream .
     *
     * @param <T> the type of the input to the function
     * @param <E> anything thrown
     * @param functional {@link ThrowableBiPredicate}
     * @param fallen {@link BiPredicate}
     * @return {@link ThrowableBiPredicate#test(Object, Object) functional.test(T, U)} if done it normally, or {@link BiPredicate#apply(Object, Object) fallen.test(E, T)} if error occured
     */
    @SuppressWarnings({ "unchecked" })
    private static <T, U, E extends Throwable> BiPredicate<T, U> orElse(final ThrowableBiPredicate<T, U> functional, final BiPredicate<E, T> fallen) {
      return (t, u) -> {/* @formatter:off */try {return functional.test(t, u);} catch (Throwable ex) {return fallen.test((E) ex, t);}/* @formatter:off */};
    }
  }

  /**
   * {@link java.util.function.UnaryOperator UnaryOperator} now get enable to throw {@link Throwable} .
   *
   * @author furplag
   *
   * @param <T> the type of the input and output of the operator
   * @see java.util.function.UnaryOperator
   * @see ThrowableFunction
   */
  @FunctionalInterface
  public interface ThrowableUnaryOperator<T> extends UnaryOperator<T>, ThrowableFunction<T, T> {

    /**
     * should never write "ugly" try-catch block for {@link Throwable} (s) in stream .
     *
     * @param <T> the type of the I/O to the function
     * @param <E> anything thrown
     * @param functional {@link ThrowableUnaryOperator}
     * @param fallen {@link BiFunction}
     * @return {@link ThrowableUnaryOperator#apply(Object) functional.apply(T)} if done it normally, or {@link BiFunction#apply(Object, Object) fallen.apply(E, T)} if error occured
     */
    @SuppressWarnings({ "unchecked" })
    private static <T, E extends Throwable> UnaryOperator<T> orElse(final ThrowableUnaryOperator<T> functional, final BiFunction<E, T, T> fallen) {
      return (t) -> {/* @formatter:off */try {return functional.apply(t);} catch (Throwable ex) {return fallen.apply((E) ex, t);}/* @formatter:off */};
    }
  }

  /**
   * the fork of {@code lombok.Lombok.sneakyThrow(Throwable)} .
   *
   * @param <E> anything thrown
   * @param ex anything thrown
   * @throws E anything thrown
   */
  @SuppressWarnings({ "unchecked" })
  private static <E extends Throwable> void sneakyThrow(final Throwable ex) throws E {
    throw (E) (ex == null ? new IllegalArgumentException("hmm, no way call me with null .") : ex);
  }

  /**
   * should never write "ugly" try-catch block for {@link Throwable} (s) in stream .
   *
   * @param <T> the type of the first argument to the function
   * @param <U> the type of the second argument to the function
   * @param <E> anything thrown
   * @param functional {@link BiConsumer}
   * @param fallen {@link BiConsumer}
   * @return {@link BiConsumer#accept(Object, Object) functional.accept(T, U)} if done it normally, or {@link BiConsumer#accept(Object, Object) fallen.accept(E, T)} if error occured
   */
  static <T, U, E extends Throwable> BiConsumer<T, U> orElse(final BiConsumer<T, U> functional, final BiConsumer<E, T> fallen) {
    return ThrowableBiConsumer.orElse(functional::accept, fallen);
  }

  /**
   * should never write "ugly" try-catch block for {@link Throwable} (s) in stream .
   *
   * @param <T> the type of the first argument to the function
   * @param <U> the type of the second argument to the function
   * @param <R> the type of the result of the function
   * @param <E> anything thrown
   * @param functional {@link BiFunction}
   * @param fallen {@link BiFunction}
   * @return {@link BiFunction#apply(Object, Object) functional.apply(T, U)} if done it normally, or {@link BiFunction#apply(Object, Object) fallen.apply(E, T)} if error occured
   */
  static <T, U, R, E extends Throwable> BiFunction<T, U, R> orElse(final BiFunction<T, U, R> functional, final BiFunction<E, T, R> fallen) {
    return ThrowableBiFunction.orElse(functional::apply, fallen);
  }

  /**
   * should never write "ugly" try-catch block for {@link Throwable} (s) in stream .
   *
   * @param <T> the type of the input to the operator
   * @param <E> anything thrown
   * @param functional {@link BinaryOperator}
   * @param fallen {@link BiFunction}
   * @return {@link BinaryOperator#apply(Object, Object) functional.apply(T, T)} if done it normally, or {@link BiFunction#apply(Object, Object) fallen.apply(E, T)} if error occured
   */
  static <T, E extends Throwable> BinaryOperator<T> orElse(final BinaryOperator<T> functional, final BiFunction<E, T, T> fallen) {
    return ThrowableBinaryOperator.orElse(functional::apply, fallen);
  }

  /**
   * should never write "ugly" try-catch block for {@link Throwable} (s) in stream .
   *
   * @param <T> the type of the input to the function
   * @param <E> anything thrown
   * @param functional {@link Consumer}
   * @param fallen {@link BiConsumer}
   * @return {@link Consumer#accept(Object) functional.accept(T)} if done it normally, or {@link BiConsumer#accept(Object, Object) fallen.accept(E, T)} if error occured
   */
  static <T, E extends Throwable> Consumer<T> orElse(final Consumer<T> functional, final BiConsumer<E, T> fallen) {
    return ThrowableConsumer.orElse(functional::accept, fallen);
  }

  /**
   * should never write "ugly" try-catch block for {@link Throwable} (s) in stream .
   *
   * @param <T> the type of the input to the function
   * @param <R> the type of the result of the function
   * @param <E> anything thrown
   * @param functional {@link Function}
   * @param fallen {@link Function}
   * @return {@link Function#apply(Object) functional.apply(T)} if done it normally, or {@link BiFunction#apply(Object, Object) fallen.apply(Throwable, T)} if error occured
   */
  static <T, R, E extends Throwable> Function<T, R> orElse(final Function<T, R> functional, final BiFunction<E, T, R> fallen) {
    return ThrowableFunction.orElse(functional::apply, fallen);
  }

  /**
   * should never write "ugly" try-catch block for {@link Throwable} (s) in stream .
   *
   * @param <T> the type of the input to the operator
   * @param <E> anything thrown
   * @param functional {@link UnaryOperator}
   * @param fallen {@link BiFunction}
   * @return {@link UnaryOperator#apply(Object) functional.apply(T)} if done it normally, or {@link BiFunction#apply(Object, Object) fallen.apply(E, T)} if error occured
   */
  static <T, E extends Throwable> UnaryOperator<T> orElse(final UnaryOperator<T> functional, final BiFunction<E, T, T> fallen) {
    return ThrowableUnaryOperator.orElse(functional::apply, fallen);
  }

  /**
   * should never write "ugly" try-catch block for {@link Throwable} (s) in stream .
   *
   * @param <T> the type of the input to the operator
   * @param <E> anything thrown
   * @param functional {@link Predicate}
   * @param fallen {@link BiPredicate}
   * @return {@link Predicate#test(Object) functional.test(T)} if done it normally, or {@link BiPredicate#test(Object, Object) fallen.test(E, T)} if error occured
   */
  static <T, E extends Throwable> Predicate<T> orElse(final Predicate<T> functional, final BiPredicate<E, T> fallen) {
    return ThrowablePredicate.orElse(functional::test, fallen);
  }

  /**
   * should never write "ugly" try-catch block for {@link Throwable} (s) in stream .
   *
   * @param <T> the type of the first argument to the function
   * @param <U> the type of the second argument to the function
   * @param <E> anything thrown
   * @param functional {@link BiPredicate}
   * @param fallen {@link BiPredicate}
   * @return {@link BiPredicate#test(Object, Object) functional.test(T, U)} if done it normally, or {@link BiPredicate#test(Object, Object) fallen.test(E, T)} if error occured
   */
  static <T, U, E extends Throwable> BiPredicate<T, U> orElse(final BiPredicate<T, U> functional, final BiPredicate<E, T> fallen) {
    return ThrowableBiPredicate.orElse(functional::test, fallen);
  }

}
