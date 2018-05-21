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

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import jp.furplag.function.Trebuchet;

/**
 * suppressing to raise any exceptions in lambda expression .
 *
 * @author furplag
 *
 */
public interface SuppressConsumer {

  /**
   * mute out any exceptions whether the function throws it .
   *
   * @param <T> the type of the input to the function
   * @param t the value of the input to the function
   * @param functional {@link Consumer}
   * @see jp.furplag.function.Trebuchet.ThrowableConsumer
   */
  static <T> void acceptWith(final T t, final Consumer<T> functional) {
    Trebuchet.orElse(functional, (ex, x) -> /* @formatter:off */{/* do nothing . */}/* @formatter:on */).accept(t);
  }

  /**
   * mute out any exceptions whether the function throws it .
   *
   * @param <T> the type of the first argument to the function
   * @param <U> the type of the second argument to the function
   * @param t the first function argument
   * @param u the second function argument
   * @param functional {@link BiConsumer}
   * @see jp.furplag.function.Trebuchet.ThrowableBiConsumer
   */
  static <T, U> void acceptWith(final T t, final U u, final BiConsumer<T, U> functional) {
    Trebuchet.orElse(functional, (ex, x) -> /* @formatter:off */{/* do nothing . */}/* @formatter:on */).accept(t, u);
  }
}
