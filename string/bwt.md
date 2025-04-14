# BWT 和 FM Index

2025-04-08⭐
@author Jiawei Mao
***

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

这就是 Burrows-Wheeler Matrix (BWM(T))。$BWM(T)$ 的最后一列，从上到下，就是 $BWT(T)$。

因此，对 $T=abaaba\$$，其 $BWT(T)=abba\$aa$。如下图所示：

<img src="./images/image-20250407112958970.png" alt="image-20250407112958970" style="zoom: 33%;" />

$BWT(T)$ 可以看作按右侧字符串（right-context）排序后的字符顺序。

### 通过 suffix array 实现 BWT

BWM 看上去与后缀数组类似：将 $T$ 的后缀排序得到后缀数组 $SA(T)$；而将 $T$ 的旋转排序得到 $BWM(T)$。如下所示：

<img src="./images/image-20250407142715876.png" alt="image-20250407142715876" style="zoom:50%;" />

可以发现，它们的顺序完全相同。这很好理解：注意 `$` 的位置，序列的排序取决于 `$` 及其之前的字符，完全不受 `$` 后面的字符影响。所以到 `$` BWM 与后缀数组的字符完全相同。所以，后缀数组 $SA(T)$ 提供了另一种构建 $BWT(T)$ 的方式。

令 $BWT[i]$ 为 $BWT(T)$ 中第 $i$  个字符（0-based），$SA[i]$ 为 $SA(T)$ 的第 $i$ 个后缀（0-based），则有：

$$
BWT[i]=\begin{cases}
    T[SA[i]-1] \quad&\text{if } SA[i]>0\\
    \$\quad&\text{if } SA[i]=0
\end{cases}
$$
即取后缀在 $T$ 的前一个字符。构建 $BWT(T)$ 的 python 代码：

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

- 首先，BWT 是可逆的：压缩使用的变换必须是可逆的，以便压缩后能够解压缩。
- 其次，BWT 使 $T$ 中具有类似 right-context 的字符聚集在一起。例如

```
>>> bwt("Tomorrow_and_tomorrow_and_tomorrow")
"w$wwdd__nnoooaattTmmmrrrrrrooo__ooo"
```

```
>>> bwt("It_was_the_best_of_times_it_was_the_worst_of_times$")
s$esttssfftteww_hhmmbootttt_ii__woeeaaressIi_______
```

这使得 $BWT(T)$ 更容易压缩。例如，可以采用 $BWT(T)$ 和游程编码（run-length encoding, RLE）进行可逆压缩。压缩软件采用多种方法来缩小 $BWT(T)$，如 move-to-front 变换、游程编码、Huffman 编码以及 arithmetic 编码等。

### 使用 LF-Mapping 实现 BWT 逆变换

如何实现 BWT 逆变换？

<img src="./images/image-20250407181631003.png" alt="image-20250407181631003" style="zoom: 33%;" />

BWT 有一个关键属性，即 LF-Mapping，该属性定义 BWM 第一列（F, First）和最后一列（L, Last）的关系。以 $T=abaaba\$$ 的 BWM 为例：

```
$ a b a a b a
a $ a b a a b
a a b a $ a b
a b a $ a b a
a b a a b a $
b a $ a b a a
b a a b a $ a
```

下面重写 $T$，对 `$` 以外的每个字符添加一个下标：$T=a_0b_0a_1a_2b_1a_3\$$。下标表示该字符在 $T$ 中已出现次数。例如，第一次出现的 $a$ 变为 $a_0$，第二次出现的 $c$ 变为 $c_1$。将下标的数字称为 **rank**。对 $ 不设置 rank，因为它只出现一次。

下面是包含 rank 的 BWM，rank 不影响字典排序。

<img src="./images/image-20250407183640007.png" alt="image-20250407183640007" style="zoom:50%;" />

只看第一列（F）和最后一列（L）的 a：

<img src="./images/image-20250407184213134.png" alt="image-20250407184213134" style="zoom:33%;" />

可以发现，在 F 和 L 中，$a$ 的顺序完全相同，都是 $a_3,a_1,a_2,a_0$。只看 b，结论一样：

<img src="./images/image-20250407184500020.png" alt="image-20250407184500020" style="zoom:33%;" />

