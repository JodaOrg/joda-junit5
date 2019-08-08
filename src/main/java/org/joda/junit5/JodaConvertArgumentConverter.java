/*
 *  Copyright 2019-present Stephen Colebourne
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.joda.junit5;

import org.joda.convert.StringConvert;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ArgumentConverter;

/**
 * Converts strings using Joda-Convert.
 */
public class JodaConvertArgumentConverter implements ArgumentConverter {

    @Override
    public Object convert(Object source, ParameterContext context) throws ArgumentConversionException {
        Class<?> targetType = context.getParameter().getType();
        if (targetType.isInstance(source)) {
            return source;
        }
        if (source instanceof String) {
            return StringConvert.INSTANCE.convertFromString(targetType, source.toString());
        }
        throw new ArgumentConversionException(
                "Unable to convert object, input must be a String, but was " + source.getClass().getName());
    }

}
