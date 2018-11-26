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

import java.util.function.BinaryOperator;

import org.junit.Test;

public class ThrowableBinaryOperatorTest {

  @Test
  public void test() {
    ThrowableBinaryOperator<Integer> one = (t, u) -> t / u;
    assertThat(ThrowableBiFunction.of(one, (t, u, e) -> 0).apply(0, 0), is(0));
    assertThat(ThrowableBiFunction.of(one, (t, u, e) -> 0).apply(1, 0), is(0));
    assertThat(ThrowableBiFunction.of(one, (t, u, e) -> 0).apply(10, 2), is(5));
    assertThat(ThrowableBiFunction.of(one, (t, u) -> t + u).apply(2, 0), is(2));
  }

  @Test
  public void testMinBy() {
    assertThat(ThrowableBinaryOperator.minBy(Integer::compare).apply(0, 1), is(BinaryOperator.minBy(Integer::compare).apply(0, 1)));
    assertThat(ThrowableBinaryOperator.maxBy(Integer::compare).apply(0, 1), is(BinaryOperator.maxBy(Integer::compare).apply(0, 1)));
    assertThat(ThrowableBinaryOperator.minBy(Integer::compare).apply(2, 1), is(BinaryOperator.minBy(Integer::compare).apply(2, 1)));
    assertThat(ThrowableBinaryOperator.maxBy(Integer::compare).apply(2, 1), is(BinaryOperator.maxBy(Integer::compare).apply(2, 1)));
  }
}
