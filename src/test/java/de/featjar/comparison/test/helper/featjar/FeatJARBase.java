package de.featjar.comparison.test.helper.featjar;

import de.featjar.base.cli.CommandLineInterface;
import de.featjar.base.extension.ExtensionManager;
import de.featjar.comparison.test.helper.IAnalyses;
import de.featjar.comparison.test.helper.IBase;

import de.featjar.formula.io.FormulaFormats;
import de.featjar.formula.structure.formula.Formula;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;

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
     * @return formula
     */
    @Override
    public Formula load(String filepath) {
        Formula f = CommandLineInterface.loadFile(filepath, extensionManager.getExtensionPoint(FormulaFormats.class).get()).orElseThrow();
        return CommandLineInterface.loadFile(filepath, extensionManager.getExtensionPoint(FormulaFormats.class).get()).orElseThrow();
    }

    /**
     * transfers formula into String
     * @param featureModel
     * @return formula as String
     */
    @Override
    public Object getFormula(Object featureModel) {
        Formula formula = (Formula) featureModel;
        return  formula.printParseable();
    }


    @Override
    public Object smoothFormula(Formula formula) {
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
     * not implemented yet
     * loads the formula from stored String -> less file access
     * @param filepath
     * @return formula
     */
    @Override
    public Formula loadFromSource(String content, String filepath) {
        // TODO
        return null;
    }

    /**
     * not implemented yet
     * @param a
     * @param b
     * @return implies query of feature a and b
     */
    @Override
    public Object createQueryImpl(String a, String b) {
        // TODO
        return null;
    }

    /**
     * not implemented yet
     * @param a
     * @param b
     * @return and not query of feature a and b
     */
    @Override
    public Object createQueryAndNot(String a, String b) {
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