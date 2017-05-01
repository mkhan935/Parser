class InvPrimary extends Primary
{
    Primary primary;

    InvPrimary(Primary p)
    {
        primary = p;
    }

    void printParseTree(String indent)
    {
        super.printParseTree(indent);
        IO.displayln("");
        IO.displayln(indent + indent.length() + " !");
        primary.printParseTree(indent+" ");
    }
}