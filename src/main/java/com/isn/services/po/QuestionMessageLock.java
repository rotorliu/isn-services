package com.isn.services.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import com.fasterxml.jackson.annotation.JsonTypeName;

@Entity
@JsonTypeName("QuestionMessageLock")
public class QuestionMessageLock extends MessageLock {

	private String question;
	private String correctAnswer;
	private String answer;
	
	protected QuestionMessageLock(){
		
	}
	
	public QuestionMessageLock(String question, String correctAnswer){
		this.question = question;
		this.correctAnswer = correctAnswer;
	}
	
	@Override
	public boolean unlock() {
		return this.correctAnswer == this.answer;
	}

	@Column
	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	@Column
	public String getQuestion() {
		return question;
	}
	
	protected void setQuestion(String question) {
		this.question = question;
	}

	@Column
	public String getCorrectAnswer() {
		return correctAnswer;
	}
	
	protected void setCorrectAnswer(String correctAnswer) {
		this.correctAnswer = correctAnswer;
	}
}
