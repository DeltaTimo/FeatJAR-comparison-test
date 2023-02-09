package de.featjar.comparison.test.helper.featjar;

import de.featjar.base.data.Computation;
import de.featjar.base.data.FutureResult;
import de.featjar.base.data.Result;
import de.featjar.comparison.test.helper.IAnalyses;
import de.featjar.formula.analysis.VariableMap;
import de.featjar.formula.analysis.bool.BooleanAssignment;
import de.featjar.formula.analysis.bool.ComputeBooleanRepresentation;
import de.featjar.formula.analysis.sat4j.AnalyzeHasSolutionSAT4J;
import de.featjar.formula.analysis.value.ComputeValueRepresentation;
import de.featjar.formula.analysis.value.ValueAssignment;
import de.featjar.formula.structure.formula.Formula;
import de.featjar.formula.transformer.ComputeCNFFormula;
import de.featjar.formula.transformer.ComputeNNFFormula;
import de.featjar.formula.analysis.sat4j.AnalyzeCoreDeadVariablesSAT4J;

import java.util.*;
import java.util.stream.Collectors;

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
        System.out.println(formula);
        System.out.println(config);

        var booleanRepresentation =
                async(formula)
                        .map(ComputeNNFFormula::new)
                        .map(ComputeCNFFormula::new)
                        .map(ComputeBooleanRepresentation.OfFormula::new);
        var booleanClauseList = getKey(booleanRepresentation);
        var variableMapComputation = getValue(booleanRepresentation);

        String[] lines = config.split("\n");
        if (lines.length < 2) {
            System.out.println(config);
            throw new IllegalArgumentException("Partial Config CSV requires at least two lines. Found " + lines.length + " (Line 1: Names, Line 2: Assignments)");
        }

        // WARN: Columns must not be escaped in double quotes, as the following simple parsing does not care about quoted columns.
        String[] variableNames = lines[0].split(";");
        String[] variableAssignments = lines[1].split(";");
        if (variableNames.length != variableAssignments.length) {
            throw new IllegalArgumentException("Partial Config CSV contains an unequal amount of header columns and non-header columns.");
        }

        // Create a map from name to assignment.
        LinkedHashMap<String, Object> assignmentMap = new LinkedHashMap<>();
        // Add all but first (description column) to map.
        for (int i = 1; i < variableNames.length; ++i) {
            char varChar = variableAssignments[i].charAt(0);
            if (varChar == '+' || varChar == '-') {
                assignmentMap.put(variableNames[i], varChar == '+');
            }
        }

        System.out.println(assignmentMap);

        var result = new AnalyzeCoreDeadVariablesSAT4J()
                .setInput(booleanClauseList)
                .setAssumedAssignment((Computation<BooleanAssignment>) new ValueAssignment(assignmentMap).toBoolean(variableMapComputation));

        //  parse result
        Computation<ValueAssignment> assignmentComputation = async(result, variableMapComputation).map(ComputeValueRepresentation.OfAssignment::new);

        Object resultDead_;
        try {
            var computed = assignmentComputation.compute();
            var core1 = computed.get().get();
            var core = (core1 == null ? "" : core1.print());
            // String core = computed.get().get().print();
            String[] coreArr = core.split(", ");
            Set<String> resultDead = new HashSet<>();
            Arrays.stream(coreArr).forEach(feature -> {
                if(!feature.isEmpty() && (feature.charAt(0)=='-')) {
                    resultDead.add(feature.replace("-", ""));
                }
            });

            // resultDead_ = resultDead;
            resultDead_ = String.join("\n", resultDead);
        } catch (Exception e) {
            System.err.println("Caught exception: " + e);
            resultDead_ = e;
        }

        return resultDead_;
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