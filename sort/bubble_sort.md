# 冒泡排序

- [冒泡排序](#冒泡排序)
  - [简介](#简介)
  - [实现](#实现)
    - [Java 实现](#java-实现)

2021-05-28, 09:18
***

## 简介

冒泡排序（Bubble Sort）是一种简单排序算法，检查相邻元素的大小，如果后者小于前者，交换位置。由于小的元素最后会交换到列表的顶端，所以称为冒泡排序。

动画演示：

![bubble sort](images/bubbleSort.gif)

## 实现

### Java 实现

```java
public class BubbleSort implements Sort{
    @Override public <T extends Comparable<T>> T[] sort(T[] unsorted){
        int last = unsorted.length;
        boolean swap;
        do {
            swap = false;
            for (int i = 0; i < last - 1; i++) {
                if (Sort.less(unsorted[i], unsorted[i + 1]))
                    swap = Sort.swap(unsorted, i, i + 1);
            }
            last--;
        } while (swap);
        return unsorted;
    }
}
```
