import java.util.*;

/** 
 * Adharsh Ranganathan
 * 2/18/17
 * Section AA
 * TA: Chloe Lathe
 * 
 * A representation of a graph.
 * Assumes that we do not have negative cost edges in the graph.
 */
public class MyGraph implements Graph {
    private Map<Vertex, Collection<Edge>> graphMap;
    private final int MAX = Integer.MAX_VALUE;

    /**
     * Creates a MyGraph object with the given collection of vertices
     * and the given collection of edges.
     * @param v a collection of the vertices in this graph
     * @param e a collection of the edges in this graph
     * @throws IllegalArgumentException if Edge source does not exist
     * or if edge already exists with different weight.
     */
    public MyGraph(Collection<Vertex> v, Collection<Edge> e) {
        this.graphMap = new HashMap<Vertex, Collection<Edge>>();
        for(Vertex vertex: v) {
            if(!this.graphMap.containsKey(v)) {
                //Deep copy the vertices
                this.graphMap.put(this.copyVertex(vertex), new HashSet<Edge>());
            }
        }

        for(Edge edge: e) {
            if(!this.graphMap.containsKey(edge.getSource())) {
                throw new IllegalArgumentException("Edge source does not exist.");
            } 

            Collection<Edge> edges = this.graphMap.get(edge.getSource());
            //Checking if edge exists with different weight
            for(Edge curEdge: edges) {
                boolean sameSource = curEdge.getSource().equals(edge.getSource());
                boolean sameDestination = curEdge.getDestination().equals(edge.getDestination());
                boolean difWeight = curEdge.getWeight() != edge.getWeight();

                if(sameSource && sameDestination && difWeight) {
                    throw new IllegalArgumentException("Edge already exists with different weight");
                }
            }

            //Adds edges if they are complete duplicates or if they are unique
            edges.add(this.copyEdge(edge));
            this.graphMap.put(copyVertex(edge.getSource()), edges);
        }
    }

    /** 
     * Return the collection of vertices of this graph
     * @return the vertices as a collection (which is anything iterable)
     */
    @Override
    public Collection<Vertex> vertices() {
        Collection<Vertex> vertices = new HashSet<Vertex>();
        for(Vertex v: this.graphMap.keySet()) {
            vertices.add(copyVertex(v));
        }

        return vertices;
    }

    /*
     * @param Vertex v
     * @return Vertex that is a copy of v
     * */
    private Vertex copyVertex(Vertex v) {
        Vertex copy = new Vertex(v.getLabel());
        return copy;
    }

    /*
     * @param Edge e
     * @return Edge that is a copy of e
     * */
    private Edge copyEdge(Edge e) {
        Vertex source = this.copyVertex(e.getSource());
        Vertex dest = this.copyVertex(e.getDestination());
        Edge copy = new Edge(source, dest, e.getWeight());
        return copy;
    }

    /** 
     * Return the collection of edges of this graph
     * @return the edges as a collection (which is anything iterable)
     */
    @Override
    public Collection<Edge> edges() {
        Collection<Edge> allEdges = new HashSet<Edge>();
        for(Vertex v: this.graphMap.keySet()) {
            Collection<Edge> edges = this.graphMap.get(v);
            for(Edge e: edges) {
                allEdges.add(this.copyEdge(e));
            }
        }

        return allEdges;
    }

    /**
     * Return a collection of vertices adjacent to a given vertex v.
     *   i.e., the set of all vertices w where edges v -> w exist in the graph.
     * Return an empty collection if there are no adjacent vertices.
     * @param v one of the vertices in the graph
     * @return an iterable collection of vertices adjacent to v in the graph
     * @throws IllegalArgumentException if v does not exist.
     */
    @Override
    public Collection<Vertex> adjacentVertices(Vertex v) {
        if(!this.graphMap.containsKey(v)) {
            throw new IllegalArgumentException();
        }

        Collection<Vertex> adjVertices = new HashSet<Vertex>();
        Collection<Edge> edges = this.graphMap.get(v);
        for(Edge e: edges) {
            adjVertices.add(this.copyVertex(e.getDestination()));
        }

        return adjVertices;
    }

