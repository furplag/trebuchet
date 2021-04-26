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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import java.util.function.BiPredicate;
import java.util.function.BooleanSupplier;
import org.junit.jupiter.api.Test;
import jp.furplag.function.Trebuchet.TriPredicate;

public class ThrowableBiPredicateTest {

  @Test
  public void paintItGreen() {
    ThrowableBiPredicate<Integer, Integer> isOdd = (t, u) -> (t + u) % 2 != 0;
    assertThrows(UnsupportedOperationException.class, () -> isOdd.andThen((t) -> !t));
  }

  @Test
  public void test() {
    ThrowableBiPredicate<Integer, Integer> isOdd = (t, u) -> (t + u) % 2 != 0;
    try {
      isOdd.test(null, 0);
      fail("there must raise NullPointerException .");
    } catch (Exception ex) {
      assertTrue(ex instanceof NullPointerException);
    }
    assertFalse(ThrowableBiPredicate.orNot(null, 1, isOdd));
    assertFalse(ThrowableBiPredicate.orNot(1, 1, isOdd));
    assertTrue(ThrowableBiPredicate.orNot(1, 2, isOdd));
  }

  @Test
  public void testAnd() {
    ThrowableBiPredicate<Integer, Integer> isOdd = (t, u) -> (t + u) % 2 != 0;
    ThrowableBiPredicate<Integer, Integer> clanOfThree = (t, u) -> (t + u) % 3 == 0;
    ThrowableBiPredicate<Integer, Integer> isOddAndClanOfThree = isOdd.and(clanOfThree);
    assertFalse(isOddAndClanOfThree.apply(0, 1));
    assertFalse(isOddAndClanOfThree.apply(3, 3));
    assertTrue(isOddAndClanOfThree.apply(6, 3));

    assertFalse(isOdd.and(null).apply(0, 1));
    assertFalse(isOdd.and(null).apply(3, 3));
    assertFalse(isOdd.and(null).apply(6, 3));
  }

  @Test
  public void testNegate() {
    ThrowableBiPredicate<Integer, Integer> isOdd = (t, u) -> (t + u) % 2 != 0;
    ThrowableBiPredicate<Integer, Integer> isEven = isOdd.negate();
    assertFalse(isEven.apply(0, 1));
    assertTrue(isEven.apply(3, 3));
    assertFalse(isEven.apply(6, 3));
    assertTrue(isEven.apply(6, 4));
  }

  @Test
  public void testOf() {
    ThrowableBiPredicate<Integer, Integer> isOdd = (t, u) -> (t + u) % 2 != 0;
    assertFalse(ThrowableBiPredicate.of(isOdd, (BiPredicate<Integer, Integer>) null).apply(null, null));
    assertFalse(ThrowableBiPredicate.of(isOdd, (t, u) -> t != null).apply(null, null));
    assertFalse(ThrowableBiPredicate.of(isOdd, (t, u) -> t != null).apply(1, 1));
    assertFalse(ThrowableBiPredicate.of(isOdd, (TriPredicate<Integer, Integer, Throwable>) null).apply(null, null));
    assertFalse(ThrowableBiPredicate.of(isOdd, (t, u, e) -> !(e instanceof NullPointerException)).apply(null, null));
    assertTrue(ThrowableBiPredicate.of(isOdd, (t, u, e) -> t != null).apply(1, 2));
  }

  @Test
  public void testOr() {
    ThrowableBiPredicate<Integer, Integer> isOdd = (t, u) -> (t + u) % 2 != 0;
    ThrowableBiPredicate<Integer, Integer> clanOfThree = (t, u) -> (t + u) % 3 == 0;
    ThrowableBiPredicate<Integer, Integer> isOddOrClanOfThree = isOdd.or(clanOfThree);
    assertTrue(isOddOrClanOfThree.apply(0, 1));
    assertTrue(isOddOrClanOfThree.apply(3, 3));
    assertTrue(isOddOrClanOfThree.apply(6, 3));
    assertFalse(isOddOrClanOfThree.apply(6, 4));

    assertTrue(isOdd.or(null).apply(0, 1));
    assertFalse(isOdd.or(null).apply(3, 3));
    assertTrue(isOdd.or(null).apply(6, 3));
  }

  @Test
  public void testOrDefault() {
    ThrowableBiPredicate<Integer, Integer> isOdd = (t, u) -> (t + u) % 2 != 0;
    assertFalse(ThrowableBiPredicate.orDefault(null, null, isOdd, false));
    assertTrue(ThrowableBiPredicate.orDefault(null, null, isOdd, true));
    assertFalse(ThrowableBiPredicate.orDefault(1, 1, isOdd, false));
    assertTrue(ThrowableBiPredicate.orDefault(1, 2, isOdd, false));
    assertFalse(ThrowableBiPredicate.orDefault(1, 1, isOdd, true));
    assertTrue(ThrowableBiPredicate.orDefault(1, 2, isOdd, true));
  }

  @Test
  public void testOrElse() {
    ThrowableBiPredicate<Integer, Integer> isOdd = (t, u) -> (t + u) % 2 != 0;
    assertFalse(ThrowableBiPredicate.orElse(null, null, isOdd, (BiPredicate<Integer, Integer>) null));
    assertFalse(ThrowableBiPredicate.orElse(null, null, isOdd, (TriPredicate<Integer, Integer, Throwable>) null));
    assertFalse(ThrowableBiPredicate.orElse(null, null, isOdd, (t, u, e) -> !(e instanceof NullPointerException)));
    assertFalse(ThrowableBiPredicate.orElse(null, null, isOdd, (t, u) -> t != null));
    assertFalse(ThrowableBiPredicate.orElse(1, 1, isOdd, (t) -> t != null));
    assertTrue(ThrowableBiPredicate.orElse(1, 2, isOdd, (t) -> t != null));
  }

  @Test
  public void testOrElseGet() {
    ThrowableBiPredicate<Integer, Integer> isOdd = (t, u) -> (t + u) % 2 != 0;
    assertFalse(ThrowableBiPredicate.orElseGet(null, null, isOdd, (BooleanSupplier) null));
    assertFalse(ThrowableBiPredicate.orElseGet(null, null, isOdd, () -> false));
    assertTrue(ThrowableBiPredicate.orElseGet(null, null, isOdd, () -> true));
    assertFalse(ThrowableBiPredicate.orElseGet(1, 1, isOdd, () -> false));
    assertTrue(ThrowableBiPredicate.orElseGet(1, 2, isOdd, () -> false));
    assertFalse(ThrowableBiPredicate.orElseGet(1, 1, isOdd, () -> true));
    assertTrue(ThrowableBiPredicate.orElseGet(1, 2, isOdd, () -> true));
  }

  @Test
  public void testOrNot() {
    ThrowableBiPredicate<Integer, Integer> isOdd = (t, u) -> (t + u) % 2 != 0;
    assertFalse(ThrowableBiPredicate.orNot(null, null, isOdd));
    assertFalse(ThrowableBiPredicate.orNot(1, 1, isOdd));
    assertTrue(ThrowableBiPredicate.orNot(1, 2, isOdd));
  }
}
