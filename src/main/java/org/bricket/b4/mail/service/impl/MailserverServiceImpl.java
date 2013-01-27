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

import java.util.List;

import javax.annotation.Resource;

import org.bricket.b4.core.service.B4ServiceImpl;
import org.bricket.b4.mail.entity.Mailserver;
import org.bricket.b4.mail.repository.MailserverRepository;
import org.bricket.b4.mail.service.MailserverService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

/**
 * @author Ingo Renner
 * @author Henning Teek
 */
@Service(value = "mailserverService")
@Transactional(readOnly = true)
public class MailserverServiceImpl extends B4ServiceImpl implements MailserverService {
    private static final String DEFAULT_MAILSERVER_NAME = "default";

    private static final int DEFAULT_SMTP_PORT = 25;

    private final Logger log = LoggerFactory.getLogger(MailserverServiceImpl.class);

    @Resource
    private MailserverRepository mailserverDao;

    @Transactional
    @Override
    protected final void onInit() {
        if (mailserverDao.count() == 0) {
            Mailserver mailserver = new Mailserver();
            mailserver.setName(DEFAULT_MAILSERVER_NAME);
            mailserver.setHost("localhost");
            mailserver.setPort(DEFAULT_SMTP_PORT);
            mailserver = saveMailserver(mailserver);
            log.info("auto generated default mailserver created. " + mailserver.getHost() + ":" + mailserver.getPort());
        }
        log.info("mail service initialized");
    }

    @Override
    @Transactional
    public final void deleteMailserver(Mailserver mailserver) {
        mailserverDao.delete(mailserverDao.findOne(mailserver.getId()));
    }

    @Override
    public final List<Mailserver> loadAllMailserver() {
        return Lists.newArrayList(mailserverDao.findAll());
    }

    @Override
    public final Mailserver loadMailserver(Long id) {
        return mailserverDao.findOne(id);
    }

    @Override
    @Transactional
    public final Mailserver saveMailserver(Mailserver mailserver) {
        return mailserverDao.save(mailserver);
    }

    @Override
    public Mailserver loadMailserverByName(String name) {
        return mailserverDao.findByName(name);
    }
}
