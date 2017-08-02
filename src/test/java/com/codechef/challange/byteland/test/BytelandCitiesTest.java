package com.codechef.challange.byteland.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.codechef.challange.byteland.model.City;
import com.codechef.challange.byteland.model.Graph;
import com.codechef.challange.byteland.model.Path;
import com.codechef.challange.byteland.solution.BytelandCities;

/**
 * @author Enes
 *
 */
public class BytelandCitiesTest {

	private List<City> cities;
	private List<Path> pathes;

	private final String[] ROUTES = { "0 1", "0 1 2 0 0 3 3", "0 1 1 1 1 0 2 2", "0 1 1 1 0 1 0 1 2 3 1 1 1",
			"0 1 0 1 0 1 0", "0 2 1 0 1 0 0 2 2 2 1 1", "0 1 2 2 2 2 2 2 1 1", "0 2 3 2 2 2 2 2 2 5 0 2 1 1" };

	private final String[] POSSIBLE_UNIONS = { "0 1 2", "0 1 2 0 0 3 3", "0 1 1 1 1 0 2 2" };

	private final String[] IMPOSSIBLE_UNIONS = { "5 5 5 5 6 4", "3 3 3 3 4 1 1 1", "3 2 1" };

	private final int[] EXPECTED_RESULTS = { 2, 3, 5, 9, 4, 5, 7, 8 };

	private Graph graph;

	private final int TEST_CASE = new Random().nextInt(1000 - 1) + 1;

	@Before
	public void setUp() {
		cities = new ArrayList<City>();
		pathes = new ArrayList<Path>();
	}

	@Test
	public void testMinumumStep() {

		for (int i = 0; i < TEST_CASE; i++) {

			for (int k = 0; k < ROUTES.length; k++) {

				String[] split = ROUTES[k].split("\\s+");

				int numberOfCities = split.length + 1;
				initLists(numberOfCities);

				for (int j = 0; j < split.length; j++) {

					int a = Integer.parseInt(split[j]);
					int b = j + 1;

					addPath("Path_" + j, a, b, 1);
				}

				graph = new Graph(cities, pathes);
				BytelandCities bytland = new BytelandCities(graph);

				int actual = bytland.getMaxValueFromMap();

				Assert.assertTrue(bytland.isBytelandUnionPossible());

				Assert.assertEquals(EXPECTED_RESULTS[k], actual);

				// In Every TestCase,clear lists...
				cities.clear();
				pathes.clear();

			}

		}

	}

	@Test
	public void testBytlandPossible() {

		for (int i = 0; i < TEST_CASE; i++) {

			for (int k = 0; k < POSSIBLE_UNIONS.length; k++) {

				String[] split = POSSIBLE_UNIONS[k].split("\\s+");

				int numberOfCities = split.length + 1;
				initLists(numberOfCities);

				for (int j = 0; j < split.length; j++) {

					int a = Integer.parseInt(split[j]);
					int b = j + 1;

					addPath("Path_" + j, a, b, 1);
				}

				graph = new Graph(cities, pathes);
				BytelandCities bytland = new BytelandCities(graph);

				Assert.assertTrue(bytland.isBytelandUnionPossible());

				// In Every TestCase,clear lists...
				cities.clear();
				pathes.clear();

			}

		}

	}

	@Test
	public void testBytlandImPossible() {

		for (int i = 0; i < TEST_CASE; i++) {

			for (int k = 0; k < IMPOSSIBLE_UNIONS.length; k++) {

				String[] split = IMPOSSIBLE_UNIONS[k].split("\\s+");

				int numberOfCities = split.length + 1;
				initLists(numberOfCities);

				for (int j = 0; j < split.length; j++) {

					int a = Integer.parseInt(split[j]);
					int b = j + 1;

					addPath("Path_" + j, a, b, 1);
				}

				graph = new Graph(cities, pathes);
				BytelandCities bytland = new BytelandCities(graph);

				Assert.assertFalse(bytland.isBytelandUnionPossible());

				// In Every TestCase,clear lists...
				cities.clear();
				pathes.clear();

			}

		}

	}

	private void initLists(Integer numberOfCities) {
		for (int i = 0; i < numberOfCities; i++) {
			City location = new City("City_" + i, "City_" + i);
			cities.add(location);
		}
	}

	private void addPath(String pathId, int sourceLocNo, int destLocNo, int distance) {
		Path path = new Path(pathId, cities.get(sourceLocNo), cities.get(destLocNo), distance);
		pathes.add(path);
	}

}
