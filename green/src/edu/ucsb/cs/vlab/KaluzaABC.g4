grammar KaluzaABC ;


kaluzaFile
	: command* EOF ;

command
	: setLogic
	| declareFun
	| assertion
	| checkSat ;

setLogic
	: LPAR SET_LOGIC LOGIC_KIND RPAR ;

checkSat
	: LPAR CHECK_SAT stringVarName RPAR ;

declareFun
	: LPAR DECLARE_FUN stringVarName LPAR RPAR TYPE_STRING RPAR ;

assertion
	: LPAR ASSERT boolExpr RPAR ;

boolExpr
	: notExpr
	| andExpr
	| inExpr
	| strEqualityExpr
	| intEqualityExpr
	| ltExpr
	| gtExpr
	| leqExpr
	| geqExpr ;

stringExpr
	: concatExpr
	| stringLiteral
	| stringVarName ;

intExpr
	: lenExpr
	| intLiteral ;


notExpr
	: LPAR NOT boolExpr RPAR ;

andExpr
	: LPAR AND boolExpr* RPAR ;

inExpr
	: LPAR IN stringExpr regexLiteral RPAR ;

strEqualityExpr
	: LPAR EQ stringExpr stringExpr RPAR ;

intEqualityExpr
	: LPAR EQ intExpr intExpr RPAR ;

ltExpr
	: LPAR LT intExpr intExpr RPAR ;

gtExpr
	: LPAR GT intExpr intExpr RPAR ;

leqExpr
	: LPAR LEQ intExpr intExpr RPAR ;

geqExpr
	: LPAR GEQ intExpr intExpr RPAR ;

concatExpr
	: LPAR CONCAT stringExpr stringExpr RPAR ;

lenExpr
	: LPAR LEN stringExpr RPAR ;

stringVarName
	: ID ;

stringLiteral
	: STRING ;

regexLiteral
	: REGEX ; // PEND: improve this

intLiteral
	: INT ;



LPAR
	: '(' ;

RPAR
	: ')' ;

SET_LOGIC
	: 'set-logic' ;

LOGIC_KIND
	: 'QF_S' ;

DECLARE_FUN
	: 'declare-fun' ;

TYPE_STRING
	: 'String' ;

CHECK_SAT
	: 'check-sat' ;

ASSERT
	: 'assert' ;

NOT
	: 'not' ;

AND
	: 'and' ;

IN
	: 'in' ;

CONCAT
	: 'concat' ;

LEN
	: 'len' ;

EQ
	: '=' ;

LT
	: '<' ;

GT
	: '>' ;

LEQ
	: '<=' ;

GEQ
	: '>=' ;

ID
	: ('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'0'..'9'|'_')* ;

INT
	: '0'..'9'+ ;

WS
	: ( ' '
        | '\t'
        | '\r'
        | '\n'
        ) -> channel(HIDDEN) ;

STRING
	: '"' (EscSeq | ~["\r\n\\])* '"' ;

REGEX
	: '/' (EscSeq | ~[/\r\n\\])* '/' ;

fragment EscSeq
	: Esc ([btnfr"'\\] | . | EOF)
	;

fragment Esc
	: '\\'
	;
