package com.taskroo.user.acceptance
import com.mongodb.DB
import com.taskroo.mongo.MongoConnector
import groovyx.net.http.RESTClient
import org.jongo.Jongo
import org.jongo.MongoCollection
import spock.lang.Specification

abstract class AcceptanceTestBase extends Specification {

    static RESTClient client = new RESTClient('http://localhost:8080/')
    private static final DB db = new MongoConnector(System.properties.getProperty('MONGO_PORT_27017_TCP_ADDR', 'localhost'),
            System.properties.getProperty('MONGO_PORT_27017_TCP_PORT', '27017')).getDatabase('taskroo')
    private static final Jongo jongo = new Jongo(db);

    protected static final MongoCollection usersCollection = jongo.getCollection('users')
    protected static final String TEST_USER_ID = 'userName'


    def setupSpec() {
        client.handler.failure = { it }
        cleanup()
    }

    void cleanup() {
        usersCollection.remove("{_id: '$TEST_USER_ID'}")
    }
}
