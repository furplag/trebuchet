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
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import org.junit.Test;

import jp.furplag.function.suppress.SuppressOperator;

public class SuppressorTest {

  @Test
  public void test() {
    assertTrue(new Suppressor() {} instanceof Suppressor);
    assertTrue(Suppressor.class.isAssignableFrom(new Suppressor() {}.getClass()));
  }

  @Test
  public void testOrElse() {
    Trebuchet.ThrowableUnaryOperator<Integer> divider = (x) -> (10 / x);
    Integer[] expect = Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> SuppressOperator.tryOut(i, divider)).toArray(Integer[]::new);

    assertArrayEquals(expect, Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> Suppressor.orElse(i, ((Function<Integer, Integer>) divider), i)).toArray(Integer[]::new));
    assertArrayEquals(expect, Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> Suppressor.orElse(i, ((UnaryOperator<Integer>) divider), i)).toArray(Integer[]::new));
    assertArrayEquals(expect, Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> Suppressor.orElse(i, ((Trebuchet.ThrowableFunction<Integer, Integer>) divider), i)).toArray(Integer[]::new));
    assertArrayEquals(expect, Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> Suppressor.orElse(i, divider, i)).toArray(Integer[]::new));
  }

  @Test
  public void testOrElseBi() {
    Trebuchet.ThrowableBinaryOperator<Integer> divider = (x, y) -> (x / y);
    Integer[] expect = Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> SuppressOperator.tryOut(i, i, divider)).toArray(Integer[]::new);

    assertArrayEquals(expect, Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> Suppressor.orElse(i, i, ((BiFunction<Integer, Integer, Integer>) divider), i)).toArray(Integer[]::new));
    assertArrayEquals(expect, Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> Suppressor.orElse(i, i, ((BinaryOperator<Integer>) divider), i)).toArray(Integer[]::new));
    assertArrayEquals(expect, Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> Suppressor.orElse(i, i, ((Trebuchet.ThrowableBiFunction<Integer, Integer, Integer>) divider), i)).toArray(Integer[]::new));
    assertArrayEquals(expect, Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).map(i -> Suppressor.orElse(i, i, divider, i)).toArray(Integer[]::new));
  }
}
