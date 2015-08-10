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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Unit test for {@link PlainMap}.
 *
 * @author Jorge Lorenzo (jorgelg@tid.es)
 */
public class PlainMapTest {

    @Test
    public void testPlainMap() throws PlainMapException {
        Map<String, Object> originalMap = new HashMap<String, Object>();
        originalMap.put("key1", "value");
        Map<String, Object> originalSubMap = new HashMap<String, Object>();
        originalSubMap.put("subKey1", "subvalue");
        originalMap.put("key2", originalSubMap);

        PlainMap plainMap = new PlainMap(originalMap);
        Assert.assertEquals("value", plainMap.get("key1"));
        Assert.assertEquals("subvalue", plainMap.get("key2.subKey1"));

        plainMap = new PlainMap(originalMap, "root.subroot");
        Assert.assertEquals("value", plainMap.get("root.subroot.key1"));
        Assert.assertEquals("subvalue", plainMap.get("root.subroot.key2.subKey1"));
    }

    @Test
    public void testPlainMap_1() throws PlainMapException {
        PlainMap plainMap = new PlainMap();
        // Adding data to the map
        plainMap.put("first", "primero");
        plainMap.put("second", "segundo");
        plainMap.put("third.fourth", "34");
        plainMap.put("third.fifth", "35");
        plainMap.put("fourth.array[0]", "0");
        plainMap.put("fourth.array[1]", "1");
        plainMap.put("fourth.array[5]", "5");
        plainMap.put("fourth.internalArray[1].one", "uno");
        plainMap.put("prueba.internalArray[1].two.three", "23");
        // Assertions for "get"
        Assert.assertEquals("primero", plainMap.get("first"));
        Assert.assertEquals("segundo", plainMap.get("second"));
        Assert.assertEquals("34", plainMap.get("third.fourth"));
        Assert.assertEquals("35", plainMap.get("third.fifth"));
        Assert.assertEquals("0", plainMap.get("fourth.array[0]"));
        Assert.assertEquals("1", plainMap.get("fourth.array[1]"));
        Assert.assertEquals("5", plainMap.get("fourth.array[5]"));
        Assert.assertEquals("6", plainMap.get("fourth.array.length"));
        Assert.assertEquals("uno", plainMap.get("fourth.internalArray[1].one"));
        Assert.assertEquals("23", plainMap.get("prueba.internalArray[1].two.three"));
        Assert.assertEquals("2", plainMap.get("prueba.internalArray.length"));
        // Assertions for "exists"
        Assert.assertTrue(plainMap.exists("first"));
        Assert.assertFalse(plainMap.exists("first.invalid"));
        Assert.assertTrue(plainMap.exists("second"));
        Assert.assertFalse(plainMap.exists("invalid"));
        Assert.assertTrue(plainMap.exists("third.fourth"));
        Assert.assertTrue(plainMap.exists("third.fifth"));
        Assert.assertFalse(plainMap.exists("third.invalid"));
        Assert.assertTrue(plainMap.exists("fourth.array[0]"));
        Assert.assertTrue(plainMap.exists("fourth.array[1]"));
        Assert.assertTrue(plainMap.exists("fourth.array[5]"));
        Assert.assertFalse(plainMap.exists("fourth.array[6]"));
        Assert.assertFalse(plainMap.exists("fourth.invalidarray[6]"));
        Assert.assertFalse(plainMap.exists("fourth.array.invalid"));
        Assert.assertTrue(plainMap.exists("fourth.internalArray[1].one"));
        Assert.assertFalse(plainMap.exists("fourth.internalArray[1].invalid"));
        Assert.assertTrue(plainMap.exists("prueba.internalArray[1].two.three"));
        // Test update of elements
        plainMap.put("third.fourth", "updated_34");
        plainMap.put("fourth.array[1]", "updated_1");
        plainMap.put("prueba.internalArray[1].two.three", "updated_23");
        plainMap.put("prueba.internalArray[0].two.three", "new_23");
        // Assertions
        Assert.assertEquals("updated_34", plainMap.get("third.fourth"));
        Assert.assertEquals("updated_1", plainMap.get("fourth.array[1]"));
        Assert.assertEquals("updated_23", plainMap.get("prueba.internalArray[1].two.three"));
        Assert.assertEquals("new_23", plainMap.get("prueba.internalArray[0].two.three"));
        Assert.assertEquals("updated_34", plainMap.get("third.fourth"));
        Assert.assertEquals("2", plainMap.get("prueba.internalArray.length"));
    }

