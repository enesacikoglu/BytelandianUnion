package com.codechef.challange.byteland.solution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.codechef.challange.byteland.exception.InvalidInputException;
import com.codechef.challange.byteland.model.City;
import com.codechef.challange.byteland.model.Graph;
import com.codechef.challange.byteland.model.Path;

/**
 * @author Enes
 *
 */
public class BytelandCities {

	private LinkedList<Integer> pathList = new LinkedList<>();

	private Map<City, Map<City, Boolean>> neighboursMap = new HashMap<>();

	private Map<City, LinkedList<City>> routedMap = new HashMap<>();

	private final List<City> cities;
	private final List<Path> pathes;

	private Map<City, City> predecessors;

	public BytelandCities(Graph graph) {

		this.cities = new ArrayList<City>(graph.getVertexes());
		this.pathes = new ArrayList<Path>(graph.getEdges());
		fillNeighbourList(Boolean.FALSE);
		// predecessors = new HashMap<>();
		fillAllCitiesPredecessors();
	}

	private Map<City, City> fillAllCitiesPredecessors() {
		predecessors = new HashMap<>();
		for (Path path : pathes) {
			predecessors.put(path.getDestination(), path.getSource());
		}
		return predecessors;
	}

	/**
	 * @param city,cityID
	 * 
	 * @return return List<City> of the given cityID.
	 */
	private List<City> getNeighboursAsCityList(String cityId) {
		List<City> neighbourList = new ArrayList<>();
		for (Path edge : pathes) {
			if (edge.getSource().getId().equals(cityId)) {
				neighbourList.add(edge.getDestination());
			} else if (edge.getDestination().getId().equals(cityId)) {
				neighbourList.add(edge.getSource());
			}
		}
		return neighbourList;
	}

	/**
	 * @param isVisited
	 * 
	 * @return fill neighbourMap with given boolean Value.
	 */
	public void fillNeighbourList(Boolean isVisited) {
		for (City city : cities) {
			Map<City, Boolean> visitedCities = new HashMap<>();
			List<City> neighboursAsCityList = getNeighboursAsCityList(city.getId());
			for (City visitedCity : neighboursAsCityList) {
				visitedCities.put(visitedCity, isVisited);
			}
			neighboursMap.put(city, visitedCities);
		}

	}

	/**
	 * @param city
	 * 
	 * @return true If startingNode is endNode .
	 */
	public boolean isStartingNodeEndNode(City city) {
		return isEndNode(city);
	}

	/**
	 * 
	 * 
	 * @return GlobalCity and NumberOf step .
	 */
	public Map<City, Integer> startUnification() throws Exception {

		Map<City, Integer> popularCity = Collections.emptyMap();
		if (isBytelandUnionPossible()) {
			popularCity = getPopularCity(getMaxValueFromMap());
			City startingPoint = getCityFromPopularMap(popularCity);
			unifyCitiesRecursivly(startingPoint);
		} else {
			throw new InvalidInputException("Can not generate bytland union with given routes!");
		}
		return popularCity;

	}

	public int getMaxValueFromMap() {
		cities.stream().forEach(t -> {
			pathList.add(neighboursMap.get(t).size());
		});
		Collections.sort(pathList);
		return pathList.getLast();
	}

	public City getCityFromPopularMap(Map<City, Integer> popularMap) {
		City pupularCity = null;
		Set<Entry<City, Integer>> entrySet = popularMap.entrySet();
		for (Entry<City, Integer> entry : entrySet) {
			pupularCity = entry.getKey();
			break;
		}
		return pupularCity;
	}

	public Map<City, Integer> getPopularCity(Integer visitedNum) {
		Map<City, Integer> popularMap = Collections.emptyMap();
		Set<Entry<City, Map<City, Boolean>>> entrySet = neighboursMap.entrySet();
		for (Entry<City, Map<City, Boolean>> entry : entrySet) {
			if (entry.getValue().size() == visitedNum) {
				popularMap = new HashMap<>();
				popularMap.put(entry.getKey(), visitedNum);
				break;
			}
		}
		return popularMap;

	}

