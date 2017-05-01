class Assign
{
    String id; // variable on the left side
    Expr expr; // expression on the right side

    Assign(String s, Expr e)
    {
        id = s;
        expr = e;
    }

    void printParseTree(String indent)
    {
        String indent1 = indent + " ";

        IO.displayln(indent + indent.length() + " <assign>");
        IO.displayln(indent1 + indent1.length() + " " + id);
        IO.displayln(indent1 + indent1.length() + " =");
        expr.printParseTree(indent1);
    }
}