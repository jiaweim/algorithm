# 插入排序

- [插入排序](#插入排序)
  - [插入排序特征](#插入排序特征)
  - [排序流程](#排序流程)
  - [代码实现](#代码实现)
    - [伪代码](#伪代码)
    - [Java](#java)

2021-05-28, 09:40
***

## 插入排序特征

- 对比排序
- 原地排序
- 平均时间复杂度：O(n^2)
- 最佳时间复杂度：O(n)
- 对少量元素的排序比较有效，对大量元素不如 quicksort, heapsort 或 mergesort有效

## 排序流程

可以通过插牌的过程理解插入排序：

![](https://github.com/jiaweiM/algorithm/blob/master/images/sort_insertion_1.jpg?raw=true)

- 一串随机排序的数
- 取第一个数
- 取第二个数，和第一个对比，大于第一个就排在后面，小于第一个就排在前面
- 现在前两个是排序好的
- 继续该过程直到结束，每次添加一个新的数字，数字都是排序好的

![](images/insertionSort.gif)

## 代码实现

要点说明：

- 使用原地排序，需要创建一个临时变量
- 每次迭代，列表的一端是已排序好的数
    - 第一次迭代，前两个数已排序
    - 2nd, 前三个数已排序
    - 3rd, 前四个数已排序
    - 所以如果只需要列表部分排序，可以在排序到一半的时候停止排序。
- 需要两个循环，一个用于从未排序列表中取数字，一个用于在已排序好的列表中找到待插入数的位置。

### 伪代码

```pseudocode
for j = 2 to A.length
    key = A[j]
    // Insert A[j] into the sorted sequence A[1...j-1].
    i = j - 1
    while i > 0 and A[i] > key
        A[i+1] = A[i]
        i = i - 1
     A[i+1] = key
```

### Java

```java
public static <T extends Comparable<T>> T[] insertSort(T[] unsorted)
{
    int length = unsorted.length;
    for (int j = 1; j < length; j++) {
        T key = unsorted[j];

        int i = j - 1;
        while (i >= 0) {
            if (unsorted[i].compareTo(key) > 0) {
                unsorted[i + 1] = unsorted[i];
                i = i - 1;
            } else
                break;
        }
        unsorted[i + 1] = key;
    }

    return unsorted;
}
```
