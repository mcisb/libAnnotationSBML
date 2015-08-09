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
package org.mcisb.ontology.chebi;

import java.util.*;
import org.mcisb.ontology.*;
import org.mcisb.util.*;
import uk.ac.ebi.chebi.webapps.chebiWS.model.*;

/**
 * 
 * @author Neil Swainston
 */
public class ChebiUtils extends OntologySource
{
	/**
	 * 
	 */
	public static final String CHEBI_PREFIX = "CHEBI:"; //$NON-NLS-1$
	
	/**
	 * 
	 */
	public static final int UNDEFINED_CHARGE = NumberUtils.UNDEFINED;
	
	/**
	 * 
	 */
	private static ChebiUtils utils = null;
	
	/**
	 * 
	 */
	private final static int MAX_SEARCH_TRIES = 16;
	
	/**
	 * 
	 */
	private final static int MIN_SEARCH_TERM_LENGTH = 1;
	
	/**
	 * 
	 */
	private final static int MAX_SEARCH_TERM_LENGTH = 256;
	
	/**
	 * 
	 * @return ChebiUtils
	 * @throws Exception
	 */
	public synchronized static ChebiUtils getInstance() throws Exception
	{
		if( utils == null )
		{
			utils = new ChebiUtils();
		}
		
		return utils;
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	private ChebiUtils() throws Exception
	{
		super( Ontology.CHEBI );
	}

	/* 
	 * (non-Javadoc)
	 * @see org.mcisb.ontology.OntologySource#getOntologyTerm(java.lang.String)
	 */
	@Override
	public OntologyTerm getOntologyTerm( String identifier ) throws Exception
	{
		String updatedIdentifier = identifier.replaceAll( OntologyTerm.ENCODED_COLON, OntologyTerm.COLON );
		
		if( updatedIdentifier.matches( "\\d+" ) ) //$NON-NLS-1$
		{
			updatedIdentifier = "CHEBI:" + updatedIdentifier; //$NON-NLS-1$
		}
		
		return super.getOntologyTerm( updatedIdentifier );
	}

	/*
	 * (non-Javadoc)
	 * @see org.mcisb.ontology.OntologySource#getOntologyTermFromId(java.lang.String)
	 */
	@Override
	public OntologyTerm getOntologyTermFromId( final String id ) throws Exception
	{
		return new ChebiTerm( id );
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.mcisb.ontology.OntologySource#search(java.lang.String)
	 */
	@Override
	public Collection<OntologyTerm> search( final String identifier ) throws Exception
	{
		return search( identifier, ChebiWebServiceClientFactory.MAXIMUM_SEARCH_RESULTS );
	}
	
	/**
	 * 
	 * @param identifier
	 * @param maximumSearchResults
	 * @return Collection<OntologyTerm>
	 * @throws Exception
	 */
	public static Collection<OntologyTerm> search( final String identifier, final int maximumSearchResults ) throws Exception
	{
		final Collection<OntologyTerm> ontologyTerms = new LinkedHashSet<>();
		
		final int identifierLength = identifier.length();
		
		if( identifierLength >= MIN_SEARCH_TERM_LENGTH && identifierLength <= MAX_SEARCH_TERM_LENGTH )
		{
			for( int count = 1; count < MAX_SEARCH_TRIES; count++ )
			{
				try
				{
					final LiteEntityList entitiesFromIdentifier = ChebiWebServiceClientFactory.getClient().getLiteEntity( identifier, SearchCategory.ALL, maximumSearchResults, StarsCategory.ALL );
				    final List<LiteEntity> resultListFromIdentifier = entitiesFromIdentifier.getListElement();
					
			    	for( Iterator<LiteEntity> iterator = resultListFromIdentifier.iterator(); iterator.hasNext(); )
			    	{
			    		final LiteEntity liteEntity = iterator.next();
			    		final OntologyTerm ontologyTerm = new ChebiTerm( liteEntity.getChebiId() );
			    		ontologyTerm.setName( liteEntity.getChebiAsciiName() );
			    		ontologyTerms.add( ontologyTerm );
			    	}

					break;
				}
				catch( Exception e )
				{
					if( count >= MAX_SEARCH_TRIES )
					{
						throw e;
					}
				}
			}
		}

    	return ontologyTerms;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.mcisb.ontology.OntologySource#getSearchWildcard()
	 */
	@Override
	public String getSearchWildcard()
	{
		final String SEARCH_WILDCARD = "*"; //$NON-NLS-1$
		return SEARCH_WILDCARD;
	}
	
	/**
	 * 
	 * @param query
	 * @param target
	 * @param relations 
	 * @return boolean
	 * @throws Exception 
	 */
	public boolean hasParent( final ChebiTerm query, final ChebiTerm target, final Collection<String> relations ) throws Exception
	{
		if( query.equals( target ) )
		{
			return true;
		}
		
		for( Map.Entry<ChebiTerm,String> entry : query.getParents().entrySet() )
		{
			if( relations.contains( entry.getValue() ) )
			{
				final boolean hasParent = hasParent( entry.getKey(), target, relations );
				
				if( hasParent )
				{
					return true;
				}
			}
		}
		
		return false;
	}
}