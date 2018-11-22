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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.IntStream;

import org.junit.Test;

import jp.furplag.function.Trebuchet.TriConsumer;

public class ThrowableBiConsumerTest {

  @Test
  public void test() {
    try {
      new ThrowableBiConsumer<Integer, Integer>() {

        @Override
        public void acceptOrThrow(Integer t, Integer u) throws Throwable {
          t.intValue();
        }}.accept(null, null);
    } catch (Exception ex) {
      assertThat(ex instanceof NullPointerException, is(true));
    }
  }

  @Test
  public void testOf() {
    List<Integer> actuals = new ArrayList<>();
    final BiConsumer<Integer, Integer> consumer1 = ThrowableBiConsumer.of((t, u) -> actuals.add(t / u), ThrowableBiConsumer.of((t, u) -> actuals.add(t - u), (t, u) -> actuals.add(-1)));
    Arrays.stream(new Integer[] {0, 1, 2, null, 4}).forEach((x) -> consumer1.accept(x, x));
    assertThat(actuals.toString(), is("[0, 1, 1, -1, 1]"));
    actuals.clear();
    final BiConsumer<Integer, Integer> consumer2 = ThrowableBiConsumer.of((t, u) -> actuals.add(t / u), ThrowableBiConsumer.of((t, u) -> actuals.add(t - u), (BiConsumer<Integer, Integer>) null));
    Arrays.stream(new Integer[] {0, 1, 2, null, 4}).forEach((x) -> consumer2.accept(x, x));
    assertThat(actuals.toString(), is("[0, 1, 1, 1]"));
  }

  @Test
  public void testOrElse() {
    List<Integer> actuals = new ArrayList<>();
    IntStream.rangeClosed(0, 2).boxed().forEach((x) -> ThrowableBiConsumer.orElse(x, x, (x0, x1) -> actuals.add(x0 / x1), (TriConsumer<Integer, Integer, Throwable>) null));
    assertThat(actuals.toString(), is("[1, 1]"));
    actuals.clear();
    IntStream.rangeClosed(0, 2).boxed().forEach((x) -> ThrowableBiConsumer.orElse(x, x, (x0, x1) -> actuals.add(x0 / x1), (BiConsumer<Integer, Integer>) null));
    assertThat(actuals.toString(), is("[1, 1]"));
    actuals.clear();
    IntStream.rangeClosed(0, 2).boxed().forEach((x) -> ThrowableBiConsumer.orElse(x, x, (x0, x1) -> actuals.add(x0 / x1), (x0, x1) -> actuals.add( x0 - x0)));
    assertThat(actuals.toString(), is("[0, 1, 1]"));
    actuals.clear();
    Arrays.stream(new Integer[] {0, 1, 2, null, 4}).forEach((x) -> ThrowableBiConsumer.orElse(x, x, (x0, x1) -> actuals.add(x0 / x1), (x0, x1, ex0) -> actuals.add(ex0 instanceof ArithmeticException ? 0 : -1)));
    assertThat(actuals.toString(), is("[0, 1, 1, -1, 1]"));
    actuals.clear();
  }

  @Test
  public void testOrNot() {
    List<Integer> actuals = new ArrayList<>();
    IntStream.rangeClosed(0, 2).boxed().forEach((x) -> ThrowableBiConsumer.orNot(x, x, (x0, x1) -> actuals.add(x0 - x1)));
    assertThat(actuals.toString(), is("[0, 0, 0]"));
    actuals.clear();
    IntStream.rangeClosed(0, 2).boxed().forEach((x) -> ThrowableBiConsumer.orNot(x, x, (x0, x1) -> actuals.add(x0 / x1)));
    assertThat(actuals.toString(), is("[1, 1]"));
  }

  @Test
  public void testAndThen() {
    List<Integer> actuals = new ArrayList<>();
    final BiConsumer<Integer, Integer> consumer = ThrowableBiConsumer.of((t, u) -> actuals.add(t / u), ThrowableBiConsumer.of((t, u) -> actuals.add(t - u), (t, u) -> actuals.add(-1)));
    final BiConsumer<Integer, Integer> newConsumer = consumer.andThen(ThrowableBiConsumer.of((t, u) -> actuals.add(t + u), (t, u) -> actuals.add(0)));
    Arrays.stream(new Integer[] {0, 1, 2, null, 4}).forEach((x) -> newConsumer.accept(x, x));
    assertThat(actuals.toString(), is("[0, 0, 1, 2, 1, 4, -1, 0, 1, 8]"));
  }
}
