package com.back;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Setter // 일반적으로 Setter 메서드는 허용하지 않음
@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private int id;

    private LocalDateTime createDate;

    @Column(length = 200, nullable = false) // varchar 255 -> 200 제한, null 허용
    private String subject;

    @Column(columnDefinition = "TEXT") // 20000자 제한
    private String content;

    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    private List<Answer> answerList;
}