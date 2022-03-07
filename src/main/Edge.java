public class Edge {
    private int u;
    private int v;
    private int label;

    public Edge(int u, int v) {
        this.u = u;
        this.v = v;
    }

    public int opposite(int w) {
        if (w == u) {
            return v;
        } else if (w == v) {
            return u;
        } else {
            return -1;
        }
    }
}
