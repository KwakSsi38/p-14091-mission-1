package com.back;

import com.back.repository.QuestionRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class SbbApplicationTests {

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    @DisplayName("findAll")
    void t1() {
        List<Question> questions = questionRepository.findAll();
        assertThat(questions).hasSize(2);

        Question question = questions.get(0);
        assertThat(question.getSubject()).isEqualTo("sbb가 무엇인가요?");
    }

    @Test
    @DisplayName("findById")
    void t2() {
        Question question = questionRepository.findById(1).get();
        assertThat(question.getSubject()).isEqualTo("sbb가 무엇인가요?");
    }

    @Test
    @DisplayName("findBySubject")
    void t3() {
        Question question = questionRepository.findBySubject("sbb가 무엇인가요?");
        // SELECT * FROM question WHERE subject = 'sbb가 무엇인가요?'
        assertThat(question.getId()).isEqualTo(1);
    }

    @Test
    @DisplayName("findBySubjectAndContent")
    void t4() {
        Question question = questionRepository.findBySubjectAndContent("sbb가 무엇인가요?", "sbb에 대해서 알고 싶습니다.");
        assertThat(question.getId()).isEqualTo(1);
    }

    @Test
    @DisplayName("findBySubjectLike")
    void t5() {
        List<Question> questions = questionRepository.findBySubjectLike("sbb%");

        Question question = questions.get(0);
        assertThat(question.getSubject()).isEqualTo("sbb가 무엇인가요?");
    }

    @Test
    @DisplayName("update")
    void t6() {
        Question question = questionRepository.findById(1).get();
        assertThat(question).isNotNull();

        question.setSubject("수정된 제목");
        questionRepository.save(question);

        Question foundQuestion = questionRepository.findBySubject("수정된 제목");
        assertThat(foundQuestion).isNotNull();
        // dirty check로 인해 저장할 필요 없음
    }

    @Test
    @DisplayName("delete")
    void t7() {
        assertThat(questionRepository.count()).isEqualTo(2);

        Question question = questionRepository.findById(1).get();
        questionRepository.delete(question);

        assertThat(questionRepository.count()).isEqualTo(1);
    }
}