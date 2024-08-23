# Graph 概念

## Complete Graph

完全图（complete graph）是一种特殊的无向图，其任意两点之间都有边直接相连。

<img src="./images/image-20240821100520325.png" alt="image-20240821100520325" style="zoom:50%;" />

包含 $n$ 个节点的 complete-graph 记为 $K_n$，包含 $\binom{n}{2}=n(n-1)/2$ 条无向边，其中 $\binom{n}{k}$ 为二项系数。在一些早期文献中，complete-graph 又称为 universal-graph。

## Graph Diameter

## Longest Path

最长路径要求给出 graph 中最长的一条路径。该问题是 NP-complete，但对 DAG 图，存在一种有效的动态规划解。

