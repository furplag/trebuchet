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

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import org.junit.Test;

public class SuppressorTest {

  @Test
  public void test() {
    assertTrue(new Suppressor() {} instanceof Suppressor);
    assertTrue(Suppressor.class.isAssignableFrom(new Suppressor() {}.getClass()));
  }

  @Test
  public void testIsCorrect() {
    Trebuchet.ThrowableFunction<Integer, Boolean> isOdd = (x) -> (x % 2 != 0);
    Boolean[] expect = Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> Trebuchet.orElse(isOdd, (e, x) -> false).apply(i)).toArray(Boolean[]::new);
    assertArrayEquals(expect, Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> Suppressor.isCorrect(i, isOdd::apply)).toArray(Boolean[]::new));

    Trebuchet.ThrowableBiFunction<Integer, Integer, Boolean> isEven = (x, y) -> (x + y) % 2 == 0;
    expect = Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> Trebuchet.orElse(isEven, (e, x) -> false).apply(i, i)).toArray(Boolean[]::new);
    assertArrayEquals(expect, Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> Suppressor.isCorrect(i, i, isEven)).toArray(Boolean[]::new));

    Predicate<Integer> isOddPredicate = (x) -> (x % 2 != 0);
    Boolean[] expectOdd = Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> Trebuchet.orElse(isOddPredicate::test, (e, x) -> false).apply(i)).toArray(Boolean[]::new);
    assertArrayEquals(expectOdd, Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> Suppressor.isCorrect(i, isOddPredicate::test)).toArray(Boolean[]::new));

    BiPredicate<Integer, Integer> isEvenPredicate = (x, y) -> (x + y) % 2 == 0;
    Boolean[] expectEven = Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> Trebuchet.orElse(isEvenPredicate::test, (e, x) -> false).apply(i, i)).toArray(Boolean[]::new);
    assertArrayEquals(expectEven, Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> Suppressor.isCorrect(i, i, isEvenPredicate::test)).toArray(Boolean[]::new));
  }

  @Test
  public void testOrElse() {
    Function<Integer, Integer> divider = (x) -> (10 / x);
    Integer[] expect = Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> Trebuchet.orElse(divider, (ex, e) -> e).apply(i)).toArray(Integer[]::new);
    assertArrayEquals(expect, Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> Suppressor.orElse(i, (x) -> (10 / x), i)).toArray(Integer[]::new));
    assertArrayEquals(expect, Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> Suppressor.orElse(i, divider::apply, i)).toArray(Integer[]::new));

    UnaryOperator<Integer> dividerOfUnaryOperator = (x) -> (10 / x);
    assertArrayEquals(expect, Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> Suppressor.orElse(i, dividerOfUnaryOperator::apply, i)).toArray(Integer[]::new));

    Predicate<Integer> isOdd = (x) -> x % 2 != 0;
    Boolean[] expectOdd = Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> ((Function<Integer, Boolean>) Trebuchet.orElse(isOdd::test, (ex, e) -> false)).apply(i)).toArray(Boolean[]::new);
    assertArrayEquals(expectOdd, Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> Suppressor.orElse(i, isOdd::test, false)).toArray(Boolean[]::new));

    BiFunction<Integer, Integer, Integer> dividerBi = (x, y) -> (x / y);
    Integer[] expectBi = Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> Trebuchet.orElse(dividerBi::apply, (ex, e) -> e).apply(i, i)).toArray(Integer[]::new);
    assertArrayEquals(expectBi, Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> Suppressor.orElse(i, i, dividerBi::apply, i)).toArray(Integer[]::new));
    assertArrayEquals(expectBi, Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> Suppressor.orElse(i, i, (x, y) -> (x / y), i)).toArray(Integer[]::new));

    BinaryOperator<Integer> dividerBinaryOperator = (x, y) -> (x / y);
    Integer[] expectBinaryOperator = Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> Trebuchet.orElse(dividerBinaryOperator::apply, (ex, e) -> e).apply(i, i)).toArray(Integer[]::new);
    assertArrayEquals(expectBinaryOperator, Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> Suppressor.orElse(i, i, dividerBinaryOperator::apply, i)).toArray(Integer[]::new));

    BiPredicate<Integer, Integer> isEven = (x, y) -> (x + y) % 2 == 0;
    Boolean[] expectOddBi = Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> ((BiFunction<Integer, Integer, Boolean>) Trebuchet.orElse(isEven::test, (ex, e) -> false)).apply(i, i)).toArray(Boolean[]::new);
    assertArrayEquals(expectOddBi, Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> Suppressor.orElse(i, i, isEven::test, false)).toArray(Boolean[]::new));
  }

  @Test
  public void testOrNot() {
    final Set<Integer> expect = new HashSet<>();
    final Set<Integer> actual = new HashSet<>();
    Consumer<Integer> expectConsumer = (x) -> expect.add(x * 2);
    Consumer<Integer> actualConsumer = (x) -> actual.add(x * 2);
    Arrays.stream(new Integer[]{0, 1, 2, 3, 4, null}).forEach(i -> Trebuchet.orElse(expectConsumer, (ex, e) -> {}).accept(i));
    Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).forEach(i -> Suppressor.orNot(i, actualConsumer::accept));
    assertArrayEquals(expect.stream().sorted(Comparator.naturalOrder()).toArray(Integer[]::new), actual.stream().sorted(Comparator.naturalOrder()).toArray(Integer[]::new));

    final Set<Integer> expectBi = new HashSet<>();
    final Set<Integer> actualBi = new HashSet<>();
    BiConsumer<Integer, Set<Integer>> consumerBi = (x, y) -> y.add(x * 2);
    Arrays.stream(new Integer[]{0, 1, 2, 3, 4, null}).forEach(i -> Trebuchet.orElse(consumerBi, (ex, e) -> {}).accept(i, expectBi));
    Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).forEach(i -> Suppressor.orNot(i, actualBi, consumerBi::accept));
    assertArrayEquals(expectBi.stream().sorted(Comparator.naturalOrder()).toArray(Integer[]::new), actual.stream().sorted(Comparator.naturalOrder()).toArray(Integer[]::new));
  }

  @Test
  public void testOrNull() {
    Function<Integer, Integer> divider = (x) -> (10 / x);
    Integer[] expect = Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> Trebuchet.orElse(divider, (e, x) -> null).apply(i)).toArray(Integer[]::new);
    assertArrayEquals(expect, Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> Suppressor.orNull(i, (x) -> (10 / x))).toArray(Integer[]::new));
    assertArrayEquals(expect, Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> Suppressor.orNull(i, divider::apply)).toArray(Integer[]::new));

    BiFunction<Integer, Integer, Integer> dividerBi = (x, y) -> (x / y);
    Integer[] expectBi = Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> Trebuchet.orElse(dividerBi, (e, x) -> null).apply(i, i)).toArray(Integer[]::new);
    assertArrayEquals(expectBi, Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> Suppressor.orNull(i, i, (x, y) -> (x / y))).toArray(Integer[]::new));
    assertArrayEquals(expectBi, Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> Suppressor.orNull(i, i, dividerBi::apply)).toArray(Integer[]::new));
  }

  @Test
  public void testTtyOut() {
    UnaryOperator<Integer> divider = (x) -> (10 / x);
    Integer[] expect = Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> Trebuchet.orElse(divider, (e, x) -> x).apply(i)).toArray(Integer[]::new);
    assertArrayEquals(expect, Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> Suppressor.orDefault(i, (x) -> (10 / x))).toArray(Integer[]::new));
    assertArrayEquals(expect, Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> Suppressor.orDefault(i, divider::apply)).toArray(Integer[]::new));

    BinaryOperator<Integer> dividerBi = (x, y) -> (x / y);
    Integer[] expectBi = Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> Trebuchet.orElse(dividerBi, (e, x) -> x).apply(i, i)).toArray(Integer[]::new);
    assertArrayEquals(expectBi, Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> Suppressor.orDefault(i, i, (x, y) -> (x / y))).toArray(Integer[]::new));
    assertArrayEquals(expectBi, Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> Suppressor.orDefault(i, i, dividerBi::apply)).toArray(Integer[]::new));
  }

}
