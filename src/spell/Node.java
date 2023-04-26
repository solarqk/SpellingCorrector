package spell;

public class Node implements INode {
    int value;
    Node[] children;
    public Node() {
        children = new Node[26]; // default to 26 for each letter in alphabet
    }


    @Override
    public int getValue() {
        return value;
    }

    @Override
    public void incrementValue() {
        value++;
    }

    @Override
    public INode[] getChildren() {
        return children;
    }

}
