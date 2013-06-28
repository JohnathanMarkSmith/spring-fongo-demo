###  Using Fongo and nosql-unit to test Spring-Data project with MongoDB, JUnit, Log4J


In this example I am going to show you how to test a Spring-Data MongoDB project with Fongo and nosql-unit.


### Test File with Data

we need to have some test data in Fongo for testing so I have setup the following two files:

five-person.json and two-person.json with test data. Now lets take a quick look at my repository.

### PersonRepository

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


as you can see my repository is not to hard to make and you should have a understanding of what it is doing.

### The Person Object

Now we are going to take a look at the object that we are going to be inserting into the database.

        @Document
        public class Person {

        @Id
        private String personId;

        private String name;
        private int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getPersonId() {
            return personId;
        }

        public void setPersonId(final String personId) {
            this.personId = personId;
        }

        public String getName() {
            return name;
        }

        public void setName(final String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(final int age) {
            this.age = age;
        }
        @Override
        public String toString() {
            return "Person [id=" + personId + ", name=" + name
                    + ", age=" + age +  "]";
        }

    }


You see we use the @Document and the @Id.

## The Test Class

Time for the testing to start

    @RunWith(SpringJUnit4ClassRunner.class)
    @ContextConfiguration
    public class PersonRepositoryTest {

        @Rule
        public MongoDbRule mongoDbRule = newMongoDbRule().defaultSpringMongoDb("demo-test");

        /**
         *
         *   nosql-unit requirement
         *
         */
        @Autowired private ApplicationContext applicationContext;

        @Autowired private PersonRepository personRepository;

        /**
         * Expected results are in "one-person.json" file
         */
        @Test
        @ShouldMatchDataSet(location = "/two-person.json")
        public void testInsertPersonWithNameJohnathanAndRandomAge(){
             this.personRepository.insertPersonWithNameJohnathan(35);
             this.personRepository.insertPersonWithNameJohnathan(67);
        }

        /**
         * Insert data from "five-person.json" and test countAllPersons method
         */
        @Test
        @UsingDataSet(locations = {"/five-person.json"}, loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
        public void testCountAllPersons(){
             long total = this.personRepository.countAllPersons();

             assertThat(total).isEqualTo(5);
        }

        /**
         * Insert data from "five-person.json" and test countUnderAge method
         */
        @Test
        @UsingDataSet(locations = {"/five-person.json"}, loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
        public void testCountUnderAge(){
             long total = this.personRepository.countUnderAge();

             assertThat(total).isEqualTo(3);
        }

        @Configuration
        @EnableMongoRepositories
        @ComponentScan(basePackageClasses = {PersonRepository.class})  // modified to not load configs from com.johnathanmarksmith.mongodb.example.MongoConfiguration
        @PropertySource("classpath:application.properties")
        static class PersonRepositoryTestConfiguration extends AbstractMongoConfiguration {

            @Override
            protected String getDatabaseName() {
                return "demo-test";
            }

            @Override
            public Mongo mongo() {
                // uses fongo for in-memory tests
                return new Fongo("mongo-test").getMongo();
            }

            @Override
            protected String getMappingBasePackage() {
                return "com.johnathanmarksmith.mongodb.example.domain";
            }

        }
    }



You can see from the above code how easy it is to test a Spring Data MongoDB project with Fongo.


### Download and The Source

You can checkout the project from github.

    git clone git@github.com:JohnathanMarkSmith/spring-fongo-demo.git
    cd spring-fongo-demo



If you have any questions please email me at john@johnathanmarksmith.com


Thanks, Johnathan Mark Smith
