package org.bricket.b4.health.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Ingo Renner
 *
 */
@Entity
@Table(name = "measurement")
public class Measurement implements Serializable {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;

	@Column(name = "date")
	private Date date;

	@Column(name = "weight")
	private long weight;

	@Column(name = "fat")
	private long fat;

	@Column(name = "water")
	private long water;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public long getWeight() {
		return weight;
	}

	public void setWeight(long weight) {
		this.weight = weight;
	}

	public long getFat() {
		return fat;
	}

	public void setFat(long fat) {
		this.fat = fat;
	}

	public long getWater() {
		return water;
	}

	public void setWater(long water) {
		this.water = water;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Measurement [id=").append(id).append(", date=")
				.append(date).append(", weight=").append(weight)
				.append(", fat=").append(fat).append(", water=").append(water)
				.append("]");
		return builder.toString();
	}
}
