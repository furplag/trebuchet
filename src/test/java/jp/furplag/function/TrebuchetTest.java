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

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import org.junit.Test;

import jp.furplag.function.Trebuchet.TrinaryOperator;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

public class TrebuchetTest {

  @Test
  public void test() {
    assertTrue(new Trebuchet() {} instanceof Trebuchet);
    assertTrue(Trebuchet.class.isAssignableFrom(new Trebuchet() {}.getClass()));
  }

  @Test
  public void testSneakyThrow() throws Throwable {
    MethodHandle sneakyThrow = MethodHandles.privateLookupIn(Trebuchet.class, MethodHandles.lookup()).findStatic(Trebuchet.class, "sneakyThrow", MethodType.methodType(void.class, Throwable.class));
    try {
      sneakyThrow.invoke(null);
    } catch (Throwable ex) {
      assertTrue(ex instanceof IllegalArgumentException);
    }
    try {
      sneakyThrow.invoke(new NullPointerException());
    } catch (Throwable ex) {
      assertTrue(ex instanceof NullPointerException);
    }

    final Throwable[] throwable = { null };
    // @formatter:off
    ThrowableConsumer.orElse("Test", (x) -> {x.toLowerCase();}, (x, ex) -> throwable[0] = ex);
    assertNull(throwable[0]);
    ThrowableConsumer.orElse((String) null, (x) -> {x.toLowerCase();}, (x, ex) -> throwable[0] = ex);
    assertTrue(throwable[0] instanceof NullPointerException);
    ThrowableConsumer.orElse(0, (x) -> {@SuppressWarnings("unused") int test = x / x;}, (x, ex) -> throwable[0] = ex);
    assertTrue(throwable[0] instanceof ArithmeticException);
    // @formatter:on
  }

  @Test
  public void testTriConsumer() {
    List<Integer> actuals = new ArrayList<>();
    Trebuchet.TriConsumer<Integer, Integer, Integer> consumer = (x, y, z) ->  actuals.add(x + y + z);
    IntStream.rangeClosed(0, 9).forEach((x) -> consumer.accept(x, x + 1, x + 2));
    assertThat(actuals.toString(), is("[3, 6, 9, 12, 15, 18, 21, 24, 27, 30]"));
    actuals.clear();
    IntStream.rangeClosed(0, 9).forEach((x) -> consumer.andThen((a, b, c) -> actuals.add(a + b + c)).accept(x, x + 1, x + 2));
    assertThat(actuals.toString(), is("[3, 3, 6, 6, 9, 9, 12, 12, 15, 15, 18, 18, 21, 21, 24, 24, 27, 27, 30, 30]"));
  }

  @Test
  public void testTriFunction() {
    @AllArgsConstructor
    @EqualsAndHashCode
    @ToString
    class A {
      private final int a;
      private final String b;
      private final String c;
    }
    @AllArgsConstructor
    @EqualsAndHashCode
    @ToString
    class B {
      private final A a;
    }
    assertThat(((Trebuchet.TriFunction<Integer, String, String, A>) (x, y, z) -> new A(x, y, z)).apply(0, "南無阿弥陀仏".substring(0, 1), "南無阿弥陀仏".substring(1)), is(new A(0, "南", "無阿弥陀仏")));
    assertThat(((Trebuchet.TriFunction<Integer, String, String, A>) (x, y, z) -> new A(x, y, z)).andThen(B::new).apply(0, "南無阿弥陀仏".substring(0, 1), "南無阿弥陀仏".substring(1)), is(new B(new A(0, "南", "無阿弥陀仏"))));
  }

  @Test
  public void testTriPredicate() {
    assertThat(((Trebuchet.TriPredicate<Integer, Integer, Integer>) (x, y, z) -> (x + y + z) % 2 == 0).test(0, 1, 2), is(false));
    assertThat(((Trebuchet.TriPredicate<Integer, Integer, Integer>) (x, y, z) -> (x + y + z) % 2 == 0).test(1, 2, 3), is(true));
    assertThat(((Trebuchet.TriPredicate<Integer, Integer, Integer>) (x, y, z) -> (x + y + z) % 2 == 0).negate().test(0, 1, 2), is(true));
    assertThat(((Trebuchet.TriPredicate<Integer, Integer, Integer>) (x, y, z) -> (x + y + z) % 2 == 0).negate().negate().test(1, 2, 3), is(true));
    assertThat(((Trebuchet.TriPredicate<Integer, Integer, Integer>) (x, y, z) -> (x + y + z) % 2 == 0).and((a, b, c) -> (a + b + c) % 3 == 0).test(2, 3, 4), is(false));
    assertThat(((Trebuchet.TriPredicate<Integer, Integer, Integer>) (x, y, z) -> (x + y + z) % 2 == 0).and((a, b, c) -> (a + b + c) % 3 == 0).test(3, 3, 4), is(false));
    assertThat(((Trebuchet.TriPredicate<Integer, Integer, Integer>) (x, y, z) -> (x + y + z) % 2 == 0).and((a, b, c) -> (a + b + c) % 3 == 0).test(3, 4, 5), is(true));
    assertThat(((Trebuchet.TriPredicate<Integer, Integer, Integer>) (x, y, z) -> (x + y + z) % 2 == 0).or((a, b, c) -> (a + b + c) % 3 == 0).test(1, 2, 2), is(false));
    assertThat(((Trebuchet.TriPredicate<Integer, Integer, Integer>) (x, y, z) -> (x + y + z) % 2 == 0).or((a, b, c) -> (a + b + c) % 3 == 0).test(0, 1, 2), is(true));
    assertThat(((Trebuchet.TriPredicate<Integer, Integer, Integer>) (x, y, z) -> (x + y + z) % 2 == 0).or((a, b, c) -> (a + b + c) % 3 == 0).test(1, 2, 3), is(true));
  }

  @Test
  public void testTrinaryOperator() {

    @ToString
    @EqualsAndHashCode
    class A implements Comparable<A> {
      @Override
      public int compareTo(A o) {
        return this.toString().compareTo(Objects.toString(o, ""));
      }
    }

    @ToString(callSuper = true)
    @EqualsAndHashCode(callSuper = true)
    class B extends A {}

    @ToString(callSuper = true)
    @EqualsAndHashCode(callSuper = true)
    class C extends B {}

    assertThat(TrinaryOperator.minBy(A::compareTo).apply(new A(), new B(), new C()), is(new A()));
    assertThat(TrinaryOperator.minBy(A::compareTo).apply(new C(), new B(), new A()), is(new A()));
    assertThat(TrinaryOperator.minBy(A::compareTo).apply(new C(), new C(), new B()), is(new B()));
    assertThat(TrinaryOperator.maxBy(A::compareTo).apply(new A(), new B(), new C()), is(new C()));
    assertThat(TrinaryOperator.maxBy(A::compareTo).apply(new C(), new B(), new A()), is(new C()));
    assertThat(TrinaryOperator.maxBy(A::compareTo).apply(new B(), new B(), new C()), is(new C()));
    assertThat(TrinaryOperator.maxBy(A::compareTo).apply(new A(), null, null), is(new A()));
    assertThat(TrinaryOperator.maxBy(A::compareTo).apply((C) null, null, null), is((A) null));
    assertThat(TrinaryOperator.minBy(A::compareTo).apply(new A(), null, null), is(new A()));
    assertThat(TrinaryOperator.minBy(A::compareTo).apply((C) null, null, null), is((A) null));
  }
}
