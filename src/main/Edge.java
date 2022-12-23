public class Edge {
    private Vertex origin;
    private Vertex destination;
    private int label;
    private Edge parent;
    private int forest;

    public Edge(Vertex origin, Vertex destination, int label) {
        this.origin = origin;
        this.destination = destination;
        this.label = label;
        this.parent = null;
        this.forest = 0;
    }

    public Edge(Vertex origin, Vertex destination) {
        this.origin = origin;
        this.destination = destination;
    }

    public Vertex opposite(Vertex w) {
        if (w.getLabel() == origin.getLabel()) {
            return destination;
        } else if (w.getLabel() == destination.getLabel()) {
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

    public int getForest() {
        return forest;
    }

    public void setForest(int forest) {
        this.forest = forest;
    }

    public Edge getParent() {
        return parent;
    }

    public void setParent(Edge parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        return "(" + origin + "," + destination + ")";
    }

}
