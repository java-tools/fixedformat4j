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
package com.ancientprogramming.fixedformat4j.format;

import com.ancientprogramming.fixedformat4j.exception.FixedFormatException;

/**
 * Interface used to interact with fixed format annotations.
 * <p/>
 * A <code>FixedFormatManager</code> is associated with one type of fixed format data.
 *
 * @author Jacob von Eyben - http://www.ancientprogramming.com
 * @since 1.0.0
 */
public interface FixedFormatManager {

  /**
   * Create an instance of the fixedFormatClass and load the data string into the object according to the annotations.
   *
   * @param clazz the class to instanciate
   * @param data  the data to load
   * @return an object loaded with the fixedformat data
   * @throws ParseException       in case that some specific parsing fails. Ex. a field could't be parsed according to some annotation instructions.
   *                              This exception contains detailed information telling what failed to be loaded.
   * @throws FixedFormatException in case the fixedFormatRecord class cannot be loaded. Ex. the Class wasn't annotated with a @Record annotation
   */
  <T> T load(Class<T> clazz, String data) throws FixedFormatException;

  <T> String export(T instance) throws FixedFormatException;

  <T> String export(String data, T instance) throws FixedFormatException;


}
