/*******************************************************************************
 * Manchester Centre for Integrative Systems Biology
 * University of Manchester
 * Manchester M1 7ND
 * United Kingdom
 * 
 * Copyright (C) 2008 University of Manchester
 * 
 * This program is released under the Academic Free License ("AFL") v3.0.
 * (http://www.opensource.org/licenses/academic.php)
 *******************************************************************************/
package org.mcisb.ontology.kegg;

import java.util.*;
import org.mcisb.ontology.*;
import org.mcisb.ontology.ec.*;
import org.mcisb.util.*;
import org.sbml.jsbml.*;

/**
 * @author Neil Swainston
 */
public class KeggGeneTerm extends KeggTerm
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param id
	 * @throws Exception
	 */
	public KeggGeneTerm( String id ) throws Exception
	{
		super( Ontology.KEGG_GENES, id, null );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mcisb.ontology.kegg.KeggTerm#parseProperty(java.lang.String,
	 * java.util.List)
	 */
	@Override
	protected void parseProperty( String propertyName, List<String> values ) throws Exception
	{
		final String DEFINITION = "DEFINITION"; //$NON-NLS-1$
		final String ORTHOLOGY = "ORTHOLOGY"; //$NON-NLS-1$

		super.parseProperty( propertyName, values );

		if( propertyName.equals( DEFINITION ) )
		{
			for( String value : values )
			{
				final String ecTerm = CollectionUtils.getFirst( RegularExpressionUtils.getMatches( value, RegularExpressionUtils.EC_REGEX ) );

				if( ecTerm != null )
				{
					addXref( EcUtils.getInstance().getOntologyTerm( ecTerm ), CVTerm.Type.BIOLOGICAL_QUALIFIER, CVTerm.Qualifier.BQB_IS_VERSION_OF );
				}
			}
		}
		else if( propertyName.equals( ORTHOLOGY ) )
		{
			for( String value : values )
			{
				final String ecTerm = CollectionUtils.getFirst( RegularExpressionUtils.getMatches( value, RegularExpressionUtils.EC_REGEX ) );

				if( ecTerm != null )
				{
					addXref( EcUtils.getInstance().getOntologyTerm( ecTerm ), CVTerm.Type.BIOLOGICAL_QUALIFIER, CVTerm.Qualifier.BQB_IS_VERSION_OF );
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mcisb.ontology.kegg.KeggTerm#addXref(org.mcisb.ontology.OntologyTerm)
	 */
	@Override
	protected void addXref( final OntologyTerm xref )
	{
		addXref( xref, CVTerm.Type.BIOLOGICAL_QUALIFIER, xref.getOntologyName().equals( Ontology.UNIPROT ) ? CVTerm.Qualifier.BQB_ENCODES : CVTerm.Qualifier.BQB_IS );
	}
}