    /**
     * Test whether vertex b is adjacent to vertex a (i.e. a -> b) in a directed graph.
     * Assumes that we do not have negative cost edges in the graph.
     * @param a one vertex
     * @param b another vertex
     * @return cost of edge if there is a directed edge from a to b in the graph, 
     * return -1 otherwise.
     * @throws IllegalArgumentException if a or b do not exist.
     */
    @Override
    public int edgeCost(Vertex a, Vertex b) {
        if(!this.graphMap.containsKey(a) || !this.graphMap.containsKey(b)) {
            throw new IllegalArgumentException();
        }

        Collection<Edge> edges = this.graphMap.get(a);
        for(Edge e: edges) {
            //Assumed that source of edge is vertex "a"
            if(e.getDestination().equals(b)) {
                return e.getWeight();
            }
        }

        return -1;       
    }

    /**
     * Returns the shortest path from a to b in the graph, or null if there is
     * no such path.  Assumes all edge weights are nonnegative.
     * Uses Dijkstra's algorithm.
     * @param a the starting vertex
     * @param b the destination vertex
     * @return a Path where the vertices indicate the path from a to b in order
     *   and contains a (first) and b (last) and the cost is the cost of 
     *   the path. Returns null if b is not reachable from a.
     * @throws IllegalArgumentException if a or b does not exist.
     */
    public Path shortestPath(Vertex a, Vertex b) {
        if(!this.graphMap.containsKey(a) || !this.graphMap.containsKey(b)) {
            throw new IllegalArgumentException("One of the vertices does not exist.");
        }

        if(a.equals(b)) {
            List<Vertex> list = new ArrayList<Vertex>();
            list.add(a);
            return new Path(list, 0);
        }

        //Use HashSet for visited so contains(v) is O(1)
        Set<Vertex> visited = new HashSet<Vertex>();
        //Use List so it easy to build Path object
        List<Vertex> notVisited = new ArrayList<Vertex>();
        Map<Vertex, Vertex> prevVertex = new HashMap<Vertex, Vertex>();
        Map<Vertex, Integer> distances = new HashMap<Vertex, Integer>();

        for(Vertex v: this.graphMap.keySet()) {
            //Initializes a with distance = 0 so it is 
            //visited first
            if(v.equals(a)) {
                distances.put(v,  0);
            } else {
                distances.put(v, MAX);
            }

            notVisited.add(v);
            prevVertex.put(v, null);
        }

        while(!notVisited.isEmpty()) {     
            Vertex currentVertex = notVisited.get(0);
            //Find unvisited vertex with shortest distance from a
            for(Vertex v: notVisited) {
                if(distances.get(v) < distances.get(currentVertex)) {
                    currentVertex = v;
                }
            }

            Collection<Vertex> adjacent = this.adjacentVertices(currentVertex);
            for(Vertex v: adjacent) {
                int edgeCost = this.edgeCost(currentVertex, v);
                //Calculates the distance from a by adding the
                //edge cost of v
                int totalCost = distances.get(currentVertex) + edgeCost;

                //Updates shortest distance from a if v's current
                //distance from a is smaller than it's old distance
                //from a
                if(!visited.contains(v) && distances.get(v) > totalCost) {
                    distances.put(v, totalCost);
                    prevVertex.put(v, currentVertex);
                }
            }

            //Add to visited after all adjacent vertices
            //have been visited
            visited.add(currentVertex); 
            notVisited.remove(currentVertex); 
        }

        //If there is no Vertex before b, path is incomplete
        if(prevVertex.get(b) == null) {
            return null;
        }

        Vertex start = b;
        //Linked list so adding to front is fast
        List<Vertex> pathList = new LinkedList<Vertex>();
        pathList.add(0, start);

        //Go from b to a by starting at b and accessing
        //previous vertices
        while(!start.equals(a)) {
            Vertex v = prevVertex.get(start);
            //Adds to the front of the list
            pathList.add(0, v);
            start = v;
        }

        //Cost of the path is b's distance from a
        Path path = new Path(pathList, distances.get(b));
        return path;
    }
}
