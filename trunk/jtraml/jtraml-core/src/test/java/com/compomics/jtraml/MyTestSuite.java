package com.compomics.jtraml;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.junit.Assert;

import java.io.File;
import java.net.URI;
import java.net.URL;

/**
 * This class wraps all test cases.
 */
public class MyTestSuite extends TestSuite {
// -------------------------- STATIC METHODS --------------------------

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        TestSuite lTestSuite = new TestSuite();

        lTestSuite.addTest(TestAgilentQQQ.suite());
        lTestSuite.addTest(TestThermoTSQ.suite());
        lTestSuite.addTest(TestABIQTRAP.suite());
        lTestSuite.addTest(TestOboInput.suite());


        return lTestSuite;
    }

    public static URI getTestResourceURI() {
        URI lTestResource = null;

        try {
            URL lAnchorString = ClassLoader.getSystemClassLoader().getResource("test.anchor.txt");
            File lAchorFile = new File(lAnchorString.getPath());
            lTestResource = lAchorFile.getParentFile().toURI();
        } catch (Exception e) {
            // Fail!!
            Assert.fail("Could not find test resources");
        }
        return lTestResource;
    }

// --------------------------- CONSTRUCTORS ---------------------------


    /**
     * Create TestSuite
     *
     * @param testName name of the test case
     */
    public MyTestSuite(String testName) {
        super(testName);
    }

// -------------------------- OTHER METHODS --------------------------
}
