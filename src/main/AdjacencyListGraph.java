import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

public class AdjacencyListGraph {

    private int numEdges;
    private static boolean isDirected;
    private ArrayList<Vertex> vertexList = new ArrayList<>();
    private ArrayList<Edge> edgeList = new ArrayList<>();

    //Creates an Adjacency List Graph with the number of vertices
    public AdjacencyListGraph(int vertices) {

        //Creates a vertex for each vertex
        for (int i = 0; i < vertices; i++) {
            vertexList.add(new Vertex(i + 1));
        }
    }

    public AdjacencyListGraph(int vertices, boolean isDirected) {
        //Creates a vertex for each vertex
        for (int i = 0; i < vertices; i++) {
            vertexList.add(new Vertex(i + 1));
        }
        this.isDirected = isDirected;
    }

    //Creates an AdjacencyListGraph from a .txt file
    private static AdjacencyListGraph createGraph(File file) throws FileNotFoundException {
        Scanner scan = new Scanner(file);

        String graphType = scan.next();
        isDirected = graphType.equals("directed");
        scan.nextLine();
        int numVertices = scan.nextInt();
        //totalVertices = numVertices;
        scan.nextLine();
        int numEdges = scan.nextInt();
        //totalEdges = numEdges;

        AdjacencyListGraph adj = new AdjacencyListGraph(numVertices);

        scan.nextLine();
        for (int i = 0; i < numEdges; i++) {
            String edge = scan.nextLine();
            String[] inputEdge = edge.split(",");
            int originLabel = Integer.parseInt(inputEdge[0]);
            int destinationLabel = Integer.parseInt(inputEdge[1]);
            int usedLabel = Integer.parseInt(inputEdge[2]);
            Vertex origin = adj.vertexList.get(originLabel - 1);
            Vertex destination = adj.vertexList.get(destinationLabel - 1);
            adj.insertEdge(origin, destination, usedLabel);
        }
        adj.printList();
        return adj;
    }

    //Inserts a vertex in the Adjacency List
    private void insertVertex(int label) {
        boolean foundVertex = false;
        for (Vertex vertex : vertexList) {
            if (vertex.getLabel() == label) {
                foundVertex = true;
                break;
            }
        }

        if (!foundVertex) {
            vertexList.add(label - 1, new Vertex(label));
        } else {
            System.out.println("This vertex already exists!");
        }
    }

    //Inserts a vertex that is specified by the user
    private void addVertex(Scanner input, AdjacencyListGraph adj) {
        System.out.print("\nSpecify the number of vertices you want to add: ");
        int numAdd = input.nextInt();
        int a = 1;
        while (a <= numAdd) {
            System.out.print("Vertex #" + a + ": ");
            int inputVertex = input.nextInt();
            adj.insertVertex(inputVertex);
            a++;
        }
        //this.totalVertices++;
        adj.printList();
    }

    //Inserts an edge in the Adjacency List
    private void insertEdge(Vertex origin, Vertex destination, int label) {
        Edge edge = new Edge(origin, destination, label);
        origin.getEdgeList().add(edge);
        if (!isDirected) {
            destination.getEdgeList().add(edge);
        }
        this.edgeList.add(edge);
    }

    private void insertEdge(Vertex origin, Vertex destination) {
        Edge edge = new Edge(origin, destination);
        origin.getEdgeList().add(edge);
        if (!isDirected) {
            destination.getEdgeList().add(edge);
        }
        this.edgeList.add(edge);
    }

    //Inserts an edge that is specified by the user
    private void addEdge(Scanner input, AdjacencyListGraph adj) {
        System.out.print("\nSpecify the number of edges you want to add: ");
        int numAdd = input.nextInt();
        int a = 1;
        while (a <= numAdd) {
            System.out.print("Edge #" + a + ": ");
            String[] inputEdge = input.next().split(",");
            int originLabel = Integer.parseInt(inputEdge[0]);
            int destinationLabel = Integer.parseInt(inputEdge[1]);
            int usedLabel = Integer.parseInt(inputEdge[2]);
            Vertex origin = adj.vertexList.get(originLabel - 1);
            Vertex destination = adj.vertexList.get(destinationLabel - 1);
            a++;
            adj.insertEdge(origin, destination, usedLabel);
        }
        //totalEdges++;
        adj.printList();
    }

    //Removes an edge in the Adjacency List
    private void deleteEdge(Vertex origin, Vertex destination) {
        Edge foundEdge = null;
        for (Edge edge : origin.getEdgeList()) {
            if (edge.opposite(destination) == origin) {
                foundEdge = edge;
                break;
            }
        }
        origin.getEdgeList().remove(foundEdge);
        if (!isDirected) {
            destination.getEdgeList().remove(foundEdge);
        }
        edgeList.remove(foundEdge);
        //totalEdges--;
    }

