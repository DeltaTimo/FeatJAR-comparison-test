package de.featjar.comparison.test.helper.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface StringFormulaTree {
    String getValue();
    default Collection<StringFormulaTree> getChildren() {
        return new ArrayList<>();
    }
    StringFormulaTree sort();

    class Leaf implements StringFormulaTree {
        public final String value;

        public Leaf(String value) {
            this.value = value;
        }

        @Override
        public String getValue() {
            return value;
        }

        @Override
        public StringFormulaTree sort() {
            // Do nothing.
            return this;
        }
    }

    static NAryOperator binaryOperator(String symbol, StringFormulaTree left, StringFormulaTree right) {
        NAryOperator op = new NAryOperator((String a, String b) -> a + " " + symbol + " " + b);
        op.getChildren().add(left);
        op.getChildren().add(right);
        return op;
    }
    class NAryOperator implements StringFormulaTree {
        public final BiFunction<String, String, String> aggregator;
        public final ArrayList<StringFormulaTree> children = new ArrayList<>();

        public NAryOperator(BiFunction<String, String, String> aggregator) {
            this.aggregator = aggregator;
        }

        public static NAryOperator plusOr() {
            return new NAryOperator((String a, String b) -> a + " + " + b);
        }
        public static NAryOperator asteriskAnd() {
            return new NAryOperator((String a, String b) -> a + " * " + b);
        }

        @Override
        public String getValue() {
            // Aggregate.
            if (children.isEmpty()) {
                return "";
            } else if (children.size() == 1) {
                return children.get(0).getValue();
            } else {
                /* More than 1 */
                String value = children.get(0).getValue();
                for (var child : children.subList(1, children.size())) {
                    value = aggregator.apply(value, child.getValue());
                }
                return "(" + value + ")";
            }
        }

        @Override
        public ArrayList<StringFormulaTree> getChildren() {
            return children;
        }

        @Override
        public StringFormulaTree sort() {
            // Sort each child.
            children.forEach(StringFormulaTree::sort);
            // Then sort children list.
            children.sort(Comparator.comparing(StringFormulaTree::getValue));
            return this;
        }
    }

    class UnaryOperator implements StringFormulaTree {
        public final Function<String, String> function;
        public final StringFormulaTree child;

        public UnaryOperator(Function<String, String> function, StringFormulaTree child) {
            this.function = function;
            this.child = child;
        }

        public static UnaryOperator minusNegate(StringFormulaTree child) {
            return new UnaryOperator((String a) -> "-" + a, child);
        }

        @Override
        public String getValue() {
            return function.apply(child.getValue());
        }

        @Override
        public StringFormulaTree sort() {
            // Do nothing.
            return this;
        }
    }
}
