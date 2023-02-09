package de.featjar.comparison.test.helper.featjar;

import de.featjar.base.computation.Computations;
import de.featjar.base.computation.ComputePresence;
import de.featjar.cli.analysis.ComputeSolutionCountSharpSAT;
import de.featjar.comparison.test.helper.IAnalyses;
import de.featjar.formula.analysis.bool.BooleanSolution;
import de.featjar.formula.analysis.bool.ComputeBooleanRepresentationOfCNFFormula;
import de.featjar.formula.analysis.sat4j.ComputeAtomicSetsSAT4J;
import de.featjar.formula.analysis.sat4j.ComputeCoreDeadVariablesSAT4J;
import de.featjar.formula.analysis.sat4j.ComputeSolutionCountSAT4J;
import de.featjar.formula.analysis.sat4j.ComputeSolutionSAT4J;
import de.featjar.formula.analysis.value.ComputeValueRepresentationOfAssignment;
import de.featjar.formula.analysis.value.ComputeValueRepresentationOfSolutionList;
import de.featjar.formula.analysis.value.ValueAssignment;
import de.featjar.formula.structure.formula.IFormula;
import de.featjar.formula.transformer.ComputeCNFFormula;
import de.featjar.formula.transformer.ComputeNNFFormula;

import java.util.*;

import static de.featjar.base.computation.Computations.*;

/**
 * This class contains all analyses of the FeatJAR library.
 * The interface IAnalyses<Formula,Object> is implemented and the analyses are
 * used in the test class FeatureModelAnalysisTests
 * @author Katjana Herbst
 * @see de.featjar.comparison.test.FeatureModelAnalysisTests
 * @see IAnalyses
 */
public class FeatJARAnalyse implements IAnalyses<IFormula, Object> {
    /**
     * not implemented yet
     * checks whether featuremodel is true under all interpretations
     * @param formula the featuremodel as formula to analyze
     * @param query the query which should be tests for tautology
     * @return true or false
     */
    @Override
    public Object isTautology(IFormula formula, Object query) {
        return null;
    }

    /**
     * checks whether featuremodel is void or not
     * @param formula the featuremodel as formula to analyze
     * @return true or false
     */
    @Override
    public Object isVoid(IFormula formula) {
        boolean isvoid = !async(formula)
                .map(ComputeNNFFormula::new)
                .map(ComputeCNFFormula::new)
                .map(ComputeBooleanRepresentationOfCNFFormula::new)
                .map(Computations::getKey)
                .map(ComputeSolutionSAT4J::new)
                .map(ComputePresence<BooleanSolution>::new)
                .get()
                .get();
        return isvoid;
    }

    /**
     * not implemented yet
     * checks whether featuremodel with partial configuration is void or not
     * @param formula the featuremodel as formula to analyze
     * @param config contains the assumption of the (un)selected features
     * @return true or false
     * @see FeatJARAnalyse#parseConfig(String, Object)
     */
    @Override
    public Object isVoid(IFormula formula, String config) {
        return null;
    }

    /**
     * analyses the featuremodel for core features which must always be selected
     * @param formula the featuremodel as formula to analyze
     * @return core feature set of featuremodel
     */
    @Override
    public Object coreFeatures(IFormula formula) {
        var booleanRepresentation = async(formula)
                .map(ComputeNNFFormula::new)
                .map(ComputeCNFFormula::new)
                .map(ComputeBooleanRepresentationOfCNFFormula::new);
        var booleanClauseList = getKey(booleanRepresentation);
        var variableMap = getValue(booleanRepresentation);
        var analysis = new ComputeCoreDeadVariablesSAT4J(booleanClauseList);

        //  parse result
        ComputeValueRepresentationOfAssignment result = new ComputeValueRepresentationOfAssignment(analysis, variableMap);
        String core = result.computeResult().get().print();
        String[] coreArr = core.split(", ");
        Set<String> resultCore = new HashSet<>();
        Arrays.stream(coreArr).forEach(feature -> {
            if(!(feature.charAt(0)=='-')) {
                resultCore.add(feature);
            }
        });
        return resultCore;
    }

    /**
     * not implemented yet
     * analyses the featuremodel for core features which must always be selected with partial configuration
     * @param formula the featuremodel as formula to analyze
     * @param config contains the assumption of the (un)selected features
     * @return core feature set of featuremodel
     * @see FeatJARAnalyse#parseConfig(String, Object)
     */
    @Override
    public Object coreFeatures(IFormula formula, String config) {
        return null;
    }

    /**
     * analyses the featuremodel for dead features which can't be selected
     * @param formula the featuremodel as formula to analyze
     * @return dead feature set of featuremodel
     */
    @Override
    public Object deadFeatures(IFormula formula) {
        var booleanRepresentation = async(formula)
                .map(ComputeNNFFormula::new)
                .map(ComputeCNFFormula::new)
                .map(ComputeBooleanRepresentationOfCNFFormula::new);
        var booleanClauseList = getKey(booleanRepresentation);
        var variableMap = getValue(booleanRepresentation);
        var analysis = new ComputeCoreDeadVariablesSAT4J(booleanClauseList);

        //  parse result
        ComputeValueRepresentationOfAssignment result = new ComputeValueRepresentationOfAssignment(analysis, variableMap);
        String core = result.computeResult().get().print();
        String[] deadArr = core.split(", ");
        Set<String> resultDead = new HashSet<>();
        Arrays.stream(deadArr).forEach(feature -> {
            if((feature.charAt(0)=='-')) {
                resultDead.add(feature.replace("-", ""));
            }
        });
        return resultDead;
    }

