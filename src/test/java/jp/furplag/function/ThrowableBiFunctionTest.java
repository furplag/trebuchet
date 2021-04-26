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
import java.util.function.Supplier;
import org.junit.jupiter.api.Test;
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
      assertTrue(ex instanceof ArithmeticException);
      assertEquals(2, function.apply(2, 1));
    }
    try {
      ThrowableBiFunction.orElse(Integer.valueOf(0), Integer.valueOf(0), (t, u) -> t / u, (t, u, e) -> {Trebuchet.sneakyThrow(e); return null;});
      fail("there must raise ArithmeticException .");
    } catch (Exception ex) {
      assertTrue(ex instanceof ArithmeticException);
    }
    final BiFunction<Integer, Integer, Integer> divider = ThrowableBiFunction.of((t, u) -> t / u, (t, u, e) -> Integer.valueOf(Objects.toString(t, "0")));
    assertArrayEquals(Arrays.stream(anArray).map((t) -> divider.apply(t, t)).toArray(Integer[]::new), new Integer[] { 0, 1, 1, 1, 1, 0 });
  }

  @Test
  @Override
  public void testAndThen() {
    final BiFunction<String, String, String[]> splitter = ThrowableBiFunction.of((t, u) -> t.split(Objects.toString(u, "")), (t, u, e) -> "諸行無常".split(""));
    final BiFunction<String, String, String> joiner = splitter.andThen((t) -> String.join("@", t));
    assertEquals("南@無@阿@弥@陀@仏", joiner.apply("南無阿弥陀仏", null));
    assertEquals("諸@行@無@常", joiner.apply(null, null));
    BiFunction<Integer, Integer, String> vanilla = ((BiFunction<Integer, Integer, Integer>) (t, u) -> t + u).andThen((x) -> x.toString());
    BiFunction<Integer, Integer, String> throwable = ThrowableBiFunction.of((Integer t, Integer u) -> t + u, (Integer t, Integer u, Throwable e) -> Integer.valueOf(Objects.toString(t, "0"))).andThen(Objects::toString);
    assertEquals("10", vanilla.apply(5, 5));
    assertEquals("10", throwable.apply(5, 5));
    String actual = null;
    try {
      actual = vanilla.apply(null, 5);
      fail("no one execute this line .");
    } catch (Exception ex) {
      actual = throwable.apply(null, 5);
    }
    assertEquals("0", actual);
  }

  @Test
  @Override
  public void testApplyOrElse() {
    try {
      ThrowableBiFunction.applyOrDefault((String) null, (Integer) null, (t, u) -> t.codePoints().findFirst().orElse(u), (Integer) null);
      fail("no one execute this line .");
    } catch (Exception ex) {
      assertTrue(ex instanceof NullPointerException);
    }
    assertEquals("仏".codePointAt(0), ThrowableBiFunction.applyOrDefault("仏", -1, (t, u) -> t.codePoints().findFirst().orElse(u), -99));
    assertEquals(-99, ThrowableBiFunction.applyOrDefault((String) null, -1, (t, u) -> t.codePoints().findFirst().orElse(u), -99));
    assertEquals(-1, ThrowableBiFunction.applyOrDefault("", -1, (t, u) -> t.codePoints().findFirst().orElse(u), -99));
    assertEquals(-99, ThrowableBiFunction.applyOrDefault("南無阿弥陀仏", -1, (t, u) -> new HashMap<String, Integer>().get(t), -99));
  }

  @Test
  @Override
  public void testOf() {
    assertEquals((String[]) null, ThrowableBiFunction.of((t, u) -> t.split(u), (BiFunction<String, String, String[]>) null).apply(null, ""));
    assertEquals((String[]) null, ThrowableBiFunction.of((t, u) -> t.split(u), (TriFunction<String, String, Exception, String[]>) null).apply(null, ""));
    assertEquals("諸行無常", ThrowableBiFunction.of((String t, String u) -> t.split(u), (t, u) -> "諸行無常".split("")).andThen((t) -> String.join("", t)).apply(null, ""));
    assertEquals("南無阿弥陀仏", ThrowableBiFunction.of((String t, String u) -> t.split(u), (t, u) -> "諸行無常".split(u)).andThen((t) -> String.join("", t)).apply("南無阿弥陀仏", ""));
    assertEquals("NullPointerException", ThrowableBiFunction.of((String t, String u) -> t.split(u), (t, u, e) -> e.getClass().getSimpleName().split("")).andThen((t) -> String.join("", t)).apply(null, null));
    assertEquals("南無阿弥陀仏", ThrowableBiFunction.of((String t, String u) -> t.split(u), (t, u, e) -> e.getClass().getSimpleName().split("")).andThen((t) -> String.join("", t)).apply("南@無@阿@弥@陀@仏", "@"));
  }

  @Test
  @Override
  public void testOrDefault() {
    assertEquals(
      ThrowableBiFunction.orDefault(anArray, "0", (t, u) -> Arrays.asList(t).stream().mapToInt((x) -> Integer.valueOf(Objects.toString(x, u))).sum(), 0)
    , ThrowableBiFunction.orElseGet(anArray, "0", (t, u) -> Arrays.asList(t).stream().mapToInt((x) -> Integer.valueOf(Objects.toString(x, u))).sum(), () -> 0)
    );
    assertEquals(
      ThrowableBiFunction.orDefault(anArray, "0", (t, u) -> Arrays.asList(t).stream().mapToInt((x) -> Integer.valueOf(Objects.toString(x, u))).sum(), null)
    , (Integer) ThrowableBiFunction.orElseGet(anArray, "0", (t, u) -> Arrays.asList(t).stream().mapToInt((x) -> Integer.valueOf(Objects.toString(x, u))).sum(), () -> null)
    );
    assertArrayEquals(Arrays.stream(anArray).map((t) -> ThrowableBiFunction.orDefault(t, t, (x, y) -> x / y, 3)).toArray(Integer[]::new), new Integer[] {3, 1, 1, 1, 1, 3});
  }

  @Test
  @Override
  public void testOrElse() {
    assertEquals((String[]) null, ThrowableBiFunction.orElse(null, "", (t, u) -> t.split(u), (BiFunction<String, String, String[]>) null));
    assertEquals((String[]) null, ThrowableBiFunction.orElse(null, "", (t, u) -> t.split(u), (TriFunction<String, String, Exception, String[]>) null));
    assertEquals("諸行無常", ThrowableBiFunction.orElse((String) null, "", (t, u) -> String.join(u, t.split(u)), (t, u) -> "諸行無常"));
    assertEquals("南無阿弥陀仏", ThrowableBiFunction.orElse("南@無@阿@弥@陀@仏", "@", (t, u) -> String.join("", t.split(u)), (t, u) -> "諸行無常"));
    assertEquals("NullPointerException", ThrowableBiFunction.orElse((String) null, (String) null, (t, u) -> String.join("", t.split(u)), (t, u, e) -> e.getClass().getSimpleName()));
    assertEquals("南無阿弥陀仏", ThrowableBiFunction.orElse("南@無@阿@弥@陀@仏", "@", (t, u) -> String.join("", t.split(u)), (t, u, e) -> e.getClass().getSimpleName()));
  }

  @Test
  @Override
  public void testOrElseGet() {
    assertEquals((String[]) null, ThrowableBiFunction.orElseGet((String) null, "", (t, u) -> t.split(u), (Supplier<String[]>) null));
    assertEquals((String[]) null, ThrowableBiFunction.orElseGet((String) null, "", (t, u) -> t.split(u), () -> null));
    assertEquals("諸行無常", ThrowableBiFunction.orElseGet((String) null, "", (t, u) -> String.join(u, t.split(u)), () -> "諸行無常"));
    assertEquals("南無阿弥陀仏", ThrowableBiFunction.orElseGet("南@無@阿@弥@陀@仏", "@", (t, u) -> String.join("", t.split(u)), () -> "諸行無常"));
    assertEquals(
      ThrowableBiFunction.orElseGet(anArray, 0, (t, u) -> Arrays.asList(t).stream().mapToInt((x) -> Integer.valueOf(Objects.toString(x, u.toString()))).sum(), null)
    , (Integer) ThrowableBiFunction.orElseGet(anArray, 0, (t, u) -> Arrays.asList(t).stream().mapToInt((x) -> Integer.valueOf(Objects.toString(x, u.toString()))).sum(), () -> null)
    );
  }

  @Test
  @Override
  public void testOrNull() {
    assertArrayEquals(new Integer[] {null, 1, 1, 1, 1, null}, Arrays.stream(anArray).map((t) -> ThrowableBiFunction.orNull(t, t, (x, y) -> x / y)).toArray(Integer[]::new));
  }

}
