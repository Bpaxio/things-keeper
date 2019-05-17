package ru.bbpax.keeper.security.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class AccessLevels {
    public static final String READ = "READ";
    public static final String WRITE = "WRITE";
    public static final String SHARE = "SHARE";
    public static final String DELETE = "DELETE";

    public static final Set<String> ALL = new HashSet<>(Arrays.asList(READ, WRITE, SHARE, DELETE));
}
