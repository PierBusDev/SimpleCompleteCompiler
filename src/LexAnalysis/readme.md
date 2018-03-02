# Lexical Analysis

Here I implemented a lexical analyzer capable of recognizing a simple programming language reading a text in input and obtaining a sequence of tokens.
This Lexer will be then used to give the inputs to the next part of the compiler (Parser, CodeGenerator, Translator)

The **Tokens** of the language are described by the following table:

| Token         | Pattern          | Name  |
| ------------- |:----------------:| -----:|
| Number        | numeric constant | 256   |
| Identifier    | letter followed by letters and numbers     |   257 |
| Relop         | relational operator (<, >, <>, >=, <=, ==) |    258 |
| If            | *if*             | 259
| Then          | *then*             | 260
| Else          | *else*             | 261
| For           | *for*             | 262
| Do            | *do*             | 263
| Print         | *print*             | 264
| Read          | *read*             | 265
| Or            | *&#124;&#124;*             | 266
| And           | *&&*             | 267
| Begin         | *begin*             | 268
| End           | *end*             | 269
| Negation      | *!*             | 33
| Round open Bracket           | *(*             | 40
| Round closed Bracket           | *)*             | 41
| Sum           | *+*             | 43
| Sub           | *-*             | 45
| Mult          | *             | 42
| Div           | */*             | 47
| Semicolon     | *;*             | 59
| Assignment    | *=*             | 61
| EOF           | End of Input             | -1

**Identifiers** are defined by regexp `[a-zA-Z][a-zA-Z0-9]*`

**Numbers** are defined by regexp `0|[1-9][0-9]*`

The Lexer *must* ignore every char recognized as a a space ( so even tabulations and carriage returns ) but has to signal as errors any character which is not part of the grammar of its language (eg '#' or '@').

The **output** must be in the form of `<token0>, <token1>, etc...`

For example with an input of `if x>y then x=0` the output must be `<259, if>, <257, x> <258, > >, <257, y>, <260, then>, <257, x>, <61>, <256, 0>, <-1>`

Note that the lexer, by definition, must not concern itself with the validation of the language structure, so it will accept also nonsense command if they have valid lexemes.
It should anyway signal any invalid sequence of symbols (eg `&&&`).


## Lexers Built

- **Lexer.java**

    this is the simplest using just the definitions wrote above.
    
- **LexerWithAdvancedIdentifiers.java**

    here the definition of **identifiers** is more complex: `([a-zA-Z]|(_(_)*[a-zA-Z0-9]))([a-zA-Z0-9]|_)*`.
    The solution uses one of the DFA we already built
    
- **LexerWithAdvancedIdentifiersAndComments.java** 

    here the Lexer must accept also comments in the two common forms (multiline comment)`/**/` and (line comment)`//` but ignore them (do not return any token!).
    The solution uses one of the DFA we already built (for the multiline comment type)
    
    Note that inside a comment there can be any symbol, even those which are not part of the grammar of the language, the Lexer must just ignore them without errors.
    
    If a multiline comment is not closed before EOF the lexer must signal it with a clear error.
    
    If the sequence `*/` is found outside a comment the Lexer must translate it as the mult token followed by the divide one (again the Lexer has no clue about the correct structure of a program in input, this is a job for the Parser)