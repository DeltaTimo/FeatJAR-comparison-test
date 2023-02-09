package de.featjar.comparison.test.helper.featureide;

import de.featjar.comparison.test.helper.IBase;
import de.featjar.comparison.test.helper.tree.StringFormulaTree;
import de.ovgu.featureide.fm.core.analysis.cnf.formula.FeatureModelFormula;
import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.init.FMCoreLibrary;
import de.ovgu.featureide.fm.core.init.LibraryManager;
import de.ovgu.featureide.fm.core.io.manager.FeatureModelIO;
import de.ovgu.featureide.fm.core.io.manager.FeatureModelManager;
import org.prop4j.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This class contains all base operations of the FeatureIDE library.
 * The interface IBase<IFeatureModel, Node> is implemented and the analyses are
 * used in the test class FeatureModelBaseTests
 * @author Katjana Herbst
 * @see de.featjar.comparison.test.FeatureModelBaseTests
 * @see IBase
 */
public class FeatureIDEBase implements IBase<IFeatureModel, Node> {

    /**
     * loads the formula of the belonging featuremodel.xml file for analyse
     * @param filepath
     * @return IFeatureModel
     */
    @Override
    public IFeatureModel load(String filepath) {
        LibraryManager.registerLibrary(FMCoreLibrary.getInstance());
        Path path = Paths.get(filepath);
        return FeatureModelManager.load(path);
    }

    /**
     * transforms formula into String
     * @param featureModel IFeatureModel
     * @return formula as String
     */
    @Override
    public Object getFormula(Object featureModel) {
        FeatureModelFormula formula = new FeatureModelFormula((IFeatureModel) featureModel);
        return formula.getCNFNode().toString();
    }

    public StringFormulaTree treeFromFeatureModel(IFeatureModel featureModel) {
        FeatureModelFormula formula = new FeatureModelFormula((IFeatureModel) featureModel);
        return treeFromFormula(formula.getCNFNode());
    }

    public StringFormulaTree treeFromFormula(Node node) {
        if (node instanceof Literal) {
            return new StringFormulaTree.Leaf(node.toString());
        } else if (node instanceof Not) {
            if (node.getChildren().length > 1) {
                throw new IllegalArgumentException("Instance \"Not\" of Node has more than one child. How do we combine them?");
            } else {
                return StringFormulaTree.UnaryOperator.minusNegate(treeFromFormula(node.getChildren()[0]));
            }
        } else if (node instanceof Or) {
            StringFormulaTree result = StringFormulaTree.NAryOperator.plusOr();
            result.getChildren().addAll(Arrays.stream(node.getChildren()).map(this::treeFromFormula).collect(Collectors.toList()));
            return result;
        } else if (node instanceof And) {
            StringFormulaTree result = StringFormulaTree.NAryOperator.asteriskAnd();
            result.getChildren().addAll(Arrays.stream(node.getChildren()).map(this::treeFromFormula).collect(Collectors.toList()));
            return result;
        } else if (node instanceof Implies) {
            if (node.getChildren().length != 2) {
                throw new IllegalArgumentException("Instance \"Implies\" of Node has more than one child. How do we combine them?");
            } else {
                return StringFormulaTree.binaryOperator("=>", treeFromFormula(node.getChildren()[0]), treeFromFormula(node.getChildren()[1]));
            }
        } else if (node instanceof Equals) {
            if (node.getChildren().length != 2) {
                throw new IllegalArgumentException("Instance \"Equals\" of Node has more than one child. How do we combine them?");
            } else {
                return StringFormulaTree.binaryOperator("==", treeFromFormula(node.getChildren()[0]), treeFromFormula(node.getChildren()[1]));
            }
        }
        return null;
}

    /**
     * transfers formula into String
     * @param formula IFeatureModel
     * @return HashSet of HashSets of Features as String
     */
    @Override
    public Object smoothFormula(IFeatureModel formula) {
        return treeFromFeatureModel(formula).sort().getValue();
    }

    /**
     * loads the formula from stored String -> less file access
     * @param filepath
     * @return IFeatureModel
     */
    @Override
    public IFeatureModel loadFromSource(String content, String filepath) {
        LibraryManager.registerLibrary(FMCoreLibrary.getInstance());
        FeatureModelIO featureModelIO = FeatureModelIO.getInstance();
        return featureModelIO.loadFromSource(content, Paths.get(filepath));
    }

    /**
     * @param feature1
     * @param feature2
     * @return implies query of feature a and b
     */
    @Override
    public Node createQueryImpl(String feature1, String feature2) {
        return new Implies(new Literal(feature1), new Literal(feature2));
    }

    /**
     * @param feature1
     * @param feature2
     * @return and not query of feature a and b
     */
    @Override
    public Node createQueryAndNot(String feature1, String feature2) {
        return new And(new Literal(feature1), new Not(new Literal(feature2)));
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
        return content;
    }
}