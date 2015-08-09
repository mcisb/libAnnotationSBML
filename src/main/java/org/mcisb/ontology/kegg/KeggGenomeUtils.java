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
public class KeggGenomeUtils extends KeggUtils
{
	/**
	 * 
	 */
	private static KeggGenomeUtils utils = null;

	/**
	 * 
	 * @return ChebiUtils
	 * @throws Exception
	 */
	public synchronized static KeggGenomeUtils getInstance() throws Exception
	{
		if( utils == null )
		{
			utils = new KeggGenomeUtils();
		}

		return utils;
	}

	/**
	 * 
	 * @throws Exception
	 */
	private KeggGenomeUtils() throws Exception
	{
		super( Ontology.KEGG_GENOME );
	}

	/**
	 * 
	 * @param taxonomyId
	 * @return OntologyTerm
	 * @throws Exception
	 */
	public OntologyTerm getOrganism( final int taxonomyId ) throws Exception
	{
		BufferedReader reader = null;

		try
		{
			reader = new BufferedReader( new InputStreamReader( new URL( "http://rest.kegg.jp/find/genome/" + taxonomyId ).openStream(), Charset.defaultCharset() ) ); //$NON-NLS-1$
			String line = null;

			while( ( line = reader.readLine() ) != null )
			{
				if( line.contains( " " + taxonomyId + ";" ) ) //$NON-NLS-1$ //$NON-NLS-2$
				{
					final String organismId = CollectionUtils.getFirst( RegularExpressionUtils.getMatches( line, "(?<=^genome:\\S{6}\\s)[^,]*" ) ); //$NON-NLS-1$
					return getOntologyTerm( organismId );
				}
			}

			return null;
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
	 * @return Collection
	 * @throws Exception
	 */
	public Collection<OntologyTerm> getOrganisms() throws Exception
	{
		final Collection<OntologyTerm> organisms = new TreeSet<>();

		for( String id : getOrganismIds() )
		{
			final OntologyTerm ontologyTerm = getOntologyTerm( id );

			if( ontologyTerm != null )
			{
				organisms.add( ontologyTerm );
			}
		}

		return organisms;
	}

	/**
	 * 
	 * @return String[]
	 * @throws Exception
	 */
	public static String[] getOrganismIds() throws Exception
	{
		final int ID_INDEX = 1;
		final Collection<String> organismIds = new TreeSet<>();
		BufferedReader reader = null;

		try
		{
			reader = new BufferedReader( new InputStreamReader( new URL( "http://rest.kegg.jp/list/organism" ).openStream(), Charset.defaultCharset() ) ); //$NON-NLS-1$
			String line = null;

			while( ( line = reader.readLine() ) != null )
			{
				final String[] tokens = line.split( "\t" ); //$NON-NLS-1$
				organismIds.add( tokens[ ID_INDEX ] );
			}

			return organismIds.toArray( new String[ organismIds.size() ] );
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