package com.compomics.jtraml;

import com.compomics.jtraml.config.OboManager;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.log4j.Logger;

import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is a
 */

public class TestOboInput extends TestCase {

    private static Logger logger = Logger.getLogger(TestOboInput.class);

    /**
     * Create TestOboInput
     *
     * @param testName name of the test case
     */
    public TestOboInput(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(TestOboInput.class);
    }

    /**
     * Main test.
     */
    public void testMain() {
        Map<String, List<String>> lMSTerms = OboManager.getInstance().getMSTerm("m/z");
        Assert.assertEquals("MS:1000040", lMSTerms.get("id").get(0));

        Map<String, List<String>> lUOTerms = OboManager.getInstance().getUOTerm("dalton");
        Assert.assertEquals("UO:0000221", lUOTerms.get("id").get(0));
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        //
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }
}
