package com.compomics.jtraml;

import com.compomics.jtraml.interfaces.TSVFileExportModel;
import com.compomics.jtraml.model.TramlToThermo;
import com.google.common.io.Files;
import com.google.common.io.Resources;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.log4j.Logger;
import org.hupo.psi.ms.traml.TraMLType;
import org.hupo.psi.ms.traml.TransitionType;
import org.systemsbiology.apps.tramlparser.TraMLParser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

/**
 * This class is a test scenario to generate a TraML file from an AgilentQQQ
 * input file.
 */
public class TestTramlCreation extends TestCase {

    private static Logger logger = Logger.getLogger(TestTramlCreation.class);

    private File iThermoTSQInputFile;

    /**
     * Create TestAgilentQQQ
     *
     * @param testName name of the test case
     */
    public TestTramlCreation(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(TestTramlCreation.class);
    }

    /**
     * Main test.
     */
    public void testTramlToThermo() {

        try {
            URL lURL = Resources.getResource("ToyExample1.TraML");
            iThermoTSQInputFile = new File(lURL.getFile());

            // Now re-read the file.
            TraMLParser lTraMLParser = new TraMLParser();
            lTraMLParser.parse_file(iThermoTSQInputFile.getCanonicalPath(), logger);

            TSVFileExportModel lTSVFileExportModel = new TramlToThermo();

            File lTempOutput = new File(MyTestSuite.getTestResourceURI().getPath(), "ToyExample1.csv");
            if (lTempOutput.exists()) {
                lTempOutput.delete();
            }
            BufferedWriter lWriter = Files.newWriter(lTempOutput, Charset.defaultCharset());

            if (lTSVFileExportModel.hasHeader()) {
                lWriter.write(lTSVFileExportModel.getHeader());
                lWriter.write("\n");
            }

            TraMLType lTraML = lTraMLParser.getTraML();
            List<TransitionType> lTransitionTypeList = lTraML.getTransitionList().getTransition();
            for (TransitionType lTransitionType : lTransitionTypeList) {
                String line = lTSVFileExportModel.parseTransitionType(lTransitionType, lTraML);
                lWriter.write(line);
                lWriter.write("\n");
            }

            // Ok. The File should have been written!
            lWriter.flush();
            lWriter.close();

            // Ok, now re-read the file.
            BufferedReader lBufferedReader = Files.newReader(lTempOutput, Charset.defaultCharset());
            String line = "";
            int lineCounter = 0;
            while ((line = lBufferedReader.readLine()) != null) {
                lineCounter++;
                // hard coded test!
                if (lineCounter == 2) {
                    String lExpectedFirstLine = "862.9467,1040.57,26,NA,NA,NA,0,0,ADTHFLLNIYDQLR-M1-T1";
                    Assert.assertEquals(lExpectedFirstLine, line);
                }
            }
            // Asset the number of entries that must have been read.
            Assert.assertEquals(3, lineCounter);
            lBufferedReader.close();

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }
}
