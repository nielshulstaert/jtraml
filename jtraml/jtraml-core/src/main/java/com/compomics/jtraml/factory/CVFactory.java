package com.compomics.jtraml.factory;

import com.compomics.jtraml.config.OboManager;
import com.compomics.jtraml.enumeration.FrequentOBoEnum;
import org.hupo.psi.ms.traml.CvListType;
import org.hupo.psi.ms.traml.CvParamType;
import org.hupo.psi.ms.traml.CvType;
import org.hupo.psi.ms.traml.ObjectFactory;

import javax.xml.rpc.ServiceException;
import java.rmi.RemoteException;
import java.util.HashMap;

/**
 * This factory generates a series of Types.
 */
public class CVFactory {

    /**
     * An ObjectFactory used by the CVFactory
     */
    private static ObjectFactory iObjectFactory = new ObjectFactory();

    private static CvType mscvType;
    private static CvType uocvType;
    private static CvType modcvType;

    // Static initialization of the Controlled Vocabularies.
    static {
        mscvType = new CvType();
        mscvType.setFullName("Proteomics Standards Initiative Mass Spectrometry Ontology");
        mscvType.setId("MS");
        mscvType.setURI("http://psidev.cvs.sourceforge.net/*checkout*/psidev/psi/psi-ms/mzML/controlledVocabulary/psi-ms.obo");
        mscvType.setVersion("3.59.0");

        uocvType = new CvType();
        uocvType.setFullName("Unit Ontology");
        uocvType.setId("UO");
        uocvType.setURI("http://obo.cvs.sourceforge.net/obo/obo/ontology/phenotype/unit.obo");
        uocvType.setVersion("unknown");
        

        modcvType = new CvType();
        modcvType.setFullName("UNIMOD CV for modifications");
        modcvType.setId("UNIMOD");
        modcvType.setURI("http://www.unimod.org/obo/unimod.obo");
        modcvType.setVersion("unknown");
    }


    /**
     * Returns the fixed Controlled Vocabularies dependencies of TraML.
     * <p/>
     * <p/>
     * <cvList>
     * <cv id="MS" fullName="" version="2.31.0"
     * URI=""/>
     * <cv id="UO" fullName="Unit Ontology" version="unknown"
     * URI=""/>
     * <cv id="UNIMOD" fullName="UNIMOD CV for modifications" version="unknown"
     * URI="http://www.unimod.org/obo/unimod.obo"/>
     * </cvList>
     *
     * @return
     */
    public static CvListType getCvListType() {

        CvListType lCvListType = new CvListType();
        lCvListType.getCv().add(getCV_MS());
        lCvListType.getCv().add(getCV_UO());
        lCvListType.getCv().add(getCV_MOD());

        return lCvListType;
    }


    /**
     * Creates a CVParamType for a retention time.
     *
     * @param aValue                retention time window for this CV Param.
     * @param aMSType               FrequentOboEnum type to be looked up in the MS vocabulary.
     * @param aUnitType             FrequentOBoEnum type to be looked up in the UO vocabulary.
     * @param aUnitFromMSVocabulary TRUE if the UnitType comes from the MS vocabulary. FALSE if the UnitType comes from the Unit Ontology.
     * @return Valid CvParamType for the retention time (minutes).
     * @throws ServiceException
     * @throws RemoteException  <cvParam cvRef="MS" accession="MS:1000895" name="local retention time" value="40.02" unitCvRef="UO"
     *                          unitAccession="UO:0000031" unitName="minute"/>
     */
    public static CvParamType createCustomCVType(String aValue, FrequentOBoEnum aMSType, FrequentOBoEnum aUnitType, boolean aUnitFromMSVocabulary) throws ServiceException, RemoteException {
        CvParamType lCvParamType = iObjectFactory.createCvParamType();

        HashMap lTerm = OboManager.getInstance().getMSTerm(aMSType.getName());
        lCvParamType.setAccession(lTerm.get("id").toString());
        lCvParamType.setValue(aValue);
        lCvParamType.setName(aMSType.getName());
        lCvParamType.setCvRef(getCV_MS());


        HashMap lUnitTerm;
        if (aUnitFromMSVocabulary) {
            lUnitTerm = OboManager.getInstance().getMSTerm(aUnitType.getName());
            lCvParamType.setUnitCvRef(getCV_MS());
        } else {
            lUnitTerm = OboManager.getInstance().getUOTerm(aUnitType.getName());
            lCvParamType.setUnitCvRef(getCV_UO());
        }
        lCvParamType.setUnitName(aUnitType.getName());
        lCvParamType.setUnitAccession(lUnitTerm.get("id").toString());

        return lCvParamType;
    }

