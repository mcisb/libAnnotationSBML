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
package org.mcisb.ontology.uniprot;

import java.io.*;
import java.nio.charset.*;
import java.util.*;
import org.mcisb.ontology.*;
import org.mcisb.util.io.*;

/**
 * 
 * @author Neil Swainston
 */
public class UniProtUtils extends DefaultOntologySource
{
	/**
	 * 
	 */
	private final static String UNIPROT_URL = "http://www.uniprot.org/uniprot/"; //$NON-NLS-1$
			
	/**
	 * 
	 */
	private String ncbiTaxonomyTerm = null;
	
	/**
	 * 
	 */
	private UniProtUtilsSearchOption searchOption = UniProtUtilsSearchOption.PROTEIN_NAME;
	
	/**
	 * 
	 * @throws Exception
	 */
	public UniProtUtils() throws Exception
	{
		super( Ontology.UNIPROT );
	}
	
	/**
	 * 
	 * @param searchOption
	 * @throws Exception
	 */
	public UniProtUtils( final UniProtUtilsSearchOption searchOption ) throws Exception
	{
		this();
		setSearchOption( searchOption );
	}
	
	/**
	 * 
	 * @param query
	 * @return Map<String,String>
	 * @throws Exception 
	 */
	public static Map<String,List<String>> searchRest( final String query ) throws Exception
	{
		final Map<String,List<String>> idToNames = new LinkedHashMap<>();
		
		final Map<String,String> parameters = new LinkedHashMap<>();
		parameters.put( "query",  query ); //$NON-NLS-1$
		parameters.put( "format", "tab" ); //$NON-NLS-1$ //$NON-NLS-2$
		parameters.put( "columns", "id,protein%20names" ); //$NON-NLS-1$ //$NON-NLS-2$
		
		final RestStringReader restReader = new RestStringReader( UNIPROT_URL, parameters );

		try( BufferedReader reader = new BufferedReader( new StringReader( (String)restReader.read() ) ) )
		{
			boolean first = true;
			String line = null;
			
			while( ( line = reader.readLine() ) != null )
			{
				if( first )
				{
					first = false;
				}
				else
				{
					final String[] tokens = line.split( "\t" ); //$NON-NLS-1$
					
					final String id = tokens[ 0 ].trim();
					final List<String> names = new ArrayList<>();
					
					for( String token : tokens[ 1 ].split( "\\(" ) ) //$NON-NLS-1$
					{
						names.add( token.replaceAll( "\\)", "" ).trim() ); //$NON-NLS-1$ //$NON-NLS-2$
					}
					
					idToNames.put( id, names );
				}
			}
			
			return idToNames;
		}
	}
	
	/**
	 * 
	 * @param accession
	 * @return List<String>
	 * @throws Exception 
	 */
	public static List<String> getNames( final String accession ) throws Exception
	{
		final List<String> allNames = new ArrayList<>();
		
		for( List<String> names : searchRest( "accession:" + accession ).values() ) //$NON-NLS-1$
		{
			allNames.addAll( names );
		}
		
		return allNames;
	}
	
	/**
	 * 
	 * @param searchOption
	 */
	public void setSearchOption( final UniProtUtilsSearchOption searchOption )
	{
		this.searchOption = searchOption;
	}

	/**
	 * 
	 * @param ncbiTaxonomyTerm
	 */
	public void setNcbiTaxonomyTerm( final String ncbiTaxonomyTerm )
	{
		this.ncbiTaxonomyTerm = ncbiTaxonomyTerm;
	}
	
	/* (non-Javadoc)
	 * @see org.mcisb.ontology.OntologySource#getOntologyTermFromId(java.lang.String)
	 */
	@Override
	public OntologyTerm getOntologyTermFromId( final String id ) throws Exception
	{
		return new UniProtTerm( id );
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.mcisb.ontology.OntologySource#search(java.lang.String)
	 */
	@Override
	public Collection<OntologyTerm> search( final String identifier ) throws Exception
	{
		switch( searchOption )
		{
			case PROTEIN_NAME:
			{
				return searchProteinName( identifier );
			}
			case GENE_NAME:
			{
				return searchGeneName( identifier );
			}
			case FULL_TEXT:
			{
				return searchFullText( identifier );
			}
			default:
			{
				return searchFullText( identifier );
			}
		}
	}
	
	/**
	 * @param identifier
	 * @return Collection
	 * @throws Exception
	 */
	public Collection<OntologyTerm> searchExactMatchIdentifier( final String identifier ) throws Exception
	{
		return doSearch( "mnemonic:" + identifier ); //$NON-NLS-1$
	}
	
	/**
	 * @param identifier
	 * @return Collection
	 * @throws Exception
	 */
	public Collection<OntologyTerm> searchProteinName( final String identifier ) throws Exception
	{
		return doSearch( "name:" + identifier ); //$NON-NLS-1$
	}
	
	/**
	 * @param identifier
	 * @return Collection
	 * @throws Exception
	 */
	public Collection<OntologyTerm> searchGeneName( final String identifier ) throws Exception
	{
		return doSearch( "gene:" + identifier ); //$NON-NLS-1$
	}
	
	/**
	 * @param identifier
	 * @return Collection
	 * @throws Exception
	 */
	public Collection<OntologyTerm> searchFullText( final String identifier ) throws Exception
	{
		return doSearch( identifier );
	}
	
	/**
	 * 
	 * @param uniProtTerms
	 * @param os
	 * @throws Exception  
	 */
	public static void writeToFasta( final Collection<UniProtTerm> uniProtTerms, final OutputStream os ) throws Exception
	{
		final String SEPARATOR = ">"; //$NON-NLS-1$
		BufferedWriter writer = null;
		
		try
		{
			writer = new BufferedWriter( new OutputStreamWriter( os, Charset.defaultCharset() ) );
			
			for( UniProtTerm uniProtTerm : uniProtTerms )
			{
				writer.write( SEPARATOR + uniProtTerm.getName() );
				writer.newLine();
				writer.write( uniProtTerm.getSequence() );
				writer.newLine();
			}
		}
		finally
		{
			if( writer != null )
			{
				writer.close();
			}
		}
	}
	
	/**
	 * 
	 * @param query
	 * @return Collection<OntologyTerm>
	 * @throws Exception
	 */
	private Collection<OntologyTerm> doSearch( final String query ) throws Exception
	{
		final Collection<OntologyTerm> ontologyTerms = new LinkedHashSet<>();
		final String modifiedQuery = ncbiTaxonomyTerm == null ? query : query + "+AND+taxonomy:" + ncbiTaxonomyTerm; //$NON-NLS-1$
		final Map<String,List<String>> idToName = searchRest( modifiedQuery );
		
		for( String id : idToName.keySet() )
		{
			ontologyTerms.add( new UniProtTerm( id ) );
		}
		
    	return ontologyTerms;
	}
	
	/**
	 * 
	 * @author Neil Swainston
	 */
	public enum UniProtUtilsSearchOption
	{
		/**
		 * 
		 */
		PROTEIN_NAME,
		
		/**
		 * 
		 */
		GENE_NAME,
		
		/**
		 * 
		 */
		FULL_TEXT
	}
}