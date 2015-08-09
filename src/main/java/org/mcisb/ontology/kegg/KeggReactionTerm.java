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
public class KeggReactionTerm extends KeggTerm
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private static final String ID_PREFIX = "rn" + OntologyTerm.ENCODED_COLON; //$NON-NLS-1$

	/**
	 * 
	 */
	private static final String REACTION_SEPARATOR = "<==>"; //$NON-NLS-1$

	/**
	 * 
	 */
	private final Map<OntologyTerm,Double> substrates = new HashMap<>();

	/**
	 * 
	 */
	private final Map<OntologyTerm,Double> products = new HashMap<>();

	/**
	 * 
	 */
	private String equation = null;

	/**
	 * 
	 */
	private String definition = null;

	/**
	 * 
	 * @param id
	 * @throws Exception
	 */
	public KeggReactionTerm( final String id ) throws Exception
	{
		super( Ontology.KEGG_REACTION, id, ID_PREFIX );
	}

	/**
	 * 
	 * @return String
	 * @throws Exception
	 */
	public String getEquation() throws Exception
	{
		init();

		return equation;
	}

	/**
	 * 
	 * @return Collection
	 * @throws Exception
	 */
	public Map<OntologyTerm,Double> getSubstrates() throws Exception
	{
		init();

		if( substrates.size() == 0 )
		{
			initReactionTerms();
		}

		return substrates;
	}

	/**
	 * 
	 * @return Collection
	 * @throws Exception
	 */
	public Map<OntologyTerm,Double> getProducts() throws Exception
	{
		init();

		if( products.size() == 0 )
		{
			initReactionTerms();
		}

		return products;
	}

	/**
	 * 
	 * @return Collection<OntologyTerm>
	 * @throws Exception
	 */
	public Collection<OntologyTerm> getPathways() throws Exception
	{
		init();
		return OntologyUtils.getInstance().getXrefs( xrefs.keySet(), Ontology.KEGG_PATHWAY );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mcisb.ontology.OntologyTerm#toString()
	 */
	@Override
	public synchronized String toString()
	{
		try
		{
			init();
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}

		return definition;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mcisb.ontology.kegg.KeggTerm#parseProperty(java.lang.String,
	 * java.util.List)
	 */
	@Override
	protected void parseProperty( final String propertyName, final List<String> values ) throws Exception
	{
		super.parseProperty( propertyName, values );

		final String ENZYME = "ENZYME"; //$NON-NLS-1$
		final String EQUATION = "EQUATION"; //$NON-NLS-1$
		final String DEFINITION = "DEFINITION"; //$NON-NLS-1$
		final String PATHWAY = "PATHWAY"; //$NON-NLS-1$
		final String OPEN_BRACKET = "\\["; //$NON-NLS-1$
		final String CLOSE_BRACKET = "\\]"; //$NON-NLS-1$
		final String EMPTY_STRING = ""; //$NON-NLS-1$
		final String PATHWAY_REGEX = "(?<=.*)rn(\\d+)(?=.*)"; //$NON-NLS-1$
		final String valuesString = getPropertyString( values );

		if( propertyName.equals( ENZYME ) )
		{
			for( Iterator<String> iterator = RegularExpressionUtils.getMatches( valuesString, RegularExpressionUtils.EC_REGEX ).iterator(); iterator.hasNext(); )
			{
				final String ec = iterator.next();
				addXref( OntologyUtils.getInstance().getOntologyTerm( Ontology.EC, ec ), CVTerm.Type.BIOLOGICAL_QUALIFIER, CVTerm.Qualifier.BQB_IS_VERSION_OF );
			}
		}
		else if( propertyName.equals( EQUATION ) )
		{
			equation = valuesString.replaceAll( OPEN_BRACKET, EMPTY_STRING ).replaceAll( CLOSE_BRACKET, EMPTY_STRING );
		}
		else if( propertyName.equals( DEFINITION ) )
		{
			definition = valuesString.replaceAll( OPEN_BRACKET, EMPTY_STRING ).replaceAll( CLOSE_BRACKET, EMPTY_STRING );
		}
		else if( propertyName.equals( PATHWAY ) )
		{
			for( Iterator<String> iterator = RegularExpressionUtils.getMatches( valuesString, PATHWAY_REGEX ).iterator(); iterator.hasNext(); )
			{
				final String pathwayId = iterator.next();
				addXref( OntologyUtils.getInstance().getOntologyTerm( Ontology.KEGG_PATHWAY, pathwayId ), CVTerm.Type.BIOLOGICAL_QUALIFIER, CVTerm.Qualifier.BQB_IS_PART_OF );
			}
		}
	}

	/**
	 * 
	 * @throws Exception
	 */
	private void initReactionTerms() throws Exception
	{
		if( equation != null )
		{
			final StringTokenizer tokenizer = new StringTokenizer( equation, REACTION_SEPARATOR );
			convert( tokenizer.nextToken().trim(), substrates );
			convert( tokenizer.nextToken().trim(), products );
		}
	}

	/**
	 * 
	 * @param term
	 * @param collection
	 * @throws Exception
	 */
	private static void convert( final String term, final Map<OntologyTerm,Double> collection ) throws Exception
	{
		final double DEFAULT_STOICHIOMETRY = 1.0;
		final String N = "(n)"; //$NON-NLS-1$
		final String N_PLUS_ONE = "(n+1)"; //$NON-NLS-1$
		final String M = "(m)"; //$NON-NLS-1$
		final String N_PLUS_M = "(n+m)"; //$NON-NLS-1$
		final String EMPTY_STRING = ""; //$NON-NLS-1$
		final KeggUtils keggCompoundUtils = KeggCompoundUtils.getInstance();
		final KeggUtils keggGlycanUtils = KeggGlycanUtils.getInstance();
		final String SEPARATOR = " \\+ "; //$NON-NLS-1$
		final String[] tokens = term.split( SEPARATOR );

		for( String token : tokens )
		{
			final StringTokenizer termTokenizer = new StringTokenizer( token.trim() );
			double stoichiometry = DEFAULT_STOICHIOMETRY;

			if( termTokenizer.countTokens() == 2 )
			{
				final String termToken = termTokenizer.nextToken();

				if( NumberUtils.isDecimal( termToken ) )
				{
					stoichiometry = Double.parseDouble( termToken );
				}
			}

			final String identifier = termTokenizer.nextToken().replace( N, EMPTY_STRING ).replace( N_PLUS_ONE, EMPTY_STRING ).replace( M, EMPTY_STRING ).replace( N_PLUS_M, EMPTY_STRING );
			final KeggUtils keggUtils = identifier.startsWith( "G" ) ? keggGlycanUtils : keggCompoundUtils; //$NON-NLS-1$

			collection.put( keggUtils.getOntologyTerm( identifier ), Double.valueOf( stoichiometry ) );
		}
	}
}