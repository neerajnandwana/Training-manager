package com.mgr.training.data;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "ATTACH")
public class Attachment implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "attach_id")
	private String id;
	@Column(name = "attach_name")
	private String name;
	@Column(name = "attach_mimetype")
	private String mimeType;	

	@Column(name = "attach_author")
	private User author;
	@Column(name = "attach_created")
	private Date created;

	@Column(name = "attach_desc")
	private String desc;
	
	@Column(name = "attach_size")
	private int size;

	@JsonIgnore
	@Lob
	@Column(name = "attach_content", nullable = false)
	@Basic(fetch = FetchType.LAZY)
	private byte[] content;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.size = content.length;
		this.content = content;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}
	
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}	

	public int getSize() {
		return size;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Attachment [");
		if (id != null) {
			builder.append("id=");
			builder.append(id);
			builder.append(", ");
		}
		if (name != null) {
			builder.append("name=");
			builder.append(name);
			builder.append(", ");
		}
		if (mimeType != null) {
			builder.append("mimeType=");
			builder.append(mimeType);
			builder.append(", ");
		}
		if (author != null) {
			builder.append("author=");
			builder.append(author);
			builder.append(", ");
		}
		if (created != null) {
			builder.append("created=");
			builder.append(created);
			builder.append(", ");
		}
		if (desc != null) {
			builder.append("desc=");
			builder.append(desc);
			builder.append(", ");
		}
		builder.append("size=");
		builder.append(size);
		builder.append("]");
		return builder.toString();
	}
	
	

}
