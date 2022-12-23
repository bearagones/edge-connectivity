import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import static java.lang.Math.min;

public class AdjacencyListGraph {

    private boolean isDirected;
    private ArrayList<Vertex> vertexList = new ArrayList<>();
    private ArrayList<Edge> edgeList = new ArrayList<>();

    //Creates an Adjacency List Graph with the number of vertices
    public AdjacencyListGraph(int vertices) {
        this(vertices, false);
    }

    //Creates an Adjacency List Graph with the number of vertices that is either directed or undirected 
    public AdjacencyListGraph(int vertices, boolean isDirected) {
        //Creates a vertex for each index
        for (int i = 0; i < vertices; i++) {
            vertexList.add(new Vertex(i + 1));
        }
        this.isDirected = isDirected;
    }

    //Creates an Adjacency List Graph from a .txt file
    private static AdjacencyListGraph createGraph(File file) throws FileNotFoundException {
        Scanner scan = new Scanner(file); // scans the .txt file

        String graphType = scan.next(); // first line corresponds to graph type
        boolean isDirected = graphType.equals("directed");
        scan.nextLine();
        int numVertices = scan.nextInt(); // second line corresponds to number of vertices
        scan.nextLine();
        int numEdges = scan.nextInt(); // third line corresponds to number of edges

        AdjacencyListGraph adj = new AdjacencyListGraph(numVertices, isDirected); // creates a graph with specified number of vertices

        scan.nextLine();
        for (int i = 0; i < numEdges; i++) { // adds in the edges
            String edge = scan.nextLine();
            String[] inputEdge = edge.split(","); // splits the edge into origin and destination
            int originLabel = Integer.parseInt(inputEdge[0]); // first number corresponds to origin
            int destinationLabel = Integer.parseInt(inputEdge[1]); // second number corresponds to destination
            int usedLabel = Integer.parseInt(inputEdge[2]); // third number corresponds to edge label
            Vertex origin = adj.vertexList.get(originLabel - 1); // translates number to origin vertex object
            Vertex destination = adj.vertexList.get(destinationLabel - 1); // translates number to destination vertex object
            adj.insertEdge(origin, destination, usedLabel); // inserts the edge into the adjacency list graph
            //adj.insertEdge(origin, destination);
        }
        adj.printList(); // prints the resulting adjacency list

        return adj;
    }

    //Inserts an edge with a label in the Adjacency List
    private void insertEdge(Vertex origin, Vertex destination, int label) {
        Edge edge = new Edge(origin, destination, label); // creates an edge with a label
        origin.getEdgeList().add(edge); // adds edge to the edge list of the origin
        if (!isDirected) { // if the edge is not directed
            destination.getEdgeList().add(edge); // add edge to the edge list of the destination
        }
        edgeList.add(edge); // add edge to global edge list
    }

    //Inserts an edge in the Adjacency List
    private void insertEdge(Vertex origin, Vertex destination) {
        Edge edge = new Edge(origin, destination); // creates an edge with no label
        origin.getEdgeList().add(edge); // adds edge to the edge list of the origin
        if (!isDirected) { // if the edge is not directed
            destination.getEdgeList().add(edge); // add edge to the edge list of the destination
        }
        edgeList.add(edge);
    }

    //Removes an edge in the Adjacency List
    private void deleteEdge(Vertex origin, Vertex destination) {
        Edge foundEdge = null;

        // checks the edge list to see if the edge exists
        for (Edge edge : origin.getEdgeList()) {
            if (edge.opposite(destination) == origin) {
                foundEdge = edge;
                break;
            }
        }

        // deletes the edge if found, and removes it from the edge lists
        origin.getEdgeList().remove(foundEdge);
        if (!isDirected) {
            destination.getEdgeList().remove(foundEdge);
        }
        edgeList.remove(foundEdge);
    }

    //Gets the vertex from an index
    private Vertex getVertex(int i){
        return vertexList.get(i-1);
    }

    //Gets the vertex from its label
    private Vertex getVertex(Vertex v){
        return getVertex(v.getLabel());
    }

    //Gets the edge between two vertices
    private Edge getEdge(Vertex origin, Vertex destination) {
        for (Edge edge : origin.getEdgeList()) {
            if ((edge.getOrigin()  == origin && edge.getDestination() == destination && edge.getLabel() == 0) ||
                    (edge.getOrigin() == destination && edge.getDestination() == origin && edge.getLabel() == 1)) {
                return edge;
            }
        }
        for (Edge edge : destination.getEdgeList()) {
            if ((edge.getOrigin()  == origin && edge.getDestination() == destination && edge.getLabel() == 0) ||
                    (edge.getOrigin() == destination && edge.getDestination() == origin && edge.getLabel() == 1)) {
                return edge;
            }
        }
        return null;
    }

