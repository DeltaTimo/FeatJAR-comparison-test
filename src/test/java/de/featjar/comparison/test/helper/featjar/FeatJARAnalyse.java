package de.featjar.comparison.test.helper.featjar;

import de.featjar.base.FeatJAR;
import de.featjar.base.data.Computation;
import de.featjar.comparison.test.helper.IAnalyses;
import de.featjar.comparison.test.helper.Result;
import de.featjar.formula.analysis.Analysis;
import de.featjar.formula.analysis.bool.BooleanClauseList;
import de.featjar.formula.analysis.bool.ToLiteralClauseList;
import de.featjar.formula.analysis.sat4j.SAT4JHasSolutionAnalysis;
import de.featjar.formula.structure.formula.Formula;
import de.featjar.formula.transformer.ToCNF;

import java.util.List;
import java.util.Set;


public class FeatJARAnalyse implements IAnalyses<Formula, Object> {

    @Override
    public Result<Boolean> isTautology(Formula formula, Object query) {
        return null;
    }

    @Override
    public Result<Boolean> isVoid(Formula formula) {
        Analysis<BooleanClauseList, Boolean> analyse = new SAT4JHasSolutionAnalysis(
                Computation.of(formula)
                        .then(ToCNF::new)
                        .then(ToLiteralClauseList::new)).setTimeout(new Long(1000));
        return new Result<>(!analyse.compute().get().get());
    }

    @Override
    public Result<Set<String>> coreFeatures(Formula formula) {
        return null;
    }

    @Override
    public Result<Set<String>> deadFeatures(Formula formula) {
        return null;
    }

    @Override
    public Result<Set<String>> falseOptional(Formula formula) {
        return null;
    }

    @Override
    public Result<Set<String>> redundantConstraints(Formula formula) {
        return null;
    }

    @Override
    public Result<List<Set<String>>> atomicSets(Formula formula) {
        return null;
    }

    @Override
    public Result<Set<String>> indeterminedHiddenFeatures(Formula formula) {
        return null;
    }

    @Override
    public Result<Long> countSolutions(Formula formula) {
        return null;
    }
}
