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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import jp.furplag.function.misc.FunctionTest;

public class ThrowableFunctionTest implements FunctionTest {

  @Test
  public void test() {
    ThrowableFunction<Integer, Integer> function = (t) -> t / t;
    try {
      function.apply(0);
      fail("there must raise ArithmeticException .");
    } catch (Exception ex) {
      assertTrue(ex instanceof ArithmeticException);
      assertEquals(1, function.apply(2));
    }
    try {
      ThrowableFunction.orElse(Integer.valueOf(0), (t) -> t / t, (t, e) -> {Trebuchet.sneakyThrow(e); return null;});
      fail("there must raise ArithmeticException .");
    } catch (Exception ex) {
      assertTrue(ex instanceof ArithmeticException);
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
    assertEquals("10", vanilla.apply(5));
    assertEquals("10", throwable.apply(5));
    String actual = null;
    try {
      actual = vanilla.apply(null);
      fail("no one execute this line .");
    } catch (Exception ex) {
      actual = throwable.apply(null);
    }
    assertEquals("0", actual);
  }

  @Test
  @Override
  public void testApplyOrElse() {
    try {
      ThrowableFunction.applyOrDefault((String) null, (String t) -> t.codePoints().findFirst().orElse(-1), (Integer) null);
      fail("no one execute this line .");
    } catch (Exception ex) {
      assertTrue(ex instanceof NullPointerException);
    }
    assertEquals("仏".codePointAt(0), ThrowableFunction.applyOrDefault("仏", (t) -> t.codePoints().findFirst().orElse(-1), -99));
    assertEquals(-99, ThrowableFunction.applyOrDefault((String) null, (String t) -> t.codePoints().findFirst().orElse(-1), -99));
    assertEquals(-1, ThrowableFunction.applyOrDefault("", (t) -> t.codePoints().findFirst().orElse(-1), -99));
    assertEquals(-99, ThrowableFunction.applyOrDefault("南無阿弥陀仏", (t) -> new HashMap<String, Integer>().get(t), -99));
  }

  @Test
  public void testIdentity() {
    IntStream.rangeClosed(0, 9999).forEach((i) -> {
      assertEquals(i, Function.identity().apply(i));
      assertEquals(i, ThrowableFunction.identity().apply(i));
      assertEquals(Function.identity().apply(i), ThrowableFunction.identity().apply(i));
      assertEquals(Function.identity().apply((String) null), ThrowableFunction.identity().apply((String) null));
    });
  }

  @Test
  @Override
  public void testOf() {
    assertEquals((String[]) null, ThrowableFunction.of((t) -> t.split(""), (Function<String, String[]>) null).apply(null));
    assertEquals((String[]) null, ThrowableFunction.of((t) -> t.split(""), (BiFunction<String, Exception, String[]>) null).apply(null));
    assertArrayEquals("諸行無常".split(""), ThrowableFunction.of((String t) -> t.split(""), (t) -> "諸行無常".split("")).apply(null));
    assertArrayEquals("南無阿弥陀仏".split(""), ThrowableFunction.of((String t) -> t.split(""), (t) -> "諸行無常".split("")).apply("南無阿弥陀仏"));
    assertArrayEquals("NullPointerException".split(""), ThrowableFunction.of((String t) -> t.split(""), (t, e) -> e.getClass().getSimpleName().split("")).apply(null));
    assertArrayEquals("南無阿弥陀仏".split(""), ThrowableFunction.of((String t) -> t.split(""), (t, e) -> e.getClass().getSimpleName().split("")).apply("南無阿弥陀仏"));
  }

  @Test
  @Override
  public void testOrDefault() {
    assertEquals(
      ThrowableFunction.orDefault(anArray, (t) -> Arrays.asList(t).stream().mapToInt((x) -> Integer.valueOf(Objects.toString(x, "0"))).sum(), 0)
    , ThrowableFunction.orElseGet(anArray, (t) -> Arrays.asList(t).stream().mapToInt((x) -> Integer.valueOf(Objects.toString(x, "0"))).sum(), () -> 0)
    );
    assertEquals(
      ThrowableFunction.orDefault(anArray, (t) -> Arrays.asList(t).stream().mapToInt((x) -> Integer.valueOf(Objects.toString(x, "0"))).sum(), null)
    , (Integer) ThrowableFunction.orElseGet(anArray, (t) -> Arrays.asList(t).stream().mapToInt((x) -> Integer.valueOf(Objects.toString(x, "0"))).sum(), () -> null)
    );
    assertArrayEquals(new Integer[] {3, 1, 1, 1, 1, 3}, Arrays.stream(anArray).map((t) -> ThrowableFunction.orDefault(t, (x) -> x / x, 3)).toArray(Integer[]::new));
  }

  @Test
  @Override
  public void testOrElse() {
    assertEquals((String[]) null, ThrowableFunction.orElse((String) null,(t) -> t.split(""), (Function<String, String[]>) null));
    assertEquals((String[]) null, ThrowableFunction.orElse((String) null,(t) -> t.split(""), (BiFunction<String, Throwable, String[]>) null));
    assertArrayEquals("諸行無常".split(""), ThrowableFunction.orElse((String) null, (t) -> t.split(""), (t) -> "諸行無常".split("")));
    assertArrayEquals("南無阿弥陀仏".split(""), ThrowableFunction.orElse("南無阿弥陀仏", (t) -> t.split(""), (t) -> "諸行無常".split("")));
    assertArrayEquals("NullPointerException".split(""), ThrowableFunction.orElse((String) null, (t) -> t.split(""), (t, e) -> e.getClass().getSimpleName().split("")));
    assertArrayEquals("南無阿弥陀仏".split(""), ThrowableFunction.orElse("南無阿弥陀仏", (t) -> t.split(""), (t, e) -> e.getClass().getSimpleName().split("")));
  }

  @Test
  @Override
  public void testOrElseGet() {
    assertEquals("諸行無常", ThrowableFunction.orElseGet((String[]) null, (t) -> String.join("", t), () -> "諸行無常"));
    assertEquals("南無阿弥陀仏", ThrowableFunction.orElseGet("南無阿弥陀仏".split(""), (t) -> String.join("", t), () -> "諸行無常"));
    assertEquals((Integer) null, ThrowableFunction.orElseGet(anArray, (t) -> Arrays.asList(t).stream().mapToInt(Integer::intValue).sum(), null));
    assertEquals(0, ThrowableFunction.orElseGet(anArray, (t) -> Arrays.asList(t).stream().mapToInt(Integer::intValue).sum(), () -> 0));
    assertEquals(10, ThrowableFunction.orElseGet(anArray, (t) -> Arrays.asList(t).stream().mapToInt((x) -> Integer.valueOf(Objects.toString(x, "0"))).sum(), () -> 0));
    assertEquals(
      ThrowableFunction.orElseGet(anArray, (t) -> Arrays.asList(t).stream().mapToInt((x) -> Integer.valueOf(Objects.toString(x, "0"))).sum(), null)
    , (Integer) ThrowableFunction.orElseGet(anArray, (t) -> Arrays.asList(t).stream().mapToInt((x) -> Integer.valueOf(Objects.toString(x, "0"))).sum(), () -> null)
    );
  }

  @Test
  @Override
  public void testOrNull() {
    assertArrayEquals(new Integer[] {null, 1, 1, 1, 1, null}, Arrays.stream(anArray).map((t) -> ThrowableFunction.orNull(t, (x) -> x / x)).toArray(Integer[]::new));
  }

}
