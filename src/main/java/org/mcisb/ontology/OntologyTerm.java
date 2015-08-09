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

import java.io.*;
import java.util.*;
import org.sbml.jsbml.CVTerm.Qualifier;
import org.sbml.jsbml.CVTerm.Type;

/**
 * 
 * @author Neil Swainston
 */
public class OntologyTerm implements Comparable<Object>, Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public static final String COLON = ":"; //$NON-NLS-1$

	/**
	 * 
	 */
	public static final String ENCODED_COLON = "%3A"; //$NON-NLS-1$

	/**
	 * 
	 */
	protected final String id;

	/**
	 * 
	 */
	protected final Ontology ontology;

	/**
	 * 
	 */
	protected final Collection<String> synonyms = new HashSet<>();

	/**
	 * 
	 */
	protected final Map<OntologyTerm,Object[]> xrefs = new HashMap<>();

	/**
	 * 
	 */
	protected String name;

	/**
	 * 
	 */
	protected boolean initialised = false;

	/**
	 * 
	 */
	protected boolean valid = true;

	/**
	 * 
	 * @param ontology
	 * @param id
	 */
	protected OntologyTerm( final Ontology ontology, final String id )
	{
		this.ontology = ontology;
		this.id = id.replace( ENCODED_COLON, COLON );
	}

	/**
	 * 
	 * @return boolean
	 */
	public boolean isValid()
	{
		return valid;
	}

	/**
	 * 
	 * @return String
	 */
	public String getId()
	{
		return id;
	}

	/**
	 * 
	 * @param name
	 */
	public void setName( final String name )
	{
		this.name = name;
	}

	/**
	 * 
	 * @return String
	 * @throws Exception
	 */
	public String getName() throws Exception
	{
		if( name == null )
		{
			init();
		}

		return name;
	}

	/**
	 * 
	 * @return Ontology
	 */
	public Ontology getOntology()
	{
		return ontology;
	}

	/**
	 * 
	 * @return String
	 */
	public String getOntologyName()
	{
		return ontology.getName();
	}

	/**
	 * 
	 * @param synonym
	 * @return boolean
	 */
	public boolean addSynonym( final String synonym )
	{
		if( synonym != null && synonym.length() > 0 )
		{
			return synonyms.add( synonym );
		}

		return false;
	}

	/**
	 * 
	 * @return Collection
	 * @throws Exception
	 */
	public Collection<String> getSynonyms() throws Exception
	{
		init();
		return synonyms;
	}

	/**
	 * 
	 * @return String
	 */
	public String toUri()
	{
		final String uriIdentifier = getOntology().getUrlIdentifier();
		return uriIdentifier + id;
	}

	/**
	 * 
	 * @return String
	 */
	public String getLink()
	{
		final String linkTemplate = getOntology().getLinkTemplate();
		return ( linkTemplate == null ) ? null : linkTemplate.replace( Ontology.WILDCARD, id );
	}

	/**
	 * 
	 * @return Collection
	 * @throws Exception
	 */
	public Map<OntologyTerm,Object[]> getXrefs() throws Exception
	{
		init();
		return xrefs;
	}

	/**
	 * 
	 * @param xref
	 * @param type
	 * @param qualifier
	 */
	public void addXref( final OntologyTerm xref, final Type type, final Qualifier qualifier )
	{
		if( xref == null )
		{
			return;
		}

		xrefs.put( xref, new Object[] { type, qualifier } );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals( final Object obj )
	{
		if( obj instanceof OntologyTerm )
		{
			return ontology.getName().equals( ( (OntologyTerm)obj ).ontology.getName() ) && id.equals( ( (OntologyTerm)obj ).id );
		}
		// else
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return ontology.getName().hashCode() + id.hashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(T)
	 */
	@Override
	public int compareTo( final Object object )
	{
		if( object instanceof OntologyTerm )
		{
			final Ontology objectOntology = ( (OntologyTerm)object ).ontology;
			final int ontologyCompare = ontology.compareTo( objectOntology );

			if( ontologyCompare == 0 )
			{
				return id.compareTo( ( (OntologyTerm)object ).id );
			}

			return ontologyCompare;
		}

		return -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		try
		{
			return toUri();
		}
		catch( Exception e )
		{
			e.printStackTrace();
			return id;
		}
	}

	/**
	 * 
	 * @throws Exception
	 */
	protected synchronized void init() throws Exception
	{
		if( !initialised )
		{
			doInitialise();
			initialised = true;
		}
	}

	/**
	 * 
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	protected void doInitialise() throws Exception
	{
		// Override if necessary.
	}
}