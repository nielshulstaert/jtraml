package com.compomics.jtraml.model;

import com.compomics.jtraml.config.CoreConfiguration;
import com.compomics.jtraml.exception.JTramlException;
import com.compomics.jtraml.factory.CVFactory;
import com.compomics.jtraml.interfaces.TSVFileImportModel;
import org.apache.log4j.Logger;
import org.hupo.psi.ms.traml.*;

import javax.xml.rpc.ServiceException;
import java.io.File;
import java.rmi.RemoteException;
import java.util.List;

/**
 * This class is an TSVFileImportModel implementation for the Thermo TSQ instrument.
 * e.g.
 * ""
 * Q1,Q3,CE,Start time (min),Stop time (min),Polarity,Trigger,Reaction category,Name
 * 651.8366,790.4038,25.5,18.61,28.61,1,1.00E+04,0,AAELQTGLETNR.2y7-1
 *
 */
public class ThermoToTraml implements TSVFileImportModel {

    private static Logger logger = Logger.getLogger(ThermoToTraml.class);

    private ObjectFactory iObjectFactory;
    private File iFile;

    /**
     * Construct a new TSVFileImportModel implementation for Thermo TSQ inputformat.
     */
    public ThermoToTraml(File aFile) {
        iFile = aFile;
        iObjectFactory = new ObjectFactory();
    }

