package com.sungung;

import com.sungung.data.model.Brewer;
import com.sungung.data.service.BrewerRepository;
import com.sungung.data.service.BrewerSearchCriteria;
import com.sungung.data.service.BrewerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * @author PARK Sungung
 * @since 0.0.1
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class DataServiceTests {

    @Autowired
    BrewerRepository brewerRepository;

    @Autowired
    BrewerService brewerService;

    @Test
    public void repositoryIsUpAndRunningTest() {

        Brewer brewer = brewerRepository.findById(1L);
        assertNotNull(brewer);

    }

    @Test(expected = IllegalArgumentException.class)
    public void searchCriteriaNullTest() {
        brewerService.findBrewers(null, null);
    }

    @Test
    public void brewerServiceSearchTest() {

        BrewerSearchCriteria criteria = new BrewerSearchCriteria("Arctic Fox Brewing");
        Page<Brewer> rs = brewerService.findBrewers(criteria, null);
        assertTrue(rs.hasContent());

    }

    @Test
    public void brewerServicePageTest() {

        int page = 0;
        int pageSize = 5;
        BrewerSearchCriteria criteria = new BrewerSearchCriteria();
        Pageable pageRequest = new PageRequest(page, pageSize);

        Page<Brewer> rs = brewerService.findBrewers(criteria, pageRequest);
        assertEquals(rs.getSize(), pageSize);
        // default sort must be ascending
        assertTrue(rs.getContent().get(0).getName().compareTo(rs.getContent().get(1).getName()) < 0);

    }

    @Test
    public void brewerServiceSortTest() {

        int page = 0;
        int pageSize = 5;
        Sort sort = new Sort(Sort.Direction.DESC, "name");
        BrewerSearchCriteria criteria = new BrewerSearchCriteria();
        Pageable pageRequest = new PageRequest(page, pageSize, sort);

        Page<Brewer> rs = brewerService.findBrewers(criteria, pageRequest);
        assertEquals(rs.getSize(), pageSize);
        assertTrue(rs.getContent().get(0).getName().compareTo(rs.getContent().get(1).getName()) > 0);

    }


}
