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
    public Object coreFeatures(IFeatureModel featureModel, String config) {
        if (featureModel != null) {
            final FeatureModelFormula formula = new FeatureModelFormula(featureModel);
            Variables variables = formula.getVariables();

            SolutionList tmp = parseConfig(config, variables);
            CNF cnf = formula.getCNF();
            CoreDeadAnalysis coreDeadAnalysis = new CoreDeadAnalysis(cnf, tmp.getSolutions().get(0));
            try {
                IMonitor<LiteralSet> monitor = new NullMonitor();
                LiteralSet coreLiterals = coreDeadAnalysis.analyze(monitor).getPositive();
                return Arrays.stream(coreLiterals.getPositive().getLiterals())
                        .mapToObj(l -> variables.getName(l))
                        .collect(Collectors.joining("\n"));
            } catch (Exception e) {
                e.printStackTrace();
            }
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
    public Object deadFeatures(IFeatureModel featureModel, String config) {
        if (featureModel != null) {
            final FeatureModelFormula formula = new FeatureModelFormula(featureModel);
            Variables variables = formula.getVariables();

            SolutionList tmp = parseConfig(config, variables);
            CNF cnf = formula.getCNF();
            CoreDeadAnalysis coreDeadAnalysis = new CoreDeadAnalysis(cnf, tmp.getSolutions().get(0));
            try {
                IMonitor<LiteralSet> monitor = new NullMonitor();
                LiteralSet coreLiterals =  coreDeadAnalysis.analyze(monitor).getNegative();
                return Arrays.stream(coreLiterals.getNegative().getLiterals())
                        .mapToObj(l -> variables.getName(l))
                        .collect(Collectors.joining("\n"));
            } catch (Exception e) {
                e.printStackTrace();
            }
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
    public Object falseOptional(IFeatureModel featureModel, String config) {
        // variables needed for analysis
        final FeatureModelFormula formula = new FeatureModelFormula(featureModel);
        Variables variables = formula.getVariables();
        SolutionList tmp = parseConfig(config, variables);
        CNF cnf = formula.getCNF();

        // collect all optional features
        List<IFeature> optionalFeatures = Functional.filterToList(featureModel.getFeatures(), new OptionalFeatureFilter());

        // create Analysis with assumptions -> partial config
        IndependentRedundancyAnalysis analysis = new IndependentRedundancyAnalysis(cnf);
        analysis.setAssumptions(tmp.getSolutions().get(0));

        // add clauses from optional features
        List<LiteralSet> literalSetList = new ArrayList();
        Iterator var5 = optionalFeatures.iterator();

        while(var5.hasNext()) {
            IFeature iFeature = (IFeature)var5.next();
            literalSetList.add(new LiteralSet(new int[]{variables.getVariable(FeatureUtils.getParent(iFeature).getName(), false), variables.getVariable(iFeature.getName(), true)}));
        }
        analysis.setClauseList(literalSetList);

        try {
            // execute analysis
            IMonitor<List<LiteralSet>> monitor = new NullMonitor();
            List<LiteralSet> result = (List)analysis.analyze(monitor);
            if(result == null) return Collections.emptyList();

            // prepare return value
            List<String> resultList = new ArrayList();
            int i = 0;

            for(Iterator var6 = result.iterator(); var6.hasNext(); ++i) {
                LiteralSet iFeature = (LiteralSet)var6.next();
                if (iFeature != null) {
                    resultList.add(optionalFeatures.get(i).getName());
                }
            }
            return resultList;
        } catch (Exception e) {
            e.printStackTrace();
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

    public SolutionList parseConfig(String config, Object variables) {
        int lineNumber = 0;
        SolutionList configurationList = new SolutionList();
        configurationList.setVariables((Variables)variables);
        Scanner scanner = new Scanner(config);
        scanner.useDelimiter(Pattern.compile("\\n+\\Z|\\n+|\\Z"));

        if (scanner.hasNext()) {
            String line = scanner.next();
            String[] split = line.split(";");
            while(scanner.hasNext()) {
                line = scanner.next();
                ++lineNumber;
                split = line.split(";");

                int[] literals = new int[configurationList.getVariables().size()];
                int index = 0;

                for(int i = 1; i < split.length; ++i) {
                    String var12 = split[i];
                    byte var13 = -1;
                    switch(var12.hashCode()) {
                        case 43:
                            if (var12.equals("+")) var13 = 1;
                            break;
                        case 45:
                            if (var12.equals("-")) var13 = 2;
                            break;
                        case 48:
                            if (var12.equals("0")) var13 = 0;
                    }
                    switch(var13) {
                        case 0:
                            break;
                        case 1:
                            literals[index++] = i;
                            break;
                        case 2:
                            literals[index++] = -i;
                            break;
                    }
                }
                if (index == configurationList.getVariables().size()) {
                    configurationList.addSolution(new LiteralSet(literals, LiteralSet.Order.INDEX, false));
                } else {
                    configurationList.addSolution(new LiteralSet(Arrays.copyOfRange(literals, 0, index), LiteralSet.Order.UNORDERED, false));
                }
            }
        }
        return configurationList;
    }
}