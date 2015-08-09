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
package org.mcisb.ontology.kegg;

import java.util.*;
import org.mcisb.ontology.*;

/**
 * 
 * @author Neil Swainston
 */
public class KeggCompoundUtils extends KeggUtils
{
	/**
	 * 
	 */
	private static final int MINIMUM_SEARCH_STRING_LENGTH = 3;

	/**
	 * 
	 */
	private static KeggCompoundUtils utils = null;

	/**
	 * 
	 * @return ChebiUtils
	 * @throws Exception
	 */
	public synchronized static KeggCompoundUtils getInstance() throws Exception
	{
		if( utils == null )
		{
			utils = new KeggCompoundUtils();
		}

		return utils;
	}

	/**
	 * 
	 * @throws Exception
	 */
	private KeggCompoundUtils() throws Exception
	{
		super( Ontology.KEGG_COMPOUND );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mcisb.ontology.OntologySource#search(java.lang.String)
	 */
	@Override
	public Collection<OntologyTerm> search( final String identifier ) throws Exception
	{
		if( identifier != null && identifier.length() >= MINIMUM_SEARCH_STRING_LENGTH )
		{
			return super.search( getCompoundsByName( identifier ) );
		}

		return new HashSet<>();
	}
}