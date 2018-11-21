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
import java.util.function.Consumer;
import java.util.stream.IntStream;

import org.junit.Test;

public class ThrowableConsumerTest {

  @Test
  public void test() {
    try {
      new ThrowableConsumer<Integer>() {

        @Override
        public void acceptOrThrow(Integer t) throws Throwable {
          t.intValue();
        }}.accept(null);;
    } catch (Exception ex) {
      assertThat(ex instanceof NullPointerException, is(true));
    }
  }

  @Test
  public void testOf() {
    List<Integer> actuals = new ArrayList<>();
    Consumer<Integer> consumer = ThrowableConsumer.of((t) -> actuals.add(t / t), ThrowableConsumer.of((t) -> actuals.add(t - t), (t) -> actuals.add(-1)));
    Arrays.stream(new Integer[] {0, 1, 2, null, 4}).forEach(consumer);
    assertThat(actuals.toString(), is("[0, 1, 1, -1, 1]"));
    actuals.clear();
    consumer = ThrowableConsumer.of((t) -> actuals.add(t / t), ThrowableConsumer.of((t) -> actuals.add(t - t), (Consumer<Integer>) null));
    Arrays.stream(new Integer[] {0, 1, 2, null, 4}).forEach(consumer);
    assertThat(actuals.toString(), is("[0, 1, 1, 1]"));
  }

  @Test
  public void testOrElse() {
    List<Integer> actuals = new ArrayList<>();
    IntStream.rangeClosed(0, 2).boxed().forEach((x) -> ThrowableConsumer.orElse(x, (x0) -> actuals.add(x0 / x0), (Consumer<Integer>) null));
    assertThat(actuals.toString(), is("[1, 1]"));
    actuals.clear();
    IntStream.rangeClosed(0, 2).boxed().forEach((x) -> ThrowableConsumer.orElse(x, (x0) -> actuals.add(x0 / x0), (BiConsumer<Integer, Exception>) null));
    assertThat(actuals.toString(), is("[1, 1]"));
    actuals.clear();
    IntStream.rangeClosed(0, 2).boxed().forEach((x) -> ThrowableConsumer.orElse(x, (x0) -> actuals.add(x0 / x0), (x0) -> actuals.add( x0 - x0)));
    assertThat(actuals.toString(), is("[0, 1, 1]"));
    actuals.clear();
    Arrays.stream(new Integer[] {0, 1, 2, null, 4}).forEach((x) -> ThrowableConsumer.orElse(x, (x0) -> actuals.add(x0 / x0), (x0, ex0) -> actuals.add(ex0 instanceof ArithmeticException ? 0 : -1)));
    assertThat(actuals.toString(), is("[0, 1, 1, -1, 1]"));
    actuals.clear();
  }

  @Test
  public void testOrNot() {
    List<Integer> actuals = new ArrayList<>();
    IntStream.rangeClosed(0, 2).boxed().forEach((x) -> ThrowableConsumer.orNot(x, (x0) -> actuals.add(x0 - x0)));
    assertThat(actuals.toString(), is("[0, 0, 0]"));
    actuals.clear();
    IntStream.rangeClosed(0, 2).boxed().forEach((x) -> ThrowableConsumer.orNot(x, (x0) -> actuals.add(x0 / x0)));
    assertThat(actuals.toString(), is("[1, 1]"));
  }

  @Test
  public void testAndThen() {
    List<Integer> actuals = new ArrayList<>();
    Consumer<Integer> consumer = ThrowableConsumer.of((t) -> actuals.add(t / t), ThrowableConsumer.of((t) -> actuals.add(t - t), (t) -> actuals.add(-1)));
    consumer = consumer.andThen(ThrowableConsumer.of((x) -> actuals.add(x + x), (x) -> actuals.add(0)));
    Arrays.stream(new Integer[] {0, 1, 2, null, 4}).forEach(consumer);
    assertThat(actuals.toString(), is("[0, 0, 1, 2, 1, 4, -1, 0, 1, 8]"));
  }
}