    /**
     * Implementing classes must be capable of writing an array of rowvalues into a TramlType instance.
     *
     * @param aTraMLType The TraMLType instance to store the rows into.
     * @param aRowValues The separates values from a single row.
     */
    public void addRowToTraml(TraMLType aTraMLType, String[] aRowValues) {

        // validate number of line values.
        if (aRowValues.length != 9) {
            throw new JTramlException("Unexpected numer of columns for the Thermo TSQ TSVFileImportModel!!");
        }

        if (aTraMLType.getTransitionList() == null) {
            aTraMLType.setTransitionList(iObjectFactory.createTransitionListType());
        }

        String lQ1 = aRowValues[0];//OK
        String lQ3 = aRowValues[1];//OK

        String lEnergy = aRowValues[2];//OK

        String lStartTime = aRowValues[3];//OK
        String lStopTime = aRowValues[4];//OK

        String lPolarity = aRowValues[5];
        String lTrigger = aRowValues[6];

        String lReactionCategory = aRowValues[7];

        String lID = aRowValues[8];// OK


        // <cvParam cvRef="MS" accession="MS:1000827" name="isolation window target m/z" value="862.9467"
        // unitCvRef="MS" unitAccession="MS:1000040" unitName="m/z"/>
        try {

            // Make required CvParamTypes from the current line.
            CvParamType lCV_Q1 = CVFactory.createCVType_MZ(lQ1);
            CvParamType lCV_Q3 = CVFactory.createCVType_MZ(lQ3);

            CvParamType lCV_CollisionEnergy = CVFactory.createCVType_CollisionEnergy(lEnergy);

            // 1. Make the Precursor Type
            PrecursorType lPrecursorType = iObjectFactory.createPrecursorType();
            lPrecursorType.getCvParam().add(lCV_Q1);

            // 2. Make the Product Type
            ProductType lProductType = iObjectFactory.createProductType();
            lProductType.getCvParam().add(lCV_Q3);

            // 3. Define the configuration
            ConfigurationType lConfigurationType = iObjectFactory.createConfigurationType();
            lConfigurationType.getCvParam().add(lCV_CollisionEnergy);

            // add this configuration to the configuration list.
            ConfigurationListType lConfigurationListType = iObjectFactory.createConfigurationListType();
            lConfigurationListType.getConfiguration().add(lConfigurationType);


            // 4. Parse the peptide, charge and iontypes from the id.
            // CSASVLPVDVQTLNSSGPPFGK.2y16-1
            String[] lSplit = lID.split("\\.");

            // Get peptide.
            // CSASVLPVDVQTLNSSGPPFGK
            String lPeptide = lSplit[0];


            // 2
            String lPrecursorChargeValue = "" + lSplit[1].charAt(0);

            // y16-1
            String[] lSecondSplit = lSplit[1].substring(1).split("-");

            // y
            String lIonType = lSecondSplit[0].charAt(0) + "";

            // 16
            String lIonNumber = lSecondSplit[0].substring(1);

            // 1
            String lProductChargeValue = lSecondSplit[1];


            String lPeptideID = lPeptide + "." + lPrecursorChargeValue;
            PeptideType lCurrentPeptideType = null;

            CompoundListType lCompoundList = aTraMLType.getCompoundList();

            if(lCompoundList == null){
                // Create the object upon first encounter.
                lCompoundList = iObjectFactory.createCompoundListType();
                aTraMLType.setCompoundList(lCompoundList);
            }

            List<PeptideType> lPeptideTypeList = lCompoundList.getPeptide();
            for (PeptideType lRunningPeptideType : lPeptideTypeList) {
                if(lRunningPeptideType.getId().equals(lPeptideID)){
                    // Ok! We need this PeptideType!
                    lCurrentPeptideType = lRunningPeptideType;
                    break;
                }
            }

            if(lCurrentPeptideType == null){
                // If null, then current PeptideId has not been seen in the previous loop.
                // so create one!
                lCurrentPeptideType = iObjectFactory.createPeptideType();
                lCurrentPeptideType.setSequence(lPeptide);
                lCurrentPeptideType.setId(lPeptideID);

                aTraMLType.getCompoundList().getPeptide().add(lCurrentPeptideType);

                RetentionTimeType lRetentionTimeType = iObjectFactory.createRetentionTimeType();

                CvParamType lCVType_retentionTimeStart = CVFactory.createCVType_RetentionTimeStart(lStartTime);
                lRetentionTimeType.getCvParam().add(lCVType_retentionTimeStart);

                CvParamType lCVType_retentionTimeStop = CVFactory.createCVType_RetentionTimeStop(lStopTime);
                lRetentionTimeType.getCvParam().add(lCVType_retentionTimeStop);

                RetentionTimeListType lRetentionTimeList = lCurrentPeptideType.getRetentionTimeList();
                if(lRetentionTimeList == null){
                    lRetentionTimeList = iObjectFactory.createRetentionTimeListType();
                    lCurrentPeptideType.setRetentionTimeList(lRetentionTimeList);
                }
                lRetentionTimeList.getRetentionTime().add(lRetentionTimeType);
            }

            // Finally, set all values to a TransitionType.

            // Make the TransitionType
            TransitionType lTransitionType = iObjectFactory.createTransitionType();
            lTransitionType.setId(lID);
            lTransitionType.setPeptideRef(lCurrentPeptideType);

            lProductType.setConfigurationList(lConfigurationListType);

            lTransitionType.setPrecursor(lPrecursorType);
            lTransitionType.setProduct(lProductType);

            // Finish by adding this TransitionType to the TraML main tag.
            aTraMLType.getTransitionList().getTransition().add(lTransitionType);

        } catch (ServiceException e) {
            logger.error(e.getMessage(), e);
        } catch (RemoteException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * {@inheritDoc}
     * <p/>
     * <SourceFileList>
     * <SourceFile id="sf1" name="OneTransition.tsv" location="file:///F:/data/Exp01">
     * <cvParam cvRef="MS" accession="MS:1000914" name="tab delimited text file" value=""/>
     * <cvParam cvRef="MS" accession="MS:1000569" name="SHA-1" value="71be39fb2700ab2f3c8b2234b91274968b6899b1"/>
     * </SourceFile>
     * </SourceFileList>
     */
    public SourceFileListType getSourceTypeList() {
        SourceFileListType lSourceFileListType = iObjectFactory.createSourceFileListType();
        SourceFileType lSourceFileType = iObjectFactory.createSourceFileType();

        lSourceFileType.setId("ThermoTSQ_to_traml_converter_v" + CoreConfiguration.VERSION);
        lSourceFileType.setLocation(iFile.getParent());
        lSourceFileType.setName(iFile.getName());

        lSourceFileListType.getSourceFile().add(lSourceFileType);

        return lSourceFileListType;
    }

    /**
     * {@inheritDoc}
     */
    public char getSeparator() {
        return ',';
    }
}
