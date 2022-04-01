import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

public class AdjacencyListGraph {

    private static int numEdges;
    private static int total;
    private static int vertices;
    private final LinkedList<Edge>[] adj;

    public AdjacencyListGraph(int vertex) {
        this.adj = new LinkedList[vertex + 1];

        for (int i = 0; i <= vertex; i++) {
            adj[i] = new LinkedList<>();
        }
    }

    private static AdjacencyListGraph createGraph(File file) throws FileNotFoundException {
        Scanner scan = new Scanner(file);

        vertices = scan.nextInt();
        scan.nextLine();
        numEdges = scan.nextInt();
        total = numEdges;

        AdjacencyListGraph adj = new AdjacencyListGraph(vertices);

        scan.nextLine();
        for (int i = 0; i < numEdges; i++) {
            String edge = scan.nextLine();
            String[] inputEdge = edge.split(",");
            int origin = Integer.parseInt(inputEdge[0]);
            int destination = Integer.parseInt(inputEdge[1]);
            adj.insertEdge(origin, destination);
        }
        adj.printList();
        return adj;
    }

    public void insertEdge(int origin, int destination) {
        Edge edge = new Edge(origin, destination);
        adj[origin].add(edge);
        adj[destination].add(edge);
    }

    private static void add(Scanner input, AdjacencyListGraph adj) {
        System.out.print("\nSpecify the number of edges you want to add: ");
        int numAdd = input.nextInt();
        int a = 1;
        while (a <= numAdd) {
            System.out.print("Edge #" + a + ": ");
            String[] inputEdge = input.next().split(",");
            int origin = Integer.parseInt(inputEdge[0]);
            int destination = Integer.parseInt(inputEdge[1]);
            a++;
            adj.insertEdge(origin, destination);
        }
        total++;
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
        System.out.println();
    }

    private static void neighborOf(Scanner input, AdjacencyListGraph adj) {
        System.out.print("\nSpecify the vertex to see the neighboring edges: ");
        int seeVertex = input.nextInt();
        try {
            adj.getNeighbors(seeVertex);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("No such vertex exists. Try again! ");
        }
    }

    public void search(int vertex) {
        boolean[] marked = new boolean[vertices];
        boolean isConnected = true;
        marked[vertex - 1] = true;
        ArrayList<Integer> markedVertices = new ArrayList();
        markedVertices.add(vertex);

        System.out.print("Reachable vertices from [" + vertex + "]: ");
        while (!markedVertices.isEmpty()) {
            int markedVertex = markedVertices.remove(0);
            System.out.print("[" + markedVertex + "] ");
            for (Edge edge : adj[markedVertex]) {
                if (!marked[edge.opposite(markedVertex) - 1]) {
                    markedVertices.add(edge.opposite(markedVertex));
                    marked[edge.opposite(markedVertex) - 1] = true;
                }
            }
        }
        for (boolean b : marked) {
            if (!b) {
                isConnected = false;
                break;
            }
        }

        if (isConnected) {
            System.out.println("\nThe graph is connected.");
        } else {
            System.out.println("\nThe graph is not connected.");
        }
    }

    private static void vertexSearch(Scanner input, AdjacencyListGraph adj) {
        System.out.print("\nSpecify the vertex that you would like to search: ");
        int vertex = input.nextInt();
        adj.search(vertex);
    }

    public void printList() {
        System.out.print("\nCurrent Adjacency List:");
        for (int i = 1; i <= vertices; i++) {
            System.out.print("\n" + i + " => [");
            for (Edge edge : adj[i]) {
                System.out.print("(" + i + "," + edge.opposite(i) + ")");
            }
            System.out.print("]");
        }
        System.out.println("\nNumber of edges: " + total);
    }

    public static void main(String[] args) throws FileNotFoundException {
        boolean close = false;
        File file = new File("resources/TestCase01.txt");

        AdjacencyListGraph graph = createGraph(file);

        while (!close) {
            System.out.print("\nPlease type one of the following actions to perform: 'add', 'remove', 'neighbor', 'search vertex', 'exit': ");
            Scanner input = new Scanner(System.in);
            String userChoice = input.nextLine();
            switch (userChoice.toLowerCase()) {
                case "add" -> add(input,graph);
                case "remove" -> remove(input,graph);
                case "neighbor" -> neighborOf(input,graph);
                case "search vertex" -> vertexSearch(input, graph);
                case "exit" -> close = true;
                default -> System.out.println("No such action exists.");
            }
        }
        System.out.println("Thank you for trying this program!");
    }
}
