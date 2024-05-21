package dev.davidson.ian.advent.year2015.day07.instruction;

import dev.davidson.ian.advent.year2015.day07.Instruction;
import dev.davidson.ian.advent.year2015.day07.Operation;
import dev.davidson.ian.advent.year2015.day07.Wire;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class OrInstruction extends Instruction implements Operation {
    // x OR y -> e
    private final Wire left;
    private final Wire right;

    public OrInstruction(final Wire left, final Wire right, final String resultLabel) {
        super(resultLabel);
        this.left = left;
        this.right = right;
    }

    @Override
    public Integer evaluate(Map<String, Integer> labelMap) {
        if (isEligible(labelMap)) {
            return left.get(labelMap) | right.get(labelMap);
        }

        return null;
    }

    @Override
    public Boolean isEligible(Map<String, Integer> labelMap) {
        return left.isEligible(labelMap) && right.isEligible(labelMap);
    }
}
