public class Edge {
    private final Vertex origin;
    private final Vertex destination;
    private int label;

    public Edge(Vertex origin, Vertex destination) {
        this.origin = origin;
        this.destination = destination;
    }

    public Vertex opposite(Vertex w) {
        if (w == origin) {
            return destination;
        } else if (w == destination) {
            return origin;
        } else {
            return null;
        }
    }

    public Vertex getOrigin() {
        return origin;
    }

    public Vertex getDestination() {
        return destination;
    }

    @Override
    public String toString() {
        return "(" + origin + "," + destination + ")";
    }
}
