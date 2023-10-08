# 符号表

- [符号表](#符号表)
  - [简介](#简介)
  - [API](#api)
  - [Ordered symbol table](#ordered-symbol-table)
  - [Sample client](#sample-client)
    - [Test client](#test-client)
    - [Performance client](#performance-client)
  - [Sequential search in an unordered linked list](#sequential-search-in-an-unordered-linked-list)
  - [Binary search in an ordered array](#binary-search-in-an-ordered-array)
    - [binary search 性能](#binary-search-性能)
  - [总结](#总结)

Last updated: 2023-10-08, 17:46
@author Jiawei Mao
****

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

实现 `Iterable<Key>` 以支持迭代所有的 keys 和 values。对 symbol-table，我们采用一个更简单的替代方法，提供一个 `keys()` 方法，返回一个 `Iterable<Key>` 对象，用于迭代 keys。

**Key equality**

为了在 symbol-table 能够判断 key 的相等性，所有 key 需要实现 equals() 方法。并且 Key 最好为 immutable 类型。

## Ordered symbol table

在典型应用中，keys 为 Comparable 对象，因此可以使用 a.compareTo(b) 来比较两个 keys a 和 b。

有一些 symbol-table 实现利用 Comparable 提供的 key 的顺序实现高效的 put() 和 get() 操作。在这种实现中，我们可以认为 symbol-table 中 keys 是按顺序保存的。

对 Comparable 类型的 keys，实现如下 API：

|`public class ST<Key extends Comparable<Key>, Value>`||
|---|---|
|ST()|create an ordered symbol table|
|void put(Key key, Value val)|put key-value pair into the table (remove key from table if value is null)|
|Value get(Key key)|value paired with key (null if key is absent)|
|void delete(Key key)|remove key (and its value) from table|
|boolean contains(Key key)|is there a value paired with key?|
|boolean isEmpty()|is the table empty?|
|int size()|number of key-value pairs|
|Key min()|smallest key|
|Key max()|largest key|
|Key floor(Key key)|largest key less than or equal to key|
|Key ceiling(Key key)|smallest key greater than or equal to key|
|int rank(Key key)|number of keys less than key|
|Key select(int k)|key of rank k|
|void deleteMin()|delete smallest key|
|void deleteMax()|delete largest key|
|int size(Key lo, Key hi)|number of keys in [lo..hi]|
|Iterable<Key> keys(Key lo, Key hi)|keys in [lo..hi], in sorted order|
|Iterable<Key> keys()|all keys in the table, in sorted order|

快捷方法的默认实现：

```java
void deleteMin(){
    delete(min());
}
void deleteMax(){
    delete(max());
}
int size(Key lo, Key hi){
    if (hi.compareTo(lo) < 0)
        return 0;
    else if (contains(hi))
        return rank(hi) - rank(lo) + 1;
    else
        return rank(hi) - rank(lo);
}
Iterable<Key> keys(){
    return keys(min(), max());
}
```

## Sample client

考虑实现两个客户端：

- test client，用于跟踪算法在小型输入上的行为
- performance client，用于对比算法性能

### Test client

从标准输入读取一系列字符串，构建一个符号表。

```java
public static void main(String[] args)
{
    ST<String, Integer> st = new ST<String, Integer>();
    for (int i = 0; !StdIn.isEmpty(); i++)
    {
        String key = StdIn.readString();
        st.put(key, i);
    }
    for (String s : st.keys())
        StdOut.println(s + " " + st.get(s));
}
```

@import "images/2023-10-08-10-09-55.png" {width="400px" title=""}

### Performance client

`FrequencyCounter` 用于查看来自标准输入的字符串序列中每个字符串出现的次数，然后遍历 keys 找到出现频率最高的字符串。

我们使用三个输入来测试性能：

- C. Dickens 的 《Tale of Two Cities》前 5  行（tinyTale.txt）和全文 (tale.txt)
- 包含 100 万个句子的数据库 Leipzig Corpora Collection (leipzig1M.txt)

例如，下面是 tinyTale.txt 的内容：

```sh
% more tinyTale.txt
it was the best of times it was the worst of times
it was the age of wisdom it was the age of foolishness
it was the epoch of belief it was the epoch of incredulity
it was the season of light it was the season of darkness
it was the spring of hope it was the winter of despair
```

该文本包含 60 个单词，有 20 个不同的单词，其中 4 个出现了 10 次（频率最高）。

@import "images/2023-10-08-10-30-55.png" {width="600px" title=""}

```java
public class FrequencyCounter {

    // Do not instantiate.
    private FrequencyCounter() { }

    /**
     * Reads in a command-line integer and sequence of words from
     * standard input and prints out a word (whose length exceeds
     * the threshold) that occurs most frequently to standard output.
     * It also prints out the number of words whose length exceeds
     * the threshold and the number of distinct such words.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        int distinct = 0, words = 0;
        int minlen = Integer.parseInt(args[0]);
        ST<String, Integer> st = new ST<String, Integer>();

        // compute frequency counts
        while (!StdIn.isEmpty()) {
            String key = StdIn.readString();
            if (key.length() < minlen) continue;
            words++;
            if (st.contains(key)) {
                st.put(key, st.get(key) + 1);
            }
            else {
                st.put(key, 1);
                distinct++;
            }
        }

        // find a key with the highest frequency count
        String max = "";
        st.put(max, 0);
        for (String word : st.keys()) {
            if (st.get(word) > st.get(max))
                max = word;
        }

        StdOut.println(max + " " + st.get(max));
        StdOut.println("distinct = " + distinct);
        StdOut.println("words    = " + words);
    }
}
```

```sh
% java FrequencyCounter 1 < tinyTale.txt
it 10
% java FrequencyCounter 8 < tale.txt
business 122
% java FrequencyCounter 10 < leipzig1M.txt
government 24763
```

该客户端和示例提出的基本问题：我们能否开发一种符号表实现，在一个有大量 get() 和 put() 操作构建的大型表上处理大量的 get() 操作？如果只进行少量搜索，任何实现都可以，但如果没有良好的符号表实现，就不能使用 FrequencyCounter 这样的客户端来解决大型问题。FrequencyCounter 是一种非常常见的场景。它具有如下特征：

- 搜索和插入操作混合
- distinct keys 数量多
- 搜索的次数可能比插入多得多
- 搜索和插入模式虽然不能预测，但不是随机的

我们的目标是实现此类符号表，实现高性能的插入和搜索操作。

## Sequential search in an unordered linked list

实现 symbol-table 的最直观的方法是使用 linkedList。如下所示：

```java
public class SequentialSearchST<Key, Value> {

    private int n;           // number of key-value pairs
    private Node first;      // the linked list of key-value pairs

    // a helper linked list data type
    private class Node {

        private Key key;
        private Value val;
        private Node next;

        public Node(Key key, Value val, Node next) {
            this.key = key;
            this.val = val;
            this.next = next;
        }
    }

    public SequentialSearchST() {}

    /**
     * Returns the number of key-value pairs in this symbol table.
     *
     * @return the number of key-value pairs in this symbol table
     */
    public int size() {
        return n;
    }

    /**
     * Returns true if this symbol table is empty.
     *
     * @return {@code true} if this symbol table is empty;
     * {@code false} otherwise
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Returns true if this symbol table contains the specified key.
     *
     * @param key the key
     * @return {@code true} if this symbol table contains {@code key};
     * {@code false} otherwise
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public boolean contains(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to contains() is null");
        return get(key) != null;
    }

    /**
     * Returns the value associated with the given key in this symbol table.
     *
     * @param key the key
     * @return the value associated with the given key if the key is in the symbol table
     * and {@code null} if the key is not in the symbol table
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public Value get(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to get() is null");
        for (Node x = first; x != null; x = x.next) {
            if (key.equals(x.key))
                return x.val;
        }
        return null;
    }

    /**
     * Inserts the specified key-value pair into the symbol table, overwriting the old
     * value with the new value if the symbol table already contains the specified key.
     * Deletes the specified key (and its associated value) from this symbol table
     * if the specified value is {@code null}.
     *
     * @param key the key
     * @param val the value
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public void put(Key key, Value val) {
        if (key == null) throw new IllegalArgumentException("first argument to put() is null");
        if (val == null) {
            delete(key);
            return;
        }

        for (Node x = first; x != null; x = x.next) {
            if (key.equals(x.key)) {
                x.val = val;
                return;
            }
        }
        first = new Node(key, val, first);
        n++;
    }

    /**
     * Removes the specified key and its associated value from this symbol table
     * (if the key is in this symbol table).
     *
     * @param key the key
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public void delete(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to delete() is null");
        first = delete(first, key);
    }

    // delete key in linked list beginning at Node x
    // warning: function call stack too large if table is large
    private Node delete(Node x, Key key) {
        if (x == null) return null;
        if (key.equals(x.key)) {
            n--;
            return x.next;
        }
        x.next = delete(x.next, key);
        return x;
    }


    /**
     * Returns all keys in the symbol table as an {@code Iterable}.
     * To iterate over all of the keys in the symbol table named {@code st},
     * use the foreach notation: {@code for (Key key : st.keys())}.
     *
     * @return all keys in the symbol table
     */
    public Iterable<Key> keys() {
        Queue<Key> queue = new Queue<Key>();
        for (Node x = first; x != null; x = x.next)
            queue.enqueue(x.key);
        return queue;
    }


    /**
     * Unit tests the {@code SequentialSearchST} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        SequentialSearchST<String, Integer> st = new SequentialSearchST<String, Integer>();
        for (int i = 0; !StdIn.isEmpty(); i++) {
            String key = StdIn.readString();
            st.put(key, i);
        }
        for (String s : st.keys())
            StdOut.println(s + " " + st.get(s));
    }
}
```

- get

为了实现 `get()` 方法，我们扫描整个列表，使用 `equals()` 来比较搜索 key 和列表中每个 `Node` 的 key。如果找到匹配项，就返回关联的值；如果没有，返回 `null`。

```java
public Value get(Key key) {
    if (key == null) throw new IllegalArgumentException("argument to get() is null");
    for (Node x = first; x != null; x = x.next) {
        if (key.equals(x.key))
            return x.val;
    }
    return null;
}
```

- put

实现 `put()` 方法也需要扫描整个列表，使用 equals() 比较 search key 和列表中每个 node 的 key。如果匹配到，则更新与该 key 关联的值；如果没有，则使用 key 和 val 创建新的 node，插入到列表开头。

```java
public void put(Key key, Value val) {
    if (key == null) throw new IllegalArgumentException("first argument to put() is null");
    if (val == null) {
        delete(key);
        return;
    }

    for (Node x = first; x != null; x = x.next) {
        if (key.equals(x.key)) {
            x.val = val;
            return;
        }
    }
    first = new Node(key, val, first);
    n++;
}
```

@import "images/2023-10-08-14-07-57.png" {width="800px" title=""}

下面用属于 *search hit* 表示搜索成功，*search miss* 表示搜索失败。

**命题 A**

在无序链表中，Search miss 和插入都需要 N 次比较，search hit 在最坏情况需要 N 次比较。在一个初始为空的链表 symbol-table 中插入 N 个不同的 key 需要 ~$N^2/2$ 次比较。

该分析表明，使用 sequential search 的 linked-list 实现太慢了，无法用于解决大型问题，如前面介绍的 FrequencyCounter。总的比较次数正比于搜索次数和插入次数的乘积，对 《Tale of Two Cities》比较次数超过 $10^9$，对《Leipzig Corpora》超过 $10^{14}$。

@import "images/2023-10-08-16-32-34.png" {width="600px" title=""}

!!! summary
    在无序链表实现中，symbol-table 的搜索和插入操作的复杂度为 $N^2$，性能差，不适合大规模数据。

## Binary search in an ordered array

使用一对并行数组实现有序 symbol-table。

其中 key 必须为 Comparable 类型，然后使用数组索引实现 `get()` 和其他操作的快速实现。

```java
public class BinarySearchST<Key extends Comparable<Key>, Value> {

    private Key[] keys;
    private Value[] vals;
    private int n = 0;

    /**
     * Initializes an empty symbol table with the specified initial capacity.
     *
     * @param capacity the maximum capacity
     */
    public BinarySearchST(int capacity) {
        keys = (Key[]) new Comparable[capacity];
        vals = (Value[]) new Object[capacity];
    }

    // resize the underlying arrays
    private void resize(int capacity) {
        assert capacity >= n;
        Key[] tempk = (Key[]) new Comparable[capacity];
        Value[] tempv = (Value[]) new Object[capacity];
        for (int i = 0; i < n; i++) {
            tempk[i] = keys[i];
            tempv[i] = vals[i];
        }
        vals = tempv;
        keys = tempk;
    }

    /**
     * @return the number of key-value pairs in this symbol table
     */
    public int size() {
        return n;
    }

    /**
     * @return {@code true} if this symbol table is empty;
     * {@code false} otherwise
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    public boolean contains(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to contains() is null");
        return get(key) != null;
    }

    /**
     * Returns the value associated with the given key in this symbol table.
     *
     * @param key the key
     * @return the value associated with the given key if the key is in the symbol table
     * and {@code null} if the key is not in the symbol table
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public Value get(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to get() is null");
        if (isEmpty()) return null;
        int i = rank(key);
        if (i < n && keys[i].compareTo(key) == 0) return vals[i];
        return null;
    }

    /**
     * Returns the number of keys in this symbol table strictly less than {@code key}.
     *
     * @param key the key
     * @return the number of keys in the symbol table strictly less than {@code key}
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public int rank(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to rank() is null");

        // binary search
        int lo = 0, hi = n - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            int cmp = key.compareTo(keys[mid]);
            if (cmp < 0) hi = mid - 1;
            else if (cmp > 0) lo = mid + 1;
            else return mid;
        }
        return lo;
    }


    /**
     * Inserts the specified key-value pair into the symbol table, overwriting the old
     * value with the new value if the symbol table already contains the specified key.
     * Deletes the specified key (and its associated value) from this symbol table
     * if the specified value is {@code null}.
     *
     * @param key the key
     * @param val the value
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public void put(Key key, Value val) {
        if (key == null) throw new IllegalArgumentException("first argument to put() is null");

        if (val == null) {
            delete(key);
            return;
        }

        int i = rank(key);

        // key is already in table
        if (i < n && keys[i].compareTo(key) == 0) {
            vals[i] = val;
            return;
        }

        // insert new key-value pair
        if (n == keys.length) resize(2 * keys.length);

        for (int j = n; j > i; j--) {
            keys[j] = keys[j - 1];
            vals[j] = vals[j - 1];
        }
        keys[i] = key;
        vals[i] = val;
        n++;

        assert check();
    }

    /**
     * Removes the specified key and associated value from this symbol table
     * (if the key is in the symbol table).
     *
     * @param key the key
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public void delete(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to delete() is null");
        if (isEmpty()) return;

        // compute rank
        int i = rank(key);

        // key not in table
        if (i == n || keys[i].compareTo(key) != 0) {
            return;
        }

        for (int j = i; j < n - 1; j++) {
            keys[j] = keys[j + 1];
            vals[j] = vals[j + 1];
        }

        n--;
        keys[n] = null;  // to avoid loitering
        vals[n] = null;

        // resize if 1/4 full
        if (n > 0 && n == keys.length / 4) resize(keys.length / 2);

        assert check();
    }

    /**
     * Removes the smallest key and associated value from this symbol table.
     *
     * @throws NoSuchElementException if the symbol table is empty
     */
    public void deleteMin() {
        if (isEmpty()) throw new NoSuchElementException("Symbol table underflow error");
        delete(min());
    }

    /**
     * Removes the largest key and associated value from this symbol table.
     *
     * @throws NoSuchElementException if the symbol table is empty
     */
    public void deleteMax() {
        if (isEmpty()) throw new NoSuchElementException("Symbol table underflow error");
        delete(max());
    }

    /**
     * Returns the smallest key in this symbol table.
     *
     * @return the smallest key in this symbol table
     * @throws NoSuchElementException if this symbol table is empty
     */
    public Key min() {
        if (isEmpty()) throw new NoSuchElementException("called min() with empty symbol table");
        return keys[0];
    }

    /**
     * Returns the largest key in this symbol table.
     *
     * @return the largest key in this symbol table
     * @throws NoSuchElementException if this symbol table is empty
     */
    public Key max() {
        if (isEmpty()) throw new NoSuchElementException("called max() with empty symbol table");
        return keys[n - 1];
    }

    /**
     * Return the kth smallest key in this symbol table.
     *
     * @param k the order statistic
     * @return the {@code k}th smallest key in this symbol table
     * @throws IllegalArgumentException unless {@code k} is between 0 and
     *                                  <em>n</em>–1
     */
    public Key select(int k) {
        if (k < 0 || k >= size()) {
            throw new IllegalArgumentException("called select() with invalid argument: " + k);
        }
        return keys[k];
    }

    /**
     * Returns the largest key in this symbol table less than or equal to {@code key}.
     *
     * @param key the key
     * @return the largest key in this symbol table less than or equal to {@code key}
     * @throws NoSuchElementException   if there is no such key
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public Key floor(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to floor() is null");
        int i = rank(key);
        if (i < n && key.compareTo(keys[i]) == 0) return keys[i];
        if (i == 0) throw new NoSuchElementException("argument to floor() is too small");
        else return keys[i - 1];
    }

    /**
     * Returns the smallest key in this symbol table greater than or equal to {@code key}.
     *
     * @param key the key
     * @return the smallest key in this symbol table greater than or equal to {@code key}
     * @throws NoSuchElementException   if there is no such key
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public Key ceiling(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to ceiling() is null");
        int i = rank(key);
        if (i == n) throw new NoSuchElementException("argument to ceiling() is too large");
        else return keys[i];
    }

    /**
     * Returns the number of keys in this symbol table in the specified range.
     *
     * @param lo minimum endpoint
     * @param hi maximum endpoint
     * @return the number of keys in this symbol table between {@code lo}
     * (inclusive) and {@code hi} (inclusive)
     * @throws IllegalArgumentException if either {@code lo} or {@code hi}
     *                                  is {@code null}
     */
    public int size(Key lo, Key hi) {
        if (lo == null) throw new IllegalArgumentException("first argument to size() is null");
        if (hi == null) throw new IllegalArgumentException("second argument to size() is null");

        if (lo.compareTo(hi) > 0) return 0;
        if (contains(hi)) return rank(hi) - rank(lo) + 1;
        else return rank(hi) - rank(lo);
    }

    /**
     * Returns all keys in this symbol table as an {@code Iterable}.
     * To iterate over all of the keys in the symbol table named {@code st},
     * use the foreach notation: {@code for (Key key : st.keys())}.
     *
     * @return all keys in this symbol table
     */
    public Iterable<Key> keys() {
        return keys(min(), max());
    }

    /**
     * Returns all keys in this symbol table in the given range,
     * as an {@code Iterable}.
     *
     * @param lo minimum endpoint
     * @param hi maximum endpoint
     * @return all keys in this symbol table between {@code lo}
     * (inclusive) and {@code hi} (inclusive)
     * @throws IllegalArgumentException if either {@code lo} or {@code hi}
     *                                  is {@code null}
     */
    public Iterable<Key> keys(Key lo, Key hi) {
        if (lo == null) throw new IllegalArgumentException("first argument to keys() is null");
        if (hi == null) throw new IllegalArgumentException("second argument to keys() is null");

        Queue<Key> queue = new Queue<Key>();
        if (lo.compareTo(hi) > 0) return queue;
        for (int i = rank(lo); i < rank(hi); i++)
            queue.enqueue(keys[i]);
        if (contains(hi)) queue.enqueue(keys[rank(hi)]);
        return queue;
    }

    private boolean check() {
        return isSorted() && rankCheck();
    }

    // are the items in the array in ascending order?
    private boolean isSorted() {
        for (int i = 1; i < size(); i++)
            if (keys[i].compareTo(keys[i - 1]) < 0) return false;
        return true;
    }

    // check that rank(select(i)) = i
    private boolean rankCheck() {
        for (int i = 0; i < size(); i++)
            if (i != rank(select(i))) return false;
        for (int i = 0; i < size(); i++)
            if (keys[i].compareTo(select(rank(keys[i]))) != 0) return false;
        return true;
    }

    public static void main(String[] args) {
        BinarySearchST<String, Integer> st = new BinarySearchST<String, Integer>();
        for (int i = 0; !StdIn.isEmpty(); i++) {
            String key = StdIn.readString();
            st.put(key, i);
        }
        for (String s : st.keys())
            StdOut.println(s + " " + st.get(s));
    }
}
```

该实现的核心是 `rank()` 方法，该方法返回小于给定 key 的 keys 数：

- 对 `get()`，如果 key 在 table 中，rank 返回 key 的位置。
- 对 `put()`，如果 key 在 table 中，rank 给出需要更新值的位置；如果 key 不在 table 中，rank 给出插入 key 的位置。

下面是 rank 的等价递归实现：

```java
public int rank(Key key, int lo, int hi)
{
    if (hi < lo) return lo;
    int mid = lo + (hi - lo) / 2;
    int cmp = key.compareTo(keys[mid]);
    if (cmp < 0)
        return rank(key, lo, mid-1);
    else if (cmp > 0)
        return rank(key, mid+1, hi);
    else return mid;
}
```

该递归实现和上面的 while 实现执行相同的比较，但是递归版本能更好理解算法结构。递归 `rank()` 的属性：

- 如果 key 在 table 中，`rank()` 返回该 key 在 table 中的索引，等于 table 中小于 key 的 keys 数
- 如果 key 不在 table 中，rank() 依然返回 table 中小于 key 的 keys 数

### binary search 性能

在包含 N 个 keys 的有序数组中，binary search 的对比次数不超过 $lgN+1$。各个方法的消耗如下：

|method|order of growth of running time|
|---|---|
|put()|N|
|get()|log N|
|delete()|N|
|contains()|log N|
|size()|1|
|min()|1|
|max()|1|
|floor()|log N|
|ceiling()|log N|
|rank()|log N|
|select()|1|
|deleteMin()|N|
|deleteMax()|1|

尽管 BinarySearchST 保证了对数搜索，但它仍然不能解决 `FrequencyCounter` 这样的大型问题。因为 put() 方法太慢了。binary search 减少了比较次数，但没有减少运行时间，因为构建有序 symbol-table 的消耗更大。

@import "images/2023-10-08-16-33-29.png" {width="600px" title=""}

《Tale of Two Cities》包含大概 $10^4$ distinct keys，构建 symbol-table 需要大概 $10^8$ 次数组访问；Leipzig 则包含 $10^6$ distinct keys，构建 symbol-table 需要 $10^{11}$ 次数组访问。该成本非常高，而且没必要。

回到 FrequencyCounter 对长度为 8 及以上单词进行 put() 操作的成本，平均成本从 SequentialSearchST 的 2246 降低到 BinarySearchST 的 484。这一改进效果很好，但是我们还可以做得更好。

## 总结

binary search 通常比 sequential search 好得多，在许多实际应用中是首选方法。

不过 binary-search 在许多应用中不可行，如 search 和 insert 操作混合、并且 table 太大的情况。现代的搜索客户端需要能够支持搜索和插入的快速实现。即需要能够构建巨大的 table，并且 search 和 insert 混合操作性能很好。

|algorithm (data structure)|worst-case cost(after N inserts)||average-case cost (after N random inserts)||efficiently support ordered operations?|
|---|---|---|---|---|---|
||search|insert|search hit|insert|
|sequential search (unordered linked list)|N|N|N/2|N|no|
|binary search (ordered array)|lg N|2N|lg N|N|yes|

我们是否能实现 search 和 insert 操作时间复杂度都为 lg 的算法？答案是肯定的，快速 symbol-table search/insert 是算法最重要的贡献之一。

为了支持有效的插入，我们似乎需要实现一个链接结构。但是单个 linked-list 无法使用 binary-search，因为 binary-search 依赖于获取数组中间值的能力。为了将 binary-search 和效率和链接结构的灵活性结合起来，我们需要更复杂的数据结构。binary-search-tree 和 hash-table 提供了该组合功能。

下面列出 6 种数据结构，对应 6 种 symbol-table 实现。

|底层数据结构|实现|优点|缺点|
|---|---|---|---|
|linked list (sequential search)|SequentialSearchST|best for tiny STs|slow for large STs|
|ordered array (binary search)|BinarySearchST|optimal search and space, order-based ops|slow insert|
|二叉搜索树|BST|easy to implement, order-based ops|no guarantees space for links|
|balanced BST|RedBlackBST|optimal search and insert, order-based ops|space for links|
|hash table|SeparateChainingHashST, LinearProbingHashST|fast search/insert for common types of data|need hash for each type no order-based ops space for links/empty|

