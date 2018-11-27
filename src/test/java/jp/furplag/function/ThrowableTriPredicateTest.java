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

import java.util.function.BooleanSupplier;
import java.util.function.Predicate;

import org.junit.Test;

import jp.furplag.function.Trebuchet.TriPredicate;

public class ThrowableTriPredicateTest {

  @Test
  public void test() {
    ThrowableTriPredicate<Integer, Integer, Integer> isOdd = (t, u, v) -> (t + u + v) % 2 != 0;
    try {
      isOdd.test(null, 0, 0);
      fail("there must raise NullPointerException .");
    } catch (Exception ex) {
      assertThat(ex instanceof NullPointerException, is(true));
    }
    assertThat(ThrowableTriPredicate.orNot(null, 1, 2, isOdd), is(false));
    assertThat(ThrowableTriPredicate.orNot(1, 2, 3, isOdd), is(false));
    assertThat(ThrowableTriPredicate.orNot(2, 3, 4, isOdd), is(true));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void paintItGreen() {
    ThrowableTriPredicate<Integer, Integer, Integer> isOdd = (t, u, v) -> (t + u + v) % 2 != 0;
    isOdd.andThen((t) -> !t);
  }

  @Test
  public void testOf() {
    ThrowableTriPredicate<Integer, Integer, Integer> isOdd = (t, u, v) -> (t + u + v) % 2 != 0;
    assertThat(ThrowableTriPredicate.of(isOdd, (TriPredicate<Integer, Integer, Integer>) null).apply(null, null, null), is(false));
    assertThat(ThrowableTriPredicate.of(isOdd, (t, u, v) -> t != null).apply(null, null, null), is(false));
    assertThat(ThrowableTriPredicate.of(isOdd, (t, u, v) -> t != null).apply(1, 2, 3), is(false));
    assertThat(ThrowableTriPredicate.of(isOdd, (Predicate<? extends Throwable>) null).apply(null, null, null), is(false));
    assertThat(ThrowableTriPredicate.of(isOdd, (e) -> !(e instanceof NullPointerException)).apply(null, null, null), is(false));
    assertThat(ThrowableTriPredicate.of(isOdd, (t, u, v) -> t != null).apply(2, 3, 4), is(true));
  }

  @Test
  public void orDefault() {
    ThrowableTriPredicate<Integer, Integer, Integer> isOdd = (t, u, v) -> (t + u + v) % 2 != 0;
    assertThat(ThrowableTriPredicate.orDefault(null, null, null, isOdd, false), is(false));
    assertThat(ThrowableTriPredicate.orDefault(null, null, null, isOdd, true), is(true));
    assertThat(ThrowableTriPredicate.orDefault(1, 2, 3, isOdd, false), is(false));
    assertThat(ThrowableTriPredicate.orDefault(2, 3, 4, isOdd, false), is(true));
    assertThat(ThrowableTriPredicate.orDefault(1, 2, 3, isOdd, true), is(false));
    assertThat(ThrowableTriPredicate.orDefault(2, 3, 4, isOdd, true), is(true));
  }

  @Test
  public void testOrElseGet() {
    ThrowableTriPredicate<Integer, Integer, Integer> isOdd = (t, u, v) -> (t + u + v) % 2 != 0;
    assertThat(ThrowableTriPredicate.orElseGet(null, null, null, isOdd, (BooleanSupplier) null), is(false));
    assertThat(ThrowableTriPredicate.orElseGet(null, null, null, isOdd, () -> false), is(false));
    assertThat(ThrowableTriPredicate.orElseGet(null, null, null, isOdd, () -> true), is(true));
    assertThat(ThrowableTriPredicate.orElseGet(1, 2, 3, isOdd, () -> false), is(false));
    assertThat(ThrowableTriPredicate.orElseGet(2, 3, 4, isOdd, () -> false), is(true));
    assertThat(ThrowableTriPredicate.orElseGet(1, 2, 3, isOdd, () -> true), is(false));
    assertThat(ThrowableTriPredicate.orElseGet(2, 3, 4, isOdd, () -> true), is(true));
  }

  @Test
  public void testOrNot() {
    ThrowableTriPredicate<Integer, Integer, Integer> isOdd = (t, u, v) -> (t + u + v) % 2 != 0;
    assertThat(ThrowableTriPredicate.orNot(null, null, null, isOdd), is(false));
    assertThat(ThrowableTriPredicate.orNot(1, 2, 3, isOdd), is(false));
    assertThat(ThrowableTriPredicate.orNot(2, 3, 4, isOdd), is(true));
  }

  @Test
  public void testAnd() {
    ThrowableTriPredicate<Integer, Integer, Integer> isOdd = (t, u, v) -> (t + u + v) % 2 != 0;
    ThrowableTriPredicate<Integer, Integer, Integer> clanOfThree = (t, u, v) -> (t + u + v) % 3 == 0;
    ThrowableTriPredicate<Integer, Integer, Integer> isOddAndClanOfThree = isOdd.and(clanOfThree);
    assertThat(isOddAndClanOfThree.apply(0, 1, 2), is(true));
    assertThat(isOddAndClanOfThree.apply(1, 2, 3), is(false));
    assertThat(isOddAndClanOfThree.apply(2, 3, 4), is(true));

    assertThat(isOdd.and(null).apply(0, 1, 2), is(false));
    assertThat(isOdd.and(null).apply(1, 2, 3), is(false));
    assertThat(isOdd.and(null).apply(2, 3, 4), is(false));
  }

  @Test
  public void testNegate() {
    ThrowableTriPredicate<Integer, Integer, Integer> isOdd = (t, u, v) -> (t + u + v) % 2 != 0;
    ThrowableTriPredicate<Integer, Integer, Integer> isEven = isOdd.negate();
    assertThat(isEven.apply(0, 1, 2), is(false));
    assertThat(isEven.apply(1, 2, 3), is(true));
    assertThat(isEven.apply(2, 3, 4), is(false));
  }

  @Test
  public void testOr() {
    ThrowableTriPredicate<Integer, Integer, Integer> isOdd = (t, u, v) -> (t + u + v) % 2 != 0;
    ThrowableTriPredicate<Integer, Integer, Integer> clanOfThree = (t, u, v) -> (t + u + v) % 3 == 0;
    ThrowableTriPredicate<Integer, Integer, Integer> isOddOrClanOfThree = isOdd.or(clanOfThree);
    assertThat(isOddOrClanOfThree.apply(0, 1, 2), is(true));
    assertThat(isOddOrClanOfThree.apply(1, 2, 3), is(true));
    assertThat(isOddOrClanOfThree.apply(2, 3, 4), is(true));

    assertThat(isOdd.or(null).apply(0, 1, 2), is(true));
    assertThat(isOdd.or(null).apply(1, 2, 3), is(false));
    assertThat(isOdd.or(null).apply(2, 3, 4), is(true));
  }
}
