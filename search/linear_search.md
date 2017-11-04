
## 线性搜索特征
- 最为简单直接的搜索方法。
- 如果列表已排序，可以对算法进一步优化，即 binary search。

## 算法说明
- 次迭代列表，每个值和待搜索值进行对比
- 如果找到该值，返回其索引；如果没找到，返回 -1.

## 代码实现

### Java

```java
public static int linearSearch(Object[] array, Object toSearch)
{
    for (int i = 0; i < array.length; i++) {
        if (array[i].equals(toSearch))
            return i;
    }
    return -1;
}
```