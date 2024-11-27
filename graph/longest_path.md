# 最长路径问题

## 简介

对一般 graph，最长路径问题是 NP-Hard 问题。对 DAG (directed acyclic graph) 则有线性时间解。最长路径通常又称为**关键路径**（critical path）。

使用拓扑排序，先将所有 vertices 的距离初始化为负无穷，将 source-vertex 的距离初始化为 0，然后找到 graph 的拓扑排序。然后按拓扑顺序逐一处理所有顶点。对每个正在处理的顶点，使用当前顶点的距离更新其相邻顶点的距离。

## 算法实现

步骤如下：

<img src="./images/LongestPath-2.png" alt="灯箱" style="zoom:50%;" />

1. 初始化 `dist[]={NINF, NINF,...}`, `dist[s]=0`，其中 `s` 为起点，`NINF` 表示负无穷
2. 创建所有 nodes 的拓扑排序
3. 对拓扑顺序的每个 node u，对 u 的相邻节点 v

```java
if (dist[v] < dist[u] + weight(u,v))
    dist[v] = dist[u] + weight(u,v)
```

拓扑排序的时间复杂度为 $O(V+E)$，确定拓扑排序后，该算法处理所有 nodes，对每个 node，通过循环检测相邻 nodes。总的相邻 nodes 数为 $O(E)$，因此内部循环运行 $O(V+E)$ 次。因此总的时间复杂度为 $O(V+E)$。



## 参考

- https://ptwiddle.github.io/Graph-Theory-Notes/s_graphalgorithms_longest-paths.html
- https://www.geeksforgeeks.org/find-longest-path-directed-acyclic-graph/