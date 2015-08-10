/*
 * Copyright 2015 Telefónica Investigación y Desarrollo, S.A.U
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package es.tid.neosdp.plainmap;

import java.util.Map;

import org.junit.Test;

/**
 * Performance test for {@link PlainMap}.
 *
 * @author Jorge Lorenzo (jorgelg@tid.es)
 */
public class PlainMapPerformanceTest {

    private static final int ITERATIONS = 1000000;

    /*
     * Performance results (2014/04/25)
     *
     * Get available elements from plainmap => Duration (1000000 iterations): 489 ms.
     * Check existence of available elements from plainmap => Duration (1000000 iterations): 417 ms.
     * Get available elements from hierarchical map => Duration (1000000 iterations): 85 ms.
     * Get not available elements from plainmap => Duration (1000000 iterations): 370 ms.
     * Check existence of not available elements from plainmap => Duration (1000000 iterations): 394 ms.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testPlainMap_1() throws PlainMapException {
        PlainMap plainMap = new PlainMap();
        // Adding data to the map
        plainMap.put("paymentRequest.amount", "0.01");
        plainMap.put("paymentRequest.referenceCode", "000108");
        plainMap.put("paymentRequest.description", "The Sound of Music");
        plainMap.put("paymentRequest.productId", "892yhnsia8");
        plainMap.put("paymentRequest.currency", "EUR");

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < PlainMapPerformanceTest.ITERATIONS; i++) {
            plainMap.get("paymentRequest.referenceCode");
            plainMap.get("paymentRequest.productId");
        }
        System.out.println("Get available elements from plainmap => Duration (" + PlainMapPerformanceTest.ITERATIONS + " iterations): " + (System.currentTimeMillis() - startTime) + " ms.");

        startTime = System.currentTimeMillis();
        for (int i = 0; i < PlainMapPerformanceTest.ITERATIONS; i++) {
            plainMap.exists("paymentRequest.referenceCode");
            plainMap.exists("paymentRequest.productId");
        }
        System.out.println("Check existance of available elements from plainmap => Duration (" + PlainMapPerformanceTest.ITERATIONS + " iterations): " + (System.currentTimeMillis() - startTime) + " ms.");

        startTime = System.currentTimeMillis();
        for (int i = 0; i < PlainMapPerformanceTest.ITERATIONS; i++) {
            Map<String, Object> paymentRequestMap = (Map<String, Object>)plainMap.getHierarchicalMap().get("paymentRequest");
            paymentRequestMap.get("referenceCode");
            paymentRequestMap.get("productId");
        }
        System.out.println("Get available elements from hierarchical map => Duration (" + PlainMapPerformanceTest.ITERATIONS + " iterations): " + (System.currentTimeMillis() - startTime) + " ms.");

        startTime = System.currentTimeMillis();
        for (int i = 0; i < PlainMapPerformanceTest.ITERATIONS; i++) {
            plainMap.get("paymentRequest.userId");
            plainMap.get("paymentRequest.paymentMethod.type");
        }
        System.out.println("Get not available elements from plainmap => Duration (" + PlainMapPerformanceTest.ITERATIONS + " iterations): " + (System.currentTimeMillis() - startTime) + " ms.");

        startTime = System.currentTimeMillis();
        for (int i = 0; i < PlainMapPerformanceTest.ITERATIONS; i++) {
            plainMap.exists("paymentRequest.userId");
            plainMap.exists("paymentRequest.paymentMethod.type");
        }
        System.out.println("Check existance of not available elements from plainmap => Duration (" + PlainMapPerformanceTest.ITERATIONS + " iterations): " + (System.currentTimeMillis() - startTime) + " ms.");
    }
}