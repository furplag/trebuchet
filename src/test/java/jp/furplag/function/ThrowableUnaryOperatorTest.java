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

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.function.UnaryOperator;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

public class ThrowableUnaryOperatorTest {

  @Test
  public void test() {
    ThrowableUnaryOperator<String> one = (t) -> t.substring(0, 1);
    assertEquals("諸行無常", ThrowableFunction.of(one, (t, e) -> "諸行無常").apply((String) null));
    assertEquals("諸", ThrowableFunction.of(one, (t, e) -> "諸行無常").apply("諸行無常"));
  }

  @Test
  public void testIdentity() {
    IntStream.rangeClosed(0, 9999).forEach((i) -> {
      assertEquals(i, UnaryOperator.identity().apply(i));
      assertEquals(i, ThrowableUnaryOperator.identity().apply(i));
      assertEquals(UnaryOperator.identity().apply(i), ThrowableUnaryOperator.identity().apply(i));
      assertEquals(UnaryOperator.identity().apply((String) null), ThrowableUnaryOperator.identity().apply((String) null));
    });
  }

}
