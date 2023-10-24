package nl.han.ica.icss.transforms;

import nl.han.ica.datastructures.IHANLinkedList;
import nl.han.ica.datastructures.HANLinkedList;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.BoolLiteral;
import nl.han.ica.icss.ast.literals.PercentageLiteral;
import nl.han.ica.icss.ast.literals.PixelLiteral;
import nl.han.ica.icss.ast.literals.ScalarLiteral;
import nl.han.ica.icss.ast.operations.AddOperation;
import nl.han.ica.icss.ast.operations.MultiplyOperation;
import nl.han.ica.icss.ast.operations.SubtractOperation;

import java.util.*;

public class Evaluator implements Transform {

    private IHANLinkedList<HashMap<String, Literal>> variableValues;

    public Evaluator() {
        variableValues = new HANLinkedList<>();
    }

    @Override
    public void apply(AST ast) {
        Stylesheet stylesheet = ast.root;

        evaluateStylesheet(stylesheet);
    }

    /**
     * Evalueer de hele stylesheet door elke child ervan te behandelen.
     *
     * @param stylesheet De stylesheet om te evalueren.
     */
    private void evaluateStylesheet(Stylesheet stylesheet) {
        variableValues.addFirst(new HashMap<>());
        List<ASTNode> nodesToRemove = new ArrayList<>();

        for (ASTNode child : stylesheet.getChildren()) {
            if (child instanceof VariableAssignment) {
                evaluateVariableAssignment((VariableAssignment) child);
                nodesToRemove.add(child);
            } else if (child instanceof Stylerule) {
                evaluateStylerule((Stylerule) child);
            }
        }

        variableValues.removeFirst();
        nodesToRemove.forEach(stylesheet::removeChild);
    }

    /**
     * Evalueer een toewijzing van een variabele en werk deze eventueel bij.
     *
     * @param variableAssignment De toewijzing van de variabele om te evalueren.
     */
    private void evaluateVariableAssignment(VariableAssignment variableAssignment) {
        Expression expression = variableAssignment.expression;
        variableAssignment.expression = evaluateExpression(expression);

        variableValues.getFirst().put(variableAssignment.name.name, (Literal) variableAssignment.expression);
    }

    /**
     * Evalueer een expressie in de AST.
     *
     * @param expression De expressie om te evalueren.
     * @return Het resultaat van de evaluatie.
     */
    private Literal evaluateExpression(Expression expression) {
        if (expression instanceof Operation) {
            return evaluateOperation((Operation) expression);
        }

        if (expression instanceof VariableReference) {
            return getVariableLiteral(((VariableReference) expression).name, variableValues);
        }

        return (Literal) expression;
    }

    /**
     * Haal de waarde van een variabele op in de gegeven linked list.
     *
     * @param key            De naam van de variabele.
     * @param variableValues De linked list met variabele waarden.
     * @return Het resultaat van de variabele evaluatie.
     */
    public Literal getVariableLiteral(String key, IHANLinkedList<HashMap<String, Literal>> variableValues) {
        for (HashMap<String, Literal> variableValue : variableValues) {
            Literal result = variableValue.get(key);
            if (result != null) {
                return result;
            }
        }

        return null;
    }


    /**
     * Evalueer een stylerule en werk deze eventueel bij.
     *
     * @param stylerule De stylerule om te evalueren.
     */
    private void evaluateStylerule(Stylerule stylerule) {
        variableValues.addFirst(new HashMap<>());
        ArrayList<ASTNode> nodesToAdd = new ArrayList<>();

        for (ASTNode child : stylerule.body) {
            evaluateStyleruleBody(child, nodesToAdd);
        }

        variableValues.removeFirst();

        if (containsDuplicatePropertyDeclarations(nodesToAdd)) {
            stylerule.setError("Stylesheet bevat CSS declaratie met identieke properties.");
        }

        stylerule.body = nodesToAdd;
    }

