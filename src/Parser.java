
/**
 * Mohammed Khan Project 2 2017 cs 316
 * So I decided to just make one abstract class, Parser.java
 * It's just easier for me as i mainly use Python and in java the inheritance is much more complicated
 * using the following rules I tried my best to define the parser
 * currently I am trying to find a way to implement
 *
 *
 * SWITCH, CASE AND CASE LIST DO NOT WORK.
 *
 *EVERYTHING ELSE SHOULD WORK FINE AS LONG AS IT DOES NOT CONTAIN SWITCH, CASE
 *
 ⟨statement⟩ → ⟨assignment⟩ | ⟨cond⟩ | ⟨switch⟩ | ⟨while loop⟩ | ⟨do loop⟩ | ⟨for loop⟩ | ⟨print⟩ | ⟨block⟩
 ⟨assignment⟩ → ⟨id⟩ "=" ⟨expr⟩ ";"
 ⟨cond⟩ → "if" "(" ⟨expr⟩ ")" ⟨statement⟩ [ "else" ⟨statement⟩ ]
 ⟨switch⟩ → "switch" "(" ⟨expr⟩ ")" "{" ⟨case list⟩ "}"
 ⟨case list⟩ → { ⟨case⟩ }+
 ⟨case⟩ → "case" ⟨label⟩ ":" ⟨s list⟩ | "default" ":" ⟨s list⟩
 ⟨while loop⟩ → "while" "(" ⟨expr⟩ ")" ⟨statement⟩
 ⟨do loop⟩ → "do" ⟨statement⟩ "while" "(" ⟨expr⟩ ")" ";"
 ⟨for loop⟩ → "for" "(" ⟨assign⟩ ";" ⟨expr⟩ ";" ⟨assign⟩ ")" ⟨statement⟩
 ⟨assign⟩ → ⟨id⟩ "=" ⟨expr⟩
 ⟨print⟩ → "print" ⟨expr⟩ ";"
 ⟨block⟩ → "{" ⟨s list⟩ "}"
 ⟨s list⟩ → { ⟨statement⟩ }
 ⟨expr⟩ → ⟨boolTerm⟩ { "||" ⟨boolTerm⟩ }
 ⟨boolTerm⟩ → ⟨boolPrimary⟩ { "&&" ⟨boolPrimary⟩ }
 ⟨boolPrimary⟩ → ⟨E⟩ [ ⟨rel op⟩ ⟨E⟩ ]
 ⟨rel op⟩ → "<" | "<=" | ">" | ">=" | "==" | "!="
 ⟨E⟩ → ⟨term⟩ { (+|−) ⟨term⟩ }
 ⟨term⟩ → ⟨primary⟩ { (*|/) ⟨primary⟩ }
 ⟨primary⟩ → ⟨id⟩ | ⟨int⟩ | ⟨float⟩ | ⟨floatE⟩ | ⟨boolLiteral⟩ | "(" ⟨expr⟩ ")" | − ⟨primary⟩ | ! ⟨primary⟩
 ⟨boolLiteral⟩ → "false" | "true"
 * */
public abstract class Parser extends LexAnalyzer //extending the Lexical Analyzer because we need the states and IO
{

	public static void statement(String s)
	{
		/**<statement> → <assignment> | <increment> | <decrement> |<block> | <cond> | <while> | <do> | <for>|<Print> */


		displayln(s + s.length() + " " +  "<statement>");
		s = s + " ";

		if(state == State.LBrace)
		{
			Block(s);
			return;
		}
		else if(state == State.Keyword_if)
		{
			cond(s);
			return;

		}
		else if(state == State.Keyword_while)
		{
		While(s);
		return;
		}
		else if(state == State.Keyword_for)
		{
			For(s);
			return;
		}
		else if(state == State.Keyword_print)
		{
			Print(s);
			return;
		}
		else if(state == State.Keyword_do)
		{
			Do(s);
			return;
		}
		else if(state == State.Id)
		{
			assignment(s);
			return;
		}

		else
		{
			errorMsg(7);
			return;
		}

	} //end of statement

