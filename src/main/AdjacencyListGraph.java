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

    public AdjacencyListGraph(int vertices, boolean isDirected) {
        //Creates a vertex for each vertex
        for (int i = 0; i < vertices; i++) {
            vertexList.add(new Vertex(i + 1));
        }
        this.isDirected = isDirected;
    }

    //Creates an AdjacencyListGraph from a .txt file
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
            //int usedLabel = Integer.parseInt(inputEdge[2]); // third number corresponds to edge label
            Vertex origin = adj.vertexList.get(originLabel - 1); // translates number to origin vertex object
            Vertex destination = adj.vertexList.get(destinationLabel - 1); // translates number to destination vertex object
            //adj.insertEdge(origin, destination, usedLabel); // inserts the edge into the adjacency list graph
            adj.insertEdge(origin, destination);
        }
        adj.printList(); // prints the resulting adjacency list

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
        edgeList.add(edge);
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
        while (numRemove > edgeList.size()) {
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
        for (Edge newEdge : getVertex(vertex).getEdgeList()) {
            neighborsList.add(newEdge.opposite(getVertex(vertex)));
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

        //System.out.print("Path from [" + origin + "] to [" + destination + "]: ");
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
            //System.out.println("There is no path between the specified vertices.");
            return null;
        }
        Vertex currently = destination;
        vertexPath.add(0, destination);
        while (!currently.equals(origin)) {
            currently = parent[currently.getLabel() - 1];
            vertexPath.add(0, currently);
        }
        //System.out.println(vertexPath);
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
        //this.vertexList = residualGraph.vertexList;
        for (Edge edge: edgeList) {
            Vertex origin = edge.getOrigin(); // refers to vertex in original graph
            Vertex newOrigin = residualGraph.getVertex(origin); // sets as new vertex

            Vertex destination = edge.getDestination();
            Vertex newDestination = residualGraph.getVertex(destination);
            if (edge.getLabel() == 0) {
                residualGraph.insertEdge(newOrigin, newDestination);
            } else {
                residualGraph.insertEdge(newDestination, newOrigin);
            }
        }
        //this.edgeList = residualGraph.edgeList;
        return residualGraph;
    }

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

    private Vertex getVertex(int i){
        return vertexList.get(i-1);
    }
    private Vertex getVertex(Vertex v){
        return getVertex(v.getLabel());
    }

    private void augmentFlow(ArrayList<Vertex> vertexPath) { //should be boolean
        for (int i = 0; i < vertexPath.size() - 1; i++) {
            Vertex origin = vertexPath.get(i);
            Vertex destination = vertexPath.get(i + 1);
            Edge edge = getEdge(getVertex(origin), getVertex(destination)); //gets the edge connecting the origin and destination
            if (edge.getLabel() == 0) { //edge.setLabel(1-edge.getLabel()); alternative
                edge.setLabel(1); //labels unused edge to used edge
            } else {
                edge.setLabel(0);// labels used edge to unused edge
            }
        }
    }

    private int getNumberOfPaths(Vertex origin, Vertex destination) {
        for (Edge edge : edgeList) {
            edge.setLabel(0);
        }
        int pathCount = 0;
        AdjacencyListGraph residualGraph;
        while (true) {
            residualGraph = createResidualGraph();
            //residualGraph.printList();
            Vertex newOrigin = residualGraph.getVertex(origin);
            Vertex newDestination = residualGraph.getVertex(destination);
            ArrayList<Vertex> vertexPath = residualGraph.search2(newOrigin, newDestination);
            if (vertexPath == null) {
                break;
            }
            augmentFlow(vertexPath);
            pathCount++;
        }

        return pathCount;
    }

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

    private int getMinNumberOfPaths() {
        AdjacencyListGraph copyGraph = createDirectedGraph();
        int m = Integer.MAX_VALUE;
        for (int i = 1; i < vertexList.size(); i++) {
            m = min(m, copyGraph.getNumberOfPaths(copyGraph.vertexList.get(0), copyGraph.vertexList.get(i)));
        }
        System.out.println("m is " + m);
        return m;
    }

    private ArrayList<Edge> kruskal() {

        AdjacencyListGraph spanningTree = new AdjacencyListGraph(vertexList.size());

        Partition tree = new Partition();
        // gives weights to the edges in ascending order (already sorted)
//        for (int i = 0; i < edgeList.size(); i++) {
//            edgeList.get(i).setLabel(i + 1);
//        }

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

    private boolean spanningTree() {

        Partition forest1 = new Partition();
        Partition forest2 = new Partition();

        AdjacencyListGraph graph1 = new AdjacencyListGraph(vertexList.size());
        AdjacencyListGraph graph2 = new AdjacencyListGraph(vertexList.size());

        for (Vertex vertex : vertexList) {
            forest1.makeCluster(vertex);
            forest2.makeCluster(vertex);
        }

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
                if (i == 0 || i == 2) { //for initial insert
                    forest = graph1;
                } else { //adding to the opposite forest
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
        //System.out.println(graph1.edgeList);
        //System.out.println(graph2.edgeList);
        boolean tree1 = graph1.edgeList.size() == vertexList.size() - 1;
        boolean tree2 = graph2.edgeList.size() == vertexList.size() - 1;
        System.out.println(tree1 && tree2);
        return tree1 && tree2;
    }

    private void exchange(AdjacencyListGraph graph1, AdjacencyListGraph graph2, Edge currently) {

        graph1.insertEdge(graph1.getVertex(currently.getOrigin()), graph1.getVertex(currently.getDestination()), 0);

        graph2.deleteEdge(graph2.getVertex(currently.getOrigin()), graph2.getVertex(currently.getDestination()));

    }

    public boolean processEdge(Edge e, Queue<Edge> queue, AdjacencyListGraph forest) {
        Vertex origin = e.getOrigin();
        Vertex destination = e.getDestination();
        Vertex newOrigin = forest.getVertex(origin);
        Vertex newDestination = forest.getVertex(destination);

        ArrayList<Vertex> vertexPath = forest.search2(newOrigin, newDestination);
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
                if (!graph.spanningTree()) {
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

    public static void main(String[] args) throws FileNotFoundException {
        boolean close = false;
        File file = new File(args[0]);
        //estAlgorithm(file);
        AdjacencyListGraph graph = createGraph(file);
        graph.printList();

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
                case "global min" -> graph.getMinNumberOfPaths();
                case "kruskal" -> graph.kruskal();
                case "spanning tree" -> graph.spanningTree();
                case "exit" -> close = true;
                default -> System.out.println("No such action exists.");
            }
        }
        System.out.println("Thank you for trying this program!");
    }
}