**LF-Mapping**：字符 $c$ 在 L 第 $i$ 次出现的与字符 $c$ 在 F 中第 $i$ 次出现对应 $T$ 相同位置的字符，即具有相同 rank。

简而言之，不管字符 $c$ 的 rank 是多少，它在 F 和 L 中以相同顺序出现。

为什么 LF-Mapping 成立？

设 $M$ 为 $T$ 的 $BWM(T)$，令 $M'$ 为将 $M$ 的所有 rows 向右旋转一个位置后得到的矩阵。$M'$ 的第一列等于 $M$ 的最后一列。

<img src="./images/image-20250407185842485.png" alt="image-20250407185842485" style="zoom:33%;" />

L 旋转一次，到 F 位置，F 的所有字符以其 right-context 排序。相同字符的顺序必然与其右侧列的顺序一致。

下面重新调整字符排序，之前的排序成为 T-ranking。而根据 $BWT(T)$ 中相同字符出现的次数重新排序，成为 B-ranking。$BWT(T)$ 的 B-ranks 为：$a_0b_0b_1a_1\$a_2a_3$。如下图：

<img src="./images/image-20250407191521399.png" alt="image-20250407191521399" style="zoom:33%;" />

此时 F 的结构非常简单。如何从 $BWT(T)$ 逆变换为 $T$？

从第一行开始，第一个字符必然是 `$`，每一行都是 $T$ 的旋转，因此第一行的最后一列包含 `$` 左侧的字符，这里为 $a$，其 rank 为 0。现在，如何得到 $a_0$ 左边的字符呢？可以找以 $a_0$ 开头的 row，通过 LF-Mapping 可以知道，因为 $a_0$ 的 rank 为 0，因此必然对应 F 列的第一个 $a$，即 F 第二行的 $a$。因此跳到第二行，查看第二行的 L 列为 $b_0$，$b_0$ 对应 F 列第一个 $b$，转到第 6 行，依此类推，直到最后一列为 `$` 的行。对上例，按以下顺序访问 rows，假设第一行的索引为 0：$(0,1,5,3,2,6,4)$，从右到左重新创建原始字符串：$a_3b_1a_1a_2b_0a_0\$$。

假设 $T$ 包含 300 A, 400 C, 250 G, 700 T，并且 $\$<A<C<G<T$。那么 BWM 的哪一行以 $G_{100}$ 开头？（rank 为 B-rank），记住，这些 rows 都是排好序的：

- Skip row starting with  `$` (1 row)
- Skip rows starting with  A (300 rows)
- Skip rows starting with C (400 rows)
- Skip first 100 rows starting with G (100 rows)

因此，$G_{100}$ 是第 1+300+400+100=801 行。

现在就可以实现 $BWT(T)$ 的逆变换了：

<img src="./images/image-20250408111451218.png" alt="image-20250408111451218" style="zoom:33%;" />

下面是 Python 实现。目前，我们假设预先计算 $T$ 的 rank 是合理的。如果 $T$ 很长，就不合理了，因为 rank 占用的内存甚至比 $BWT(T)$ 更多。bzip2 等方法通过将文本分解为较小的 blocks 来弥补这一点。

```python
def rankBwt(bw):
    """ Given BWT string bw, returns a parallel list of B-ranks. Also
    returns tots, a mapping from characters to # times the
    character appears in BWT. """
    tots = dict()
    ranks = []
    for c in bw:
        if c not in tots:
        	tots[c] = 0
        ranks.append(tots[c])
        tots[c] += 1
    return ranks, tots

def firstCol(tots):
    """ Return a map from characters to the range of cells in the first
    column containing the character. """
    first = {}
    totc = 0
    for c, count in sorted(tots.iteritems()):
        first[c] = (totc, totc + count)
        totc += count
    return first

def reverseBwt(bw):
    """ Make T from BWT(T) """
    ranks, tots = rankBwt(bw)
    first = firstCol(tots)
    rowi = 0
    t = "$"
    while bw[rowi] != ’$’:
        c = bw[rowi]
        t = c + t
        rowi = first[c][0] + ranks[rowi]
    return t
```

## FM Index

In 2000, six years after the BWT was published, Paolo Ferragina and Giovanni Manzini published a paper describing how the BWT, together with some small auxilliary data structures, can be used as a space-efficient index of $T$。It generally uses less than half the space required to store $m$ integers. They named it the *FM Index*。Just as the LF Mapping was the key to understanding how the BWT is reversible, it's also the key to how it can be used as an index.

