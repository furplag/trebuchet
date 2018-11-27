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

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

import org.junit.Test;

import jp.furplag.function.Trebuchet.TriFunction;
import jp.furplag.function.misc.FunctionTest;

public class ThrowableTriFunctionTest implements FunctionTest {

  @Test
  public void test() {
    ThrowableTriFunction<Integer, Integer, Integer, Integer> function = (t, u, v) -> (t + u) / v;
    try {
      function.apply(0, 0, 0);
      fail("there must raise ArithmeticException .");
    } catch (Exception ex) {
      assertThat(ex instanceof ArithmeticException, is(true));
      assertThat(function.apply(1, 1, 2), is(1));
    }
    try {
      ThrowableTriFunction.orElse(0, 0, 0, (t, u, v) -> (t + u) / v, (e) -> {Trebuchet.sneakyThrow(e); return null;});
      fail("there must raise ArithmeticException .");
    } catch (Exception ex) {
      assertThat(ex instanceof ArithmeticException, is(true));
    }
    final TriFunction<Integer, Integer, Integer, Integer> divider = ThrowableTriFunction.of((t, u, v) -> (t + u) / v, (t, u, v) -> Integer.valueOf(Objects.toString(t, "0")));
    assertArrayEquals(new Integer[] { 0, 2, 2, 2, 2, 0 }, Arrays.stream(anArray).map((t) -> divider.apply(t, t, t)).toArray(Integer[]::new));
  }

  @Test
  @Override
  public void testAndThen() {
    final TriFunction<Integer, Integer, Integer, Integer> divider = ThrowableTriFunction.of((t, u, v) -> (t + u) / v, (t, u, v) -> Integer.valueOf(Objects.toString(t, "0")));
    final TriFunction<Integer, Integer, Integer, String> stringifier = divider.andThen(Objects::toString);
    assertArrayEquals(Arrays.asList(new Integer[] { 0, 2, 2, 2, 2, 0 }).stream().map(Objects::toString).toArray(String[]::new), Arrays.stream(anArray).map((t) -> stringifier.apply(t, t, t)).toArray(String[]::new));
    TriFunction<Integer, Integer, Integer, String> vanilla = ((TriFunction<Integer, Integer, Integer, Integer>) (t, u, v) -> (t + u + v)).andThen((t) -> t.toString());
    TriFunction<Integer, Integer, Integer, String> throwable = ThrowableTriFunction.of((Integer t, Integer u, Integer v) -> t + u + v, (t, u, v) -> Integer.valueOf(Objects.toString(t, "0"))).andThen(Objects::toString);
    assertThat(vanilla.apply(1, 2, 3), is("6"));
    assertThat(throwable.apply(1, 2, 3), is("6"));
    String actual = null;
    try {
      actual = vanilla.apply(null, null, null);
      fail("no one execute this line .");
    } catch (Exception ex) {
      actual = throwable.apply(null, null, null);
    }
    assertThat(actual, is("0"));
  }

  @Test
  @Override
  public void testApplyOrElse() {
    try {
      ThrowableTriFunction.applyOrDefault(null, null, null, (String t, Integer u, Integer v) -> t.substring(u, v), (String) null);
      fail("no one execute this line .");
    } catch (Exception ex) {
      assertThat(ex instanceof NullPointerException, is(true));
    }
    assertThat(ThrowableTriFunction.applyOrDefault(null, 0, 1, (String t, Integer u, Integer v) -> t.substring(u, v), "滅"), is("滅"));
    assertThat(ThrowableTriFunction.applyOrDefault("南無阿弥陀仏", 0, 1, (String t, Integer u, Integer v) -> t.substring(u, v), "滅"), is("南"));
  }

  @Test
  @Override
  public void testOf() {
    assertThat(ThrowableTriFunction.of((t, u, v) -> String.join("", t.substring(u, v)), (TriFunction<String, Integer, Integer, String>) null).apply(null, null, null), is((String) null));
    assertThat(ThrowableTriFunction.of((String t, Integer u, Integer v) -> t.substring(u, v), (Function<Exception, String>) null).apply(null, null, null), is((String) null));
    assertThat(ThrowableTriFunction.of((String t, Integer u, Integer v) -> t.substring(u, v), (t, u, v) -> "諸行無常").apply(null, null, null), is("諸行無常"));
    assertThat(ThrowableTriFunction.of((String t, Integer u, Integer v) -> t.substring(u, v), (t, u, v) -> "諸行無常").apply(null, 0, 2), is("諸行無常"));
    assertThat(ThrowableTriFunction.of((String t, Integer u, Integer v) -> t.substring(u, v), (t, u, v) -> "諸行無常").apply("南無阿弥陀仏", 0, 2), is("南無"));
    assertThat(ThrowableTriFunction.of((String t, Integer u, Integer v) -> t.substring(u, v), (e) -> e.getClass().getSimpleName()).apply(null, null, null), is("NullPointerException"));
    assertThat(ThrowableTriFunction.of((String t, Integer u, Integer v) -> t.substring(u, v), (e) -> e.getClass().getSimpleName()).apply("南無阿弥陀仏", 0, 999), is("StringIndexOutOfBoundsException"));
    assertThat(ThrowableTriFunction.of((String t, Integer u, Integer v) -> t.substring(u, v), (e) -> e.getClass().getSimpleName()).apply("南無阿弥陀仏", 0, 6), is("南無阿弥陀仏"));
  }

