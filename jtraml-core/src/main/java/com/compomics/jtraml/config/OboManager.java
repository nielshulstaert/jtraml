package com.compomics.jtraml.config;

import com.compomics.jtraml.exception.JTramlException;
import org.apache.log4j.Logger;
import uk.ac.ebi.pride.utilities.ols.web.service.client.Client;
import uk.ac.ebi.pride.utilities.ols.web.service.client.OLSClient;
import uk.ac.ebi.pride.utilities.ols.web.service.config.OLSWsConfigProd;
import uk.ac.ebi.pride.utilities.ols.web.service.model.Term;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private HashMap<String, Map<String, List<String>>> iMSTermsMap = new HashMap<>();

    /**
     * HashMap which stores the Unit ontology terms to reduce the load on the service.
     */
    private HashMap<String, Map<String, List<String>>> iUOTermsMap = new HashMap<>();
    /**
     * The OLS client.
     */
    private Client olsClient;

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
        olsClient = new OLSClient(new OLSWsConfigProd());
    }

    /**
     * Returns a String name from the PSI obo file.
     *
     * @param aTermName MS vocabulary term name
     * @return HashMap with the name, description and id for the specified term name.
     */
    public Map<String, List<String>> getMSTerm(String aTermName) {

        if (iMSTermsMap.containsKey(aTermName)) {
            // Return the cached value.
            return iMSTermsMap.get(aTermName);
        } else {
            logger.debug("querying PSI-MS ontology for " + aTermName);

            Term term = olsClient.getExactTermByName(aTermName, "ms");

            if (term == null) {
                throw new JTramlException("cannot find term for name " + aTermName + "!!");
            }

            Map<String, List<String>> annotations = olsClient.getAnnotations(term.getOboId(), "ms");

            // Store the value in our locally cached TermsMap.
            iMSTermsMap.put(aTermName, annotations);

            // And return this value.
            return annotations;
        }
    }


    /**
     * Returns a String name from the PSI obo file.
     *
     * @param aName UO vocabulary term name
     * @return HashMap with the name, description and id for the specified term name.
     */
    public Map getUOTerm(String aName) {
        if (iUOTermsMap.containsKey(aName)) {
            // Return the cached value.
            return iUOTermsMap.get(aName);
        } else {
            logger.debug("querying UO for " + aName);

            Term term = olsClient.getExactTermByName(aName, "uo");

            if (term == null) {
                throw new JTramlException("cannot find term for name " + aName + "!!");
            }

            Map<String, List<String>> annotations = olsClient.getAnnotations(term.getOboId(), "ms");

            // Store the value in our locally cached TermsMap.
            iUOTermsMap.put(aName, annotations);

            // And return this value.
            return annotations;
        }
    }


}
