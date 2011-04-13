package com.compomics.traml.model.rowmodel;

import com.compomics.traml.exception.JTramlException;
import com.compomics.traml.factory.CustomTypeFactory;
import com.compomics.traml.interfaces.ColumnModel;
import com.compomics.traml.interfaces.RowModel;
import org.apache.log4j.Logger;
import org.hupo.psi.ms.traml.*;

import javax.xml.rpc.ServiceException;
import java.rmi.RemoteException;

/**
 * This class is an RowModel implementation for the Agilent QQQ instrument.
 * e.g.
 * ""
 * Dynamic MRM
 * Compound Name	ISTD?	Precursor Ion	MS1 Res	Product Ion	MS2 Res	Fragmentor	Collision Energy	Cell Accelerator Voltage	Ret Time (min)	Delta Ret Time	Polarity
 * CSASVLPVDVQTLNSSGPPFGK.2y16-1	FALSE	1130.5681	Wide	1642.8233	Unit	125	39.8	5	42.35	5.00	Positive
 */
public class RowModelAgilentImpl implements RowModel {

    private static Logger logger = Logger.getLogger(RowModelAgilentImpl.class);

    private static ColumnModel[] iColumnModels;
    private ObjectFactory iObjectFactory;

    static {
        iColumnModels = new ColumnModel[12];
//        iColumnModels[0] = new TransitionIDImpl();
    }


    /**
     * Construct a new RowModel implementation for Agilent inputformat.
     */
    public RowModelAgilentImpl() {
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
        if (aRowValues.length != iColumnModels.length) {
            throw new JTramlException("Unexpected numer of columns for the Agilent RowModel!!");
        }

        if (aTraMLType.getTransitionList() == null) {
            aTraMLType.setTransitionList(iObjectFactory.createTransitionListType());
        }

        String lCompoundName = aRowValues[0];
        String lISTD = aRowValues[1];
        String lQ1 = aRowValues[2];

        String lRes1 = aRowValues[3];
        String lQ3 = aRowValues[4];
        String lRes3 = aRowValues[5];

        String lFragmentor = aRowValues[6];
        String lEnergy = aRowValues[7];
        String lVoltage = aRowValues[8];

        String lRt = aRowValues[9];
        String lRtdelta = aRowValues[10];
        String lPolarity = aRowValues[11];

//        CompoundListType lCompoundList = aTraMLType.getCompoundList();


        // <cvParam cvRef="MS" accession="MS:1000827" name="isolation window target m/z" value="862.9467"
        // unitCvRef="MS" unitAccession="MS:1000040" unitName="m/z"/>
        try {

            // Make required CvParamTypes from the current line.
            CvParamType lCV_Q1 = CustomTypeFactory.createCVType_MZ(lQ1);
            CvParamType lCV_Q3 = CustomTypeFactory.createCVType_MZ(lQ3);
            CvParamType lCV_CollisionEnergy = CustomTypeFactory.createCVType_CollisionEnergy(lEnergy);

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



            // Finally, set all values to a TransitionType.

            // Make the TransitionType
            TransitionType lTransitionType = iObjectFactory.createTransitionType();
            lTransitionType.setId(lCompoundName);

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
}
