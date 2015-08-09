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
import org.mcisb.ontology.miriam.*;

/**
 * 
 * @author Neil Swainston
 */
public class OntologyFactory
{
	/**
	 * 
	 */
	private static Map<String,Ontology> ontologies = null;

	/**
	 * 
	 * @return Collection
	 * @throws Exception
	 */
	public static synchronized Collection<Ontology> getOntologies() throws Exception
	{
		return OntologyFactory.getOntologiesMap().values();
	}

	/**
	 * 
	 * @param name
	 * @return Ontology
	 * @throws Exception
	 */
	public static synchronized Ontology getOntology( final String name ) throws Exception
	{
		Ontology ontology = OntologyFactory.getOntologiesMap().get( name );

		if( ontology == null && name != null )
		{
			// Consider synonyms:
			if( name.equalsIgnoreCase( "PubChem" ) ) //$NON-NLS-1$
			{
				return OntologyFactory.getOntologiesMap().get( Ontology.PUBCHEM_SUBSTANCE );
			}
			else if( name.equals( "GO" ) ) //$NON-NLS-1$
			{
				return OntologyFactory.getOntologiesMap().get( Ontology.GO );
			}
			else if( name.equals( "UniProt" ) ) //$NON-NLS-1$
			{
				return OntologyFactory.getOntologiesMap().get( Ontology.UNIPROT );
			}
		}

		return ontology;
	}

	/**
	 * 
	 * @param uri
	 * @return Ontology
	 * @throws Exception
	 */
	public static Ontology getOntologyFromUri( final String uri ) throws Exception
	{
		final String SEPARATOR = uri.contains( Ontology.URL_SEPARATOR ) ? Ontology.URL_SEPARATOR : Ontology.URI_SEPARATOR;
		int index = -1;

		while( ( index = uri.lastIndexOf( SEPARATOR ) ) != -1 )
		{
			final String uriIdentifier = uri.substring( 0, index );

			for( Iterator<Ontology> iterator = getOntologies().iterator(); iterator.hasNext(); )
			{
				final Ontology ontology = iterator.next();

				if( ontology.getUriIdentifiers().contains( uriIdentifier ) )
				{
					return ontology;
				}
			}
		}

		return null;
	}

	/**
	 * 
	 * 
	 * @return OntologyFactory
	 * @throws Exception
	 */
	private synchronized static Map<String,Ontology> getOntologiesMap() throws Exception
	{
		if( ontologies == null )
		{
			ontologies = new HashMap<>();
			ontologies.putAll( MiriamUtils.getOntologies() );
		}

		return ontologies;
	}
}