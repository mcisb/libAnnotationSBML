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
package org.mcisb.ontology;

import java.lang.reflect.*;
import java.util.*;
import org.json.simple.*;
import org.mcisb.ontology.kegg.*;

/**
 * @author Neil Swainston
 */
public class OntologyTermFactory
{
	/**
	 * 
	 */
	private final OntologyUtils utils = OntologyUtils.getInstance();
	
	/**
	 * @throws Exception 
	 */
	public OntologyTermFactory() throws Exception
	{
		// No implementation.
	}
	
	/**
	 * 
	 * @param ontologyName
	 * @param identifier
	 * @return String
	 * @throws Exception
	 */
	public String search( final String ontologyName, final String identifier ) throws Exception
	{
		final Collection<Collection<String>> values = new ArrayList<>();
		final OntologySource ontologySource = utils.getOntologySource( ontologyName );
		
		if( ontologySource != null )
		{
			for( final OntologyTerm ontologyTerm : ontologySource.search( identifier ) )
			{
				values.add( Arrays.asList( ontologyTerm.toUri(), ontologyTerm.getName() ) );
			}
		}
			 
		return getJson( values );
	}
	
	/**
	 * 
	 * @param urn
	 * @return String
	 * @throws Exception
	 */
	public String getLink( final String urn ) throws Exception
	{
		final OntologyTerm ontologyTerm = getOntologyTerm( urn );
		String link = null;
		
		if( ontologyTerm != null )
		{
			link = ontologyTerm.getLink();
		}
			 
		return getJson( link );
	}
	
	/**
	 * 
	 * @param urn
	 * @return String
	 * @throws Exception
	 */
	public String getName( final String urn ) throws Exception
	{
		final OntologyTerm ontologyTerm = getOntologyTerm( urn );
		String name = null;
		
		if( ontologyTerm != null )
		{
			name = ontologyTerm.getName();
		}
			 
		return getJson( name );
	}
	
	/**
	 * 
	 * @param urn
	 * @return String
	 * @throws Exception
	 */
	public String getSynonyms( final String urn ) throws Exception
	{
		final OntologyTerm ontologyTerm = getOntologyTerm( urn );
		final Collection<String> synonyms = new ArrayList<>();
		
		if( ontologyTerm != null )
		{
			synonyms.addAll( ontologyTerm.getSynonyms() );
		}
			 
		return getJson( synonyms );
	}
	
	/**
	 * 
	 * @param urn
	 * @return String
	 * @throws Exception
	 */
	public String getXrefs( final String urn ) throws Exception
	{
		final Collection<String> xrefIds = new ArrayList<>();
		final OntologyTerm ontologyTerm = getOntologyTerm( urn );
		
		if( ontologyTerm != null )
		{
			for( final OntologyTerm xref : ontologyTerm.getXrefs().keySet() )
			{
				xrefIds.add( xref.toUri() );
			}
		}
			 
		return getJson( xrefIds );
	}
	
	/**
	 * 
	 * @param urn
	 * @return String
	 * @throws Exception
	 */
	public String getReactions( final String urn ) throws Exception
	{
		final Collection<String> keggReactionUrns = new ArrayList<>();
		final OntologyTerm ontologyTerm = getOntologyTerm( urn );
		
		if( ontologyTerm != null && ontologyTerm instanceof KeggReactionParticipantTerm )
		{
			for( final String reactionId : ( (KeggReactionParticipantTerm)ontologyTerm ).getReactions() )
			{
				keggReactionUrns.add( KeggReactionUtils.getInstance().getOntologyTermFromId( reactionId ).toUri() );
			}
		}
			 
		return getJson( keggReactionUrns );
	}
	
	/**
	 * @param urn 
	 * @return OntologyTerm
	 * @throws Exception 
	 */
	private OntologyTerm getOntologyTerm( final String urn ) throws Exception
	{
		return utils.getOntologyTerm( urn );
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 * @return String
	 */
	private static String getJson( final Object value )
	{
		return JSONValue.toJSONString( value );
	}
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main( String[] args ) throws Exception
	{
		final String SEARCH = "search"; //$NON-NLS-1$
		final Method[] methods = OntologyTermFactory.class.getDeclaredMethods();
		
		for( int i = 0; i < methods.length; i++ )
		{
			final String methodName = methods[ i ].getName();
			
			if( methodName.equals( args[ 0 ] ) )
			{
				if( methodName.equals( SEARCH ) )
				{
					final String SPACE = " "; //$NON-NLS-1$
					OntologySource ontologySource = null;
					final StringBuffer ontologyName = new StringBuffer();
					final StringBuffer identifier = new StringBuffer();
					
					for( int j = 1; j < args.length; j++ )
					{
						if( ontologySource != null )
						{
							identifier.append( args[ j ] );
							identifier.append( SPACE );
						}
						else
						{
							ontologyName.append( args[ j ] );
							ontologyName.append( SPACE );
							ontologySource = OntologyUtils.getInstance().getOntologySource( ontologyName.toString().trim() );
						}
					}
					
					System.out.println( methods[ i ].invoke( OntologyTermFactory.class.newInstance(), new Object[] { ontologyName.toString().trim(), identifier.toString().trim() } ) );
				}
				else
				{
					final Object[] parameters = Arrays.copyOfRange( args, 1, args.length );
					System.out.println( methods[ i ].invoke( OntologyTermFactory.class.newInstance(), parameters ) );
				}
				
				break;
			}
		}
	}
}