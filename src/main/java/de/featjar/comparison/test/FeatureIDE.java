package de.featjar.comparison.test;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.prop4j.Implies;
import org.prop4j.Literal;
import org.prop4j.Node;
import org.prop4j.Not;

import de.ovgu.featureide.fm.core.FeatureModelAnalyzer;
import de.ovgu.featureide.fm.core.analysis.cnf.ClauseList;
import de.ovgu.featureide.fm.core.analysis.cnf.Nodes;
import de.ovgu.featureide.fm.core.analysis.cnf.formula.FeatureModelFormula;
import de.ovgu.featureide.fm.core.analysis.cnf.solver.SimpleSatSolver;
import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.init.FMCoreLibrary;
import de.ovgu.featureide.fm.core.init.LibraryManager;
import de.ovgu.featureide.fm.core.io.manager.FeatureModelManager;

public class FeatureIDE implements ITestLibrary {
    @Override
    public Result<Boolean> isTautology(String filePath) {
        LibraryManager.registerLibrary(FMCoreLibrary.getInstance());
        final Path path = Paths.get(filePath);
        final IFeatureModel featureModel = FeatureModelManager.load(path);
        if (featureModel != null) {
            FeatureModelFormula formula = new FeatureModelFormula(featureModel);
            final FeatureModelAnalyzer analyzer = formula.getAnalyzer();
            analyzer.analyzeFeatureModel(null);
            System.out.println("Feature model is " + (analyzer.isValid(null) ? "not " : "") + "void");
            System.out.println("Dead features: " + analyzer.getDeadFeatures(null));
            System.out.println(analyzer.getExplanation(featureModel.getFeature("Bluetooth")));

            final SimpleSatSolver solver = new SimpleSatSolver(formula.getCNF());
            final Node query = new Implies(new Literal("Navigation"), new Literal("Ports"));
            System.out.print("Is \"FM => (" + query + ")\" a tautology? ");
            ClauseList queryClauses = Nodes.convert(formula.getCNF().getVariables(), new Not(query), true);
            solver.addClauses(queryClauses);
            switch (solver.hasSolution()) {
                case FALSE:
                    System.out.println("yes");
                    return new Result<>(true);
                case TRUE:
                    System.out.println("no");
                    return new Result<>(false);
                case TIMEOUT:
                    System.out.println("cannot decide (timeout)");
                    return new Result<>();
                default:
                    break;
            }
        } else {
            System.out.println("Feature model could not be read!");
        }
        return new Result<>();
    }
}
