package com.compomics.traml.factory;

import com.compomics.traml.config.OboManager;
import com.compomics.traml.enumeration.FrequentOBoEnum;
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
public class CustomTypeFactory {

    /**
     * An ObjectFactory used by the CustomTypeFactory
     */
    private static ObjectFactory iObjectFactory = new ObjectFactory();


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
        CvType lMSCVType = new CvType();
        lMSCVType.setFullName("Proteomics Standards Initiative Mass Spectrometry Ontology");
        lMSCVType.setId("MS");
        lMSCVType.setURI("http://psidev.cvs.sourceforge.net/*checkout*/psidev/psi/psi-ms/mzML/controlledVocabulary/psi-ms.obo");

        CvType lUOCVType = new CvType();
        lUOCVType.setFullName("Unit Ontology");
        lUOCVType.setId("UO");
        lUOCVType.setURI("http://obo.cvs.sourceforge.net/obo/obo/ontology/phenotype/unit.obo");

        CvType lMODCVType = new CvType();
        lMODCVType.setFullName("UNIMOD CV for modifications");
        lMODCVType.setId("UNIMOD");
        lMODCVType.setURI("http://www.unimod.org/obo/unimod.obo");

        CvListType lCvListType = new CvListType();
        lCvListType.getCv().add(lMSCVType);
        lCvListType.getCv().add(lUOCVType);
        lCvListType.getCv().add(lMODCVType);

        return lCvListType;
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
        CvParamType lCvParamType = iObjectFactory.createCvParamType();
        HashMap lTerm = OboManager.getInstance().getMSTerm(FrequentOBoEnum.ISOLATION_WINDOW.getName());

        lCvParamType.setAccession(lTerm.get("id").toString());
        lCvParamType.setValue(aValue);
        lCvParamType.setName(FrequentOBoEnum.ISOLATION_WINDOW.getName());

        HashMap lUnitTerm = OboManager.getInstance().getMSTerm(FrequentOBoEnum.MZ.getName());
        lCvParamType.setUnitName(FrequentOBoEnum.MZ.getName());
        lCvParamType.setUnitAccession(lUnitTerm.get("id").toString());

        return lCvParamType;
    }


    /**
     * Creates a CVParamType for a collision energy value.
     *
     * @param aValue collision energy for this CV Param.
     * @return Valid CvParamType for the specified colision energy value.
     * @throws ServiceException
     * @throws RemoteException
     * '<'cvParam cvRef="MS" accession="MS:1000045" name="collision energy" value="26" unitCvRef="UO"
     *                          unitAccession="UO:0000266" unitName="electronvolt"/>
     */
    public static CvParamType createCVType_CollisionEnergy(String aValue) throws ServiceException, RemoteException {
        CvParamType lCvParamType = iObjectFactory.createCvParamType();
        HashMap lTerm = OboManager.getInstance().getMSTerm(FrequentOBoEnum.COLLISION_ENERGY.getName());

        lCvParamType.setAccession(lTerm.get("id").toString());
        lCvParamType.setValue(aValue);
        lCvParamType.setName(FrequentOBoEnum.COLLISION_ENERGY.getName());

        HashMap lUnitTerm = OboManager.getInstance().getUOTerm(FrequentOBoEnum.ELECTRON_VOLT.getName());
        lCvParamType.setUnitName(FrequentOBoEnum.ELECTRON_VOLT.getName());
        lCvParamType.setUnitAccession(lUnitTerm.get("id").toString());

        return lCvParamType;
    }

}
