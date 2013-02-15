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
package de.inren.frontend.common.dataprovider;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Easy to use dataprovider for PagingAndSortingRepository.
 * 
 * The repository must be injected by @SpringBean into the wicket component to avoid serializing problems.
 * 
 * @author Ingo Renner
 *
 */
public class RepositoryDataProvider <T extends Serializable> extends SortableDataProvider<T, String> implements Serializable {

    private PagingAndSortingRepository<T, ? extends Serializable> repository;
    
    public RepositoryDataProvider(PagingAndSortingRepository<T, ? extends Serializable> repository) {
        super();
        this.repository = repository;

    }

    @Override
    public Iterator<? extends T> iterator(long first, long count) {
        final Pageable pageable;
        long page;

        if (first > 0L && count != 0L) {
            page = first / count;
        } else {
            page = 0L;
        }

        if (getSort() == null || "".equals(getSort().getProperty().trim())) {
            pageable = new PageRequest((int)page, (int)count);
        } else {
            pageable = new PageRequest((int)page, (int)count, getSort().isAscending() ? Direction.ASC : Direction.DESC, getSort().getProperty());
        }
        List<T> res = new ArrayList<>();
        res.addAll(repository.findAll(pageable).getContent());
        return res.iterator();
    }

    @Override
    public long size() {
        return repository.count();
    }

    @Override
    public IModel<T> model(T object) {
        return new Model<T>(object);
    }
}
