package com.johnathanmarksmith.mongodb.example.repository;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.johnathanmarksmith.mongodb.example.domain.Person;


/**
 * Date:   6/26/13 / 1:22 PM
 * Author: Johnathan Mark Smith
 * Email:  john@johnathanmarksmith.com
 * <p/>
 * Comments:
 * <p/>
 * This is my Person Repository
 */


@Repository
public class PersonRepository {

    static final Logger logger = LoggerFactory.getLogger(PersonRepository.class);

    @Autowired
    MongoTemplate mongoTemplate;

    public long countUnderAge() {
        List<Person> results = null;

        Query query = new Query();
        Criteria criteria = new Criteria();
        criteria = criteria.and("age").lte(21);

        query.addCriteria(criteria);
        //results = mongoTemplate.find(query, Person.class);
        long count = this.mongoTemplate.count(query, Person.class);
        
        logger.info("Total number of under age in database: {}", count);
        return count;
    }

    /**
     * This will count how many Person Objects I have
     */
    public long countAllPersons() {
    	// findAll().size() approach is very inefficient, since it returns the whole documents
    	// List<Person> results = mongoTemplate.findAll(Person.class);
        
    	long total = this.mongoTemplate.count(null, Person.class);
        logger.info("Total number in database: {}", total);
        
        return total;
    }

    /**
     * This will install a new Person object with my
     * name and random age
     */
    public void insertPersonWithNameJohnathan(double age) {
        Person p = new Person("Johnathan", (int) age);

        mongoTemplate.insert(p);
    }

    /**
     * this will create a {@link Person} collection if the collection does not already exists
     */
    public void createPersonCollection() {
        if (!mongoTemplate.collectionExists(Person.class)) {
            mongoTemplate.createCollection(Person.class);
        }
    }

    /**
     * this will drop the {@link Person} collection if the collection does already exists
     */
    public void dropPersonCollection() {
        if (mongoTemplate.collectionExists(Person.class)) {
            mongoTemplate.dropCollection(Person.class);
        }
    }
}
