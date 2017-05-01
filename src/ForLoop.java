class ForLoop extends Statement
{
    Assign assign1;
    Expr expr;
    Assign assign2;
    Statement statement;

    ForLoop(Assign a1, Expr e, Assign a2, Statement s)
    {
        assign1 = a1;
        expr = e;
        assign2 = a2;
        statement = s;
    }

    void printParseTree(String indent)
    {
        String indent1 = indent + " ";
        String indent2 = indent + "  ";

        super.printParseTree(indent);
        IO.displayln(indent1 + indent1.length() + " <for loop>");
        IO.displayln(indent2 + indent2.length() + " for");
        assign1.printParseTree(indent2);
        expr.printParseTree(indent2);
        assign2.printParseTree(indent2);
        statement.printParseTree(indent2);
    }
}
