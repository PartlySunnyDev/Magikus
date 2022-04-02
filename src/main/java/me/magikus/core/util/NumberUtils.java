package me.magikus.core.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.TreeMap;

public class NumberUtils {

    private static final TreeMap<Integer, String> map = new TreeMap<>();

    private static void initRomanConverter() {
        map.put(1000, "M");
        map.put(900, "CM");
        map.put(500, "D");
        map.put(400, "CD");
        map.put(100, "C");
        map.put(90, "XC");
        map.put(50, "L");
        map.put(40, "XL");
        map.put(10, "X");
        map.put(9, "IX");
        map.put(5, "V");
        map.put(4, "IV");
        map.put(1, "I");
    }

    public static String toRoman(int number) {
        if (map.size() < 1) {
            initRomanConverter();
        }
        int l = map.floorKey(number);
        if (number == l) {
            return map.get(number);
        }
        return map.get(l) + toRoman(number - l);
    }

    public static int clamp(int number, int min, int max) {
        if (number < min) {
            return min;
        }
        return Math.min(number, max);
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static String getIntegerStringOf(double value, int round) {
        String s = Double.toString(round(value, round));
        if (s.contains("E")) {
            s = new BigDecimal(s).toPlainString();
        }
        if (s.endsWith(".0")) {
            return s.substring(0, s.length() - 2);
        }
        return s;
    }

}
