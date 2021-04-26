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
import java.util.function.Consumer;
import org.junit.jupiter.api.Test;
import jp.furplag.function.misc.ConsumerTest;

public class ThrowableConsumerTest implements ConsumerTest {

  @Test
  public void test() {
    try {
      ThrowableConsumer.orElse(Integer.valueOf(0), (t) -> System.out.println(t / t), (t, e) -> Trebuchet.sneakyThrow(e));
      fail("there must raise ArithmeticException .");
    } catch (Exception ex) {
      assertTrue(ex instanceof ArithmeticException);
    }
    final int[] divided = { -1 };
    final Consumer<Integer> divider = ThrowableConsumer.of((t) -> divided[0] = (t / t), (t, e) -> divided[0] = Integer.valueOf(Objects.toString(t, "0")));
    Arrays.stream(anArray).forEach((t) -> {
      divider.accept(t);
      assertEquals(t == null || t == 0 ? 0 : 1, divided[0]);
    });
  }

  @Test
  public void testAndThen() {
    final int[] divided = { -1 };
    final String[] actual = { "" };
    Consumer<Integer> divider = ThrowableConsumer.of((t) -> divided[0] = (t / t), (t, e) -> divided[0] = Integer.valueOf(Objects.toString(t, "0")));
    final Consumer<Integer> consumer = divider.andThen((t) -> actual[0] = Objects.toString(t));
    Arrays.stream(anArray).forEach((t) -> {
      consumer.accept(t);
      assertEquals(t == null || t == 0 ? 0 : 1, divided[0]);
      assertEquals(Objects.toString(t), actual[0]);
    });
  }

  @Test
  public void testOf() {
    List<Integer> result = new ArrayList<>();
    Arrays.stream(anArray).forEach((i) -> ThrowableConsumer.of((t) -> result.add(t / t), ThrowableConsumer.of((t) -> result.add(t - t), (Consumer<Integer>) null)).accept(i));
    assertArrayEquals(new Integer[] {0, 1, 1, 1, 1}, result.toArray(Integer[]::new));

    result.clear();
    Arrays.stream(anArray).forEach((i) -> ThrowableConsumer.of((Integer t) -> result.add(t / t), ThrowableConsumer.of((Integer t) -> result.add(t - t), (t) -> result.add(Integer.valueOf(Objects.toString(t, "-1"))))).accept(i));
    assertArrayEquals(new Integer[] {0, 1, 1, 1, 1, -1}, result.toArray(Integer[]::new));

    result.clear();
    Map<Integer, Class<?>> errors = new HashMap<>();
    Arrays.stream(anArray).forEach((i) -> ThrowableConsumer.of((Integer t) -> result.add(t / t), (t, e) -> errors.put(Integer.valueOf(Objects.toString(t, "-1")), e.getClass())).accept(i));
    assertArrayEquals(new Integer[] {1, 1, 1, 1}, result.toArray(Integer[]::new));
    assertArrayEquals(new Class<?>[] {NullPointerException.class, ArithmeticException.class}, errors.entrySet().stream().sorted(Comparator.comparing(Map.Entry::getKey)).map(Map.Entry::getValue).toArray(Class<?>[]::new));
  }

  @Test
  public void testOrElse() {
    List<Integer> result = new ArrayList<>();
    Arrays.stream(anArray).forEach((i) -> ThrowableConsumer.orElse(i, (t) -> result.add(t / t), ThrowableConsumer.of((t) -> result.add(t - t), (Consumer<Integer>) null)));
    assertArrayEquals(new Integer[] {0, 1, 1, 1, 1}, result.toArray(Integer[]::new));

    result.clear();
    Arrays.stream(anArray).forEach((i) -> ThrowableConsumer.orElse(i, (t) -> result.add(t / t), ThrowableConsumer.of((Integer t) -> result.add(t - t), (t) -> result.add(Integer.valueOf(Objects.toString(t, "-1"))))));
    assertArrayEquals(new Integer[] {0, 1, 1, 1, 1, -1}, result.toArray(Integer[]::new));

    result.clear();
    Map<Integer, Class<?>> errors = new HashMap<>();
    Arrays.stream(anArray).forEach((i) -> ThrowableConsumer.orElse(i, (t) -> result.add(t / t), (t, e) -> errors.put(Integer.valueOf(Objects.toString(t, "-1")), e.getClass())));
    assertArrayEquals(new Integer[] {1, 1, 1, 1}, result.toArray(Integer[]::new));
    assertArrayEquals(new Class<?>[] {NullPointerException.class, ArithmeticException.class}, errors.entrySet().stream().sorted(Comparator.comparing(Map.Entry::getKey)).map(Map.Entry::getValue).toArray(Class<?>[]::new));
  }

  @Test
  public void testOrNot() {
    List<Integer> expect = new ArrayList<>();
    List<Integer> actual = new ArrayList<>();
    Arrays.stream(anArray).forEach((i) -> ThrowableConsumer.orElse(i, (t) -> expect.add(t / t), ThrowableConsumer.of((t) -> expect.add(t - t), (Consumer<Integer>) null)));
    Arrays.stream(anArray).forEach((i) -> ThrowableConsumer.orElse(i, (t) -> actual.add(t / t), (t) -> ThrowableConsumer.orNot(t, (x) -> actual.add(x - x))));
    assertArrayEquals(expect.toArray(Integer[]::new), actual.toArray(Integer[]::new));
  }
}
