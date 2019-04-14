/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private ArrayList<String> wordList;
    private HashSet<String> wordSet;
    private HashMap<String, ArrayList<String>> lettersToWord;
    private HashMap<Integer, ArrayList<String>> sizeToWords;

    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;
        wordList = new ArrayList<>();
        wordSet = new HashSet<>();
        lettersToWord = new HashMap<>();
        sizeToWords = new HashMap<>();

        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordList.add(word);

            if(!wordSet.contains(word)){
                wordSet.add(word);

                int wordLen = word.length();
                if(!sizeToWords.containsKey(wordLen))
                    sizeToWords.put(wordLen, new ArrayList<String>());
                sizeToWords.get(wordLen).add(word);

                String sortedWord = sortWord(word);
                if(!lettersToWord.containsKey(sortedWord))
                    lettersToWord.put(sortedWord, new ArrayList<String>());
                lettersToWord.get(sortedWord).add(word);
            }
        }
    }

    public boolean isGoodWord(String word, String base) {
        return wordSet.contains(word) && !word.contains(base);
    }

    private String sortWord(String word) {
        char tmp[] = word.toCharArray();
        Arrays.sort(tmp);
        return new String(tmp);
    }

    public List<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();
        targetWord = sortWord(targetWord);
        for(String word: wordList) {
            if(sortWord(word) == targetWord) {
                result.add(word);
            }
        }
        return result;
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        for(char addition_char = 'a'; addition_char <= 'z'; addition_char++){
            String tmp_key = sortWord(word+addition_char);
            if(lettersToWord.containsKey(tmp_key)){
                for(String possible_word: lettersToWord.get(tmp_key)){
                    if(isGoodWord(possible_word, word))
                        result.add(possible_word);
                }
            }
        }
        return result;
    }

    public String pickGoodStarterWord() {
        int wordLength = DEFAULT_WORD_LENGTH;

        while(true){
            ArrayList<String> tmp = new ArrayList<>(sizeToWords.get(wordLength));
            int randomNum = random.nextInt(tmp.size() + 1);
            String word_selected = tmp.get(randomNum);
            if(getAnagramsWithOneMoreLetter(word_selected).size() >= MIN_NUM_ANAGRAMS)
                return word_selected;
            if(wordLength < MAX_WORD_LENGTH) wordLength += 1;
        }
    }
}
