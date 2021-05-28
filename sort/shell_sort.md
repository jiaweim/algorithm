# 希尔排序

- [希尔排序](#希尔排序)
  - [概述](#概述)
  - [实现](#实现)
    - [Java 实现](#java-实现)

2021-05-28, 09:37
@Jiawei Mao
***

## 概述

希尔排序（Shell Sort），也称为递减增量排序，是插入排序的一种更高效的改进版本，由希尔（Donald Shell）在1959年提出。

希尔排序在插入排序上的改进：

- 插入排序在对几乎已经排序好的数据上操作效率高，可以达到线性排序的效率；
- 但插入排序一般来说效率低下，每次只能移动一位数据。

希尔排序的基本思想：现将整个待排序的序列分割成若干个子序列，分别进行插入排序，然后对整体进行插入排序。

下面选择初始增量 gap=length/2 进行图解步骤：

![](images/2019-06-11-15-27-45.png)

## 实现

### Java 实现

```java
public class ShellSort implements Sort{
    private int[] shells;

    public ShellSort(int[] shells){
        this.shells = shells;
    }

    @Override public <T extends Comparable<T>> T[] sort(T[] unsorted){
        int N = unsorted.length;
        for (int gap : shells) {
            for (int i = gap; i < N; i++) {
                int j = i;
                while (j - gap >= 0 && Sort.less(unsorted[j], unsorted[j - gap])) {
                    Sort.swap(unsorted, j, j - gap);
                    j -= gap;
                }
            }
        }
        return unsorted;
    }
}
```
