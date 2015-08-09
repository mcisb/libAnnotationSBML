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
package org.mcisb.ontology.gene;

import org.mcisb.ontology.*;

/**
 * 
 * @author Neil Swainston
 */
public class GeneUtils extends OntologySource
{
	/**
	 * 
	 */
	private static GeneUtils utils = null;
	
	/**
	 * 
	 * @return ChebiUtils
	 * @throws Exception
	 */
	public synchronized static GeneUtils getInstance() throws Exception
	{
		if( utils == null )
		{
			utils = new GeneUtils();
		}
		
		return utils;
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	private GeneUtils() throws Exception
	{
		super( Ontology.NCBI_GENE );
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.mcisb.ontology.OntologySource#getOntologyTermFromId(java.lang.String)
	 */
	@Override
	protected OntologyTerm getOntologyTermFromId( final String id ) throws Exception
	{
		return new GeneTerm( id );
	}
}