    @Test
    public void testPlainMap_2() throws PlainMapException {
        PlainMap plainMap = new PlainMap();
        // Adding data to the map
        plainMap.put("[0]", "primero");
        plainMap.put("[1].two", "1.2");
        plainMap.put("[3][0]", "3_0");
        plainMap.put("[3][1]", "3_1");
        // Asserts "get"
        Assert.assertEquals("primero", plainMap.get("[0]"));
        Assert.assertEquals("1.2", plainMap.get("[1].two"));
        Assert.assertEquals("3_0", plainMap.get("[3][0]"));
        Assert.assertEquals("3_1", plainMap.get("[3][1]"));
        Assert.assertEquals("4", plainMap.get(".length"));
        Assert.assertEquals("2", plainMap.get("[3].length"));
        // Asserts "exists"
        Assert.assertTrue(plainMap.exists("[0]"));
        Assert.assertTrue(plainMap.exists("[1].two"));
        Assert.assertTrue(plainMap.exists("[3][0]"));
        Assert.assertTrue(plainMap.exists("[3][1]"));
        Assert.assertFalse(plainMap.exists("[4]"));
        Assert.assertFalse(plainMap.exists("[1].undefined"));
        Assert.assertFalse(plainMap.exists("[3][2]"));
        // Add a root path
        plainMap.addRootPath("root.test");
        // Asserts "get"
        Assert.assertEquals("primero", plainMap.get("root.test[0]"));
        Assert.assertEquals("1.2", plainMap.get("root.test[1].two"));
        Assert.assertEquals("3_0", plainMap.get("root.test[3][0]"));
        Assert.assertEquals("3_1", plainMap.get("root.test[3][1]"));
        Assert.assertEquals("4", plainMap.get("root.test.length"));
        Assert.assertEquals("2", plainMap.get("root.test[3].length"));
        // Asserts "exists"
        Assert.assertTrue(plainMap.exists("root.test[0]"));
        Assert.assertTrue(plainMap.exists("root.test[1].two"));
        Assert.assertTrue(plainMap.exists("root.test[3][0]"));
        Assert.assertTrue(plainMap.exists("root.test[3][1]"));
        Assert.assertFalse(plainMap.exists("root.test[4]"));
        Assert.assertFalse(plainMap.exists("root.test[1].undefined"));
        Assert.assertFalse(plainMap.exists("root.test[3][2]"));
        // Remove elements
        plainMap.remove("root.test[3][0]");
        plainMap.remove("root.test[1].two");
        // Asserts
        Assert.assertNull(plainMap.get("root.test[1].two"));
        Assert.assertNull(plainMap.get("root.test[2]"));
        Assert.assertEquals("3_1", plainMap.get("root.test[3][0]"));
        Assert.assertEquals("1", plainMap.get("root.test[3].length"));
        // Remove a root path
        plainMap.removeRootPath("root");
        // Asserts
        Assert.assertEquals("primero", plainMap.get("test[0]"));
        Assert.assertNull(plainMap.get("test[2]"));
        Assert.assertEquals("3_1", plainMap.get("test[3][0]"));
        Assert.assertEquals("4", plainMap.get("test.length"));
        Assert.assertEquals("1", plainMap.get("test[3].length"));
    }

    @Test
    public void testPlainMap_3() throws PlainMapException {
        PlainMap plainMap = new PlainMap();
        plainMap.put("getCommercialGroup.id", "5");
        plainMap.remove("getCommercialGroup.id");
        Assert.assertNull(plainMap.get("getCommercialGroup"));
    }

