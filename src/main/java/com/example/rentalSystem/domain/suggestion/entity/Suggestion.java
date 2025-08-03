package com.example.rentalSystem.domain.suggestion.entity;

import com.example.rentalSystem.domain.common.BaseTimeEntity;
import com.example.rentalSystem.domain.facility.entity.Facility;
import com.example.rentalSystem.domain.member.student.entity.Student;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Suggestion extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SuggestionCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_id", nullable = false)
    private Facility facility;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    @Lob
    private String adminAnswer;

    private LocalDateTime answeredAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SuggestionStatus status;

    @Builder
    public Suggestion(SuggestionCategory category, Facility facility, Student student,
                      String title, String content) {
        this.category = category;
        this.facility = facility;
        this.student = student;
        this.title = title;
        this.content = content;
        this.status = SuggestionStatus.RECEIVED;
    }

    public void updateSuggestion(SuggestionCategory category, Facility facility, String title, String content) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.facility = facility;
    }

    public void updateAnswer(String answer) {
        if (answer == null || answer.trim().isEmpty()) {
            throw new IllegalArgumentException("답변 내용은 비어 있을 수 없습니다.");
        }
        this.adminAnswer = answer;
        this.answeredAt = LocalDateTime.now();
    }

    public void setStatus(SuggestionStatus status) {
        if (this.status == SuggestionStatus.COMPLETED) {
            throw new IllegalStateException("완료된 건의는 상태 변경이 불가합니다.");
        }
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return super.getCreatedAt(); // BaseTimeEntity에 정의된 필드 기준
    }

    public String getAnswer() {
        return this.adminAnswer;
    }

}