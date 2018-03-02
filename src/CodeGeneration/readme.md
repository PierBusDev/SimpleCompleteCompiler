# Code Generation

In this part I built a code generator which as output produces an assembly like code that can eventually be converted in a .class file using [Jasmin](http://jasmin.sourceforge.net/).

Basically my program will produce a *.j* file in a pseudo assembly language that is then given to the jasmin assembler via:
```$xslt
java -jar jasmin.jar filename.j
``` 

It will produce a file called `filename.class` that can then by runned in the JVM via `java filename.class`.


The first Code Generator called **ExpressionTranslator.java** works on the following grammar:
```
PROG     ::=    print( EXPR ) eof

EXPR     ::=    TERM EXPRP

EXPRP    ::=    + TERM EXPRP
          |     - TERM EXPRP
          |     epsilon 
       
TERM     ::=    FACT TERMP

TERMP    ::=    * FACT TERMP
          |     / FACT TERMP
          |     epsilon
          
FACT     ::=    ( EXPR )
          |     number 
```

The second **ProgTranslator.java** uses the following one:
``` 
PROG      ::=     STATLIST eof

STAT      ::=     id = EXPR
           |      print ( EXPR )
           |      read  ( id )
           |      if BEXPR then STAT ELSE_STAT
           |      for ( id = EXPR; BEXPR ) do STAT
           |      begin STATLIST end
           
ELSE_STAT ::=     else STAT
           |      epsilon
          
STATLIST  ::=     STAT STATLISTP

STATLISTP ::=     ; STAT STATLISTP
           |      epsilon
           
BEXPR     ::=     EXPR relop EXPR

EXPR      ::=     TERM EXPRP

EXPRP     ::=     + TERM EXPRP
           |      - TERM EXPRP
           |      epsilon
           
TERM      ::=     FACT TERMP

TERMP     ::=     * FACT TERMP
           |      / FACT TERMP
           |      epsilon
           
FACT      ::=     ( EXPR )
           |      num
           |      id

```

Note that both of them will use **Parsers** already created in previous sections