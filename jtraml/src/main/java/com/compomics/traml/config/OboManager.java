package com.compomics.traml.config;

import com.compomics.traml.exception.JTramlException;
import org.apache.log4j.Logger;
import uk.ac.ebi.ols.soap.QueryServiceLocator;

import javax.xml.rpc.ServiceException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Set;

/**
 * This class manages access to ontology terms.
 */
public class OboManager {
    private static Logger logger = Logger.getLogger(OboManager.class);

    /**
     * Singleton instance.
     */
    private static OboManager iManager = null;

    /**
     * HashMap which stores the MS ontology terms to reduce the load on the service.
     */
    private HashMap<String, HashMap> iMSTermsMap = new HashMap<String, HashMap>();

    /**
     * HashMap which stores the Unit ontology terms to reduce the load on the service.
     */
    private HashMap<String, HashMap> iUOTermsMap = new HashMap<String, HashMap>();


    private QueryServiceLocator iServiceLocator;


    /**
     * Returns the singleton instance of the Obo manager.
     *
     * @return
     */
    public static OboManager getInstance() {
        if (iManager == null) {
            iManager = new OboManager();
        }
        return iManager;
    }

    /**
     * Private singleton constructor for a Obo file manager.
     */
    private OboManager() {
        iServiceLocator = new QueryServiceLocator();
    }

    /**
     * Returns a String name from the PSI obo file.
     *
     * @param aTermName MS vocabulary term name
     * @return HashMap with the name, description and id for the specified term name.
     */
    public HashMap getMSTerm(String aTermName) throws ServiceException, RemoteException {

        if (iMSTermsMap.containsKey(aTermName)) {
            // Return the cached value.
            return iMSTermsMap.get(aTermName);
        } else {
            logger.debug("querying PSI-MS ontology for " + aTermName);


            Set lTerms = iServiceLocator.getOntologyQuery().getTermsByExactName(aTermName, "MS").keySet();

            if (lTerms.size() == 0) {
                throw new JTramlException("cannot find term for name " + aTermName + "!!");
            } else if (lTerms.size() > 1) {
                throw new JTramlException("more then one term was returned for name " + aTermName + "!!");
            }

            String lID = lTerms.toArray()[0].toString();
            HashMap lMetadata = iServiceLocator.getOntologyQuery().getTermMetadata(lID, "MS");
            lMetadata.put("id", lID);

            // Store the value in our locally cached TermsMap.
            iMSTermsMap.put(aTermName, lMetadata);

            // And return this value.
            return lMetadata;
        }
    }


    /**
     * Returns a String name from the PSI obo file.
     *
     * @param aName UO vocabulary term name
     * @return HashMap with the name, description and id for the specified term name.
     */
    public HashMap getUOTerm(String aName) throws ServiceException, RemoteException {
        if (iUOTermsMap.containsKey(aName)) {
            // Return the cached value.
            return iUOTermsMap.get(aName);
        } else {
            logger.debug("querying UO for " + aName);

            Set lTerms = iServiceLocator.getOntologyQuery().getTermsByExactName(aName, "UO").keySet();

            if (lTerms.size() == 0) {
                throw new JTramlException("cannot find term for name " + aName + "!!");
            } else if (lTerms.size() > 1) {
                throw new JTramlException("more then one term was returned for name " + aName + "!!");
            }

            String lID = lTerms.toArray()[0].toString();
            HashMap lMetadata = iServiceLocator.getOntologyQuery().getTermMetadata(lID, "UO");
            lMetadata.put("id", lID);

            // Store the value,
            iUOTermsMap.put(aName, lMetadata);

            // And return this value.
            return lMetadata;
        }
    }


}
