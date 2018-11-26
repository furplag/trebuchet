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
import java.util.HashMap;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import org.junit.Test;

import jp.furplag.function.Trebuchet.TriFunction;
import jp.furplag.function.misc.FunctionTest;

public class ThrowableBiFunctionTest implements FunctionTest {

  @Test
  public void test() {
    ThrowableBiFunction<Integer, Integer, Integer> function = (t, u) -> t / u;
    try {
      function.apply(0, 0);
      fail("there must raise ArithmeticException .");
    } catch (Exception ex) {
      assertThat(ex instanceof ArithmeticException, is(true));
      assertThat(function.apply(2, 1), is(2));
    }
    try {
      ThrowableBiFunction.orElse(Integer.valueOf(0), Integer.valueOf(0), (t, u) -> t / u, (t, u, e) -> {Trebuchet.sneakyThrow(e); return null;});
      fail("there must raise ArithmeticException .");
    } catch (Exception ex) {
      assertThat(ex instanceof ArithmeticException, is(true));
    }
    final BiFunction<Integer, Integer, Integer> divider = ThrowableBiFunction.of((t, u) -> t / u, (t, u, e) -> Integer.valueOf(Objects.toString(t, "0")));
    assertArrayEquals(new Integer[] { 0, 1, 1, 1, 1, 0 }, Arrays.stream(anArray).map((t) -> divider.apply(t, t)).toArray(Integer[]::new));
  }

  @Test
  @Override
  public void testAndThen() {
    final BiFunction<String, String, String[]> splitter = ThrowableBiFunction.of((t, u) -> t.split(Objects.toString(u, "")), (t, u, e) -> "諸行無常".split(""));
    final BiFunction<String, String, String> joiner = splitter.andThen((t) -> String.join("@", t));
    assertThat(joiner.apply("南無阿弥陀仏", null), is("南@無@阿@弥@陀@仏"));
    assertThat(joiner.apply(null, null), is("諸@行@無@常"));
    BiFunction<Integer, Integer, String> vanilla = ((BiFunction<Integer, Integer, Integer>) (t, u) -> t + u).andThen((x) -> x.toString());
    BiFunction<Integer, Integer, String> throwable = ThrowableBiFunction.of((Integer t, Integer u) -> t + u, (Integer t, Integer u, Throwable e) -> Integer.valueOf(Objects.toString(t, "0"))).andThen(Objects::toString);
    assertThat(vanilla.apply(5, 5), is("10"));
    assertThat(throwable.apply(5, 5), is("10"));
    String actual = null;
    try {
      actual = vanilla.apply(null, 5);
      fail("no one execute this line .");
    } catch (Exception ex) {
      actual = throwable.apply(null, 5);
    }
    assertThat(actual, is("0"));
  }

  @Test
  @Override
  public void testOf() {
    assertThat(ThrowableBiFunction.of((t, u) -> t.split(u), (BiFunction<String, String, String[]>) null).apply(null, ""), is((String[]) null));
    assertThat(ThrowableBiFunction.of((t, u) -> t.split(u), (TriFunction<String, String, Exception, String[]>) null).apply(null, ""), is((String[]) null));
    assertThat(ThrowableBiFunction.of((String t, String u) -> t.split(u), (t, u) -> "諸行無常".split("")).andThen((t) -> String.join("", t)).apply(null, ""), is("諸行無常"));
    assertThat(ThrowableBiFunction.of((String t, String u) -> t.split(u), (t, u) -> "諸行無常".split(u)).andThen((t) -> String.join("", t)).apply("南無阿弥陀仏", ""), is("南無阿弥陀仏"));
    assertThat(ThrowableBiFunction.of((String t, String u) -> t.split(u), (t, u, e) -> e.getClass().getSimpleName().split("")).andThen((t) -> String.join("", t)).apply(null, null), is("NullPointerException"));
    assertThat(ThrowableBiFunction.of((String t, String u) -> t.split(u), (t, u, e) -> e.getClass().getSimpleName().split("")).andThen((t) -> String.join("", t)).apply("南@無@阿@弥@陀@仏", "@"), is("南無阿弥陀仏"));
  }

