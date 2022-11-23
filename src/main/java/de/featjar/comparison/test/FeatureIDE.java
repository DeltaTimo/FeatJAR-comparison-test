package de.featjar.comparison.test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.ovgu.featureide.fm.core.base.IConstraint;
import de.ovgu.featureide.fm.core.base.IFeature;
import de.ovgu.featureide.fm.core.explanations.Explanation;
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
    public Result<Boolean> isTautology(String filePath, String[] parameters) {
        final IFeatureModel featureModel = loadModel(filePath);
        if (featureModel != null) {
            FeatureModelFormula formula = new FeatureModelFormula(featureModel);
            final FeatureModelAnalyzer analyzer = formula.getAnalyzer();
            analyzer.analyzeFeatureModel(null);
            final SimpleSatSolver solver = new SimpleSatSolver(formula.getCNF());
            final Node query = new Implies(new Literal(parameters[0]), new Literal(parameters[1]));
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

    @Override
    public Result<Boolean> isVoid(String filePath) {
        final IFeatureModel featureModel = loadModel(filePath);
        if (featureModel != null) {
            FeatureModelFormula formula = new FeatureModelFormula(featureModel);
            final FeatureModelAnalyzer analyzer = formula.getAnalyzer();
            analyzer.analyzeFeatureModel(null);
            if(analyzer.isValid(null))  return new Result<>(false);
            return new Result<>(true);
        }
        return new Result<>();
    }

    @Override
    public Result<Set<String>> coreFeatures(String filePath) {
        final IFeatureModel featureModel = loadModel(filePath);
        if (featureModel != null) {
            FeatureModelFormula formula = new FeatureModelFormula(featureModel);
            final FeatureModelAnalyzer analyzer = formula.getAnalyzer();
            List<IFeature> core = analyzer.getCoreFeatures(null);
            Set<String> result = new HashSet<>();
            core.forEach(iFeature -> result.add(iFeature.toString()));
            return new Result<>(result);
        }
        return new Result<>();
    }

    @Override
    public Result<Set<String>> deadFeatures(String filePath) {
        final IFeatureModel featureModel = loadModel(filePath);
        if (featureModel != null) {
            FeatureModelFormula formula = new FeatureModelFormula(featureModel);
            final FeatureModelAnalyzer analyzer = formula.getAnalyzer();
            List<IFeature> dead = analyzer.getDeadFeatures(null);
            Set<String> result = new HashSet<>();
            dead.forEach(iFeature -> result.add(iFeature.toString()));
            return new Result<>(result);
        }
        return new Result<>();
    }

    @Override
    public Result<Set<String>> falseOptional(String filePath) {
        final IFeatureModel featureModel = loadModel(filePath);
        if (featureModel != null) {
            FeatureModelFormula formula = new FeatureModelFormula(featureModel);
            final FeatureModelAnalyzer analyzer = formula.getAnalyzer();
            List<IFeature> falseOptionalFeatures = analyzer.getFalseOptionalFeatures(null);
            Set<String> result = new HashSet<>();
            falseOptionalFeatures.forEach(iFeature -> result.add(iFeature.toString()));
            return new Result<>(result);
        }
        return new Result<>();
    }

    @Override
    public Result<Set<String>> redundantConstraints(String filePath) {
        final IFeatureModel featureModel = loadModel(filePath);
        if (featureModel != null) {
            FeatureModelFormula formula = new FeatureModelFormula(featureModel);
            final FeatureModelAnalyzer analyzer = formula.getAnalyzer();
            List<IConstraint> redundantConstraints = analyzer.getRedundantConstraints(null);
            Set<String> result = new HashSet<>();
            redundantConstraints.forEach(iFeature -> result.add(iFeature.getDisplayName()));
            return new Result<>(result);
        }
        return new Result<>();
    }

    @Override
    public Result<List<Set<String>>> atomicSets(String filePath) {
        final IFeatureModel featureModel = loadModel(filePath);
        if (featureModel != null) {
            FeatureModelFormula formula = new FeatureModelFormula(featureModel);
            final FeatureModelAnalyzer analyzer = formula.getAnalyzer();
            List<List<IFeature>> analyzerAtomicSets = analyzer.getAtomicSets(null);
            List<Set<String>> result = new ArrayList<>();
            for(int i = 0; i < analyzerAtomicSets.size(); i++) {
                Set<String> tmp = new HashSet<>();
                for(int j = 0; j < analyzerAtomicSets.get(i).size(); j++) {
                    tmp.add(analyzerAtomicSets.get(i).get(j).getName());
                }
                result.add(tmp);
            }
        }
        return new Result<>();
    }

    @Override
    public Result<Set<String>> indeterminedHiddenFeatures(String filePath) {
        final IFeatureModel featureModel = loadModel(filePath);
        if (featureModel != null) {
            FeatureModelFormula formula = new FeatureModelFormula(featureModel);
            final FeatureModelAnalyzer analyzer = formula.getAnalyzer();
            List<IFeature> analyzerIndeterminedHiddenFeatures = analyzer.getIndeterminedHiddenFeatures(null);
            Set<String> result = new HashSet<>();
            analyzerIndeterminedHiddenFeatures.forEach(iFeature -> result.add(iFeature.toString()));
            System.out.println(result); // TODO was genau sind idetermined hidden features
            return new Result<>(result);
        }
        return new Result<>();
    }

    private IFeatureModel loadModel(String filePath) {
        LibraryManager.registerLibrary(FMCoreLibrary.getInstance());
        Path path = Paths.get(filePath);
        return FeatureModelManager.load(path);
    }
}
