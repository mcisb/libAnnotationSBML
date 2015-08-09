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
package org.mcisb.ontology.sbo;

import java.io.*;
import java.util.*;
import org.mcisb.ontology.*;
import org.mcisb.util.*;
import org.mcisb.util.xml.*;
import uk.ac.ebi.sbo.common.*;
import uk.ac.ebi.sbo.ws.client.*;

/**
 * 
 * @author Neil Swainston
 */
public class SboUtils extends OntologySource
{
	/**
	 * 
	 */
	public final static int ENZYME = 14;

	/**
	 * 
	 */
	public final static int SUBSTRATE = 15;

	/**
	 * 
	 */
	public final static int CATALYTIC_RATE_CONSTANT = 25;

	/**
	 * 
	 */
	public final static int MICHAELIS_CONSTANT = 27;

	/**
	 * 
	 */
	public final static int HILL_COEFFICIENT = 190;

	/**
	 * 
	 */
	public final static int ENZYMATIC_RATE_LAW_FOR_IRREVERSIBLE_NON_MODULATED_NON_INTERACTING_UNIREACTANT_ENZYMES = 28;

	/**
	 * 
	 */
	public final static int BIOCHEMICAL_REACTION = 176;

	/**
	 * 
	 */
	public final static int TRANSPORT_REACTION = 185;

	/**
	 * 
	 */
	public final static int OMITTED_PROCESS = 397;

	/**
	 * 
	 */
	public final static int COMPARTMENT = 290;

	/**
	 * 
	 */
	public final static int PROTEIN_COMPLEX = 297;

	/**
	 * 
	 */
	public final static int SIMPLE_CHEMICAL = 247;

	/**
	 * 
	 */
	public final static int RIBONUCLEIC_ACID = 250;

	/**
	 * 
	 */
	public final static int POLYPEPTIDE_CHAIN = 252;

	/**
	 * 
	 */
	public final static int ENZYME_CONCENTRATION = 505;

	/**
	 * 
	 */
	public final static int SUBSTRATE_CONCENTRATION = 515;

	/**
	 * 
	 */
	public final static int PROTEIN_COMPLEX_FORMATION_REACTION = 526;

	/**
	 * 
	 */
	private final SBOLink link = new SBOLink();

	/**
	 * 
	 */
	private static SboUtils utils = null;

	/**
	 * 
	 * @return OntologyUtils
	 * @throws Exception
	 */
	public static synchronized SboUtils getInstance() throws Exception
	{
		if( utils == null )
		{
			utils = new SboUtils();
		}

		return utils;
	}

	/**
	 * 
	 * @throws Exception
	 */
	private SboUtils() throws Exception
	{
		super( Ontology.SBO );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mcisb.ontology.OntologySource#getOntologyTermFromId(java.lang.String)
	 */
	@Override
	protected OntologyTerm getOntologyTermFromId( final String id ) throws Exception
	{
		String normalisedId = id;

		if( id.contains( Ontology.ID_SEPARATOR ) )
		{
			normalisedId = id.substring( id.indexOf( Ontology.ID_SEPARATOR ) + Ontology.ID_SEPARATOR.length() );
		}

		return getOntologyTerm( Integer.parseInt( normalisedId ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mcisb.ontology.OntologySource#search(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public Collection<OntologyTerm> search( final String identifier ) throws Exception
	{
		final List<Term> terms = link.search( identifier );
		final Collection<OntologyTerm> ontologyTerms = new ArrayList<>();

		if( terms != null )
		{
			for( Term term : terms )
			{
				ontologyTerms.add( getOntologyTerm( term ) );
			}
		}

		return ontologyTerms;
	}

	/**
	 * 
	 * @param sboTerm
	 * @return OntologyTerm
	 * @throws Exception
	 */
	public OntologyTerm getOntologyTerm( final int sboTerm ) throws Exception
	{
		return getOntologyTerm( link.convertId( Integer.valueOf( sboTerm ) ) );
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public Term[] getTree( final int id )
	{
		final List<Term> terms = link.getTree( link.convertId( Integer.valueOf( id ) ) );
		return terms.toArray( new Term[ terms.size() ] );
	}

	/**
	 * 
	 * @param termId
	 * @param parentId
	 * @return
	 */
	public boolean isChildOf( final int termId, final int parentId )
	{
		return link.isChildOf( link.convertId( Integer.valueOf( termId ) ), link.convertId( Integer.valueOf( parentId ) ) );
	}

	/**
	 * 
	 * @param term
	 * @return OntologyTerm
	 * @throws Exception
	 */
	public static OntologyTerm getOntologyTerm( final Term term ) throws Exception
	{
		final SboTerm ontologyTerm = new SboTerm( term.getIdentifier() );
		ontologyTerm.setName( term.getName() );
		ontologyTerm.setMath( term.getMathml() );
		return ontologyTerm;
	}

	/**
	 * 
	 * @param math
	 * @param sboTerm
	 * @return String
	 * @throws Exception
	 */
	public String getShortName( final String math, final int sboTerm ) throws Exception
	{
		final String CI = "ci"; //$NON-NLS-1$
		final String PREFIX = "http://biomodels.net/SBO/#"; //$NON-NLS-1$
		final String attributeValue = PREFIX + getOntologyTerm( sboTerm ).getId().replaceAll( OntologyTerm.ENCODED_COLON, OntologyTerm.COLON ); // SBO
																																				// is
																																				// using
																																				// old-style
																																				// URLs
		return CollectionUtils.getFirst( XmlUtils.getElementValues( CI, attributeValue, new ByteArrayInputStream( math.getBytes() ) ) );
	}
}