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
import org.mcisb.util.*;

/**
 *
 * @author Neil Swainston
 */
public class KeggGeneUtils extends KeggUtils
{
	/**
	 * 
	 */
	private static KeggGeneUtils utils = null;
	
	/**
	 * 
	 * @return ChebiUtils
	 * @throws Exception
	 */
	public synchronized static KeggGeneUtils getInstance() throws Exception
	{
		if( utils == null )
		{
			utils = new KeggGeneUtils();
		}
		
		return utils;
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	private KeggGeneUtils() throws Exception
	{
		super( Ontology.KEGG_GENES );
	}

	/*
	 * (non-Javadoc)
	 * @see org.mcisb.ontology.kegg.KeggUtils#getOntologyTerm(java.lang.String)
	 */
	@Override
	public OntologyTerm getOntologyTerm( final String identifier ) throws Exception
	{
		final Collection<String> matches = RegularExpressionUtils.getMatches( identifier.replaceAll( OntologyTerm.ENCODED_COLON, OntologyTerm.COLON ), ontology.getRegularExpression() );
		
		if( matches.size() == 1 )
		{
			return getOntologyTermFromId( CollectionUtils.getFirst( matches ) );
		}
		
		return null;
	}
}