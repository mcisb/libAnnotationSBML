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
package org.mcisb.ontology.taxonomy;

import org.mcisb.ontology.*;

/**
 * 
 * @author Neil Swainston
 */
public class TaxonomyUtils extends DefaultOntologySource
{
	/**
	 * 
	 */
	private static TaxonomyUtils utils = null;

	/**
	 * 
	 * @return ChebiUtils
	 * @throws Exception
	 */
	public synchronized static TaxonomyUtils getInstance() throws Exception
	{
		if( utils == null )
		{
			utils = new TaxonomyUtils();
		}

		return utils;
	}

	/**
	 * 
	 * @throws Exception
	 */
	private TaxonomyUtils() throws Exception
	{
		super( Ontology.TAXONOMY );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mcisb.ontology.OntologySource#getOntologyTerm(java.lang.String)
	 */
	@Override
	public OntologyTerm getOntologyTermFromId( final String id ) throws Exception
	{
		return new TaxonomyTerm( id );
	}
}