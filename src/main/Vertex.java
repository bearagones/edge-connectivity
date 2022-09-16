import java.util.LinkedList;

public class Vertex {
    private final int label;
    private Vertex parent;
    private final LinkedList<Edge> edgeList;

    //Each vertex will have an edgeList associated with it
    public Vertex(int label) {
        this.label = label;
        this.edgeList = new LinkedList<>();
    }

    public Vertex getParent() {
        return parent;
    }
    public void setParent(Vertex parent) {
        this.parent = parent;
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
