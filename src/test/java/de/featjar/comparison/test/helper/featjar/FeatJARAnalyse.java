package de.featjar.comparison.test.helper.featjar;

import de.featjar.base.FeatJAR;
import de.featjar.base.data.Computation;
import de.featjar.comparison.test.helper.IAnalyses;
import de.featjar.formula.analysis.Analysis;
import de.featjar.formula.analysis.bool.BooleanClauseList;
import de.featjar.formula.analysis.bool.ToLiteralClauseList;
import de.featjar.formula.analysis.sat4j.SAT4JHasSolutionAnalysis;
import de.featjar.formula.structure.formula.Formula;
import de.featjar.formula.transformer.ToCNF;

import java.util.List;
import java.util.Map;
import java.util.Set;


public class FeatJARAnalyse implements IAnalyses<Formula, Object> {

    @Override
    public Object isTautology(Formula formula, Object query) {
        return null;
    }

    @Override
    public Object isVoid(Formula formula) {
        Analysis<BooleanClauseList, Boolean> analyse = new SAT4JHasSolutionAnalysis(
                Computation.of(formula)
                        .then(ToCNF::new)
                        .then(ToLiteralClauseList::new)).setTimeout(new Long(1000));
        return !analyse.compute().get().get();
    }

    @Override
    public Object isVoid(Formula featureModel, String config) {
        return null;
    }

    @Override
    public Object coreFeatures(Formula formula) {
        return null;
    }

    @Override
    public Object deadFeatures(Formula formula) {
        return null;
    }

    @Override
    public Object falseOptional(Formula formula) {
        return null;
    }

    @Override
    public Object redundantConstraints(Formula formula) {
        return null;
    }

    @Override
    public Object atomicSets(Formula formula) {
        return null;
    }

    @Override
    public Object indeterminedHiddenFeatures(Formula formula) {
        return null;
    }

    @Override
    public Object countSolutions(Formula formula) {
        return null;
    }
}
