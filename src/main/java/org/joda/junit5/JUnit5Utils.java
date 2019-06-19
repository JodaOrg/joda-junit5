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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.function.Executable;

/**
 * Utilities for this package.
 */
final class JUnit5Utils {

    // check if method is a test method
    static boolean isTestMethod(Method method) {
        return Modifier.isPublic(method.getModifiers()) &&
                !Modifier.isStatic(method.getModifiers()) &&
                !Modifier.isAbstract(method.getModifiers()) &&
                !method.isBridge() &&
                !method.isSynthetic() &&
                method.getReturnType().equals(void.class) &&
                !isAnnotated(method, Test.class) &&
                !isAnnotated(method, TestTemplate.class) &&
                method.getAnnotation(BeforeAll.class) == null &&
                method.getAnnotation(AfterAll.class) == null &&
                method.getAnnotation(BeforeEach.class) == null &&
                method.getAnnotation(AfterEach.class) == null;
    }

    // check if method is a test method
    static Executable wrapAsExecutable(Object testInstance, Method method) {
        // inner classes used so that toString() can be used in test name
        if (method.getParameterCount() > 0) {
            return new Executable() {
                @Override
                public void execute() throws Throwable {
                    throw new IllegalStateException("Method '" + method.getName() +
                            "' takes parameters, so it must be non-public or annotated with @ParameterizedTest");
                }

                @Override
                public String toString() {
                    return method.getName();
                }
            };
        }
        return new Executable() {
            @Override
            public void execute() throws Throwable {
                method.invoke(testInstance);
            }

            @Override
            public String toString() {
                return method.getName();
            }
        };
    }

    // is the method annotated
    private static boolean isAnnotated(Method method, Class<? extends Annotation> annoType) {
        return isAnnotated(method, annoType, new HashSet<>());
    }

    // is the method annotated
    private static <A extends Annotation> boolean isAnnotated(
            Method method,
            Class<A> annoType,
            Set<Annotation> visited) {

        // search the declared and indirect annotations
        return method.getDeclaredAnnotation(annoType) != null ||
                isMetaAnnotated(annoType, method.getAnnotations(), visited);
    }

    // look for meta annotations
    private static <A extends Annotation> boolean isMetaAnnotated(
            Class<A> annoType,
            Annotation[] searchAnnos,
            Set<Annotation> visited) {

        for (Annotation searchAnno : searchAnnos) {
            Class<? extends Annotation> searchAnnoType = searchAnno.annotationType();
            if (visited.add(searchAnno)) {
                boolean found = searchAnnoType.getDeclaredAnnotation(annoType) != null ||
                        isMetaAnnotated(annoType, searchAnnoType.getAnnotations(), visited);
                if (found) {
                    return true;
                }
            }
        }
        return false;
    }

    //-----------------------------------------------------------------------
    // restricted constructor
    private JUnit5Utils() {
    }

}
