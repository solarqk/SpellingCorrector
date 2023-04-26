package spell;

import java.io.IOException;
import java.io.File;
import java.util.*;

public class SpellCorrector implements ISpellCorrector {

    Trie dictionary = new Trie();

    //use of TreeSet so it sorts alphabetically
    Set<String> set_ = new TreeSet<String>(); //suggestion words after one iteration
    Set<String> set_2 = new TreeSet<String>(); //suggestion words after two iterations

    private boolean firstIterationDone = false;


    @Override
    public void useDictionary(String dictionaryFileName) throws IOException {
        File wordList = new File(dictionaryFileName);

        Scanner scanner = new Scanner(wordList);
        while(scanner.hasNext()) {
            dictionary.add(scanner.next()); //puts all the words from the dictionary into the Trie

        }


        System.out.println(dictionary.getNodeCount());
        System.out.println(dictionary.getWordCount());
    }

    @Override
    public String suggestSimilarWord(String inputWord) {


        String input = inputWord.toLowerCase();

        if (Objects.equals(input, "")) { //if empty, return null
            return null;
        }

        if (dictionary.find(input) != null ) { //if spelled correctly, return word
            return input;
        }

        firstIterationDone = false;
        set_.clear();       //clears the sets
        set_2.clear();

        runFunctions(input);    //if not spelled correctly, go through the methods
                                    //adds correctly spelled words into set_
                                    //adds all words, regardless of correctness, into set_2

        firstIterationDone = true;
        if (set_.isEmpty()) {//if there are no correctly spelled words after first iteration

            //makes a copy of set_2 so we are not iterating through something while adding to it
            TreeSet<String> set_3 = new TreeSet<String>(set_2);

            for (String word : set_3) { //sends set_3 into the functions to check if another iteration fixes it

                runFunctions(word);
            }
            if (set_.isEmpty()) {
                for (String word : set_2) {
                    //System.out.print(word + " ");
                    if (dictionary.find(word) != null) {
                        return word;
                    }

                } //returns a value, based alphabetically
            }
        }
        TreeSet<String> set_4 = new TreeSet<String>();
        int maxVal = 0;
        for (String word : set_) { //
            if (dictionary.find(word).getValue() > maxVal) {
                maxVal = dictionary.find(word).getValue();
                set_4.clear();
                set_4.add(word);
            }
            else if(dictionary.find(word).getValue() == maxVal) {
                set_4.add(word);
            }
            //return word;
        }
        for (String word : set_4) {
            return word;
        }
        return null;
    }


    //replaces characters
    private void wrongCharacter(String inputWord) {
        for (int i = 0; i < inputWord.length(); i++) {
            for (char chara = 'a'; chara < 'z'; chara++ ) {
                StringBuilder currWord = new StringBuilder(inputWord);
                currWord.setCharAt(i, chara);

                if (dictionary.find(currWord.toString()) != null) {
                    set_.add(currWord.toString());
                }
                set_2.add(currWord.toString());
            }
        }
    }

    private void omitCharacter(String inputWord) {
        if (inputWord.length() <= 1) {
            return;
        }
        for (int i = 0; i < inputWord.length(); i++) {
            StringBuilder currWord = new StringBuilder(inputWord);

            currWord.deleteCharAt(i);

            if (dictionary.find(currWord.toString()) != null) {
                set_.add(currWord.toString());
            }
            set_2.add(currWord.toString());
        }
    }

    private void insertExtraCharacter(String inputWord) {

        for (int i = 0; i < inputWord.length() + 1; i++) {
            for (int j = 0; j < 26; j++) {
                StringBuilder currWord = new StringBuilder(inputWord);
                char chara = (char) ('a' + j); //cast to a char before adding chars

                currWord.insert(i, chara);

                if (i == inputWord.length() - 1) {
                    StringBuilder newString = new StringBuilder(inputWord);
                    newString.append(chara);

                    if (dictionary.find(newString.toString()) != null) {
                        set_.add(newString.toString());
                    }
                    set_2.add(newString.toString());
                }

                if (dictionary.find(currWord.toString()) != null) {
                    set_.add(currWord.toString());
                }
                set_2.add(currWord.toString());
            }
        }
    }

    private void transposeCharacters(String inputWord) {

        for (int i = 1; i < inputWord.length(); i++) {
            StringBuilder currWord = new StringBuilder(inputWord);

            char iChar = (char) currWord.charAt(i);
            char iCharMinusOne = (char) currWord.charAt(i - 1);


            currWord.setCharAt(i, iCharMinusOne);
            currWord.setCharAt(i - 1, iChar);

            if (dictionary.find(currWord.toString()) != null) {
                set_.add(currWord.toString());
            }
            set_2.add(currWord.toString());
        }
    }
    private void runFunctions(String inputWord) {
        wrongCharacter(inputWord);
        omitCharacter(inputWord);
        insertExtraCharacter(inputWord);
        transposeCharacters(inputWord);
    }
}
