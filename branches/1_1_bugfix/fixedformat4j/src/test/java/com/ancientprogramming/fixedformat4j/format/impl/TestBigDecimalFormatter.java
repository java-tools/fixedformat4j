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
package com.ancientprogramming.fixedformat4j.format.impl;

import junit.framework.TestCase;
import com.ancientprogramming.fixedformat4j.format.FormatInstructions;
import com.ancientprogramming.fixedformat4j.format.data.FixedFormatDecimalData;
import com.ancientprogramming.fixedformat4j.format.data.FixedFormatNumberData;
import com.ancientprogramming.fixedformat4j.annotation.Align;
import com.ancientprogramming.fixedformat4j.annotation.Sign;

import java.math.BigDecimal;

/**
 * @author Jacob von Eyben www.ancientprogramming.com
 * @since 1.0.0
 */
public class TestBigDecimalFormatter extends TestCase {
  BigDecimalFormatter formatter = new BigDecimalFormatter();

  public void testParse() {
    assertEquals(new BigDecimal("100.50"), formatter.parse("+000010050", new FormatInstructions(10, Align.RIGHT, '0', null, null, new FixedFormatNumberData(Sign.PREPEND), new FixedFormatDecimalData(2, false, '.'))));
    assertEquals(new BigDecimal("1234.56"), formatter.parse("+000123456", new FormatInstructions(10, Align.RIGHT, '0', null, null, new FixedFormatNumberData(Sign.PREPEND), new FixedFormatDecimalData(2, false, '.'))));

    //use delimiter
    assertEquals(new BigDecimal("1234.56"), formatter.parse("+001234.56", new FormatInstructions(10, Align.RIGHT, '0', null, null, new FixedFormatNumberData(Sign.PREPEND), new FixedFormatDecimalData(2, true, '.'))));
    assertEquals(new BigDecimal("123.456"), formatter.parse("+00123_456", new FormatInstructions(10, Align.RIGHT, '0', null, null, new FixedFormatNumberData(Sign.PREPEND), new FixedFormatDecimalData(3, true, '_'))));

    //no decimals
    assertEquals(new BigDecimal("123456"), formatter.parse("+000123456", new FormatInstructions(10, Align.RIGHT, '0', null, null, new FixedFormatNumberData(Sign.PREPEND), new FixedFormatDecimalData(0, true, '_'))));

    assertEquals(new BigDecimal("0"), formatter.parse("+000000000", new FormatInstructions(10, Align.RIGHT, '0', null, null, new FixedFormatNumberData(Sign.PREPEND), new FixedFormatDecimalData(0, true, '_'))));
    assertEquals(new BigDecimal("0"), formatter.parse("-000000000", new FormatInstructions(10, Align.RIGHT, '0', null, null, new FixedFormatNumberData(Sign.PREPEND), new FixedFormatDecimalData(0, true, '_'))));

    assertEquals(new BigDecimal("-100.50"), formatter.parse("-000010050", new FormatInstructions(10, Align.RIGHT, '0', null, null, new FixedFormatNumberData(Sign.PREPEND), new FixedFormatDecimalData(2, false, '.'))));

    //test signing prepended
    assertEquals(new BigDecimal("0"), formatter.parse("000000000+", new FormatInstructions(10, Align.RIGHT, '0', null, null, new FixedFormatNumberData(Sign.APPEND), new FixedFormatDecimalData(2, true, '.'))));

    assertEquals(new BigDecimal("0"), formatter.parse("000000000+", new FormatInstructions(10, Align.RIGHT, '0', null, null, new FixedFormatNumberData(Sign.APPEND), new FixedFormatDecimalData(2, false, '.'))));
    assertEquals(new BigDecimal("0"), formatter.parse("000000000-", new FormatInstructions(10, Align.RIGHT, '0', null, null, new FixedFormatNumberData(Sign.APPEND), new FixedFormatDecimalData(2, false, '.'))));
    assertEquals(new BigDecimal("100.50"), formatter.parse("000010050+", new FormatInstructions(10, Align.RIGHT, '0', null, null, new FixedFormatNumberData(Sign.APPEND), new FixedFormatDecimalData(2, false, '.'))));

    assertEquals(new BigDecimal("-100.50"), formatter.parse("000010050-", new FormatInstructions(10, Align.RIGHT, '0', null, null, new FixedFormatNumberData(Sign.APPEND), new FixedFormatDecimalData(2, false, '.'))));
  }

  public void testFormat() {
    assertEquals("+000010050", formatter.format(new BigDecimal(100.5), new FormatInstructions(10, Align.RIGHT, '0', null, null, new FixedFormatNumberData(Sign.PREPEND), new FixedFormatDecimalData(2, false, '.'))));
    assertEquals("+000001005", formatter.format(new BigDecimal(100.51), new FormatInstructions(10, Align.RIGHT, '0', null, null, new FixedFormatNumberData(Sign.PREPEND), new FixedFormatDecimalData(1, false, '.'))));
    assertEquals("+000123456", formatter.format(new BigDecimal(1234.562), new FormatInstructions(10, Align.RIGHT, '0', null, null, new FixedFormatNumberData(Sign.PREPEND), new FixedFormatDecimalData(2, false, '.'))));

    assertEquals("+001234.56", formatter.format(new BigDecimal(1234.562), new FormatInstructions(10, Align.RIGHT, '0', null, null, new FixedFormatNumberData(Sign.PREPEND), new FixedFormatDecimalData(2, true, '.'))));
    assertEquals("+00123.456", formatter.format(new BigDecimal(123.4562), new FormatInstructions(10, Align.RIGHT, '0', null, null, new FixedFormatNumberData(Sign.PREPEND), new FixedFormatDecimalData(3, true, '.'))));

    assertEquals("+00000.000", formatter.format(new BigDecimal(0.0), new FormatInstructions(10, Align.RIGHT, '0', null, null, new FixedFormatNumberData(Sign.PREPEND), new FixedFormatDecimalData(3, true, '.'))));
    assertEquals("+00000.000", formatter.format(null, new FormatInstructions(10, Align.RIGHT, '0', null, null, new FixedFormatNumberData(Sign.PREPEND), new FixedFormatDecimalData(3, true, '.'))));

    //the number is bigger than the total length
    assertEquals("+456789.01", formatter.format(new BigDecimal(123456789.01), new FormatInstructions(10, Align.RIGHT, '0', null, null, new FixedFormatNumberData(Sign.PREPEND), new FixedFormatDecimalData(2, true, '.'))));

    assertEquals("-000010050", formatter.format(new BigDecimal(-100.5), new FormatInstructions(10, Align.RIGHT, '0', null, null, new FixedFormatNumberData(Sign.PREPEND), new FixedFormatDecimalData(2, false, '.'))));
    assertEquals("+000000.00", formatter.format(new BigDecimal(0), new FormatInstructions(10, Align.RIGHT, '0', null, null, new FixedFormatNumberData(Sign.PREPEND), new FixedFormatDecimalData(2, true, '.'))));
  }
}
