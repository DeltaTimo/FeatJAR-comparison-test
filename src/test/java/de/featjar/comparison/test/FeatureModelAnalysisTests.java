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

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * An example usage of the FeatureIDE library for feature-model analysis.
 *
 * @author Thomas Thuem
 */
public class FeatureModelAnalysisTests {
	private String getPathFromResource(String resource) throws FileNotFoundException {
		final URL resourceURL = getClass().getClassLoader().getResource(resource);
		if (resourceURL == null) {
			throw new FileNotFoundException(resource);
		} else {
			return resourceURL.getPath();
		}
	}

	@Test
	public void isTautology() throws FileNotFoundException {
		String resource = getPathFromResource("FeatureModelAnalysis/car.xml");

		ITestLibrary library1 = new FeatureIDE();
		ITestLibrary library2 = new FeatureIDE();

		assertEquals(Result.get(() -> library1.isTautology(resource)), Result.get(() -> library2.isTautology(resource)));
	}

}
