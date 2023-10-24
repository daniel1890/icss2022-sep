package nl.han.ica.icss.generator;

import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.ColorLiteral;
import nl.han.ica.icss.ast.literals.PercentageLiteral;
import nl.han.ica.icss.ast.literals.PixelLiteral;

import java.util.stream.Collectors;


public class Generator {

	private final StringBuilder stringBuilder;

	public Generator() {
		this.stringBuilder = new StringBuilder();
	}

	public String generate(AST ast) {
		generateNode(ast.root);
		return stringBuilder.toString();
	}

	private void generateNode(ASTNode astNode) {
		for (ASTNode node : astNode.getChildren()) {
			if (node instanceof Stylerule) {
				generateStylerule((Stylerule) node);
			}
		}

		if (stringBuilder.length() > 1) {
			stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());
		}
	}

	private void generateStylerule(Stylerule stylerule) {
		generateSelectors(stylerule);

		for (ASTNode node : stylerule.getChildren()) {
			if (node instanceof Declaration) {
				generateDeclaration((Declaration) node);
			}
		}

		stringBuilder.append("}\n\n");
	}

	private void generateSelectors(Stylerule stylerule) {
		String selectors = stylerule.selectors.stream()
				.map(ASTNode::toString)
				.collect(Collectors.joining(", "));

		stringBuilder.append(selectors).append(" {\n");
	}

	private void generateDeclaration(Declaration declaration) {
		stringBuilder.append("  ")
				.append(declaration.property.name)
				.append(": ")
				.append(expressionToString(declaration.expression))
				.append(";\n");
	}

	private String expressionToString(Expression expression) {
		if (expression instanceof PercentageLiteral) {
			return ((PercentageLiteral) expression).value + "%";
		}
		if (expression instanceof PixelLiteral) {
			return ((PixelLiteral) expression).value + "px";
		}
		if (expression instanceof ColorLiteral) {
			return ((ColorLiteral) expression).value;
		}
		return "";
	}
}
