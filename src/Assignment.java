class Assignment extends Statement
{
    String id; // variable on the left side of the assignment
    Expr expr; // expression on the right side of the assignment

    Assignment(String s, Expr e)
    {
        id = s;
        expr = e;
    }

    void printParseTree(String indent)
    {
        String indent1 = indent + " ";
        String indent2 = indent + "  ";

        super.printParseTree(indent);
        IO.displayln(indent1 + indent1.length() + " <assignment>");
        IO.displayln(indent2 + indent2.length() + " " + id);
        IO.displayln(indent2 + indent2.length() + " =");
        expr.printParseTree(indent2);
    }
}