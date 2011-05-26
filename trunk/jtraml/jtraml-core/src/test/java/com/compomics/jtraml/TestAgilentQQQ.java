package com.compomics.jtraml;

import com.compomics.jtraml.config.CoreConfiguration;
import com.compomics.jtraml.factory.CVFactory;
import com.compomics.jtraml.interfaces.FileModel;
import com.compomics.jtraml.model.rowmodel.AgilentQQQImpl;
import com.google.common.io.Files;
import com.google.common.io.Resources;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.log4j.Logger;
import org.hupo.psi.ms.traml.ObjectFactory;
import org.hupo.psi.ms.traml.TraMLType;
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

/**
 * This class is a test scenario to generate a TraML file from an AgilentQQQ input file.
 */

public class TestAgilentQQQ extends TestCase {

    private static Logger logger = Logger.getLogger(TestAgilentQQQ.class);

    private File iAgilentInputFile;

    /**
     * Create TestAgilentQQQ
     *
     * @param testName name of the test case
     */
    public TestAgilentQQQ(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(TestAgilentQQQ.class);
    }

    /**
     * Main test.
     */
    public void testMain() {

        try {
            URL lURL = Resources.getResource("AgilentQQQ_example.tsv");
            iAgilentInputFile = new File(lURL.getFile());

            BufferedReader br = Files.newReader(iAgilentInputFile, Charset.defaultCharset());

            ObjectFactory lObjectFactory = new ObjectFactory();
            TraMLType lTraMLType = lObjectFactory.createTraMLType();

            String line = "";

            // skip the first two lines.
            br.readLine();
            br.readLine();

            String sep = CoreConfiguration.getSeparationChar();
            FileModel lFileModel = new AgilentQQQImpl(iAgilentInputFile);

            logger.debug("reading AgilentQQQ input file\t" + lURL);

            while ((line = br.readLine()) != null) {
                String[] lValues = line.split(sep);
                lFileModel.addRowToTraml(lTraMLType, lValues);
            }
            logger.debug("finished reading AgilentQQQ input file\t");

            lTraMLType.setCvList(CVFactory.getCvListType());
            lTraMLType.setSourceFileList(lFileModel.getSourceTypeList());

            // Ok, all rows have been added.
            TraMLCreator lTraMLCreator = new TraMLCreator();
            lTraMLCreator.setTraML(lTraMLType);

            File lTempOutput = new File(MyTestSuite.getTestResourceURI().getPath(), "test.agilent.traml");

            if (lTempOutput.exists()) {
                lTempOutput.delete();
            }

            BufferedWriter lWriter = Files.newWriter(lTempOutput, Charset.defaultCharset());

            lWriter.write(lTraMLCreator.asString());
            // Ok. The File should have been written!
            lWriter.flush();
            lWriter.close();


            // Now re-read the file.
            TraMLParser lTraMLParser = new TraMLParser();
            lTraMLParser.parse_file(lTempOutput.getAbsolutePath(), logger);

            int lSize = lTraMLParser.getTraML().getTransitionList().getTransition().size();

            // Now test whether we have actually parsed 'n' transitions.
            Assert.assertEquals(1354, lSize);


            // Now run the Validator.

            logger.debug("Running validator");
            File ontologyFile = new File(Resources.getResource("xml" + File.separator + "ontologies.xml").getFile());
            File mappingRules = new File(Resources.getResource("xml" + File.separator + "TraML-mapping.xml").getFile());
            File objectRules = new File(Resources.getResource("xml" + File.separator + "object_rules.xml").getFile());

            TraMLValidator validator = new TraMLValidator(new FileInputStream(ontologyFile),
                    new FileInputStream(mappingRules),
                    new FileInputStream(objectRules));

            TraMLType traml = lTraMLParser.getTraML();

            final Collection<ValidatorMessage> messages = validator.validate(traml);

            logger.debug("Validation run collected " + messages.size() + " message(s):");
            String errorMessage = "";
            for (ValidatorMessage message : messages) {
                if (message.getLevel().isHigher(MessageLevel.INFO)) {
                    errorMessage += message.getMessage() + "\n";
                }else{
                    logger.debug(message.getMessage());
                }
            }

            if(!errorMessage.equals("")){
                Assert.fail("The should not have been errors in the Validation!!");
                logger.debug(errorMessage);
            }

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
