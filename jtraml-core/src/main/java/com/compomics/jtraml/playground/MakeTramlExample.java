package com.compomics.jtraml.playground;

import com.compomics.jtraml.factory.CVFactory;
import com.google.common.io.Files;
import org.apache.log4j.Logger;
import org.hupo.psi.ms.traml.*;
import org.systemsbiology.apps.tramlcreator.TraMLCreator;

import java.io.BufferedWriter;
import java.io.File;
import java.nio.charset.Charset;

/**
 * This class is a
 */
public class MakeTramlExample {
    private static Logger logger = Logger.getLogger(MakeTramlExample.class);
    /**
     * Main constructor.
     */
    public MakeTramlExample() {

        try {

            // create a Factory instance to create TraML types
            ObjectFactory lObjectFactory = new ObjectFactory();

            // make the root TraML element
            TraMLType lTraMLType = lObjectFactory.createTraMLType();
            lTraMLType.setTransitionList(lObjectFactory.createTransitionListType());

            // define the controlled vocabularies that can be used. (Mass Spectrometry Ontology, Unit Ontoloy, Unimod Ontology)
            lTraMLType.setCvList(CVFactory.getCvListType());

            // Define a single transition identififier.
            String lID = "example_1.TESTPEPTIDE";//

            // Create the cvparams fromt the above properties
            CvParamType lCV_Q1 = CVFactory.createCVType_MZ("1000.00");
            CvParamType lCV_Q3 = CVFactory.createCVType_MZ("200.00");
            CvParamType lCV_CollisionEnergy = CVFactory.createCVType_CollisionEnergy("30.00");
            CvParamType lCV_RetentionTime = CVFactory.createCVType_RetentionTime("20.00");

            // Make the different Type instances
            PrecursorType lPrecursorType = lObjectFactory.createPrecursorType();
            ProductType lProductType = lObjectFactory.createProductType();
            RetentionTimeType lRetentionTimeType = lObjectFactory.createRetentionTimeType();

            ConfigurationListType lConfigurationListType = lObjectFactory.createConfigurationListType();
            ConfigurationType lConfigurationType = lObjectFactory.createConfigurationType();
            lConfigurationListType.getConfiguration().add(lConfigurationType);

            // Add the cvparams
            lPrecursorType.getCvParam().add(lCV_Q1);
            lProductType.getCvParam().add(lCV_Q3);
            lConfigurationType.getCvParam().add(lCV_CollisionEnergy);
            lRetentionTimeType.getCvParam().add(lCV_RetentionTime);

            // add this configuration to the configuration list.
            TransitionType lTransitionType = lObjectFactory.createTransitionType();
            lTransitionType.setId(lID);
            lTransitionType.setPrecursor(lPrecursorType);
            lTransitionType.setProduct(lProductType);
            lTransitionType.setRetentionTime(lRetentionTimeType);

            // Ok, all rows have been added.
            lTraMLType.getTransitionList().getTransition().add(lTransitionType);


            TraMLCreator lTraMLCreator = new TraMLCreator();
            lTraMLCreator.setTraML(lTraMLType);

            File lOutputFile = new File("/Users/compomics/tmp/example.traml");

            BufferedWriter lWriter = Files.newWriter(lOutputFile, Charset.defaultCharset());
            lWriter.write(lTraMLCreator.asString());

            // Ok. The File should have been written!
            lWriter.flush();
            lWriter.close();


        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    public static void main(String[] args) {
        new MakeTramlExample();
    }
}
