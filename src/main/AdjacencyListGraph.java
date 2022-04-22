import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

public class AdjacencyListGraph {

    private static int numVertices;
    private static int numEdges;
    private static int totalVertices;
    private static int totalEdges;
    private static Vertex[] vertexList;
    private static final LinkedList<Edge> neighborsList = new LinkedList<>();

    //Creates an Adjacency List Graph with the number of vertices
    public AdjacencyListGraph(int vertices) {
        vertexList = new Vertex[vertices + 1];

        //Creates a vertex for each vertex
        for (int i = 0; i < vertices; i++) {
            vertexList[i] = new Vertex(i+1);
        }
    }

    //Creates an AdjacencyListGraph from a .txt file
    private static AdjacencyListGraph createGraph(File file) throws FileNotFoundException {
        Scanner scan = new Scanner(file);

        numVertices = scan.nextInt();
        totalVertices = numVertices;
        scan.nextLine();
        numEdges = scan.nextInt();
        totalEdges = numEdges;

        AdjacencyListGraph adj = new AdjacencyListGraph(numVertices);

        scan.nextLine();
        for (int i = 0; i < numEdges; i++) {
            String edge = scan.nextLine();
            String[] inputEdge = edge.split(",");
            int originLabel = Integer.parseInt(inputEdge[0]);
            int destinationLabel = Integer.parseInt(inputEdge[1]);
            Vertex origin = vertexList[originLabel - 1];
            Vertex destination = vertexList[destinationLabel - 1];
            adj.insertEdge(origin, destination);
        }
        adj.printList();
        return adj;
    }

    //Inserts an edge in the Adjacency List
    public void insertEdge(Vertex origin, Vertex destination) {
        Edge edge = new Edge(origin, destination);
        origin.getEdgeList().add(edge);
        destination.getEdgeList().add(edge);
    }

    //Inserts an edge that is specified by the user
    private static void add(Scanner input, AdjacencyListGraph adj) {
        System.out.print("\nSpecify the number of edges you want to add: ");
        int numAdd = input.nextInt();
        int a = 1;
        while (a <= numAdd) {
            System.out.print("Edge #" + a + ": ");
            String[] inputEdge = input.next().split(",");
            int originLabel = Integer.parseInt(inputEdge[0]);
            int destinationLabel = Integer.parseInt(inputEdge[1]);
            Vertex origin = vertexList[originLabel - 1];
            Vertex destination = vertexList[destinationLabel - 1];
            a++;
            adj.insertEdge(origin, destination);
        }
        totalEdges++;
        adj.printList();
    }

    //Removes an edge in the Adjacency List
    public void removeEdge(Vertex origin, Vertex destination) {
        Edge foundEdge = null;
        for (Edge edge : origin.getEdgeList()) {
            if (edge.opposite(destination) == origin) {
                foundEdge = edge;
                break;
            }
        }
        origin.getEdgeList().remove(foundEdge);
        destination.getEdgeList().remove(foundEdge);
        totalEdges--;
    }

    //Removes an edge that is specified by the user
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
                int originLabel = Integer.parseInt(inputEdge[0]);
                int destinationLabel = Integer.parseInt(inputEdge[1]);
                Vertex origin = vertexList[originLabel - 1];
                Vertex destination = vertexList[destinationLabel - 1];
                r++;

                adj.removeEdge(origin, destination);
            }
        }
        adj.printList();
    }

    //Gets the neighboring edges of the vertex
    public void getNeighbors(Vertex vertex) {
        System.out.print(vertex + " => [");
        for (Edge edge : vertex.getEdgeList()) {
            neighborsList.add(edge);
            System.out.print("(" + vertex + "," + edge.opposite(vertex) + ")");
        }
        System.out.print("]");
        System.out.println();
    }

    private static void neighborOf(Scanner input, AdjacencyListGraph adj) {
        System.out.print("\nSpecify the vertex to see the neighboring edges: ");
        int seeVertex = input.nextInt();
        try {
            adj.getNeighbors(vertexList[seeVertex - 1]);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("No such vertex exists. Try again! ");
        }
    }

    public boolean search(Vertex vertex) {
        boolean[] marked = new boolean[totalVertices];
        boolean isConnected = true;
        marked[vertex.getLabel() - 1] = true;
        ArrayList<Vertex> markedVertices = new ArrayList<>();
        markedVertices.add(vertex);

        System.out.print("Reachable vertices from [" + vertex + "]: ");
        while (!markedVertices.isEmpty()) {
            Vertex markedVertex = markedVertices.remove(0);
            System.out.print("[" + markedVertex + "] ");
            for (Edge edge : markedVertex.getEdgeList()) {
                if (!marked[edge.opposite(markedVertex).getLabel() - 1]) {
                    markedVertices.add(edge.opposite(markedVertex));
                    marked[edge.opposite(markedVertex).getLabel() - 1] = true;
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
        return isConnected;
    }

    private static void vertexSearch(Scanner input, AdjacencyListGraph adj) {
        System.out.print("\nSpecify the vertex that you would like to search: ");
        int v = input.nextInt();
        Vertex vertex = vertexList[v - 1];
        adj.search(vertex);
    }

    public void search2(int origin, int destination) {

    }

    private static void pathSearch(Scanner input, AdjacencyListGraph adj) {
        System.out.println("Specify the origin vertex that you would like to start at: ");
        int origin = input.nextInt();
        System.out.println("Specify the destination vertex that you would like to end at: ");
        int destination = input.nextInt();
        adj.search2(origin, destination);
    }

    //Prints the current Adjacency List
    public void printList() {
        System.out.print("\nCurrent Adjacency List:");
        for (int i = 1; i <= numVertices; i++) {
            System.out.print("\n" + i + " => [");
            Vertex currentVertex = vertexList[i - 1];
            for (Edge edge : currentVertex.getEdgeList()) {
                System.out.print("(" + i + "," + edge.opposite(currentVertex) + ")");
            }
            System.out.print("]");
        }
        System.out.println("\nNumber of edges: " + totalEdges);
    }

    public static void main(String[] args) throws FileNotFoundException {
        boolean close = false;
        File file = new File(args[0]);

        AdjacencyListGraph graph = createGraph(file);

        while (!close) {
            System.out.print("\nPlease type one of the following actions to perform: 'add', 'remove', 'neighbor', 'search vertex', 'search path', 'exit': ");
            Scanner input = new Scanner(System.in);
            String userChoice = input.nextLine();
            switch (userChoice.toLowerCase()) {
                case "add" -> add(input,graph);
                case "remove" -> remove(input,graph);
                case "neighbor" -> neighborOf(input,graph);
                case "search vertex" -> vertexSearch(input, graph);
                case "search path" -> pathSearch(input, graph);
                case "exit" -> close = true;
                default -> System.out.println("No such action exists.");
            }
        }
        System.out.println("Thank you for trying this program!");
    }
}
