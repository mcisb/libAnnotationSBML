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
import org.sbml.jsbml.*;

/**
 * 
 * @author Neil Swainston
 */
public class KeggTerm extends OntologyTerm
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	private final String idPrefix;
	
	/**
	 *
	 * @param ontologyName
	 * @param id
	 * @param idPrefix
	 * @throws Exception
	 */
	public KeggTerm( final String ontologyName, final String id, final String idPrefix ) throws Exception
	{
		super( OntologyFactory.getOntology( ontologyName ), id );
		this.idPrefix = idPrefix;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.mcisb.ontology.OntologyTerm#doInitialise()
	 */
	@Override
	protected void doInitialise() throws Exception
	{
		final String EMPTY_STRING = ""; //$NON-NLS-1$
		final String PATTERN = "(///)|((\\S+)(\\s+)(.+))"; //$NON-NLS-1$
		final String SEPARATOR = " "; //$NON-NLS-1$
		
		final StringBuffer value = new StringBuffer();
		BufferedReader reader = null;
		
		try
		{
			reader = new BufferedReader( new InputStreamReader( new URL( "http://rest.kegg.jp/get/" + ( ( ( idPrefix == null || id.startsWith( idPrefix.replaceAll( ENCODED_COLON, COLON ) ) ) ? EMPTY_STRING : idPrefix ) + id ).replaceAll( OntologyTerm.ENCODED_COLON, OntologyTerm.COLON ) ).openStream(), Charset.defaultCharset() ) ); //$NON-NLS-1$
			String line = null;
			String propertyName = null;
			List<String> values = new ArrayList<>();
			
			while( ( line = reader.readLine() ) != null )
			{
				final StringTokenizer tokenizer = new StringTokenizer( line );
				
				if( value.length() > 0 )
				{
					value.setLength( value.length() - 1 );
					values.add( value.toString() );
					value.setLength( 0 );
				}
	
				if( line.matches( PATTERN ) )
				{
					if( propertyName != null )
					{
						parseProperty( propertyName, values );
					}
					
					propertyName = tokenizer.nextToken();
					values = new ArrayList<>();
				}
				
				while( tokenizer.hasMoreTokens() )
				{
					value.append( tokenizer.nextToken() );
					value.append( SEPARATOR );
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
	
	/**
	 *
	 * @param propertyName
	 * @param values
	 * @throws Exception
	 */
	protected void parseProperty( final String propertyName, final List<String> values ) throws Exception
	{
		final String DBLINKS = "DBLINKS"; //$NON-NLS-1$
		final String NAME = "NAME"; //$NON-NLS-1$
		final String SEMI_COLON = ";"; //$NON-NLS-1$
		final String EMPTY_STRING = ""; //$NON-NLS-1$
		final String SEPARATOR = ", "; //$NON-NLS-1$
		final String PUBCHEM = "PubChem"; //$NON-NLS-1$
		
		if( propertyName.equals( DBLINKS ) )
		{
			String xrefOntologyName = null;
			
			for( int i = 0; i < values.size(); i++ )
			{
				final StringTokenizer tokenizer2 = new StringTokenizer( values.get( i ), Ontology.ID_SEPARATOR );
				
				if( tokenizer2.countTokens() == 2 )
				{
					xrefOntologyName = tokenizer2.nextToken().trim();
					xrefOntologyName = ( xrefOntologyName.equals( PUBCHEM ) ? Ontology.PUBCHEM_SUBSTANCE : xrefOntologyName );
				}
				
				final StringTokenizer tokenizer3 = new StringTokenizer( tokenizer2.nextToken().trim() );
				
				while( tokenizer3.hasMoreTokens() )
				{
    				final OntologyTerm xref = OntologyUtils.getInstance().getOntologyTerm( xrefOntologyName, tokenizer3.nextToken() );
    				
    				if( xref != null )
    				{
    					addXref( xref );
    				}
				}
			}
		}
		else if( propertyName.equals( NAME ) )
		{
			final List<String> allNames = new ArrayList<>();
			
			for( Iterator<String> iterator = values.iterator(); iterator.hasNext(); )
			{
				final String names = iterator.next();
				allNames.addAll( Arrays.asList( names.split( SEPARATOR ) ) );
			}
			
			name = CollectionUtils.getFirst( allNames ).replace( SEMI_COLON, EMPTY_STRING ).trim();
		
			for( int i = 1; i < allNames.size(); i++ )
    		{
				addSynonym( allNames.get( i ).replace( SEMI_COLON, EMPTY_STRING ).trim() );
    		}
		}
	}
	
	/**
	 *
	 * @param definition
	 * @return String
	 */
	protected static String getPropertyString( final Object definition )
	{
		if( definition instanceof Object[] )
		{
			final Object[] array = (Object[])definition;
			final StringBuffer buffer = new StringBuffer();
			
			for( int i = 0; i < array.length; i++ )
			{
				buffer.append( array[ i ] );
			}
			
			return buffer.toString();
		}
		
		if( definition != null )
		{
			return definition.toString();
		}
		
		return null;
	}
	
	/**
	 * 
	 * @param xref
	 */
	protected void addXref( final OntologyTerm xref )
	{
		addXref( xref, CVTerm.Type.BIOLOGICAL_QUALIFIER, CVTerm.Qualifier.BQB_IS );
	}
}