/* FeatureIDE - A Framework for Feature-Oriented Software Development
 * Copyright (C) 2005-2019  FeatureIDE team, University of Magdeburg, Germany
 *
 * This file is part of FeatureIDE.
 *
 * FeatureIDE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FeatureIDE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FeatureIDE.  If not, see <http://www.gnu.org/licenses/>.
 *
 * See http://featureide.cs.ovgu.de/ for further information.
 */
package de.featjar.comparison.test;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * An example usage of the FeatureIDE library for feature-model analysis.
 *
 * @author Thomas Thuem
 */
public class FeatureModelAnalysisTests {

	private final List<String> modelNames = Arrays.asList( //
			"basic",
			"simple",
			"car"
	);

	ITestLibrary library1;
	ITestLibrary library2;

	private String getPathFromResource(String resource) throws FileNotFoundException {
		final URL resourceURL = getClass().getClassLoader().getResource(resource);
		if (resourceURL == null) {
			throw new FileNotFoundException(resource);
		} else {
			return resourceURL.getPath().substring(1);
		}
	}

	@BeforeEach
	public void setup() {
		library1 = new FeatureIDE();
		library2 = new FeatureIDE();
	}

	@Test
	public void isTautology() throws FileNotFoundException {
		String[] basic = {"Root","C"};
		String[] simple = {"Base","F2"};
		String[] car = {"Navigation","Ports"};

		// parameters for Tautologie
		HashMap<String, String[]> map = new HashMap<>();
		map.put(modelNames.get(0), basic);
		map.put(modelNames.get(1), simple);
		map.put(modelNames.get(2), car);

		for (Map.Entry<String, String[]> entry : map.entrySet()) {
			String resource = getPathFromResource("FeatureModelAnalysis/" + entry.getKey() + ".xml");
			assertEquals(Result.get(() -> library1.isTautology(resource, entry.getValue())), Result.get(() -> library2.isTautology(resource, entry.getValue())));
		}
	}

	@Test
	public void isVoid() throws FileNotFoundException {
		for (final String modelName : modelNames) {
			String resource = getPathFromResource("FeatureModelAnalysis/" + modelName + ".xml");
			assertEquals(Result.get(() -> library1.isVoid(resource)), Result.get(() -> library2.isVoid(resource)));
		}
	}

	@Test
	public void coreFeatures() throws FileNotFoundException {
		for (final String modelName : modelNames) {
			String resource = getPathFromResource("FeatureModelAnalysis/" + modelName + ".xml");
			assertEquals(Result.get(() -> library1.coreFeatures(resource)), Result.get(() -> library2.coreFeatures(resource)));
		}
	}

	@Test
	public void deadFeatures() throws FileNotFoundException {
		for (final String modelName : modelNames) {
			String resource = getPathFromResource("FeatureModelAnalysis/" + modelName + ".xml");
			assertEquals(Result.get(() -> library1.deadFeatures(resource)), Result.get(() -> library2.deadFeatures(resource)));
		}
	}

	@Test
	public void falseOptional() throws FileNotFoundException {
		for (final String modelName : modelNames) {
			String resource = getPathFromResource("FeatureModelAnalysis/" + modelName + ".xml");
			assertEquals(Result.get(() -> library1.falseOptional(resource)), Result.get(() -> library2.falseOptional(resource)));
		}
	}

	@Test
	public void redundantConstraints() throws FileNotFoundException {
		for (final String modelName : modelNames) {
			String resource = getPathFromResource("FeatureModelAnalysis/" + modelName + ".xml");
			assertEquals(Result.get(() -> library1.redundantConstraints(resource)), Result.get(() -> library2.redundantConstraints(resource)));
		}
	}

	@Test
	public void atomicSet() throws FileNotFoundException {
		for (final String modelName : modelNames) {
			String resource = getPathFromResource("FeatureModelAnalysis/" + modelName + ".xml");
			assertEquals(Result.get(() -> library1.atomicSets(resource)), Result.get(() -> library2.atomicSets(resource)));
		}
	}

	@Test
	public void indeterminedHiddenFeatures() throws FileNotFoundException {
		for (final String modelName : modelNames) {
			String resource = getPathFromResource("FeatureModelAnalysis/" + modelName + ".xml");
			assertEquals(Result.get(() -> library1.indeterminedHiddenFeatures(resource)), Result.get(() -> library2.indeterminedHiddenFeatures(resource)));
		}
	}
}
