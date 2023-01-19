package de.featjar.comparison.test.helper.featureide;

import de.featjar.comparison.test.helper.IBase;
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

    /**
     * transfers formula into String
     * @param formula IFeatureModel
     * @return HashSet of HashSets of Features as String
     */
    @Override
    public Object smoothFormula(IFeatureModel formula) {
        String f = (String) getFormula(formula);
        String[] splitArr =  f.split("\\&");
        Set<HashSet> result = new HashSet<>();
        for(int i = 0; i < splitArr.length; i++) {
            Set<String> tmp = new HashSet<>();
            String conjunctionParts = splitArr[i].replaceAll("[\\[\\](){}]","");
            conjunctionParts = conjunctionParts.replaceAll("\\s+","");

            String[] splitArrTmp;
            if(conjunctionParts.contains("&")) {
                splitArrTmp = conjunctionParts.split("\\&");
                Arrays.stream(splitArrTmp).forEach(entry -> tmp.add(entry));
            } else if(conjunctionParts.contains("|")) {
                splitArrTmp = conjunctionParts.split("\\|");
                Arrays.stream(splitArrTmp).forEach(entry -> tmp.add(entry));
            } else {
                tmp.add(conjunctionParts);
            }
            result.add((HashSet) tmp);
        }
        return result;
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