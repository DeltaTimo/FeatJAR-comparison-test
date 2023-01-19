package de.featjar.comparison.test.helper.featureide;

import java.nio.file.Paths;

import de.featjar.comparison.test.helper.ITransformations;
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
 * example transformation from featuremodel in different formats of the FeatureIDE library.
 * The interface ITransformations<IFeatureModel> is implemented and the analyses are
 * used in the test class FeatureModelTransformationTests
 * @author Katjana Herbst
 * @see de.featjar.comparison.test.FeatureModelTransformationTests
 * @see ITransformations
 */
public class FeatureIDETransformation implements ITransformations<IFeatureModel> {
    static {
        LibraryManager.registerLibrary(FMCoreLibrary.getInstance());
    }

     /**
      * not used yet
      * saves a featuremodel to file
      * @param model featuremodel
      * @param savePath
      * @param  format
      * @return void
     */
    public static void saveFeatureModel(IFeatureModel model, String savePath, IPersistentFormat<IFeatureModel> format) {
        FeatureModelManager.save(model, Paths.get(savePath), format);
    }

     /**
      * transforms featuremodel into dimacs
      * @param model featuremodel
      * @return String
     */
    public Object getDimacs(IFeatureModel model) {
        FeatureModelFormula formula = new FeatureModelFormula(model);
        DimacsWriter dimacsWriter = new DimacsWriter(formula.getCNF());
        return dimacsWriter.write();
    }

     /**
      * transforms featuremodel into cnf
      * @param model featuremodel
      * @return String
     * */
    public Object getCNF(IFeatureModel model) {
        FeatureModelFormula formula = new FeatureModelFormula(model);
        CNF cnf = formula.getCNF();
        return cnf.toString();
    }

     /**
      * transforms featuremodel into uvl
      * @param model featuremodel
      * @return String
     */
    public Object getUVL(IFeatureModel model) {
        final IFeatureModelFormat format = new UVLFeatureModelFormat();
        return format.getInstance().write(model);
    }

     /**
      * transforms featuremodel into velvet
      * @param model featuremodel
      * @return String
     */
    public Object getVelvet(IFeatureModel model) {
        final IFeatureModelFormat format = new SimpleVelvetFeatureModelFormat();
        return format.getInstance().write(model);
    }

     /**
      * transforms featuremodel into sxfml
      * @param model featuremodel
      * @return String
     */
    @Override
    public Object getSxfml(IFeatureModel model) {
        final IFeatureModelFormat format = new SXFMFormat();
        return format.getInstance().write(model);
    }
}