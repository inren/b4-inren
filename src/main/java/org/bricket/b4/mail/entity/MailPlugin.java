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
package org.bricket.b4.mail.entity;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import org.bricket.b4.core.entity.DomainObject;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * @author Ingo Renner
 * @author Henning Teek
 */
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
public class MailPlugin extends DomainObject {
    @OneToOne
    private Mailserver defaultMailserver;

    public void setDefaultMailserver(Mailserver defaultMailserver) {
        this.defaultMailserver = defaultMailserver;
    }

    public Mailserver getDefaultMailserver() {
        return defaultMailserver;
    }
}
