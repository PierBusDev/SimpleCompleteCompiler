# SLR Parsing 
Here I implemented a simple but efficient bottom-up parser.

The Grammar used is the following one is that already used in the other parts for recognizing simple arithmetic operations BUT it is more intuitive because I have not removed the left recursion (they are not a problem for an SLR parser):
```$xslt
note that the following production are numbered to be easily identificable 
in case of a reduce move

1) EXPR     ::=      EXPR + TERM
2)           |       EXPR - TERM
3)           |       TERM
          
4) TERM     ::=      TERM * FACT
5)           |       TERM / FACT\
6)           |       FACT
          
7) FACT     ::=      ( EXPR )
8)           |       num
```

The deterministic **LR(0)** automata is not draw here because of its dimensions but it is very easy to build and should have exactly 15 states.

The related SLR parsing table is the following (note that you can have different states number based on in which order you create your LR(0) automata):

| STATES |  num  |  (  |  )  |  eof  |  +  |  -  |  *  |  /  |  E  |  T  |  F  | 
|--------|-------|-----|-----|-------|-----|-----|-----|-----|-----|-----|-----|
|    0   |   S5  |  S4 |     |       |     |     |     |     |  1  |  2  |  3  |
|    1   |       |     |     | accept|  S6 |  S7 |     |     |     |     |     |
|    2   |       |     |  R3 |  R3   |  R3 |  R3 |  S10| S11 |     |     |     |
|    3   |       |     |  R6 |  R6   |  R6 |  R6 |  R6 |  R6 |     |     |     |
|    4   |   S5  | S4  |     |       |     |     |     |     |  14 |  2  |  3  |
|    5   |       |     |  R8 |  R8   |  R8 |  R8 |  R8 |  R8 |     |     |     |
|    6   |   S5  | S4  |     |       |     |     |     |     |     |  8  |  3  |
|    7   |   S5  | S4  |     |       |     |     |     |     |     |  9  |  3  |
|    8   |       |     |  R1 |  R1   |  R1 |  R1 | S10 | S11 |     |     |     |
|    9   |       |     |  R2 |  R2   |  R2 |  R2 | S10 | S11 |     |     |     |
|   10   |   S5  | S4  |     |       |     |     |     |     |     |     | 13  |
|   11   |   S5  | S4  |     |       |     |     |     |     |     |     | 12  |
|   12   |       |     |  R5 |  R5   |  R5 |  R5 |  R5 |  R5 |     |     |     |
|   13   |       |     |  R4 |  R4   |  R4 |  R4 |  R4 |  R4 |     |     |     |
|   14   |       |     | S15 |       |  S6 |  S7 |     |     |     |     |     |
|   16   |       |     |  R7 |  R7   |  R7 |  R7 |  R7 |  R7 |     |     |     |


Legend: 
- **S***number* => shift to the state of the LR(0) automata identified by that *number*
- **R***number* => reduce move based on the production numbered *number*
- *number* for NONTERM => goto move


Note that this table shows us that this grammar has not conflicts (nor Reduce/Reduce neither Shift/Reduce)
