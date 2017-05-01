class RelPrimary extends BoolPrimary
{
    static String[] relop_st = { "<", "<=", ">", ">=", "==", "!=" };
    static String[] relop_instruction = { "lt", "le", "gt", "ge", "eq", "neq" };

    E e1;
    E e2;
    State relop;

    RelPrimary(E e_1, E e_2, State rel)
    {
        e1 = e_1;
        e2 = e_2;
        relop = rel;
    }

    void printParseTree(String indent)
    {
        String indent1 = indent + " ";

        super.printParseTree(indent);
        e1.printParseTree(indent1);
        IO.displayln(indent1 + indent1.length() + " " + relop_st[relop.ordinal()-7]);
        e2.printParseTree(indent1);
    }
}