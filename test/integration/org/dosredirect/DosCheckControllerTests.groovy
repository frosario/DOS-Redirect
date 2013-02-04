package org.dosredirect
import static org.junit.Assert.*
import org.junit.*


class DosCheckControllerTests {
    DosCheckController dcc = new DosCheckController()

    @Before
    void setUp() {
        // Set up logic here
    }


    @After
    void tearDown() {
        // Tear down logic here
    }


    @Test
    void testIndex() {
        dcc.index()
        assertEquals 200, dcc.response.status
    }

/*
    void testIndexDosRedirect() {
        org.codehaus.groovy.grails.commons.ConfigurationHolder.config.antiDOS.maxRequestsInWindow = -1
        org.codehaus.groovy.grails.commons.ConfigurationHolder.config.antiDOS.timeWindowInSeconds = -1
        assertEquals 302, dcc.response.status
    }
*/
}