    @Test
    public void testPlainMap_4() throws PlainMapException {
        PlainMap plainMap = new PlainMap();
        plainMap.put("id", "18301");
        plainMap.put("serviceId", "5");
        plainMap.put("appProviderId", "1");
        plainMap.put("status.value", "1");
        plainMap.put("status.lastUpdated", "Sat Oct 13 13:22:28 CEST 2012");
        plainMap.put("appApi[0].apiId", "102");
        plainMap.put("appApi[0].configParam[0].name", "endpoint");
        plainMap.put("appApi[0].configParam[0].value[0]", "path");
        plainMap.put("appApi[0].configParam[0].value[1]", "http://localhost:8888/auth/login");
        plainMap.put("appApi[0].accessScope[0].scopeValue", "oauth2.authCode");
        plainMap.put("appApi[0].accessScope[0].authorizationRequired", "false");
        plainMap.put("appApi[0].accessScope[1].scopeValue", "oauth2.implicit");
        plainMap.put("appApi[0].accessScope[1].authorizationRequired", "true");
        plainMap.put("appApi[1].apiId", "7");
        plainMap.put("appApi[1].accessScope[0].scopeValue", "userdata.*");
        plainMap.put("appApi[1].accessScope[0].authorizationRequired", "true");
        Assert.assertEquals("18301", plainMap.get("id"));
        Assert.assertEquals("Sat Oct 13 13:22:28 CEST 2012", plainMap.get("status.lastUpdated"));
        Assert.assertEquals("path", plainMap.get("appApi{apiId=102}.configParam{name=endpoint}.value[0]"));
        Assert.assertEquals("http://localhost:8888/auth/login", plainMap.get("appApi{apiId=102}.configParam{name=endpoint}.value[1]"));
        Assert.assertEquals("false", plainMap.get("appApi{apiId=102}.accessScope{scopeValue=oauth2.authCode}.authorizationRequired"));
        Assert.assertEquals("oauth2.authCode", plainMap.get("appApi{apiId=102}.accessScope{authorizationRequired=false}.scopeValue"));
        Assert.assertEquals("true", plainMap.get("appApi{apiId=102}.accessScope{scopeValue=oauth2.implicit}.authorizationRequired"));
        Assert.assertEquals("oauth2.implicit", plainMap.get("appApi{apiId=102}.accessScope{authorizationRequired=true}.scopeValue"));
        Assert.assertEquals("true", plainMap.get("appApi{apiId=7}.accessScope{scopeValue=userdata.*}.authorizationRequired"));
        Assert.assertEquals("userdata.*", plainMap.get("appApi{apiId=7}.accessScope{authorizationRequired=true}.scopeValue"));
        Assert.assertNull(plainMap.get("appApi{apiId=7}.accessScope{authorizationRequired=false}.scopeValue"));
        // Asserts "exists"
        Assert.assertTrue(plainMap.exists("id"));
        Assert.assertTrue(plainMap.exists("status.lastUpdated"));
        Assert.assertFalse(plainMap.exists("appApi{apiId=103}"));
        Assert.assertTrue(plainMap.exists("appApi{apiId=102}.configParam{name=endpoint}"));
        Assert.assertTrue(plainMap.exists("appApi{apiId=102}.configParam{name=endpoint}.value[0]"));
        Assert.assertTrue(plainMap.exists("appApi{apiId=102}.configParam{name=endpoint}.value[1]"));
        Assert.assertTrue(plainMap.exists("appApi{apiId=102}.accessScope{scopeValue=oauth2.authCode}.authorizationRequired"));
        Assert.assertTrue(plainMap.exists("appApi{apiId=102}.accessScope{authorizationRequired=false}.scopeValue"));
        Assert.assertTrue(plainMap.exists("appApi{apiId=102}.accessScope{authorizationRequired=true}.scopeValue"));
        Assert.assertTrue(plainMap.exists("appApi{apiId=7}.accessScope{scopeValue=userdata.*}.authorizationRequired"));
        Assert.assertTrue(plainMap.exists("appApi{apiId=102}.accessScope{authorizationRequired=true}.scopeValue"));
        Assert.assertTrue(plainMap.exists("appApi{apiId=7}.accessScope{scopeValue=userdata.*}.authorizationRequired"));
        Assert.assertTrue(plainMap.exists("appApi{apiId=7}.accessScope{authorizationRequired=true}.scopeValue"));

        // Test invalid values for associative array path
        Assert.assertNull(plainMap.get("appApi{apiId=102"));
        Assert.assertNull(plainMap.get("appApi{apiId:102}"));

        // Test invalid values for array path
        Assert.assertNull(plainMap.get("appApi{apiId=102}.configParam{name=endpoint}.value[0"));
        Assert.assertNull(plainMap.get("appApi{apiId=102}.configParam{name=endpoint}.value[zero]"));
    }

    @Test
    public void testPlainMap_5() throws PlainMapException {
        PlainMap plainMap = new PlainMap();
        plainMap.put("root.value", "ok");
        plainMap.put("single", "node");
        Assert.assertEquals("ok", plainMap.get("root.value"));
        Assert.assertEquals("ok", plainMap.get("root[0].value"));
        Assert.assertNull(plainMap.get("root[1].value"));
        Assert.assertEquals("node", plainMap.get("single"));
        Assert.assertEquals("node", plainMap.get("single[0]"));
        Assert.assertNull(plainMap.get("single[1]"));
        Assert.assertNull(plainMap.get("single[0].value"));
        Assert.assertNull(plainMap.get("single[1].value"));
    }

