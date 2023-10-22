# Geïmplementeerde eisen en uitbreiding Compileropdracht

## Eisen

| Code | Omschrijving | Geïmplementeerd |
|------|--------------|-------------|
| AL01 | De code behoudt de packagestructuur van de aangeleverde startcode. Toegevoegde code bevindt zich in de relevante packages.             |  &check;           |
| AL02 | Alle code compileert en is te bouwen met Maven 3.6 of hoger, onder OpenJDK 13. Tip: controleer dit door eerst mvn clean uit te voeren alvorens te compileren en in te leveren, hierop een onvoldoende halen is echt zonde. Gebruik van Oracle versies van Java is uitdrukkelijk niet toegestaan.             |  &check;           |
| AL03 | De code is goed geformatteerd, zo nodig voorzien van commentaar, correcte variabelenamen gebruikt, bevat geen onnodig ingewikkelde constructies en is zo onderhoudbaar mogelijk opgesteld. (naar oordeel van docent)             |             |
| AL04 | De docent heeft vastgesteld (tijdens les, assessment of op een andere manier) dat de compiler eigen werk is en dat je voldoet aan de beoordelingscriteria van APP-6, te weten: - Kent de standaardarchitectuur van compilers; - Kent de basisbegrippen over programmeertalen (zoals syntaxis, semantiek).             |             |
_tabel 1: algemene eisen_
<br />
<br />

| Code | Omschrijving | Geïmplementeerd |
|------|--------------|-----------------|
| PA00 | De parser dient zinvol gebruik te maken van jouw eigen implementatie van een stack generic voor ASTNode (VT: zie huiswerk IHANStack<ASTNode>)             | &check;                |
| PA01 | Implementeer een grammatica plus listener die AST’s kan maken voor ICSS documenten die “eenvoudige opmaak” kan parseren, zoals beschreven in de taalbeschrijving. In level0.icss vind je een voorbeeld van ICSS code die je moet kunnen parseren. testParseLevel0() slaagt.             | &check;                |
| PA02 | Breid je grammatica en listener uit zodat nu ook assignments van variabelen en het gebruik ervan geparseerd kunnen worden. In level1.icss vind je voorbeeldcode die je nu zou moeten kunnen parseren. testParseLevel1() slaagt.	             | &check;                |
| PA03 | Breid je grammatica en listener uit zodat je nu ook optellen en aftrekken en vermenigvuldigen kunt parseren. In level2.icss vind je voorbeeld- code die je nu ook zou moeten kunnen parseren. Houd hierbij rekening met de rekenregels (vermenigvuldigen gaat voor optellen en aftrekken, optellen en aftrekken gaan van links naar rechts; zie ook deze site.”testParseLevel2() slaagt.	             | &check;                |
| PA04 | Breid je grammatica en listener uit zodat je if/else-statements aankunt. In level3.icss vind je voorbeeldcode die je nu ook zou moeten kunnen parseren. testParseLevel3() slaagt.	             | &check;                |
| PA05 | PA01 t/m PA04 leveren minimaal 30 punten op	             | &check;                |
_tabel 2: eisen parser_
<br />
<br />


| Code | Omschrijving | Geïmplementeerd |
|------|--------------|-----------------|
| CH00 | Minimaal vier van onderstaande checks moeten zijn geïmplementeerd	             |  &check;                |
| CH01 | Controleer of er geen variabelen worden gebruikt die niet gedefinieerd zijn.	             | &check;                 |
| CH02 | Controleer of de operanden van de operaties plus en min van gelijk type zijn. Je mag geen pixels bij percentages optellen bijvoorbeeld. Controleer dat bij vermenigvuldigen minimaal een operand een scalaire waarde is. Zo mag 20% * 3 en 4 * 5 wel, maar mag 2px * 3px niet.	             |  &check;                |
| CH03 | Controleer of er geen kleuren worden gebruikt in operaties (plus, min en keer).	             | &check;                 |
| CH04 | Controleer of bij declaraties het type van de value klopt met de property. Declaraties zoals width: #ff0000 of color: 12px zijn natuurlijk onzin.	             | &check;                 |
| CH05 | Controleer of de conditie bij een if-statement van het type boolean is (zowel bij een variabele-referentie als een boolean literal)	             | &check;                 |
| CH06 | Controleer of variabelen enkel binnen hun scope gebruikt worden	             | &check;                 |
_tabel 3: eisen checker_
<br />
<br />


| Code | Omschrijving | Geïmplementeerd |
|------|--------------|-----------------|
| TR01 | Evalueer expressies. Schrijf een transformatie in Evaluator die alle Expression knopen in de AST door een Literal knoop met de berekende waarde vervangt.	             |                 |
| TR02 | Evalueer if/else expressies. Schrijf een transformatie in Evaluator die alle IfClauses uit de AST verwijdert. Wanneer de conditie van de IfClause TRUE is wordt deze vervangen door de body van het if-statement. Als de conditie FALSE is dan vervang je de IfClause door de body van de ElseClause. Als er geen ElseClause is bij een negatieve conditie dan verwijder je de IfClause volledig uit de AST.	             |                 |
_tabel 4: eisen transformer_
<br />
<br />


| Code | Omschrijving | Geïmplementeerd |
|------|--------------|-----------------|
| GE01 | Implementeer de generator in nl.han.ica.icss.generator.Generator die de AST naar een CSS2-compliant string omzet.	             |                 |
| GE02 | Zorg dat de CSS met twee spaties inspringing per scopeniveau gegenereerd wordt.	             |                 |
_tabel 5: eisen generator_

## Eigen uitbreidingen

| Code | Omschrijving                              | Geïmplementeerd |
|------|-------------------------------------------|-----------------|
| EU01 | Voeg naast multiplicatie ook divisie toe	 |                 |
| EU02 | 	                                         |                 |
_tabel 6: eigen uitbreidingen compiler_