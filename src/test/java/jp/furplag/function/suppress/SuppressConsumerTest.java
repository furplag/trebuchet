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

package jp.furplag.function.suppress;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import jp.furplag.function.Trebuchet;

public class SuppressConsumerTest {

  @Test
  public void test() {
    assertTrue(new SuppressConsumer() {} instanceof SuppressConsumer);
    assertTrue(SuppressConsumer.class.isAssignableFrom(new SuppressConsumer() {}.getClass()));
  }

  @Test
  public void testAcceptWith() {
    final Set<Integer> expect = new HashSet<>();
    final Set<Integer> actual = new HashSet<>();
    Arrays.stream(new Integer[]{0, 1, 2, 3, 4, null}).forEach(i -> Trebuchet.orElse((Integer x) -> expect.add(x * 2), (ex, e) -> {}).accept(i));
    Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).forEach(i -> SuppressConsumer.acceptWith(i, (x) -> actual.add(x * 2)));
    assertArrayEquals(expect.stream().sorted(Comparator.naturalOrder()).toArray(Integer[]::new), actual.stream().sorted(Comparator.naturalOrder()).toArray(Integer[]::new));
  }

  @Test
  public void testAcceptWitBi() {
    final Set<Integer> expect = new HashSet<>();
    final Set<Integer> actual = new HashSet<>();
    Arrays.stream(new Integer[]{0, 1, 2, 3, 4, null}).forEach(i -> Trebuchet.orElse((Integer x, Set<Integer> y) -> y.add(x * 2), (ex, e) -> {}).accept(i, expect));
    Arrays.stream(new Integer[] { 0, 1, 2, 3, 4, null }).forEach(i -> SuppressConsumer.acceptWith(i, actual, (x, y) -> y.add(x * 2)));
    assertArrayEquals(expect.stream().sorted(Comparator.naturalOrder()).toArray(Integer[]::new), actual.stream().sorted(Comparator.naturalOrder()).toArray(Integer[]::new));
  }

}
