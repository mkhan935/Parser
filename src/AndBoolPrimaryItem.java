class AndBoolPrimaryItem extends BoolPrimaryItem

// Represents "&& <boolPrimary>"

{
    // BoolPrimary boolPrimary; inherited from BoolPrimaryItem

    AndBoolPrimaryItem(BoolPrimary bp)
    {
        boolPrimary = bp;
    }

    void printParseTree(String indent)
    {
        IO.displayln(indent + indent.length() + " &&");
        boolPrimary.printParseTree(indent);
    }
}