package com.codechef.challange.byteland.main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.codechef.challange.byteland.model.City;
import com.codechef.challange.byteland.model.Graph;
import com.codechef.challange.byteland.model.Path;
import com.codechef.challange.byteland.solution.BytelandCities;

/**
 * @author Enes
 *
 */
public class BytelandMain {

	private static List<City> cities;
	private static List<Path> pathes;

	private static void fillCityList(Integer numberOfCities) {

		for (int i = 0; i < numberOfCities; i++) {
			City location = new City("City_" + i, "City_" + i);
			cities.add(location);
		}

	}

	public static void main(String[] args) throws Exception {

		cities = new ArrayList<City>();
		pathes = new ArrayList<Path>();

		boolean isContinue = true;

		while (isContinue) {

			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			System.out.print("Please enter the number of the test cases for Byteland Solution :");
			int numTests = Integer.parseInt(in.readLine());

			if (numTests > 1000) {
				System.out.println("number of test cases should be smaller than 1000");
				continue;
			}

			for (int t = 0; t < numTests; t++) {
				System.out.print("Please enter the number of cities in Bytland :");
				int numberOfCities = Integer.parseInt(in.readLine());
				if (!(2 <= numberOfCities && numberOfCities <= 600)) {
					System.out.println("number of cities should be between [2, 600]");
					continue;
				}

				fillCityList(numberOfCities);

				System.out.println("Please enter the route sequence : { length must be " + --numberOfCities
						+ " for your input and split with spaces for each one }");
				String[] edgeStrings = in.readLine().split("\\s+");

				if (edgeStrings.length != cities.size() - 1) {
					int requiredSize = cities.size() - 1;
					throw new Exception("Routes size must be " + requiredSize);
				}

				for (int x = 0; x < numberOfCities; x++) {

					int a = Integer.parseInt(edgeStrings[x]);
					int b = x + 1;

					addPath("Path_" + x, a, b, 1);
				}
				isContinue = false;
			}

		} // end Of While

		Graph graph = new Graph(cities, pathes);

		BytelandCities bytland = new BytelandCities(graph);

		bytland.startUnification();

		Map<City, Integer> popularCity = bytland.getPopularCity(bytland.getMaxValueFromMap());
		Set<Entry<City, Integer>> entrySet = popularCity.entrySet();

		for (Entry<City, Integer> entry : entrySet) {
			System.out.println("Global city is : " + entry.getKey() + "\n minumum step is : " + entry.getValue());
			break;
		}

	}

	private static void addPath(String pathId, int sourceLocNo, int destLocNo, int distance) {
		Path path = new Path(pathId, cities.get(sourceLocNo), cities.get(destLocNo), distance);
		pathes.add(path);
	}

}
