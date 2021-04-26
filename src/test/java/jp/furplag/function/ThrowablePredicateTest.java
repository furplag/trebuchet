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
import java.util.function.Predicate;
import org.junit.jupiter.api.Test;

public class ThrowablePredicateTest {

  @Test
  public void orDefault() {
    assertFalse(ThrowablePredicate.orDefault((Integer) null, (t) -> t % 2 != 0, false));
    assertTrue(ThrowablePredicate.orDefault((Integer) null, (t) -> t % 2 != 0, true));
    assertFalse(ThrowablePredicate.orDefault(2, (t) -> t % 2 != 0, false));
    assertTrue(ThrowablePredicate.orDefault(3, (t) -> t % 2 != 0, false));
    assertFalse(ThrowablePredicate.orDefault(2, (t) -> t % 2 != 0, true));
    assertTrue(ThrowablePredicate.orDefault(3, (t) -> t % 2 != 0, true));
  }

  @Test
  public void paintItGreen() {
    ThrowablePredicate<Integer> isOdd = (t) -> t % 2 != 0;
    assertThrows(UnsupportedOperationException.class, () -> isOdd.andThen((t) -> !t));
  }

  @Test
  public void test() {
    ThrowablePredicate<Integer> isOdd = (t) -> t % 2 != 0;
    try {
      isOdd.test(null);
      fail("there must raise NullPointerException .");
    } catch (Exception ex) {
      assertTrue(ex instanceof NullPointerException);
    }
    assertFalse(ThrowablePredicate.orNot(null, isOdd));
    assertFalse(ThrowablePredicate.orNot(2, isOdd));
    assertTrue(ThrowablePredicate.orNot(3, isOdd));
  }

  @Test
  public void testAnd() {
    ThrowablePredicate<Integer> isOdd = (t) -> t % 2 != 0;
    ThrowablePredicate<Integer> clanOfThree = (t) -> (t) % 3 == 0;
    ThrowablePredicate<Integer> isOddAndClanOfThree = isOdd.and(clanOfThree);
    assertFalse(isOddAndClanOfThree.apply(1));
    assertFalse(isOddAndClanOfThree.apply(2));
    assertTrue(isOddAndClanOfThree.apply(3));

    assertFalse(isOdd.and(null).apply(1));
    assertFalse(isOdd.and(null).apply(2));
    assertFalse(isOdd.and(null).apply(3));
  }

  @Test
  public void testNegate() {
    ThrowablePredicate<Integer> isOdd = (t) -> t % 2 != 0;
    ThrowablePredicate<Integer> isEven = isOdd.negate();
    assertFalse(isEven.apply(1));
    assertTrue(isEven.apply(2));
    assertFalse(isEven.apply(3));
  }

  @Test
  public void testOf() {
    assertFalse(ThrowablePredicate.of((t) -> t % 2 != 0, (Predicate<Integer>) null).apply(null));
    assertFalse(ThrowablePredicate.of((t) -> t % 2 != 0, (BiPredicate<Integer, Throwable>) null).apply(null));
    assertFalse(ThrowablePredicate.of((Integer t) -> t % 2 != 0, (t, e) -> !(e instanceof NullPointerException)).apply(null));
    assertFalse(ThrowablePredicate.of((Integer t) -> t % 2 != 0, (t) -> t != null).apply(null));
    assertFalse(ThrowablePredicate.of((Integer t) -> t % 2 != 0, (t) -> t != null).apply(2));
    assertTrue(ThrowablePredicate.of((Integer t) -> t % 2 != 0, (t) -> t != null).apply(3));
  }

  @Test
  public void testOr() {
    ThrowablePredicate<Integer> isOdd = (t) -> t % 2 != 0;
    ThrowablePredicate<Integer> clanOfThree = (t) -> (t) % 3 == 0;
    ThrowablePredicate<Integer> isOddOrClanOfThree = isOdd.or(clanOfThree);
    assertTrue(isOddOrClanOfThree.apply(1));
    assertFalse(isOddOrClanOfThree.apply(2));
    assertTrue(isOddOrClanOfThree.apply(3));

    assertTrue(isOdd.or(null).apply(1));
    assertFalse(isOdd.or(null).apply(2));
    assertTrue(isOdd.or(null).apply(3));
  }

  @Test
  public void testOrElse() {
    assertFalse(ThrowablePredicate.orElse((Integer) null, (t) -> t % 2 != 0, (Predicate<Integer>) null));
    assertFalse(ThrowablePredicate.orElse((Integer) null, (t) -> t % 2 != 0, (BiPredicate<Integer, Throwable>) null));
    assertFalse(ThrowablePredicate.orElse((Integer) null, (t) -> t % 2 != 0, (t, e) -> !(e instanceof NullPointerException)));
    assertFalse(ThrowablePredicate.orElse((Integer) null, (t) -> t % 2 != 0, (t) -> t != null));
    assertFalse(ThrowablePredicate.orElse(2, (t) -> t % 2 != 0, (t) -> t != null));
    assertTrue(ThrowablePredicate.orElse(3, (t) -> t % 2 != 0, (t) -> t != null));
  }

  @Test
  public void testOrElseGet() {
    assertFalse(ThrowablePredicate.orElseGet((Integer) null, (t) -> t % 2 != 0, (BooleanSupplier) null));
    assertFalse(ThrowablePredicate.orElseGet((Integer) null, (t) -> t % 2 != 0, () -> false));
    assertTrue(ThrowablePredicate.orElseGet((Integer) null, (t) -> t % 2 != 0, () -> true));
    assertFalse(ThrowablePredicate.orElseGet(2, (t) -> t % 2 != 0, () -> false));
    assertTrue(ThrowablePredicate.orElseGet(3, (t) -> t % 2 != 0, () -> false));
    assertFalse(ThrowablePredicate.orElseGet(2, (t) -> t % 2 != 0, () -> true));
    assertTrue(ThrowablePredicate.orElseGet(3, (t) -> t % 2 != 0, () -> true));
  }

  @Test
  public void testOrNot() {
    assertFalse(ThrowablePredicate.orNot((Integer) null, (t) -> t % 2 != 0));
    assertFalse(ThrowablePredicate.orNot(2, (t) -> t % 2 != 0));
    assertTrue(ThrowablePredicate.orNot(3, (t) -> t % 2 != 0));
  }
}
