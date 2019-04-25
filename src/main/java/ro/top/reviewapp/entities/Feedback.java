package ro.top.reviewapp.entities;

import java.sql.Date;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "feedbacks")
public class Feedback {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "structure", nullable = false)
	private int structureNote;
	
	@Column(name = "materials", nullable = false)
	private int materialsNote;
	
	@Column(name = "teaching", nullable = false)
	private int teachingNote;
	
	@Column(name = "practice", nullable = false)
	private int practiceNote;
	
	@Column(name = "experience", nullable = false)
	private int experienceNote;
	
	@Column(name = "student_name", nullable = true, length = 100)
	private String studentName;
	
	@Column(name = "review", nullable = true, length = 1000)
	private String feedback;
	
	@Column(name = "time_stamp", updatable = false)
	private Date timeStamp;

	

	public Feedback(int structureNote, int materialsNote, int teachingNote, int practiceNote, int experienceNote,
			String studentName, String feedback) {
		super();
		this.structureNote = structureNote;
		this.materialsNote = materialsNote;
		this.teachingNote = teachingNote;
		this.practiceNote = practiceNote;
		this.experienceNote = experienceNote;
		this.studentName = studentName.equals("") ? null : studentName;
		this.feedback = feedback.equals("") ? null : feedback;
		this.timeStamp = new Date(Calendar.getInstance().getTimeInMillis());
	}

	public Feedback() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFeedback() {
		return feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}
	
	public Date getTimeStamp() {
		return timeStamp;
	}
	
	

	public int getStructureNote() {
		return structureNote;
	}

	public void setStructureNote(int structureNote) {
		this.structureNote = structureNote;
	}

	public int getMaterialsNote() {
		return materialsNote;
	}

	public void setMaterialsNote(int materialsNote) {
		this.materialsNote = materialsNote;
	}

	public int getTeachingNote() {
		return teachingNote;
	}

	public void setTeachingNote(int teachingNote) {
		this.teachingNote = teachingNote;
	}

	public int getPracticeNote() {
		return practiceNote;
	}

	public void setPracticeNote(int practiceNote) {
		this.practiceNote = practiceNote;
	}

	public int getExperienceNote() {
		return experienceNote;
	}

	public void setExperienceNote(int experienceNote) {
		this.experienceNote = experienceNote;
	}
		
	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Feedback other = (Feedback) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "FeedBack [feedback=" + feedback + ", timeStamp=" + timeStamp + "]";
	}
	
	
}