    /**
     * Creates a CVParamType for a retention time.
     *
     * @param aValue                retention time window for this CV Param.
     * @param aMSType               FrequentOboEnum type to be looked up in the MS vocabulary.
     * @return Valid CvParamType for the retention time (minutes).
     * @throws ServiceException
     * @throws RemoteException  <cvParam cvRef="MS" accession="MS:1000895" name="local retention time" value="40.02" unitCvRef="UO"
     *                          unitAccession="UO:0000031" unitName="minute"/>
     */
    public static CvParamType createCustomCVType(String aValue, FrequentOBoEnum aMSType) throws ServiceException, RemoteException {
        CvParamType lCvParamType = iObjectFactory.createCvParamType();

        HashMap lTerm = OboManager.getInstance().getMSTerm(aMSType.getName());
        lCvParamType.setAccession(lTerm.get("id").toString());
        lCvParamType.setValue(aValue);
        lCvParamType.setName(aMSType.getName());
        lCvParamType.setCvRef(getCV_MS());

        return lCvParamType;
    }

    /**
     * Creates a CVParamType for a retention time.
     *
     * @param aMSType               FrequentOboEnum type to be looked up in the MS vocabulary.
     * @return Valid CvParamType for the retention time (minutes).
     * @throws ServiceException
     * @throws RemoteException  <cvParam cvRef="MS" accession="MS:1000895" name="local retention time" value="40.02" unitCvRef="UO"
     *                          unitAccession="UO:0000031" unitName="minute"/>
     */
    public static CvParamType createCustomCVType(FrequentOBoEnum aMSType) throws ServiceException, RemoteException {
        CvParamType lCvParamType = iObjectFactory.createCvParamType();

        HashMap lTerm = OboManager.getInstance().getMSTerm(aMSType.getName());
        lCvParamType.setAccession(lTerm.get("id").toString());
        lCvParamType.setName(aMSType.getName());
        lCvParamType.setCvRef(getCV_MS());

        return lCvParamType;
    }

    /**
     * Creates a CVParamType for a retention time.
     *
     * @param aValue    retention time window for this CV Param.
     * @param aMSType   FrequentOboEnum type to be looked up in the MS vocabulary.
     * @param aUnitType FrequentOBoEnum type to be looked up in the UO vocabulary
     * @param aUnitType retention time window for this CV Param.
     * @return Valid CvParamType for the retention time (minutes).
     * @throws ServiceException
     * @throws RemoteException  <cvParam cvRef="MS" accession="MS:1000895" name="local retention time" value="40.02" unitCvRef="UO"
     *                          unitAccession="UO:0000031" unitName="minute"/>
     */
    public static CvParamType createCustomCVType(String aValue, FrequentOBoEnum aMSType, FrequentOBoEnum aUnitType) throws ServiceException, RemoteException {
        return createCustomCVType(aValue, aMSType, aUnitType, false); // lookup in the Unit Ontology!
    }

    /**
     * Creates a CVParamType for the polarity.
     *
     * @param aPolarity boolean. TRUE means positive charge, FALSE means negative charge.
     * @throws RemoteException <cvParam cvRef="MS" accession="MS:1000037" name="polarity" value="40.02" unitCvRef="UO"
     *                         unitAccession="UO:0000031" unitName="minute"/>
     */
    public static CvParamType createCVTypePolarity(boolean aPolarity) throws ServiceException, RemoteException {
        CvParamType lCvParamType = iObjectFactory.createCvParamType();

        HashMap lTerm = OboManager.getInstance().getMSTerm(FrequentOBoEnum.POLARITY.getName());
        lCvParamType.setAccession(lTerm.get("id").toString());
        lCvParamType.setName(FrequentOBoEnum.POLARITY.getName());
        lCvParamType.setCvRef(getCV_MS());
        if(aPolarity){
            lCvParamType.setValue(FrequentOBoEnum.POLARITY_POSITIVE.getName());
        }else{
            lCvParamType.setValue(FrequentOBoEnum.POLARITY_NEGATIVE.getName());
        }

        return lCvParamType;
    }


    /**
     * Creates a CVParamType for a m/z value.
     *
     * @param aValue mz value for this m/z based CV Param.
     * @return Valid CvParamType for the specified m/z value.
     * @throws ServiceException
     * @throws RemoteException
     */
    public static CvParamType createCVType_MZ(String aValue) throws ServiceException, RemoteException {
        return createCustomCVType(aValue, FrequentOBoEnum.ISOLATION_WINDOW, FrequentOBoEnum.MZ, true);
    }


