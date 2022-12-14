package de.featjar.comparison.test.helper.featjar;

import de.featjar.base.cli.CommandLineInterface;
import de.featjar.base.extension.ExtensionManager;
import de.featjar.comparison.test.helper.IBase;

import de.featjar.formula.io.FormulaFormats;
import de.featjar.formula.structure.formula.Formula;

public class FeatJARBase implements IBase<Formula, Object> {
    protected final ExtensionManager extensionManager = new ExtensionManager();

    // close Extensionmanager
    public void cleanUp() {
        extensionManager.close();
    }

    @Override
    public Formula load(String filepath) {
        Formula f = CommandLineInterface.loadFile(filepath, extensionManager.getExtensionPoint(FormulaFormats.class).get()).orElseThrow();
        return CommandLineInterface.loadFile(filepath, extensionManager.getExtensionPoint(FormulaFormats.class).get()).orElseThrow();
    }

    @Override
    public Object getFormula(Object featureModel) {
        Formula formula = (Formula) featureModel;
        return  formula.cloneTree().printParseable();
    }

    @Override
    public Formula loadFromSource(String content, String filepath) {
        return null;
    }

    @Override
    public Object createQueryImpl(String a, String b) {
        return null;
    }

    @Override
    public Object createQueryAndNot(String a, String b) {
        return null;
    }

    @Override
    public String loadConfiguration(String filepath) {
        return null;
    }
}