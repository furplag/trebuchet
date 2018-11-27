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
import java.util.function.Function;
import java.util.stream.IntStream;

import org.junit.Test;

import jp.furplag.function.misc.FunctionTest;

public class ThrowableFunctionTest implements FunctionTest {

  @Test
  public void test() {
    ThrowableFunction<Integer, Integer> function = (t) -> t / t;
    try {
      function.apply(0);
      fail("there must raise ArithmeticException .");
    } catch (Exception ex) {
      assertThat(ex instanceof ArithmeticException, is(true));
      assertThat(function.apply(2), is(1));
    }
    try {
      ThrowableFunction.orElse(Integer.valueOf(0), (t) -> t / t, (t, e) -> {Trebuchet.sneakyThrow(e); return null;});
      fail("there must raise ArithmeticException .");
    } catch (Exception ex) {
      assertThat(ex instanceof ArithmeticException, is(true));
    }
    final Function<Integer, Integer> divider = ThrowableFunction.of((t) -> t / t, (t, e) -> Integer.valueOf(Objects.toString(t, "0")));
    assertArrayEquals(new Integer[] { 0, 1, 1, 1, 1, 0 }, Arrays.stream(anArray).map(divider).toArray(Integer[]::new));
  }

  @Test
  @Override
  public void testAndThen() {
    final Function<Integer, Integer> divider = ThrowableFunction.of((t) -> t / t, (t, e) -> Integer.valueOf(Objects.toString(t, "0")));
    final Function<Integer, String> stringifier = divider.andThen(Objects::toString);
    assertArrayEquals(Arrays.asList(new Integer[] { 0, 1, 1, 1, 1, 0 }).stream().map(Objects::toString).toArray(String[]::new), Arrays.stream(anArray).map(stringifier).toArray(String[]::new));
    Function<Integer, String> vanilla = ((Function<Integer, Integer>) (i) -> (i + i)).andThen((i) -> i.toString());
    Function<Integer, String> throwable = ThrowableFunction.of((Integer t) -> t + t, (Integer t, Throwable e) -> Integer.valueOf(Objects.toString(t, "0"))).andThen(Objects::toString);
    assertThat(vanilla.apply(5), is("10"));
    assertThat(throwable.apply(5), is("10"));
    String actual = null;
    try {
      actual = vanilla.apply(null);
      fail("no one execute this line .");
    } catch (Exception ex) {
      actual = throwable.apply(null);
    }
    assertThat(actual, is("0"));
  }

  @Test
  @Override
  public void testApplyOrElse() {
    try {
      ThrowableFunction.applyOrDefault((String) null, (String t) -> t.codePoints().findFirst().orElse(-1), (Integer) null);
      fail("no one execute this line .");
    } catch (Exception ex) {
      assertThat(ex instanceof NullPointerException, is(true));
    }
    assertThat(ThrowableFunction.applyOrDefault("仏", (t) -> t.codePoints().findFirst().orElse(-1), -99), is("仏".codePointAt(0)));
    assertThat(ThrowableFunction.applyOrDefault((String) null, (String t) -> t.codePoints().findFirst().orElse(-1), -99), is(-99));
    assertThat(ThrowableFunction.applyOrDefault("", (t) -> t.codePoints().findFirst().orElse(-1), -99), is(-1));
    assertThat(ThrowableFunction.applyOrDefault("南無阿弥陀仏", (t) -> new HashMap<String, Integer>().get(t), -99), is(-99));
  }

  @Test
  public void testIdentity() {
    IntStream.rangeClosed(0, 9999).forEach((i) -> {
      assertThat(Function.identity().apply(i), is(i));
      assertThat(ThrowableFunction.identity().apply(i), is(i));
      assertThat(ThrowableFunction.identity().apply(i), is(Function.identity().apply(i)));
      assertThat(ThrowableFunction.identity().apply((String) null), is(Function.identity().apply((String) null)));
    });
  }

  @Test
  @Override
  public void testOf() {
    assertThat(ThrowableFunction.of((t) -> t.split(""), (Function<String, String[]>) null).apply(null), is((String[]) null));
    assertThat(ThrowableFunction.of((t) -> t.split(""), (BiFunction<String, Exception, String[]>) null).apply(null), is((String[]) null));
    assertArrayEquals("諸行無常".split(""), ThrowableFunction.of((String t) -> t.split(""), (t) -> "諸行無常".split("")).apply(null));
    assertArrayEquals("南無阿弥陀仏".split(""), ThrowableFunction.of((String t) -> t.split(""), (t) -> "諸行無常".split("")).apply("南無阿弥陀仏"));
    assertArrayEquals("NullPointerException".split(""), ThrowableFunction.of((String t) -> t.split(""), (t, e) -> e.getClass().getSimpleName().split("")).apply(null));
    assertArrayEquals("南無阿弥陀仏".split(""), ThrowableFunction.of((String t) -> t.split(""), (t, e) -> e.getClass().getSimpleName().split("")).apply("南無阿弥陀仏"));
  }

