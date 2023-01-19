package de.featjar.comparison.test.helper.featureide;

import de.featjar.comparison.test.helper.IModification;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

/**
 * This class contains all basic tasks for modifications on featuremodels of the FeatureIDE library.
 * The interface IModification<IFeatureModel> is implemented and the analyses are
 * used in the test class FeatureModelModificationTest
 * @author Katjana Herbst
 * @see de.featjar.comparison.test.FeatureModelModificationTest
 * @see IModification
 */
public class FeatureIDEModification implements IModification<IFeatureModel> {

    Map<String, String[]> parameters = new HashMap<>();

    private IFeatureModelFactory getFMFactory() {
        return DefaultFeatureModelFactory.getInstance();
    }

     /**
     * add feature to featuremodel
     * @param featureModel featuremodel to analyze
     * @param fileName
     * @return result Set<String>
     */
    @Override
    public Object addFeature(IFeatureModel featureModel, String fileName) {
        final IFeatureModelFactory factory = getFMFactory();
        final IFeature f = factory.createFeature(featureModel, parameters.get(fileName)[2]);
        // nach root einfügen
        featureModel.getStructure().getRoot().addChild(f.getStructure());
        f.getStructure().setMandatory(false);
        f.getStructure().setAbstract(false);
        featureModel.addFeature(f);

        Set<String> result = new HashSet<>();
        Collection<IFeature> c = featureModel.getFeatures();
        Iterator<IFeature> features = c.iterator();
        while (features.hasNext()) {
            result.add(features.next().getName());
        }
        return result;
    }

     /**
     * remove feature to featuremodel
     * @param featureModel featuremodel to analyze
     * @return result List<String>
     */
    @Override
    public Object removeFeature(IFeatureModel featureModel) {
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
        return featureModel.getFeatureOrderList();
    }

     /**
     * add constraint to featuremodel
     * @param featureModel featuremodel to analyze
     * @param fileName
     * @return constraints Set<String>
     */
    @Override
    public Object addConstraint(IFeatureModel featureModel, String fileName) {
        final IFeatureModelFactory factory = getFMFactory();
        final IConstraint c = factory.createConstraint(featureModel, new Implies(new Literal(parameters.get(fileName)[0]), new Literal(parameters.get(fileName)[1])));
        featureModel.addConstraint(c);
        List<IConstraint> constraintList = featureModel.getConstraints();
        Set<String> constraints = new HashSet<>();
        constraintList.forEach(iConstraint -> constraints.add(iConstraint.toString()));
        return constraints;
    }

     /**
     * remove constraint to featuremodel
     * @param featureModel featuremodel to analyze
     * @return constraints Set<String>
     */
    @Override
    public Object removeConstraint(IFeatureModel featureModel) {
        featureModel.removeConstraint(0);
        List<IConstraint> constraintList = featureModel.getConstraints();
        Set<String> constraints = new HashSet<>();
        constraintList.forEach(iConstraint -> constraints.add(iConstraint.toString()));
        return constraints;
    }

     /**
      * slice featuremodel
      * @param featureModel featuremodel to analyze
      * @param fileName
      * @return result Set<String>*
      * @see FeatureIDEModification#toCollection(Object)
     */
    @Override
    public Object slice(IFeatureModel featureModel, String fileName) {
        FeatureModelFormula formula = new FeatureModelFormula(featureModel);
        final IFeature f = featureModel.getFeature(parameters.get(fileName)[0]);
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
        return result;
    }

     /**
     * comparator Specialization of the featuremodel
     * @param featureModel featuremodel to analyze
     * @param fileName
     * @return String
     */
    @Override
    public Object comparatorSpecialization(IFeatureModel featureModel, String fileName) {
        IFeatureModel tmp = featureModel.clone();
        final IFeatureModelFactory factory = getFMFactory();
        final IFeature f = factory.createFeature(tmp, parameters.get(fileName)[2]);
        tmp.addFeature(f);

        final ModelComparator comparator = new ModelComparator(1000000);
        final Comparison comparison = comparator.compare(tmp, featureModel);
        return comparison.toString();
    }

     /**
     * comparator Generalization of the featuremodel
     * @param featureModel featuremodel to analyze
     * @param fileName
     * @return String
     */
    @Override
    public Object comparatorGeneralization(IFeatureModel featureModel, String fileName) {
        IFeatureModel tmp = featureModel.clone();
        final IFeatureModelFactory factory = getFMFactory();
        final IFeature f = factory.createFeature(tmp, parameters.get(fileName)[2]);
        tmp.addFeature(f);

        final ModelComparator comparator = new ModelComparator(1000000);
        final Comparison comparison = comparator.compare(featureModel, tmp);
        return comparison.toString();
    }

     /**
      * helper method
      * @param element IFeature
      * @return <IFeature> Collection<IFeature>
     */
    public static <IFeature> Collection<IFeature> toCollection(IFeature element) {
        List<IFeature> list = new ArrayList<IFeature>(1);
        list.add(element);
        return list;
    }

     /**
      * load data for modification from file in Map<String, String[]>
      * @param filePath String
      * @return void
      * @see FeatureIDEModification#parameters
     */
    public void getDataForModification(String filePath) {
        BufferedReader br = null;
        try {
            File file = new File(filePath);
            br = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":");
                String fileName = parts[0].trim();
                String[] features = parts[1].trim().split(",");

                if (!fileName.equals("") && features.length>0)
                    parameters.put(fileName, features);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {}
            }
        }
    }
}