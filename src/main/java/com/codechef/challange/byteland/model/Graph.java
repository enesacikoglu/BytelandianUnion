package com.codechef.challange.byteland.model;

import java.util.List;



/**
 * @author Enes
 *
 */
public class Graph {
    private final List<City> vertexes;
    private final List<Path> edges;

    public Graph(List<City> vertexes, List<Path> edges) {
        this.vertexes = vertexes;
        this.edges = edges;
    }

    public List<City> getVertexes() {
        return vertexes;
    }

    public List<Path> getEdges() {
        return edges;
    }

	@Override
	public String toString() {
		return "Graph [vertexes=" + vertexes + ", edges=" + edges + "]";
	}
    
   

}