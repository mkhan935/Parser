/**

 This class is a top-down, recursive-descent parser for the following syntactic categories:

 <statement> --> <assignment> | <cond> | <switch> | <while loop> | <do loop> | <for loop> | <print> | <block>
 <assignment> --> <id> "=" <expr> ";"
 <cond> --> "if" "(" <expr> ")" <statement> [ "else" <statement> ]
 <switch> --> "switch" "(" <expr> ")" "{" <case list> "}"
 <case list> --> { <case> }+
 <case> --> "case" <label> ":" <s list> | "default" ":" <s list>
 <label> --> <int>
 <while loop> --> "while" "(" <expr> ")" <statement>
 <do loop> --> "do" <statement> "while" "(" <expr> ")" ";"
 <for loop> --> "for" "(" <assign> ";" <expr> ";" <assign> ")" <statement>
 <assign> --> <id> "=" <expr>
 <print> --> "print" <expr> ";"
 <block> --> "{" <s list> "}"
 <s list> --> { <statement> }
 <expr> --> <boolTerm> { "||" <boolTerm> }
 <boolTerm> --> <boolPrimary> { "&&" <boolPrimary> }
 <boolPrimary> --> <E> [ <rel op> <E> ]
 <rel op> --> "<" | "<=" | ">" | ">=" | "==" | "!="
 <E> --> <term> { (+|-) <term> }
 <term> --> <primary> { (*|/) <primary> }
 <primary> --> <id> | <int> | <float> | <floatE> | <boolLiteral> | "(" <expr> ")" | - <primary> | ! <primary>
 <boolLiteral> --> "false" | "true"

 NOTE: In the 2-branch conditionals, each "else" matches the closest preceding unmatched "if".
 NOTE: The binary operators +, -, *, /, ||, && associate to left.

 The definitions of the tokens are given in the lexical analyzer class file "LexAnalyzer.java".

 The following variables and functions of the "LexAnalyzer" class are used:

 static String t // holds an extracted token
 static State state // the current state of the finite automaton
 static int getToken() // extracts the next token
 static void display(String s)
 static void displayln(String s)
 static void setIO(String inFile, String outFile)
 static void closeIO()

 The program will display the parse tree in linearly indented form.
 Each syntactic category name labeling a node is displayed on a separate line,
 prefixed with the integer i representing the node's depth and indented by i blanks.
 The string variable "indent" will keep track of the correct number of blanks for indentation and
 will be passed to parse functions corresponding to syntactic categories.

 **/

import java.util.*;

public abstract class Parser extends LexAnalyzer
{
    static boolean syntaxErrorFound = false;


    public static Statement statement()

    // <statement> --> <assignment> | <cond> | <switch> | <while loop> | <do lpop> | <for loop> | <print> | <block>

    {
        switch ( state )
        {
            case Id:             return assignment();
            case Keyword_if:     return cond();
            case Keyword_switch: return switchStatement();
            case Keyword_while:  return whileLoop();
            case Keyword_do:     return doLoop();
            case Keyword_for:    return forLoop();
            case Keyword_print:  return print();
            case LBrace:         return block();
            default:
                errorMsg(6);
                return null;
        }
    }

    public static Assignment assignment()

    // <assignment> --> <id> "=" <expr> ";"

    {
        String id = t;
        getToken();

        if ( state == State.Assign )
        {
            getToken();
            Expr expr = expr();
            if ( state == State.Semicolon )
            {
                getToken();
                return new Assignment(id, expr);
            }
            else
                errorMsg(4);
        }
        else
            errorMsg(5);
        return null;
    }

    public static Cond cond()

    // <cond> --> "if" "(" <expr> ")" <statement> [ "else" <statement> ]

    {
        getToken(); // flush "if"
        if ( state == State.LParen )
        {
            getToken();
            Expr expr = expr();
            if ( state == State.RParen )
            {
                getToken();
                Statement statement1 = statement();
                if ( state == State.Keyword_else )
                {
                    getToken();
                    Statement statement2 = statement();
                    return new If2(expr, statement1, statement2);
                }
                else
                    return new If1(expr, statement1);
            }
            else
                errorMsg(7);
        }
        else
            errorMsg(8);
        return null;
    }

    public static Switch switchStatement()

    // <switch> --> "switch" "(" <expr> ")" "{" <case list> "}"

    {
        getToken(); // flush "switch"
        if ( state == State.LParen )
        {
            getToken();
            Expr expr = expr();
            if ( state == State.RParen )
            {
                getToken();
                if ( state == State.LBrace )
                {
                    getToken();
                    CaseList caseList = caseList();
                    if ( state == State.RBrace )
                    {
                        getToken();
                        return new Switch(expr, caseList);
                    }
                    else
                        errorMsg(3);
                }
                else
                    errorMsg(11);
            }
            else
                errorMsg(7);
        }
        else
            errorMsg(8);
        return null;
    }

    public static CaseList caseList()

    // <case list> --> { <case> }+

