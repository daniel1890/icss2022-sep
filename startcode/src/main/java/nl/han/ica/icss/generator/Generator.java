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

	/**
	 * Genereert CSS-regels op basis van de AST en retourneert de gegenereerde CSS-tekst.
	 *
	 * @param ast De Abstracte Syntax Tree (AST) om CSS uit te genereren.
	 * @return De gegenereerde CSS als een tekstreeks.
	 */
	public String generate(AST ast) {
		generateNode(ast.root);
		return stringBuilder.toString();
	}

	/**
	 * Genereert CSS-regels voor de gegeven AST-node en zijn kinderen.
	 *
	 * @param astNode De AST-node om CSS-regels voor te genereren.
	 */
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

	/**
	 * Genereert CSS-regels voor een Stylerule en voegt deze toe aan de gegenereerde CSS-tekst.
	 *
	 * @param stylerule De Stylerule om CSS-regels voor te genereren.
	 */
	private void generateStylerule(Stylerule stylerule) {
		generateSelectors(stylerule);
		generateDeclarations(stylerule);

		stringBuilder.append("}\n\n");
	}

	/**
	 * Genereert de selectors voor een Stylerule en voegt ze toe aan de gegenereerde CSS-tekst.
	 *
	 * @param stylerule De Stylerule waarvoor selectors worden gegenereerd.
	 */
	private void generateSelectors(Stylerule stylerule) {
		String selectors = stylerule.selectors.stream()
				.map(ASTNode::toString)
				.collect(Collectors.joining(", "));

		stringBuilder.append(selectors).append(" {\n");
	}

	private void generateDeclarations(Stylerule stylerule) {
		for (ASTNode node : stylerule.getChildren()) {
			if (node instanceof Declaration) {
				generateDeclaration((Declaration) node);
			}
		}
	}

	/**
	 * Genereert een CSS-declaratie en voegt deze toe aan de gegenereerde CSS-tekst.
	 *
	 * @param declaration De Declaration om een CSS-declaratie voor te genereren.
	 */
	private void generateDeclaration(Declaration declaration) {
		stringBuilder.append("  ")
				.append(declaration.property.name)
				.append(": ")
				.append(expressionToString(declaration.expression))
				.append(";\n");
	}

	/**
	 * Converteert een Expression naar een CSS-stringrepresentatie.
	 *
	 * @param expression De Expression om te converteren.
	 * @return Een CSS-stringrepresentatie van de Expression.
	 */
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