    //Gets the edge between two vertices
    private Edge getEdge2(Vertex origin, Vertex destination) {
        for (Edge edge : origin.getEdgeList()) {
            Vertex u = edge.getOrigin();
            Vertex v = edge.getDestination();
            if ((origin == u && destination == v) || (origin == v && destination == u)) {
                return edge;
            }
        }
        return null;
    }

    //Gets the neighboring vertices of a vertex in the Adjacency List
    private ArrayList<Vertex> getNeighbors(Vertex vertex) {
        ArrayList<Vertex> neighborsList = new ArrayList<>();

        // adds the neighboring vertices to the list
        for (Edge newEdge : getVertex(vertex).getEdgeList()) {
            neighborsList.add(newEdge.opposite(getVertex(vertex)));
        }
        return neighborsList;
    }

    //Searches for a vertex to check graph connectivity
    private boolean searchVertex(Vertex vertex) {
        boolean[] marked = new boolean[vertexList.size()];
        boolean isConnected = true;
        marked[vertex.getLabel() - 1] = true;
        ArrayList<Vertex> markedVertices = new ArrayList<>();
        markedVertices.add(vertex);

        while (!markedVertices.isEmpty()) {
            Vertex markedVertex = markedVertices.remove(0);
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

    //Searches for a path between two vertices
    private ArrayList<Vertex> searchPath(Vertex origin, Vertex destination) {
        ArrayList<Vertex> vertexPath = new ArrayList<>();
        boolean[] marked = new boolean[vertexList.size()];
        Vertex[] parent = new Vertex[vertexList.size()];

        marked[origin.getLabel() - 1] = true;
        ArrayList<Vertex> markedVertices = new ArrayList<>();
        markedVertices.add(origin);
        parent[origin.getLabel() - 1] = origin;

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
            return null;
        }
        Vertex currently = destination;
        vertexPath.add(0, destination);
        while (!currently.equals(origin)) {
            currently = parent[currently.getLabel() - 1];
            vertexPath.add(0, currently);
        }
        return vertexPath;
    }

    //Searches for bridges in the Adjacency List
    private void searchBridge(AdjacencyListGraph adj) {
        ArrayList<Edge> edgeCopy = new ArrayList<>(adj.edgeList);

        for (Edge edge : edgeCopy) {
            Vertex origin = edge.getOrigin();
            Vertex destination = edge.getDestination();
            adj.deleteEdge(origin, destination);
            if (!adj.searchVertex(origin)) {
                System.out.print("[");
                System.out.print(edge);
                System.out.print("]");
            }
            adj.insertEdge(origin, destination, edge.getLabel());
        }
    }

    //Creates a residual graph
    private AdjacencyListGraph createResidualGraph() {
        AdjacencyListGraph residualGraph = new AdjacencyListGraph(vertexList.size(), true); // creates a residual graph with same # of vertices

        // inserts edges into the residual graph
        for (Edge edge: edgeList) {
            Vertex origin = edge.getOrigin();
            Vertex newOrigin = residualGraph.getVertex(origin);

            Vertex destination = edge.getDestination();
            Vertex newDestination = residualGraph.getVertex(destination);

            // inserts the edge based on the label
            if (edge.getLabel() == 0) {
                residualGraph.insertEdge(newOrigin, newDestination);
            } else {
                residualGraph.insertEdge(newDestination, newOrigin);
            }
        }
        return residualGraph;
    }

    //Augments the flow of edges within a path
    private void augmentFlow(ArrayList<Vertex> vertexPath) {

        // for each vertex in the path
        for (int i = 0; i < vertexPath.size() - 1; i++) {
            Vertex origin = vertexPath.get(i);
            Vertex destination = vertexPath.get(i + 1);
            Edge edge = getEdge(getVertex(origin), getVertex(destination)); // gets the edge connecting the origin and destination
            if (edge.getLabel() == 0) {
                edge.setLabel(1); // labels unused edge to used edge
            } else {
                edge.setLabel(0);// labels used edge to unused edge
            }
        }
    }

    //Finds the edge connectivity of a graph
    private static void edgeConnectivity(Scanner input, AdjacencyListGraph adj) {
        System.out.println("Specify the origin vertex: ");
        int originLabel = input.nextInt();
        Vertex origin = adj.vertexList.get(originLabel - 1);
        System.out.println("Specify the destination vertex: ");
        int destinationLabel = input.nextInt();
        Vertex destination = adj.vertexList.get(destinationLabel - 1);
        int pathCount = adj.getNumberOfPaths(origin, destination);

        System.out.println("The edge connectivity is " + pathCount + ".");
    }

    //Finds the number of paths between two vertices
    private int getNumberOfPaths(Vertex origin, Vertex destination) {
        for (Edge edge : edgeList) {
            edge.setLabel(0);
        }
        int pathCount = 0;
        AdjacencyListGraph residualGraph;
        while (true) {
            residualGraph = createResidualGraph();
            Vertex newOrigin = residualGraph.getVertex(origin);
            Vertex newDestination = residualGraph.getVertex(destination);
            ArrayList<Vertex> vertexPath = residualGraph.searchPath(newOrigin, newDestination);
            if (vertexPath == null) {
                break;
            }
            augmentFlow(vertexPath);
            pathCount++;
        }

        return pathCount;
    }

    //Creates a directed graph
    private AdjacencyListGraph createDirectedGraph() {
        AdjacencyListGraph directedGraph = new AdjacencyListGraph(vertexList.size() + (2*edgeList.size()), true);
        for (int i = 0; i < edgeList.size(); i++) {
            Edge e = edgeList.get(i);
            Vertex u = directedGraph.vertexList.get(e.getOrigin().getLabel() - 1);
            Vertex v = directedGraph.vertexList.get(e.getDestination().getLabel() - 1);
            Vertex w = directedGraph.vertexList.get(vertexList.size() + 2*i);
            Vertex x = directedGraph.vertexList.get(vertexList.size() + (2*i) + 1);
            directedGraph.insertEdge(u, w);
            directedGraph.insertEdge(w, v);
            directedGraph.insertEdge(v, x);
            directedGraph.insertEdge(x, u);
        }
        return directedGraph;
    }

    //Finds the global minimum number of paths
    private int getMinNumberOfPaths() {
        AdjacencyListGraph copyGraph = createDirectedGraph();
        int m = Integer.MAX_VALUE;
        for (int i = 1; i < vertexList.size(); i++) {
            m = min(m, copyGraph.getNumberOfPaths(copyGraph.vertexList.get(0), copyGraph.vertexList.get(i)));
        }
        System.out.println("Minimum number of paths is " + m);
        return m;
    }

    //An implementation of Kruskal's algorithm
    private ArrayList<Edge> kruskal() {

        AdjacencyListGraph spanningTree = new AdjacencyListGraph(vertexList.size());

        Partition tree = new Partition();

        //gives weights to the edges in ascending order (assuming it's already sorted in the .txt file)
        //for (int i = 0; i < edgeList.size(); i++) {
        //    edgeList.get(i).setLabel(i + 1);
        //}

        for (Vertex vertex : vertexList) {
            tree.makeCluster(vertex);
        }

        // sorts the edges by weight/label
        edgeList.sort(Comparator.comparing(Edge::getLabel));

        int i = 0;
        while (spanningTree.edgeList.size() < vertexList.size() - 1) {
            Edge edge = edgeList.get(i);
            if (!tree.isSameCluster(edge.getOrigin(), edge.getDestination())) {
                spanningTree.edgeList.add(edge);
                tree.union(edge.getOrigin(), edge.getDestination());
            }
            i++;
        }

        for (Edge edge : spanningTree.edgeList) {
            System.out.println(edge + ": weight " + edge.getLabel());
        }

        return spanningTree.edgeList;
    }

    //Checks if there are two spanning trees in the graph
    private boolean hasTwoSpanningTrees() {

        AdjacencyListGraph graph1 = new AdjacencyListGraph(vertexList.size());
        AdjacencyListGraph graph2 = new AdjacencyListGraph(vertexList.size());

        for (Edge edge : edgeList) {
            edge.setForest(0);
        }

        for (Edge edge : edgeList) {
            for (Edge e : edgeList) {
                e.setParent(null);
            }
            Queue<Edge> edgeQueue = new LinkedList<>();
            edgeQueue.add(edge);
            while (!edgeQueue.isEmpty()) {
                Edge chosenEdge = edgeQueue.remove();
                int i = chosenEdge.getForest();
                AdjacencyListGraph forest = null;
                if (i == 0 || i == 2) { // for initial insert
                    forest = graph1;
                } else { // adding to the opposite forest
                    forest = graph2;
                }
                if (processEdge(chosenEdge, edgeQueue, forest)) {
                    Edge currently = chosenEdge;
                    while (!currently.equals(edge)) {
                        int k = currently.getForest();
                        if (k == 1) {
                            exchange(graph2, graph1, currently);
                            currently.setForest(2);
                        } else {
                            exchange(graph1, graph2, currently);
                            currently.setForest(1);
                        }
                        currently = currently.getParent();
                    }
                    Vertex origin = graph1.getVertex(currently.getOrigin());
                    Vertex destination = graph1.getVertex(currently.getDestination());
                    graph1.insertEdge(origin, destination, 0);
                    currently.setForest(1);
                    break;
                }
            }
        }
        System.out.println(graph1.edgeList);
        System.out.println(graph2.edgeList);
        boolean tree1 = graph1.edgeList.size() == vertexList.size() - 1;
        boolean tree2 = graph2.edgeList.size() == vertexList.size() - 1;
        System.out.println(tree1 && tree2);
        return tree1 && tree2;
    }

    //Checks if there is a fundamental cycle and adds the edges to the queue if there is
    public boolean processEdge(Edge e, Queue<Edge> queue, AdjacencyListGraph forest) {
        Vertex origin = e.getOrigin();
        Vertex destination = e.getDestination();
        Vertex newOrigin = forest.getVertex(origin);
        Vertex newDestination = forest.getVertex(destination);

        ArrayList<Vertex> vertexPath = forest.searchPath(newOrigin, newDestination);
        if (vertexPath == null) {
            return true;
        } else {
            for (int i = 0; i < vertexPath.size() - 1; i++) {
                Vertex u = getVertex(vertexPath.get(i));
                Vertex v = getVertex(vertexPath.get(i+1));
                Edge edge = getEdge2(u, v);
                if (edge.getParent() == null) {
                    edge.setParent(e);
                    queue.add(edge);
                }
            }
            return false;
        }
    }

    //Exchanges the edge between two forests
    private void exchange(AdjacencyListGraph graph1, AdjacencyListGraph graph2, Edge currently) {

        graph1.insertEdge(graph1.getVertex(currently.getOrigin()), graph1.getVertex(currently.getDestination()), 0);

        graph2.deleteEdge(graph2.getVertex(currently.getOrigin()), graph2.getVertex(currently.getDestination()));

    }

    //Separate method used to test the 4-regular graphs
    private static boolean testAlgorithm(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.println(line);
            String[] lineSplit = line.split(" ");
            int numVertices = Integer.parseInt(lineSplit[0]);
            String connection = lineSplit[1];
            AdjacencyListGraph graph = new AdjacencyListGraph(numVertices, false);

            String[] splitConnection = connection.split(",");

            for (int i = 0; i < splitConnection.length; i++) {
                for (int j = 0; j < splitConnection[i].length(); j++) {
                    int destinationLabel = splitConnection[i].charAt(j) - 'a' + 1;
                    Vertex origin = graph.getVertex(i + 1);
                    Vertex destination = graph.getVertex(destinationLabel);
                    if (i + 1 < destinationLabel) {
                        graph.insertEdge(origin, destination);
                    }
                }
            }

            if (graph.getMinNumberOfPaths() != 4) {
                if (!graph.hasTwoSpanningTrees()) {
                    System.out.println(line);
                    return false;
                }
            }
        }
        return true;
    }

    //Prints the current Adjacency List
    public void printList() {
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

    //Main method
    public static void main(String[] args) throws FileNotFoundException {
        boolean close = false;
        File file = new File(args[0]);
        //testAlgorithm(file); // --> for 4reg-graphs.txt -> uncomment this line and comment out the rest of the main method below if testing this file
        AdjacencyListGraph graph = createGraph(file);

        while (!close) {
            System.out.print("\nPlease type one of the following actions to perform: ");
            Scanner input = new Scanner(System.in);
            System.out.println("'search bridge', 'edge connectivity', 'global min', 'kruskal', 'spanning tree', 'exit'");
            String userChoice = input.nextLine();
            switch (userChoice.toLowerCase()) {
                case "search bridge" -> graph.searchBridge(graph);
                case "edge connectivity" -> edgeConnectivity(input, graph);
                case "global min" -> graph.getMinNumberOfPaths();
                case "kruskal" -> graph.kruskal();
                case "spanning tree" -> graph.hasTwoSpanningTrees();
                case "exit" -> close = true;
                default -> System.out.println("No such action exists.");
            }
        }
        System.out.println("Thank you for trying this program!");
    }
}
