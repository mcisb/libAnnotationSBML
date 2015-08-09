/*******************************************************************************
 * Manchester Centre for Integrative Systems Biology
 * University of Manchester
 * Manchester M1 7ND
 * United Kingdom
 * 
 * Copyright (C) 2007 University of Manchester
 * 
 * This program is released under the Academic Free License ("AFL") v3.0.
 * (http://www.opensource.org/licenses/academic.php)
 *******************************************************************************/
package org.mcisb.ontology.chebi;

import uk.ac.ebi.chebi.webapps.chebiWS.client.*;

/**
 * 
 * @author Neil Swainston
 */
public class ChebiWebServiceClientFactory
{
	/**
	 * 
	 */
	public static final int MAXIMUM_SEARCH_RESULTS = 50;

	/**
	 * 
	 */
	private static ChebiWebServiceClient client = null;

	/**
	 * 
	 * @return ChebiWebServiceClient
	 */
	public synchronized static ChebiWebServiceClient getClient()
	{
		if( client == null )
		{
			client = new ChebiWebServiceClient();
		}

		return client;
	}
}