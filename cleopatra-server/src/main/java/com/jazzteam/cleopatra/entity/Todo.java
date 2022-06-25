package com.jazzteam.cleopatra.entity;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public class Todo {
    @NonNull
    private UUID id;
    @NonNull
    private String title;
    @NonNull
    private String description;
    @NonNull
    private Priority priority;

    private LocalDate createDate;
    private LocalDate endDate;
    private Status status;
}