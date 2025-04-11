# Indexing

## Preprocessing

### Naïve 算法

朴素（Naïve）精确匹配算法**不预处理**文本。

### Boyer-Moore

Boyer-Moore 算法预处理模式 $P$。

<img src="./images/image-20250409100026614.png" alt="image-20250409100026614" style="zoom: 33%;" />

相同 pattern 匹配不同文本，则可以重复利用预处理的 $P$。

