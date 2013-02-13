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
package org.bricket.b4.securityinren.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import org.bricket.b4.core.entity.DomainObject;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * @author Henning Teek
 * @author Ingo Renner
 * 
 */
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(name = "b4_User", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
@Data
@ToString(exclude = { "password" })
@EqualsAndHashCode(callSuper = true, exclude = "groups")
public class User extends DomainObject {
    @Column(nullable = false)
    private String email;

    private String password;

    private String firstname;

    private String lastname;

    private Boolean enabled;

    private String activationKey;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "b4_User_Role", joinColumns = { @JoinColumn(name = "userId", referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(name = "roleId", referencedColumnName = "id") })
    private Set<Role> roles;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "b4_User_Group", joinColumns = { @JoinColumn(name = "userId", referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(name = "groupId", referencedColumnName = "id") })
    private Set<Group> groups;

}
