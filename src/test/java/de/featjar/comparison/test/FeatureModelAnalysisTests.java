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

import de.featjar.comparison.test.helper.WrapperFeatureModels;
import de.featjar.comparison.test.helper.featureide.FeatureIDEAnalyse;
import de.featjar.comparison.test.helper.Result;
import de.featjar.comparison.test.helper.featureide.FeatureIDEBase;
import de.ovgu.featureide.fm.core.base.IFeatureModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.prop4j.*;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * An example usage of the FeatureIDE library for feature-model analysis.
 *
 * @author Katjana Herbst
 */
public class FeatureModelAnalysisTests {

	private static final List<String> modelNames = Arrays.asList( //
			"FeatureModelAnalysis/basic.xml",
			"FeatureModelAnalysis/simple.xml",
			"FeatureModelAnalysis/car.xml",
			"FeatureModelAnalysis/hidden.xml"
	);
	private static final List<WrapperFeatureModels> featureModels = new ArrayList<>();
	private static FeatureIDEBase baseOperationsLib1;
	private static FeatureIDEBase baseOperationsLib2;
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
		baseOperationsLib1 = new FeatureIDEBase();
		baseOperationsLib2 = new FeatureIDEBase();
		library1 = new FeatureIDEAnalyse();
		library2 = new FeatureIDEAnalyse();
		modelNames.forEach(module -> {
			// TODO test load in own test class
			try {
				featureModels.add(new WrapperFeatureModels(baseOperationsLib1.load(getPathFromResource(module)), baseOperationsLib2.load(getPathFromResource(module))));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				throw new RuntimeException();
			}
		});
	}

	@Test
	public void testIsTautology() {
		// queries for tautology TODO read from file -> more generic
		Node basic = new Implies(new Literal("Root"), new Literal("C"));
		Node simple = new Implies(new Literal("Base"), new Literal("F2"));
		Node car = new Implies(new Literal("Navigation"), new Literal("Ports"));
		Node hidden =  new And(new Literal("MainGpl"), new Not(new Literal("UndirectedWithNeighbors")));

		HashMap<WrapperFeatureModels, Node> map = new HashMap<>();
		map.put(featureModels.get(0), basic);
		map.put(featureModels.get(1), simple);
		map.put(featureModels.get(2), car);
		map.put(featureModels.get(3), hidden);

		map.entrySet()
				.stream()
				.forEach(entry -> assertEquals(Result.get(() -> library1.isTautology((IFeatureModel) entry.getKey().getFeatureModel1(), entry.getValue())), Result.get(() -> library2.isTautology((IFeatureModel) entry.getKey().getFeatureModel2(), entry.getValue()))));
	}

	@Test
	public void testIsVoid() {
		featureModels.forEach(featureModel -> assertEquals(Result.get(() -> library1.isVoid((IFeatureModel) featureModel.getFeatureModel1())), Result.get(() -> library2.isVoid((IFeatureModel) featureModel.getFeatureModel2()))));
	}

	@Test
	public void testCoreFeatures() {
		featureModels.forEach(featureModel -> assertEquals(Result.get(() -> library1.coreFeatures((IFeatureModel) featureModel.getFeatureModel1())), Result.get(() -> library2.coreFeatures((IFeatureModel) featureModel.getFeatureModel2()))));
	}

	@Test
	public void testDeadFeatures() {
		featureModels.forEach(featureModel -> assertEquals(Result.get(() -> library1.deadFeatures((IFeatureModel) featureModel.getFeatureModel1())), Result.get(() -> library2.deadFeatures((IFeatureModel) featureModel.getFeatureModel2()))));
	}

	@Test
	public void testFalseOptional() {
		featureModels.forEach(featureModel -> assertEquals(Result.get(() -> library1.falseOptional((IFeatureModel) featureModel.getFeatureModel1())), Result.get(() -> library2.falseOptional((IFeatureModel) featureModel.getFeatureModel2()))));
	}

	@Test
	public void testRedundantConstraints() {
		featureModels.forEach(featureModel -> assertEquals(Result.get(() -> library1.redundantConstraints((IFeatureModel) featureModel.getFeatureModel1())), Result.get(() -> library2.redundantConstraints((IFeatureModel) featureModel.getFeatureModel2()))));
	}

	@Test
	public void testAtomicSet() {
		featureModels.forEach(featureModel -> assertEquals(Result.get(() -> library1.atomicSets((IFeatureModel) featureModel.getFeatureModel1())), Result.get(() -> library2.atomicSets((IFeatureModel) featureModel.getFeatureModel2()))));
	}

	@Test
	public void testIndeterminedHiddenFeatures() {
		featureModels.forEach(featureModel -> assertEquals(Result.get(() -> library1.indeterminedHiddenFeatures((IFeatureModel) featureModel.getFeatureModel1())), Result.get(() -> library2.indeterminedHiddenFeatures((IFeatureModel) featureModel.getFeatureModel2()))));
	}

	@Test
	public void testCountSolutions() {
		featureModels.forEach(featureModel -> assertEquals(Result.get(() -> library1.countSolutions((IFeatureModel) featureModel.getFeatureModel1())), Result.get(() -> library2.countSolutions((IFeatureModel) featureModel.getFeatureModel2()))));
	}
}
