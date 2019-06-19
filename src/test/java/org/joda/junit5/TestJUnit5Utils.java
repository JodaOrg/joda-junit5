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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

/**
 * Test.
 */
public class TestJUnit5Utils implements AutoTest {

    public void test_isTestMethod() throws Exception {
        assertTrue(JUnit5Utils.isTestMethod(TestAutoTest.class.getDeclaredMethod("realTest")));
        assertFalse(JUnit5Utils.isTestMethod(TestAutoTest.class.getDeclaredMethod("nonTest")));
        assertFalse(JUnit5Utils.isTestMethod(TestAutoTest.class.getDeclaredMethod("annotatedTest")));
        assertFalse(JUnit5Utils.isTestMethod(TestAutoTest.class.getDeclaredMethod("baseTest")));
        assertFalse(JUnit5Utils.isTestMethod(TestAutoTest.class.getDeclaredMethod("derivedTest")));
    }

    public void test_wrapAsExecutable() throws Exception {
        TestAutoTest test = new TestAutoTest();
        Executable exec = JUnit5Utils.wrapAsExecutable(
                test, TestAutoTest.class.getDeclaredMethod("multiArg", String.class, String.class));
        assertThrows(IllegalStateException.class, () -> exec.execute());
    }

}

@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Test
@interface BaseTest {

}

@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@BaseTest
@interface DerivedTest {

}
