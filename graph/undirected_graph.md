# 无向图

## 简介

**无向图**中，边（edge）仅仅是两个顶点（vertex）之间的连接。

特定的图：

- 自环（self-loop），一条边首尾连接到同一个顶点；
- 平行边（parallel），多条边连接相同的两个顶点。

如下图：

<img src="./images/image-20240819131845899.png" alt="image-20240819131845899" style="zoom:50%;" />

## 术语

包含平行边的 graph 通常称为 **multigraph**，没有平行边和 self-loops 的 graph 称为 simple-graph。

其它概念：

- adjacent，两个由 edge 连接的 vertices，该 edge 与这两个 vertices 关联（incident）；
- degree，vertex 的 degree 指与其关联的 edge 数；
- subgraph，一个 graph 的 edge 子集和相关 vertices 构成的 graph；

> [!IMPORTANT]
>
> path，由 edges 连接的 vertices 序列。
>
> simple-path，没有重复 vertices 的 path。
>
> cycle，至少包含一个 self-loop 的 path。
>
> simple-cycle，
>
> path length，path 包含的 edge 数目。



## Depth-first search

```java
public class DepthFirstSearch {
    private boolean[] marked;
    private int count;
    
    public DepthFirstSearch(Graph G, int s){  
        marked = new boolean[G.V()];
        dfs(G, s);
    }
    
    private void dfs(Graph G, int v) {
        marked[v] = true;
        count++;
        for (int w : G.adj(v))
            if (!marked[w]) dfs(G, w);
    }
    public boolean marked(int w){  
        return marked[w];  
    }
    public int count() {  
        return count;  
    }
}
```

DFS 步骤：

- 访问一个顶点，将其标记为 marked
- 递归访问它所有没有被标记过的 adjacent 顶点

