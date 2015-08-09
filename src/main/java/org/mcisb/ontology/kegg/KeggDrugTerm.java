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
public class KeggDrugTerm extends KeggTerm
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private static final String ID_PREFIX = "dr" + OntologyTerm.ENCODED_COLON; //$NON-NLS-1$
	
	/**
	 * 
	 */
	private String formula = null;
	
	/**
	 *
	 * @param id
	 * @throws Exception
	 */
	public KeggDrugTerm( final String id ) throws Exception
	{
		super( Ontology.KEGG_DRUG, id, ID_PREFIX );
	}
	
	/**
	 * 
	 * @return String
	 * @throws Exception
	 */
	public String getFormula() throws Exception
	{
		init();
		return formula;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.mcisb.ontology.kegg.KeggReactionParticipantTerm#parseProperty(java.lang.String, java.util.List)
	 */
	@Override
	protected void parseProperty( final String propertyName, final List<String> values ) throws Exception
	{
		final String NAME = "NAME"; //$NON-NLS-1$
		final String FORMULA = "FORMULA"; //$NON-NLS-1$
		final String SEMI_COLON = ";"; //$NON-NLS-1$
		final String EMPTY_STRING = ""; //$NON-NLS-1$
		
		if( propertyName.equals( NAME ) )
		{
			final List<String> allNames = new ArrayList<>();
			
			for( Iterator<String> iterator = values.iterator(); iterator.hasNext(); )
			{
				allNames.add( iterator.next() );
			}
			
			name = CollectionUtils.getFirst( allNames ).replace( SEMI_COLON, EMPTY_STRING ).trim();
		
			for( int i = 1; i < allNames.size(); i++ )
       		{
    			addSynonym( allNames.get( i ).replace( SEMI_COLON, EMPTY_STRING ).trim() );
       		}
		}
		else if( propertyName.equals( FORMULA ) )
		{
			formula = CollectionUtils.getFirst( values ).trim();
		}
		else
		{
			super.parseProperty( propertyName, values );
		}
	}
}