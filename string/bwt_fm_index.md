# Burrows-Wheeler Transform and FM Index

## BWT

Burrows-Wheeler Transform (BWT) 是一种将字符串 $T$ 的字符转换为另一个字符串 $BWT(T)$ 的方式。该变换是可逆的，最初由 David Wheeler 于 1983 年发现，并由 Michael Burrows 和 David Wheeler 于 1994 年出版。

BWT 主要有两类应用：压缩和索引。下面首先讨论从 $T$ 到 $BTW(T)$ 的变换。

### BWM 实现 BWT

设 $T$ 为要转换的字符串：

- $m=|T|$ 表示 $T$ 的长度
- 字符串末尾加上 `$` 表示结束，`$` 为 `T` 中没有出现过的字符
- 以 DNA 字符串为例，字母顺序为 `$<A<C<G<T`



## 参考

- https://www.langmead-lab.org/teaching.html
- https://www.youtube.com/playlist?list=PL2mpR0RYFQsBiCWVJSvVAO3OJ2t7DzoHA
- https://en.wikipedia.org/wiki/Burrows%E2%80%93Wheeler_transform