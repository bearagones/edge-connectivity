import java.util.LinkedList;

public class Vertex {
    private final int label;
    private final LinkedList<Edge> edgeList;

    public Vertex(int label) {
        this.label = label;
        this.edgeList = new LinkedList<>();
    }

    public int getLabel() {
        return label;
    }

    public LinkedList<Edge> getEdgeList() {
        return edgeList;
    }

    @Override
    public String toString() {
        return Integer.toString(label);
    }
}
