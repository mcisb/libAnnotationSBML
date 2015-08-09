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
import java.net.*;
import java.util.*;
import org.mcisb.ontology.*;

/**
 * 
 * @author Neil Swainston
 */
public class UniProtTerm extends OntologyTerm
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private Collection<String> organisms = null;

	/**
	 * 
	 */
	private Collection<String> ncbiTaxonomyIds = null;

	/**
	 * 
	 */
	private Collection<OntologyTerm> goTerms = null;

	/**
	 * 
	 */
	private String sequence;

	/**
	 * 
	 */
	private String geneId;

	/**
	 * 
	 */
	private String uniProtId;

	/**
	 * 
	 * @param id
	 * @throws Exception
	 */
	public UniProtTerm( final String id ) throws Exception
	{
		super( OntologyFactory.getOntology( Ontology.UNIPROT ), id );
	}

	/**
	 * 
	 * @return String
	 * @throws Exception
	 */
	public synchronized String getUniProtId() throws Exception
	{
		if( uniProtId == null )
		{
			init();
		}

		return uniProtId;
	}

	/**
	 * 
	 * @return Collection
	 * @throws Exception
	 */
	public synchronized Collection<String> getOrganisms() throws Exception
	{
		if( organisms == null )
		{
			init();
		}

		return organisms;
	}

	/**
	 * 
	 * @return Collection
	 * @throws Exception
	 */
	public synchronized Collection<String> getNcbiTaxonomyIds() throws Exception
	{
		if( ncbiTaxonomyIds == null )
		{
			init();
		}

		return ncbiTaxonomyIds;
	}

	/**
	 * 
	 * @return Collection
	 * @throws Exception
	 */
	public synchronized Collection<OntologyTerm> getGoTerms() throws Exception
	{
		if( goTerms == null )
		{
			init();
		}

		return goTerms;
	}

	/**
	 * 
	 * @return String
	 * @throws Exception
	 */
	public synchronized String getSequence() throws Exception
	{
		if( sequence == null )
		{
			init();
		}

		return sequence;
	}

	/**
	 * 
	 * @return String
	 * @throws Exception
	 */
	public synchronized String getGeneId() throws Exception
	{
		if( geneId == null )
		{
			init();
		}

		return geneId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mcisb.ontology.OntologyTerm#doInitialise()
	 */
	@Override
	protected synchronized void doInitialise() throws Exception
	{
		final URL url = new URL( "http://www.uniprot.org/uniprot/?query=id:" + id + "&format=tab&columns=entry%20name,genes,organism-id,organism,go-id,protein%20names,sequence" ); //$NON-NLS-1$ //$NON-NLS-2$

		try ( BufferedReader reader = new BufferedReader( new InputStreamReader( url.openStream() ) ) )
		{
			reader.readLine();

			final String line = reader.readLine();

			if( line != null )
			{
				organisms = new LinkedHashSet<>();
				ncbiTaxonomyIds = new LinkedHashSet<>();
				goTerms = new LinkedHashSet<>();

				final String[] tokens = line.split( "\t" ); //$NON-NLS-1$

				uniProtId = tokens[ 0 ].trim();
				geneId = tokens[ 1 ].trim();
				ncbiTaxonomyIds.addAll( Arrays.asList( tokens[ 2 ].trim().split( ";\\s+" ) ) ); //$NON-NLS-1$

				final String[] allOrganisms = tokens[ 3 ].trim().split( "\\)?\\s\\(" ); //$NON-NLS-1$

				for( String organism : allOrganisms )
				{
					organisms.add( organism.replaceAll( "\\)$", "" ) ); //$NON-NLS-1$ //$NON-NLS-2$
				}

				for( String goId : tokens[ 4 ].trim().split( ";\\s+" ) ) //$NON-NLS-1$
				{
					goTerms.add( OntologyUtils.getInstance().getOntologyTerm( Ontology.GO, goId ) );
				}

				final String[] allSynonyms = tokens[ 5 ].trim().split( "\\)?\\s\\(" ); //$NON-NLS-1$
				name = allSynonyms[ 0 ];

				for( String synonym : allSynonyms )
				{
					synonyms.add( synonym.replaceAll( "\\)$", "" ) ); //$NON-NLS-1$ //$NON-NLS-2$
				}

				sequence = tokens[ 6 ].trim();

				addSynonym( id );
				addSynonym( geneId );
				addSynonym( uniProtId );
			}
		}
	}
}