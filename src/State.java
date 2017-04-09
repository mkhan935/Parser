public enum State
{
// final states    ordinal number  token accepted

    Add,             // 0          +
    Sub,             // 1          -
    Mul,             // 2          *
    Div,	         // 3          /
    Or,              // 4          ||
    And,             // 5          &&
    Inv,             // 6          !
    Lt,              // 7          <
    Le,              // 8          <=
    Gt,              // 9          >
    Ge,              // 10         >=
    Eq,              // 11         ==
    Neq,             // 12         !=
    Assign,          // 13         =
    Id,              // 14         identifiers
    Int,             // 15         integers
    Float,           // 16         floats without exponentiation part
    FloatE,          // 17         floats with exponentiation part
    LParen,          // 18         (
    RParen,          // 19         )
    LBrace,          // 20         {
    RBrace,          // 21         }
    Colon,           // 22         :
    Semicolon,       // 23         ;

// non-final states                string recognized

    Start,           // 24      the empty string
    Bar,             // 25         |
    Ampersand,       // 26         &
    Period,          // 27        "."
    E,               // 28      float parts ending with E or e
    EPlusMinus,      // 29      float parts ending with + or - in exponentiation part

// keyword states

    Keyword_if,
    Keyword_else,
    Keyword_switch,
    Keyword_case,
    Keyword_default,
    Keyword_while,
    Keyword_do,
    Keyword_for,
    Keyword_print,
    Keyword_false,
    Keyword_true,

    UNDEF;

    boolean isFinal()
    {
        return ( this.compareTo(State.Semicolon) <= 0 );
    }

    boolean isRelationalOp()
    {
        return this.compareTo(Lt) >= 0 && this.compareTo(Neq) <= 0;
    }
}

// By enumerating the final states first and then the non-final states,
// test for a final state can be done by testing if the state's ordinal number
// is less than or equal to that of Semicolon.