    {
        LinkedList<Case> caseList = new LinkedList<Case>();

        while ( state == State.Keyword_case || state == State.Keyword_default )
        {
            Case case_ = case_();
            caseList.add(case_);
        }
        return new CaseList(caseList);
    }

    public static Case case_()

    // <case> --> "case" <label> ":" <s list> | "default" ":" <s list>

    {
        if ( state == State.Keyword_case )
        {
            getToken();
            Label label = label();
            if ( state == State.Colon )
            {
                getToken();
                SList sList = sList();
                return new LabeledCase(label, sList);
            }
            else
                errorMsg(9);
        }
        else // state == State.Keyword_default
        {
            getToken();
            if ( state == State.Colon )
            {
                getToken();
                SList sList = sList();
                return new DefaultCase(sList);
            }
            else
                errorMsg(9);
        }
        return null;
    }

    public static Label label()

    // <label> --> <int>

    {
        if ( state == State.Int )
        {
            Label label = new Label(Integer.parseInt(t));
            getToken();
            return label;
        }
        else
            errorMsg(10);
        return null;
    }

    public static WhileLoop whileLoop()

    // <while loop> --> "while" "(" <expr> ")" <statement>

    {
        getToken(); // flush "while"
        if ( state == State.LParen )
        {
            getToken();
            Expr expr = expr();
            if ( state == State.RParen )
            {
                getToken();
                Statement statement = statement();
                return new WhileLoop(expr, statement);
            }
            else
                errorMsg(7);
        }
        else
            errorMsg(8);
        return null;
    }

    public static DoLoop doLoop()

    // <do loop> --> "do" <statement> "while" "(" <expr> ")" ";"

    {
        getToken(); // flush "do"
        Statement statement = statement();
        if ( state == State.Keyword_while )
        {
            getToken();
            if ( state == State.LParen )
            {
                getToken();
                Expr expr = expr();
                if ( state == State.RParen )
                {
                    getToken();
                    if ( state == State.Semicolon )
                    {
                        getToken();
                        return new DoLoop(statement, expr);
                    }
                    else
                        errorMsg(4);
                }
                else
                    errorMsg(7);
            }
            else
                errorMsg(8);
        }
        else
            errorMsg(12);
        return null;
    }

    public static ForLoop forLoop()

    // <for loop> --> "for" "(" <assign> ";" <expr> ";" <assign> ")" <statement>

    {
        getToken(); // flush "for"
        if ( state == State.LParen )
        {
            getToken();
            Assign assign1 = assign();
            if ( state == State.Semicolon )
            {
                getToken();
                Expr expr = expr();
                if ( state == State.Semicolon )
                {
                    getToken();
                    Assign assign2 = assign();
                    if ( state == State.RParen )
                    {
                        getToken();
                        Statement statement = statement();
                        return new ForLoop(assign1, expr, assign2, statement);
                    }
                    else
                        errorMsg(7);
                }
                else
                    errorMsg(4);
            }
            else
                errorMsg(4);
        }
        else
            errorMsg(8);
        return null;
    }

    public static Assign assign()

    // <assign> --> <id> "=" <expr>

    {
        String id = t;
        getToken();

        if ( state == State.Assign )
        {
            getToken();
            Expr expr = expr();
            return new Assign(id, expr);
        }
        else
            errorMsg(5);
        return null;
    }

    public static Print print()

    // <print> --> "print" <expr> ";"

    {
        getToken(); // flush "print"
        Expr expr = expr();
        if ( state == State.Semicolon )
        {
            getToken();
            return new Print(expr);
        }
        else
            errorMsg(4);
        return null;
    }

    public static Block block()

    // <block> --> "{" <s list> "}"

    {
        getToken(); // flush "{"
        SList sList = sList();
        if ( state == State.RBrace )
        {
            getToken();
            return new Block(sList);
        }
        else
            errorMsg(3);
        return null;
    }

    public static SList sList()

    // <s list> --> { <statement> }

    {
        LinkedList<Statement> sList = new LinkedList<Statement>();

        while ( beginsStatement() )
        {
            Statement statement = statement();
            sList.add(statement);
        }
        return new SList(sList);
    }

    static boolean beginsStatement()
    {
        return
                state == State.Id || state == State.Keyword_if || state == State.Keyword_switch ||
                        state == State.Keyword_while || state == State.Keyword_do || state == State.Keyword_for ||
                        state == State.Keyword_print || state == State.LBrace
                ;
    }

    public static Expr expr()

    // <expr> --> <boolTerm> { "||" <boolTerm> }

    {
        LinkedList<BoolTermItem> boolTermItemList = new LinkedList<BoolTermItem>();

        BoolTerm boolTerm = boolTerm();
        boolTermItemList.add(new SingleBoolTermItem(boolTerm));
        while ( state == State.Or )
        {
            getToken();
            boolTerm = boolTerm();
            boolTermItemList.add(new OrBoolTermItem(boolTerm));
        }
        return new Expr(boolTermItemList);
    }

    public static BoolTerm boolTerm()

