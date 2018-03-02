# Syntax Analisis: Parsing

Here I built two **Recursive Descent Parser**.

The first one in **Parser.java** recognizes very simple arithmetic operation of non negative integers.

The Grammar used is the following:
```
START    ::=    EXPR eof

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

The construction of FIRST and FOLLOW sets is not written here but it is very easy to make. 

The related *parsing table* can confirm that this is an **LL(1)** Grammar so we can proceed with a recursive descent algo.

|          |  +  |  -  |  *  |  /  |  (  |  )  |  num  |  eof  |
| ---------|:---:|:---:|:---:|:---:|:---:|:---:|:-----:|:-----:|
| START    |     |     |     |     | EXPR eof| | EXPR eof|       |
| EXPR     |     |     |     |     | TERM EXPRP|     | TERM EXPRP|       |
| EXPRP    |+ TERM EXPRP|- TERM EXPRP|     | |epsilon     |     |       |epsilon|
| TERM     |     |     |     |     |FACT TERMP|     |FACT TERMP|       |
| TERMP    |epsilon|epsilon|* FACT TERMP|/ FACT TERMP|     |epsilon|       |epsilon|
| FACT     |     |     |     |     |( EXPR )|     |num|       |



---


For the second parser **ParserSimpleProgLang.java** we use a more complex grammar, defined as follow:
``` 
PROG      ::=     STATLIST eof

STAT      ::=     id = EXPR
           |      print ( EXPR )
           |      read  ( id )
           |      if BEXPR then STAT
           |      if BEXPR then STAT else STAT
           |      for ( id = EXPR; BEXPR ) do STAT
           |      begin STATLIST end
          
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

Note that *relop* is, as defined in the Lexer part, an element of {==, <>, <, >, >=, <=}.

With this grammar the if do not use brackets, every instruction must be followed by a semicolon except for the last one, `read` is a function which (ideally) reads something from an external input an put it in the identifier passed as argument (so it cannot be preceed by an assignment operator).

The Parsing table is very long so is not written here, but it is easy to build (and anyway you can find info about it in the comments i put in the code).

Note that, for how the STAT nonterminal is built, this is not an LL(1) grammar and is not even possible to make it left recursive enough to transform it in a true LL(1) grammar.
This means that tecnically we should not use a recursive descent parser ( a bottom-up SLR parser would have no problem with it) but there is an easy solution that consist in changing the grammar in this way:

```$xslt
STAT      ::= ...
           |      if BEXPR then STAT ELSE_STAT
           |  ...
           
ELSE_STAT ::=     else STAT
          ::=     epsilon
```

It is still not LL(1) but if you force the decision to **shift** everytime you can match an *else* it works flawlessly (there are also other solutions to the problem, this is just the one I liked more).

