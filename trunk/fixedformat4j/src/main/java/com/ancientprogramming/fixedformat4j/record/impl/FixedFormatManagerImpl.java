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
package com.ancientprogramming.fixedformat4j.record.impl;

import com.ancientprogramming.fixedformat4j.annotation.*;
import com.ancientprogramming.fixedformat4j.exception.FixedFormatException;
import com.ancientprogramming.fixedformat4j.format.FixedFormatData;
import com.ancientprogramming.fixedformat4j.format.FixedFormatMetadata;
import static com.ancientprogramming.fixedformat4j.format.FixedFormatProcessor.*;
import com.ancientprogramming.fixedformat4j.format.FixedFormatter;
import com.ancientprogramming.fixedformat4j.format.data.FixedFormatBooleanData;
import com.ancientprogramming.fixedformat4j.format.data.FixedFormatDecimalData;
import com.ancientprogramming.fixedformat4j.format.data.FixedFormatPatternData;
import com.ancientprogramming.fixedformat4j.record.FixedFormatManager;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import static java.lang.String.format;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Set;

/**
 * reads and writes data to and from fixedformat
 *
 * @author Jacob von Eyben www.ancientprogramming.com
 * @since 1.0.0
 */
public class FixedFormatManagerImpl implements FixedFormatManager {

  private static final Log LOG = LogFactory.getLog(FixedFormatManagerImpl.class);

  public <T> T load(Class<T> fixedFormatRecordClass, String data) {
    HashMap<String, Object> foundData = new HashMap<String, Object>();
    //assert the record is marked with a Record
    Record record = getRecordAnnotation(fixedFormatRecordClass);

    //create instance to set data into
    T instance;
    try {
      Constructor<T> constructor = fixedFormatRecordClass.getConstructor();
      instance = constructor.newInstance();
    } catch (NoSuchMethodException e) {
      throw new FixedFormatException(format("%s is missing a default constructor which is nessesary to be loaded through %s", fixedFormatRecordClass.getName(), getClass().getName()));
    } catch (Exception e) {
      throw new FixedFormatException(format("unable to create instance of %s", fixedFormatRecordClass.getName()), e);
    }

    //look for setter annotations and read data from the 'data' string
    Method[] allMethods = fixedFormatRecordClass.getMethods();
    for (Method method : allMethods) {
      String methodName = stripMethodPrefix(method.getName());
      Field fieldAnnotation = method.getAnnotation(Field.class);
      Fields fieldsAnnotation = method.getAnnotation(Fields.class);
      if (fieldAnnotation != null) {
        Object loadedData = readDataAccordingFieldAnnotation(data, method, fieldAnnotation);
        foundData.put(methodName, loadedData);
      } else if (fieldsAnnotation != null) {
        //assert that the fields annotation contains minimum one field anno
        if (fieldsAnnotation.value() == null || fieldsAnnotation.value().length == 0) {
          throw new FixedFormatException(format("%s annotation must contain minimum one %s annotation", Fields.class.getName(), Field.class.getName()));
        }
        Object loadedData = readDataAccordingFieldAnnotation(data, method, fieldsAnnotation.value()[0]);
        foundData.put(methodName, loadedData);
      }
    }

    Set<String> keys = foundData.keySet();
    for (String key : keys) {
      String setterMethodName = "set" + key;

      Object foundDataObj = foundData.get(key);
      if (foundDataObj != null) {
        Class datatype = foundData.get(key).getClass();
        Method method;
        try {
          method = fixedFormatRecordClass.getMethod(setterMethodName, datatype);
        } catch (NoSuchMethodException e) {
          throw new FixedFormatException(format("setter method named %s.%s(%s) does not exist", fixedFormatRecordClass.getName(), setterMethodName, datatype));
        }
        try {
          method.invoke(instance, foundData.get(key));
        } catch (Exception e) {
          throw new FixedFormatException(format("could not invoke method %s.%s(%s)", fixedFormatRecordClass.getName(), setterMethodName, datatype), e);
        }
      }

    }
    return instance;
  }

  public <T> String export(String existingData, T fixedFormatRecord) {
    StringBuffer result = new StringBuffer(existingData);
    Record record = getRecordAnnotation(fixedFormatRecord.getClass());

    Class<T> fixedFormatRecordClass = (Class<T>) fixedFormatRecord.getClass();
    HashMap<Integer, String> foundData = new HashMap<Integer, String>(); // hashmap containing offset and data to write
    Method[] allMethods = fixedFormatRecordClass.getMethods();
    for (Method method : allMethods) {
      Field fieldAnnotation = method.getAnnotation(Field.class);
      Fields fieldsAnnotation = method.getAnnotation(Fields.class);
      if (fieldAnnotation != null) {
        String exportedData = exportDataAccordingFieldAnnotation(fixedFormatRecord, method, fieldAnnotation);
        foundData.put(fieldAnnotation.offset(), exportedData);
      } else if (fieldsAnnotation != null) {
        Field[] fields = fieldsAnnotation.value();
        for (Field field : fields) {
          String exportedData = exportDataAccordingFieldAnnotation(fixedFormatRecord, method, field);
          foundData.put(field.offset(), exportedData);
        }
      }
    }

    Set<Integer> sortedoffsets = foundData.keySet();
    for (Integer offset : sortedoffsets) {
      String data = foundData.get(offset);
      appendData(result, record.paddingChar(), offset, data);
    }

    if (record.length() != -1) { //pad with paddingchar
      while (result.length() < record.length()) {
        result.append(record.paddingChar());
      }
    }
    return result.toString();
  }

