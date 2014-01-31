package com.compomics.jtraml.model;

import com.compomics.jtraml.config.CoreConfiguration;
import com.compomics.jtraml.exception.JTramlException;
import com.compomics.jtraml.factory.CVFactory;
import com.compomics.jtraml.factory.InstrumentFactory;
import com.compomics.jtraml.interfaces.TSVFileImportModel;
import org.apache.log4j.Logger;
import org.hupo.psi.ms.traml.*;

import javax.xml.rpc.ServiceException;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.rmi.RemoteException;
import java.util.List;

/**
 * This class is an TSVFileImportModel implementation for the Agilent QQQ instrument.
 * e.g.
 * ""
 * Dynamic MRM
 * Compound Name	ISTD?	Precursor Ion	MS1 Res	Product Ion	MS2 Res	Fragmentor	Collision Energy	Cell Accelerator Voltage	Ret Time (min)	Delta Ret Time	Polarity
 * CSASVLPVDVQTLNSSGPPFGK.2y16-1	FALSE	1130.5681	Wide	1642.8233	Unit	125	39.8	5	42.35	5.00	Positive
 */
public class AgilentToTraml extends TSVFileImportModel {

    private static Logger logger = Logger.getLogger(AgilentToTraml.class);

    /**
     * Jaxb generated Factory for the traml xsd.
     */
    private ObjectFactory iObjectFactory;

    /**
     * The specified file for the model.
     */
    private File iFile;
    public boolean hasPolarity = false;
    public CvParamType iParamTypePolarity = null;

    /**
     * Construct a new TSVFileImportModel instance for an Agilent tsv file.
     * <p/>
     * Dynamic MRM
     * Compound Name	ISTD?	Precursor Ion	MS1 Res	Product Ion	MS2 Res	Fragmentor	Collision Energy	Cell Accelerator Voltage	Ret Time (min)	Delta Ret Time	Polarity
     * CSASVLPVDVQTLNSSGPPFGK.2y16-1	FALSE	1130.5681	Wide	1642.8233	Unit	125	39.8	5	42.35	5.00	Positive
     */

    public AgilentToTraml(File aFile) {
        iFile = aFile;
        iObjectFactory = new ObjectFactory();
    }

