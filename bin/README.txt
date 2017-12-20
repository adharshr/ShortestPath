 1. Topological sort: 
 
 A B(1) C D(2) E F G H
 In degree 0: A G
 Removed: A
 
 B(0) C D(1) E F G H
 In degree 0: B, G
 Removed: A, B
 
 C D(0) E(1) F G H
 In degree 0: D, G
 Removed: A, B, D
 
 E(0) C(1) G F
 In degree 0: E G
 Removed: A, B, D, E
 
 C(0) G(0) F(1)
 In degree 0: C, G
 Removed: A, B, D, E, C
 
 G(0) F(1)
 In degree 0: G
 Removed: A, B, D, E, C, G
 
 F(0) 
 In degree 0: F
 
 Final Answer: A, B, D, E, C, G, F
 
 2.
 a. Kruskal's Algorithm:
 
 Edges in sorted order:
 1: BE, CG, DG
 2: AB, DC, EF
 3: AC, BF, EG
 4: AD, FG
 
 Order of edges added to min span tree:
 1. BE
 2. CG
 3. DG
 4. AB
 5. EF
 6. EG
 
 Ignored edges: DC, AC, BF, AD, FG
 
 b. Prim's Algorithm
 
 Order of edges added to min span tree:
 1. AB
 2. BE
 3. EF
 4. EG
 5. GC
 6. GD
 
 table:
 
 Vertex: isVisited, cost, previous
 
 A: Y, 0, null
 B: N, 2, A
 C: 
 D: N, 4, A
 E: N, 3, A
 F:
 G:
 
 A: Y, 0, null
 B: Y, 2, A
 C: 
 D: N, 4, A
 E: N, 1, B
 F: N, 3, B
 G:

 A: Y, 0, null
 B: Y, 2, A
 C: 
 D: N, 4, A
 E: Y, 1, B
 F: N, 2, E
 G: N, 3, E
 
 A: Y, 0, null
 B: Y, 2, A
 C: N, 1, G
 D: N, 1, G
 E: Y, 1, B
 F: Y, 2, E
 G: Y, 3, E
 
 A: Y, 0, null
 B: Y, 2, A
 C: Y, 1, G
 D: Y, 1, G
 E: Y, 1, B
 F: Y, 2, E
 G: Y, 3, E
 
 3. Graph pseudocode:
 
 public Map<Vertex, Set<Vertex>> reverse(Map<Vertex, Set<Vertex>> graph) {
    Map<Vertex, Set<Vertex>> output
    
    for(each key v in graph) {
        Set<Vertex> s = get the value corresponding to v in graph
        
        for(each Vertex v2 in s) {
            if(output does not contain a key for v2) {
                Set<Vertex> s2 // new set
                add v to s2
                output.put(v2, s2)
            } else {
                Set<Vertex> s2 = get value corresponding to v2 in output
                add v to s2
                output.put(v2, s2)
            }
        }
    }
    
    return output
 } 
 
 public Set<Vertex> nextAdjacentVertices(Map<Vertex, Set<Vertex>> graph, Vertex source) {
    Set<Vertex> adj = graph.get(source)
    Set<Vertex> output
    for(each Vertex v in adj) {
        Set<Vertex> adjNeighbors = get value corresponding to v in graph
        output.addAll(adjNeighbors)
    }
    
    return output
 }
 
 Runtimes:
 part a: O(|V|^2)
 part b: O(|V|^2)
 
4. a. No it will not, it will just output the path that was discovered first.
You can modify the algorithm by considering "edges to A" similar 
to how you consider "distance to A." The "fewest edges" can be represented
with a Map similar to how the shortest distance is. 
If there is a tie in the distance to A, compare the "edges to A"
to break such ties.

b. vertices: a, b, c
edges: ab: 3, ac: 1, bc: -10

a is marked as visited, c is the next vertex
Distances are:
a 0
b 3
c 1

c does not have any adjacent vertices so
Djikstras stops.
final shortest distances from a:
a 0
b 3
c 1

However, shortest distance from a to c
is from a to b, then b to c which
has path cost of -7. 

c. vertices: a, b, c
edges: ab: 1, bc: 1, ad: -1
constant: k = 2 added
edges: ab: 3, bc: 3, ad: 1

a 0
b 3
c infinity
d 1

visit b

a 0
b 3
c 6
d 1

subtract k from shortest distances

a 0
b 1
c 4
d -2

shortest distance to c is 2 not 4

This algorithm does not work, because paths with multiple 
edges will have too large of a cost even after the constant
is subtracted from the shortest distance values.

5. I tested my shortestPath method by inputing my own vertices and edges.
I solved for the shortest path by hand and made sure shortestPath outputed
the same path cost. I also outputed the vertices of the outputed path
to make sure the path I solved by hand matched the method's output.
I tested edge cases such as going from a to a and going to a non-existent
path to test the correctness of my output and exceptions.  
I also printed the vertices as they were being visited and the vertices 
of the final path to check if the algorithm was running correctly.   