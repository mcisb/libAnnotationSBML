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
package org.mcisb.ontology.ec;

import org.mcisb.ontology.*;
import org.mcisb.ontology.kegg.*;

/**
 *
 * @author Neil Swainston
 */
public class EcUtils extends KeggUtils
{
	/**
	 * 
	 */
	private static EcUtils utils = null;
	
	/**
	 *
	 * @return EcTermUtils
	 * @throws Exception
	 */
	public synchronized static EcUtils getInstance() throws Exception
	{
		if( utils == null )
		{
			utils = new EcUtils();
		}
		
		return utils;
	}
	
	/**
	 *
	 * @throws Exception
	 */
	private EcUtils() throws Exception
	{
		super( Ontology.EC );
	}

	/*
	 * (non-Javadoc)
	 * @see org.mcisb.ontology.kegg.KeggUtils#getOntologyTermFromId(java.lang.String)
	 */
	@Override
	public OntologyTerm getOntologyTermFromId( final String identifier ) throws Exception
	{
		return new EcTerm( identifier );
	}
	
	/**
	 * 
	 * @param term1
	 * @param term2
	 * @return boolean
	 * @throws Exception 
	 */
	public boolean matches( final String term1, final String term2 ) throws Exception
	{
		final String term1Id = getOntologyTermFromId( term1 ).getId();
		final String term2Id = getOntologyTermFromId( term2 ).getId();
		return term2Id.matches( term1Id.replaceAll( "-", "[\\\\d]+" ).replaceAll( "\\.", "\\\\." ) ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}
}