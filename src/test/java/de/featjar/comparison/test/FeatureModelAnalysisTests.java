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

import de.featjar.comparison.test.helper.IAnalyses;
import de.featjar.comparison.test.helper.Wrapper.LibraryObject;
import de.featjar.comparison.test.helper.Wrapper.Wrapper;
import de.featjar.comparison.test.helper.Wrapper.WrapperLibrary;
import de.featjar.comparison.test.helper.featjar.FeatJARAnalyse;
import de.featjar.comparison.test.helper.featjar.FeatJARBase;
import de.featjar.comparison.test.helper.featureide.FeatureIDEAnalyse;
import de.featjar.comparison.test.helper.featureide.FeatureIDEBase;
import de.featjar.formula.structure.formula.IFormula;
import de.ovgu.featureide.fm.core.base.IFeatureModel;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.prop4j.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * An example usage of the FeatureIDE library and the FeatJAR library for feature-model analysis.
 * includes the tests between the two libraries and the comparison of the test results.
 *
 * @author Katjana Herbst
 * @see IAnalyses
 * @see FeatureIDEAnalyse
 * @see FeatJARAnalyse
 * @see org.junit.jupiter.api
 */
public class FeatureModelAnalysisTests extends ATest{

	 /**
	  * array that contains the names + location of the featuremodels files
	 */
	private static final List<String> MODEL_NAMES = Arrays.asList( //
			"FeatureModelAnalysis/basic.xml",
			"FeatureModelAnalysis/simple.xml",
			"FeatureModelAnalysis/car.xml",
			"FeatureModelAnalysis/hidden.xml"
	);

	 /**
	  * List of Wrapper for loaded featuremodels of the two libraries
	  * @see WrapperLibrary
	 */
	private static final List<WrapperLibrary> featureModels = new ArrayList<>();
	private static FeatureIDEBase baseOperationsLib1;
	private static FeatJARBase baseOperationsLib2;
	private static FeatureIDEAnalyse library1;
	private static FeatJARAnalyse library2;

	 /**
	  * executed before the tests begin
	  * uses the base operations of the libraries to load the featuremodel in the corresponding format
	  * @see FeatureIDEBase base operation of the FeatureIDE library
	  * @see FeatJARBase base operation of the FeatJAR library
	  * @see WrapperLibrary Object that contains two LibraryObjects
	  * @see LibraryObject Object that stores the information of the library inside
	 */
	@BeforeAll
	public static void setup() {
		// old lib
		baseOperationsLib1 = new FeatureIDEBase();
		library1 = new FeatureIDEAnalyse();
		// new lib
		baseOperationsLib2 = new FeatJARBase();
		library2 = new FeatJARAnalyse();

		MODEL_NAMES.forEach(module -> {
			LibraryObject libraryObjectFirst = new LibraryObject(baseOperationsLib1.load(getPathFromResource(module)), "", baseOperationsLib1.loadConfiguration(getPathFromResource(module.replaceFirst(".xml", ".csv"))));
			LibraryObject libraryObjectSecond = new LibraryObject(baseOperationsLib2.load(getPathFromResource(module)), "", baseOperationsLib2.loadConfiguration(getPathFromResource(module.replaceFirst(".xml", ".csv"))));
			featureModels.add(new WrapperLibrary(libraryObjectFirst, libraryObjectSecond));
		});
	}

	@AfterAll
	public static void cleanUp() {
		baseOperationsLib2.cleanUp();
	}

