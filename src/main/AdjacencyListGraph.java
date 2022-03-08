import java.util.LinkedList;
import java.util.Scanner;

public class AdjacencyListGraph {

    private final LinkedList<Edge>[] adj;

    public AdjacencyListGraph(int vertex) {
        this.adj = new LinkedList[vertex + 1];

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
    }

    public int numEdge() {
        return adj.length;
    }

    public void printList() {
        System.out.print("Current Adjacency List:");
        for (int i = 1; i < numEdge(); i++) {
            System.out.print("\n" + (i) + " => [");
            for (Edge edge : adj[i]) {
                System.out.print("(" + i + "," + edge.opposite(i) + ")");
            }
            System.out.print("]");
        }
        System.out.println("\nNumber of edges: " + (numEdge() - 1) + "\n");
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.print("Specify the number of vertices: ");
        int vertices = input.nextInt();

        AdjacencyListGraph adj = new AdjacencyListGraph(vertices);

        System.out.print("Specify the number of edges: ");
        int numEdges = input.nextInt();
        while (numEdges > ((vertices*(vertices-1)))/2) {
            System.out.println("It's not possible to have this number of edges. Try again! ");
            System.out.print("Specify the number of edges: ");
            numEdges = input.nextInt();
        }

        System.out.println("Type in the edges using <origin,destination>");
        int k = 1;
        while (k <= numEdges) {
            System.out.print("Edge #" + k + ": ");
            String[] inputEdge = input.next().split(",");
            int origin = Integer.parseInt(inputEdge[0]);
            int destination = Integer.parseInt(inputEdge[1]);
            k++;

            adj.insertEdge(origin, destination);
        }
        adj.printList();
    }
}