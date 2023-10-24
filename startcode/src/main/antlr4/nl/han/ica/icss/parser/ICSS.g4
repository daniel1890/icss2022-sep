grammar ICSS;

//--- LEXER: ---

// IF support:
IF: 'if';
ELSE: 'else';
BOX_BRACKET_OPEN: '[';
BOX_BRACKET_CLOSE: ']';


//Literals
TRUE: 'TRUE';
FALSE: 'FALSE';
PIXELSIZE: [0-9]+ 'px';
PERCENTAGE: [0-9]+ '%';
SCALAR: [0-9]+;


//Color value takes precedence over id idents
COLOR: '#' [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f];

//Specific identifiers for id's and css classes
ID_IDENT: '#' [a-z0-9\-]+;
CLASS_IDENT: '.' [a-z0-9\-]+;

//General identifiers
LOWER_IDENT: [a-z] [a-z0-9\-]*;
CAPITAL_IDENT: [A-Z] [A-Za-z0-9_]*;

//All whitespace is skipped
WS: [ \t\r\n]+ -> skip;

//
OPEN_BRACE: '{';
CLOSE_BRACE: '}';
SEMICOLON: ';';
COLON: ':';
PLUS: '+';
MIN: '-';
MUL: '*';
DIV: '/'; // EIGEN UITBREIDING
ASSIGNMENT_OPERATOR: ':=';




//--- PARSER: ---
// stylesheet: Matcht een volledige CSS-stylesheet en stelt het in staat variabele toewijzingen, stijlregels en declaraties te bevatten.
stylesheet: variableAssignment* styleRule* EOF;

propertyName: LOWER_IDENT;

// styleRule: Vertegenwoordigt een CSS-stijlregel met een selector en een reeks declaraties zoals in ".button { color: #ff0000; }".
styleRule: selector OPEN_BRACE ruleBody CLOSE_BRACE;

// ruleBody: Hier zijn declaraties omvat die samen een stijlregel vormen, bijvoorbeeld "{ color: #ff0000; font-size: 14px; }".
ruleBody: (declaration | ifClause | variableAssignment)*;

// classSelector, idSelector, tagSelector: Definieert verschillende soorten CSS-selectoren die gebruikt kunnen worden om elementen te selecteren, zoals ".button", "#header", "p".
classSelector: CLASS_IDENT;
idSelector: ID_IDENT;
tagSelector: LOWER_IDENT;

// selector: Hiermee specificeren we de selectors voor stijlregels, zoals "p", ".button", of "#header".
selector: classSelector | idSelector | tagSelector;

// declaration: Stelt een CSS-declaratie voor met een eigenschap en een waarde, bijvoorbeeld "color: #ff0000;".
declaration: propertyName COLON expression SEMICOLON;

// variableReference en variableAssignment: Hiermee kunnen variabelen gedefinieerd worden en bepaalde waardes toegewezen worden, zoals "LinkColor := #ff0000;".
variableReference: CAPITAL_IDENT;
variableAssignment: variableReference ASSIGNMENT_OPERATOR expression+ SEMICOLON;

// literal: Definieert mogelijke letterlijke waarden zoals kleuren, grootten, scalars, booleaanse waarden en variabelen, bijvoorbeeld "TRUE", "#ff0000", "500px".
boolLiteral: TRUE | FALSE;
colorLiteral: COLOR;
percentageLiteral: PERCENTAGE;
pixelLiteral: PIXELSIZE;
scalarLiteral: SCALAR;
literal: boolLiteral | colorLiteral | percentageLiteral | pixelLiteral | scalarLiteral | variableReference;

// expression: Definieert expressies en de volgorde van evaluatie, bijvoorbeeld "5 + 3 * (LinkColor + 2px)".
expression: literal | expression (MUL | DIV) expression | expression (PLUS | MIN) expression;

// ifClause: Hiermee worden voorwaardelijke expressies met een IF-voorwaarde en optionele ELSE-tak opgesteld, zoals "if [UseLinkColor] { color: LinkColor; } else { color: #000000; }".
ifClause: IF BOX_BRACKET_OPEN (variableReference | boolLiteral) BOX_BRACKET_CLOSE OPEN_BRACE ruleBody CLOSE_BRACE elseClause?;
elseClause: ELSE OPEN_BRACE ruleBody CLOSE_BRACE;
