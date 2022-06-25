package com.jazzteam.cleopatra.entity;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class Priority {
    private Integer id;
    private String name;
    private Integer weight;

    @Override
    public String toString() {
        return name;
    }
}
