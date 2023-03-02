package hangman;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class EvilHangmanGame extends SubsetKey implements IEvilHangmanGame {
  String emptyWord;
  String currentWord;
  public Set<String>word_set = new HashSet<>();
  Set<Character>usedGuesses = new HashSet<>();


  String blankLetters = null;



    //the idea is that the user will guess a letter, then will create partitions of the set meaning different subs
    // the subsets will contain words that contain that letter at a given position
    //take all the words that hold the letter in one position. then the second position then the second and first position . Each word only belongs to one subset
    // the biggest subset then becomes the new set to be partitioned by the second guess, by doing so we keep the guess as large as possible.
    // create the subsets as you encounter the words
    // use a map to implement the data structure
    // 1. go through dictionary with guessed letter in hand. Go through each word and create the datastructes as you come across examples
    // If the word does not contain the letter than skip it easy.
    

  @Override
    public void startGame(File dictionary, int wordLength) throws IOException, EmptyDictionaryException {
      // plan: In the make guess section partition through the sets and try and figure out the sets to keep etc. You should have all the info you need in all of the classes now.
        Scanner dScanner = new Scanner(dictionary);
        word_set = new HashSet<>();
        if(dScanner.hasNext() == false){
          throw new EmptyDictionaryException();
        }
        while(dScanner.hasNext()){
          String potentialWord = dScanner.next();
          if(potentialWord.length() == wordLength){
            word_set.add(potentialWord);

          }
        }
        if(word_set.isEmpty()){
          throw new EmptyDictionaryException();
        }
        emptyWord = createEmptyWord(wordLength);
        currentWord = emptyWord;
        //loads all of the words from the dictionary in to a set<String>
        //creates a dictionary essentially
        //goes through the set and throws away all of the words that are not of the specified length wordLength
        // goes through the entire thing, makes sure that the word that we give back to the player is not one they picked


    }

  private String createEmptyWord(int wordLength) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < wordLength; i++) {
      sb.append('_');

    }
    return sb.toString();
  }

  @Override
    public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {
    guess = Character.toLowerCase(guess);
    if (usedGuesses.contains(guess)){

      //throw empty exception
      throw new GuessAlreadyMadeException();
    }
    usedGuesses.add(guess);

      String keyToReturn = null;


      Map<String,Set<String>>pmap = new HashMap<>();

        for (String word:word_set) {
          Set<String>emptySet = new HashSet<>();
          SubsetKey subsetKey = getSubsetKey(word, guess);

            pmap.putIfAbsent(subsetKey.getSubsetString(),emptySet);
            pmap.get(subsetKey.getSubsetString()).add(word);
        }




      for (String keyToTest:pmap.keySet()) {
        //set key to return
        if (keyToReturn == null) {
          keyToReturn = keyToTest;
        }
        //compare sizes of the words
        else if (pmap.get(keyToReturn).size() < pmap.get(keyToTest).size()) {
          keyToReturn = keyToTest;
        }
        else if (pmap.get(keyToReturn).size() == pmap.get(keyToTest).size()){
          //this is what to do when the size is equal
          //return the empty words
          if(!keyToReturn.equals(emptyWord) && keyToTest.equals(emptyWord)){
            keyToReturn = keyToTest;
          }
          else if(keyToReturn.equals(emptyWord) && !keyToTest.equals(emptyWord)){
            keyToReturn = keyToReturn;

          }
          //both have characters
          else if(!keyToReturn.equals(emptyWord) && !keyToTest.equals(emptyWord)){
            int keyToReturnCount = getKeyCount(keyToReturn, guess);
            int keyToTestCount = getKeyCount(keyToTest,guess);

            if(keyToReturnCount < keyToTestCount){
              keyToReturn = keyToReturn;
            }
            else if(keyToReturnCount > keyToTestCount){
              keyToReturn = keyToTest;
            }
            // this is the furthest right thing
            else if(keyToReturnCount == keyToTestCount){
              keyToReturn = testFurthestRight(keyToReturn,keyToTest,guess);

            }


          }
        }
      }


      //this is where you display if the word was found or not.
      if(keyToReturn.equals(emptyWord)){
        System.out.println("Sorry, there are no " + guess + "'s" + "\n");
      }
      else if (getKeyCount(keyToReturn,guess) > 1){
        System.out.println("Yes, there are "+ getKeyCount(keyToReturn,guess)+" "+ guess + "'s"+ "\n");

      }
      else if (getKeyCount(keyToReturn,guess) == 1){
        System.out.println("Yes, there is 1 " + guess+ "\n");

      }
      setCommonWord(keyToReturn);
      word_set = pmap.get(keyToReturn);

      return word_set;
    }

  private String testFurthestRight(String keyToReturn, String keyToTest, char guess) {
    for (int i = keyToReturn.length() - 1; i >= 0; i--) {
      if (keyToReturn.charAt(i) == guess && !(keyToTest.charAt(i) == guess)) {
        return keyToReturn;
      }
      else if(!((keyToReturn.charAt(i)) == guess) && (keyToTest.charAt(i) == guess)){
        return keyToTest;
      }
    }
    return keyToReturn;
  }

  private int getKeyCount(String mostCommonKey, char guess) {
    int count = 0;
    for (int i = 0; i < mostCommonKey.length(); i++) {
      if(mostCommonKey.charAt(i) == guess){
        count++;
    }
    }

    return count;
  }

  private void setCommonWord(String mostCommonKey) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < mostCommonKey.length(); i++) {

      if (currentWord.charAt(i) == '_' && mostCommonKey.charAt(i) == '_'){
        sb.append('_');
      }
      else if (mostCommonKey.charAt(i) != '_'){
        sb.append(mostCommonKey.charAt(i));
      }
      else{
        sb.append(currentWord.charAt(i));
      }

    }
    currentWord = sb.toString();
  }


  @Override
    public SortedSet<Character> getGuessedLetters() {

        return new TreeSet<>(usedGuesses);
    }

  public String getCurrentWord() {
    return currentWord;
  }
  public String getFirstWord(){
    String firstWord = word_set.stream().findFirst().toString().replace("Optional[","");
    firstWord = firstWord.replace("]","");
    return firstWord;

  }
}