  @Test
  @Override
  public void testOrDefault() {
    assertThat(ThrowableTriFunction.orDefault(null, null, null, (String t, Integer u, Integer v) -> t.substring(u, v), null), is((String) null));
    assertThat(ThrowableTriFunction.orDefault(null, 0, 2, (String t, Integer u, Integer v) -> t.substring(u, v), null), is((String) null));
    assertThat(ThrowableTriFunction.orDefault(null, 0, 2, (String t, Integer u, Integer v) -> t.substring(u, v), "諸行無常"), is("諸行無常"));
    assertThat(ThrowableTriFunction.orDefault("南無阿弥陀仏", 0, 2, (t, u, v) -> t.substring(u, v), "諸行無常"), is("南無"));
  }

  @Test
  @Override
  public void testOrElse() {
    assertThat(ThrowableTriFunction.orElse(null, null, null, (t, u, v) -> t.substring(u, v), (TriFunction<String, Integer, Integer, String>) null), is((String) null));
    assertThat(ThrowableTriFunction.orElse(null, null, null, (String t, Integer u, Integer v) -> t.substring(u, v), (Function<Exception, String>) null), is((String) null));
    assertThat(ThrowableTriFunction.orElse(null, null, null, (String t, Integer u, Integer v) -> t.substring(u, v), (t, u, v) -> "諸行無常"), is("諸行無常"));
    assertThat(ThrowableTriFunction.orElse(null, 0, 2, (String t, Integer u, Integer v) -> t.substring(u, v), (t, u, v) -> "諸行無常"), is("諸行無常"));
    assertThat(ThrowableTriFunction.orElse("南無阿弥陀仏", 0, 2, (t, u, v) -> t.substring(u, v), (t, u, v) -> "諸行無常"), is("南無"));
    assertThat(ThrowableTriFunction.orElse(null, null, null, (String t, Integer u, Integer v) -> t.substring(u, v), (e) -> e.getClass().getSimpleName()), is("NullPointerException"));
    assertThat(ThrowableTriFunction.orElse("南無阿弥陀仏", 0, 999, (String t, Integer u, Integer v) -> t.substring(u, v), (e) -> e.getClass().getSimpleName()), is("StringIndexOutOfBoundsException"));
    assertThat(ThrowableTriFunction.orElse("南無阿弥陀仏", 0, 6, (String t, Integer u, Integer v) -> t.substring(u, v), (e) -> e.getClass().getSimpleName()), is("南無阿弥陀仏"));
  }

  @Test
  @Override
  public void testOrElseGet() {
    assertThat(ThrowableTriFunction.orElseGet(null, null, null, (String t, Integer u, Integer v) -> t.substring(u, v), (Supplier<String>) null), is((String) null));
    assertThat(ThrowableTriFunction.orElseGet(null, null, null, (String t, Integer u, Integer v) -> t.substring(u, v), () -> "諸行無常"), is("諸行無常"));
    assertThat(ThrowableTriFunction.orElseGet(null, 0, 2, (String t, Integer u, Integer v) -> t.substring(u, v), () -> "諸行無常"), is("諸行無常"));
    assertThat(ThrowableTriFunction.orElseGet("南無阿弥陀仏", 0, 2, (t, u, v) -> t.substring(u, v), () -> "諸行無常"), is("南無"));
  }

  @Test
  @Override
  public void testOrNull() {
    assertThat(ThrowableTriFunction.orNull(null, null, null, (String t, Integer u, Integer v) -> t.substring(u, v)), is((String) null));
    assertThat(ThrowableTriFunction.orNull(null, 0, 2, (String t, Integer u, Integer v) -> t.substring(u, v)), is((String) null));
    assertThat(ThrowableTriFunction.orNull("南無阿弥陀仏", 0, 2, (t, u, v) -> t.substring(u, v)), is("南無"));
  }

}
