package fi.jussi.hangman.util;

import java.util.*;

/**
 * Created by Jussi on 9.6.2016.
 */
public class Utils {

    public static Set<Character> listToSet(List<String> list) {
        Set<Character> newSet = new TreeSet<>();
        for (String str : list) {
            newSet.add(str.charAt(0));
        }
        return newSet;
    }

    public static Map<String, List<Integer>> wordCharMapStrKey(Map<Character, List<Integer>> wordCharMap) {
        Map<String, List<Integer>> wordCharMapNew = new TreeMap<>();
        for (Map.Entry<Character, List<Integer>> ent : wordCharMap.entrySet()) {
            wordCharMapNew.put(ent.getKey().toString(), ent.getValue());
        }
        return wordCharMapNew;
    }

    public static Map<Character, List<Integer>> mapStrToChr(Map<String, List<Integer>> wordCharMap) {
        Map<Character, List<Integer>> newMap = new TreeMap<>();
        for (Map.Entry<String, List<Integer>> ent : wordCharMap.entrySet()) {
            newMap.put(ent.getKey().charAt(0), ent.getValue());
        }
        return newMap;
    }

}
