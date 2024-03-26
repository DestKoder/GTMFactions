package ru.dest.gtmfractions.object;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

@Getter
@AllArgsConstructor
public class FactionGroup {

    private final String id;
    private final String group;
    private final int salaryTime;
    private final int salary;

    @Nullable
    private final String next;
    @Nullable
    private final String prev;

}
