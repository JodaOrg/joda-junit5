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

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

/**
 * Base interface for JUnit 5 where tests are automatically identified.
 * <p>
 * To run JUnit 5 tests, simply make your test class extend this one and write public void no-args test methods.
 * Note that the test methods do <i>not</i> need to start with "test". Any methods that are static,
 * abstract or annotated with a JUnit 5 {@code Test} or {@code TestTemplate} annotation (or meta-annotation)
 * will not be treated as test methods.
 * <p>
 * In technical terms, this interface provides one parameterized test method where
 * the parameter is the {@link Method} object representing the actual test method.
 * As such, all dynamically discovered test methods will be grouped under "runTests" in the results.
 * <p>
 * This interface also sets {@code @TestInstance(Lifecycle.PER_CLASS)}.
 */
@TestInstance(Lifecycle.PER_CLASS)
public interface AutoTest {
    // prototyping was performed with @TestFactory and @ParameterizedTest. The @TestFactory approach
    // was slightly simpler, but didn't call @BeforeEach/@AfterEach properly, whereas this approach does.
    // No approach could be found that allowed an annotation to manage the tests.

    /**
     * Runs the tests in the current class.
     * 
     * @return the tests in the implementing class
     */
    @TestFactory
    public default Stream<DynamicTest> runTests() {
        return Arrays.stream(getClass().getDeclaredMethods())
                .filter(method -> JUnit5Utils.isTestMethod(method))
                .map(method -> DynamicTest.dynamicTest(method.getName(), () -> method.invoke(this)));
    }

}
