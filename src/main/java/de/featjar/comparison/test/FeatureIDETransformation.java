package de.featjar.comparison.test;

import java.nio.file.Paths;

import de.ovgu.featureide.fm.core.analysis.cnf.CNF;
import de.ovgu.featureide.fm.core.analysis.cnf.formula.FeatureModelFormula;
import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.init.FMCoreLibrary;
import de.ovgu.featureide.fm.core.init.LibraryManager;
import de.ovgu.featureide.fm.core.io.IFeatureModelFormat;
import de.ovgu.featureide.fm.core.io.IPersistentFormat;
import de.ovgu.featureide.fm.core.io.dimacs.DimacsWriter;
import de.ovgu.featureide.fm.core.io.manager.FeatureModelManager;
import de.ovgu.featureide.fm.core.io.uvl.UVLFeatureModelFormat;
import de.ovgu.featureide.fm.core.io.sxfm.SXFMFormat;
import de.ovgu.featureide.fm.core.io.velvet.SimpleVelvetFeatureModelFormat;

/**
 * example transformation from featuremodel in diffrent formats
 *
 * @author Katjana Herbst
 */
public class FeatureIDETransformation implements ITestLibraryTransformation{
    static {
        LibraryManager.registerLibrary(FMCoreLibrary.getInstance());
    }

    public static void saveFeatureModel(IFeatureModel model, String savePath, IPersistentFormat<IFeatureModel> format) {
        FeatureModelManager.save(model, Paths.get(savePath), format);
    }

    public Result<String> getDimacs(String path) {
        IFeatureModel model = FeatureModelManager.load(Paths.get(path));
        FeatureModelFormula formula = new FeatureModelFormula(model);
        DimacsWriter dimacsWriter = new DimacsWriter(formula.getCNF());
        return new Result<>(dimacsWriter.write());
    }

    public Result<String> getCNF(String path) {
        IFeatureModel model = FeatureModelManager.load(Paths.get(path));
        FeatureModelFormula formula = new FeatureModelFormula(model);
        CNF cnf = formula.getCNF();
        return new Result<>(cnf.toString());
    }

    public Result<String> getUVL(String path) {
        IFeatureModel model = FeatureModelManager.load(Paths.get(path));
        final IFeatureModelFormat format = new UVLFeatureModelFormat();
        return new Result<>(format.getInstance().write(model));
    }

    public Result<String> getVelvet(String path) {
        IFeatureModel model = FeatureModelManager.load(Paths.get(path));
        final IFeatureModelFormat format = new SimpleVelvetFeatureModelFormat();
        return new Result<>(format.getInstance().write(model));
    }

    public Result<String> getSxfml(String path) {
        IFeatureModel model = FeatureModelManager.load(Paths.get(path));
        final IFeatureModelFormat format = new SXFMFormat();
        return new Result<>(format.getInstance().write(model));
    }
}