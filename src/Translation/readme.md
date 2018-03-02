# Syntax Direct Translation

This is based on the **Parser.java** created previously and used to **evaluate** the simple math expression of its grammar using the following **SDT**:

NOTE: the numbers near EXPRP and TERMP are used just to make a distinction between the occurrence of the nonterm in the head of the production and the one in the body.
```
START    ::=    EXPR eof { print(EXPR.val) }

EXPR     ::=    TERM {expr.i = term.val} EXPRP {EXPR.val = EXPRP.val}

EXPRP    ::=    + TERM {EXPRP1.i = EXPRP.i + TERM.val} EXPRP1 {EXPRP.val = EXPRP1.val}
          |     - TERM {EXPRP1.i = EXPRP.i - TERM.val} EXPRP1 {EXPRP.val = EXPRP1.val}
          |     epsilon {EXPRP.val = EXPRP.i}
       
TERM     ::=    FACT {TERMP.i = FACT.val} TERMP {TERM.val = TERMP.val}

TERMP    ::=    * FACT {TERMP1.i = TERMP.i * FACT.val} TERMP1 {TERMP.val = TERMP1.val }
          |     / FACT {TERMP1..i = TERMP.i / FACT.val} TERMP1 {TERMP.val = TERMP1.val }
          |     epsilon {TERMP.val = TERMP.i }
          
FACT     ::=    ( EXPR ) {FACT.val = EXPR.val}
          |     number  {FACT.val = number.value}
``` 

Note also that the attribute *value* of *number* is the numeric value of the terminal given by the lexical analyzer.