    /**
     * Implementing classes must be capable of writing an array of rowvalues into a TramlType instance.
     * <p/>
     * Dynamic MRM
     * Compound Name	ISTD?	Precursor Ion	MS1 Res	Product Ion	MS2 Res	Fragmentor	Collision Energy	Cell Accelerator Voltage	Ret Time (min)	Delta Ret Time	Polarity
     * CSASVLPVDVQTLNSSGPPFGK.2y16-1	FALSE	1130.5681	Wide	1642.8233	Unit	125	39.8	5	42.35	5.00	Positive
     *
     * @param aTraMLType The TraMLType instance to store the rows into.
     * @param aRowValues The separates values from a single row.
     */
    public void addRowToTraml(TraMLType aTraMLType, String[] aRowValues) {

        // validate number of line values.
        if (aRowValues.length != 12) {
            throw new JTramlException("Unexpected number of columns for the Agilent TSVFileImportModel!!");
        }

        if (aTraMLType.getTransitionList() == null) {
            aTraMLType.setTransitionList(iObjectFactory.createTransitionListType());
        }

        String lID = aRowValues[0];// OK
        String lISTD = aRowValues[1];
        String lQ1 = aRowValues[2];//OK

        String lRes1 = aRowValues[3];
        String lQ3 = aRowValues[4];//OK
        String lRes3 = aRowValues[5];

        String lFragmentor = aRowValues[6];
        String lEnergy = aRowValues[7];//OK
        String lAccVoltage = aRowValues[8];//OK

        String lRt = aRowValues[9];//OK
        if (boolShiftRetentionTime) { // Do we need to shift the retention time?
            Double d = Double.parseDouble(lRt);
            d = d + iRetentionTimeShift;
            BigDecimal bd = new BigDecimal(d);
            bd = bd.setScale(4, RoundingMode.HALF_UP);
            lRt = bd.toString();
        }

        String lRtdelta = aRowValues[10];//OK
        String lPolarity = aRowValues[11];

        //CompoundListType lCompoundList = aTraMLType.getCompoundList();
        // <cvParam cvRef="MS" accession="MS:1000827" name="isolation window target m/z" value="862.9467"
        // unitCvRef="MS" unitAccession="MS:1000040" unitName="m/z"/>

        try {

            // Make required CvParamTypes from the current line.
            CvParamType lCV_Q1 = CVFactory.createCVType_MZ(lQ1);
            CvParamType lCV_Q3 = CVFactory.createCVType_MZ(lQ3);

            CvParamType lCV_AcceleratingVoltage = CVFactory.createCVType_AcceleratingVoltage(lAccVoltage);
            CvParamType lCV_CollisionEnergy = CVFactory.createCVType_CollisionEnergy(lEnergy);

            // 1. Make the Precursor Type
            PrecursorType lPrecursorType = iObjectFactory.createPrecursorType();
            lPrecursorType.getCvParam().add(lCV_Q1);

            // 2. Make the Product Type
            ProductType lProductType = iObjectFactory.createProductType();
            lProductType.getCvParam().add(lCV_Q3);

            // 3. Define the configuration
            ConfigurationType lConfigurationType = iObjectFactory.createConfigurationType();
            lConfigurationType.setInstrumentRef(InstrumentFactory.getAgilentInstrument());
            lConfigurationType.getCvParam().add(lCV_CollisionEnergy);
            lConfigurationType.getCvParam().add(lCV_AcceleratingVoltage);

            // add this configuration to the configuration list.
            ConfigurationListType lConfigurationListType = iObjectFactory.createConfigurationListType();
            lConfigurationListType.getConfiguration().add(lConfigurationType);

            // Try and set the Polarity Type.
            if(hasPolarity() == false){

                if(lPolarity.toLowerCase().equals("positive")){ // positive polarity
                    iParamTypePolarity = CVFactory.createCVTypePolarity(true);
                    hasPolarity = true;
                }else if(lPolarity.toLowerCase().equals("negative")){ // negative polarity
                    iParamTypePolarity = CVFactory.createCVTypePolarity(false);
                    hasPolarity = true;
                }
            }


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

            if (lCompoundList == null) {
                // Create the object upon first encounter.
                lCompoundList = iObjectFactory.createCompoundListType();
                aTraMLType.setCompoundList(lCompoundList);
            }

            List<PeptideType> lPeptideTypeList = lCompoundList.getPeptide();
            for (PeptideType lRunningPeptideType : lPeptideTypeList) {
                if (lRunningPeptideType.getId().equals(lPeptideID)) {
                    // Ok! We need this PeptideType!
                    lCurrentPeptideType = lRunningPeptideType;
                    break;
                }
            }

            if (lCurrentPeptideType == null) {
                // If null, then current PeptideId has not been seen in the previous loop.
                // so create one!
                lCurrentPeptideType = iObjectFactory.createPeptideType();
                lCurrentPeptideType.setSequence(lPeptide);
                lCurrentPeptideType.setId(lPeptideID);

                aTraMLType.getCompoundList().getPeptide().add(lCurrentPeptideType);

                RetentionTimeType lRetentionTimeType = iObjectFactory.createRetentionTimeType();
                CvParamType lCVType_retentionTime = CVFactory.createCVType_RetentionTime(lRt);
                lRetentionTimeType.getCvParam().add(lCVType_retentionTime);

                CvParamType lRetentionTimeWindow = CVFactory.createCVType_RetentionTimeWindow(lRtdelta);
                lRetentionTimeType.getCvParam().add(lRetentionTimeWindow);

                RetentionTimeListType lRetentionTimeList = lCurrentPeptideType.getRetentionTimeList();
                if (lRetentionTimeList == null) {
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

        lSourceFileType.setId("AgilentQQQ_to_traml_converter_v" + CoreConfiguration.VERSION);
        lSourceFileType.setLocation(iFile.getParent());
        lSourceFileType.setName(iFile.getName());
        
        CvParamType cvParamType = new CvParamType();
        cvParamType.setAccession("MS:1000914");
        cvParamType.setName("tab delimited text file");
        cvParamType.setCvRef(CVFactory.getCV_MS());
        
        lSourceFileType.getCvParam().add(cvParamType);

        lSourceFileListType.getSourceFile().add(lSourceFileType);

        return lSourceFileListType;
    }

    /**
     * Implementing classes must report whether they have found Polarity information.
     *
     * @return boolean True/False.
     */
    @Override
    public boolean hasPolarity() {
        return hasPolarity;
    }

    /**
     * Implementing classes must be able to return a CvParameter.
     * Can be NULL if the implementing class has not found Polarity information.
     *
     * @return CVParam instance.
     */
    @Override
    public CvParamType getPolarityCVParam() {
        return iParamTypePolarity;
    }

    /**
     * {@inheritDoc}
     */
    public char getSeparator() {
        return '\t';
    }
    
    @Override
    public InstrumentListType getInstrumentTypeList() {
        InstrumentListType instrumentListType = iObjectFactory.createInstrumentListType();
        
        // add default instrument
        instrumentListType.getInstrument().add(InstrumentFactory.getAgilentInstrument());
        
        return instrumentListType;
    }
}
