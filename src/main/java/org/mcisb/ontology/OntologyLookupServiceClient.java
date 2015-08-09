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
package org.mcisb.ontology;

import java.io.*;
import java.rmi.*;
import java.util.*;
import javax.xml.rpc.*;
import uk.ac.ebi.ols.soap.*;

/**
 * 
 * @author Neil Swainston
 */
public class OntologyLookupServiceClient implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private static Query query;

	/**
	 * 
	 * @return Map
	 * @throws RemoteException
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public static Map<String,String> getOntologyNames() throws RemoteException, ServiceException
	{
		return getQuery().getOntologyNames();
	}

	/**
	 * 
	 * @param partialName
	 * @param ontologyName
	 * @param reverseKeyOrder
	 * @return Map
	 * @throws RemoteException
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public static Map<String,String> getTermsByName( final String partialName, final String ontologyName, final boolean reverseKeyOrder ) throws RemoteException, ServiceException
	{
		return getQuery().getTermsByName( partialName, ontologyName, reverseKeyOrder );
	}

	/**
	 * 
	 * @param termId
	 * @param ontologyName
	 * @return String
	 * @throws RemoteException
	 * @throws ServiceException
	 */
	public static String getTermById( final String termId, final String ontologyName ) throws RemoteException, ServiceException
	{
		return getQuery().getTermById( termId, ontologyName );
	}

	/**
	 * 
	 * @param termId
	 * @param ontologyName
	 * @return Map<String,String>
	 * @throws RemoteException
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public static Map<String,String> getParentTerms( final String termId, final String ontologyName ) throws RemoteException, ServiceException
	{
		return getQuery().getTermParents( termId, ontologyName );
	}

	/**
	 * 
	 * @return Query
	 * @throws ServiceException
	 */
	private static synchronized Query getQuery() throws ServiceException
	{
		if( query == null )
		{
			query = new QueryServiceLocator().getOntologyQuery();
		}

		return query;
	}
}