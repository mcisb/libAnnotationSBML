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
import org.sbml.jsbml.*;

/**
 * 
 * @author Neil Swainston
 */
public class KeggGenomeTerm extends KeggTerm
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private static final String ID_PREFIX = "gn" + OntologyTerm.ENCODED_COLON; //$NON-NLS-1$
	
	/**
	 * 
	 */
	private static final String TAXONOMY = "TAXONOMY"; //$NON-NLS-1$
	
	/**
	 * 
	 */
	private static final String DEFINITION = "DEFINITION"; //$NON-NLS-1$

	
	/**
	 * @param id
	 * @throws Exception
	 */
	public KeggGenomeTerm( final String id ) throws Exception
	{
		super( Ontology.KEGG_GENOME, id, ID_PREFIX );
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.mcisb.ontology.kegg.KeggTerm#parseProperty(java.lang.String, java.util.List)
	 */
	@Override
	protected void parseProperty( final String propertyName, final List<String> values ) throws Exception
	{
		super.parseProperty( propertyName, values );
		
		if( propertyName.equals( TAXONOMY ) )
		{
			final String SEPARATOR = ":"; //$NON-NLS-1$
			String taxonomyId = CollectionUtils.getFirst( values );
			taxonomyId = taxonomyId.substring( taxonomyId.indexOf( SEPARATOR ) + SEPARATOR.length() );
			addXref( OntologyUtils.getInstance().getOntologyTerm( Ontology.TAXONOMY, taxonomyId ), CVTerm.Type.BIOLOGICAL_QUALIFIER, CVTerm.Qualifier.BQB_IS );
		}
		else if( propertyName.equals( DEFINITION ) )	
		{
			setName( CollectionUtils.getFirst( values ) );
		}
	}
}
