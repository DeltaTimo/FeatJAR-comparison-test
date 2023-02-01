package de.featjar.comparison.test.helper.featjar;

import de.featjar.base.cli.CommandLineInterface;
import de.featjar.base.extension.ExtensionManager;
import de.featjar.comparison.test.helper.IBase;

import de.featjar.comparison.test.helper.tree.StringFormulaTree;
import de.featjar.formula.io.FormulaFormats;
import de.featjar.formula.structure.Expression;
import de.featjar.formula.structure.formula.Formula;
import de.featjar.formula.structure.formula.connective.*;
import de.featjar.formula.structure.formula.predicate.Literal;
import de.featjar.formula.structure.formula.predicate.Predicate;
import de.featjar.formula.structure.term.value.Value;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * This class contains all base operations of the FeatJAR library.
 * The interface IBase<Formula, Object> is implemented and the analyses are
 * used in the test class FeatureModelBaseTests
 * @author Katjana Herbst
 * @see de.featjar.comparison.test.FeatureModelBaseTests
 * @see IBase
 */
public class FeatJARBase implements IBase<Formula, Object> {
    protected final ExtensionManager extensionManager = new ExtensionManager();

    // close Extensionmanager
    public void cleanUp() {
        extensionManager.close();
    }

    /**
     * loads the formula of the belonging featuremodel.xml file for analyse
     * @param filepath
     * @return Formula
     */
    @Override
    public Formula load(String filepath) {
        Formula f = CommandLineInterface.loadFile(filepath, extensionManager.getExtensionPoint(FormulaFormats.class).get()).orElseThrow();
        return CommandLineInterface.loadFile(filepath, extensionManager.getExtensionPoint(FormulaFormats.class).get()).orElseThrow();
    }

    /**
     * transforms formula into String
     * @param featureModel
     * @return formula as String
     */
    @Override
    public Object getFormula(Object featureModel) {
        Formula formula = (Formula) featureModel;
        return  formula.printParseable();
    }

    public StringFormulaTree treeFromExpression(Expression formula) {
        if (formula instanceof Value) {
            return new StringFormulaTree.Leaf(formula.getName());
        } else if (formula instanceof Literal) {
            // Make "not" out of literal.
            if (formula.getChildren().size() > 1) {
                throw new IllegalArgumentException("Instance \"Not\" of Node has more than one child. How do we combine them?");
            } else {
                if (((Literal) formula).isPositive()) {
                    return treeFromExpression(formula.getChildren().get(0));
                } else {
                    return StringFormulaTree.UnaryOperator.minusNegate(treeFromExpression(formula.getChildren().get(0)));
                }
            }
        } else if (formula instanceof Not) {
            if (formula.getChildren().size() > 1) {
                throw new IllegalArgumentException("Instance \"Not\" of Node has more than one child. How do we combine them?");
            } else {
                return StringFormulaTree.UnaryOperator.minusNegate(treeFromExpression(formula.getChildren().get(0)));
            }
        } else if (formula instanceof Or) {
            StringFormulaTree result = StringFormulaTree.NAryOperator.plusOr();
            result.getChildren().addAll(formula.getChildren().stream().map(this::treeFromExpression).collect(Collectors.toList()));
            return result;
        } else if (formula instanceof And) {
            StringFormulaTree result = StringFormulaTree.NAryOperator.asteriskAnd();
            result.getChildren().addAll(formula.getChildren().stream().map(this::treeFromExpression).collect(Collectors.toList()));
            return result;
        } else if (formula instanceof Implies) {
            if (formula.getChildren().size() != 2) {
                throw new IllegalArgumentException("Instance \"Implies\" of Node has more than one child. How do we combine them?");
            } else {
                return StringFormulaTree.binaryOperator("=>", treeFromExpression(formula.getChildren().get(0)), treeFromExpression(formula.getChildren().get(1)));
            }
        }
        return null;
    }

    /**
     * transfers formula into String
     * @param formula Formula
     * @return HashSet of HashSets of Features as String
     */
    @Override
    public Object smoothFormula(Formula formula) {
        return treeFromExpression(formula).sort().getValue();
    }

    /**
     * not implemented yet
     * loads the formula from stored String -> less file access
     * @param filepath
     * @return Formula
     */
    @Override
    public Formula loadFromSource(String content, String filepath) {
        // TODO
        return null;
    }

    /**
     * not implemented yet
     * @param feature1
     * @param feature2
     * @return implies query of feature a and b
     */
    @Override
    public Object createQueryImpl(String feature1, String feature2) {
        // TODO
        return null;
    }

    /**
     * not implemented yet
     * @param feature1
     * @param feature2
     * @return and not query of feature a and b
     */
    @Override
    public Object createQueryAndNot(String feature1, String feature2) {
        // TODO
        return null;
    }

    /**
     * loads the content of the featuremode.xml as file
     * @param filepath
     * @return content of file as String
     */
    @Override
    public String loadConfiguration(String filepath) {
        String content = null;
        try {
            FileInputStream fis = new FileInputStream(filepath);
            byte[] buffer = new byte[10];
            StringBuilder sb = new StringBuilder();
            while (fis.read(buffer) != -1) {
                sb.append(new String(buffer));
                buffer = new byte[10];
            }
            fis.close();
            content = sb.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scanner scanner = new Scanner(content);
        scanner.useDelimiter(Pattern.compile("\\n+\\Z|\\n+|\\Z"));
        String result = null;
        if (scanner.hasNext()) {
            String line = scanner.next();
            if(scanner.hasNext()) {
                result = scanner.next();
            } else {
                throw new RuntimeException();
            }
        }
        return result.substring(0, result.length()-2);
    }
}