    //Removes an edge that is specified by the user
    private void removeEdge(Scanner input, AdjacencyListGraph adj) {
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
                Vertex origin = adj.vertexList.get(originLabel - 1);
                Vertex destination = adj.vertexList.get(destinationLabel - 1);
                r++;

                adj.deleteEdge(origin, destination);
            }
        }
        adj.printList();
    }

    //Gets the neighboring vertices of a vertex in the Adjacency List
    private ArrayList<Vertex> getNeighbors(Vertex vertex) {
        ArrayList<Vertex> neighborsList = new ArrayList<>();
        for (Edge newEdge : vertex.getEdgeList()) {
            neighborsList.add(newEdge.opposite(vertexList.get(vertex.getLabel() - 1)));
        }
        return neighborsList;
    }

    //Gets the neighboring vertices of a vertex that is specified by the user
    private void neighborOf(Scanner input, AdjacencyListGraph adj) {
        System.out.print("\nSpecify the vertex to see the neighboring vertices: ");
        int seeVertex = input.nextInt();
        try {
            adj.getNeighbors(adj.vertexList.get(seeVertex - 1));
            for (Vertex vertex : adj.getNeighbors(adj.vertexList.get(seeVertex - 1))) {
                System.out.print("[" + vertex + "] ");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("No such vertex exists. Try again! ");
        }
    }

    //Searches for a vertex to check graph connectivity
    private boolean search(Vertex vertex) {
        boolean[] marked = new boolean[vertexList.size()];
        boolean isConnected = true;
        marked[vertex.getLabel() - 1] = true;
        ArrayList<Vertex> markedVertices = new ArrayList<>();
        markedVertices.add(vertex);

        while (!markedVertices.isEmpty()) {
            Vertex markedVertex = markedVertices.remove(0);
            //System.out.print("[" + markedVertex + "] ");
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

        return isConnected;
    }

    //Searches for a vertex that is specified by the user to check graph connectivity
    private void vertexSearch(Scanner input, AdjacencyListGraph adj) {
        System.out.print("\nSpecify the vertex that you would like to search: ");
        int v = input.nextInt();
        Vertex vertex = adj.vertexList.get(v - 1);
        boolean isConnected = adj.search(vertex);

        //System.out.print("Reachable vertices from [" + vertex + "]: ");

        if (isConnected) {
            System.out.println("\nThe graph is connected.");
        } else {
            System.out.println("\nThe graph is not connected.");
        }
    }

    //Searches for a path between two vertices
    private ArrayList<Vertex> search2(Vertex origin, Vertex destination) {
        ArrayList<Vertex> vertexPath = new ArrayList<>();
        boolean[] marked = new boolean[vertexList.size()];
        Vertex[] parent = new Vertex[vertexList.size()];

        marked[origin.getLabel() - 1] = true;
        ArrayList<Vertex> markedVertices = new ArrayList<>();
        markedVertices.add(origin);
        parent[origin.getLabel() - 1] = origin;

        System.out.print("Path from [" + origin + "] to [" + destination + "]: ");
        while (!markedVertices.isEmpty()) {
            Vertex markedVertex = markedVertices.remove(0);
            for (Vertex neighbor : getNeighbors(markedVertex)) {
                if (!marked[neighbor.getLabel() - 1]) {
                    marked[neighbor.getLabel() - 1] = true;
                    markedVertices.add(neighbor);
                    parent[neighbor.getLabel() - 1] = markedVertex;
                }
            }
        }

        if (parent[destination.getLabel() - 1] == null) {
            System.out.println("There is no path between the specified vertices.");
            return null;
        }
        Vertex currently = destination;
        vertexPath.add(0, destination);
        while (!currently.equals(origin)) {
            currently = parent[currently.getLabel() - 1];
            vertexPath.add(0, currently);
        }
        System.out.println(vertexPath);
        return vertexPath;
    }

    //Searches for a path between two vertices that are specified by the user
    private void pathSearch(Scanner input, AdjacencyListGraph adj) {
        System.out.println("Specify the origin vertex that you would like to start at: ");
        int originLabel = input.nextInt();
        Vertex origin = adj.vertexList.get(originLabel - 1);
        System.out.println("Specify the destination vertex that you would like to end at: ");
        int destinationLabel = input.nextInt();
        Vertex destination = adj.vertexList.get(destinationLabel - 1);
        adj.search2(origin, destination);

    }

    //Searches for bridges in the Adjacency List
    private void searchBridge(AdjacencyListGraph adj) {
        ArrayList<Edge> edgeCopy = new ArrayList<>(adj.edgeList);

        for (Edge edge : edgeCopy) {
            Vertex origin = edge.getOrigin();
            Vertex destination = edge.getDestination();
            adj.deleteEdge(origin, destination);
            if (!adj.search(origin)) {
                System.out.print("[");
                System.out.print(edge);
                System.out.print("]");
            }
            adj.insertEdge(origin, destination, edge.getLabel());
        }
    }

    private AdjacencyListGraph createResidualGraph() {
        AdjacencyListGraph residualGraph = new AdjacencyListGraph(vertexList.size(), true); // creates a residual graph with same # of vertices and edges (not specified)
        this.vertexList = residualGraph.vertexList;
        for (Edge edge: edgeList) {
            Vertex origin = edge.getOrigin();
            edge.setOrigin(vertexList.get(origin.getLabel() - 1));
            Vertex newOrigin = edge.getOrigin();

            Vertex destination = edge.getDestination(); // refers to original destination
            edge.setDestination(vertexList.get(destination.getLabel() - 1));
            Vertex newDestination = edge.getDestination();
            if (edge.getLabel() == 0) {
                residualGraph.insertEdge(newOrigin, newDestination);
            } else {
                residualGraph.insertEdge(newDestination, newOrigin);
            }
        }
        this.edgeList = residualGraph.edgeList;
        return residualGraph;
    }
    private Edge getEdge(Vertex origin, Vertex destination) { // works as expected
        Edge foundEdge = null;
        for (Edge edge : origin.getEdgeList()) {
            if ((vertexList.get(origin.getLabel() - 1)  == origin && edge.getDestination() == destination && edge.getLabel() == 0) ||
                    (vertexList.get(origin.getLabel() - 1) == destination && edge.getDestination() == origin && edge.getLabel() == 1)) {
                foundEdge = edge;
            }
        }
        return foundEdge;
    }

    private void augmentFlow(ArrayList<Vertex> vertexPath) { //should be boolean
        for (int i = 0; i < vertexPath.size() - 1; i++) {
            Vertex origin = vertexPath.get(i);
            Vertex destination = vertexPath.get(i + 1);
            Edge edge = this.getEdge(origin, destination); //gets the edge connecting the origin and destination
            if (edge.getLabel() == 0) { //edge.setLabel(1-edge.getLabel()); alternative
                edge.setLabel(1); //labels unused edge to used edge
            } else {
                edge.setLabel(0);// labels used edge to unused edge
            }
        }
    }

    private int getNumberOfPaths(Vertex origin, Vertex destination) {
        int pathCount = 0;
        while (true) {
            AdjacencyListGraph residualGraph = createResidualGraph();
            Vertex newOrigin = residualGraph.vertexList.get(origin.getLabel() - 1);
            Vertex newDestination = residualGraph.vertexList.get(destination.getLabel() - 1);
            ArrayList<Vertex> vertexPath = search2(newOrigin, newDestination);
            if (vertexPath == null) {
                break;
            }
            residualGraph.augmentFlow(vertexPath);
            pathCount++;
        }
        System.out.println("The edge connectivity is " + pathCount + ".");
        return pathCount;
    }

    private static void edgeConnectivity(Scanner input, AdjacencyListGraph adj) {
        System.out.println("Specify the origin vertex: ");
        int originLabel = input.nextInt();
        Vertex origin = adj.vertexList.get(originLabel - 1);
        System.out.println("Specify the destination vertex: ");
        int destinationLabel = input.nextInt();
        Vertex destination = adj.vertexList.get(destinationLabel - 1);
        adj.getNumberOfPaths(origin, destination);
    }

    //Prints the current Adjacency List
    private void printList() {
        System.out.print("\nCurrent Adjacency List:");
        for (int i = 1; i <= vertexList.size(); i++) {
            System.out.print("\n" + vertexList.get(i - 1).getLabel() + " => [");
            Vertex currentVertex = vertexList.get(i - 1);
            for (Edge edge : currentVertex.getEdgeList()) {
                System.out.print("(" + i + "," + edge.opposite(currentVertex) + ")");
            }
            System.out.print("]");
        }
        System.out.println("\nNumber of edges: " + edgeList.size());
    }

    public static void main(String[] args) throws FileNotFoundException {
        boolean close = false;
        File file = new File(args[0]);
        AdjacencyListGraph graph = createGraph(file);

        while (!close) {
            System.out.print("\nPlease type one of the following actions to perform: ");
            Scanner input = new Scanner(System.in);
            System.out.println("'add edge', 'remove edge', 'add vertex', 'neighbor', 'search vertex', 'search path', 'search bridge', 'edge connectivity', 'exit'");
            String userChoice = input.nextLine();
            switch (userChoice.toLowerCase()) {
                case "add edge" -> graph.addEdge(input,graph);
                case "remove edge" -> graph.removeEdge(input,graph);
                case "add vertex" -> graph.addVertex(input, graph);
                case "neighbor" -> graph.neighborOf(input,graph);
                case "search vertex" -> graph.vertexSearch(input, graph);
                case "search path" -> graph.pathSearch(input, graph);
                case "search bridge" -> graph.searchBridge(graph);
                case "edge connectivity" -> edgeConnectivity(input, graph);
                case "exit" -> close = true;
                default -> System.out.println("No such action exists.");
            }
        }
        System.out.println("Thank you for trying this program!");
    }
}