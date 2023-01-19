package de.featjar.comparison.test.helper.featureide;

import de.featjar.comparison.test.helper.IConfigurationGenerator;
import de.ovgu.featureide.fm.core.analysis.cnf.CNF;
import de.ovgu.featureide.fm.core.analysis.cnf.LiteralSet;
import de.ovgu.featureide.fm.core.analysis.cnf.formula.FeatureModelFormula;
import de.ovgu.featureide.fm.core.analysis.cnf.generator.configuration.AllConfigurationGenerator;
import de.ovgu.featureide.fm.core.analysis.cnf.generator.configuration.PairWiseConfigurationGenerator;
import de.ovgu.featureide.fm.core.analysis.cnf.generator.configuration.RandomConfigurationGenerator;
import de.ovgu.featureide.fm.core.analysis.cnf.generator.configuration.SPLCAToolConfigurationGenerator;
import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.job.LongRunningWrapper;
import de.ovgu.featureide.fm.core.job.monitor.ConsoleMonitor;

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains all algorithms to generate configurations of the FeatureIDE library.
 * The interface IConfigurationGenerator<IFeatureModel> is implemented and the analyses are
 * used in the test class FeatureMoelConfigurationGeneratorTest
 * @author Katjana Herbst
 * @see de.featjar.comparison.test.FeatureMoelConfigurationGeneratorTest
 * @see IConfigurationGenerator
 */
public class FeatureIDEConfigurationGenerator implements IConfigurationGenerator<IFeatureModel> {
    final private int LIMIT_CONFIGURATION_NUMBER = Integer.MAX_VALUE;
    final private int t = 0;

     /**
     * generates all pairwise configurations of the featuremodel
     * @param featureModel featuremodel to analyze
     * @return resultList List<String>
     */
    @Override
    public Object pairwise(IFeatureModel featureModel) {
        FeatureModelFormula formula = new FeatureModelFormula(featureModel);
        CNF cnf = formula.getCNF();
        PairWiseConfigurationGenerator generator = new PairWiseConfigurationGenerator(cnf, LIMIT_CONFIGURATION_NUMBER);
        final List<LiteralSet> result = LongRunningWrapper.runMethod(generator, new ConsoleMonitor<>());

        List<String> pairwiseResult = new ArrayList<>();
        result.forEach(literalSet -> pairwiseResult.add(literalSet.toString()));
        List<String> resultList = new ArrayList<>();
        result.forEach(literalSet -> resultList.add(literalSet.toString()));
        return resultList;
    }

     /**
     * generates all configurations with icpl of the featuremodel
     * @param featureModel featuremodel to analyze
     * @return resultList List<String>
     */
    @Override
    public Object icpl(IFeatureModel featureModel) {
        FeatureModelFormula formula = new FeatureModelFormula(featureModel);
        CNF cnf = formula.getCNF();
        SPLCAToolConfigurationGenerator generator = new SPLCAToolConfigurationGenerator(cnf, "ICPL", t , LIMIT_CONFIGURATION_NUMBER);
        final List<LiteralSet> result = LongRunningWrapper.runMethod(generator, new ConsoleMonitor<>());

        List<String> icplResult = new ArrayList<>();
        result.forEach(literalSet -> icplResult.add(literalSet.toString()));
        List<String> resultList = new ArrayList<>();
        result.forEach(literalSet -> resultList.add(literalSet.toString()));
        return resultList;
    }

     /**
     * generates all configurations with chvatal of the featuremodel
     * @param featureModel featuremodel to analyze
     * @return resultList List<String>
     */
    @Override
    public Object chvatal(IFeatureModel featureModel) {
        FeatureModelFormula formula = new FeatureModelFormula(featureModel);
        CNF cnf = formula.getCNF();
        SPLCAToolConfigurationGenerator generator = new SPLCAToolConfigurationGenerator(cnf, "Chvatal", t, LIMIT_CONFIGURATION_NUMBER);
        final List<LiteralSet> result = LongRunningWrapper.runMethod(generator, new ConsoleMonitor<>());

        List<String> chvatalResult = new ArrayList<>();
        result.forEach(literalSet -> chvatalResult.add(literalSet.toString()));
        List<String> resultList = new ArrayList<>();
        result.forEach(literalSet -> resultList.add(literalSet.toString()));
        return resultList;
    }

     /**
     * generates all configurations of the featuremodel
     * @param featureModel featuremodel to analyze
     * @return resultList List<String>
     */
    @Override
    public Object all(IFeatureModel featureModel) {
        FeatureModelFormula formula = new FeatureModelFormula(featureModel);
        CNF cnf = formula.getCNF();
        AllConfigurationGenerator generator = new AllConfigurationGenerator(cnf, LIMIT_CONFIGURATION_NUMBER);
        final List<LiteralSet> result = LongRunningWrapper.runMethod(generator, new ConsoleMonitor<>());
        List<String> resultList = new ArrayList<>();
        result.forEach(literalSet -> resultList.add(literalSet.toString()));
        return resultList;
    }

    /**
     * generates random configurations of the featuremodel -> no usage for testing
     * @param featureModel featuremodel to analyze
     * @return resultList List<String>
     */
    @Override
    public Object random(IFeatureModel featureModel) {
        FeatureModelFormula formula = new FeatureModelFormula(featureModel);
        CNF cnf = formula.getCNF();
        RandomConfigurationGenerator generator = new RandomConfigurationGenerator(cnf, LIMIT_CONFIGURATION_NUMBER);
        final List<LiteralSet> result = LongRunningWrapper.runMethod(generator, new ConsoleMonitor<>());
        List<String> resultList = new ArrayList<>();
        result.forEach(literalSet -> resultList.add(literalSet.toString()));
        return resultList;
    }
}