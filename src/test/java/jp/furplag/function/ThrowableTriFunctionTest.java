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
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import org.junit.jupiter.api.Test;
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
      assertTrue(ex instanceof ArithmeticException);
      assertEquals(1, function.apply(1, 1, 2));
    }
    try {
      ThrowableTriFunction.orElse(0, 0, 0, (t, u, v) -> (t + u) / v, (e) -> {Trebuchet.sneakyThrow(e); return null;});
      fail("there must raise ArithmeticException .");
    } catch (Exception ex) {
      assertTrue(ex instanceof ArithmeticException);
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
    assertEquals("6", vanilla.apply(1, 2, 3));
    assertEquals("6", throwable.apply(1, 2, 3));
    String actual = null;
    try {
      actual = vanilla.apply(null, null, null);
      fail("no one execute this line .");
    } catch (Exception ex) {
      actual = throwable.apply(null, null, null);
    }
    assertEquals("0", actual);
  }

  @Test
  @Override
  public void testApplyOrElse() {
    try {
      ThrowableTriFunction.applyOrDefault(null, null, null, (String t, Integer u, Integer v) -> t.substring(u, v), (String) null);
      fail("no one execute this line .");
    } catch (Exception ex) {
      assertTrue(ex instanceof NullPointerException);
    }
    assertEquals("滅", ThrowableTriFunction.applyOrDefault(null, 0, 1, (String t, Integer u, Integer v) -> t.substring(u, v), "滅"));
    assertEquals("南", ThrowableTriFunction.applyOrDefault("南無阿弥陀仏", 0, 1, (String t, Integer u, Integer v) -> t.substring(u, v), "滅"));
  }

  @Test
  @Override
  public void testOf() {
    assertEquals((String) null, ThrowableTriFunction.of((t, u, v) -> String.join("", t.substring(u, v)), (TriFunction<String, Integer, Integer, String>) null).apply(null, null, null));
    assertEquals((String) null, ThrowableTriFunction.of((String t, Integer u, Integer v) -> t.substring(u, v), (Function<Exception, String>) null).apply(null, null, null));
    assertEquals("諸行無常", ThrowableTriFunction.of((String t, Integer u, Integer v) -> t.substring(u, v), (t, u, v) -> "諸行無常").apply(null, null, null));
    assertEquals("諸行無常", ThrowableTriFunction.of((String t, Integer u, Integer v) -> t.substring(u, v), (t, u, v) -> "諸行無常").apply(null, 0, 2));
    assertEquals("南無", ThrowableTriFunction.of((String t, Integer u, Integer v) -> t.substring(u, v), (t, u, v) -> "諸行無常").apply("南無阿弥陀仏", 0, 2));
    assertEquals("NullPointerException", ThrowableTriFunction.of((String t, Integer u, Integer v) -> t.substring(u, v), (e) -> e.getClass().getSimpleName()).apply(null, null, null));
    assertEquals("StringIndexOutOfBoundsException", ThrowableTriFunction.of((String t, Integer u, Integer v) -> t.substring(u, v), (e) -> e.getClass().getSimpleName()).apply("南無阿弥陀仏", 0, 999));
    assertEquals("南無阿弥陀仏", ThrowableTriFunction.of((String t, Integer u, Integer v) -> t.substring(u, v), (e) -> e.getClass().getSimpleName()).apply("南無阿弥陀仏", 0, 6));
  }

  @Test
  @Override
  public void testOrDefault() {
    assertEquals((String) null, ThrowableTriFunction.orDefault(null, null, null, (String t, Integer u, Integer v) -> t.substring(u, v), null));
    assertEquals((String) null, ThrowableTriFunction.orDefault(null, 0, 2, (String t, Integer u, Integer v) -> t.substring(u, v), null));
    assertEquals("諸行無常", ThrowableTriFunction.orDefault(null, 0, 2, (String t, Integer u, Integer v) -> t.substring(u, v), "諸行無常"));
    assertEquals("南無", ThrowableTriFunction.orDefault("南無阿弥陀仏", 0, 2, (t, u, v) -> t.substring(u, v), "諸行無常"));
  }

  @Test
  @Override
  public void testOrElse() {
    assertEquals((String) null, ThrowableTriFunction.orElse(null, null, null, (t, u, v) -> t.substring(u, v), (TriFunction<String, Integer, Integer, String>) null));
    assertEquals((String) null, ThrowableTriFunction.orElse(null, null, null, (String t, Integer u, Integer v) -> t.substring(u, v), (Function<Exception, String>) null));
    assertEquals("諸行無常", ThrowableTriFunction.orElse(null, null, null, (String t, Integer u, Integer v) -> t.substring(u, v), (t, u, v) -> "諸行無常"));
    assertEquals("諸行無常", ThrowableTriFunction.orElse(null, 0, 2, (String t, Integer u, Integer v) -> t.substring(u, v), (t, u, v) -> "諸行無常"));
    assertEquals("南無", ThrowableTriFunction.orElse("南無阿弥陀仏", 0, 2, (t, u, v) -> t.substring(u, v), (t, u, v) -> "諸行無常"));
    assertEquals("NullPointerException", ThrowableTriFunction.orElse(null, null, null, (String t, Integer u, Integer v) -> t.substring(u, v), (e) -> e.getClass().getSimpleName()));
    assertEquals("StringIndexOutOfBoundsException", ThrowableTriFunction.orElse("南無阿弥陀仏", 0, 999, (String t, Integer u, Integer v) -> t.substring(u, v), (e) -> e.getClass().getSimpleName()));
    assertEquals("南無阿弥陀仏", ThrowableTriFunction.orElse("南無阿弥陀仏", 0, 6, (String t, Integer u, Integer v) -> t.substring(u, v), (e) -> e.getClass().getSimpleName()));
  }

  @Test
  @Override
  public void testOrElseGet() {
    assertEquals((String) null, ThrowableTriFunction.orElseGet(null, null, null, (String t, Integer u, Integer v) -> t.substring(u, v), (Supplier<String>) null));
    assertEquals("諸行無常", ThrowableTriFunction.orElseGet(null, null, null, (String t, Integer u, Integer v) -> t.substring(u, v), () -> "諸行無常"));
    assertEquals("諸行無常", ThrowableTriFunction.orElseGet(null, 0, 2, (String t, Integer u, Integer v) -> t.substring(u, v), () -> "諸行無常"));
    assertEquals("南無", ThrowableTriFunction.orElseGet("南無阿弥陀仏", 0, 2, (t, u, v) -> t.substring(u, v), () -> "諸行無常"));
  }

  @Test
  @Override
  public void testOrNull() {
    assertEquals((String) null, ThrowableTriFunction.orNull(null, null, null, (String t, Integer u, Integer v) -> t.substring(u, v)));
    assertEquals((String) null, ThrowableTriFunction.orNull(null, 0, 2, (String t, Integer u, Integer v) -> t.substring(u, v)));
    assertEquals("南無", ThrowableTriFunction.orNull("南無阿弥陀仏", 0, 2, (t, u, v) -> t.substring(u, v)));
  }

}
