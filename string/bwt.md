# BWT 和 FM Index

## BWT

BWT（Burrows-Wheeler Transform）是一种将字符串 $T$ 转换为另一个字符串 $BWT(T)$ 的方法。这种转换是可逆的。BWT 最初由 David Wheeler 于 1983 年发现，并由 Michael Burrows 和 David Wheeler 于 1994 年出版。

BWT 有两个主要应用：压缩和索引。

下面介绍从 $T$ 到 $BWT(T)$ 的转换。

### 通过 BWM 实现 BWT

设 $T$ 为要转换的字符串，$m=|T|$ 为字符串 $T$ 的长度。令 $T$ 以特殊终止符结尾，该字符不在 $T$ 中出现，且字典排序优于其它字符，记为 `$`。例如，对 DNA 字符串，带 `$` 的字段顺序为 $<A<C<G<T。

取 $T=abaaba\$$。首先，写下 $T$ 的旋转，即反复从开头取一个字符放到末尾：

```
$ a b a a b a
a $ a b a a b
b a $ a b a a
a b a $ a b a
a a b a $ a b
b a a b a $ a
a b a a b a $
```

得到一个 $m\times m$ 矩阵。然后按字母顺序排序：

```
$ a b a a b a
a $ a b a a b
a a b a $ a b
a b a $ a b a
a b a a b a $
b a $ a b a a
b a a b a $ a
```

这就是 Burrows-Wheeler Matrix (BWM(T))。BWM(T) 的最后一列，从上到下，就是 BWT(T)。

因此，对 $T=abaaba\$$，其 $BWT(T)=abba\$aa$。

### 通过 suffix array 实现 BWT

BWM 看上去与后缀数组类似：将 $T$ 的后缀排序得到后缀数组 $SA(T)$，将 $T$ 的旋转排序得到 $BWM(T)$。如下所示：

```
BWM       SA: Suffixes given by SA
$abaaba 6 $
a$abaab 5 a$
aaba$ab 2 aaba$
aba$aba 3 aba$
abaaba$ 0 abaaba$
ba$abaa 4 ba$
baaba$a 1 baaba$
```

可以发现，它们的顺序完全相同。所以，后缀数组 $SA(T)$ 是定义 $BWT(T)$ 的另一种方式。

令 $BWT[i]$ 为 $BWT(T)$ 中第 $i$  个字符（0-based），令 $SA[i]$ 为 $SA(T)$ 的第 $i$ 个后缀（0-based），则有：

$$
BWT[i]=\begin{cases}
    T[SA[i]-1] \quad&\text{if } SA[i]>0\\
    \$\quad&\text{if } SA[i]=0
\end{cases}
$$
构建 $BWT(T)$ 的 python 代码：

```python
def suffixArray(s):
    """ Given T return suffix array SA(T). We use Python’s sorted
    function here for simplicity, but we can do better. """
    # Empty suffix ’’ plays role of $.
    satups = sorted([(s[i:], i) for i in xrange(0, len(s)+1)])
    # Extract and return just the offsets
    return map(lambda x: x[1], satups)

def bwt(t):
    """ Given T, returns BWT(T), by way of the suffix array. """
    bw = []
    for si in suffixArray(t):
        if si == 0:
        	bw.append(’$’)
        else:
        	bw.append(t[si-1])
    return ’’.join(bw) # return string-ized version of list bw
```

### BWT 用于压缩

BWT 如何用于压缩？

- 首先，压缩必须是可逆的，以便压缩后能够解压缩。
- 其次，
