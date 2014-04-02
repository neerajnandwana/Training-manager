package com.mgr.training.data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.google.common.collect.Lists;

@Entity
@Table(name = "TRNG")
public class Training  implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "id")
	private String trainingId;
	@Column(name = "trng_title", unique = true, nullable = false)
	private String trainingTitle;
	@Column(name = "trng_desc")
	private String description;
	@Column(name = "trng_mode")
	private String mode;
	@Column(name = "trng_kind")
	private String kind;
	@Column(name = "trng_num_hrs")
	private int numberOfHours;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_on", nullable = false)
	private Date createdOn;
	@Column(name = "start_date")
	private Date trainingStartDate;
	@Column(name = "end_date")
	private Date trainingEndDate;
	@Column(name = "level_allowed")
	private String levelAllowed;
	
	@ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	private List<Employee> trainerIds = Lists.newArrayList();
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<TrainingMetadata> meta = Lists.newArrayList();
	
	@PrePersist 
	protected void onCreate(){
		createdOn = new Date();
	}
	
	enum Kind {
		ANALYSIS("Analysis"), 
		BUSINESS_AWARENESS("Business Awareness"), 
		FINANCE("Finance"), 
		SOFT_SKILLS("Soft Skills"), 
		SYSTEMS_AND_TOOLS("System and Tools");
		
		private final String name;
		private Kind(String name){
			this.name = name;
		}
		public String toString(){
			return name;
		}
	}

	enum Mode {
		CLASS_ROOM("Class Room"), // trainer and and trainee are in common place
		E_LEARNING("E Learning"), // online training, without any help from trainer
		VIRTUAL("Vertual"); // via teleconference, web conference with help of trainer
		
		private final String name;
		private Mode(String name){
			this.name = name;
		}
		public String toString(){
			return name;
		}
	}

	public String getTrainingId() {
		return trainingId;
	}

	public void setTrainingId(String trainingId) {
		this.trainingId = trainingId;
	}

	public String getTrainingTitle() {
		return trainingTitle;
	}

	public void setTrainingTitle(String trainingTitle) {
		this.trainingTitle = trainingTitle;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public int getNumberOfHours() {
		return numberOfHours;
	}

	public void setNumberOfHours(int numberOfHours) {
		this.numberOfHours = numberOfHours;
	}

	public Date getTrainingStartDate() {
		return trainingStartDate;
	}

	public void setTrainingStartDate(Date trainingStartDate) {
		this.trainingStartDate = trainingStartDate;
	}

	public Date getTrainingEndDate() {
		return trainingEndDate;
	}

	public void setTrainingEndDate(Date trainingEndDate) {
		this.trainingEndDate = trainingEndDate;
	}

	public String getLevelAllowed() {
		return levelAllowed;
	}

	public void setLevelAllowed(String levelAllowed) {
		this.levelAllowed = levelAllowed;
	}

	public List<Employee> getTrainerIds() {
		return trainerIds;
	}

	public void setTrainerIds(List<Employee> trainerIds) {
		this.trainerIds = trainerIds;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public List<TrainingMetadata> getMeta() {
		return meta;
	}

	public void setMeta(List<TrainingMetadata> metas) {
		getMeta().clear();
		for(TrainingMetadata meta: metas){
			addAttendedMetadata(meta);
		}
	}

	public TrainingMetadata addAttendedMetadata(TrainingMetadata trainingMeta) {
		getMeta().add(trainingMeta);
		trainingMeta.setTraining(this);
		return trainingMeta;
	}

	public TrainingMetadata removeAttendedMetadata(TrainingMetadata trainingMeta) {
		getMeta().remove(trainingMeta);
		trainingMeta.setTraining(null);
		return trainingMeta;
	}
}