    /**
     * Creates a CVParamType for a collision energy value.
     *
     * @param aValue collision energy for this CV Param.
     * @return Valid CvParamType for the specified colision energy value.
     * @throws ServiceException
     * @throws RemoteException  '<'cvParam cvRef="MS" accession="MS:1000045" name="collision energy" value="26" unitCvRef="UO"
     *                          unitAccession="UO:0000266" unitName="electronvolt"/>
     */
    public static CvParamType createCVType_CollisionEnergy(String aValue) throws ServiceException, RemoteException {
        return createCustomCVType(aValue, FrequentOBoEnum.COLLISION_ENERGY, FrequentOBoEnum.ELECTRON_VOLT);
    }

    /**
     * Creates a CVParamType for a accelerating voltage.
     *
     * @param aValue collision energy for this CV Param.
     * @return Valid CvParamType for the specified accelerating voltage (volt).
     * @throws ServiceException
     * @throws RemoteException  '<'cvParam cvRef="MS" accession="MS:1000045" name="collision energy" value="26" unitCvRef="UO"
     *                          unitAccession="UO:0000266" unitName="electronvolt"/>
     */
    public static CvParamType createCVType_AcceleratingVoltage(String aValue) throws ServiceException, RemoteException {
        return createCustomCVType(aValue, FrequentOBoEnum.ACCELERATING_VOLTAGE, FrequentOBoEnum.VOLT);
    }


    /**
     * Creates a CVParamType for a retention time start.
     *
     * @param aValue time for this CV Param.
     * @return Valid CvParamType for the retention time (minutes).
     * @throws ServiceException
     * @throws RemoteException  <cvParam cvRef="MS" accession="MS:1000895" name="retention time window lower offset" value="40.02" unitCvRef="UO"
     *                          unitAccession="UO:0000031" unitName="minute"/>
     */
    public static CvParamType createCVType_RetentionTimeStart(String aValue) throws ServiceException, RemoteException {
        return createCustomCVType(aValue, FrequentOBoEnum.RETENTION_TIME_LOWER, FrequentOBoEnum.MINUTES);
    }


    /**
     * Creates a CVParamType for a retention time stop.
     *
     * @param aValue time for this CV Param.
     * @return Valid CvParamType for the retention time (minutes).
     * @throws ServiceException
     * @throws RemoteException  <cvParam cvRef="MS" accession="MS:1000895" name="retention time window lower offset" value="40.02" unitCvRef="UO"
     *                          unitAccession="UO:0000031" unitName="minute"/>
     */
    public static CvParamType createCVType_RetentionTimeStop(String aValue) throws ServiceException, RemoteException {
        return createCustomCVType(aValue, FrequentOBoEnum.RETENTION_TIME_UPPER, FrequentOBoEnum.MINUTES);
    }


    /**
     * Creates a CVParamType for a retention time.
     *
     * @param aValue collision energy for this CV Param.
     * @return Valid CvParamType for the retention time (minutes).
     * @throws ServiceException
     * @throws RemoteException  <cvParam cvRef="MS" accession="MS:1000895" name="local retention time" value="40.02" unitCvRef="UO"
     *                          unitAccession="UO:0000031" unitName="minute"/>
     */
    public static CvParamType createCVType_RetentionTime(String aValue) throws ServiceException, RemoteException {
        return createCustomCVType(aValue, FrequentOBoEnum.RETENTION_TIME, FrequentOBoEnum.MINUTES);
    }


    /**
     * Creates a CVParamType for a retention time.
     *
     * @param aValue retention time window for this CV Param.
     * @return Valid CvParamType for the retention time (minutes).
     * @throws ServiceException
     * @throws RemoteException  <cvParam cvRef="MS" accession="MS:1000895" name="local retention time" value="40.02" unitCvRef="UO"
     *                          unitAccession="UO:0000031" unitName="minute"/>
     */
    public static CvParamType createCVType_RetentionTimeWindow(String aValue) throws ServiceException, RemoteException {
        return createCustomCVType(aValue, FrequentOBoEnum.RETENTION_TIME_WINDOW, FrequentOBoEnum.MINUTES);
    }


    /**
     * Returns the static instance of the PSI-MS controlled vocabulary.
     *
     * @return
     */
    public static CvType getCV_MS() {
        return mscvType;
    }

    /**
     * Returns the static instance of the Unit Ontology controlled vocabulary.
     *
     * @return
     */
    public static CvType getCV_UO() {
        return uocvType;
    }

    /**
     * Returns the static instance of the UNIMOD controlled vocabulary.
     *
     * @return
     */
    public static CvType getCV_MOD() {
        return modcvType;
    }

}
