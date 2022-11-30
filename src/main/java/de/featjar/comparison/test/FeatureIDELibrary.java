package de.featjar.comparison.test;

import de.ovgu.featureide.fm.core.FeatureModelAnalyzer;
import de.ovgu.featureide.fm.core.analysis.cnf.CNF;
import de.ovgu.featureide.fm.core.analysis.cnf.ClauseList;
import de.ovgu.featureide.fm.core.analysis.cnf.Nodes;
import de.ovgu.featureide.fm.core.analysis.cnf.analysis.CountSolutionsAnalysis;
import de.ovgu.featureide.fm.core.analysis.cnf.formula.FeatureModelFormula;
import de.ovgu.featureide.fm.core.analysis.cnf.formula.SlicedCNFCreator;
import de.ovgu.featureide.fm.core.analysis.cnf.solver.SimpleSatSolver;
import de.ovgu.featureide.fm.core.base.IConstraint;
import de.ovgu.featureide.fm.core.base.IFeature;
import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.base.IFeatureModelFactory;
import de.ovgu.featureide.fm.core.base.impl.DefaultFeatureModelFactory;
import de.ovgu.featureide.fm.core.editing.Comparison;
import de.ovgu.featureide.fm.core.editing.ModelComparator;
import de.ovgu.featureide.fm.core.filter.FeatureSetFilter;
import de.ovgu.featureide.fm.core.io.IFeatureModelFormat;
import de.ovgu.featureide.fm.core.io.dimacs.DimacsWriter;
import de.ovgu.featureide.fm.core.io.sxfm.SXFMFormat;
import de.ovgu.featureide.fm.core.io.uvl.UVLFeatureModelFormat;
import de.ovgu.featureide.fm.core.io.velvet.SimpleVelvetFeatureModelFormat;
import org.prop4j.Implies;
import org.prop4j.Literal;
import org.prop4j.Node;
import org.prop4j.Not;

import java.util.*;

