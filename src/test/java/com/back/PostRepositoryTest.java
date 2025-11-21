package com.back;

import com.back.answer.Answer;
import com.back.answer.AnswerRepository;
import com.back.question.Question;
import com.back.question.QuestionRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class PostRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;

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

        Optional<Question> foundQuestion = questionRepository.findById(1);
        assertThat(foundQuestion).isEmpty();

        assertThat(questionRepository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("answer create")
    void t8() {
        Question question = questionRepository.findById(2).get();

        Answer answer = new Answer();
        answer.setContent("네 자동으로 생성됩니다.");
        answer.setQuestion(question);
        answer.setCreateDate(LocalDateTime.now());
        answerRepository.save(answer);
    }

    @Test
    @DisplayName("answer create by oneToMany")
    void t9() {
        Question question = questionRepository.findById(2).get();
        int beforeCount = question.getAnswers().size();
        Answer newAnswer = question.addAnswer("네 자동으로 생성됩니다.");
        // 트랜잭션이 종료된 이후에 DB에 반영되기 때문에 현재는 일단 0으로 설정된다.
        int afterCount = question.getAnswers().size();
        assertThat(afterCount).isEqualTo(beforeCount + 1);
    }

    @Test
    @DisplayName("answer get")
    void t10() {
        Answer answer = answerRepository.findById(1).get();
        assertThat(answer.getId()).isEqualTo(1);
    }

    @Test
    @DisplayName("answer get by oneToMany")
    void t11() {
        Question question = questionRepository.findById(2).get();

        List<Answer> answers = question.getAnswers();
        assertThat(answers).hasSize(1);

        Answer answer = answers.get(0);
        assertThat(answer.getContent()).isEqualTo("네 자동으로 생성됩니다.");
    }

    @Test
    @DisplayName("findAnswer by question")
    void t12() {
        Question question = questionRepository.findById(2).get();
        Answer answer1 = question.getAnswers().get(0);
        assertThat(answer1.getId()).isEqualTo(1);
    }
}