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
 * Formatter interface capable of transforming data to and from a string used in text records
 *
 * @author Jacob von Eyben www.ancientprogramming.com
 * @since 1.0.0
 */
public interface FixedFormatter<T> {

  T parse(String value, FormatInstructions instructions) throws FixedFormatException;

  String format(T value, FormatInstructions instructions) throws FixedFormatException;
}