    @Test
    public void testPut() throws PlainMapException {
        // Check simple types (different from string)
        PlainMap plainMap = new PlainMap();
        plainMap.put("root.integer", new Integer(4));
        plainMap.put("root.boolean", Boolean.TRUE);
        Assert.assertEquals(4, plainMap.get("root.integer"));
        Assert.assertEquals(true, plainMap.get("root.boolean"));
        // Check maps
        PlainMap plainMap2 = new PlainMap();
        plainMap2.put("", plainMap.getHierarchicalMap());
        plainMap2.put("map", plainMap.getHierarchicalMap());
        Assert.assertEquals(4, plainMap2.get("root.integer"));
        Assert.assertEquals(true, plainMap2.get("root.boolean"));
        Assert.assertEquals(4, plainMap2.get("map.root.integer"));
        Assert.assertEquals(true, plainMap2.get("map.root.boolean"));
        // Check lists
        PlainMap plainMap3 = new PlainMap();
        List<Object> list = new ArrayList<Object>();
        list.add(plainMap.getHierarchicalMap());
        plainMap3.put("", list);
        plainMap3.put("root.list", list);
        Assert.assertEquals(4, plainMap3.get("[0].root.integer"));
        Assert.assertEquals(true, plainMap3.get("[0].root.boolean"));
        Assert.assertEquals(4, plainMap3.get("root.list[0].root.integer"));
        Assert.assertEquals(true, plainMap3.get("root.list[0].root.boolean"));
        // Check plainmaps
        PlainMap plainMap4 = new PlainMap();
        plainMap4.put("plainmapKey", "plainmapValue");
        plainMap4.put("plainmapOtherKey.parent.child[0]", 20);
        plainMap3.put(null, plainMap4);
        Assert.assertEquals(4, plainMap3.get("[0].root.integer"));
        Assert.assertEquals(true, plainMap3.get("[0].root.boolean"));
        Assert.assertEquals(4, plainMap3.get("root.list[0].root.integer"));
        Assert.assertEquals(true, plainMap3.get("root.list[0].root.boolean"));
        Assert.assertEquals("plainmapValue", plainMap3.get("plainmapKey"));
        Assert.assertEquals(20, plainMap3.get("plainmapOtherKey.parent.child[0]"));
    }

    @Test
    public void testGetPlainMap() throws PlainMapException {
        PlainMap plainMap = new PlainMap();
        // Adding data to the map
        plainMap.put("first", "primero");
        plainMap.put("third.fourth", "34");
        plainMap.put("fourth.array[0]", "0");
        plainMap.put("fourth.array[5]", "5");
        plainMap.put("fourth.internalArray[1].one", "uno");
        plainMap.put("prueba.internalArray[1].two.three", "23");

        Map<String, String> map = plainMap.getPlainMap();
        Assert.assertEquals("primero", map.get("first"));
        Assert.assertEquals("34", map.get("third.fourth"));
        Assert.assertEquals("0", map.get("fourth.array[0]"));
        Assert.assertEquals("5", map.get("fourth.array[5]"));
        Assert.assertEquals("uno", map.get("fourth.internalArray[1].one"));
        Assert.assertEquals("23", map.get("prueba.internalArray[1].two.three"));
    }

    @Test
    public void testClear() throws PlainMapException {
        PlainMap plainMap = new PlainMap();
        plainMap.put("first", "primero");
        plainMap.put("third.fourth", "34");
        plainMap.put("fourth.array[0]", "0");
        plainMap.put("fourth.array[5]", "5");
        plainMap.put("fourth.internalArray[1].one", "uno");
        plainMap.put("prueba.internalArray[1].two.three", "23");

        plainMap.clear();
        Assert.assertTrue(plainMap.getHierarchicalMap().isEmpty());
    }

