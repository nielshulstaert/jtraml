package com.compomics.traml.compare;

import com.compomics.traml.interfaces.ColumnModel;

import java.util.Comparator;
import java.util.HashMap;

/**
 * This class serves to compare a set of ColumnModels
 */
public class ColumnModelComparator implements Comparator<ColumnModel> {

    /**
     * This map tracks the rank of the distinct JaxB generated Classes for the TraML specification.
     */
    private static HashMap<String, Integer> iRankMap = new HashMap<String, Integer>();

    static {
        // This block is a hard coded column "ordering" for the TraML classes.
        iRankMap.put("org.hupo.psi.ms.traml.TraMLType", 1);
        iRankMap.put("org.hupo.psi.ms.traml.ProteinType", 20);
        iRankMap.put("org.hupo.psi.ms.traml.PeptideType", 30);
        iRankMap.put("org.hupo.psi.ms.traml.PrecursorType", 40);
        iRankMap.put("org.hupo.psi.ms.traml.ProductType", 41);
    }
    /**
     *
     * @{@inheritDoc}
     */
    public int compare(ColumnModel aColumnModelA, ColumnModel aColumnModelB) {
        String A = aColumnModelA.getClassName();
        String B = aColumnModelB.getClassName();

        int lRankA = iRankMap.get(A);
        int lRankB = iRankMap.get(B);

        boolean lResult = lRankA < lRankB;
        return lResult ? 1 : 0;
    }
}