  @Test
  @Override
  public void testOrDefault() {
    assertThat(
      ThrowableFunction.orDefault(anArray, (t) -> Arrays.asList(t).stream().mapToInt((x) -> Integer.valueOf(Objects.toString(x, "0"))).sum(), 0)
    , is(ThrowableFunction.orElseGet(anArray, (t) -> Arrays.asList(t).stream().mapToInt((x) -> Integer.valueOf(Objects.toString(x, "0"))).sum(), () -> 0))
    );
    assertThat(
      ThrowableFunction.orDefault(anArray, (t) -> Arrays.asList(t).stream().mapToInt((x) -> Integer.valueOf(Objects.toString(x, "0"))).sum(), null)
    , is((Integer) ThrowableFunction.orElseGet(anArray, (t) -> Arrays.asList(t).stream().mapToInt((x) -> Integer.valueOf(Objects.toString(x, "0"))).sum(), () -> null))
    );
    assertArrayEquals(new Integer[] {3, 1, 1, 1, 1, 3}, Arrays.stream(anArray).map((t) -> ThrowableFunction.orDefault(t, (x) -> x / x, 3)).toArray(Integer[]::new));
  }

  @Test
  @Override
  public void testOrElse() {
    assertThat(ThrowableFunction.orElse((String) null,(t) -> t.split(""), (Function<String, String[]>) null), is((String[]) null));
    assertThat(ThrowableFunction.orElse((String) null,(t) -> t.split(""), (BiFunction<String, Throwable, String[]>) null), is((String[]) null));
    assertArrayEquals("諸行無常".split(""), ThrowableFunction.orElse((String) null, (t) -> t.split(""), (t) -> "諸行無常".split("")));
    assertArrayEquals("南無阿弥陀仏".split(""), ThrowableFunction.orElse("南無阿弥陀仏", (t) -> t.split(""), (t) -> "諸行無常".split("")));
    assertArrayEquals("NullPointerException".split(""), ThrowableFunction.orElse((String) null, (t) -> t.split(""), (t, e) -> e.getClass().getSimpleName().split("")));
    assertArrayEquals("南無阿弥陀仏".split(""), ThrowableFunction.orElse("南無阿弥陀仏", (t) -> t.split(""), (t, e) -> e.getClass().getSimpleName().split("")));
  }

  @Test
  @Override
  public void testOrElseGet() {
    assertThat(ThrowableFunction.orElseGet((String[]) null, (t) -> String.join("", t), () -> "諸行無常"), is("諸行無常"));
    assertThat(ThrowableFunction.orElseGet("南無阿弥陀仏".split(""), (t) -> String.join("", t), () -> "諸行無常"), is("南無阿弥陀仏"));
    assertThat(ThrowableFunction.orElseGet(anArray, (t) -> Arrays.asList(t).stream().mapToInt(Integer::intValue).sum(), null), is((Integer) null));
    assertThat(ThrowableFunction.orElseGet(anArray, (t) -> Arrays.asList(t).stream().mapToInt(Integer::intValue).sum(), () -> 0), is(0));
    assertThat(ThrowableFunction.orElseGet(anArray, (t) -> Arrays.asList(t).stream().mapToInt((x) -> Integer.valueOf(Objects.toString(x, "0"))).sum(), () -> 0), is(10));
    assertThat(
      ThrowableFunction.orElseGet(anArray, (t) -> Arrays.asList(t).stream().mapToInt((x) -> Integer.valueOf(Objects.toString(x, "0"))).sum(), null)
    , is((Integer) ThrowableFunction.orElseGet(anArray, (t) -> Arrays.asList(t).stream().mapToInt((x) -> Integer.valueOf(Objects.toString(x, "0"))).sum(), () -> null))
    );
  }

  @Test
  @Override
  public void testOrNull() {
    assertArrayEquals(new Integer[] {null, 1, 1, 1, 1, null}, Arrays.stream(anArray).map((t) -> ThrowableFunction.orNull(t, (x) -> x / x)).toArray(Integer[]::new));
  }

}
