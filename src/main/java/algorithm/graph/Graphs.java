package algorithm.graph;

/**
 *
 *
 * @author Jiawei Mao
 * @version 0.1.0
 * @since 21 Aug 2024, 2:00 PM
 */
public class Graphs {
    /**
     * compute degree of v.
     */
    public static int degree(Graph g, int v) {
        int degree = 0;
        for (int ignored : g.adj(v))
            degree++;
        return degree;
    }

    /**
     * compute maximum degree
     */
    public static int maxDegree(Graph g) {
        int max = 0;
        for (int v = 0; v < g.V(); v++) {
            if (degree(g, v) > max)
                max = degree(g, v);
        }
        return max;
    }

    /**
     * compute average degree
     */
    public static int avgDegree(Graph g) {
        return 2 * g.E() / g.V();
    }

    /**
     * count self-loops
     */
    public static int numberOfSelfLoops(Graph g) {
        int count = 0;
        for (int v = 0; v < g.V(); v++) {
            for (int w : g.adj(v)) {
                if (v == w)
                    count++;
            }
        }
        return count / 2;
    }


}