	@Test
	public void testIsTautology() {
		// queries for tautology TODO read from file -> more generic
		HashMap<WrapperLibrary, Wrapper> map = new HashMap<>();
		map.put(featureModels.get(0), new Wrapper(baseOperationsLib1.createQueryImpl("Root", "C"), baseOperationsLib2.createQueryImpl("Root", "C")));
		map.put(featureModels.get(1), new Wrapper(baseOperationsLib1.createQueryImpl("Base", "F2"), baseOperationsLib2.createQueryImpl("Base", "F2")));
		map.put(featureModels.get(2), new Wrapper(baseOperationsLib1.createQueryImpl("Navigation", "Ports"), baseOperationsLib2.createQueryImpl("Navigation", "Ports")));
		map.put(featureModels.get(3),new Wrapper(baseOperationsLib1.createQueryAndNot("MainGpl", "UndirectedWithNeighbors"), baseOperationsLib2.createQueryAndNot("MainGpl", "UndirectedWithNeighbors")));

		map.entrySet()
				.stream()
				.forEach(entry -> assertEquals(run(() -> library1.isTautology((IFeatureModel) entry.getKey().getObjectLib1().getFeatureModel(), (Node) entry.getValue().getObjectLib1())), run(() -> library2.isTautology((IFormula) entry.getKey().getObjectLib2().getFeatureModel(), entry.getValue().getObjectLib2()))));
	}

	@Test
	public void testIsVoid() {
		featureModels.forEach(featureModel -> assertEquals(run(() -> library1.isVoid((IFeatureModel) featureModel.getObjectLib1().getFeatureModel())), run(() -> library2.isVoid((IFormula) featureModel.getObjectLib2().getFeatureModel()))));
	}

	@Test
	public void testIsVoidPartialConfig() {
		featureModels.forEach(featureModel -> {
			LibraryObject libraryObjectFirst = featureModel.getObjectLib1();
			LibraryObject libraryObjectSecond = featureModel.getObjectLib2();
			assertEquals(run(() -> library1.isVoid((IFeatureModel) libraryObjectFirst.getFeatureModel(), libraryObjectFirst.getConfig())), run(() -> library2.isVoid((IFormula) libraryObjectSecond.getFeatureModel(), libraryObjectSecond.getConfig())));
		});
	}

	@Test
	public void testCoreFeatures() {
		featureModels.forEach(featureModel -> assertEquals(run(() -> library1.coreFeatures((IFeatureModel) featureModel.getObjectLib1().getFeatureModel())), run(() -> library2.coreFeatures((IFormula) featureModel.getObjectLib2().getFeatureModel()))));
	}

	@Test
	public void testCoreFeaturesPartialConfig() {
		featureModels.forEach(featureModel -> {
			LibraryObject libraryObjectFirst = featureModel.getObjectLib1();
			LibraryObject libraryObjectSecond = featureModel.getObjectLib2();
			assertEquals(run(() -> library1.coreFeatures((IFeatureModel) libraryObjectFirst.getFeatureModel(), libraryObjectFirst.getConfig())), run(() -> library2.coreFeatures((IFormula) libraryObjectSecond.getFeatureModel(), libraryObjectSecond.getConfig())));
		});
	}

	@Test
	public void testDeadFeatures() {
		featureModels.forEach(featureModel -> assertEquals(run(() -> library1.deadFeatures((IFeatureModel) featureModel.getObjectLib1().getFeatureModel())), run(() -> library2.deadFeatures((IFormula) featureModel.getObjectLib2().getFeatureModel()))));
	}

	@Test
	public void testDeadFeaturesPartialConfig() {
		featureModels.forEach(featureModel -> {
			LibraryObject libraryObjectFirst = featureModel.getObjectLib1();
			LibraryObject libraryObjectSecond = featureModel.getObjectLib2();
			assertEquals(run(() -> library1.deadFeatures((IFeatureModel) libraryObjectFirst.getFeatureModel(), libraryObjectFirst.getConfig())), run(() -> library2.deadFeatures((IFormula) libraryObjectSecond.getFeatureModel(), libraryObjectSecond.getConfig())));
		});
	}

	@Test
	public void testFalseOptional() {
		featureModels.forEach(featureModel -> assertEquals(run(() -> library1.falseOptional((IFeatureModel) featureModel.getObjectLib1().getFeatureModel())), run(() -> library2.falseOptional((IFormula) featureModel.getObjectLib2().getFeatureModel()))));
	}