	private City unifyCity(City city) {
		City unifiedCity = null;
		for (Path path : pathes) {
			if (path.getSource().equals(city)) {
				neighboursMap.get(city).replace(path.getDestination(), Boolean.TRUE);
				neighboursMap.get(path.getDestination()).replace(city, Boolean.TRUE);
				unifiedCity = path.getDestination();
			}

			else if (path.getDestination().equals(city)) {
				neighboursMap.get(city).replace(path.getSource(), Boolean.TRUE);
				neighboursMap.get(path.getSource()).replace(city, Boolean.TRUE);
				unifiedCity = path.getSource();
			}

		}
		return unifiedCity;

	}

	public boolean allCitiesVisited() {
		for (City city : cities) {
			Map<City, Boolean> map = neighboursMap.get(city);
			Set<Entry<City, Boolean>> entrySet = map.entrySet();
			for (Entry<City, Boolean> entry : entrySet) {
				if (!entry.getValue()) {
					return Boolean.FALSE;
				}

			}
		}
		return Boolean.TRUE;

	}

	public void unifyCitiesRecursivly(City city) throws Exception {
		Map<City, Boolean> cityNeighbours = getCityNeighbours(city);
		Set<Entry<City, Boolean>> entrySet = cityNeighbours.entrySet();
		for (Entry<City, Boolean> entry : entrySet) {
			if (!isCityVisitedFromCity(city, entry.getKey())) {
				setCityAsVisited(city, entry.getKey());
				unifyCity(city);
				putToRoutedMap(city, entry.getKey());
				if (!isEndNode(entry.getKey())) {
					if (isAllNeigboursVisited(city)) {
						unifyCitiesRecursivly(entry.getKey());
					}

				}
			}
		}
	}

	public void putToRoutedMap(City source, City lastVisited) {
		LinkedList<City> routedCity = routedMap.get(source);
		if (routedCity == null) {
			routedCity = new LinkedList<City>();
			routedMap.put(source, routedCity);
		}
		routedCity.add(lastVisited);
	}

	public City getWhoCalledMeLast(City me) {
		City whoCalledMeLast = null;
		Set<Entry<City, LinkedList<City>>> entrySet = routedMap.entrySet();
		for (Entry<City, LinkedList<City>> entry : entrySet) {
			if (entry.getValue().getLast().equals(me)) {
				whoCalledMeLast = entry.getKey();
				break;
			}
		}

		if (whoCalledMeLast == null) {
			whoCalledMeLast = predecessors.get(me);
		}
		return whoCalledMeLast;

	}

	/**
	 * @param city,city
	 * 
	 * @return return True If source visited to Destination.
	 */
	public boolean setCityAsVisited(City source, City dest) {

		for (Path path : pathes) {
			if (path.getSource().equals(source) && path.getDestination().equals(dest)) {
				neighboursMap.get(source).replace(dest, Boolean.TRUE);
				neighboursMap.get(dest).replace(source, Boolean.TRUE);
				return true;
			}

			else if (path.getSource().equals(dest) && path.getDestination().equals(source)) {

				neighboursMap.get(source).replace(dest, Boolean.TRUE);
				neighboursMap.get(dest).replace(source, Boolean.TRUE);
				return true;
			}
		}
		return false;

	}

	/**
	 * @param city
	 * @return return Unvisited neighbors of the city.
	 */
	public List<City> getUnvisitedCityNeighbours(City city) {
		List<City> unvisitedCities = new ArrayList<>();
		Map<City, Boolean> map = neighboursMap.get(city);
		Set<Entry<City, Boolean>> entrySet = map.entrySet();
		for (Entry<City, Boolean> entry : entrySet) {
			if (!entry.getValue()) {
				unvisitedCities.add(entry.getKey());
			}
		}
		return unvisitedCities;

	}

