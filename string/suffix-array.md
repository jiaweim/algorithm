# 后缀数组

## 简介

假设 $s$ 是长度为 $n$ 的字符串。那么 $s$ 的第 $i$ 个后缀为 $s[i...n-1]$。

将字符串的所有后缀进行排序，后缀的起始索引序列就是**后缀数组**（suffix array）。

以字符串 $s=abaab$ 为例，其后缀为：

| 起始索引 | 后缀 |
| ---- | ---- |
| 0 | abaab |
| 1 | baab |
| 2 | aab |
| 3 | ab |
| 4 | b |
按索引排序：

| 起始索引 | 后缀 |
| ---- | ---- |
| 2 | aab |
| 3 | ab |
| 0 | abaab |
| 4 | b |
| 1 | baab |

因此，后缀数组为 `(2, 3, 0, 4, 1)`。

作为一个数据结构，后缀数组在数据压缩、生物信息学以及字符串处理和匹配问题中有着广泛引用。

## 构建

### $O(n^2logn)$ 方法

这是最直观的做法。即获得所有后缀，然后通过 quicksort 或 mergesort 进行排序，同时保留原始索引。排序需要 $O(nlogn)$ 次比较，而比较两个字符串选哟 $O(n)$ 时间，因此最终复杂度为 $O(n^2logn)$。

### $O(nlogn)$ 方法

严格来说，下面的算法不是对后缀排序，而是对字符串的循环位移进行排序。但是很容易从中推导出一个后缀排序算法：只需要在字符串末尾添加一个特殊字符。通常用 `$`。例如，对字符串 `dabbb`：

| 起始索引 | 循环位移字符串 | 对应后缀 |
| ---- | ---- | ---- |
| 1 | abbb$d | abbb |
| 4 | b$dabb | b |
| 3 | bb$dab | bb |
| 2 | bbb$da | bbb |
| 0 | dabbb$ | dabbb |

我们用 $s[i...j]$ 表示字符串 $s$  的循环子串（cyclic substring），其中 `i` 可以大于 `j`。此时，实际的字符串为 $s[i...n-1]+s[0...j]$。此外，所有索引值对字符串 `s` 的长度取模，为了简单起见，后面省略取模操作。

下面讨论的算法将执行 $\lceil \log n\rceil+1$ 次迭代。在第 $k$ 次迭代（$k=0\dots \lceil \log n\rceil$），

## libsais

libsais 是一个用于快速（线性时间）构建后缀数组、LCP和 Burrows-Wheeler transform 的 C 语言库。采用如下论文的诱导排序（induced sorting）算法：

- Ge Nong, Sen Zhang, Wai Hong Chan *Two Efficient Algorithms for Linear Suffix Array Construction*, 2009
- Juha Karkkainen, Giovanni Manzini, Simon J. Puglisi *Permuted Longest-Common-Prefix Array*, 2009
- Nataliya Timoshevskaya, Wu-chun Feng *SAIS-OPT: On the characterization and optimization of the SA-IS algorithm for suffix array construction*, 2014
- Jing Yi Xie, Ge Nong, Bin Lao, Wentao Xu *Scalable Suffix Sorting on a Multicore Machine*, 2020

libsais 提供 C99 API。

### 算法描述

libsais 使用 SA-IS（suffix array induced sorting）算法，通过递归分解和诱导排序构建后缀数组和 Burrows-Wheeler 变换：

- 

## 算法选择

目前最快的算法为：

- libsais: CPU，其次为 divsufsort
- [libcubwt](https://github.com/IlyaGrebnov/libcubwt): GPU


## 参考

- https://cp-algorithms.com/string/suffix-array.html
- https://github.com/IlyaGrebnov/libsais/blob/master/Benchmarks.md
- 《Algorithms on Strings》