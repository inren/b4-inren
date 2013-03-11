package org.bricket.b4.health.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * @author Ingo Renner
 *
 */
@Entity
@Table(name = "measurement")
@Data
public class Measurement implements Serializable {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;

        @Column(name = "uid")
        private long uid;

        @Column(name = "date")
	private Date date;

	@Column(name = "weight")
	private long weight;

	@Column(name = "fat")
	private long fat;

	@Column(name = "water")
	private long water;
}