	/**
	 * @param city
	 * @return return visited neighbors of the city.
	 */
	public List<City> getVisitedCityNeighbours(City city) {
		List<City> visitedCities = new ArrayList<>();
		Map<City, Boolean> map = neighboursMap.get(city);
		Set<Entry<City, Boolean>> entrySet = map.entrySet();
		for (Entry<City, Boolean> entry : entrySet) {
			if (entry.getValue()) {
				visitedCities.add(entry.getKey());
			}
		}
		return visitedCities;

	}

	/**
	 * @param city
	 * @return true: If the given city neighbors visited.
	 * @throws Exception
	 */
	public boolean isAllNeigboursVisited(City city) throws Exception {

		Map<City, Boolean> map = neighboursMap.get(city);
		if (!map.isEmpty()) {
			Set<Entry<City, Boolean>> entrySet = map.entrySet();
			for (Entry<City, Boolean> entry : entrySet) {
				if (!entry.getValue()) {
					return Boolean.FALSE;
				}
			}
		} else {
			throw new Exception("No neighbour found for the " + city);
		}
		return Boolean.TRUE;

	}

	/**
	 * @param city
	 *            : subCity
	 * @return parent Node of the City
	 */
	public City getCityPredecessor(City subCity) {
		City city = null;
		for (Path path : pathes) {
			if (path.getDestination().equals(subCity)) {
				city = path.getSource();
				break;
			}

		}
		return city;
	}

	public Map<City, Boolean> getCityNeighbours(City city) {
		Map<City, Boolean> cityList = Collections.emptyMap();

		Map<City, Boolean> cityDestinations = getCityDestinations(city);
		Map<City, Boolean> citySources = getCitySources(city);

		if (!cityDestinations.isEmpty()) {
			cityList = new HashMap<>();
			cityList.putAll(cityDestinations);
		}
		if (!citySources.isEmpty()) {
			if (cityList.isEmpty()) {
				cityList = new HashMap<>();
			}
			cityList.putAll(citySources);
		}

		return cityList;
	}

	/**
	 * @param city
	 * @return return true ,If city end node on the graph.
	 */
	public boolean isEndNode(City city) {

		int num = getCityDestinations(city).size() + getCitySources(city).size();
		if (num == 2) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	/**
	 * @param city
	 * @return return true If city not in the graph.
	 */
	public boolean isDeadNode(City city) {

		int num = getCityDestinations(city).size() + getCitySources(city).size();
		if (num < 2) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	/**
	 * 
	 * @return return true ,If bytland Union not possible for given Graph.
	 */
	public boolean isBytelandUnionPossible() {
		for (City city : cities) {
			if (isDeadNode(city)) {
				return Boolean.FALSE;
			}
		}
		return Boolean.TRUE;
	}

	/**
	 * @param city
	 * @return return source Map<City, Boolean> for given city.
	 */
	public Map<City, Boolean> getCitySources(City city) {
		Map<City, Boolean> cityOfSources = Collections.emptyMap();
		Set<Entry<City, Map<City, Boolean>>> entrySet = neighboursMap.entrySet();
		for (Entry<City, Map<City, Boolean>> entry : entrySet) {
			if (entry.getValue().containsKey(city)) {
				cityOfSources = new HashMap<>();
				cityOfSources.put(entry.getKey(), isCityVisitedFromCity(entry.getKey(), city));
			}
		}
		return cityOfSources;
	}

	/**
	 * @param city
	 *            : source
	 * @param city
	 *            : destination
	 * @return return true If source visited to Destination.
	 */
	private boolean isCityVisitedFromCity(City source, City dest) {
		return neighboursMap.get(source).get(dest);
	}

	/**
	 * @param city
	 * @return return destination Map<City, Boolean> for given city.
	 */
	public Map<City, Boolean> getCityDestinations(City city) {
		Map<City, Boolean> cityOfDestination = Collections.emptyMap();
		cityOfDestination = neighboursMap.get(city);
		return cityOfDestination;
	}

}
