package com.compomics.jtraml.factory;

import org.hupo.psi.ms.traml.CvParamType;
import org.hupo.psi.ms.traml.ObjectFactory;

import org.hupo.psi.ms.traml.InstrumentType;

/**
 * This factory generates a series of Types.
 */
public class InstrumentFactory {

    /**
     * An ObjectFactory used by the CVFactory
     */
    private static ObjectFactory iObjectFactory = new ObjectFactory();
    
    private static InstrumentType abiInstrumentType;
    private static InstrumentType agilentInstrumentType;
    private static InstrumentType thermoInstrumentType;

    // Static initialization of the Controlled Vocabularies.
    static {
        abiInstrumentType = new InstrumentType();
        abiInstrumentType.setId("ABI");
        CvParamType abiCvParam = new CvParamType();
        abiCvParam.setAccession("MS:1000121");
        abiCvParam.setName("AB SCIEX instrument model");
        abiCvParam.setCvRef(CVFactory.getCV_MS());
        abiInstrumentType.setCvParam(abiCvParam);
        
        agilentInstrumentType = new InstrumentType();
        agilentInstrumentType.setId("Agilent");
        CvParamType agilentCvParam = new CvParamType();
        agilentCvParam.setAccession("MS:1000490");
        agilentCvParam.setName("Agilent instrument model");
        agilentCvParam.setCvRef(CVFactory.getCV_MS());
        agilentInstrumentType.setCvParam(agilentCvParam);
        
        thermoInstrumentType = new InstrumentType();
        thermoInstrumentType.setId("Thermo");
        CvParamType thermoCvParam = new CvParamType();
        thermoCvParam.setAccession("MS:1000494");
        thermoCvParam.setName("Thermo Scientific instrument model");
        thermoCvParam.setCvRef(CVFactory.getCV_MS());        
        thermoInstrumentType.setCvParam(thermoCvParam);
    }    

    /**
     * Returns the static instance of the ABI instrument.
     *
     * @return
     */
    public static InstrumentType getAbiInstrument() {
        return abiInstrumentType;
    }

    /**
     * Returns the static instance of the Agilent instrument.
     *
     * @return
     */
    public static InstrumentType getAgilentInstrument() {
        return agilentInstrumentType;
    }

    /**
     * Returns the static instance of the thermo instrument.
     *
     * @return
     */
    public static InstrumentType getThermoInstrument() {
        return thermoInstrumentType;
    }
    
}