	public static void assignment(String s)
	{
		/** ⟨assignment⟩ → ⟨id⟩ "=" ⟨expr⟩ ";" */

		displayln(s + s.length() + " <assignment>");
		s = s + " ";

		if(state == State.Id)
		{
			displayln(s + s.length() + " " + t);
			getToken();

			if(state == State.Assign)
			{
				displayln(s + s.length() + " " + t);
				getToken();
				expr(s);
				if(state == State.Semicolon)
				{
					getToken();
					return;
				}
				else
				{
					errorMsg(6);
					return;

				}
			}
			else
			{
				errorMsg(4);
				return;
			}
		}
		else
		{
			errorMsg(2);
			return;
		}
	} //end of assignment


public static void For(String s){
		/** ⟨for loop⟩ → "for" "(" ⟨assign⟩ ";" ⟨expr⟩ ";" ⟨assign⟩ ")" ⟨statement⟩ */
	displayln(s + s.length() + " " + t);
	s = s + " ";
	getToken();
	if(state == State.LParen)
	{
		getToken();
		assignment(s);
		getToken();
		expr(s);
		getToken();
		assignment(s);
		if(state == State.RParen)
		{
			getToken();
			statement(s);
		}
	}


	else {
		errorMsg(7);
	}
} // end of For

public static void Print(String s){
	/** ⟨print⟩ → "print" ⟨expr⟩ ";" */
	displayln(s + s.length() + " " + t);
	s = s + " ";
	if(state == State.Keyword_print) {

		expr(s);
		getToken();
		if(state == State.Semicolon){
			return;
		}
	}
	//primary(s);
} // end of print





public static void While (String s){

		/** ⟨while loop⟩ → "while" "(" ⟨expr⟩ ")" ⟨statement⟩ */

		displayln(s + s.length() + " " + t);
		s = s + " ";
		getToken();
		if(state == State.LParen)
		{
			getToken();
			expr(s);
			if(state == State.RParen)
			{
				getToken();
				statement(s);
				return;
			}
			else
			{
				errorMsg(1);
				return;
			}
		}
		else
		{
			errorMsg(3);
			return;
		}
	}//end of while

		public static void Do(String s){

			/** ⟨do loop⟩ → "do" ⟨statement⟩ "while" "(" ⟨expr⟩ ")" ";" */
			displayln(s + s.length() + " " + t);
			s = s + " ";
			getToken();
			statement(s);

			//getToken();
			if(state == State.Keyword_while){
				displayln(s + s.length() + " " + t);
				getToken();
			if(state == State.LParen)
			{
				getToken();
				expr(s);
				if(state == State.RParen)
				{
					return;
				}
				else
				{
					errorMsg(1);
					return;
				}
			}
			else
			{
				errorMsg(1);
				return;
			}
			}
			else
				return;
		}//end of Do







	public static void SList(String s)
	{
		/** ⟨s list⟩ → { ⟨statement⟩ } */

		displayln(s + s.length() + " <SList>");
		s = s + " ";

		statement(s);

		while(state == State.Keyword_if || state == State.Keyword_do
				|| state == State.Id || state == State.Keyword_while ||
				 state == State.LBrace || state == State.Keyword_for ||
				state == State.Keyword_print)
		{

			displayln(s + s.length() + " <SList>");
			s = s + " ";
			statement(s);
		}
	}//end of SList

	public static void Block(String s)
	{
		/** ⟨block⟩ → "{" ⟨s list⟩ "}" */
		displayln(s + s.length() + " <Block>");
		s = s + " ";

		if(state == State.LBrace)
		{
			getToken();
			SList(s);
			if(state == State.RBrace)
			{
				getToken();
				return;
			}
			else
			{
				errorMsg(5);
				return;
			}
		}

	} // end of block


	public static void cond (String s){
		/** ⟨cond⟩ → "if" "(" ⟨expr⟩ ")" ⟨statement⟩ [ "else" ⟨statement⟩ ] */
		displayln(s + s.length() + " <Cond>" +t);
		s = s + " ";
		getToken();
		if(state == State.LParen)
		{
			getToken();
			expr(s);
			if(state == State.RParen)
			{
				getToken();
				statement(s);

				if(state == State.Keyword_else)
				{
					displayln(s + s.length() + " " + t);
					s = s + " ";
					getToken();
					statement(s);
					return;
				}
				else
				{
					return;

				}
			}
			else
			{
				errorMsg(1);
				return;
			}
		}
		else
		{
			errorMsg(3);
			return;
		}
	}//end of cond aka condition

	public static void expr(String s)
	{
		/**⟨expr⟩ → ⟨boolTerm⟩ { "||" ⟨boolTerm⟩ }*/

		displayln(s + s.length() + " <expr>");
		s = s + " ";

		boolTerm(s);

		while(state == State.Or)
		{
			displayln(s + s.length() + " " + t);
			getToken();
			boolTerm(s);
		}
	} //end of expr

	public static void boolTerm(String s)
	{
		/** ⟨boolTerm⟩ → ⟨boolPrimary⟩ { "&&" ⟨boolPrimary⟩ } */

		displayln(s + s.length() + " <boolTerm>");
		s = s + " ";

		boolPrimary(s);

		while(state == State.And)
		{
			displayln(s + s.length() + " " + t);
			getToken();
			boolPrimary(s);
		}
	} //end of boolTerm