	@Test
	public void testFalseOptionalPartialConfig() {
		featureModels.forEach(featureModel -> {
			LibraryObject libraryObjectFirst = featureModel.getObjectLib1();
			LibraryObject libraryObjectSecond = featureModel.getObjectLib2();
			assertEquals(run(() -> library1.falseOptional((IFeatureModel) libraryObjectFirst.getFeatureModel(), libraryObjectFirst.getConfig())), run(() -> library2.falseOptional((IFormula) libraryObjectSecond.getFeatureModel(), libraryObjectSecond.getConfig())));
		});
	}

	@Test
	public void testRedundantConstraints() {
		featureModels.forEach(featureModel -> assertEquals(run(() -> library1.redundantConstraints((IFeatureModel) featureModel.getObjectLib1().getFeatureModel())), run(() -> library2.redundantConstraints((IFormula) featureModel.getObjectLib2().getFeatureModel()))));
	}

	@Test
	public void testRedundantConstraintsConfig() {
		featureModels.forEach(featureModel -> {
			LibraryObject libraryObjectFirst = featureModel.getObjectLib1();
			LibraryObject libraryObjectSecond = featureModel.getObjectLib2();
			assertEquals(run(() -> library1.redundantConstraints((IFeatureModel) libraryObjectFirst.getFeatureModel(), libraryObjectFirst.getConfig())), run(() -> library2.redundantConstraints((IFormula) libraryObjectSecond.getFeatureModel(), libraryObjectSecond.getConfig())));
		});
	}

	@Test
	public void testAtomicSet() {
		featureModels.forEach(featureModel -> assertEquals(run(() -> library1.atomicSets((IFeatureModel) featureModel.getObjectLib1().getFeatureModel())), run(() -> library2.atomicSets((IFormula) featureModel.getObjectLib2().getFeatureModel()))));
	}

	@Test
	public void testAtomicSetConfig() {
		featureModels.forEach(featureModel -> {
			LibraryObject libraryObjectFirst = featureModel.getObjectLib1();
			LibraryObject libraryObjectSecond = featureModel.getObjectLib2();
			assertEquals(run(() -> library1.atomicSets((IFeatureModel) libraryObjectFirst.getFeatureModel(), libraryObjectFirst.getConfig())), run(() -> library2.atomicSets((IFormula) libraryObjectSecond.getFeatureModel(), libraryObjectSecond.getConfig())));
		});
	}

	@Test
	public void testIndeterminedHiddenFeatures() {
		featureModels.forEach(featureModel -> assertEquals(run(() -> library1.indeterminedHiddenFeatures((IFeatureModel) featureModel.getObjectLib1().getFeatureModel())), run(() -> library2.indeterminedHiddenFeatures((IFormula) featureModel.getObjectLib2().getFeatureModel()))));
	}

	@Test
	public void testIndeterminedHiddenFeaturesConfig() {
		featureModels.forEach(featureModel -> {
			LibraryObject libraryObjectFirst = featureModel.getObjectLib1();
			LibraryObject libraryObjectSecond = featureModel.getObjectLib2();
			assertEquals(run(() -> library1.indeterminedHiddenFeatures((IFeatureModel) libraryObjectFirst.getFeatureModel(), libraryObjectFirst.getConfig())), run(() -> library2.indeterminedHiddenFeatures((IFormula) libraryObjectSecond.getFeatureModel(), libraryObjectSecond.getConfig())));
		});
	}

	@Test
	public void testCountSolutions() {
		featureModels.forEach(featureModel -> assertEquals(run(() -> library1.countSolutions((IFeatureModel) featureModel.getObjectLib1().getFeatureModel())), run(() -> library2.countSolutions((IFormula) featureModel.getObjectLib2().getFeatureModel()))));
	}

	@Test
	public void testCountSolutionsConfig() {
		featureModels.forEach(featureModel -> {
			LibraryObject libraryObjectFirst = featureModel.getObjectLib1();
			LibraryObject libraryObjectSecond = featureModel.getObjectLib2();
			assertEquals(run(() -> library1.countSolutions((IFeatureModel) libraryObjectFirst.getFeatureModel(), libraryObjectFirst.getConfig())), run(() -> library2.countSolutions((IFormula) libraryObjectSecond.getFeatureModel(), libraryObjectSecond.getConfig())));
		});
	}
}