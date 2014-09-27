package com.taskroo.user.acceptance

import com.taskroo.testing.RunJetty
import groovyx.net.http.ContentType

@RunJetty
class UsersServiceSpec extends AcceptanceTestBase {

    def "should create new user in DB when new create request is send with first name, last name, email, username, and password"() {
        when: 'sending create new user account'
        def response = client.post(
                path: 'signup',
                body: [firstName: 'Test', lastName: 'User', email: 'username@example.com', username: TEST_USER_ID, password: 'secretPass'],
                headers: ['X-InvitationCode': 'youCanFindLotsOfInterestingStaffInTests'],
                requestContentType: ContentType.JSON)
        then: "response is 201"
        response.status == 201
        and: "new user gets created in the DB"
        usersCollection.count("{_id: '$TEST_USER_ID'}") == 1
    }

    def "should return bad request (400) when invalid email address given to create new user"() {
        when: 'sending create new user account with invalid email address value'
        def response = client.post(
                path: 'signup',
                body: [firstName: 'Test', lastName: 'User', email: 'invalidEmailAddress', username: TEST_USER_ID, password: 'secretPass'],
                headers: ['X-InvitationCode': 'youCanFindLotsOfInterestingStaffInTests'],
                requestContentType: ContentType.JSON)
        then: "response is 400"
        response.status == 400
    }

    def "should return bad request (400) when trying to create account for user with existing username/id"() {
        given: 'user with username "userName" exists'
        client.post(
                path: 'signup',
                body: [firstName: 'Test', lastName: 'User', email: 'username@example.com', username: TEST_USER_ID, password: 'secretPass'],
                headers: ['X-InvitationCode': 'youCanFindLotsOfInterestingStaffInTests'],
                requestContentType: ContentType.JSON)
        when: 'sending create new user account with the same username: "userName"'
        def response = client.post(
                path: 'signup',
                body: [firstName: 'Test2', lastName: 'User2', email: 'username2@example.com', username: TEST_USER_ID, password: 'secretPass2'],
                headers: ['X-InvitationCode': 'youCanFindLotsOfInterestingStaffInTests'],
                requestContentType: ContentType.JSON)
        then: "response is 400 with user object"
        response.status == 400
    }

    def "should return unauthorized (401) when X-InvitationCode with proper value is not given"() {
        when: 'sending create new user account'
        def response = client.post(
                path: 'signup',
                body: [firstName: 'Test', lastName: 'User', email: 'username@example.com', username: TEST_USER_ID, password: 'secretPass'],
                requestContentType: ContentType.JSON)
        then: "response is 401 with user object"
        response.status == 401
    }
}
