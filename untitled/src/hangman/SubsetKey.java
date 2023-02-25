package hangman;

public class SubsetKey {
    private String subsetString;
    private int numChars;

    public int getNumChars() {
        return numChars;
    }

    public String getSubsetString() {
        return subsetString;
    }


    SubsetKey getSubsetKey(String word, char guess) {
        StringBuilder stringTR = new StringBuilder();

        for (int i = 0; i < word.length(); i++) {
            if(word.charAt(i) == guess){
                stringTR.append(guess);
                numChars++;
            }
            else{
                stringTR.append('_');
            }
        }
        subsetString = stringTR.toString();
        return this;
    }
}
