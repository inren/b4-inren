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
package org.bricket.b4.health.service.impl;

import java.io.Serializable;

/**
 * @author Ingo Renner
 *
 */
public class UidMapping implements  Serializable {
    
    private Long id;
    
    private String email;
    
    public UidMapping() {
        super();
    }
    
    public UidMapping(Long id, String email) {
        super();
        this.id = id;
        this.email = email;
    }

    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "UidMapping [id=" + id + ", email=" + email + "]";
    }
}
