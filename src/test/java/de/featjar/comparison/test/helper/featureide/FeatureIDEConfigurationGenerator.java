package de.featjar.comparison.test.helper.featureide;

import de.featjar.comparison.test.helper.IConfigurationGenerator;
import de.ovgu.featureide.fm.core.analysis.cnf.CNF;
import de.ovgu.featureide.fm.core.analysis.cnf.LiteralSet;
import de.ovgu.featureide.fm.core.analysis.cnf.SolutionList;
import de.ovgu.featureide.fm.core.analysis.cnf.formula.FeatureModelFormula;
import de.ovgu.featureide.fm.core.analysis.cnf.generator.configuration.AllConfigurationGenerator;
import de.ovgu.featureide.fm.core.analysis.cnf.generator.configuration.PairWiseConfigurationGenerator;
import de.ovgu.featureide.fm.core.analysis.cnf.generator.configuration.RandomConfigurationGenerator;
import de.ovgu.featureide.fm.core.analysis.cnf.generator.configuration.SPLCAToolConfigurationGenerator;
import de.ovgu.featureide.fm.core.base.IFeature;
import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.functional.Functional;
import de.ovgu.featureide.fm.core.io.csv.ConfigurationListFormat;
import de.ovgu.featureide.fm.core.io.manager.FileHandler;
import de.ovgu.featureide.fm.core.job.LongRunningWrapper;
import de.ovgu.featureide.fm.core.job.monitor.ConsoleMonitor;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FeatureIDEConfigurationGenerator implements IConfigurationGenerator<IFeatureModel> {
    final private int LIMIT_CONFIGURATION_NUMBER = Integer.MAX_VALUE;
    final private int t = 0;
    Path outputFile = new File("C:\\Users\\natha\\Documents\\GitHub\\FeatJAR\\FeatJAR-comparison-test\\src\\test\\java\\de\\featjar\\comparison\\test\\helper\\featureide\\tmp.csv").toPath();

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

    @Override
    public Object icpl(IFeatureModel featureModel) {
        FeatureModelFormula formula = new FeatureModelFormula(featureModel);
        CNF cnf = formula.getCNF();
        SPLCAToolConfigurationGenerator generator = new SPLCAToolConfigurationGenerator(cnf, "ICPL", t , LIMIT_CONFIGURATION_NUMBER);
        // TODO Exception splar/core/fm/FeatureModelException
        return null;
    }

    @Override
    public Object chvatal(IFeatureModel featureModel) {
        FeatureModelFormula formula = new FeatureModelFormula(featureModel);
        CNF cnf = formula.getCNF();
        SPLCAToolConfigurationGenerator generator = new SPLCAToolConfigurationGenerator(cnf, "Chvatal", t, LIMIT_CONFIGURATION_NUMBER);
        final List<LiteralSet> result = LongRunningWrapper.runMethod(generator, new ConsoleMonitor<>());
        // TODO Exception splar/core/fm/FeatureModelException
        return null;
    }

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
