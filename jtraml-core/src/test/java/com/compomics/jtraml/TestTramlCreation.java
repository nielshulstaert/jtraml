package com.compomics.jtraml;

import com.compomics.jtraml.factory.CVFactory;
import com.compomics.jtraml.interfaces.TSVFileExportModel;
import com.compomics.jtraml.model.TramlToThermo;
import com.google.common.io.Files;
import com.google.common.io.Resources;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.log4j.Logger;
import org.hupo.psi.ms.traml.ObjectFactory;
import org.hupo.psi.ms.traml.TraMLType;
import org.hupo.psi.ms.traml.TransitionType;
import org.systemsbiology.apps.tramlcreator.TraMLCreator;
import org.systemsbiology.apps.tramlparser.TraMLParser;
import org.systemsbiology.apps.tramlvalidator.TraMLValidator;
import psidev.psi.tools.validator.MessageLevel;
import psidev.psi.tools.validator.ValidatorMessage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.List;

/**
 * This class is a test scenario to generate a TraML file from an AgilentQQQ input file.
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
    public void createCustomTraml() {

        try {
            // create a Traml root instance
            ObjectFactory lObjectFactory = new ObjectFactory();
            TraMLType lTraMLType = lObjectFactory.createTraMLType();

            String line = "";

            // define the controlled vocabularies that can be used. (Mass Spectrometry Ontology, Unit Ontoloy, Unimod Ontology)
            lTraMLType.setCvList(CVFactory.getCvListType());




            // Ok, all rows have been added.
            TraMLCreator lTraMLCreator = new TraMLCreator();
            lTraMLCreator.setTraML(lTraMLType);

            File lOutputFile = new File("/Users/kennyhelsens/tmp/example.traml");

            BufferedWriter lWriter = Files.newWriter(lOutputFile, Charset.defaultCharset());

            lWriter.write(lTraMLCreator.asString());
            // Ok. The File should have been written!
            lWriter.flush();
            lWriter.close();

            // Now re-read the file.
            TraMLParser lTraMLParser = new TraMLParser();
            lTraMLParser.parse_file(lOutputFile.getAbsolutePath(), logger);

            int lSize = lTraMLParser.getTraML().getTransitionList().getTransition().size();

            // Now test whether we have actually parsed 'n' transitions.
            Assert.assertEquals(1941, lSize);


            // Now run the Validator.
            logger.debug("Running validator");
            File ontologyFile = new File(Resources.getResource("xml" + File.separator + "ontologies.xml").getFile());
            File mappingRules = new File(Resources.getResource("xml" + File.separator + "TraML-mapping.xml").getFile());
            File objectRules = new File(Resources.getResource("xml" + File.separator + "object_rules.xml").getFile());

            TraMLValidator validator = new TraMLValidator(
                    new FileInputStream(ontologyFile),
                    new FileInputStream(mappingRules),
                    new FileInputStream(objectRules)
            );

            TraMLType traml = lTraMLParser.getTraML();

            final Collection<ValidatorMessage> messages = validator.validate(traml);

            logger.debug("Validation run collected " + messages.size() + " message(s):");
            String errorMessage = "";
            for (ValidatorMessage message : messages) {
                if (message.getLevel().isHigher(MessageLevel.INFO)) {
                    errorMessage += message.getMessage() + "\n";
                } else {
                    logger.debug(message.getMessage());
                }
            }

            if (!errorMessage.equals("")) {
//                Assert.fail("The should not have been errors in the Validation!!");
                logger.debug("The should not have been errors in the Validation!!");
                logger.debug(errorMessage);
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }

    /**
     * Main test.
     */
    public void testTramlToThermo() {

        try {
            URL lURL = Resources.getResource("test.thermo.traml");
            iThermoTSQInputFile = new File(lURL.getFile());

            // Now re-read the file.
            TraMLParser lTraMLParser = new TraMLParser();
            lTraMLParser.parse_file(iThermoTSQInputFile.getCanonicalPath(), logger);

            TSVFileExportModel lTSVFileExportModel = new TramlToThermo();


            File lTempOutput = new File(MyTestSuite.getTestResourceURI().getPath(), "test.thermo.csv");
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
                    String lExpectedFirstLine = "651.8366,790.4038,25.5,18.61,28.61,NA,0,0,AAELQTGLETNR.2y7-1";
                    Assert.assertEquals(lExpectedFirstLine, line);
                }
            }
            // Asset the number of entries that must have been read.
            Assert.assertEquals(1942, lineCounter);
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
