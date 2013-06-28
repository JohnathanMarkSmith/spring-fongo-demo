package com.johnathanmarksmith.mongodb.example;

import com.mongodb.Mongo;
import com.mongodb.ServerAddress;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.ArrayList;


/**
 * Date:   5/24/13 / 8:05 AM
 * Author: Johnathan Mark Smith
 * Email:  john@johnathanmarksmith.com
 * <p/>
 * Comments:
 * <p/>
 * This is a example on how to setup a database with Spring's Java Configuration (JavaConfig) style.
 * <p/>
 * As you can see from the code below this is easy and a lot better then using the old style of XML files.
 * <p/>
 * T
 */

@Configuration
@EnableMongoRepositories
@ComponentScan(basePackageClasses = {MongoDBApp.class})
@PropertySource("classpath:application.properties")
public class MongoConfiguration extends AbstractMongoConfiguration {


    @Override
    protected String getDatabaseName() {
        return "demo";
    }



    @Override
    public Mongo mongo() throws Exception {
        /**
         *
         * this is for a single db
         */

        // return new Mongo();


        /**
         *
         * This is for a relset of db's
         */

        return new Mongo(new ArrayList<ServerAddress>() {{ add(new ServerAddress("127.0.0.1", 27017)); add(new ServerAddress("127.0.0.1", 27027)); add(new ServerAddress("127.0.0.1", 27037)); }});

    }

    @Override
    protected String getMappingBasePackage() {
        return "com.johnathanmarksmith.mongodb.example.domain";
    }

}
