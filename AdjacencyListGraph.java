import java.util.LinkedList;

public class AdjacencyListGraph {

    class Edge {
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

    private LinkedList<Edge>[] adj;
    private int vertex;
    private int edge;

    public AdjacencyListGraph(int vertex) {
        this.vertex = vertex;
        this.edge = 0;
        this.adj = new LinkedList[vertex];

        for (int i = 0; i <= vertex; i++) {
            adj[i] = new LinkedList<>();
        }
    }

    public void insertEdge(int origin, int destination) {
        Edge edge = new Edge(origin, destination);
        adj[origin].add(edge);
        adj[destination].add(edge);
    }

    public void removeEdge(int origin, int destination) {
        Edge foundEdge = null;
        // insert code to choose shorter list between origin and destination
        for (Edge edge : adj[origin]) {
            if (edge.opposite(destination) == origin) {
                foundEdge = edge;
                break;
            }
        }
        adj[origin].remove(foundEdge);
        adj[destination].remove(foundEdge);
        edge--;
    }

    public int numEdge() {
        return adj.length;
    }

    public void printList() {
        System.out.print("Current Adjacency List:");
        for (int i = 0; i < adj.length; i++) {
            System.out.print("\n" + (i + 1) + " => [");
            for (Edge edge : adj[i]) {
                System.out.print("(" + i + "," + edge.opposite(i) + ")");
            }
            System.out.print("]");
        }
        System.out.println("\nNumber of edges: " + adj.length + "\n");
    }

    public static void main(String[] args) {
        int vertex = 5;

        AdjacencyListGraph adj = new AdjacencyListGraph(vertex);

        adj.insertEdge(1, 2);
        adj.insertEdge(1, 3);
        adj.insertEdge(2, 4);
        adj.insertEdge(3, 5);
        adj.insertEdge(3, 4);
        adj.insertEdge(4, 5);
        adj.printList();

        adj.removeEdge(3, 5);
        adj.printList();
    }
}