package de.featjar.comparison.test.helper.featureide;

import de.featjar.comparison.test.helper.IModification;
import de.featjar.comparison.test.helper.Result;
import de.ovgu.featureide.fm.core.analysis.cnf.CNF;
import de.ovgu.featureide.fm.core.analysis.cnf.formula.FeatureModelFormula;
import de.ovgu.featureide.fm.core.analysis.cnf.formula.SlicedCNFCreator;
import de.ovgu.featureide.fm.core.base.IConstraint;
import de.ovgu.featureide.fm.core.base.IFeature;
import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.base.IFeatureModelFactory;
import de.ovgu.featureide.fm.core.base.impl.DefaultFeatureModelFactory;
import de.ovgu.featureide.fm.core.editing.Comparison;
import de.ovgu.featureide.fm.core.editing.ModelComparator;
import de.ovgu.featureide.fm.core.filter.FeatureSetFilter;
import org.prop4j.Implies;
import org.prop4j.Literal;

import java.util.*;

/**
 * basic tasks for modifications on featuremodels
 *
 * @author Katjana Herbst
 */
public class FeatureIDEModification implements IModification<IFeatureModel> {

    private IFeatureModelFactory getFMFactory() {
        return DefaultFeatureModelFactory.getInstance();
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
}