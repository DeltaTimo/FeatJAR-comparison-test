package de.featjar.comparison.test.helper.featureide;

import de.featjar.comparison.test.helper.IBase;
import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.base.impl.FeatureModel;
import de.ovgu.featureide.fm.core.init.FMCoreLibrary;
import de.ovgu.featureide.fm.core.init.LibraryManager;
import de.ovgu.featureide.fm.core.io.manager.FeatureModelIO;
import de.ovgu.featureide.fm.core.io.manager.FeatureModelManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FeatureIDEBase implements IBase<IFeatureModel> {

    @Override
    public IFeatureModel load(String filepath) {
        LibraryManager.registerLibrary(FMCoreLibrary.getInstance());
        Path path = Paths.get(filepath);
        return FeatureModelManager.load(path);
    }

    @Override
    public IFeatureModel loadFromSource(String content, String filepath) {
        FeatureModelIO featureModelIO = FeatureModelIO.getInstance();

        System.out.println(featureModelIO.loadFromSource(content, Paths.get(filepath)));
        return featureModelIO.loadFromSource(content, Paths.get(filepath));
    }
}
