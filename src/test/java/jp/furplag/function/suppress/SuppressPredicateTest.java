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

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

import org.junit.Test;

import jp.furplag.function.Trebuchet;

public class SuppressPredicateTest {

  @Test
  public void test() {
    assertTrue(new SuppressPredicate() {} instanceof SuppressPredicate);
    assertTrue(SuppressPredicate.class.isAssignableFrom(new SuppressPredicate() {}.getClass()));
  }

  @Test
  public void testIsCorrectFunction() {
    Trebuchet.ThrowableFunction<Integer, Boolean> isOdd = (x) -> (x % 2 != 0);
    Boolean[] expect = Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> Trebuchet.orElse(isOdd, (e, x) -> false).apply(i)).toArray(Boolean[]::new);
    assertArrayEquals(expect, Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> SuppressPredicate.isCorrect(i, ((Function<Integer, Boolean>) isOdd))).toArray(Boolean[]::new));
    assertArrayEquals(expect, Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> SuppressPredicate.isCorrect(i, isOdd)).toArray(Boolean[]::new));

    Trebuchet.ThrowableBiFunction<Integer, Integer, Boolean> isEven = (x, y) -> (x + y) % 2 == 0;
    expect = Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> Trebuchet.orElse(isEven, (e, x) -> false).apply(i, i)).toArray(Boolean[]::new);
    assertArrayEquals(expect, Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> SuppressPredicate.isCorrect(i, i, ((BiFunction<Integer, Integer, Boolean>) isEven))).toArray(Boolean[]::new));
    assertArrayEquals(expect, Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> SuppressPredicate.isCorrect(i, i, isEven)).toArray(Boolean[]::new));
  }

  @Test
  public void testIsCorrectPredicate() {
    Trebuchet.ThrowablePredicate<Integer> isOdd = (x) -> (x % 2 != 0);
    Boolean[] expect = Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> Trebuchet.orElse(isOdd, (e, x) -> false).test(i)).toArray(Boolean[]::new);
    assertArrayEquals(expect, Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> SuppressPredicate.isCorrect(i, ((Predicate<Integer>) isOdd))).toArray(Boolean[]::new));
    assertArrayEquals(expect, Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> SuppressPredicate.isCorrect(i, isOdd)).toArray(Boolean[]::new));

    Trebuchet.ThrowableBiPredicate<Integer, Integer> isEven = (x, y) -> (x + y) % 2 == 0;
    expect = Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> Trebuchet.orElse(isEven, (e, x) -> false).test(i, i)).toArray(Boolean[]::new);
    assertArrayEquals(expect, Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> SuppressPredicate.isCorrect(i, i, ((BiPredicate<Integer, Integer>) isEven))).toArray(Boolean[]::new));
    assertArrayEquals(expect, Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> SuppressPredicate.isCorrect(i, i, isEven)).toArray(Boolean[]::new));
  }
}
