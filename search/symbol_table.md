# 符号表

## 简介

符号表（symbol-table）主要用来将 value 与 key 关联起来。用户可以向 symbol-table 插入 key-value 对，期望稍后能够从 symbol-table 中检索与指定 key 关联的 value。

实现 symbol-table 数据结构有多种，要求 insert 和 search 操作高效，并提供其它一些便捷操作。要实现 symbol-table，需要定义一个底层数据结构，然后为插入、搜索和其他创建和操作该数据结构的操作实现算法。

搜索对很多计算机应用都很重要，symbol-table 在很多编程环境中都是高级抽象。下表给出了一些在典型应用中可能使用的 key-value。

!!! definition
    symbol-table 是一种键值对结构，支持两种操作：插入（insert）和检索（search）。
    
|应用|搜索目的|key|value|
|---|---|---|---|
|dictionary|find definition|word|definition|
|book index|find relevant pages|term|list of page numbers|
|file share|find song to download|name of song|computer ID|
|account management|process transactions|account number|transaction details|
|web search|find relevant web pages|keyword|list of page names|
|compiler|find type and value|variable name|type and value|

## API

symbol-table 是典型的抽象数据类型：它表示一组定义良好的值和这些值上的操作，使我们能够分离客户端和实现。

下面用 API 定义客户端和实现之间的协议：

|`public class ST<Key, Value>`||
|---|---|
|ST()|create a symbol table|
|void put(Key key, Value val)|put key-value pair into the table (remove key from table if value is null)|
|Value get(Key key)|value paired with key (null if key is absent)|
|void delete(Key key)|remove key (and its value) from table|
|boolean contains(Key key)|is there a value paired with key?|
|boolean isEmpty()|is the table empty?|
|int size()|number of key-value pairs in the table|
|Iterable<Key> keys()|all the keys in the table|

**泛型（Generics）**

使用泛型以处理不同的元素类型，并通过显式指定 key 和 value 的类型来强调 key 和 value 在搜索中的独立作用。

**Duplicate keys**

在所有实现中采用以下约定：

- 每个 key 只关联一个 value，即 symbol-table 中没有重复 keys
- 当用户将一个 key-value 放入 table，如果 table 中已有该 key，则新 value 替换旧 value

这些约定定义了关联数组抽象，可以把 symbol-table 想象成一个数组，其中 key 使索引，value 为数组值。在传统数组中，key 使 integer 索引，用于快速访问数组值；在关联数组（symbol-table）中，key 使任意类型，但我们仍然可以使用它们来快速访问值。有些编程语言（不是 Java）为关联数组提供了特殊支持，程序员可以使用 `st[key]` 代替 `st.get(key)`，用 `st[key]=val` 代替 `st.put(key, value)`，其中 `key` 和 `value` 可以是任意类型。

**Null keys**

key 不能为 `null`。

**Null values**

key 对应的值不能为 `null`。该约定直接与 API 的实现有关，API 要求 `get()` 方法对 table 中不存在的 key 返回 `null`。该约定 有两个结果：首先，我们可以通过测试 `get()` 是否返回 null 来测试 symbol-table 是否包含与指定 key 关联的值；其次，在调用 `put()` 时将 null 作为第二个参数可以实现删除操作。

**Deletion**

symbol-table 中的删除操作有两种策略：

- lazy 删除，即将要删除的 key 的值设置为 null，然后在以后的某个时间删除所有这些 keys；
- eager 删除，立即从 table 中删除 key.

方法 `put(key, null)` 为 `delete(key)` 的 lazy 实现。`delete()` 则提供 eager 实现。

为了保证 lazy 实现的有效，在 `put()` 实现中添加防御代码：

```java
if (val == null) { delete(key); return; }
```

以确保 symbol-table 中没有与 null 关联的 key。

**其它快捷方法**

为了让客户端代码更清晰，在 API 中包含了 contains() 和 isEmpty() 方法，默认的单行实现如下。为了简介，接下来不重复这些代码，但假设在所有 symbol-table API 实现中都包含这些方法。

|method|default implementation|
|---|---|
|void delete(Key key)|put(key, null);|
|boolean contains(key)|return get(key) != null;|
|boolean isEmpty()|return size() == 0;|

**迭代**

