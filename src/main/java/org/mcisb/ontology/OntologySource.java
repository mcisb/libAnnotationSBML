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
package org.mcisb.ontology;

import java.util.*;
import org.mcisb.util.*;

/**
 * 
 * @author Neil Swainston
 */
public abstract class OntologySource
{
	/**
	 * 
	 */
	protected final Ontology ontology;
	
	/**
	 * 
	 */
	private final Map<String,OntologyTerm> identifierToTerm = new HashMap<>();
	
	/**
	 *
	 * @param ontologyName
	 * @throws Exception
	 */
	public OntologySource( final String ontologyName ) throws Exception
	{
		this.ontology = OntologyFactory.getOntology( ontologyName );
	}
	
	/**
	 * 
	 * @param identifier
	 * @return OntologyTerm
	 * @throws Exception
	 */
	public OntologyTerm getOntologyTerm( final String identifier ) throws Exception
	{
		OntologyTerm ontologyTerm = identifierToTerm.get( identifier );
		
		if( ontologyTerm != null )
		{
			return ontologyTerm;
		}
		
		final Collection<String> matches = RegularExpressionUtils.getMatches( identifier, ontology.getRegularExpression() );
		
		if( matches.size() == 1 )
		{
			ontologyTerm = getOntologyTermFromId( CollectionUtils.getFirst( matches ) );
			identifierToTerm.put( identifier, ontologyTerm );
			return ontologyTerm;
		}
		
		final Collection<OntologyTerm> ontologyTerms = search( identifier );
		
		for( Iterator<OntologyTerm> iterator = ontologyTerms.iterator(); iterator.hasNext(); )
		{
			ontologyTerm = iterator.next();
			
			if( ontologyTerm.getName().equalsIgnoreCase( identifier ) )
			{
				identifierToTerm.put( identifier, ontologyTerm );
				return ontologyTerm;
			}
		}
		
		for( Iterator<OntologyTerm> iterator = ontologyTerms.iterator(); iterator.hasNext(); )
		{
			ontologyTerm = iterator.next();
			
			if( ontologyTerm.getId().contains( identifier ) )
			{
				identifierToTerm.put( identifier, ontologyTerm );
				return ontologyTerm;
			}
		}
		
		for( Iterator<OntologyTerm> iterator = ontologyTerms.iterator(); iterator.hasNext(); )
		{
			ontologyTerm = iterator.next();
			
			if( matchesSynonyms( identifier, ontologyTerm ) )
			{
				identifierToTerm.put( identifier, ontologyTerm );
				return ontologyTerm;
			}
		}
		
		return null;
	}
	
	/**
	 * @param id
	 * @return OntologyTerm
	 * @throws Exception 
	 */
	@SuppressWarnings("unused")
	protected OntologyTerm getOntologyTermFromId( final String id ) throws Exception
	{
		return new OntologyTerm( ontology, id );
	}
	
	/**
	 * @param identifier
	 * @return Collection
	 * @throws Exception
	 */
	@SuppressWarnings({ "static-method", "unused" })
	public Collection<OntologyTerm> search( final String identifier ) throws Exception
	{
		return new ArrayList<>();
	}
	
	/**
	 * 
	 * @return String
	 */
	@SuppressWarnings("static-method")
	public String getSearchWildcard()
	{
		final String EMPTY_STRING = ""; //$NON-NLS-1$
		return EMPTY_STRING;
	}
	
	/**
	 *
	 * @param identifier
	 * @param ontologyTerm
	 * @return boolean
	 * @throws Exception
	 */
	private static boolean matchesSynonyms( final String identifier, final OntologyTerm ontologyTerm ) throws Exception
	{
		for( Iterator<String> iterator = ontologyTerm.getSynonyms().iterator(); iterator.hasNext(); )
		{
			final String synonym = iterator.next();
			
			if( identifier.equalsIgnoreCase( synonym ) )
			{
				return true;
			}
		}
		
		return false;
	}
}