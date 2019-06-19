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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

/**
 * Test.
 */
public class TestAutoTest implements AutoTest {

    int count;

    @AfterAll
    public void afterAll() {
        assertEquals(4, count);
    }

    public void realTest() {
        count++;
    }

    void nonTest() {
        fail();
    }

    @Test
    public void annotatedTest() {
        count++;
    }

    @BaseTest
    public void baseTest() {
        count++;
    }

    @DerivedTest
    public void derivedTest() {
        count++;
    }

    public static void multiArg(String a, String b) {
    }

}
