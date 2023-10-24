package nl.han.ica.icss.checker;

import nl.han.ica.datastructures.HANLinkedList;
import nl.han.ica.datastructures.IHANLinkedList;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.types.ExpressionType;

import java.util.ArrayList;
import java.util.HashMap;
import nl.han.ica.icss.ast.literals.*;
import nl.han.ica.icss.ast.operations.*;

/**
 * De Checker-klasse is verantwoordelijk voor het uitvoeren van diverse checks op een ICSS Abstract Syntax Tree (AST).
 * De volgende CH-vereisten worden gedekt:
 * - CH00: Minimaal vier van de onderstaande checks moeten zijn geïmplementeerd
 * - CH01: Controleer of er geen variabelen worden gebruikt die niet gedefinieerd zijn.
 * - CH02: Controleer of de operanden van de operaties plus en min van gelijk type zijn.
 * - CH03: Controleer of er geen kleuren worden gebruikt in operaties (plus, min en keer).
 * - CH04: Controleer of bij declaraties het type van de value klopt met de property.
 * - CH05: Controleer of de conditie bij een if-statement van het type boolean is.
 * - CH06: Controleer of variabelen enkel binnen hun scope gebruikt worden.
 */
public class Checker {
    private final IHANLinkedList<HashMap<String, ExpressionType>> variableTypes;

    public Checker() {
        this.variableTypes = new HANLinkedList<>();
    }

    /**
     * Voert de checks uit op de gegeven AST.
     *
     * @param ast Het Abstract Syntax Tree-object dat moet worden gecontroleerd.
     */
    public void check(AST ast) {
        checkStylesheet(ast.root);
    }

    /**
     * Voert checks uit op een Stylesheet-knooppunt in de AST.
     *
     * @param astNode Het Stylesheet-knooppunt dat moet worden gecontroleerd.
     */
    private void checkStylesheet(ASTNode astNode) {
        Stylesheet stylesheet = (Stylesheet) astNode;
        variableTypes.addFirst(new HashMap<>());

        for (ASTNode child : stylesheet.getChildren()) {
            if (child instanceof VariableAssignment) {
                checkVariableAssignment(child);
            } else if (child instanceof Stylerule) {
                variableTypes.addFirst(new HashMap<>());
                checkStylerule(child);
                variableTypes.removeFirst();
            }
        }

        variableTypes.clear();
    }

    /**
     * Voert checks uit op een Stylerule-knooppunt in de AST.
     *
     * @param astNode Het Stylerule-knooppunt dat moet worden gecontroleerd.
     */
    private void checkStylerule(ASTNode astNode) {
        Stylerule stylerule = (Stylerule) astNode;
        checkRuleBody(stylerule.body);
    }

    /**
     * Voert checks uit op de verzameling declaraties en if-clauses in een Stylerule.
     *
     * @param astNodes De lijst met declaraties en if-clauses die moeten worden gecontroleerd.
     */
    private void checkRuleBody(ArrayList<ASTNode> astNodes) {
        for (ASTNode astNode : astNodes) {
            if (astNode instanceof Declaration) {
                checkDeclaration(astNode);
            } else if (astNode instanceof IfClause) {
                checkIfClause(astNode);
            } else if (astNode instanceof VariableAssignment) {
                checkVariableAssignment(astNode);
            }
        }
    }

    /**
     * Voert checks uit op een if-clause in de AST.
     *
     * @param astNode De if-clause die moet worden gecontroleerd.
     */
    private void checkIfClause(ASTNode astNode) {
        IfClause ifClause = (IfClause) astNode;
        variableTypes.addFirst(new HashMap<>());

        Expression conditionalExpression = ifClause.getConditionalExpression();
        ExpressionType expressionType = checkExpressionType(conditionalExpression);

        if (expressionType != ExpressionType.BOOL) {
            ifClause.setError("Conditional expression moet een boolean literal type hebben.");
        }

        checkRuleBody(ifClause.body);

        variableTypes.removeFirst();

        if (ifClause.getElseClause() != null) {
            variableTypes.addFirst(new HashMap<>());
            checkElseClause(ifClause.getElseClause());
            variableTypes.removeFirst();
        }
    }

    /**
     * Voert checks uit op een else-clausule in de AST.
     *
     * @param astNode De else-clausule die moet worden gecontroleerd.
     */
    private void checkElseClause(ASTNode astNode) {
        ElseClause elseClause = (ElseClause) astNode;
        checkRuleBody(elseClause.body);
    }

    /**
     * Voert checks uit op een declaratie in de AST.
     *
     * @param astNode De declaratie die moet worden gecontroleerd.
     */
    private void checkDeclaration(ASTNode astNode) {
        Declaration declaration = (Declaration) astNode;
        ExpressionType expressionType = checkExpression(declaration.expression);

        switch (declaration.property.name) {
            case "color":
                if (expressionType != ExpressionType.COLOR) {
                    astNode.setError("Color waarde kan alleen van type color literal zijn.");
                }
                break;
            case "background-color":
                if (expressionType != ExpressionType.COLOR) {
                    astNode.setError("Background-color waarde kan alleen van color literal type zijn.");
                }
                break;
            case "width":
                if (expressionType != ExpressionType.PIXEL && expressionType != ExpressionType.PERCENTAGE) {
                    astNode.setError("Width waarde kan alleen van type pixel, of percentage literal zijn.");
                }
                break;
            case "height":
                if (expressionType != ExpressionType.PIXEL && expressionType != ExpressionType.PERCENTAGE) {
                    astNode.setError("Height waarde kan alleen van percentage of pixel literal type zijn.");
                }
                break;
            default:
                astNode.setError("De enige toegestane properties zijn: height, weight, color of background-color.");
                break;
        }
    }

