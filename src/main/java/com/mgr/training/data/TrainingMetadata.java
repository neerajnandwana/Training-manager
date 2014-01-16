package com.mgr.training.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "TRNG_META")
public class TrainingMetadata  implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@Column(name = "meta_id")
	private int id;
	@Column(name = "attendees_id")
	private String attendeesId;
	@Column(name = "attend")
	private boolean attend;
	@Column(name = "hrs_complete")
	private int hoursCompleted;
	@Column(name = "rating")
	private int rating;
	@Column(name = "feedback")
	private String feedback;
	@Column(name = "trainer_feedback")
	private String trainerFeedback;	
	
	@JsonIgnore
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "trng_id", nullable = false)
	private Training training;
	
	enum Rating {
		EXCELLENT(5), GOOD(4), SATISFACTORY(3), AVERAGE(2), POOR(1);

		private final int code;

		private Rating(int code) {
			this.code = code;
		}

		public int getCode() {
			return code;
		}
	}
	
	public String getAttendeesId() {
		return attendeesId;
	}
	public void setAttendeesId(String attendeesId) {
		this.attendeesId = attendeesId;
	}
	public boolean isAttend() {
		return attend;
	}
	public void setAttend(boolean attend) {
		this.attend = attend;
	}
	public int getHoursCompleted() {
		return hoursCompleted;
	}
	public void setHoursCompleted(int hoursCompleted) {
		this.hoursCompleted = hoursCompleted;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	public String getFeedback() {
		return feedback;
	}
	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}
	public String getTrainerFeedback() {
		return trainerFeedback;
	}
	public void setTrainerFeedback(String trainerFeedback) {
		this.trainerFeedback = trainerFeedback;
	}	
	public Training getTraining() {
		return training;
	}
	public void setTraining(Training training) {
		this.training = training;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
}