  @Test
  @Override
  public void testOrElse() {
    assertThat(ThrowableBiFunction.orElse(null, "", (t, u) -> t.split(u), (BiFunction<String, String, String[]>) null), is((String[]) null));
    assertThat(ThrowableBiFunction.orElse(null, "", (t, u) -> t.split(u), (TriFunction<String, String, Exception, String[]>) null), is((String[]) null));
    assertThat(ThrowableBiFunction.orElse((String) null, "", (t, u) -> String.join(u, t.split(u)), (t, u) -> "諸行無常"), is("諸行無常"));
    assertThat(ThrowableBiFunction.orElse("南@無@阿@弥@陀@仏", "@", (t, u) -> String.join("", t.split(u)), (t, u) -> "諸行無常"), is("南無阿弥陀仏"));
    assertThat(ThrowableBiFunction.orElse((String) null, (String) null, (t, u) -> String.join("", t.split(u)), (t, u, e) -> e.getClass().getSimpleName()), is("NullPointerException"));
    assertThat(ThrowableBiFunction.orElse("南@無@阿@弥@陀@仏", "@", (t, u) -> String.join("", t.split(u)), (t, u, e) -> e.getClass().getSimpleName()), is("南無阿弥陀仏"));
  }

  @Test
  @Override
  public void testOrElseGet() {
    assertThat(ThrowableBiFunction.orElseGet((String) null, "", (t, u) -> t.split(u), (Supplier<String[]>) null), is((String[]) null));
    assertThat(ThrowableBiFunction.orElseGet((String) null, "", (t, u) -> t.split(u), () -> null), is((String[]) null));
    assertThat(ThrowableBiFunction.orElseGet((String) null, "", (t, u) -> String.join(u, t.split(u)), () -> "諸行無常"), is("諸行無常"));
    assertThat(ThrowableBiFunction.orElseGet("南@無@阿@弥@陀@仏", "@", (t, u) -> String.join("", t.split(u)), () -> "諸行無常"), is("南無阿弥陀仏"));
    assertThat(
      ThrowableBiFunction.orElseGet(anArray, 0, (t, u) -> Arrays.asList(t).stream().mapToInt((x) -> Integer.valueOf(Objects.toString(x, u.toString()))).sum(), null)
    , is((Integer) ThrowableBiFunction.orElseGet(anArray, 0, (t, u) -> Arrays.asList(t).stream().mapToInt((x) -> Integer.valueOf(Objects.toString(x, u.toString()))).sum(), () -> null))
    );
  }

  @Test
  @Override
  public void testOrDefault() {
    assertThat(
      ThrowableBiFunction.orDefault(anArray, "0", (t, u) -> Arrays.asList(t).stream().mapToInt((x) -> Integer.valueOf(Objects.toString(x, u))).sum(), 0)
    , is(ThrowableBiFunction.orElseGet(anArray, "0", (t, u) -> Arrays.asList(t).stream().mapToInt((x) -> Integer.valueOf(Objects.toString(x, u))).sum(), () -> 0))
    );
    assertThat(
        ThrowableBiFunction.orDefault(anArray, "0", (t, u) -> Arrays.asList(t).stream().mapToInt((x) -> Integer.valueOf(Objects.toString(x, u))).sum(), null)
    , is((Integer) ThrowableBiFunction.orElseGet(anArray, "0", (t, u) -> Arrays.asList(t).stream().mapToInt((x) -> Integer.valueOf(Objects.toString(x, u))).sum(), () -> null))
    );
    assertArrayEquals(new Integer[] {3, 1, 1, 1, 1, 3}, Arrays.stream(anArray).map((t) -> ThrowableBiFunction.orDefault(t, t, (x, y) -> x / y, 3)).toArray(Integer[]::new));
  }

  @Test
  @Override
  public void testApplyOrElse() {
    try {
      ThrowableBiFunction.applyOrDefault((String) null, (Integer) null, (t, u) -> t.codePoints().findFirst().orElse(u), (Integer) null);
      fail("no one execute this line .");
    } catch (Exception ex) {
      assertThat(ex instanceof NullPointerException, is(true));
    }
    assertThat(ThrowableBiFunction.applyOrDefault("仏", -1, (t, u) -> t.codePoints().findFirst().orElse(u), -99), is("仏".codePointAt(0)));
    assertThat(ThrowableBiFunction.applyOrDefault((String) null, -1, (t, u) -> t.codePoints().findFirst().orElse(u), -99), is(-99));
    assertThat(ThrowableBiFunction.applyOrDefault("", -1, (t, u) -> t.codePoints().findFirst().orElse(u), -99), is(-1));
    assertThat(ThrowableBiFunction.applyOrDefault("南無阿弥陀仏", -1, (t, u) -> new HashMap<String, Integer>().get(t), -99), is(-99));
  }

  @Test
  @Override
  public void testOrNull() {
    assertArrayEquals(new Integer[] {null, 1, 1, 1, 1, null}, Arrays.stream(anArray).map((t) -> ThrowableBiFunction.orNull(t, t, (x, y) -> x / y)).toArray(Integer[]::new));
  }

}
