/*
 * Copyright 2004 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ancientprogramming.fixedformat4j.record;

/**
 * Interface used to interact with fixed format annotations.
 *
 * A <code>FixedFormatManager</code> is associated with one type of fixed format data.
 * @author Jacob von Eyben www.ancientprogramming.com
 * @since 1.0.0
 */
public interface FixedFormatManager {

  /**
   * Create a fixed format record from string data
   * @param fixedFormatRecord the class
   * @param data the data to load
   * @return an object loaded with the fixedformat data
   */
  <T> T load(Class<T> fixedFormatRecord, String data);

  <T> String export(T fixedFormatRecord);

  <T> String export(String existingData, T fixedFormatRecord);


}
