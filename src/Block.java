class Block extends Statement
{
    SList slist;

    Block(SList s)
    {
        slist = s;
    }

    void printParseTree(String indent)
    {
        String indent1 = indent + " ";

        super.printParseTree(indent);
        IO.displayln(indent1 + indent1.length() + " <block>");
        slist.printParseTree(indent1+" ");
    }
}