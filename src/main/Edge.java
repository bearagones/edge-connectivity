public class Edge {
    private final Vertex origin;
    private final Vertex destination;
    private int label;

    public Edge(Vertex origin, Vertex destination, int label) {
        this.origin = origin;
        this.destination = destination;
        this.label = label;
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

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "(" + origin + "," + destination + ")";
    }
}