    // <boolTerm> --> <boolPrimary> { "&&" <boolPrimary> }

    {
        LinkedList<BoolPrimaryItem> boolPrimaryItemList = new LinkedList<BoolPrimaryItem>();

        BoolPrimary boolPrimary = boolPrimary();
        boolPrimaryItemList.add(new SingleBoolPrimaryItem(boolPrimary));
        while ( state == State.And )
        {
            getToken();
            boolPrimary = boolPrimary();
            boolPrimaryItemList.add(new AndBoolPrimaryItem(boolPrimary));
        }
        return new BoolTerm(boolPrimaryItemList);
    }

    public static BoolPrimary boolPrimary()

    // <boolPrimary> --> <E> [ <relop> <E> ]
    // <rel op> --> "<" | "<=" | ">" | ">=" | "==" | "!="

    {
        E e1 = E();

        if ( state.isRelationalOp() ) // state = Lt, Le, Gt, Ge, Eq, or Neq
        {
            State relop = state;
            getToken();
            E e2 = E();
            return new RelPrimary(e1, e2, relop);
        }
        else
            return new SingleE(e1);
    }

    public static E E()

    // <E> --> <term> { (+|-) <term> }

    {
        LinkedList<TermItem> termItemList = new LinkedList<TermItem>();

        Term term = term();
        termItemList.add(new SingleTermItem(term));
        while ( state == State.Add | state == State.Sub )
        {
            State op = state;
            getToken();
            term = term();
            if ( op == State.Add )
                termItemList.add(new AddTermItem(term));
            else // op == State.Sub
                termItemList.add(new SubTermItem(term));
        }
        return new E(termItemList);
    }

    public static Term term()

    // <term> --> <primary> { (*|/) <primary> }

    {
        LinkedList<PrimaryItem> primaryItemList = new LinkedList<PrimaryItem>();

        Primary primary = primary();
        primaryItemList.add(new SinglePrimaryItem(primary));
        while ( state == State.Mul | state == State.Div )
        {
            State op = state;
            getToken();
            primary = primary();
            if ( op == State.Mul )
                primaryItemList.add(new MulPrimaryItem(primary));
            else // op == State.Div
                primaryItemList.add(new DivPrimaryItem(primary));
        }
        return new Term(primaryItemList);
    }

    public static Primary primary()

    // <primary> --> <id> | <int> | <float> | <floatE> | <boolLiteral> | "(" <expr> ")" | - <primary> | ! <primary>
    // <boolLiteral> --> "false" | "true"

    {
        switch ( state )
        {
            case Id:

                Id id = new Id(t);
                getToken();
                return id;

            case Int:

                Int intElem = new Int(Integer.parseInt(t));
                getToken();
                return intElem;

            case Float: case FloatE:

            Floatp floatElem = new Floatp(Float.parseFloat(t));
            getToken();
            return floatElem;

            case Keyword_false:

                getToken();
                return new Bool(false);

            case Keyword_true:

                getToken();
                return new Bool(true);

            case LParen:

                getToken();
                Expr expr = expr();
                if ( state == State.RParen )
                {
                    getToken();
                    Parenthesized paren = new Parenthesized(expr);
                    return paren;
                }
                else
                {
                    errorMsg(1);
                    return null;
                }

            case Sub:

                getToken();
                Primary prim = primary();
                NegPrimary negprim = new NegPrimary(prim);
                return negprim;

            case Inv:

                getToken();
                Primary prim_ = primary();
                InvPrimary invprim = new InvPrimary(prim_);
                return invprim;

            default:

                errorMsg(2);
                return null;
        }
    }

    public static void errorMsg(int i)
    {
        syntaxErrorFound = true;

        display(t + " : Syntax Error, unexpected symbol where");

        switch( i )
        {
            case 1:	 displayln(" arith op or ) expected"); return;
            case 2:  displayln(" id, int, float, bool literal, (, -, or ! expected"); return;
            case 3:	 displayln(" } expected"); return;
            case 4:	 displayln(" ; expected"); return;
            case 5:	 displayln(" = expected"); return;
            case 6:	 displayln(" id, if, switch, while, do, for, print, or { expected"); return;
            case 7:	 displayln(" ) expected"); return;
            case 8:  displayln(" ( expected"); return;
            case 9:  displayln(" : expected"); return;
            case 10: displayln(" integer label expected"); return;
            case 11: displayln(" { expected"); return;
            case 12: displayln(" while expected"); return;
            case 13: displayln(" "); return;
            case 14: displayln(" "); return;
        }
    }

    public static void main(String argv[])
    {
        // argv[0]: input file containing a statement
        // argv[1]: output file displaying the parse tree or error messages

        setIO( argv[0], argv[1] );
        setLex();

        getToken();
        Statement statement = statement(); // build a parse tree
        if ( ! t.isEmpty() )
            displayln(t + " : Syntax Error, unexpected symbol");
        else if ( ! syntaxErrorFound )
            statement.printParseTree("");

        closeIO();
    }
}