package com.springboot.lococo.userai.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "festival_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FestivalItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
}