  public <T> String export(T fixedFormatRecord) {
    return export("", fixedFormatRecord);
  }

  private void appendData(StringBuffer result, Character paddingChar, Integer offset, String data) {
    int zeroBasedOffset = offset - 1;
    while (result.length() < zeroBasedOffset) {
      result.append(paddingChar);
    }
    int length = data.length();
    if (result.length() < zeroBasedOffset + length) {
      result.append(StringUtils.leftPad("", (zeroBasedOffset + length) - result.length(), paddingChar));
    }
    result.replace(zeroBasedOffset, zeroBasedOffset + length, data);
  }


  private <T> Record getRecordAnnotation(Class<T> fixedFormatRecordClass) {
    Record recordAnno = FixedFormatAnnotationUtil.getAnnotation(fixedFormatRecordClass, Record.class);
    if (recordAnno == null) {
      throw new FixedFormatException(format("%s has to be marked with the record annotation to be loaded", fixedFormatRecordClass.getName()));
    }
    return recordAnno;
  }

  private <T> Object readDataAccordingFieldAnnotation(String data, Method method, Field fieldAnno) {
    Class datatype = null;
    if (isGetter(method)) {
      datatype = method.getReturnType();
    } else {
      throw new FixedFormatException(format("%s annotations must be placed on getter methods", fieldAnno.getClass().getName()));
    }
    FixedFormatMetadata metadata = getMetadata(datatype, fieldAnno);
    FixedFormatter formatter = getFixedFormatterInstance(metadata.getFormatter(), metadata);
    FixedFormatData formatdata = getFormatData(method, fieldAnno);

    assertIsPatternRequired(formatdata, metadata, formatter);
    assertIsBooleanRequired(formatdata, metadata, formatter);
    assertIsDecimalRequired(formatdata, metadata, formatter);
    Object loadedData = formatter.parse(fetchData(new StringBuffer(data), formatdata, metadata), formatdata);
    if (LOG.isDebugEnabled()) {
      LOG.debug("the loaded data[" + loadedData + "]");
    }
    return loadedData;
  }

  private <T> String exportDataAccordingFieldAnnotation(T fixedFormatRecord, Method method, Field fieldAnno) {
    String result;
    Class datatype;
    if (isGetter(method)) {
      datatype = method.getReturnType();
    } else {
      throw new FixedFormatException(format("%s annotations must be placed on getter methods", fieldAnno.getClass().getName()));
    }

    FixedFormatMetadata metadata = getMetadata(datatype, fieldAnno);
    FixedFormatter formatter = getFixedFormatterInstance(metadata.getFormatter(), metadata);
    FixedFormatData formatdata = getFormatData(method, fieldAnno);
    Object valueObject = null;
    try {
      valueObject = method.invoke(fixedFormatRecord);
    } catch (Exception e) {
      throw new FixedFormatException(format("could not invoke method %s.%s(%s)", fixedFormatRecord.getClass().getName(), method.getName(), datatype), e);
    }
    result = formatter.format(valueObject, formatdata);
    if (LOG.isDebugEnabled()) {
      LOG.debug(format("exported %s ", result));
    }
    return result;
  }


  private String stripMethodPrefix(String name) {
    return name.substring(3);
  }


  private FixedFormatMetadata getMetadata(Class datatype, Field fieldAnno) {
    FixedFormatMetadata metadata = null;
    if (fieldAnno != null) {
      metadata = new FixedFormatMetadata(fieldAnno.offset(), datatype, fieldAnno.formatter());
    }
    return metadata;

  }

  private FixedFormatData getFormatData(Method method, Field fieldAnno) {
    FixedFormatPatternData patternData = getFixedFormatPatternData(method.getAnnotation(FixedFormatPattern.class));
    FixedFormatBooleanData booleanData = getFixedFormatBooleanData(method.getAnnotation(FixedFormatBoolean.class));
    FixedFormatDecimalData decimalData = getFixedFormatDecimalData(method.getAnnotation(FixedFormatDecimal.class));
    return new FixedFormatData(fieldAnno.length(), fieldAnno.align(), fieldAnno.paddingChar(), patternData, booleanData, decimalData);
  }

  private FixedFormatPatternData getFixedFormatPatternData(FixedFormatPattern annotation) {
    FixedFormatPatternData result = null;
    if (annotation != null) {
      result = new FixedFormatPatternData(annotation.value());
    }
    return result;
  }

  private FixedFormatBooleanData getFixedFormatBooleanData(FixedFormatBoolean annotation) {
    FixedFormatBooleanData result = null;
    if (annotation != null) {
      result = new FixedFormatBooleanData(annotation.trueValue(), annotation.falseValue());
    }
    return result;
  }

  private FixedFormatDecimalData getFixedFormatDecimalData(FixedFormatDecimal annotation) {
    FixedFormatDecimalData result = null;
    if (annotation != null) {
      result = new FixedFormatDecimalData(annotation.decimals(), annotation.useDecimalDelimiter(), annotation.decimalDelimiter());
    }
    return result;
  }

  private boolean isGetter(Method method) {
    return method.getName().startsWith("get");
  }
}