Though BWM is related to suffix array, we can't query it the same way:

<img src="./images/image-20250408144247393.png" alt="image-20250408144247393" style="zoom: 50%;" />

We don't have middle columns, binary search isn't possible.

Let's start by considering just the first column (F) and the last column (L) of the BWM, as well as the rank array.

```
F  L  rank
$  a  0
a  b  0
a  b  1
a  a  1
a  $  0
b  a  2
b  a  3
```

We will refine this to obtain the FM Index.

### Searching

Say we are searching for occurrences of a string $P=aba$. Like the suffix array, the BWM is sorted. This implies that any rows having $P$ as a prefix will be consecutive.

We proceed first by finding the rows beginning with the **shortest proper suffix** of $P$: $a$ in this case. The first column is part of our index, so this is trivial. These are the rows in the 0-based range [1,5). Let's visualize this in the context of the whole matrix:

<img src="./images/image-20250408140615567.png" alt="image-20250408140615567" style="zoom:50%;" />

> [!NOTE]
>
> Even though we just drew the entire matrix, out index so far contains just **F**, **L** and **rank**.

Now we must find all rows beginning with the next-longest proper suffix of $P$: $ba$. Observe the shaded characters in the L above. We see two *b*s, indicating there are two instances where $a$ is preceeded by $b$. Also, the LF Mapping and rank array tell us which rows have $ba$ as prefix: the rows beginning with $b_0$ and $b_1$: i.e. the first two rows in the "b section"。

<img src="./images/image-20250408140956948.png" alt="image-20250408140956948" style="zoom:50%;" />

Now we find rows beginning with the final suffix, $aba$. Again we look at the shaded characters in the last column. We see that the occurrences of $ba$ are preceded by $a_2$ and $a_3$, giving us the range of rows prefixed by $P$:

<img src="./images/image-20250408141200235.png" alt="image-20250408141200235" style="zoom:50%;" />

This is called *backwards matching*. In short, we apply the LF Mapping repeatedly to ﬁnd the range of rows preﬁxed by successively longer proper suﬃxes of $P$ until the range becomes empty, in which case $P$ does not occur in $T$, or until we run out of suﬃxes. If we run out of suﬃxes, the size of the range equals the number of times $P$ occurs in $T$.

Here is a Python implementation:

```python
def countMatches(bw, p):
    """ Given BWT(T) and a pattern string p, return the number of times
    p occurs in T. """
    ranks, tots = rankBwt(bw)
    first = firstCol(tots)
    l, r = first[p[-1]]
    i = len(p)-2
    while i >= 0 and r > l:
        c = p[i]
        # scan from left, looking for occurrences of c
        j = l
        while j < r:
            if bw[j] == c:
                l = first[c][0] + ranks[j]
                break
            j += 1
        if j == r:
            l = r
            break # no occurrences -> no match
        r -= 1
        while bw[r] != c:
        	r -= 1
        r = first[c][0] + ranks[r] + 1
        i -= 1
    return r - l
```

And some example output:

```python
>>> bw = bwt("Tomorrow_and_tomorrow_and_tomorrow")
>>> bw
’w$wwdd__nnoooaattTmmmrrrrrrooo__ooo’
>>> countMatches(bw, "tomorrow")
2
>>> countMatches(bw, "Tomorrow")
1
>>> countMatches(bw, "tomorrow")
2
>>> countMatches(bw, "omorrow")
3
>>> countMatches(bw, "and")
2
>>> countMatches(bw, "r")
6
>>> countMatches(bw, "o")
9
>>> countMatches(bw, "xyz")
0
```

A drawback is that, in each step, we are scanning a **range of elements** in L. This is $O(m)$ (where $m=|T|$).

<img src="./images/image-20250408181810881.png" alt="image-20250408181810881" style="zoom:50%;" />

**Fast rank calculations**

We can make this $O(1)$ by augmenting the rank array in the following way. Instread of a $m\times 1$ *rank* rray, we store a $m\times |\sum|$ *rankAll* matrix. In each row of rankAll, we store an integer for each character of the alphabet equal to the number of times that character has been observed up to and including that position in $BWT(T)$. For example:

