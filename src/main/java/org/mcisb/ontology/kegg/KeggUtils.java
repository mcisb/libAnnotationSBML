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

import java.io.*;
import java.net.*;
import java.nio.charset.*;
import java.util.*;
import org.mcisb.ontology.*;
import org.mcisb.util.*;

/**
 * 
 * @author Neil Swainston
 */
public class KeggUtils extends OntologySource
{
	/**
	 * 
	 */
	private final static String PATH_PREFIX = "path:"; //$NON-NLS-1$
	
	/**
	 * 
	 */
	private final static String REACTION_PREFIX = "rn:"; //$NON-NLS-1$
	
	/**
	 * 
	 */
	private final static String EMPTY_STRING = ""; //$NON-NLS-1$

	/**
	 *
	 * @param ontologyName
	 * @throws Exception
	 */
	public KeggUtils( final String ontologyName ) throws Exception
	{
		super( ontologyName );
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.mcisb.ontology.OntologySource#getOntologyTerm(java.lang.String)
	 */
	@Override
	public OntologyTerm getOntologyTerm( final String identifier ) throws Exception
	{
		if( identifier.contains( OntologyTerm.ENCODED_COLON ) )
		{
			return super.getOntologyTerm( identifier.substring( identifier.indexOf( OntologyTerm.ENCODED_COLON ) + 1 ) );
		}
		else if( identifier.contains( OntologyTerm.COLON ) )
		{
			return super.getOntologyTerm( identifier.substring( identifier.indexOf( OntologyTerm.COLON ) + 1 ) );
		}
		
		return super.getOntologyTerm( identifier );
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.mcisb.ontology.OntologySource#getOntologyTermFromId(java.lang.String)
	 */
	@Override
	public OntologyTerm getOntologyTermFromId( final String identifier ) throws Exception
	{
		final String ontologyName = ontology.getName();
		
		if( ontologyName.equals( Ontology.KEGG_COMPOUND ) )
		{
			return new KeggCompoundTerm( identifier );
		}
		if( ontologyName.equals( Ontology.KEGG_DRUG ) )
		{
			return new KeggDrugTerm( identifier );
		}
		else if( ontologyName.equals( Ontology.KEGG_REACTION ) )
		{
			return new KeggReactionTerm( identifier );
		}
		else if( ontologyName.equals( Ontology.KEGG_GENES ) )
		{
			return new KeggGeneTerm( identifier );
		}
		else if( ontologyName.equals( Ontology.KEGG_GENOME ) )
		{
			return new KeggGenomeTerm( identifier );
		}
		else if( ontologyName.equals( Ontology.KEGG_GLYCAN ) )
		{
			return new KeggGlycanTerm( identifier );
		}
		else if( ontologyName.equals( Ontology.KEGG_PATHWAY ) )
		{
			return new KeggPathwayTerm( identifier );
		}
		
		return null;
	}
	
	/**
	 * 
	 * @param keggOrganismId
	 * @return String[]
	 * @throws IOException
	 */
	public static String[] getPathwaysByOrganism( final String keggOrganismId ) throws IOException
	{
		final int ID_INDEX = 0;
		final Collection<String> ids = new TreeSet<>();
		BufferedReader reader = null;
		
		try
		{
			reader = new BufferedReader( new InputStreamReader( new URL( "http://rest.kegg.jp/list/pathway/" + keggOrganismId ).openStream(), Charset.defaultCharset() ) ); //$NON-NLS-1$
			String line = null;
			
			while( ( line = reader.readLine() ) != null )
			{
				final String[] tokens = line.split( "\t" ); //$NON-NLS-1$
				ids.add( tokens[ ID_INDEX ].replace( PATH_PREFIX, EMPTY_STRING ) );
			}
			
			return ids.toArray( new String[ ids.size() ] );
		}
		finally
		{
			if( reader != null )
			{
				reader.close();
			}
		}
	}
	
	/**
	 * 
	 * @param keggPathwayId
	 * @return String[]
	 * @throws IOException 
	 */
	public static String[] getReactionsByPathway( final String keggPathwayId ) throws IOException
	{
		final int ID_INDEX = 0;
		final Collection<String> ids = new TreeSet<>();
		BufferedReader reader = null;
		
		try
		{
			reader = new BufferedReader( new InputStreamReader( new URL( "http://rest.kegg.jp/list/reaction/" + keggPathwayId ).openStream(), Charset.defaultCharset() ) ); //$NON-NLS-1$
			String line = null;
			
			while( ( line = reader.readLine() ) != null )
			{
				final String[] tokens = line.split( "\t" ); //$NON-NLS-1$
				ids.add( tokens[ ID_INDEX ].replace( REACTION_PREFIX, EMPTY_STRING ) );
			}
			
			return ids.toArray( new String[ ids.size() ] );
		}
		finally
		{
			if( reader != null )
			{
				reader.close();
			}
		}
	}
	
	/**
	 * 
	 * @param keggOrganismId
	 * @return String[]
	 * @throws IOException 
	 */
	public static String[] getReactionsByOrganism( final String keggOrganismId ) throws IOException
	{
		final Collection<String> reactions = new LinkedHashSet<>();
		
		for( final String pathway : getPathwaysByOrganism( keggOrganismId ) )
		{
			reactions.addAll( Arrays.asList( getReactionsByPathway( pathway ) ) );
		}
		
		return reactions.toArray( new String[ reactions.size() ] );
	}
	
	/**
	 * 
	 * @param identifier
	 * @return String[]
	 * @throws IOException
	 */
	public static String[] getCompoundsByName( final String identifier ) throws IOException
	{
		return search( identifier, "compound" ); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @param identifier
	 * @return String[]
	 * @throws IOException
	 */
	public static String[] getDrugsByName( final String identifier ) throws IOException
	{
		return search( identifier, "drug" ); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @param identifier
	 * @return String[]
	 * @throws IOException
	 */
	public static String[] getGlycansByName( final String identifier ) throws IOException
	{
		return search( identifier, "glycan" ); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @param identifier
	 * @return String[]
	 * @throws Exception 
	 */
	public static String[] getReactionsByCompound( final String identifier ) throws Exception
	{
		return search( new KeggCompoundTerm( CollectionUtils.getFirst( getCompoundsByName( identifier ) ) ).getName(), "reaction" ); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @param keggCompoundId
	 * @return OntologyTerm
	 * @throws Exception
	 */
	public static OntologyTerm getChebiXref( final String keggCompoundId ) throws Exception
	{
		return getLinkDBRelationTerm( keggCompoundId, Ontology.CHEBI );
	}
	
	/**
	 * 
	 * @param keggCompoundId
	 * @return OntologyTerm
	 * @throws Exception
	 */
	public static OntologyTerm getPubChemXref( final String keggCompoundId ) throws Exception
	{
		return getLinkDBRelationTerm( keggCompoundId, Ontology.PUBCHEM_SUBSTANCE );
	}
	
	/**
	 * 
	 * @param resultsArray
	 * @return Collection<OntologyTerm>
	 * @throws Exception
	 */
	protected Collection<OntologyTerm> search( final String[] resultsArray ) throws Exception
	{
		final Collection<OntologyTerm> ontologyTerms = new HashSet<>();
		final Collection<String> searchResults = Arrays.asList( resultsArray );

		for( Iterator<String> iterator = searchResults.iterator(); iterator.hasNext(); )
    	{
			ontologyTerms.add( getOntologyTerm( iterator.next() ) );
    	}
		
		return ontologyTerms;
	}
	
	/**
	 * 
	 * @param keggCompoundId
	 * @return OntologyTerm
	 * @throws Exception
	 */
	private static OntologyTerm getLinkDBRelationTerm( final String keggCompoundId, final String ontologyName ) throws Exception
	{
		if( ontologyName.equals( Ontology.CHEBI ) || ontologyName.equals( Ontology.PUBCHEM_SUBSTANCE ) )
		{
			final int ID_INDEX = 1;
			BufferedReader reader = null;
			
			try
			{
				final String database = ontologyName.equals( Ontology.CHEBI ) ? "chebi" : "pubchem"; //$NON-NLS-1$ //$NON-NLS-2$
				reader = new BufferedReader( new InputStreamReader( new URL( "http://rest.kegg.jp/conv/" + database + "/cpd:" + keggCompoundId ).openStream(), Charset.defaultCharset() ) ); //$NON-NLS-1$ //$NON-NLS-2$
				String line = null;
				
				while( ( line = reader.readLine() ) != null )
				{
					if( line.length() > 0 )
					{
						final String[] tokens = line.split( "\t" ); //$NON-NLS-1$
						final String id = tokens[ ID_INDEX ].replaceAll( database + ":", EMPTY_STRING ); //$NON-NLS-1$
						return OntologyUtils.getInstance().getOntologyTerm( ontologyName, id );
					}
				}
			}
			finally
			{
				if( reader != null )
				{
					reader.close();
				}
			}
		}
		
		return null;
	}
	
	/**
	 * 
	 * @param searchTerm
	 * @param database
	 * @return String[]
	 * @throws IOException
	 */
	public static String[] search( final String searchTerm, final String database ) throws IOException
	{
		final int ID_INDEX = 0;
		final Collection<String> ids = new TreeSet<>();
		BufferedReader reader = null;
		
		try
		{
			reader = new BufferedReader( new InputStreamReader( new URL( "http://rest.kegg.jp/find/" + database + "/" + searchTerm ).openStream(), Charset.defaultCharset() ) ); //$NON-NLS-1$ //$NON-NLS-2$
			String line = null;
			
			while( ( line = reader.readLine() ) != null )
			{
				if( line.length() > 0 )
				{
					final String[] tokens = line.split( "\t" ); //$NON-NLS-1$
					ids.add( tokens[ ID_INDEX ] );
				}
			}
			
			return ids.toArray( new String[ ids.size() ] );
		}
		finally
		{
			if( reader != null )
			{
				reader.close();
			}
		}
	}
}