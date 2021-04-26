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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.junit.jupiter.api.Test;
import jp.furplag.function.Trebuchet.TriConsumer;
import jp.furplag.function.misc.ConsumerTest;

public class ThrowableTriConsumerTest implements ConsumerTest {

  @Test
  public void test() {
    try {
      ThrowableTriConsumer.orElse(Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), (t, u, v) -> System.out.println(t / u + (v - v)), (e) -> Trebuchet.sneakyThrow(e));
      fail("there must raise ArithmeticException .");
    } catch (Exception ex) {
      assertTrue(ex instanceof ArithmeticException);
    }
    final int[] divided = { -1 };
    final TriConsumer<Integer, Integer, Integer> divider = ThrowableTriConsumer.of((t, u, v) -> divided[0] = (t / u + (v - v)), (t, u, v) -> divided[0] = Integer.valueOf(Objects.toString(t, "0")));
    Arrays.stream(anArray).forEach((t) -> {
      divider.accept(t, t, t);
      assertEquals(t == null || t == 0 ? 0 : 1, divided[0]);
    });
  }

  @Test
  public void testAndThen() {
    final int[] divided = { -1 };
    final String[] actual = { "" };
    TriConsumer<Integer, Integer, Integer> divider = ThrowableTriConsumer.of((t, u, v) -> divided[0] = (t / u + (v - v)), (t, u, v) -> divided[0] = Integer.valueOf(Objects.toString(t, "0")));
    final TriConsumer<Integer, Integer, Integer> consumer = divider.andThen((t, u, v) -> actual[0] = Objects.toString(t));
    Arrays.stream(anArray).forEach((t) -> {
      consumer.accept(t, t, t);
      assertEquals(t == null || t == 0 ? 0 : 1, divided[0]);
      assertEquals(Objects.toString(t), actual[0]);
    });
  }

  @Test
  public void testOf() {
    List<Integer> result = new ArrayList<>();
    Arrays.stream(anArray).forEach((i) -> ThrowableTriConsumer.of((t, u, v) -> result.add(t / u + (v - v)), ThrowableTriConsumer.of((t, u, v) -> result.add(t - u + (v - v)), (TriConsumer<Integer, Integer, Integer>) null)).accept(i, i, i));
    assertArrayEquals(new Integer[] {0, 1, 1, 1, 1}, result.toArray(Integer[]::new));

    result.clear();
    Arrays.stream(anArray).forEach((i) -> ThrowableTriConsumer.of((t, u, v) -> result.add(t / u + (v - v)), ThrowableTriConsumer.of((t, u, v) -> result.add(t - u + (v - v)), (TriConsumer<Integer, Integer, Integer>) null)).accept(i, i, i));
    assertArrayEquals(new Integer[] {0, 1, 1, 1, 1}, result.toArray(Integer[]::new));

    result.clear();
    Arrays.stream(anArray).forEach((i) -> ThrowableTriConsumer.of((Integer t, Integer u, Integer v) -> result.add(t / u + (v - v)), ThrowableTriConsumer.of((Integer t, Integer u, Integer v) -> result.add(t - u + (v - v)), (t, u, v) -> result.add(Integer.valueOf(Objects.toString(t, "-1"))))).accept(i, i, i));
    assertArrayEquals(new Integer[] {0, 1, 1, 1, 1, -1}, result.toArray(Integer[]::new));

    result.clear();
    Map<Integer, Class<?>> errors = new HashMap<>();
    Arrays.stream(anArray).forEach((i) -> ThrowableTriConsumer.of((Integer t, Integer u, Integer v) -> result.add(t / u + (v - v)), (e) -> errors.put(errors.size(), e.getClass())).accept(i, i, i));
    assertArrayEquals(new Integer[] {1, 1, 1, 1}, result.toArray(Integer[]::new));
    assertArrayEquals(new Class<?>[] {ArithmeticException.class, NullPointerException.class}, errors.entrySet().stream().sorted(Comparator.comparing(Map.Entry::getKey)).map(Map.Entry::getValue).toArray(Class<?>[]::new));
  }

  @Test
  public void testOrElse() {
    List<Integer> result = new ArrayList<>();
    Arrays.stream(anArray).forEach((i) -> ThrowableTriConsumer.orElse(i, i, i, (t, u, v) -> result.add(t / u + (v - v)), ThrowableTriConsumer.of((t, u, v) -> result.add(t - u + (v - v)), (TriConsumer<Integer, Integer, Integer>) null)));
    assertArrayEquals(new Integer[] {0, 1, 1, 1, 1}, result.toArray(Integer[]::new));

    result.clear();
    Arrays.stream(anArray).forEach((i) -> ThrowableTriConsumer.orElse(i, i, i, (t, u, v) -> result.add(t / u + (v - v)), ThrowableTriConsumer.of((t, u, v) -> result.add(t - t + (v - v)), (t, u, v) -> result.add(Integer.valueOf(Objects.toString(t, "-1"))))));
    assertArrayEquals(new Integer[] {0, 1, 1, 1, 1, -1}, result.toArray(Integer[]::new));

    result.clear();
    Map<Integer, Class<?>> errors = new HashMap<>();
    Arrays.stream(anArray).forEach((i) -> ThrowableTriConsumer.orElse(i, i, i, (t, u, v) -> result.add(t / u + (v - v)), (e) -> errors.put(Integer.valueOf(Objects.toString(errors.size(), "-1")), e.getClass())));
    assertArrayEquals(new Integer[] {1, 1, 1, 1}, result.toArray(Integer[]::new));
    assertArrayEquals(new Class<?>[] {ArithmeticException.class, NullPointerException.class}, errors.entrySet().stream().sorted(Comparator.comparing(Map.Entry::getKey)).map(Map.Entry::getValue).toArray(Class<?>[]::new));
  }

  @Test
  public void testOrNot() {
    List<Integer> expect = new ArrayList<>();
    List<Integer> actual = new ArrayList<>();
    Arrays.stream(new Integer[] {0, 1, 2, 3, 4, null}).forEach((i) -> ThrowableTriConsumer.orElse(i, i, i, (t, u, v) -> expect.add(t / u + (v - v)), ThrowableTriConsumer.of((t, u, v) -> expect.add(t - u), (TriConsumer<Integer, Integer, Integer>) null)));
    Arrays.stream(new Integer[] {0, 1, 2, 3, 4, null}).forEach((i) -> ThrowableTriConsumer.orElse(i, i, i, (t, u, v) -> actual.add(t / u + (v - v)), (t, u, v) -> ThrowableTriConsumer.orNot(t, u, v, (x, y, z) -> actual.add(x - y))));
    assertArrayEquals(expect.toArray(Integer[]::new), actual.toArray(Integer[]::new));
  }
}