    /**
     * not implemented yet
     * analyses the featuremodel for dead features which must always be selected with partial configuration
     * @param formula the featuremodel as formula to analyze
     * @param config contains the assumption of the (un)selected features
     * @return dead feature set of featuremodel
     * @see FeatJARAnalyse#parseConfig(String, Object)
     */
    @Override
    public Object deadFeatures(IFormula formula, String config) {
        return null;
    }

    /**
     * not implemented yet
     * analyses the featuremodel for features which are false optional
     * @param formula the featuremodel as formula to analyze
     * @return false optional feature set of featuremodel
     */
    @Override
    public Object falseOptional(IFormula formula) {
        return null;
    }

    /**
     * not implemented yet
     * analyses the featuremodel for features which are false optional with partial configuration
     * @param formula the featuremodel as formula to analyze
     * @param config contains the assumption of the (un)selected features
     * @return false optional feature set of featuremodel
     * @see FeatJARAnalyse#parseConfig(String, Object)
     */
    @Override
    public Object falseOptional(IFormula formula, String config) {
        return null;
    }

    /**
     * not implemented yet
     * analyses the featuremodel for redundant constraints
     * @param formula the featuremodel as formula to analyze
     * @return set of redundant constraints
     */
    @Override
    public Object redundantConstraints(IFormula formula) {
        return null;
    }

    /**
     * not implemented yet
     * analyses the featuremodel for redundant constraints of partial configuration
     * @param formula the featuremodel as formula to analyze
     * @param config contains the assumption of the (un)selected features
     * @return set of redundant constraints
     * @see FeatJARAnalyse#parseConfig(String, Object)
     */
    @Override
    public Object redundantConstraints(IFormula formula, String config) {
        return null;
    }

    /**
     * not implemented yet
     * analyses the featuremodel for atomic sets
     * @param formula the featuremodel as formula to analyze
     * @return atomic sets
     */
    @Override
    public Object atomicSets(IFormula formula) {
        // TODO at the moment works only for basic.xml | Error: java.util.NoSuchElementException: no object present
        var booleanRepresentation = async(formula)
                .map(ComputeNNFFormula::new)
                .map(ComputeCNFFormula::new)
                .map(ComputeBooleanRepresentationOfCNFFormula::new);
        var booleanClauseList = getKey(booleanRepresentation);
        var variableMap = getValue(booleanRepresentation);

        var analysis = new ComputeAtomicSetsSAT4J(booleanClauseList);
        //  parse result
        List<Set<String>> atomicList = new ArrayList<>();
        ComputeValueRepresentationOfSolutionList valueSolutionList = new ComputeValueRepresentationOfSolutionList(analysis, variableMap);
        valueSolutionList.computeResult().get().forEach(valueSolution -> atomicList.add(valueSolution.getVariableNames()));
        return atomicList;
    }

    /**
     * not implemented yet
     * analyses the featuremodel for atomic sets of partial configuration
     * @param formula the featuremodel as formula to analyze
     * @param config contains the assumption of the (un)selected features
     * @return atomic sets
     * @see FeatJARAnalyse#parseConfig(String, Object)
     */
    @Override
    public Object atomicSets(IFormula formula, String config) {
        return null;
    }

    /**
     * not implemented yet
     * analyses the featuremodel for idetermined hidden features
     * @param formula the featuremodel as formula to analyze
     * @return set of indetermined hidden features
     */
    @Override
    public Object indeterminedHiddenFeatures(IFormula formula) {
        return null;
    }

    /**
     * not implemented yet
     * analyses the featuremodel for idetermined hidden features of partial configuration
     * @param formula the featuremodel as formula to analyze
     * @param config contains the assumption of the (un)selected features
     * @return set of indetermined hidden features
     * @see FeatJARAnalyse#parseConfig(String, Object)
     */
    @Override
    public Object indeterminedHiddenFeatures(IFormula formula, String config) {
        return null;
    }

    /**
     * not implemented yet
     * count the number of possible solutions of the featuremodel configurations
     * @param formula the featuremodel as formula to analyze
     * @return number
     */
    @Override
    public Object countSolutions(IFormula formula) {
        // TODO SharpSAT Error:   class file for de.featjar.formula.analysis.sharpsat.ASharpSATAnalysis not found
       /*
        var cnfFormula = async(formula)
                .map(ComputeNNFFormula::new)
                .map(ComputeCNFFormula::new);
        var analysis = new ComputeSolutionCountSharpSAT().newAnalysis(cnfFormula);
        return analysis.computeResult().get();*/

        var booleanRepresentation = async(formula)
                .map(ComputeNNFFormula::new)
                .map(ComputeCNFFormula::new)
                .map(ComputeBooleanRepresentationOfCNFFormula::new);
        var booleanClauseList = getKey(booleanRepresentation);
        var analysis = new ComputeSolutionCountSAT4J(booleanClauseList);
        return analysis.get().get().longValueExact();
    }

    /**
     * not implemented yet
     * count the number of possible solutions of the featuremodel configurations
     * with partial configuration
     * @param formula the featuremodel as formula to analyze
     * @return number
     * @see FeatJARAnalyse#parseConfig(String, Object)
     */
    @Override
    public Object countSolutions(IFormula formula, String config) {
        return null;
    }

    /**
     * not implemented yet
     * help method to transform configuration to assumption
     * used for partial configuration
     * @param config (un)selected features as String
     * @param variables the all features of the model
     * @return assumption
     */
    @Override
    public Object parseConfig(String config, Object variables) {
        return null;
    }
}