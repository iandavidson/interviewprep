package org.example.advent.year2023.twenty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class PulseVector {
    private final PULSE pulse;
    private final String senderLabel;
    private final SignalReceiver signalReceiver;
}
