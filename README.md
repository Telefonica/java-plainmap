# plainmap

Java library to access elements in a hierarchical map as a simple map.

The library simplifies the construction and access of complex Java maps, with several levels or hierarchies, using a simple notation where each level is separated by `.`.

## Operations

| Operation | Description |
| --------- | ----------- |
| put | Put a value in the hierarchical map by a plain map path. |
| get | Get an element of the hierarchical map by a plain map path. |
| exists | Check if the path specified actually exists in the plain map. |
| clear | Clear the plain map. |
| remove | Remove an element (or elements) under the path specified. |
| getParent | Get the parent element name. |
| extractParent | Extract the parent element and returns the parent element name. |
| addRootPath | Add a root path to every element in the map. |
| removeRootPath | Remove a root path reducing the hierarchy of the map. |
| getHierarchicalMap | Return a Map<String, Object> represented by the plain map. |
| getPlainMap | Return a Map<String, String> where each key corresponds to a plain map path to each leave value. |

## Basic usage

### Creating a hierarchical map

It simplifies the creation of objects and arrays in an object, especially when it has several levels.

The following sample creates a simple map: `{parent={child=childValue}}`:

```java
Map<String, Object> parentMap = new HashMap<>();
Map<String, Object> childMap = new HashMap<>();
childMap.put("child", "childValue");
parentMap.put("parent", childMap);
```

But using plainmap library:

```java
PlainMap plainMap = new PlainMap();
plainMap.put("child.parent", "childValue");
Map<String, Object> parentMap = plainMap.getHierarchicalMap();
```

A more complex example, also involving an array:

```java
import es.tid.neosdp.plainmap.PlainMap;
import es.tid.neosdp.plainmap.PlainMapException;

public class CreateMap {

    public static Map main(String[] args) {
        try {
            PlainMap plainMap = new PlainMap();
            plainMap.put("rootElement", "A");
            plainMap.put("array[0]", 0);
            plainMap.put("array[2]", 2);
            plainMap.put("array[3].child", "testChild");
            // Print:
            // {rootElement=A, array=[0, null, 2, {child=testChild}]}
            System.out.println(plainMap.getHierarchicalMap());
        } catch (PlainMapException e) {
        }
    }

}
```

### Accessing a hierarchical map

It is possible to access an element in the plainmap with the same notation. For example, considering the previous map with content: `{parent={child=childValue}}`:

```java
PlainMap plainMap = new PlainMap(parentMap);
String childValue = (String) plainMap.get("parent.child");
```

## License

Copyright 2015 [Telefónica Investigación y Desarrollo, S.A.U](http://www.tid.es)

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
