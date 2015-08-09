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
package org.mcisb.ontology.go;

import java.util.*;
import org.mcisb.ontology.*;

/**
 * 
 * @author Neil Swainston
 */
public class GoUtils extends OntologySource
{
	/**
	 * 
	 */
	public static final String CHLOROPLAST_GO_TERM_ID = "GO:0009507"; //$NON-NLS-1$
	
	/**
	 * 
	 */
	public static final String CYTOPLASM_GO_TERM_ID = "GO:0005737"; //$NON-NLS-1$
	
	/**
	 * 
	 */
	public static final String CYTOSOL_GO_TERM_ID = "GO:0005829"; //$NON-NLS-1$
	
	/**
	 * 
	 */
	public static final String CYTOSKELETON_GO_TERM_ID = "GO:0005856"; //$NON-NLS-1$
	
	/**
	 * 
	 */
	public static final String INTRACELLULAR_GO_TERM_ID = "GO:0005622"; //$NON-NLS-1$
	
	/**
	 * 
	 */
	public static final String EXTRACELLULAR_GO_TERM_ID = "GO:0005576"; //$NON-NLS-1$
	
	/**
	 * 
	 */
	public static final String ENDOPLASMIC_RETICULUM_GO_TERM_ID = "GO:0005783"; //$NON-NLS-1$
	
	/**
	 * 
	 */
	public static final String GOLGI_APPARATUS_GO_TERM_ID = "GO:0005794"; //$NON-NLS-1$
	
	/**
	 * 
	 */
	public static final String LYSOSOME_GO_TERM_ID = "GO:0005764"; //$NON-NLS-1$
	
	/**
	 * 
	 */
	public static final String MITOCHONDRIA_GO_TERM_ID = "GO:0005739"; //$NON-NLS-1$
	
	/**
	 * 
	 */
	public static final String NUCLEUS_GO_TERM_ID = "GO:0005634"; //$NON-NLS-1$
	
	/**
	 * 
	 */
	public static final String PEROXISOME_GO_TERM_ID = "GO:0005777"; //$NON-NLS-1$
	
	/**
	 * 
	 */
	public static final String PLASMA_MEMBRANE_GO_TERM_ID = "GO:0005886"; //$NON-NLS-1$

	/**
	 * 
	 */
	public static final String VASCUOLAR_MEMBRANE_GO_TERM_ID = "GO:0005774"; //$NON-NLS-1$
	
	/**
	 * 
	 */
	private final static String GO = "GO"; //$NON-NLS-1$
	
	/**
	 * 
	 */
	private static GoUtils utils = null;
	
	/**
	 * 
	 * @return ChebiUtils
	 * @throws Exception
	 */
	public synchronized static GoUtils getInstance() throws Exception
	{
		if( utils == null )
		{
			utils = new GoUtils();
		}
		
		return utils;
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	private GoUtils() throws Exception
	{
		super( Ontology.GO );
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.mcisb.ontology.OntologySource#getOntologyTermFromId(java.lang.String)
	 */
	@Override
	protected OntologyTerm getOntologyTermFromId( final String id ) throws Exception
	{
		final String termId = OntologyLookupServiceClient.getTermById( id.replaceAll( OntologyTerm.ENCODED_COLON, OntologyTerm.COLON ), GO );
		
		for( final OntologyTerm ontologyTerm : search( termId ) )
		{
			if( ontologyTerm.getName().equals( termId ) )
			{
    			return ontologyTerm;
			}
		}
		
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.mcisb.ontology.OntologySource#search(java.lang.String)
	 */
	@Override
	public Collection<OntologyTerm> search( final String identifier ) throws Exception
	{
		final Collection<OntologyTerm> ontologyTerms = new HashSet<>();
		final Map<String,String> terms = OntologyLookupServiceClient.getTermsByName( identifier, GO, false );
		
		for( Iterator<Map.Entry<String,String>> iterator = terms.entrySet().iterator(); iterator.hasNext(); )
		{
			final Map.Entry<String,String> entry = iterator.next();
			final OntologyTerm ontologyTerm = new GoTerm( entry.getKey() );
    		ontologyTerm.setName( entry.getValue() );
    		ontologyTerm.addSynonym( ontologyTerm.getName() );
    		ontologyTerms.add( ontologyTerm );
		}
		
		return ontologyTerms;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.mcisb.ontology.OntologySource#getOntologyTerm(java.lang.String)
	 */
	@Override
	public OntologyTerm getOntologyTerm( final String identifier ) throws Exception
	{
		final String REGEX = "\\d{7}"; //$NON-NLS-1$
		final String EMPTY_STRING = ""; //$NON-NLS-1$
		String formattedIdentifier = identifier.replaceAll( OntologyTerm.ENCODED_COLON, OntologyTerm.COLON );
		formattedIdentifier = ( ( formattedIdentifier.matches( REGEX ) ) ? GO + OntologyTerm.COLON : EMPTY_STRING ) + formattedIdentifier;
		
		// UniProt term translations:
		formattedIdentifier = formattedIdentifier.replaceAll( "^Secreted$", "GO:0005576" ); //$NON-NLS-1$ //$NON-NLS-2$
		formattedIdentifier = formattedIdentifier.replaceAll( "^Mitochondrion(?!$)", "mitochondrial" ); //$NON-NLS-1$ //$NON-NLS-2$
		formattedIdentifier = formattedIdentifier.replaceAll( "^M-band$", "GO:0031430" ); //$NON-NLS-1$ //$NON-NLS-2$
		formattedIdentifier = formattedIdentifier.replaceAll( "^perinuclear region$", "GO:0048471" ); //$NON-NLS-1$ //$NON-NLS-2$
		
		return super.getOntologyTerm( formattedIdentifier );
	}
	
	/**
	 * 
	 * @param ontologyTerm
	 * @return Collection<OntologyTerm>
	 * @throws Exception
	 */
	public Collection<OntologyTerm> getParents( final OntologyTerm ontologyTerm ) throws Exception
	{
		final Collection<OntologyTerm> parents = new LinkedHashSet<>();
		
		final Map<String,String> parentTerms = OntologyLookupServiceClient.getParentTerms( ontologyTerm.getId().replaceAll( OntologyTerm.ENCODED_COLON, OntologyTerm.COLON ), GO );
		
		for( String parentTerm : parentTerms.keySet() )
		{
			parents.add( getOntologyTerm( parentTerm ) );
		}
		
		return parents;
	}
}