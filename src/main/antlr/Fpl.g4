grammar Fpl;

/*
 *  Parser
 */

// not-fixing issue, thus no way to turn this warning off :'(
file
    : block EOF
    ;

block
    : (statements+=statement)*
    ;

blockWithBraces
    : '{' block '}'
    ;

statement
    : function
    | variable
    | expression
    | whileStatement
    | ifStatement
    | assignment
    | returnStatement
    ;

// function

function
    : Fun name=Identifier '(' parameterList ')' blockWithBraces
    ;

parameterList
    : names+=Identifier (',' names+=Identifier)*
    |
    ;

// variable

variable
    : Var name=Identifier ('=' expression)?
    ;

// expression

expression
    : name=Identifier '(' argumentsList ')'             # FcallExpr
    | lhs=expression op=(Add | Sub) rhs=expression      # AddExpr
    | lhs=expression op=(Mul | Div | Mod) rhs=expression      # MulExpr
    | lhs=expression op=(G | Ge | L | Le | Eq | Neq) rhs=expression # CompareExpr
    | lhs=expression op=(Or | And) rhs=expression          # LogicalExpr
    | op=('+' | '-') expression                                    # UnaryExpr
    | Identifier                                        # IdentifierExpr
    | Int                                            # IntExpr
    | Double                                        # DoubleExpr
    | '(' expression ')'                                # ExprInExpr
    ;

argumentsList
    : arguments+=expression (',' arguments+=expression)*
    |
    ;

// while

whileStatement
    : While '(' expression ')' blockWithBraces
    ;

// if

ifStatement
    : If '(' expression ')' thenBlock=blockWithBraces (Else elseBlock=blockWithBraces)?
    ;

// assigment

assignment
    : varName=Identifier '=' expression
    ;

// return

returnStatement
    : Return expression
    ;

number
    : Int
    | Double
    ;


/*
 *  Lexer
 */

While
    : 'while'
    ;

If
    : 'if'
    ;

Else
    : 'else'
    ;

Return
    : 'return'
    ;

Fun
    : 'fun'
    ;

Var
    : 'var'
    ;

fragment WordChar
    : ('a'..'z' | 'A'..'Z' | '_')
    ;

fragment Digit
    : ('0'..'9')
    ;

Int
    : ('1'..'9') Digit*
    | '0'
    ;

Double
    : Int ('.' Digit*)
    ;

Add : '+';
Sub : '-';
Mul : '*';
Div : '/';
Mod : '%';
G   : '>';
Ge  : '>=';
L   : '<';
Le  : '<=';
Eq  : '==';
Neq : '!=';
Or  : '||';
And : '&&';

Identifier
    : WordChar (Int | WordChar)*
    ;

LineComment
    : '//' ~[\r\n]*
      -> skip
    ;

Whitespace
    : (' ' | '\t' | '\r'| '\n')
      -> skip
    ;