    /**
     * Voert checks uit op een variabele-assignment in de AST.
     *
     * @param astNode De variabele-assignment die moet worden gecontroleerd.
     */
    private void checkVariableAssignment(ASTNode astNode) {
        VariableAssignment variableAssignment = (VariableAssignment) astNode;
        VariableReference variableReference = variableAssignment.name;
        ExpressionType expressionType = checkExpression(variableAssignment.expression);

        if (expressionType == null || expressionType == ExpressionType.UNDEFINED) {
            astNode.setError("Variabele assignment lukt niet omdat de expression type undefined is.");
            return;
        }

        ExpressionType previousExpressionType = getVariableType(variableReference.name);
        if (variableTypeChanged(expressionType, previousExpressionType)) {
            astNode.setError("Een variabele kan niet veranderen van type: " + previousExpressionType + " ,naar type: " + expressionType);
        }

        putVariableType(variableReference.name, expressionType);
    }

    /**
     * Voert checks uit op een expressie in de AST, inclusief operaties.
     *
     * @param astNode De expressie die moet worden gecontroleerd.
     * @return Het type van de expressie na de controle.
     */
    private ExpressionType checkExpression(ASTNode astNode) {
        Expression expression = (Expression) astNode;

        if (expression instanceof Operation) {
            return checkOperation((Operation) expression);
        }

        return checkExpressionType(expression);
    }

    /**
     * Voert checks uit op een operation-expressie in de AST.
     *
     * @param operation De operation-expressie die moet worden gecontroleerd.
     * @return Het type van de expressie na de controle.
     */
    private ExpressionType checkOperation(Operation operation) {
        ExpressionType left;
        ExpressionType right;

        if (operation.lhs instanceof Operation) {
            left = checkOperation((Operation) operation.lhs);
        } else {
            left = checkExpressionType(operation.lhs);
        }

        if (operation.rhs instanceof Operation) {
            right = checkOperation((Operation) operation.rhs);
        } else {
            right = checkExpressionType(operation.rhs);
        }


        if (left == ExpressionType.COLOR || right == ExpressionType.COLOR || left == ExpressionType.BOOL || right == ExpressionType.BOOL) {
            operation.setError("Booleans en colors zijn niet toegestaan in een operation.");
            return ExpressionType.UNDEFINED;
        }

        if (operation instanceof MultiplyOperation) {
            if (left != ExpressionType.SCALAR && right != ExpressionType.SCALAR) {
                operation.setError("Multiply uitvoeren is alleen toegestaan met minimaal één scalar waarde.");
                return ExpressionType.UNDEFINED;
            }
            return right != ExpressionType.SCALAR ? right : left;
        } else if ((operation instanceof SubtractOperation || operation instanceof AddOperation) && left != right) {
            operation.setError("Add en subtract operations mogen alleen uitgevoerd worden met dezelfde type literal.");
            return ExpressionType.UNDEFINED;
        }

        return left;
    }

    /**
     * Controleert of een expressie van het juiste type is.
     *
     * @param expression De expressie die moet worden gecontroleerd.
     * @return Het type van de expressie na de controle.
     */
    private ExpressionType checkExpressionType(Expression expression) {
        if (expression instanceof VariableReference) {
            return checkVariableReference((VariableReference) expression);
        } else if (expression instanceof PercentageLiteral) {
            return ExpressionType.PERCENTAGE;
        } else if (expression instanceof PixelLiteral) {
            return ExpressionType.PIXEL;
        } else if (expression instanceof ColorLiteral) {
            return ExpressionType.COLOR;
        } else if (expression instanceof ScalarLiteral) {
            return ExpressionType.SCALAR;
        } else if (expression instanceof BoolLiteral) {
            return ExpressionType.BOOL;
        }

        return ExpressionType.UNDEFINED;
    }

    /**
     * Controleert of een variabele-referentie geldig is.
     *
     * @param variableReference De variabele-referentie die moet worden gecontroleerd.
     * @return Het type van de variabele-referentie na de controle.
     */
    private ExpressionType checkVariableReference(VariableReference variableReference) {
        ExpressionType expressionType = getVariableType(variableReference.name);
        if (expressionType == null) {
            variableReference.setError("Variabele is nog niet gedeclareerd of is niet in dezelfde scope.");
            return ExpressionType.UNDEFINED;
        }

        return expressionType;
    }

    /**
     * Slaat het type van een variabele op in de huidige scope.
     *
     * @param name De naam van de variabele.
     * @param type Het type van de variabele.
     */
    private void putVariableType(String name, ExpressionType type) {
        variableTypes.getFirst().put(name, type);
    }

    /**
     * Haalt het type van een variabele op.
     *
     * @param name De naam van de variabele.
     * @return Het type van de variabele of null als deze niet is gedefinieerd.
     */
    private ExpressionType getVariableType(String name) {
        for (HashMap<String, ExpressionType> scope : variableTypes) {
            ExpressionType type = scope.get(name);
            if (type != null) {
                return type;
            }
        }

        return null;
    }

    /**
     * Controleert of het type van een variabele is gewijzigd.
     *
     * @param currentType Het huidige type van de variabele.
     * @param previousType Het vorige type van de variabele.
     * @return true als het type is gewijzigd, anders false.
     */
    private boolean variableTypeChanged(ExpressionType currentType, ExpressionType previousType) {
        return (previousType != null) && currentType != previousType;
    }
}
