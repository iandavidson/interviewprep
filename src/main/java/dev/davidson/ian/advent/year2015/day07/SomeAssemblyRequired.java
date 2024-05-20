package dev.davidson.ian.advent.year2015.day07;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

@Slf4j
public class SomeAssemblyRequired {

    private static final String TERMINAL_LABEL = "a";
//    private static final String TERMINAL_LABEL = "i";

    private static final String SAMPLE_PATH = "adventOfCode/2015/day07/sample.txt";
    private static final String INPUT_PATH = "adventOfCode/2015/day07/input.txt";

    public static void main(String[] args) {
        SomeAssemblyRequired someAssemblyRequired = new SomeAssemblyRequired();
        log.info("part1: {}", someAssemblyRequired.part1());
    }

    public int part1() {
        List<Operation> operations = readFile();
        Map<String, Integer> map = new HashMap<>();

        while (!map.containsKey(TERMINAL_LABEL)) {
            for (Operation current : operations) {
                Integer result = current.evaluate(map);

                if (result != null) {
                    String nextLabel = current.getResultLabel();
                    map.put(nextLabel, result);
                }
            }
        }

        return map.get(TERMINAL_LABEL);
        /*
        d: 72
e: 507
f: 492
g: 114
h: 65412
i: 65079
x: 123
y: 456
         */
    }

    private List<Operation> readFile() {
        List<Operation> operations = new ArrayList<>();

        ClassLoader cl = SomeAssemblyRequired.class.getClassLoader();
        File file = new File(cl.getResource(INPUT_PATH).getFile());
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                operations.add(OperationFactory.newOperation(scanner.nextLine()));
            }
        } catch (FileNotFoundException e) {
            throw new IllegalStateException();
        }

        return operations;
    }


}
