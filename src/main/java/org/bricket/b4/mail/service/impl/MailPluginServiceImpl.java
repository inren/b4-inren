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
package org.bricket.b4.mail.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.velocity.app.VelocityEngine;
import org.bricket.b4.core.service.B4ServiceException;
import org.bricket.b4.core.service.B4ServiceImpl;
import org.bricket.b4.mail.entity.MailPlugin;
import org.bricket.b4.mail.entity.Mailserver;
import org.bricket.b4.mail.repository.MailPluginRepository;
import org.bricket.b4.mail.service.MailPluginService;
import org.bricket.b4.mail.service.MailServiceException;
import org.bricket.b4.mail.service.MailserverService;
import org.bricket.b4.securityinren.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.velocity.VelocityEngineUtils;

/**
 * @author Ingo Renner
 * @author Henning Teek
 */
@Service(value = "mailPluginService")
@Transactional(readOnly = true)
public class MailPluginServiceImpl extends B4ServiceImpl implements MailPluginService {
    private final Logger log = LoggerFactory.getLogger(MailPluginServiceImpl.class);

    @Autowired
    private MailserverService mailserverService;

    @Autowired
    private VelocityEngine velocityEngine;

    @Resource
    private MailPluginRepository mailPluginDao;

    private Map<String, JavaMailSenderImpl> mailSenders = new HashMap<String, JavaMailSenderImpl>();

    @Transactional
    @Override
    protected final void onInit() throws B4ServiceException {
        mailserverService.init();

        if (mailPluginDao.count() == 0) {
            List<Mailserver> ms = mailserverService.loadAllMailserver();
            if (!ms.isEmpty()) {
                MailPlugin mailPlugin = new MailPlugin();
                mailPlugin.setDefaultMailserver(ms.get(0));
                mailPlugin = saveMailPlugin(mailPlugin);
                log.info("auto generated default mail plugin created. " + mailPlugin.getDefaultMailserver().toString());
            }
        }

        log.info("mail plugin service initialized");
    }

    @Override
    @Transactional
    public MailPlugin saveMailPlugin(MailPlugin mailPlugin) {
        return mailPluginDao.save(mailPlugin);
    }

    @Override
    public MailPlugin loadMailPlugin() {
        return mailPluginDao.findAll().iterator().next();
    }

    @Override
    public final void sendSignupMail(final User user) throws B4ServiceException {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("user", user);
        String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "org/bricket/b4/mail/service/signup.vm", model);
        sendMail("webmaster@bricket.org", user.getEmail(), "Account activation change configs", text);
    }

    @Override
    public final void sendMail(final String from, final String to, final String subject, final String text) throws B4ServiceException {
        sendMail(null, from, to, null, null, subject, text, true);
    }

    @Override
    public final void sendMail(final String mailserver, final String from, final String to, final String cc, final String bcc, final String subject, final String text, final boolean html)
            throws B4ServiceException {
        MimeMessagePreparator preparator = new MailMimeMessagePreparator(bcc, subject, text, to, cc, from, html);

        try {
            if (mailserver != null) {
                getMailSender(mailserverService.loadMailserverByName(mailserver)).send(preparator);
            } else {
                getMailSender(loadMailPlugin().getDefaultMailserver()).send(preparator);
            }
        } catch (MailException me) {
            throw new MailServiceException(MailServiceException.SERVER_NOT_FOUND, me);
        }
    }

    private JavaMailSenderImpl getMailSender(Mailserver mailserver) throws B4ServiceException {
        if (mailserver == null) {
            throw new MailServiceException(MailServiceException.SERVER_NOT_FOUND);
        }

        if (!mailSenders.containsKey(mailserver.getName())) {
            JavaMailSenderImpl mailsender = new JavaMailSenderImpl();
            mailsender.setHost(mailserver.getHost());
            if (mailserver.getPort() > 0) {
                mailsender.setPort(mailserver.getPort());
            }
            if (mailserver.getUsername() != null) {
                mailsender.setUsername(mailserver.getUsername());
            }
            if (mailserver.getPassword() != null) {
                mailsender.setUsername(mailserver.getPassword());
            }
            mailSenders.put(mailserver.getName(), mailsender);
        }
        return mailSenders.get(mailserver.getName());
    }

    public final boolean send(SimpleMailMessage mailMsg) throws B4ServiceException {
        return send(null, mailMsg);
    }

    public final boolean send(String server, SimpleMailMessage mailMsg) throws B4ServiceException {
        JavaMailSenderImpl ms = server != null ? getMailSender(mailserverService.loadMailserverByName(server)) : getMailSender(loadMailPlugin().getDefaultMailserver());
        try {
            ms.send(mailMsg);
        } catch (Exception me) {
            log.error("error sending mail", me);
            throw new MailServiceException(MailServiceException.UNKNOWN_ERROR, me);
        }
        return true;
    }

    private static final class MailMimeMessagePreparator implements MimeMessagePreparator {
        private final String bcc;
        private final String subject;
        private final String text;
        private final String to;
        private final String cc;
        private final String from;
        private final boolean html;

        private MailMimeMessagePreparator(String bcc, String subject, String text, String to, String cc, String from, boolean html) {
            this.bcc = bcc;
            this.subject = subject;
            this.text = text;
            this.to = to;
            this.cc = cc;
            this.from = from;
            this.html = html;
        }

        public void prepare(MimeMessage mimeMessage) throws MessagingException {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
            message.setFrom(from);
            message.setTo(to);
            if (cc != null) {
                message.setCc(cc);
            }
            if (bcc != null) {
                message.setBcc(bcc);
            }
            message.setSubject(subject);
            message.setText(text, html);
        }
    }
}
