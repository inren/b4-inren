/**
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.bricket.b4.health.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.bricket.b4.core.entity.DomainObject;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * @author Ingo Renner
 *
 */
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(name = "healthsettings", uniqueConstraints = @UniqueConstraint(columnNames = "uid"))
@Data
@EqualsAndHashCode(callSuper = true)
public class HealthSettings extends DomainObject {
    
    @Column(nullable = false)
    private Long uid;

    @Column(name = "height")
    private Double height;

    @Column(name = "birthday")
    private Date birthday;

    @Column(name = "male")
    private Boolean male;
}
