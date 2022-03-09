import java.util.LinkedList;
import java.util.Scanner;

public class AdjacencyListGraph {

    private static int numEdges;
    private static int total;
    private final LinkedList<Edge>[] adj;

    public AdjacencyListGraph(int vertex) {
        this.adj = new LinkedList[vertex + 1];

        for (int i = 0; i <= vertex; i++) {
            adj[i] = new LinkedList<>();
        }
    }

    private static AdjacencyListGraph createGraph(Scanner input) {
        System.out.print("Specify the number of vertices: ");
        int vertices = input.nextInt();

        System.out.print("Specify the number of edges: ");
        numEdges = input.nextInt();
        total = numEdges;
        while (numEdges > ((vertices*(vertices-1)))/2) {
            System.out.println("It's not possible to have this number of edges. Try again! ");
            System.out.print("Specify the number of edges: ");
            numEdges = input.nextInt();
        }
        AdjacencyListGraph adj = new AdjacencyListGraph(vertices);
        add(input,adj);
        return adj;
    }

    public void insertEdge(int origin, int destination) {
        Edge edge = new Edge(origin, destination);
        adj[origin].add(edge);
        adj[destination].add(edge);
    }

    private static void add(Scanner input, AdjacencyListGraph adj) {
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

    public void removeEdge(int origin, int destination) {
        Edge foundEdge = null;
        for (Edge edge : adj[origin]) {
            if (edge.opposite(destination) == origin) {
                foundEdge = edge;
                break;
            }
        }
        adj[origin].remove(foundEdge);
        adj[destination].remove(foundEdge);
        total--;
    }

    private static void remove(Scanner input, AdjacencyListGraph adj) {
        System.out.print("\nSpecify the number of edges you want removed: ");
        int numRemove = input.nextInt();
        while (numRemove > numEdges) {
            System.out.println("It's not possible to remove this number of edges. Try again! ");
            System.out.print("Specify the number of edges you want removed: ");
            numRemove = input.nextInt();
        }

        if (numRemove > 0) {
            System.out.println("Type in the edges using <origin,destination>");
            int r = 1;
            while (r <= numRemove) {
                System.out.print("Edge #" + r + ": ");
                String[] inputEdge = input.next().split(",");
                int origin = Integer.parseInt(inputEdge[0]);
                int destination = Integer.parseInt(inputEdge[1]);
                r++;

                adj.removeEdge(origin, destination);
            }
        }
        adj.printList();
    }

    public void getNeighbors(int vertex) {
        System.out.print(vertex + " => [");
        for (Edge edge : adj[vertex]) {
            System.out.print("(" + vertex + "," + edge.opposite(vertex) + ")");
        }
        System.out.print("]");
    }

    private static void neighborOf(Scanner input, AdjacencyListGraph adj) {
        System.out.print("\nSpecify the vertex to see the neighboring edges: ");
        int seeVertex = input.nextInt();
        try {
            adj.getNeighbors(seeVertex);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("No such vertex exists. Try again! ");
        }
        adj.printList();
    }

    public void printList() {
        System.out.print("\nCurrent Adjacency List:");
        for (int i = 1; i <= numEdges; i++) {
            System.out.print("\n" + i + " => [");
            for (Edge edge : adj[i]) {
                System.out.print("(" + i + "," + edge.opposite(i) + ")");
            }
            System.out.print("]");
        }
        System.out.println("\nNumber of edges: " + total);
    }

    public static void main(String[] args) {
        boolean close = false;
        Scanner input = new Scanner(System.in);

        AdjacencyListGraph graph = createGraph(input);

        while (!close) {
            System.out.println("\nPlease type one of the following actions to perform: 'add', 'remove', 'neighbor', 'exit'");
            String userChoice = input.next();
            switch (userChoice.toLowerCase()) {
                case "add" -> add(input,graph);
                case "remove" -> remove(input,graph);
                case "neighbor" -> neighborOf(input,graph);
                case "exit" -> close = true;
                default -> System.out.println("No such action exists.");
            }
        }
        System.out.println("Thank you for trying this program!");
    }
}
