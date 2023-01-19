package de.featjar.comparison.test.helper.featjar;

import de.featjar.base.data.Computation;
import de.featjar.comparison.test.helper.IAnalyses;
import de.featjar.formula.analysis.bool.ComputeBooleanRepresentation;
import de.featjar.formula.analysis.sat4j.AnalyzeHasSolutionSAT4J;
import de.featjar.formula.analysis.value.ComputeValueRepresentation;
import de.featjar.formula.analysis.value.ValueAssignment;
import de.featjar.formula.structure.formula.Formula;
import de.featjar.formula.transformer.ComputeCNFFormula;
import de.featjar.formula.transformer.ComputeNNFFormula;
import de.featjar.formula.analysis.sat4j.AnalyzeCoreDeadVariablesSAT4J;

import java.util.*;

import static de.featjar.base.data.Computations.*;

/**
 * This class contains all analyses of the FeatJAR library.
 * The interface IAnalyses<Formula,Object> is implemented and the analyses are
 * used in the test class FeatureModelAnalysisTests
 * @author Katjana Herbst
 * @see de.featjar.comparison.test.FeatureModelAnalysisTests
 * @see IAnalyses
 */
public class FeatJARAnalyse implements IAnalyses<Formula, Object> {
    /**
     * not implemented yet
     * checks whether featuremodel is true under all interpretations
     * @param formula the featuremodel as formula to analyze
     * @param query the query which should be tests for tautology
     * @return true or false
     */
    @Override
    public Object isTautology(Formula formula, Object query) {
        return null;
    }

    /**
     * checks whether featuremodel is void or not
     * @param formula the featuremodel as formula to analyze
     * @return true or false
     */
    @Override
    public Object isVoid(Formula formula) {
        var booleanRepresentation =
                async(formula)
                        .map(ComputeNNFFormula::new)
                        .map(ComputeCNFFormula::new)
                        .map(ComputeBooleanRepresentation.OfFormula::new);
        var booleanClauseList = getKey(booleanRepresentation);
        var result = new AnalyzeHasSolutionSAT4J().setInput(booleanClauseList);
        /*
        Analysis<BooleanClauseList, Boolean> analyse = new AnalyzeHasSolutionSAT4J(
                Computation.of(formula).then(ToCNF::new)
                        .then(ToLiteralClauseList::new)).setTimeout(new Long(1000));
        return !analyse.compute().get().get();*/
        return !result.compute().get().get();
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
    public Object isVoid(Formula formula, String config) {
        return null;
    }

    /**
     * analyses the featuremodel for core features which must always be selected
     * @param formula the featuremodel as formula to analyze
     * @return core feature set of featuremodel
     */
    @Override
    public Object coreFeatures(Formula formula) {
        var booleanRepresentation =
                async(formula)
                        .map(ComputeNNFFormula::new)
                        .map(ComputeCNFFormula::new)
                        .map(ComputeBooleanRepresentation.OfFormula::new);
        var booleanClauseList = getKey(booleanRepresentation);
        var variableMap = getValue(booleanRepresentation);
        var result = new AnalyzeCoreDeadVariablesSAT4J().setInput(booleanClauseList);

        //  parse result
        Computation<ValueAssignment> assignmentComputation = async(result, variableMap).map(ComputeValueRepresentation.OfAssignment::new);
        String core = assignmentComputation.compute().get().get().print();
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
    public Object coreFeatures(Formula formula, String config) {
        return null;
    }

    /**
     * analyses the featuremodel for dead features which can't be selected
     * @param formula the featuremodel as formula to analyze
     * @return dead feature set of featuremodel
     */
    @Override
    public Object deadFeatures(Formula formula) {
        var booleanRepresentation =
                async(formula)
                        .map(ComputeNNFFormula::new)
                        .map(ComputeCNFFormula::new)
                        .map(ComputeBooleanRepresentation.OfFormula::new);
        var booleanClauseList = getKey(booleanRepresentation);
        var variableMap = getValue(booleanRepresentation);
        var result = new AnalyzeCoreDeadVariablesSAT4J().setInput(booleanClauseList);

        //  parse result
        Computation<ValueAssignment> assignmentComputation = async(result, variableMap).map(ComputeValueRepresentation.OfAssignment::new);
        String core = assignmentComputation.compute().get().get().print();
        String[] coreArr = core.split(", ");
        Set<String> resultDead = new HashSet<>();
        Arrays.stream(coreArr).forEach(feature -> {
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
    public Object deadFeatures(Formula formula, String config) {
        return null;
    }

    /**
     * not implemented yet
     * analyses the featuremodel for features which are false optional
     * @param formula the featuremodel as formula to analyze
     * @return false optional feature set of featuremodel
     */
    @Override
    public Object falseOptional(Formula formula) {
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
    public Object falseOptional(Formula formula, String config) {
        return null;
    }

    /**
     * not implemented yet
     * analyses the featuremodel for redundant constraints
     * @param formula the featuremodel as formula to analyze
     * @return set of redundant constraints
     */
    @Override
    public Object redundantConstraints(Formula formula) {
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
    public Object redundantConstraints(Formula formula, String config) {
        return null;
    }

    /**
     * not implemented yet
     * analyses the featuremodel for atomic sets
     * @param formula the featuremodel as formula to analyze
     * @return atomic sets
     */
    @Override
    public Object atomicSets(Formula formula) {
        return null;
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
    public Object atomicSets(Formula formula, String config) {
        return null;
    }

    /**
     * not implemented yet
     * analyses the featuremodel for idetermined hidden features
     * @param formula the featuremodel as formula to analyze
     * @return set of indetermined hidden features
     */
    @Override
    public Object indeterminedHiddenFeatures(Formula formula) {
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
    public Object indeterminedHiddenFeatures(Formula formula, String config) {
        return null;
    }

    /**
     * not implemented yet
     * count the number of possible solutions of the featuremodel configurations
     * @param formula the featuremodel as formula to analyze
     * @return number
     */
    @Override
    public Object countSolutions(Formula formula) {
        return null;
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
    public Object countSolutions(Formula formula, String config) {
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