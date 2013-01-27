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
package org.bricket.b4.mail.service;

import org.bricket.b4.core.service.B4Service;
import org.bricket.b4.core.service.B4ServiceException;
import org.bricket.b4.mail.entity.MailPlugin;
import org.bricket.b4.security.entity.User;
import org.springframework.mail.SimpleMailMessage;

/**
 * @author Ingo Renner
 * @author Henning Teek
 */
public interface MailPluginService extends B4Service {
    MailPlugin saveMailPlugin(MailPlugin mailPlugin) throws B4ServiceException;

    MailPlugin loadMailPlugin();

    void sendSignupMail(User user) throws B4ServiceException;

    boolean send(SimpleMailMessage mailMsg) throws B4ServiceException;

    boolean send(String server, SimpleMailMessage mailMsg) throws B4ServiceException;

    void sendMail(String from, String to, String subject, String text) throws B4ServiceException;

    void sendMail(String server, String from, String to, String cc, String bcc, String subject, String text,
            boolean html) throws B4ServiceException;
}
