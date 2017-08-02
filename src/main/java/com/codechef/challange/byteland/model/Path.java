package com.codechef.challange.byteland.model;


/**
 * @author Enes
 *
 */
public class Path {
	private final String id;
	private final City source;
	private final City destination;
	private final int distance;

	public Path(String id, City source, City destination, int weight) {
		this.id = id;
		this.source = source;
		this.destination = destination;
		this.distance = weight;
	}

	public String getId() {
		return id;
	}

	public City getDestination() {
		return destination;
	}

	public City getSource() {
		return source;
	}

	public int getWeight() {
		return distance;
	}

	@Override
	public String toString() {
		return source + " " + destination;
	}

}