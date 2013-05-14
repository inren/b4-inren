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
package org.bricket.b4.securityinren.service;

import java.io.File;

import org.bricket.b4.core.service.B4Service;
import org.bricket.b4.core.service.B4ServiceException;

/**
 * @author Ingo Renner
 *
 */
public interface UsersXmlBackupRestoreService extends B4Service {

    String dumpDbToXml() throws B4ServiceException;
    
    void restoreFromXmlFile(File file) throws B4ServiceException;

}
