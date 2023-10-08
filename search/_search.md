# 搜索

## 概述

现代计算机和互联网使人们可以获得大量信息，有效检索这些信息的能力是处理这些信息的基础。下面介绍几十年来在众多不同应用中被证明有效的经典搜索算法。

我们使用符号表（symbol table）来描述保存信息的抽象机制，可以通过指定 key 来检索这些信息。key 和 value 的性质取决于应用。可能存在大量 keys 和大量信息，因此实现一个高效的 symbol-table 是一个巨大挑战。

- symbol-table 也被称为字典（dictionary）：在英语字典中，key 是单词，它的 value 是与该单词相关的条目，包括该单词的定义、发音和词源等。
- symbol-table 也被称为索引（index）：如教科书末尾按字母顺序列出术语在书中的位置，其中 key 是感兴趣的词，value 是页面列表，告诉读着在书的哪里可以找到该词。

下面介绍三种支持高效 symbol-table 实现的经典数据结构：二叉查找树（binary search tree）、红黑树（red-black tree）和哈希表（hash table）。

## TOC

- [符号表](./symbol_table.md)

## 参考

- 《Algorithms, 4ed, 2011》Robert Sedgewick & Kevin Wayne