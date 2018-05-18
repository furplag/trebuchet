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
import java.util.function.BinaryOperator;
import java.util.function.Function;

import org.junit.Test;

import jp.furplag.function.Trebuchet;

public class SuppressOperatorTest {

  @Test
  public void test() {
    assertTrue(new SuppressOperator() {} instanceof SuppressOperator);
    assertTrue(SuppressOperator.class.isAssignableFrom(new SuppressOperator() {}.getClass()));
  }

  @Test
  public void testTtyOut() {
    Trebuchet.ThrowableUnaryOperator<Integer> divider = (x) -> (10 / x);
    Integer[] expect = Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> Trebuchet.orElse(divider, (e, x) -> x).apply(i)).toArray(Integer[]::new);
    assertArrayEquals(expect, Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> SuppressOperator.tryOut(i,  ((Function<Integer, Integer>) (x) -> (10 / x)))).toArray(Integer[]::new));
    assertArrayEquals(expect, Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> SuppressOperator.tryOut(i,  ((Trebuchet.ThrowableFunction<Integer, Integer>) (x) -> (10 / x)))).toArray(Integer[]::new));
    assertArrayEquals(expect, Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> SuppressOperator.tryOut(i, divider)).toArray(Integer[]::new));
  }

  @Test
  public void testTtyOutBi() {
    Trebuchet.ThrowableBinaryOperator<Integer> divider = (x, y) -> (x / y);
    Integer[] expect = Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> Trebuchet.orElse(divider, (e, x) -> x).apply(i, i)).toArray(Integer[]::new);
    assertArrayEquals(expect, Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> SuppressOperator.tryOut(i, i, (BiFunction<Integer, Integer, Integer>) (x, y) -> (x / y))).toArray(Integer[]::new));
    assertArrayEquals(expect, Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> SuppressOperator.tryOut(i, i, (BinaryOperator<Integer>) (x, y) -> (x / y))).toArray(Integer[]::new));
    assertArrayEquals(expect, Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> SuppressOperator.tryOut(i, i, divider)).toArray(Integer[]::new));
  }

}
