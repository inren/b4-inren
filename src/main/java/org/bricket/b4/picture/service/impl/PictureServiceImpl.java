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
package org.bricket.b4.picture.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.bricket.b4.core.service.B4ServiceImpl;
import org.bricket.b4.picture.entity.Picture;
import org.bricket.b4.picture.repository.PictureRepository;
import org.bricket.b4.picture.service.PictureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Ingo Renner
 *
 */
@Service(value = "pictureService")
@Transactional(readOnly = true)
public class PictureServiceImpl extends B4ServiceImpl implements PictureService {
    private final Logger log = LoggerFactory.getLogger(PictureServiceImpl.class);

    @Resource
    private PictureRepository pictureRepository;

    @Override
    protected void onInit() {
        log.info("picture service initialized");
    }

    @Override
    @Transactional
    public Picture savePicture(Picture picture) {
        return pictureRepository.save(picture);
    }

    @Override
    @Transactional
    public void deletePicture(Picture picture) {
        pictureRepository.delete(pictureRepository.findOne(picture.getId()));

    }

    @Override
    public Picture loadPicture(Long id) {
        return pictureRepository.findOne(id);
    }

    @Override
    public List<Picture> loadAllPicture() {
        List<Picture> res = new ArrayList<Picture>();
        Iterator<Picture> iterator = pictureRepository.findAll().iterator();
        while (iterator.hasNext()) {
            res.add(iterator.next());
        }
        return res;
    }
}
