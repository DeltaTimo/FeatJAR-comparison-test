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

import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.init.FMCoreLibrary;
import de.ovgu.featureide.fm.core.init.LibraryManager;
import de.ovgu.featureide.fm.core.io.manager.FeatureModelManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.prop4j.*;

import java.io.FileNotFoundException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * An example usage of the FeatureIDE library for feature-model analysis.
 *
 * @author
 */
public class FeatureModelAnalysisTests {

	private static final List<String> modelNames = Arrays.asList( //
			"FeatureModelAnalysis/basic.xml",
			"FeatureModelAnalysis/simple.xml",
			"FeatureModelAnalysis/car.xml",
			"FeatureModelAnalysis/hidden.xml"
	);
	private static final List<IFeatureModel> featureModels = new ArrayList<>();
	private static FeatureIDEAnalyse library1;
	private static FeatureIDEAnalyse library2;

	private static String getPathFromResource(String resource) throws FileNotFoundException {
		final URL resourceURL = FeatureModelAnalysisTests.class.getClassLoader().getResource(resource);
		if (resourceURL == null) {
			throw new FileNotFoundException(resource);
		} else {
			return resourceURL.getPath().substring(1);
		}
	}

	@BeforeAll
	public static void setup() {
		library1 = new FeatureIDEAnalyse();
		library2 = new FeatureIDEAnalyse();
		modelNames.forEach(module -> {
			try {
				featureModels.add(loadModel(getPathFromResource(module)));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		});
	}

	private static IFeatureModel loadModel(String filePath) {
		LibraryManager.registerLibrary(FMCoreLibrary.getInstance());
		Path path = Paths.get(filePath);
		return FeatureModelManager.load(path);
	}

	@Test
	public void testIsTautology() {
		// queries for tautology
		Node basic = new Implies(new Literal("Root"), new Literal("C"));
		Node simple = new Implies(new Literal("Base"), new Literal("F2"));
		Node car = new Implies(new Literal("Navigation"), new Literal("Ports"));
		Node hidden =  new And(new Literal("MainGpl"), new Not(new Literal("UndirectedWithNeighbors")));

		HashMap<IFeatureModel, Node> map = new HashMap<>();
		map.put(featureModels.get(0), basic);
		map.put(featureModels.get(1), simple);
		map.put(featureModels.get(2), car);
		map.put(featureModels.get(3), hidden);

		map.entrySet()
				.stream()
				.forEach(entry -> assertEquals(Result.get(() -> library1.isTautology(entry.getKey(), entry.getValue())), Result.get(() -> library2.isTautology(entry.getKey(), entry.getValue()))));
	}

	@Test
	public void testIsVoid() {
		featureModels.forEach(featureModel -> assertEquals(Result.get(() -> library1.isVoid(featureModel)), Result.get(() -> library2.isVoid(featureModel))));
	}

	@Test
	public void testCoreFeatures() {
		featureModels.forEach(featureModel -> assertEquals(Result.get(() -> library1.coreFeatures(featureModel)), Result.get(() -> library2.coreFeatures(featureModel))));
	}

	@Test
	public void testDeadFeatures() {
		featureModels.forEach(featureModel -> assertEquals(Result.get(() -> library1.deadFeatures(featureModel)), Result.get(() -> library2.deadFeatures(featureModel))));
	}

	@Test
	public void testFalseOptional() {
		featureModels.forEach(featureModel -> assertEquals(Result.get(() -> library1.falseOptional(featureModel)), Result.get(() -> library2.falseOptional(featureModel))));
	}

	@Test
	public void testRedundantConstraints() {
		featureModels.forEach(featureModel -> assertEquals(Result.get(() -> library1.redundantConstraints(featureModel)), Result.get(() -> library2.redundantConstraints(featureModel))));
	}

	@Test
	public void testAtomicSet() {
		featureModels.forEach(featureModel -> assertEquals(Result.get(() -> library1.atomicSets(featureModel)), Result.get(() -> library2.atomicSets(featureModel))));
	}

	@Test
	public void testIndeterminedHiddenFeatures() {
		featureModels.forEach(featureModel -> assertEquals(Result.get(() -> library1.indeterminedHiddenFeatures(featureModel)), Result.get(() -> library2.indeterminedHiddenFeatures(featureModel))));
	}

	@Test
	public void testCountSolutions() {
		featureModels.forEach(featureModel -> assertEquals(Result.get(() -> library1.countSolutions(featureModel)), Result.get(() -> library2.countSolutions(featureModel))));
	}
}
