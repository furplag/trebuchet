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
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
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
    assertArrayEquals(expect, Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> Suppressor.isCorrect(i, ((Function<Integer, Boolean>) (x) -> (x % 2 != 0)))).toArray(Boolean[]::new));
    assertArrayEquals(expect, Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> Suppressor.isCorrect(i, ((Predicate<Integer>) (x) -> (x % 2 != 0)))).toArray(Boolean[]::new));
    assertArrayEquals(expect, Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> Suppressor.isCorrect(i, isOdd)).toArray(Boolean[]::new));
  }

  @Test
  public void testIsCorrectBi() {
    Trebuchet.ThrowableBiFunction<Integer, Integer, Boolean> isEven = (x, y) -> (x + y) % 2 == 0;
    Boolean[] expect = Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> Trebuchet.orElse(isEven, (e, x) -> false).apply(i, i)).toArray(Boolean[]::new);
    assertArrayEquals(expect, Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> Suppressor.isCorrect(i, i, ((BiFunction<Integer, Integer, Boolean>) (x, y) -> ((x + y) % 2 == 0)))).toArray(Boolean[]::new));
    assertArrayEquals(expect, Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> Suppressor.isCorrect(i, i, ((BiPredicate<Integer, Integer>) (x, y) -> ((x + y) % 2 == 0)))).toArray(Boolean[]::new));
    assertArrayEquals(expect, Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> Suppressor.isCorrect(i, i, isEven)).toArray(Boolean[]::new));
  }

  @Test
  public void testOrNull() {
    Trebuchet.ThrowableFunction<Integer, Integer> divider = (x) -> (10 / x);
    Integer[] expect = Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> Trebuchet.orElse(divider, (e, x) -> null).apply(i)).toArray(Integer[]::new);
    assertArrayEquals(expect, Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> Suppressor.orNull(i, divider)).toArray(Integer[]::new));
    assertArrayEquals(expect, Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> Suppressor.orNull(i, ((Function<Integer, Integer>) (x) -> (10 / x)))).toArray(Integer[]::new));
    assertArrayEquals(expect, Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> Suppressor.orNull(i, ((UnaryOperator<Integer>) (x) -> (10 / x)))).toArray(Integer[]::new));
  }

  @Test
  public void testOrNullBi() {
    Trebuchet.ThrowableBiFunction<Integer, Integer, Integer> divider = (x, y) -> (x / y);
    Integer[] expect = Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> Trebuchet.orElse(divider, (e, x) -> null).apply(i, i)).toArray(Integer[]::new);
    assertArrayEquals(expect, Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> Suppressor.orNull(i, i, (BiFunction<Integer, Integer, Integer>) (x, y) -> (x / y))).toArray(Integer[]::new));
    assertArrayEquals(expect, Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> Suppressor.orNull(i, i, (BinaryOperator<Integer>) (x, y) -> (x / y))).toArray(Integer[]::new));
    assertArrayEquals(expect, Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> Suppressor.orNull(i, i, divider)).toArray(Integer[]::new));
  }

  @Test
  public void testSuppress() {
    final Set<Integer> expect = new HashSet<>();
    final Set<Integer> actual = new HashSet<>();
    Arrays.stream(new Integer[]{0, 1, 2, 3, 4, null}).forEach(i -> Trebuchet.orElse((Integer x) -> expect.add(x * 2), (ex, e) -> {}).accept(i));
    Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).forEach(i -> Suppressor.suppress(i, (x) -> actual.add(x * 2)));
    assertArrayEquals(expect.stream().sorted(Comparator.naturalOrder()).toArray(Integer[]::new), actual.stream().sorted(Comparator.naturalOrder()).toArray(Integer[]::new));
  }

  @Test
  public void testSuppressBi() {
    final Set<Integer> expect = new HashSet<>();
    final Set<Integer> actual = new HashSet<>();
    Arrays.stream(new Integer[]{0, 1, 2, 3, 4, null}).forEach(i -> Trebuchet.orElse((Integer x, Set<Integer> y) -> y.add(x * 2), (ex, e) -> {}).accept(i, expect));
    Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).forEach(i -> Suppressor.suppress(i, actual, (x, y) -> y.add(x * 2)));
    assertArrayEquals(expect.stream().sorted(Comparator.naturalOrder()).toArray(Integer[]::new), actual.stream().sorted(Comparator.naturalOrder()).toArray(Integer[]::new));
  }

  @Test
  public void testTtyOut() {
    Trebuchet.ThrowableOperator<Integer> divider = (x) -> (10 / x);
    Integer[] expect = Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> Trebuchet.orElse(divider, (e, x) -> x).apply(i)).toArray(Integer[]::new);
    assertArrayEquals(expect, Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> Suppressor.tryOut(i,  ((Function<Integer, Integer>) (x) -> (10 / x)))).toArray(Integer[]::new));
    assertArrayEquals(expect, Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> Suppressor.tryOut(i,  ((Trebuchet.ThrowableFunction<Integer, Integer>) (x) -> (10 / x)))).toArray(Integer[]::new));
    assertArrayEquals(expect, Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> Suppressor.tryOut(i, divider)).toArray(Integer[]::new));
  }
}
