package com.jazzteam.cleopatra.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class Status {
    private Integer id;
    private String name;

    @Override
    public String toString() {
        return name;
    }
}
