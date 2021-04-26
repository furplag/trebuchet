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
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;
import org.junit.jupiter.api.Test;
import jp.furplag.function.Trebuchet.TriPredicate;

public class ThrowableTriPredicateTest {

  @Test
  public void orDefault() {
    ThrowableTriPredicate<Integer, Integer, Integer> isOdd = (t, u, v) -> (t + u + v) % 2 != 0;
    assertFalse(ThrowableTriPredicate.orDefault(null, null, null, isOdd, false));
    assertTrue(ThrowableTriPredicate.orDefault(null, null, null, isOdd, true));
    assertFalse(ThrowableTriPredicate.orDefault(1, 2, 3, isOdd, false));
    assertTrue(ThrowableTriPredicate.orDefault(2, 3, 4, isOdd, false));
    assertFalse(ThrowableTriPredicate.orDefault(1, 2, 3, isOdd, true));
    assertTrue(ThrowableTriPredicate.orDefault(2, 3, 4, isOdd, true));
  }

  @Test
  public void paintItGreen() {
    ThrowableTriPredicate<Integer, Integer, Integer> isOdd = (t, u, v) -> (t + u + v) % 2 != 0;
    assertThrows(UnsupportedOperationException.class, () -> isOdd.andThen((t) -> !t));
  }

  @Test
  public void test() {
    ThrowableTriPredicate<Integer, Integer, Integer> isOdd = (t, u, v) -> (t + u + v) % 2 != 0;
    try {
      isOdd.test(null, 0, 0);
      fail("there must raise NullPointerException .");
    } catch (Exception ex) {
      assertTrue(ex instanceof NullPointerException);
    }
    assertFalse(ThrowableTriPredicate.orNot(null, 1, 2, isOdd));
    assertFalse(ThrowableTriPredicate.orNot(1, 2, 3, isOdd));
    assertTrue(ThrowableTriPredicate.orNot(2, 3, 4, isOdd));
  }

  @Test
  public void testAnd() {
    ThrowableTriPredicate<Integer, Integer, Integer> isOdd = (t, u, v) -> (t + u + v) % 2 != 0;
    ThrowableTriPredicate<Integer, Integer, Integer> clanOfThree = (t, u, v) -> (t + u + v) % 3 == 0;
    ThrowableTriPredicate<Integer, Integer, Integer> isOddAndClanOfThree = isOdd.and(clanOfThree);
    assertTrue(isOddAndClanOfThree.apply(0, 1, 2));
    assertFalse(isOddAndClanOfThree.apply(1, 2, 3));
    assertTrue(isOddAndClanOfThree.apply(2, 3, 4));

    assertFalse(isOdd.and(null).apply(0, 1, 2));
    assertFalse(isOdd.and(null).apply(1, 2, 3));
    assertFalse(isOdd.and(null).apply(2, 3, 4));
  }

  @Test
  public void testNegate() {
    ThrowableTriPredicate<Integer, Integer, Integer> isOdd = (t, u, v) -> (t + u + v) % 2 != 0;
    ThrowableTriPredicate<Integer, Integer, Integer> isEven = isOdd.negate();
    assertFalse(isEven.apply(0, 1, 2));
    assertTrue(isEven.apply(1, 2, 3));
    assertFalse(isEven.apply(2, 3, 4));
  }

  @Test
  public void testOf() {
    ThrowableTriPredicate<Integer, Integer, Integer> isOdd = (t, u, v) -> (t + u + v) % 2 != 0;
    assertFalse(ThrowableTriPredicate.of(isOdd, (TriPredicate<Integer, Integer, Integer>) null).apply(null, null, null));
    assertFalse(ThrowableTriPredicate.of(isOdd, (t, u, v) -> t != null).apply(null, null, null));
    assertFalse(ThrowableTriPredicate.of(isOdd, (t, u, v) -> t != null).apply(1, 2, 3));
    assertFalse(ThrowableTriPredicate.of(isOdd, (Predicate<? extends Throwable>) null).apply(null, null, null));
    assertFalse(ThrowableTriPredicate.of(isOdd, (e) -> !(e instanceof NullPointerException)).apply(null, null, null));
    assertTrue(ThrowableTriPredicate.of(isOdd, (t, u, v) -> t != null).apply(2, 3, 4));
  }

  @Test
  public void testOr() {
    ThrowableTriPredicate<Integer, Integer, Integer> isOdd = (t, u, v) -> (t + u + v) % 2 != 0;
    ThrowableTriPredicate<Integer, Integer, Integer> clanOfThree = (t, u, v) -> (t + u + v) % 3 == 0;
    ThrowableTriPredicate<Integer, Integer, Integer> isOddOrClanOfThree = isOdd.or(clanOfThree);
    assertTrue(isOddOrClanOfThree.apply(0, 1, 2));
    assertTrue(isOddOrClanOfThree.apply(1, 2, 3));
    assertTrue(isOddOrClanOfThree.apply(2, 3, 4));

    assertTrue(isOdd.or(null).apply(0, 1, 2));
    assertFalse(isOdd.or(null).apply(1, 2, 3));
    assertTrue(isOdd.or(null).apply(2, 3, 4));
  }

  @Test
  public void testOrElseGet() {
    ThrowableTriPredicate<Integer, Integer, Integer> isOdd = (t, u, v) -> (t + u + v) % 2 != 0;
    assertFalse(ThrowableTriPredicate.orElseGet(null, null, null, isOdd, (BooleanSupplier) null));
    assertFalse(ThrowableTriPredicate.orElseGet(null, null, null, isOdd, () -> false));
    assertTrue(ThrowableTriPredicate.orElseGet(null, null, null, isOdd, () -> true));
    assertFalse(ThrowableTriPredicate.orElseGet(1, 2, 3, isOdd, () -> false));
    assertTrue(ThrowableTriPredicate.orElseGet(2, 3, 4, isOdd, () -> false));
    assertFalse(ThrowableTriPredicate.orElseGet(1, 2, 3, isOdd, () -> true));
    assertTrue(ThrowableTriPredicate.orElseGet(2, 3, 4, isOdd, () -> true));
  }

  @Test
  public void testOrNot() {
    ThrowableTriPredicate<Integer, Integer, Integer> isOdd = (t, u, v) -> (t + u + v) % 2 != 0;
    assertFalse(ThrowableTriPredicate.orNot(null, null, null, isOdd));
    assertFalse(ThrowableTriPredicate.orNot(1, 2, 3, isOdd));
    assertTrue(ThrowableTriPredicate.orNot(2, 3, 4, isOdd));
  }
}