    @Test
    public void testRemove() throws PlainMapException {
        PlainMap plainMap = new PlainMap();
        // Adding data to the map
        plainMap.put("first", "primero");
        plainMap.put("third.fourth", "34");
        plainMap.put("fourth.array[0]", "0");
        plainMap.put("fourth.array[5]", "5");
        plainMap.put("fourth.internalArray[1].one", "uno");
        plainMap.put("prueba.internalArray[1].two.three", "23");

        // Remove existent element
        plainMap.remove("first");
        Assert.assertFalse(plainMap.exists("first"));
        plainMap.remove("third.fourth");
        Assert.assertFalse(plainMap.exists("third.fourth"));

        // Remove non existent elements are ignored
        plainMap.remove("invalid");
        plainMap.remove("fourth.array[20]");
        plainMap.remove("");
        plainMap.remove(null);
        plainMap.remove("fourth.invalid[0]");
        plainMap.remove("prueba.internalArray[1].invalid.one");

        // Remove with invalid array syntax
        try {
            plainMap.remove("fourth.array20]");
            Assert.fail("It should have thrown a PlainMapException because the array syntax is wrong");
        } catch (PlainMapException e) {
            Assert.assertEquals("Error removing an element with a wrong path: fourth.array20]", e.getMessage());
        }
        try {
            plainMap.remove("fourth.array[twenty]");
            Assert.fail("It should have thrown a PlainMapException because the array syntax is wrong");
        } catch (PlainMapException e) {
            Assert.assertEquals("The path: fourth.array[twenty] specifies a wrong list index", e.getMessage());
        }

        // Remove with invalid type (not a list)
        try {
            plainMap.remove("prueba.internalArray[1].two[three]");
            Assert.fail("It should have thrown a PlainMapException because prueba.internalArray[1].two is a map, not a list");
        } catch (PlainMapException e) {
            Assert.assertEquals("The path: prueba.internalArray[1].two[three] targets a list but it is not a list", e.getMessage());
        }

        // Remove with invalid type (not a map)
        try {
            plainMap.remove("fourth.array.five");
            Assert.fail("It should have thrown a PlainMapException because fourth.array is a list, not a map");
        } catch (PlainMapException e) {
            Assert.assertEquals("The path: fourth.array.five targets a map but it is not a map", e.getMessage());
        }

        // Create a plainmap with a root different from a map
        plainMap = new PlainMap();
        plainMap.put(null, "value");
        Assert.assertEquals("value", plainMap.get(null));
        Assert.assertEquals("value", plainMap.get(""));
        plainMap.remove(null);
        Assert.assertFalse(plainMap.exists(null));
        Assert.assertFalse(plainMap.exists(""));
    }

    @Test
    public void testAddRootPath() throws PlainMapException {
        PlainMap plainMap = new PlainMap();
        plainMap.put("parent.child1", "value1");
        plainMap.put("parent.child2", "value2");
        plainMap.put("other", "value3");

        // Null or empty path has no effect on the plainmap
        plainMap.addRootPath(null);
        plainMap.addRootPath("");
        Assert.assertEquals("value1", plainMap.get("parent.child1"));
        Assert.assertEquals("value2", plainMap.get("parent.child2"));
        Assert.assertEquals("value3", plainMap.get("other"));

        plainMap.addRootPath("root");
        Assert.assertEquals("value1", plainMap.get("root.parent.child1"));
        Assert.assertEquals("value2", plainMap.get("root.parent.child2"));
        Assert.assertEquals("value3", plainMap.get("root.other"));
    }

    @Test
    public void testRemoveRootPath() throws PlainMapException {
        PlainMap plainMap = new PlainMap();
        plainMap.put("root.parent.child1", "value1");
        plainMap.put("root.parent.child2", "value2");
        plainMap.put("root.other", "value3");

        plainMap.removeRootPath("root.parent");
        Assert.assertEquals("value1", plainMap.get("child1"));
        Assert.assertEquals("value2", plainMap.get("child2"));
        Assert.assertFalse(plainMap.exists("root.other"));

        plainMap.removeRootPath("child2");
        Assert.assertEquals("value2", plainMap.get(""));
        Assert.assertFalse(plainMap.exists("child1"));

        plainMap.removeRootPath("invalid");
        Assert.assertTrue(plainMap.getHierarchicalMap().isEmpty());
    }

    @Test
    public void testGetParent() throws PlainMapException {
        PlainMap plainMap = new PlainMap();
        plainMap.put("root.child1", "value1");
        plainMap.put("root.child2", "value2");
        Assert.assertEquals("root", plainMap.getParent());
        // Verify the root element is not actually extracted from the plainmap
        Assert.assertEquals("root", plainMap.getParent());

        plainMap.put("otherroot.child3", "value3");
        try {
            plainMap.getParent();
            Assert.fail("It should have raised a PlainMapException because there are 2 root elements");
        } catch (PlainMapException e) {
            Assert.assertEquals("There is not one and only one parent element in the document map", e.getMessage());
        }
    }

    @Test
    public void testExtractParent() throws PlainMapException {
        PlainMap plainMap = new PlainMap();
        plainMap.put("root.child1", "value1");
        plainMap.put("root.child2", "value2");
        plainMap.extractParent();
        Assert.assertEquals("value1", plainMap.get("child1"));
        Assert.assertEquals("value2", plainMap.get("child2"));

        plainMap = new PlainMap();
        plainMap.put("root", "value");
        plainMap.extractParent();
        Assert.assertEquals("value", plainMap.get(""));
    }


}