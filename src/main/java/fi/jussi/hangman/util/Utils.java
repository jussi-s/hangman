package fi.jussi.hangman.util;

import java.util.*;

/**
 * Created by Jussi on 9.6.2016.
 *
 * @author Jussi
 * Utility methods for the Hangman Game.
 */
public class Utils {

    /**
     * Converts list of Strings to a Set of Characters. As MongoDB stores the
     * Character Sets as a list, it is necessary to use a converter method.
     * @param list The list of characters (the strings should be of length 1).
     * @return The set of characters.
     */
    public static Set<Character> listToSet(List<String> list) {
        Set<Character> newSet = new TreeSet<>();
        for (String str : list) {
            newSet.add(str.charAt(0));
        }
        return newSet;
    }

    /**
     * Converts a character/indices map to String/indices map - used for MongoDB
     * as it doesn't support the Character class.
     * @param wordCharMap
     * @return The wordCharMap with String as the key type.
     */
    public static Map<String, List<Integer>> wordCharMapStrKey(Map<Character, List<Integer>> wordCharMap) {
        Map<String, List<Integer>> wordCharMapNew = new TreeMap<>();
        for (Map.Entry<Character, List<Integer>> ent : wordCharMap.entrySet()) {
            wordCharMapNew.put(ent.getKey().toString(), ent.getValue());
        }
        return wordCharMapNew;
    }

    /**
     * Converts a string/indices map to character/indices map - used after retrieving the
     * data from a MongoDB database.
     * @param wordCharMap
     * @return The wordCharMap with Character as the key type.
     */
    public static Map<Character, List<Integer>> mapStrToChr(Map<String, List<Integer>> wordCharMap) {
        Map<Character, List<Integer>> newMap = new TreeMap<>();
        for (Map.Entry<String, List<Integer>> ent : wordCharMap.entrySet()) {
            newMap.put(ent.getKey().charAt(0), ent.getValue());
        }
        return newMap;
    }

}
