package com.upgrad.quora.service.entity;


import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.ZonedDateTime;

@Entity
    @Table(name = "answer")
    @NamedQueries(
            {
                    @NamedQuery(name = "answerByUUID", query = "select ans from AnswerEntity ans where ans.uuid = :uuid"),
                    @NamedQuery(name = "answerByQuestionId", query = "select ans from AnswerEntity ans where ans.question.id = :questionId")
            }
    )
    public class AnswerEntity implements Serializable {

        @Id
        @Column(name = "id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        @Column(name = "uuid")
        @NotNull
        @Size(max = 200)
        private String uuid;

        @Column(name = "ans")
        @NotNull
        @Size(max = 255)
        private String ans;

        @Column(name = "date")
        @NotNull
        private ZonedDateTime date;

        @ManyToOne
        @OnDelete(action = OnDeleteAction.CASCADE)
        @JoinColumn(name = "user_id")
        @NotNull
        private UserEntity user;

        @ManyToOne
        @OnDelete(action = OnDeleteAction.CASCADE)
        @JoinColumn(name = "question_id")
        @NotNull
        private QuestionEntity question;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getAns() {
            return ans;
        }

        public void setAns(String ans) {
            this.ans = ans;
        }

        public ZonedDateTime getDate() {
            return date;
        }

        public void setDate(ZonedDateTime date) {
            this.date = date;
        }

        public UserEntity getUser() {
            return user;
        }

        public void setUser(UserEntity user) {
            this.user = user;
        }

        public QuestionEntity getQuestion() {
            return question;
        }

        public void setQuestion(QuestionEntity question) {
            this.question = question;
        }
}
