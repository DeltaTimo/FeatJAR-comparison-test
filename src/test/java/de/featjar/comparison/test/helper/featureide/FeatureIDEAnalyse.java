package de.featjar.comparison.test.helper.featureide;

import java.util.*;

import de.featjar.comparison.test.helper.IAnalyses;
import de.ovgu.featureide.fm.core.analysis.cnf.CNF;
import de.ovgu.featureide.fm.core.analysis.cnf.analysis.CountSolutionsAnalysis;
import de.ovgu.featureide.fm.core.base.IConstraint;
import de.ovgu.featureide.fm.core.base.IFeature;
import de.ovgu.featureide.fm.core.configuration.*;
import de.ovgu.featureide.fm.core.job.LongRunningWrapper;
import org.prop4j.Node;
import org.prop4j.Not;

import de.ovgu.featureide.fm.core.FeatureModelAnalyzer;
import de.ovgu.featureide.fm.core.analysis.cnf.ClauseList;
import de.ovgu.featureide.fm.core.analysis.cnf.Nodes;
import de.ovgu.featureide.fm.core.analysis.cnf.formula.FeatureModelFormula;
import de.ovgu.featureide.fm.core.analysis.cnf.solver.SimpleSatSolver;
import de.ovgu.featureide.fm.core.base.IFeatureModel;

public class FeatureIDEAnalyse implements IAnalyses<IFeatureModel, Node> {
    @Override
    public Object isTautology(IFeatureModel featureModel, Node query) {
        if (featureModel != null) {
            FeatureModelFormula formula = new FeatureModelFormula(featureModel);
            final FeatureModelAnalyzer analyzer = formula.getAnalyzer();
            analyzer.analyzeFeatureModel(null);
            final SimpleSatSolver solver = new SimpleSatSolver(formula.getCNF());
            System.out.print("Is \"FM => (" + query + ")\" a tautology? ");
            ClauseList queryClauses = Nodes.convert(formula.getCNF().getVariables(), new Not(query), true);
            solver.addClauses(queryClauses);
            switch (solver.hasSolution()) {
                case FALSE:
                    System.out.println("yes");
                    return true;
                case TRUE:
                    System.out.println("no");
                    return false;
                case TIMEOUT:
                    System.out.println("cannot decide (timeout)");
                    return null;
                default:
                    break;
            }
        } else {
            System.out.println("Feature model could not be read!");
        }
        return null;
    }

    @Override
    public Object isVoid(IFeatureModel featureModel) {
        if (featureModel != null) {
            FeatureModelFormula formula = new FeatureModelFormula(featureModel);
            final FeatureModelAnalyzer analyzer = formula.getAnalyzer();
            analyzer.analyzeFeatureModel(null);
            if(analyzer.isValid(null))  return false;
            return true;
        }
        return null;
    }

    @Override
    public Object isVoid(IFeatureModel featureModel, String config) {
        final FeatureModelFormula formula = new FeatureModelFormula(featureModel);
        final Configuration c = new Configuration(formula);
        final DefaultFormat r = new DefaultFormat();
        r.read(c, config);

        final IConfigurationPropagator propagator = new ConfigurationPropagator(formula, c);
        return !LongRunningWrapper.runMethod(propagator.canBeValid());
    }

    @Override
    public Object coreFeatures(IFeatureModel featureModel) {
        if (featureModel != null) {
            FeatureModelFormula formula = new FeatureModelFormula(featureModel);
            final FeatureModelAnalyzer analyzer = formula.getAnalyzer();
            List<IFeature> core = analyzer.getCoreFeatures(null);
            Set<String> result = new HashSet<>();
            core.forEach(iFeature -> result.add(iFeature.toString()));
            return result;
        }
        return null;
    }

    @Override
    public Object deadFeatures(IFeatureModel featureModel) {
        if (featureModel != null) {
            FeatureModelFormula formula = new FeatureModelFormula(featureModel);
            final FeatureModelAnalyzer analyzer = formula.getAnalyzer();
            List<IFeature> dead = analyzer.getDeadFeatures(null);
            Set<String> result = new HashSet<>();
            dead.forEach(iFeature -> result.add(iFeature.toString()));
            return result;
        }
        return null;
    }

    @Override
    public Object falseOptional(IFeatureModel featureModel) {
        if (featureModel != null) {
            FeatureModelFormula formula = new FeatureModelFormula(featureModel);
            final FeatureModelAnalyzer analyzer = formula.getAnalyzer();
            List<IFeature> falseOptionalFeatures = analyzer.getFalseOptionalFeatures(null);
            Set<String> result = new HashSet<>();
            falseOptionalFeatures.forEach(iFeature -> result.add(iFeature.toString()));
            return result;
        }
        return null;
    }

    @Override
    public Object redundantConstraints(IFeatureModel featureModel) {
        if (featureModel != null) {
            FeatureModelFormula formula = new FeatureModelFormula(featureModel);
            final FeatureModelAnalyzer analyzer = formula.getAnalyzer();
            List<IConstraint> redundantConstraints = analyzer.getRedundantConstraints(null);
            Set<String> result = new HashSet<>();
            redundantConstraints.forEach(iFeature -> result.add(iFeature.getDisplayName()));
            return result;
        }
        return null;
    }

    @Override
    public Object atomicSets(IFeatureModel featureModel) {
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
        return null;
    }

    @Override
    public Object indeterminedHiddenFeatures(IFeatureModel featureModel) {
        if (featureModel != null) {
            FeatureModelFormula formula = new FeatureModelFormula(featureModel);
            final FeatureModelAnalyzer analyzer = formula.getAnalyzer();
            List<IFeature> analyzerIndeterminedHiddenFeatures = analyzer.getIndeterminedHiddenFeatures(null);
            Set<String> result = new HashSet<>();
            analyzerIndeterminedHiddenFeatures.forEach(iFeature -> result.add(iFeature.toString()));
            return result;
        }
        return null;
    }

    @Override
    public Object countSolutions(IFeatureModel featureModel) {
        FeatureModelFormula formula = new FeatureModelFormula(featureModel);
        CNF cnf = formula.getCNF();
        CountSolutionsAnalysis countSolutionsAnalysis = new CountSolutionsAnalysis(cnf);
        try {
            return countSolutionsAnalysis.analyze(null);
        } catch (Exception e) {
            return null;
        }
    }
}