public class FeatureIDELibrary implements IAnalyses, ITransformations, IModification{
    @Override
    public Result<Boolean> isTautology(IFeatureModel featureModel, String[] parameters) {
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
                    return new Result<>(true);
                case TRUE:
                    return new Result<>(false);
                default:
                    return new Result<>();
            }
        }
        return new Result<>();
    }

    @Override
    public Result<Boolean> isVoid(IFeatureModel featureModel) {
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
    public Result<Set<String>> coreFeatures(IFeatureModel featureModel) {
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
    public Result<Set<String>> deadFeatures(IFeatureModel featureModel) {
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
    public Result<Set<String>> falseOptional(IFeatureModel featureModel) {
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
    public Result<Set<String>> redundantConstraints(IFeatureModel featureModel) {
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
    public Result<List<Set<String>>> atomicSets(IFeatureModel featureModel) {
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
    public Result<Set<String>> indeterminedHiddenFeatures(IFeatureModel featureModel) {
        if (featureModel != null) {
            FeatureModelFormula formula = new FeatureModelFormula(featureModel);
            final FeatureModelAnalyzer analyzer = formula.getAnalyzer();
            List<IFeature> analyzerIndeterminedHiddenFeatures = analyzer.getIndeterminedHiddenFeatures(null);
            Set<String> result = new HashSet<>();
            analyzerIndeterminedHiddenFeatures.forEach(iFeature -> result.add(iFeature.toString()));
            return new Result<>(result);
        }
        return new Result<>();
    }

    @Override
    public Result<Long> countSolutions(IFeatureModel featureModel) {
        FeatureModelFormula formula = new FeatureModelFormula(featureModel);
        CNF cnf = formula.getCNF();
        CountSolutionsAnalysis countSolutionsAnalysis = new CountSolutionsAnalysis(cnf);
        try {
            System.out.println(countSolutionsAnalysis.analyze(null));
            return new Result<>(countSolutionsAnalysis.analyze(null));
        } catch (Exception e) {
            return new Result<>();
        }
    }

    public Result<String> getDimacs(IFeatureModel model) {
        FeatureModelFormula formula = new FeatureModelFormula(model);
        DimacsWriter dimacsWriter = new DimacsWriter(formula.getCNF());
        return new Result<>(dimacsWriter.write());
    }

    public Result<String> getCNF(IFeatureModel model) {
        FeatureModelFormula formula = new FeatureModelFormula(model);
        CNF cnf = formula.getCNF();
        return new Result<>(cnf.toString());
    }

    public Result<String> getUVL(IFeatureModel model) {
        final IFeatureModelFormat format = new UVLFeatureModelFormat();
        return new Result<>(format.getInstance().write(model));
    }

    public Result<String> getVelvet(IFeatureModel model) {
        final IFeatureModelFormat format = new SimpleVelvetFeatureModelFormat();
        return new Result<>(format.getInstance().write(model));
    }

    @Override
    public Result<String> getSxfml(IFeatureModel model) {
        final IFeatureModelFormat format = new SXFMFormat();
        return new Result<>(format.getInstance().write(model));
    }

    @Override
    public Result<Set<String>> addFeature(IFeatureModel featureModel) {
        final IFeatureModelFactory factory = getFMFactory();
        final IFeature f = factory.createFeature(featureModel, "D");
        featureModel.addFeature(f);

        Set<String> result = new HashSet<>();
        Collection<IFeature> c = featureModel.getFeatures();
        Iterator<IFeature> features = c.iterator();
        while (features.hasNext()) {
            result.add(features.next().getName());
        }
        return new Result<>(result);
    }

    @Override
    public Result<List<String>> removeFeature(IFeatureModel featureModel) {
        Collection<IFeature> c = featureModel.getFeatures();
        Iterator<IFeature> features = c.iterator();
        // delete second feature in model
        int count = 1;
        while (features.hasNext()) {
            if(count == 2) {
                featureModel.deleteFeature(features.next());
                break;
            }
            features.next();
            count++;
        }
        return new Result<>(featureModel.getFeatureOrderList());
    }

    @Override
    public Result<Set<String>> addConstraint(IFeatureModel featureModel) {
        final IFeatureModelFactory factory = getFMFactory();
        final IConstraint c = factory.createConstraint(featureModel, new Implies(new Literal("A"), new Literal("C")));
        featureModel.addConstraint(c);
        List<IConstraint> constraintList = featureModel.getConstraints();
        Set<String> constraints = new HashSet<>();
        constraintList.forEach(iConstraint -> constraints.add(iConstraint.toString()));
        return new Result<>(constraints);
    }

    @Override
    public Result<Set<String>> removeConstraint(IFeatureModel featureModel) {
        featureModel.removeConstraint(0);
        List<IConstraint> constraintList = featureModel.getConstraints();
        Set<String> constraints = new HashSet<>();
        constraintList.forEach(iConstraint -> constraints.add(iConstraint.toString()));
        return new Result<>(constraints);
    }

    @Override
    public Result<Set<String>> slice(IFeatureModel featureModel) {
        FeatureModelFormula formula = new FeatureModelFormula(featureModel);
        final IFeature f = featureModel.getFeature("A");
        Collection<IFeature> sliceFeatures = toCollection(f);
        final CNF slicedCNF = formula.getElement(new SlicedCNFCreator(new FeatureSetFilter(sliceFeatures)) {
            @Override
            protected CNF create() {
                return super.create();
            }
        });
        Set<String> result = new HashSet<>();
        String[] variables = slicedCNF.getVariables().getNames();
        Arrays.stream(variables).forEach(featureName -> result.add(featureName));
        return new Result<>(result);
    }

    @Override
    public Result<String> comparatorSpecialization(IFeatureModel featureModel) {
        IFeatureModel tmp = featureModel.clone();
        final IFeatureModelFactory factory = getFMFactory();
        final IFeature f = factory.createFeature(tmp, "D");
        tmp.addFeature(f);

        final ModelComparator comparator = new ModelComparator(1000000);
        final Comparison comparison = comparator.compare(tmp, featureModel);
        return new Result<>(comparison.toString());
    }

    @Override
    public Result<String> comparatorGeneralization(IFeatureModel featureModel) {
        IFeatureModel tmp = featureModel.clone();
        final IFeatureModelFactory factory = getFMFactory();
        final IFeature f = factory.createFeature(tmp, "D");
        tmp.addFeature(f);

        final ModelComparator comparator = new ModelComparator(1000000);
        final Comparison comparison = comparator.compare(featureModel, tmp);
        return new Result<>(comparison.toString());
    }

    public static <IFeature> Collection<IFeature> toCollection(IFeature element) {
        List<IFeature> list = new ArrayList<IFeature>(1);
        list.add(element);
        return list;
    }

    private IFeatureModelFactory getFMFactory() {
        return DefaultFeatureModelFactory.getInstance();
    }
}