	public static void boolPrimary(String s)
	{
		/** ⟨boolPrimary⟩ → ⟨E⟩ [ ⟨rel op⟩ ⟨E⟩ ] */

		displayln(s + s.length() + " <boolPrimary>");
		s = s + " ";

		E(s);

		if(state == State.Lt || state == State.Le || state == State.Gt
				              || state == State.Ge || state == State.Eq || state == State.Neq)
		{

			relOp(s);
			E(s);
			return;
		}
	} // end of boolPrimary

	public static void relOp(String s)
	{
		/**  ⟨rel op⟩ → "<" | "<=" | ">" | ">=" | "==" | "!=" */

		if(state == State.Lt)
		{
			displayln(s + s.length() + " " + t);
			getToken();
			return;
		}
		else if(state == State.Le)
		{

			displayln(s + s.length() + " " + t);
			getToken();
			return;
		}
		else if(state == State.Lt)
		{
			displayln(s + s.length() + " " + t);
			getToken();
			return;
		}
		else if(state == State.Gt)
		{
			displayln(s + s.length() + " " + t);
			getToken();
			return;
		}
		else if(state == State.Ge)
		{
			displayln(s + s.length() + " " + t);
			getToken();
			return;
		}
		else if(state == State.Eq)
		{
			displayln(s + s.length() + " " + t);
			getToken();
			return;
		}
		else if(state == State.Neq)
		{
			displayln(s + s.length() + " " + t);
			getToken();
			return;
		}
		else
		{
			errorMsg(8);
		}
	} //End Of relOps aka Real operations
    public static void E(String s)

    /** <E> → <term> | <term> + <E> | <term> - <E> */

    {
        displayln(s + s.length() + " <E>");
        s = s + " ";

        term(s);
        while ( state == State.Add || state == State.Sub )
        {
            displayln(s+ s.length()+ " "+ t);
            getToken();
            term(s);
        }
    } // end of E


    public static void term(String s)

    /** <term> → <primary> | <primary> * <term> | <primary> / <term> */

    {
        displayln(s+ s.length()+ " <term>");
        s = s+" ";

        primary(s);
        while ( state == State.Mul || state == State.Div )
        {
            displayln(s+s.length()+" "+t);
            getToken();
            primary(s);
        }
    } // end of term


    public static void primary(String s)

    /** ⟨primary⟩ → ⟨id⟩ | ⟨int⟩ | ⟨float⟩ | ⟨floatE⟩ | ⟨boolLiteral⟩ | "(" ⟨expr⟩ ")" | − ⟨primary⟩ | ! ⟨primary⟩ */

    {
        display(s+ s.length()+ " <primary>");
        s = s + " ";

        switch( state )
        {
			case Keyword_print:
				displayln(" "+ t);
				getToken();
				expr(s);
				return;
        case Id:
        	displayln(" " + t);
            getToken();
            return;

        case Int:
        	displayln(" "+t);
            getToken();
            return;

        case Float:
        	displayln(" "+t);
            getToken();
            return;

        case FloatE:

            displayln(" "+t);
            getToken();
            return;

        case Keyword_true:
          displayln("\n");
        	displayln(s+s.length()+"<boolLiteral> "+t);
        	getToken();
        	return;

        case Keyword_false:

        	displayln("\n");
            displayln(s+s.length()+"<boolLiteral> "+t);
        	getToken();
        	return;


        case LParen:

            displayln("");

            getToken();
            expr(s);
            if ( state == State.RParen )
                getToken();
            else
                errorMsg(1);
            return;




        case Sub:

        	displayln("");
        	display(s + s.length() + " " + t);
        	getToken();
        	displayln("");
        	primary(s);
        	return;

        case Inv:

        	displayln("");
        	displayln(s + s.length() + " " + t);
        	getToken();
        	primary(s);
        	return;


        default:
            errorMsg(2);
            return;
        }
    } // end of primary

    public static void errorMsg(int i)
    {
        display(t + " : unexpected symbol where");

        switch( i )
        {
        case 1: displayln(" op or ) expected"); return;
        case 2: displayln(" id, int, float, or ( expected"); return;
        case 3: displayln(" ( expected"); return;
        case 4: displayln(" = expected"); return;
        case 5: displayln(" } expected"); return;
        case 6: displayln(" ; expected"); return;
        case 7: displayln(" id, {, if, while, or do expected"); return;
        case 8: displayln(" <, <=, >, >=, ==, != expected"); return;

        }
    } // end of the error errorMsg

    public static void main(String argv[])

    // The input/output file have to be passed as argv[0] and argv[1] made this error on the first part.

    {
        setIO(argv[0],argv[1]);
        setLex();

        String s = ""; // used to properly indent

        getToken();
        statement(s);
        if ( ! t.isEmpty() )
            displayln(t + "  : unexpected symbol");

        closeIO();
    } // end of main
}
