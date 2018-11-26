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
import java.util.function.Predicate;

import org.junit.Test;

public class ThrowablePredicateTest {

  @Test
  public void test() {
    ThrowablePredicate<Integer> isOdd = (t) -> t % 2 != 0;
    try {
      isOdd.test(null);
      fail("there must raise NullPointerException .");
    } catch (Exception ex) {
      assertThat(ex instanceof NullPointerException, is(true));
    }
    assertThat(ThrowablePredicate.orNot(null, isOdd), is(false));
    assertThat(ThrowablePredicate.orNot(2, isOdd), is(false));
    assertThat(ThrowablePredicate.orNot(3, isOdd), is(true));
  }

  @Test
  public void testOf() {
    assertThat(ThrowablePredicate.of((t) -> t % 2 != 0, (Predicate<Integer>) null).apply(null), is(false));
    assertThat(ThrowablePredicate.of((t) -> t % 2 != 0, (BiPredicate<Integer, Throwable>) null).apply(null), is(false));
    assertThat(ThrowablePredicate.of((Integer t) -> t % 2 != 0, (t, e) -> !(e instanceof NullPointerException)).apply(null), is(false));
    assertThat(ThrowablePredicate.of((Integer t) -> t % 2 != 0, (t) -> t != null).apply(null), is(false));
    assertThat(ThrowablePredicate.of((Integer t) -> t % 2 != 0, (t) -> t != null).apply(2), is(false));
    assertThat(ThrowablePredicate.of((Integer t) -> t % 2 != 0, (t) -> t != null).apply(3), is(true));
  }

  @Test
  public void orDefault() {
    assertThat(ThrowablePredicate.orDefault((Integer) null, (t) -> t % 2 != 0, false), is(false));
    assertThat(ThrowablePredicate.orDefault((Integer) null, (t) -> t % 2 != 0, true), is(true));
    assertThat(ThrowablePredicate.orDefault(2, (t) -> t % 2 != 0, false), is(false));
    assertThat(ThrowablePredicate.orDefault(3, (t) -> t % 2 != 0, false), is(true));
    assertThat(ThrowablePredicate.orDefault(2, (t) -> t % 2 != 0, true), is(false));
    assertThat(ThrowablePredicate.orDefault(3, (t) -> t % 2 != 0, true), is(true));
  }

  @Test
  public void testOrElse() {
    assertThat(ThrowablePredicate.orElse((Integer) null, (t) -> t % 2 != 0, (Predicate<Integer>) null), is(false));
    assertThat(ThrowablePredicate.orElse((Integer) null, (t) -> t % 2 != 0, (BiPredicate<Integer, Throwable>) null), is(false));
    assertThat(ThrowablePredicate.orElse((Integer) null, (t) -> t % 2 != 0, (t, e) -> !(e instanceof NullPointerException)), is(false));
    assertThat(ThrowablePredicate.orElse((Integer) null, (t) -> t % 2 != 0, (t) -> t != null), is(false));
    assertThat(ThrowablePredicate.orElse(2, (t) -> t % 2 != 0, (t) -> t != null), is(false));
    assertThat(ThrowablePredicate.orElse(3, (t) -> t % 2 != 0, (t) -> t != null), is(true));
  }

  @Test
  public void testOrElseGet() {
    assertThat(ThrowablePredicate.orElseGet((Integer) null, (t) -> t % 2 != 0, (BooleanSupplier) null), is(false));
    assertThat(ThrowablePredicate.orElseGet((Integer) null, (t) -> t % 2 != 0, () -> false), is(false));
    assertThat(ThrowablePredicate.orElseGet((Integer) null, (t) -> t % 2 != 0, () -> true), is(true));
    assertThat(ThrowablePredicate.orElseGet(2, (t) -> t % 2 != 0, () -> false), is(false));
    assertThat(ThrowablePredicate.orElseGet(3, (t) -> t % 2 != 0, () -> false), is(true));
    assertThat(ThrowablePredicate.orElseGet(2, (t) -> t % 2 != 0, () -> true), is(false));
    assertThat(ThrowablePredicate.orElseGet(3, (t) -> t % 2 != 0, () -> true), is(true));
  }

  @Test
  public void testOrNot() {
    assertThat(ThrowablePredicate.orNot((Integer) null, (t) -> t % 2 != 0), is(false));
    assertThat(ThrowablePredicate.orNot(2, (t) -> t % 2 != 0), is(false));
    assertThat(ThrowablePredicate.orNot(3, (t) -> t % 2 != 0), is(true));
  }

}
