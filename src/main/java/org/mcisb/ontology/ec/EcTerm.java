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

import java.util.*;
import org.mcisb.ontology.*;
import org.mcisb.ontology.kegg.*;
import org.sbml.jsbml.*;

// import org.mcisb.util.*;

/**
 * 
 * @author Neil Swainston
 */
public class EcTerm extends KeggReactionParticipantTerm
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private static final String ID_PREFIX = "ec" + OntologyTerm.ENCODED_COLON; //$NON-NLS-1$

	/**
	 * 
	 */
	private static final String ARATH_PREFIX = "ath" + OntologyTerm.ENCODED_COLON; //$NON-NLS-1$

	/**
	 * 
	 * @param id
	 * @throws Exception
	 */
	public EcTerm( final String id ) throws Exception
	{
		super( Ontology.EC, id, ID_PREFIX );
	}

	/**
	 * 
	 * @param propertyName
	 * @param values
	 * @throws Exception
	 */
	@Override
	protected void parseProperty( final String propertyName, final List<String> values ) throws Exception
	{
		super.parseProperty( propertyName, values );

		final String GENES = "GENES"; //$NON-NLS-1$
		final String SEPARATOR = "\\s+"; //$NON-NLS-1$
		final String OPEN_BRACKET = "("; //$NON-NLS-1$

		final KeggGeneUtils utils = KeggGeneUtils.getInstance();

		if( propertyName.equals( GENES ) )
		{
			String organismId = null;

			for( Iterator<String> iterator = values.iterator(); iterator.hasNext(); )
			{
				final String line = iterator.next();
				final int space = line.indexOf( ':' ) + 1;
				String geneIdLine = null;

				if( space > 0 )
				{
					organismId = line.substring( 0, space ).trim().toLowerCase( Locale.getDefault() );
					geneIdLine = line.substring( space );
				}
				else
				{
					geneIdLine = line;
				}

				final String[] geneIds = geneIdLine.trim().split( SEPARATOR );

				for( int i = 0; i < geneIds.length; i++ )
				{
					String geneId = geneIds[ i ];

					if( geneId.contains( OPEN_BRACKET ) )
					{
						geneId = geneId.substring( 0, geneId.indexOf( OPEN_BRACKET ) );
					}

					if( organismId != null && organismId.equals( ARATH_PREFIX ) )
					{
						final OntologyTerm tairLocusTerm = OntologyUtils.getInstance().getOntologyTerm( Ontology.TAIR_LOCUS, geneId );
						addXref( tairLocusTerm, CVTerm.Type.BIOLOGICAL_QUALIFIER, CVTerm.Qualifier.BQB_HAS_VERSION );

						final OntologyTerm keggGeneTerm = utils.getOntologyTerm( organismId + geneId );
						final Map<OntologyTerm,Object[]> keggGeneTerms = new HashMap<>();
						keggGeneTerms.put( keggGeneTerm, new Object[] { CVTerm.Type.BIOLOGICAL_QUALIFIER, CVTerm.Qualifier.BQB_IS } );

						// final OntologyTerm uniProtTerm =
						// (OntologyTerm)CollectionUtils.getFirst(
						// OntologyUtils.getXrefs( keggGeneTerms,
						// Ontology.UNIPROT ).keySet() );
						// addXref( uniProtTerm,
						// libsbmlConstants.BIOLOGICAL_QUALIFIER,
						// libsbmlConstants.BQB_HAS_VERSION );
					}
				}
			}
		}
	}
}