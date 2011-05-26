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
import java.util.ArrayList;
import java.util.List;

/**
 * This class is a TSVFileImportModel implementation for the ABI QTRAP instrument.
 * <p/>
 * e.g.;
 * <i>564.9618,663.4081,10,LSTADPADASTIYAVVV.O95866.O95866-3.O95866-5.3y6,29.3</i>
 * <nu>
 * <li >564.9618 = precursor mass
 * <li >663.4081 = fragment mass
 * <li >10 = ???
 * <li >LSTADPADASTIYAVVV = peptide sequence
 * <li >O95866 = protein accession (A)
 * <li >O95866-3 = protein accession (B)
 * <li >O95866-5 = protein accession (C)
 * <li >3y6 = precursor charge 3+, fragment y6
 * <li >29.3 = retention time
 * </nu>
 */
public class ABIToTraml implements TSVFileImportModel {


    private static Logger logger = Logger.getLogger(AgilentToTraml.class);

    /**
     * Jaxb generated Factory for the traml xsd.
     */
    private ObjectFactory iObjectFactory;

    /**
     * The specified file for the model.
     */
    private File iFile;

    /**
     * Construct a new TSVFileImportModel instance for an ABI QTRAP csv file.
     * <p/>
     * <p/>
     * e.g.;
     * <i>564.9618,663.4081,10,LSTADPADASTIYAVVV.O95866.O95866-3.O95866-5.3y6,29.3</i>
     * <nu>
     * <li >564.9618 = precursor mass
     * <li >663.4081 = fragment mass
     * <li >10 = ???
     * <li >LSTADPADASTIYAVVV = peptide sequence
     * <li >O95866 = protein accession (A)
     * <li >O95866-3 = protein accession (B)
     * <li >O95866-5 = protein accession (C)
     * <li >3y6 = precursor charge 3+, fragment y6
     * <li >29.3 = retention time
     * </nu>
     */

    public ABIToTraml(File aFile) {
        iFile = aFile;
        iObjectFactory = new ObjectFactory();
    }

    /**
     * Implementing classes must be capable of writing an array of rowvalues into a TramlType instance.
     * <p/>
     * <p/>
     * e.g.;
     * <i>564.9618,663.4081,10,LSTADPADASTIYAVVV.O95866.O95866-3.O95866-5.3y6,29.3</i>
     * <nu>
     * <li >564.9618 = precursor mass
     * <li >663.4081 = fragment mass
     * <li >10 = ???
     * <li >LSTADPADASTIYAVVV.O95866.O95866-3.O95866-5.3y6 = peptide sequence, protein accession a,b,c, precursor charge, fragment y6
     * <li >29.3 = retention time
     * </nu>
     *
     * @param aTraMLType The TraMLType instance to store the rows into.
     * @param aRowValues The separates values from a single row.
     */
    public void addRowToTraml(TraMLType aTraMLType, String[] aRowValues) {

        // validate number of line values.
        if (aRowValues.length != 5) {
            throw new JTramlException("Unexpected number of columns for the ABI QTRAP TSVFileImportModel!!");
        }

        if (aTraMLType.getTransitionList() == null) {
            aTraMLType.setTransitionList(iObjectFactory.createTransitionListType());
        }

        String lQ1 = aRowValues[0];//OK
        String lQ3 = aRowValues[1];//OK

        String lEnergy = aRowValues[2];//OK

        String lID = aRowValues[3];// OK

        String lRt = aRowValues[4];//OK


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
            lConfigurationListType.setConfiguration(lConfigurationType);


            // 4. Parse the peptide, charge and iontypes from the id.
            // LSTADPADASTIYAVVV.O95866.O95866-3.O95866-5.3y6
            String[] lSplit = lID.split("\\.");

            // Get peptide.
            // LSTADPADASTIYAVVV
            String lPeptide = lSplit[0];

            // All but the last are protein identifiers.
            // O95866   O95866-3  O95866-5
            String[] lProteins = new String[lSplit.length - 2];
            for (int i = 0; i < lProteins.length; i++) {
                lProteins[i] = lSplit[i + 1];
            }

            // 3y6
            String lFragment = lSplit[lSplit.length - 1];

            // 3
            String lPrecursorChargeValue = "" + lFragment.charAt(0);

            // y
            String lIonType = "" + lFragment.charAt(1);

            // 6
            String lIonNumber = lFragment.substring(2);

            String lPeptideID = lPeptide + "." + lPrecursorChargeValue;
            PeptideType lCurrentPeptideType = null;

            // Add protein refs.
            ProteinListType lProteinListType = aTraMLType.getProteinList();
            if (lProteinListType == null) {
                // Create the ProteinList upon first encounter.
                lProteinListType = iObjectFactory.createProteinListType();
                aTraMLType.setProteinList(lProteinListType);
            }

            List<ProteinType> lFullProteinList = lProteinListType.getProtein();
            List<ProteinRefType> lRunningProteinRefList = new ArrayList<ProteinRefType>();
            // Iterate over this transition's parent proteins.
            boolean lMatch;
            for (String lProtein : lProteins) {
                lMatch = false;
                // Iterate over all known proteins.
                for (ProteinType lProteinType : lFullProteinList) {
                    if (lProtein.equals(lProteinType.getId())) {
                        lMatch = true;
                        ProteinRefType lProteinRefType = iObjectFactory.createProteinRefType();
                        lProteinRefType.setRef(lProteinType);
                        lRunningProteinRefList.add(lProteinRefType);
                        break;
                    }
                }

                if (lMatch == false) {
                    // This proteinid has not been seen before.
                    // create a new ProteinType
                    ProteinType lProteinType = iObjectFactory.createProteinType();
                    lProteinType.setId(lProtein);
                    lProteinListType.getProtein().add(lProteinType);

                    // add a new ProteinRefType to be added to the peptide compound.
                    ProteinRefType lProteinRefType = iObjectFactory.createProteinRefType();
                    lProteinRefType.setRef(lProteinType);
                    lRunningProteinRefList.add(lProteinRefType);
                }

            }


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

                RetentionTimeListType lRetentionTimeList = lCurrentPeptideType.getRetentionTimeList();
                if (lRetentionTimeList == null) {
                    lRetentionTimeList = iObjectFactory.createRetentionTimeListType();
                    lCurrentPeptideType.setRetentionTimeList(lRetentionTimeList);
                }
                lRetentionTimeList.getRetentionTime().add(lRetentionTimeType);

                // Add the Protein refs.
                for (ProteinRefType lProteinRefType : lRunningProteinRefList) {
                    lCurrentPeptideType.getProteinRef().add(lProteinRefType);
                }
            }

            // Finally, set all values to a TransitionType.

            // Make the TransitionType
            TransitionType lTransitionType = iObjectFactory.createTransitionType();
            lTransitionType.setId(lID);
            lTransitionType.setPeptideRef(lCurrentPeptideType);

            lTransitionType.setPrecursor(lPrecursorType);
            lTransitionType.setProduct(lProductType);

            lTransitionType.setConfigurationList(lConfigurationListType);

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

        lSourceFileType.setId("ABIQTRAP_to_traml_converter_v" + CoreConfiguration.VERSION);
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