```
        rankAll
F  L  $  a  b
$  a  0  1  0
a  b  0  1  1
a  b  0  1  2
a  a  0  2  2
a  $  1  2  2
b  a  1  3  2
b  a  1  4  2
```

Now, instead of scanning the last column, we can simply look up the appropriate character of *rankAll* at the left and right extremes of our range. If there is no difference between the two lookups, then the character does not occur. If there are one of more occurrences of the character, the lookups will give the ranks of those occurrences.

<img src="./images/image-20250408183246554.png" alt="image-20250408183246554" style="zoom:50%;" />

Here is a Python implementation oof this refinement:

```python
def rankAllBwt(bw):
    """ Given BWT string bw, returns a map of lists. Keys are
        characters and lists are cumulative # of occurrences up to and
        including the row. """
    tots = {}
    rankAll = {}
    for c in bw:
        if c not in tots:
            tots[c] = 0
            rankAll[c] = []
    for c in bw:
        tots[c] += 1
        for c in tots.iterkeys():
        	rankAll[c].append(tots[c])
    return rankAll, tots

def countMatches2(bw, p):
    """ Given BWT(T) and a pattern string p, return the number of times
    	p occurs in T. """
    rankAll, tots = rankAllBwt(bw)
    first = firstCol(tots)
    if p[-1] not in first:
    	return 0 # character doesn’t occur in T
    l, r = first[p[-1]]
    i = len(p)-2
    while i >= 0 and r > l:
        c = p[i]
        l = first[c][0] + rankAll[c][l-1]
        r = first[c][0] + rankAll[c][r-1]
        i -= 1
    return r - l # return size of final range
```

### Rank checkpoints

So far we have assumed that it's resonable to pre-calculate and store the ranks array or *rankAll* matrix. But this is probably not reasonable for larget T. Dividing T into blocks, as is done in compression, is not a good option since our goal is to build an index over all of T.

Instead we discard most but not all of the rows from the *rankAll* matrix. For instance, we might keep one in every 32 rows and discard the rest. We call the retained rows *rank checkpoints*. Now each time we would like to look up `rankAll[c][i]`, we are in one of two cases:

1. row $i$ was not discarded, in which case we do the lookup as usual
2. row $i$ was discarded, in which case we scan characters in $L$ starting at $i$ and moving forward (or backward) until we reach the next rank checkpoint.

<img src="./images/image-20250408183607472.png" alt="image-20250408183607472" style="zoom:50%;" />

