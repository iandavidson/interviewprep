package dev.davidson.ian.advent.year2015.day19;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;

@Slf4j
public class MedicineForRudolph {

    private static final String INPUT_PATH = "adventOfCode/2015/day19/input.txt";
//    private static final String SAMPLE_PATH = "adventOfCode/2015/day19/sample.txt";

    public static void main(String[] args) {
        MedicineForRudolph medicineForRudolph = new MedicineForRudolph();
        log.info("Part1: {}", medicineForRudolph.part1());
    }

    public long part1() {
        List<Rule> rules = new ArrayList<>();
        String input = readFile(rules);

        Set<String> results = new HashSet<>();
        for (Rule rule : rules) {
            int patternLength = rule.pattern().length();
            for (int i = 0; i < input.length() - patternLength + 1; i++) {
                String sub = input.substring(i, i + patternLength);
                if (sub.equals(rule.pattern())) {
                    results.add(left(input, i) +
                            rule.treated() +
                            right(input, i + patternLength));
                }
            }
        }

        return results.size();
    }

    private String left(String original, final int end) {
        if (end <= -1) {
            return "";
        } else {
            return original.substring(0, end);
        }
    }

    private String right(String original, final int start) {
        if (start > original.length()) {
            return "";
        } else {
            return original.substring(start);
        }
    }

    private String readFile(final List<Rule> rules) {

        ClassLoader cl = MedicineForRudolph.class.getClassLoader();
        File file = new File(Objects.requireNonNull(cl.getResource(INPUT_PATH)).getFile());
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String nextLine = scanner.nextLine();
                if (nextLine.isBlank()) {
                    break;
                }
                String[] parts = nextLine.split("=>");
                rules.add(new Rule(parts[0].trim(), parts[1].trim()));
            }

            return scanner.nextLine();
        } catch (FileNotFoundException e) {
            throw new IllegalStateException(e.getCause());
        }
    }
}
