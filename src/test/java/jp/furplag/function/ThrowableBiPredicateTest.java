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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.function.BiPredicate;
import java.util.function.BooleanSupplier;

import org.junit.Test;

import jp.furplag.function.Trebuchet.TriPredicate;

public class ThrowableBiPredicateTest {

  @Test
  public void test() {
    ThrowableBiPredicate<Integer, Integer> isOdd = (t, u) -> (t + u) % 2 != 0;
    try {
      isOdd.test(null, 0);
      fail("there must raise NullPointerException .");
    } catch (Exception ex) {
      assertThat(ex instanceof NullPointerException, is(true));
    }
    assertThat(ThrowableBiPredicate.orNot(null, 1, isOdd), is(false));
    assertThat(ThrowableBiPredicate.orNot(1, 1, isOdd), is(false));
    assertThat(ThrowableBiPredicate.orNot(1, 2, isOdd), is(true));
  }

  @Test
  public void testOf() {
    ThrowableBiPredicate<Integer, Integer> isOdd = (t, u) -> (t + u) % 2 != 0;
    assertThat(ThrowableBiPredicate.of(isOdd, (BiPredicate<Integer, Integer>) null).apply(null, null), is(false));
    assertThat(ThrowableBiPredicate.of(isOdd, (t, u) -> t != null).apply(null, null), is(false));
    assertThat(ThrowableBiPredicate.of(isOdd, (t, u) -> t != null).apply(1, 1), is(false));
    assertThat(ThrowableBiPredicate.of(isOdd, (TriPredicate<Integer, Integer, Throwable>) null).apply(null, null), is(false));
    assertThat(ThrowableBiPredicate.of(isOdd, (t, u, e) -> !(e instanceof NullPointerException)).apply(null, null), is(false));
    assertThat(ThrowableBiPredicate.of(isOdd, (t, u, e) -> t != null).apply(1, 2), is(true));
  }

  @Test
  public void orDefault() {
    ThrowableBiPredicate<Integer, Integer> isOdd = (t, u) -> (t + u) % 2 != 0;
    assertThat(ThrowableBiPredicate.orDefault(null, null, isOdd, false), is(false));
    assertThat(ThrowableBiPredicate.orDefault(null, null, isOdd, true), is(true));
    assertThat(ThrowableBiPredicate.orDefault(1, 1, isOdd, false), is(false));
    assertThat(ThrowableBiPredicate.orDefault(1, 2, isOdd, false), is(true));
    assertThat(ThrowableBiPredicate.orDefault(1, 1, isOdd, true), is(false));
    assertThat(ThrowableBiPredicate.orDefault(1, 2, isOdd, true), is(true));
  }

  @Test
  public void testOrElse() {
    ThrowableBiPredicate<Integer, Integer> isOdd = (t, u) -> (t + u) % 2 != 0;
    assertThat(ThrowableBiPredicate.orElse(null, null, isOdd, (BiPredicate<Integer, Integer>) null), is(false));
    assertThat(ThrowableBiPredicate.orElse(null, null, isOdd, (TriPredicate<Integer, Integer, Throwable>) null), is(false));
    assertThat(ThrowableBiPredicate.orElse(null, null, isOdd, (t, u, e) -> !(e instanceof NullPointerException)), is(false));
    assertThat(ThrowableBiPredicate.orElse(null, null, isOdd, (t, u) -> t != null), is(false));
    assertThat(ThrowableBiPredicate.orElse(1, 1, isOdd, (t) -> t != null), is(false));
    assertThat(ThrowableBiPredicate.orElse(1, 2, isOdd, (t) -> t != null), is(true));
  }

  @Test
  public void testOrElseGet() {
    ThrowableBiPredicate<Integer, Integer> isOdd = (t, u) -> (t + u) % 2 != 0;
    assertThat(ThrowableBiPredicate.orElseGet(null, null, isOdd, (BooleanSupplier) null), is(false));
    assertThat(ThrowableBiPredicate.orElseGet(null, null, isOdd, () -> false), is(false));
    assertThat(ThrowableBiPredicate.orElseGet(null, null, isOdd, () -> true), is(true));
    assertThat(ThrowableBiPredicate.orElseGet(1, 1, isOdd, () -> false), is(false));
    assertThat(ThrowableBiPredicate.orElseGet(1, 2, isOdd, () -> false), is(true));
    assertThat(ThrowableBiPredicate.orElseGet(1, 1, isOdd, () -> true), is(false));
    assertThat(ThrowableBiPredicate.orElseGet(1, 2, isOdd, () -> true), is(true));
  }

  @Test
  public void testOrNot() {
    ThrowableBiPredicate<Integer, Integer> isOdd = (t, u) -> (t + u) % 2 != 0;
    assertThat(ThrowableBiPredicate.orNot(null, null, isOdd), is(false));
    assertThat(ThrowableBiPredicate.orNot(1, 1, isOdd), is(false));
    assertThat(ThrowableBiPredicate.orNot(1, 2, isOdd), is(true));
  }

  @Test
  public void testAnd() {
    ThrowableBiPredicate<Integer, Integer> isOdd = (t, u) -> (t + u) % 2 != 0;
    ThrowableBiPredicate<Integer, Integer> clanOfThree = (t, u) -> (t + u) % 3 == 0;
    ThrowableBiPredicate<Integer, Integer> isOddAndClanOfThree = isOdd.and(clanOfThree);
    assertThat(isOddAndClanOfThree.apply(0, 1), is(false));
    assertThat(isOddAndClanOfThree.apply(3, 3), is(false));
    assertThat(isOddAndClanOfThree.apply(6, 3), is(true));
  }

  @Test
  public void testNegate() {
    ThrowableBiPredicate<Integer, Integer> isOdd = (t, u) -> (t + u) % 2 != 0;
    ThrowableBiPredicate<Integer, Integer> isEven = isOdd.negate();
    assertThat(isEven.apply(0, 1), is(false));
    assertThat(isEven.apply(3, 3), is(true));
    assertThat(isEven.apply(6, 3), is(false));
    assertThat(isEven.apply(6, 4), is(true));
  }

  @Test
  public void testOr() {
    ThrowableBiPredicate<Integer, Integer> isOdd = (t, u) -> (t + u) % 2 != 0;
    ThrowableBiPredicate<Integer, Integer> clanOfThree = (t, u) -> (t + u) % 3 == 0;
    ThrowableBiPredicate<Integer, Integer> isOddOrClanOfThree = isOdd.or(clanOfThree);
    assertThat(isOddOrClanOfThree.apply(0, 1), is(true));
    assertThat(isOddOrClanOfThree.apply(3, 3), is(true));
    assertThat(isOddOrClanOfThree.apply(6, 3), is(true));
    assertThat(isOddOrClanOfThree.apply(6, 4), is(false));
  }
}
