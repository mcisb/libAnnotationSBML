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
public class KeggDrugUtils extends KeggUtils
{
	/**
	 * 
	 */
	private static KeggDrugUtils utils = null;
	
	/**
	 * 
	 * @return ChebiUtils
	 * @throws Exception
	 */
	public synchronized static KeggDrugUtils getInstance() throws Exception
	{
		if( utils == null )
		{
			utils = new KeggDrugUtils();
		}
		
		return utils;
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	private KeggDrugUtils() throws Exception
	{
		super( Ontology.KEGG_DRUG );
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.mcisb.ontology.OntologySource#search(java.lang.String)
	 */
	@Override
	public Collection<OntologyTerm> search( final String identifier ) throws Exception
	{
		if( identifier != null && identifier.length() > 0 )
		{
			return super.search( getDrugsByName( identifier ) );
		}
		
		return new HashSet<>();
	}
}