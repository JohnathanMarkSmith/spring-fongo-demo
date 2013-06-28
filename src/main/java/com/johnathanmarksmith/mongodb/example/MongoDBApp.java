package com.johnathanmarksmith.mongodb.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Date:   6/28/13 / 10:40 AM
 * Author: Johnathan Mark Smith
 * Email:  john@johnathanmarksmith.com
 * <p/>
 * Comments:
 *  This main really does not have to be here but I just wanted to add something for the demo..
 *
 */


public class MongoDBApp {

    static final Logger logger = LoggerFactory.getLogger(MongoDBApp.class);

    public static void main(String[] args) {
        logger.info("Fongo Demo application");

        ApplicationContext context = new AnnotationConfigApplicationContext(MongoConfiguration.class);




        logger.info("Fongo Demo application");
    }
}