If we count $x$ occurrences of $c$ on our way to a checkpoint at row $i'$, then `rankAll[c][i']-x` given the same value we would have found in `rankAll[c][i]-x`。

Another example (tally means count)：

<img src="./images/image-20250408184033249.png" alt="image-20250408184033249" style="zoom:50%;" />

If checkpoints are spaced at most $C$ rows from each other, where $C$ is a small constant, then this operation is $O(1)$. In which case, backward search for a pattern $P$ of length $n$ is $O(n)$ time. 

At the expense of adding checkpoints to index, solve the first two problem:

<img src="./images/image-20250408185032628.png" alt="image-20250408185032628" style="zoom:50%;" />

### Looking up offsets

So far we have discussed how to use the FM index to determine whether and how many times a substring $P$ occurs within $T$, but we have not discussed how to find **where** $P$ occurs, i.e., the substring's offset in $T$.

If our index included the suffix array $SA(T)$, we could simply look this up in $SA(T)$. For example, here was the range we ended up with after searching for $P=aba$ within $BWT(abaaba\$)$：

<img src="./images/image-20250408170324466.png" alt="image-20250408170324466" style="zoom:50%;" />

$SA(T)$ tells us these matches occurred at $T$ offsets 0 and 3.

However, the suffix array takes $m$ integers, and we would like to use less space than that. We again use the idea of **discarding** most of the entries and re-creating them as needed: Instead of storing every entry of $SA(T)$, we store, say every $4^{th}$. If we want to look up $SA[3]$ but find it has been discarded (discarded rows given "-" below):

<img src="./images/image-20250408170545737.png" alt="image-20250408170545737" style="zoom:50%;" />

We can use LF Mapping to step one position to the left in the text:

<img src="./images/image-20250408170628880.png" alt="image-20250408170628880" style="zoom:50%;" />

We're still in a discarded row, so we keep stepping until we reach a retained row. This happens after two more steps:

<img src="./images/image-20250408170711740.png" alt="image-20250408170711740" style="zoom:50%;" />

$SA(T)$ at this row equals 0 and it took us 3 steps to get here, so we conclude the row we started from had offset 3.

Note that if we retain an entry of $SA(T)$ every $C$ positions of $T$, where $C$ is a small constant, then lookintg up the text offset associated with a row of the BWM is $O(1)$。

At the expense of adding some SA values to index, solve the last problem.

Components of the FM Index:

- First column (F): ~ $|\sum|$ integers, can compress
- Last column (L): $m$ characters, BWT
- SA sample: $m\cdot a$ integers, where $a$ is fraction of rows kept
- checkpoints: $m\times |\sum|\cdot b$ integers, where b is fraction of rows checkpointed

## Suffix Arrays, BWT and FM-index

**Naïve exact search**

- Text="banana"
- Query="nana"
- Linear search

<img src="./images/image-20250408145101896.png" alt="image-20250408145101896" style="zoom: 33%;" />

- Naïve search is too slow
- Complexity of search scales **linearly** with the length of the text

**Suffix array**

- a space efficient alternative to suffix tree
- sorted array of all suffixes of a given text
- allows fast search of very large texts (e.g. genomes)

build suffix array: generate suffixes and then sort alphabetically, the suffix array is the index of the sorted suffix in the original string.

<img src="./images/image-20250408145335146.png" alt="image-20250408145335146" style="zoom:33%;" />

- search for **prefixes** in the suffix array that match our query string
- SA is sorted, so we can use binary search

<img src="./images/image-20250408145702318.png" alt="image-20250408145702318" style="zoom:33%;" />

<img src="./images/image-20250408145735023.png" alt="image-20250408145735023" style="zoom: 33%;" />

<img src="./images/image-20250408145813593.png" alt="image-20250408145813593" style="zoom:33%;" />

<img src="./images/image-20250408145847470.png" alt="image-20250408145847470" style="zoom:33%;" />

**SA vs. naïve search**

Searching the human genome (~3 billion base pairs, n) for a single-end read (100 base pairs, m)

- naïve search $O(mn)$
- suffix array search $O(m\log(n))$

| n    | O(n) | O(log(n)) |
| ---- | ---- | --------- |
| 8    | 8    | 3         |
| 16   | 16   | 4         |
| 32   | 32   | 5         |
| 64   | 64   | 6         |
| 128  | 128  | 7         |
| 256  | 256  | 8         |
| 512  | 512  | 9         |

> [!IMPORTANT]
>
> Human genome is ~3 billion basepairs
>
> assume 5 bytes per basepair (1 byte characters, 4 byte integers)=**~14 GB**

**Burrows-Wheeler transform**

- Invented by Burrows and Wheeler (1994)
- used in compression (.bz2)
- BWT matrix truncated at "$" in each row is the suffix array of the same text
- BWT can be computed directly from the suffix array

**FM-Index**

- All BWT allows us to do is compress text
- Ferragina and Manzini (2000) "**F**ull-text index in **M**inute space"
- Combine BWT with other auxiliary data stuctures to get an index
- Space savings: e.g. Human genome (3 billion bp)
  - SA=~14 GB (5  bytes/bp)
  - FM=~1.5 GB(2 bits/bp)

Cannot search BWT like SA:

- rotation matrix contains the suffix array
- but we only store F and L columns, so binary search of prefixes not possible

BWT search:

- in SA, we matched successively long prefixes (left-to-right) of query string (binary search)
- in BWT, we will match successively longer suffixes (right-to-left) of query string (reverse BWT transform)

we know BWT **contains** the query, but unlike SA, we do not know the **location** of the match in T!

## 参考

- https://www.langmead-lab.org/teaching.html
- https://www.youtube.com/watch?v=4n7NPk5lwbI
- https://www.youtube.com/watch?v=kvVGj5V65io
- https://www.youtube.com/playlist?list=PL2mpR0RYFQsADmYpW2YWBrXJZ_6EL_3nu
- http://loytynojalab.biocenter.helsinki.fi/data/evogeno/SA_BWT_FMI_lecture.pdf
- https://www.youtube.com/watch?v=5G2Db41pSHE&list=PL2mpR0RYFQsADmYpW2YWBrXJZ_6EL_3nu
