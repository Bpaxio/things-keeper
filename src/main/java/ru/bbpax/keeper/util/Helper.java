package ru.bbpax.keeper.util;

public class Helper {
    public static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    public static String getFileExtention(String filename) {
        if (filename == null) return ".jpg";
        return filename.substring(filename.lastIndexOf('.'));
    }
}
