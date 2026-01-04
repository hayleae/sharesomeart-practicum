package com.example.demo3.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name="posts")
public class Post{
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	private Long userId;
	private String title;
	private String description;
	private String tag;
	@Lob
	@Column(columnDefinition = "VARCHAR(MAX)")
	private String image;
	private String author;
	private LocalDateTime date;
	private Boolean hidden;

	public Post() {
		userId = null;
		title = "";
		description = "";
		tag = "";
		image = null;
		author = null;
		date = LocalDateTime.now();
		hidden = false;
	}
	
	public Post(String title, String description, String tag, String image) {
		this.userId = null;
		this.title = title;
		this.description = description;
		this.tag = tag;
		this.image = image;
		this.date = LocalDateTime.now();
		this.hidden = false;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String desc) {
		description = desc;
	}
	
	public String getTag() {
		return tag;
	}
	
	public void setTag(String tag) {
		this.tag = tag;	
	}

	public String getImage(){
		return image;
	}

	public void setImage(String image){
		this.image = image;
	}

	public Long getUserId(){
		return userId;
	}

	public void setUserId(Long id){
		this.userId = id;
	}

		public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDate() {
		DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yy HH:mm:ss");
		String formatted = date.format(format);
		return formatted;
	}

	public void setDate() {
		this.date = LocalDateTime.now();
	}

	public Boolean getHidden(){
		return this.hidden;
	}

	public void setHidden(){
		if (this.hidden == false){
			this.hidden = true;
		} else {
			this.hidden = false;
		}
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
	
}
