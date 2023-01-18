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

public class FeatureIDEBase implements IBase<IFeatureModel, Node> {

    @Override
    public IFeatureModel load(String filepath) {
        LibraryManager.registerLibrary(FMCoreLibrary.getInstance());
        Path path = Paths.get(filepath);
        return FeatureModelManager.load(path);
    }

    @Override
    public Object getFormula(Object featureModel) {
        FeatureModelFormula formula = new FeatureModelFormula((IFeatureModel) featureModel);
        return formula.getCNFNode().toString();
    }

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

    @Override
    public IFeatureModel loadFromSource(String content, String filepath) {
        LibraryManager.registerLibrary(FMCoreLibrary.getInstance());
        FeatureModelIO featureModelIO = FeatureModelIO.getInstance();
        return featureModelIO.loadFromSource(content, Paths.get(filepath));
    }

    @Override
    public Node createQueryImpl(String feature1, String feature2) {
        return new Implies(new Literal(feature1), new Literal(feature2));
    }

    @Override
    public Node createQueryAndNot(String feature1, String feature2) {
        return new And(new Literal(feature1), new Not(new Literal(feature2)));
    }

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