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

import com.ancientprogramming.fixedformat4j.annotation.*;

import java.util.Date;

/**
 * A record used in testcases
 *
 * @author Jacob von Eyben www.ancientprogramming.com
 * @since 1.0.0
 */
@com.ancientprogramming.fixedformat4j.annotation.Record
public class MyRecord {

  private String stringData;
  private Integer integerData;
  private Date dateData;
  private Character charData;
  private Boolean booleanData;
  private Long longData;
  private Double doubleData;
  private Float floatData;


  @FixedFormatField(offset = 1, length = 10, align = Align.RIGHT, paddingChar = ' ')
  public String getStringData() {
    return stringData;
  }

  public void setStringData(String stringData) {
    this.stringData = stringData;
  }




  @FixedFormatField(offset = 11, length = 5, align = Align.RIGHT, paddingChar = '0')
  public Integer getIntegerData() {
    return integerData;
  }

  public void setIntegerData(Integer integerData) {
    this.integerData = integerData;
  }



  @FixedFormatField(offset = 16, length = 8)
  @FixedFormatPattern("yyyyMMdd")
  public Date getDateData() {
    return dateData;
  }

  public void setDateData(Date dateData) {
    this.dateData = dateData;
  }

  @FixedFormatField(offset = 24, length = 1)
  public Character getCharData() {
    return charData;
  }

  public void setCharData(Character charData) {
    this.charData = charData;
  }

  @FixedFormatField(offset = 25, length = 1)
  @FixedFormatBoolean
  public Boolean getBooleanData() {
    return booleanData;
  }

  public void setBooleanData(Boolean booleanData) {
    this.booleanData = booleanData;
  }

  @FixedFormatField(offset = 26, length = 4, align = Align.RIGHT, paddingChar = '0')
  public Long getLongData() {
    return longData;
  }

  public void setLongData(Long longData) {
    this.longData = longData;
  }

  @FixedFormatField(offset = 30, length = 10, align = Align.RIGHT, paddingChar = '0')
  @FixedFormatDecimal
  public Double getDoubleData() {
    return doubleData;
  }

  public void setDoubleData(Double doubleData) {
    this.doubleData = doubleData;
  }

  @FixedFormatField(offset = 40, length = 10, align = Align.RIGHT, paddingChar = '0')
  @FixedFormatDecimal  
  public Float getFloatData() {
    return floatData;
  }

  public void setFloatData(Float floatData) {
    this.floatData = floatData;
  }


}