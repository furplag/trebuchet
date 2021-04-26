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
import java.util.Comparator;
import org.junit.jupiter.api.Test;
import jp.furplag.function.Trebuchet.TriFunction;

public class ThrowableTrinaryOperatorTest {

  @Test
  public void test() {
    TriFunction<Integer, Integer, Integer, Integer> minBy = ThrowableTrinaryOperator.minBy(Comparator.naturalOrder());
    TriFunction<Integer, Integer, Integer, Integer> maxBy = ThrowableTrinaryOperator.maxBy(Comparator.naturalOrder());
    assertEquals(1, minBy.apply(1, 2, 3));
    assertEquals(3, maxBy.apply(1, 2, 3));
    TriFunction<Integer, Integer, Integer, Integer> minByR = ThrowableTrinaryOperator.minBy(Comparator.reverseOrder());
    TriFunction<Integer, Integer, Integer, Integer> maxByR = ThrowableTrinaryOperator.maxBy(Comparator.reverseOrder());
    assertEquals(3, minByR.apply(1, 2, 3));
    assertEquals(1, maxByR.apply(1, 2, 3));
  }
}
