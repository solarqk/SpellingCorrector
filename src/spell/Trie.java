package spell;

public class Trie implements ITrie {
    private final Node root;
    private int nodeCount;
    private int wordCount;


    public Trie() {
        root = new Node();
        wordCount = 0;
        nodeCount = 1; // starts at 1 accounting for root node
    }


    @Override
    public void add(String word) {

        Node currNode = (Node) root;

        word = word.toLowerCase();

        for (int i = 0; i < word.length(); i++) {
            char chara = word.charAt(i);
            int index = chara - 'a';
            if (currNode.getChildren()[index] == null) {
                currNode.getChildren()[index] = new Node(); //creates a new Node if spot is empty
                nodeCount++;
            }
            if (i == word.length() - 1) {
                if (currNode.getChildren()[index].getValue() == 0) {
                    wordCount++;

                }
                currNode.getChildren()[index].incrementValue();
            }
            currNode = (Node) currNode.getChildren()[index]; //had to cast to fix problems
        }
    }

    @Override
    public INode find(String word) {
        int index = 0;
        char chara = 'Q'; //testing
        Node currNode = (Node) root;

        word = word.toLowerCase();

        for (int i = 0; i < word.length(); i++) {

            chara = word.charAt(i);
            index = chara - 'a';
            Node child = (Node) currNode.getChildren()[index];

            if (child == null ) {
                return null;
            }

            if (i < word.length() - 1) {
                currNode = child;           //updates tree
            }
            else {
                currNode = child;
                if (currNode.getValue() > 0) {
                    return currNode;

                }
            }
                //^^^^^^^^  check if end of word, check val on node, if greater than 0, there is a word
                //          return the last node

        }
        return null;
    }

    @Override
    public int getWordCount() {
        return wordCount;
    }
    @Override
    public int getNodeCount() {
        return nodeCount;
    }

    @Override
    public int hashCode() {
        int index = 0;
        //combine following values
        //1. wordCount
        //2. nodeCount
        //3. the index of each of the root node's non-null children

        for (int i = 0; i < 26; i++) {
            if (root.getChildren()[i] != null) {
                index = i;
                break;
            }
        }

        //index of first children, //or sum of all indicies of children
        return ((wordCount * 31) + (nodeCount * 19) * index);
    }

    @Override
    public String toString() {
        StringBuilder currWord = new StringBuilder();
        StringBuilder output = new StringBuilder();

        toStringHelper(root, currWord, output);
       return output.toString();
    }

    private void toStringHelper(INode n, StringBuilder currWord, StringBuilder output) { //this is different from video
        if (n.getValue() > 0) {
            output.append(currWord.toString());
            output.append("\n");
        }
        for (int i = 0; i < 26; i++) {
            INode child = n.getChildren()[i];
            if (child != null) {

                char childLetter = (char)('a' + i);
                currWord.append(childLetter);
                toStringHelper(child, currWord, output);

                currWord.deleteCharAt(currWord.length() -1);
            }
        }
    }

    public boolean equals(Object o) {

        //is o == null?
        if (o == null) {
            return false;
        }

        //is o == this?
        if (o == this) {
            return true;
        }

        //do this and o have the same class?
        if (getClass() != o.getClass()) {
            return false;
        }

        Trie t = (Trie) o;

        //do this and t hae the same wordCount and nodeCount?
        if (t.nodeCount != this.nodeCount) { //does adding the t. do anything? they are already private variables for the class
            return false;
        }
        else if (t.wordCount != this.wordCount) {
            return false;
        }

        //if passes all these tests, then do the expensive comparing
        return equalsHelper(this.root, t.root);
    }

    private boolean equalsHelper(Node n1, Node n2) {
        if (n1 != null && n2 == null) {
            return false;
        }
        else if (n1 == null && n2 != null) {
            return false;
        }
        else if (n1 != null && n2 != null) {
            if (n1.getValue() != n2.getValue()) { //if we don't do this first, there's a loose end
                return false;
            }
            for (int i = 0; i < n1.getChildren().length; i++) {
                    if (!equalsHelper( (Node)n1.getChildren()[i], (Node)n2.getChildren()[i])) {
                        return false;
                    }
                //being equal at the end will be the most expensive answer
            }
        }
        return true;
        //compare n1 and n2 to see if they are the same
            //do n1 and n2 have the same count - each node has a count
                //if they don't have the same count, return false
            //do n1 and n2 have non-null children in exactly the same indexes?

        //recurse on the children and compare the child subtrees
    }


}
