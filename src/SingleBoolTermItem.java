class SingleBoolTermItem extends BoolTermItem

// Represents the first <boolTerm> in <Expr>

{
    // BoolTerm boolTerm; inherited from BoolTermItem

    SingleBoolTermItem(BoolTerm bt)
    {
        boolTerm = bt;
    }

    void printParseTree(String indent)
    {
        boolTerm.printParseTree(indent);
    }
}