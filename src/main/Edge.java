public class Edge {
    private int origin;
    private int destination;
    private int label;

    public Edge(int origin, int destination) {
        this.origin = origin;
        this.destination = destination;
    }

    public int opposite(int w) {
        if (w == origin) {
            return destination;
        } else if (w == destination) {
            return origin;
        } else {
            return -1;
        }
    }
}