    /**
     * Controleer of er duplicaten van CSS-eigenschappen zijn in de gegeven lijst van regels.
     *
     * @param ruleBody De lijst van regels om te controleren.
     * @return True als duplicaten worden gevonden, anders false.
     */
    private boolean containsDuplicatePropertyDeclarations(List<ASTNode> ruleBody) {
        Set<String> appeared = new HashSet<>();
        for (ASTNode rule : ruleBody) {
            if (rule instanceof Declaration) {
                String propertyName = ((Declaration) rule).property.name;
                if (!appeared.add(propertyName)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Evalueer een operatie en retourneer het resultaat.
     *
     * @param operation De operatie om te evalueren.
     * @return Het resultaat van de evaluatie als een literal.
     */
    private Literal evaluateOperation(Operation operation) {
        Literal left = getLiteralFromExpression(operation.lhs);
        Literal right = getLiteralFromExpression(operation.rhs);

        int leftValue = getLiteralValue(left);
        int rightValue = getLiteralValue(right);

        if (operation instanceof AddOperation) {
            return createLiteral(left, leftValue + rightValue);
        } else if (operation instanceof SubtractOperation) {
            return createLiteral(left, leftValue - rightValue);
        } else if (operation instanceof MultiplyOperation) {
            if (right instanceof ScalarLiteral) {
                return createLiteral(left, leftValue * rightValue);
            } else {
                return createLiteral(right, leftValue * rightValue);
            }
        } else {
            return createLiteral(left, leftValue / rightValue);
        }
    }

    /**
     * Creëer een nieuwe literal op basis van het gegeven waarde en het type van de originele literal.
     *
     * @param literal De originele literal waarvan het type wordt behouden.
     * @param value   De nieuwe waarde van de literal.
     * @return Een nieuwe literal met de bijgewerkte waarde.
     */
    private Literal createLiteral(Literal literal, int value) {
        if (literal instanceof PixelLiteral) {
            return new PixelLiteral(value);
        } else if (literal instanceof ScalarLiteral) {
            return new ScalarLiteral(value);
        } else {
            return new PercentageLiteral(value);
        }
    }

    /**
     * Haal een literal op uit een expressie en evalueer deze indien nodig.
     *
     * @param expression De expressie waaruit een literal moet worden opgehaald of geëvalueerd.
     * @return Een literal afkomstig uit de expressie of een geëvalueerde literal.
     */
    private Literal getLiteralFromExpression(Expression expression) {
        if (expression instanceof Operation) {
            return evaluateOperation((Operation) expression);
        } else if (expression instanceof VariableReference) {
            return getVariableLiteral(((VariableReference) expression).name, variableValues);
        } else {
            return (Literal) expression;
        }
    }

    /**
     * Haal de waarde van een literal op.
     *
     * @param literal De literal waarvan de waarde wordt opgehaald.
     * @return De integer waarde van de literal.
     */
    private int getLiteralValue(Literal literal) {
        if (literal instanceof PixelLiteral) {
            return ((PixelLiteral) literal).value;
        } else if (literal instanceof ScalarLiteral) {
            return ((ScalarLiteral) literal).value;
        } else {
            return ((PercentageLiteral) literal).value;
        }
    }

    /**
     * Evalueer de body van een stylerule en werk deze bij.
     *
     * @param ruleBody   Het kind van de stylerule om te evalueren.
     * @param parentBody De lijst waarin de bijgewerkte nodes worden toegevoegd.
     */
    private void evaluateStyleruleBody(ASTNode ruleBody, ArrayList<ASTNode> parentBody) {
        if (ruleBody instanceof VariableAssignment) {
            evaluateVariableAssignment((VariableAssignment) ruleBody);
        } else if (ruleBody instanceof Declaration) {
            evaluateDeclaration((Declaration) ruleBody);
            parentBody.add(ruleBody);
        } else if (ruleBody instanceof IfClause) {
            evaluateIfClause((IfClause) ruleBody, parentBody);
        }
    }

    /**
     * Evalueer een declaratie en werk de expressie binnenin bij.
     *
     * @param declaration De declaratie om te evalueren.
     */
    private void evaluateDeclaration(Declaration declaration) {
        declaration.expression = evaluateExpression(declaration.expression);
    }

    /**
     * Evalueer een if-clause en werk de body bij afhankelijk van de voorwaarde.
     *
     * @param ifClause   De if-clausule om te evalueren.
     * @param parentBody De lijst waarin de bijgewerkte nodes worden toegevoegd.
     */
    private void evaluateIfClause(IfClause ifClause, ArrayList<ASTNode> parentBody) {
        ifClause.conditionalExpression = evaluateExpression(ifClause.conditionalExpression);

        if (((BoolLiteral) ifClause.conditionalExpression).value) {
            if (ifClause.elseClause != null) {
                ifClause.elseClause.body = new ArrayList<>();
            }
        } else {
            if (ifClause.elseClause == null) {
                ifClause.body = new ArrayList<>();
            } else {
                ifClause.body = ifClause.elseClause.body;
                ifClause.elseClause.body = new ArrayList<>();
            }
        }

        for (ASTNode child : ifClause.getChildren()) {
            evaluateStyleruleBody(child, parentBody);
        }
    }

}
