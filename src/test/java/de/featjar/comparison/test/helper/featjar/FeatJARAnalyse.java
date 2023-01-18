package de.featjar.comparison.test.helper.featjar;

import de.featjar.base.data.Computation;
import de.featjar.comparison.test.helper.IAnalyses;
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

import static de.featjar.base.data.Computations.*;


public class FeatJARAnalyse implements IAnalyses<Formula, Object> {

    @Override
    public Object isTautology(Formula formula, Object query) {
        return null;
    }

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

    @Override
    public Object isVoid(Formula formula, String config) {
        return null;
    }

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

    @Override
    public Object coreFeatures(Formula featureModel, String config) {
        return null;
    }

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

    @Override
    public Object deadFeatures(Formula featureModel, String config) {
        return null;
    }

    @Override
    public Object falseOptional(Formula formula) {
        return null;
    }

    @Override
    public Object falseOptional(Formula featureModel, String config) {
        return null;
    }

    @Override
    public Object redundantConstraints(Formula formula) {
        return null;
    }

    @Override
    public Object redundantConstraints(Formula featureModel, String config) {
        return null;
    }

    @Override
    public Object atomicSets(Formula formula) {
        return null;
    }

    @Override
    public Object atomicSets(Formula featureModel, String config) {
        return null;
    }

    @Override
    public Object indeterminedHiddenFeatures(Formula formula) {
        return null;
    }

    @Override
    public Object indeterminedHiddenFeatures(Formula featureModel, String config) {
        return null;
    }

    @Override
    public Object countSolutions(Formula formula) {
        return null;
    }

    @Override
    public Object countSolutions(Formula featureModel, String config) {
        return null;
    }

    @Override
    public Object parseConfig(String config, Object variables) {
        return null;
    }
}