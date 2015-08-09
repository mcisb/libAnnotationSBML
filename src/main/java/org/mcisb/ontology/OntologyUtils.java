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
import org.mcisb.ontology.chebi.*;
import org.mcisb.ontology.ec.*;
import org.mcisb.ontology.gene.*;
import org.mcisb.ontology.go.*;
import org.mcisb.ontology.kegg.*;
import org.mcisb.ontology.pubchem.*;
import org.mcisb.ontology.sbo.*;
import org.mcisb.ontology.taxonomy.*;
import org.mcisb.ontology.uniprot.*;
import org.mcisb.util.*;
import org.mcisb.util.chem.*;

/**
 * 
 * @author Neil Swainston
 */
public class OntologyUtils extends OntologySource
{
	/**
	 * 
	 * @author Neil Swainston
	 *
	 */
	public enum MatchCriteria { IDENTICAL, COMMON_ANCESTOR, CONJUGATES, TAUTOMERS, ANY }
	
	/**
	 * 
	 */
	private final Map<String,OntologySource> identifierToOntologySource = new HashMap<>();

	/**
	 * 
	 */
	private final Map<String,Map<OntologyTerm,Object[]>> ontologyTermUriToXrefs = new TreeMap<>();
	
	/**
	 * 
	 */
	private final Map<MatchCriteria,Map<Set<String>,Boolean>> matchCriteriaToOntologyTermsToEquivalence = new HashMap<>();
	
	/**
	 * 
	 */
	private final static String IDENTIFIERS_ORG_PREFIX = "http://identifiers.org/"; //$NON-NLS-1$
	
	/**
	 * 
	 */
	private static OntologyUtils utils = null;
	
	/**
	 *
	 * @return OntologyUtils
	 * @throws Exception
	 */
	public static synchronized OntologyUtils getInstance() throws Exception
	{
		if( utils == null )
		{
			utils = new OntologyUtils();
		}
		
		return utils;
	}
	
	/**
	 *
	 * @throws Exception
	 */
	private OntologyUtils() throws Exception
	{
		super( null );
		
		for( Iterator<Ontology> iterator = OntologyFactory.getOntologies().iterator(); iterator.hasNext(); )
		{
			final Ontology currentOntology = iterator.next();
			
			if( currentOntology.getName().equals( Ontology.CHEBI ) )
			{
				identifierToOntologySource.put( currentOntology.getUrlIdentifier(), ChebiUtils.getInstance() );
			}
			else if( currentOntology.getName().equals( Ontology.EC ) )
			{
				identifierToOntologySource.put( currentOntology.getUrlIdentifier(), EcUtils.getInstance() );
			}
			else if( currentOntology.getName().equals( Ontology.GO ) )
			{
				identifierToOntologySource.put( currentOntology.getUrlIdentifier(), GoUtils.getInstance() );
			}
			else if( currentOntology.getName().equals( Ontology.KEGG_COMPOUND ) )
			{
				identifierToOntologySource.put( currentOntology.getUrlIdentifier(), KeggCompoundUtils.getInstance() );
			}
			else if( currentOntology.getName().equals( Ontology.KEGG_DRUG ) )
			{
				identifierToOntologySource.put( currentOntology.getUrlIdentifier(), KeggDrugUtils.getInstance() );
			}
			else if( currentOntology.getName().equals( Ontology.KEGG_GENES ) )
			{
				identifierToOntologySource.put( currentOntology.getUrlIdentifier(), KeggGeneUtils.getInstance() );
			}
			else if( currentOntology.getName().equals( Ontology.KEGG_GENOME ) )
			{
				identifierToOntologySource.put( currentOntology.getUrlIdentifier(), KeggGenomeUtils.getInstance() );
			}
			else if( currentOntology.getName().equals( Ontology.KEGG_PATHWAY ) )
			{
				identifierToOntologySource.put( currentOntology.getUrlIdentifier(), new KeggUtils( Ontology.KEGG_PATHWAY ) );
			}
			else if( currentOntology.getName().equals( Ontology.KEGG_REACTION ) )
			{
				identifierToOntologySource.put( currentOntology.getUrlIdentifier(), KeggReactionUtils.getInstance() );
			}
			else if( currentOntology.getName().equals( Ontology.KEGG_GLYCAN ) )
			{
				identifierToOntologySource.put( currentOntology.getUrlIdentifier(), KeggGlycanUtils.getInstance() );
			}
			else if( currentOntology.getName().equals( Ontology.NCBI_GENE ) )
			{
				identifierToOntologySource.put( currentOntology.getUrlIdentifier(), GeneUtils.getInstance() );
			}
			else if( currentOntology.getName().equals( Ontology.PUBCHEM_COMPOUND ) )
			{
				identifierToOntologySource.put( currentOntology.getUrlIdentifier(), new PubChemUtils( Ontology.PUBCHEM_COMPOUND ) );
			}
			else if( currentOntology.getName().equals( Ontology.PUBCHEM_SUBSTANCE ) )
			{
				identifierToOntologySource.put( currentOntology.getUrlIdentifier(), new PubChemUtils( Ontology.PUBCHEM_SUBSTANCE ) );
			}
			else if( currentOntology.getName().equals( Ontology.SBO ) )
			{
				identifierToOntologySource.put( currentOntology.getUrlIdentifier(), SboUtils.getInstance() );
			}
			else if( currentOntology.getName().equals( Ontology.TAXONOMY ) )
			{
				identifierToOntologySource.put( currentOntology.getUrlIdentifier(), TaxonomyUtils.getInstance() );
			}
			else if( currentOntology.getName().equals( Ontology.UNIPROT ) )
			{
				identifierToOntologySource.put( currentOntology.getUrlIdentifier(), new UniProtUtils() );
			}
			else
			{
				identifierToOntologySource.put( currentOntology.getUrlIdentifier(), new DefaultOntologySource( currentOntology.getName() ) );
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.mcisb.ontology.OntologySource#getOntologyTerm(java.lang.String)
	 */
	@Override
	public OntologyTerm getOntologyTerm( final String id ) throws Exception
	{
		if( id != null )
		{
			String reformattedId = id;
			
			if( reformattedId.contains( IDENTIFIERS_ORG_PREFIX ) )
			{
				final String EMPTY_STRING = ""; //$NON-NLS-1$
				final String uriIdentifier = CollectionUtils.getFirst( RegularExpressionUtils.getMatches( reformattedId, "http://identifiers.org/[^/]*/" ) ); //$NON-NLS-1$
				
				for( Iterator<Ontology> iterator = OntologyFactory.getOntologies().iterator(); iterator.hasNext(); )
	    		{
	    			final Ontology currentOntology = iterator.next();
	    			
	    			if( currentOntology.getUriIdentifiers().contains( uriIdentifier ) )
	    			{
	    				final OntologySource ontologySource = identifierToOntologySource.get( currentOntology.getUrlIdentifier() );
	        			
	        			if( ontologySource != null )
	        			{
	        				return ontologySource.getOntologyTerm( reformattedId.replace( uriIdentifier, EMPTY_STRING ) );
	        			}
	    			}
	    		}
			}
			else
			{
				final String SEPARATOR = reformattedId.contains( Ontology.URL_SEPARATOR ) ? Ontology.URL_SEPARATOR : Ontology.URI_SEPARATOR;
				int index = -1;
				String uriIdentifier = reformattedId;
				
				while( ( index = uriIdentifier.lastIndexOf( SEPARATOR ) ) != -1 )
				{
					uriIdentifier = uriIdentifier.substring( 0, index );
					
					for( Iterator<Ontology> iterator = OntologyFactory.getOntologies().iterator(); iterator.hasNext(); )
		    		{
		    			final Ontology currentOntology = iterator.next();
		    			
		    			if( currentOntology.getUriIdentifiers().contains( uriIdentifier ) )
		    			{
		    				final OntologySource ontologySource = identifierToOntologySource.get( currentOntology.getUrlIdentifier() );
		        			
		        			if( ontologySource != null )
		        			{
		        				return ontologySource.getOntologyTerm( reformattedId.substring( index + 1 ) );
		        			}
		    			}
		    		}
				}
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
		final Collection<OntologyTerm> ontologyTerms = new LinkedHashSet<>();
		
		for( Iterator<OntologySource> iterator = identifierToOntologySource.values().iterator(); iterator.hasNext(); )
		{
			final OntologySource ontologySource = iterator.next();
			ontologyTerms.addAll( ontologySource.search( identifier ) );
		}
		
		return ontologyTerms;
	}
	
	/**
	 *
	 * @param ontologyName
	 * @param id
	 * @return OntologyTerm
	 * @throws Exception
	 */
	public OntologyTerm getOntologyTerm( final String ontologyName, final String id ) throws Exception
	{
		final Ontology requiredOntology = OntologyFactory.getOntology( ontologyName );
		
		if( requiredOntology != null )
		{
    		final OntologySource ontologySource = identifierToOntologySource.get( requiredOntology.getUrlIdentifier() );
    		
    		if( ontologySource != null )
    		{
    			return ontologySource.getOntologyTerm( id );
    		}
		}
		
		return null;
	}
	
	/**
	 * 
	 * @param ontologyName
	 * @return OntologySource
	 * @throws Exception 
	 */
	public OntologySource getOntologySource( final String ontologyName ) throws Exception
	{
		final Ontology requiredOntology = OntologyFactory.getOntology( ontologyName );
		
		if( requiredOntology != null )
		{
    		return identifierToOntologySource.get( requiredOntology.getUrlIdentifier() );
		}
		
		return null;
	}
	
	/**
	 *
	 * @param ontologyTerms
	 * @throws Exception
	 */
	public void getXrefs( final Map<OntologyTerm,Object[]> ontologyTerms ) throws Exception
	{
		for( Iterator<Map.Entry<OntologyTerm,Object[]>> iterator = ontologyTerms.entrySet().iterator(); iterator.hasNext(); )
		{
			final Map.Entry<OntologyTerm,Object[]> entry = iterator.next();
			final OntologyTerm ontologyTerm = entry.getKey();
			final int ontologyTermsSize = ontologyTerms.size();
			ontologyTerms.putAll( getXrefs( ontologyTerm ) );

			if( ontologyTermsSize != ontologyTerms.size() )
			{
				getXrefs( ontologyTerms );
				break;
			}
		}
	}
	
	/**
	 *
	 * @param ontologyTerms
	 * @param xrefOntologyName
	 * @return Collection
	 * @throws Exception
	 */
	public Collection<OntologyTerm> getXrefs( final Collection<OntologyTerm> ontologyTerms, final String xrefOntologyName ) throws Exception
	{
		final Collection<OntologyTerm> xrefs = new LinkedHashSet<>();
		final Collection<OntologyTerm> allOntologyTerms = new HashSet<>( ontologyTerms );
		
		for( OntologyTerm ontologyTerm : ontologyTerms )
		{
			if( ontologyTerm.getOntologyName().equals( xrefOntologyName ) )
			{
				xrefs.add( ontologyTerm );
			}
		}
		
		if( xrefs.size() > 0 )
		{
			return xrefs;
		}
		
		for( OntologyTerm ontologyTerm : ontologyTerms )
		{
			allOntologyTerms.addAll( getXrefs( ontologyTerm ).keySet() );
			
			if( allOntologyTerms.size() != ontologyTerms.size() )
			{
				xrefs.addAll( getXrefs( allOntologyTerms, xrefOntologyName ) );
				
				if( xrefs.size() > 0 )
				{
					return xrefs;
				}
			}
		}
		
		return xrefs;
	}
	
	/**
	 * 
	 * @param ontologyTerm
	 * @param xRefOntologyTermName
	 * @return OntologyTerm
	 * @throws Exception
	 */
	public OntologyTerm getXref( final OntologyTerm ontologyTerm, final String xRefOntologyTermName ) throws Exception
	{
		if( ontologyTerm.getOntologyName().equals( xRefOntologyTermName ) )
		{
			return ontologyTerm;
		}
		
		for( OntologyTerm xrefTerm : getXrefs( ontologyTerm ).keySet() )
		{
			if( xrefTerm.getOntologyName().equals( xRefOntologyTermName ) )
			{
				return xrefTerm;
			}
		}
		
		return null;
	}
	
	/**
	 * 
	 * @param ontologyTerm1
	 * @param ontologyTerm2
	 * @return boolean
	 * @throws Exception 
	 */
	public OntologyTerm getCommonAncestor( final ChebiTerm ontologyTerm1, final ChebiTerm ontologyTerm2 ) throws Exception
	{
		return getCommonAncestor( ontologyTerm1, ontologyTerm2, true );
	}
	
	/**
	 * 
	 * @param ontologyTerm1
	 * @param ontologyTerm2
	 * @param considerFormula 
	 * @return boolean
	 * @throws Exception 
	 */
	public OntologyTerm getCommonAncestor( final ChebiTerm ontologyTerm1, final ChebiTerm ontologyTerm2, final boolean considerFormula ) throws Exception
	{
		if( considerFormula )
		{
			final String R = "R"; //$NON-NLS-1$
			final Formula formula1 = Formula.getFormula( ontologyTerm1.getFormula() );
			final Formula formula2 = Formula.getFormula( ontologyTerm2.getFormula() );
			
			if( ontologyTerm1.getFormula() == null || ontologyTerm2.getFormula() == null
				|| !formula1.equals( formula2 )
				|| formula1.get( R ) > 0
				|| formula2.get( R ) > 0 )
			{
				return null;
			}
			
			return getCommonAncestor( ontologyTerm1, ontologyTerm2, ontologyTerm1, formula1 );
		}
		
		return getCommonAncestor( ontologyTerm1, ontologyTerm2, ontologyTerm1, null );
	}
	
	/**
	 * 
	 * @param ontologyTerm1
	 * @param ontologyTerm2
	 * @return boolean
	 * @throws Exception 
	 */
	public boolean areConjugates( final ChebiTerm ontologyTerm1, final ChebiTerm ontologyTerm2 ) throws Exception
	{
		return areConjugates( ontologyTerm1, ontologyTerm2, true ) || areConjugates( ontologyTerm1, ontologyTerm2, false );
	}
	
	/**
	 * 
	 * @param ontologyTerm1
	 * @param ontologyTerm2
	 * @return boolean
	 * @throws Exception
	 */
	public boolean areEquivalent( final OntologyTerm ontologyTerm1, final OntologyTerm ontologyTerm2, final MatchCriteria matchCriteria ) throws Exception
	{
		final List<MatchCriteria> allMatchCriteria = new ArrayList<>();
		allMatchCriteria.add( matchCriteria );
		return areEquivalent( ontologyTerm1, ontologyTerm2, allMatchCriteria );
	}
	
	/**
	 * 
	 * @param ontologyTerm1
	 * @param ontologyTerm2
	 * @param allMatchCriteria
	 * @return boolean
	 * @throws Exception
	 */
	public boolean areEquivalent( final OntologyTerm ontologyTerm1, final OntologyTerm ontologyTerm2, final Collection<MatchCriteria> allMatchCriteria ) throws Exception
	{
		if( ontologyTerm1.equals( ontologyTerm2 ) )
		{
			return true;
		}
		
		if( ontologyTerm1 instanceof ChebiTerm && ontologyTerm2 instanceof ChebiTerm )
		{
			final Set<String> ontologyTermIds = new TreeSet<>();
			ontologyTermIds.add( ontologyTerm1.getId() );
			ontologyTermIds.add( ontologyTerm2.getId() );
			
			for( MatchCriteria matchCriteria : allMatchCriteria )
			{
				Map<Set<String>,Boolean> ontologyTermsToEquivalence = matchCriteriaToOntologyTermsToEquivalence.get( matchCriteria );
				
				if( ontologyTermsToEquivalence != null )
				{
					Boolean equivalence = ontologyTermsToEquivalence.get( ontologyTermIds );
					
					if( equivalence != null && ( equivalence.booleanValue() || allMatchCriteria.size() == 1 ) )
					{
						return equivalence.booleanValue();
					}
				}
			}
			
			// else
			final ChebiTerm chebiTerm1 = (ChebiTerm)ontologyTerm1;
			final ChebiTerm chebiTerm2 = (ChebiTerm)ontologyTerm2;
			boolean calculatedEquivalence = false;
			
			if( allMatchCriteria.contains( MatchCriteria.ANY ) )
			{
				calculatedEquivalence = areConjugates( chebiTerm1, chebiTerm2 ) || getCommonAncestor( chebiTerm1, chebiTerm2 ) != null || areTautomers( chebiTerm1, chebiTerm2 );
				cacheEquivalence( MatchCriteria.ANY, ontologyTermIds, calculatedEquivalence );
				
				if( calculatedEquivalence )
				{
					return true;
				}
			}
			
			if( allMatchCriteria.contains( MatchCriteria.COMMON_ANCESTOR ) )
			{
				calculatedEquivalence = getCommonAncestor( chebiTerm1, chebiTerm2 ) != null;
				cacheEquivalence( MatchCriteria.COMMON_ANCESTOR, ontologyTermIds, calculatedEquivalence );
				
				if( calculatedEquivalence )
				{
					return true;
				}
			}
			
			if( allMatchCriteria.contains( MatchCriteria.CONJUGATES ) )
			{
				calculatedEquivalence = areConjugates( chebiTerm1, chebiTerm2 );
				cacheEquivalence( MatchCriteria.CONJUGATES, ontologyTermIds, calculatedEquivalence );

				if( calculatedEquivalence )
				{
					return true;
				}
			}
			
			if( allMatchCriteria.contains( MatchCriteria.TAUTOMERS ) )
			{
				calculatedEquivalence = areTautomers( chebiTerm1, chebiTerm2 );
				cacheEquivalence( MatchCriteria.TAUTOMERS, ontologyTermIds, calculatedEquivalence );
				
				if( calculatedEquivalence )
				{
					return true;
				}
			}
		}
		
		return false;
	}

	/**
	 * 
	 * @param chebiTerm1
	 * @param chebiTerm2
	 * @return boolean
	 * @throws Exception
	 */
	public static boolean areTautomers( final ChebiTerm chebiTerm1, final ChebiTerm chebiTerm2 ) throws Exception
	{
		final Collection<ChebiTerm> parentTerms = chebiTerm1.getParents( ChebiTerm.IS_TAUTOMER_OF );
		
		for( ChebiTerm parentTerm : parentTerms )
		{
			if( parentTerm.equals( chebiTerm2 ) )
			{
				return true;
			}
		}
		
		return false;
	}

	/**
	 * 
	 * @param ontologyTerm1
	 * @param ontologyTerm2
	 * @param originalOntologyTerm1 
	 * @param formula 
	 * @return OntologyTerm
	 * @throws Exception
	 */
	private OntologyTerm getCommonAncestor( final ChebiTerm ontologyTerm1, final ChebiTerm ontologyTerm2, final ChebiTerm originalOntologyTerm1, final Formula formula ) throws Exception
	{
		final Collection<ChebiTerm> parentTerms1 = ontologyTerm1.getParents( Arrays.asList( ChebiTerm.IS_A ) );
		parentTerms1.add( ontologyTerm1 );
		final Collection<ChebiTerm> parentTerms2 = ontologyTerm2.getParents( Arrays.asList( ChebiTerm.IS_A ) );
		parentTerms2.add( ontologyTerm2 );
		
		if( formula != null )
		{
			for( Iterator<ChebiTerm> iterator = parentTerms1.iterator(); iterator.hasNext(); )
			{
				final String parentTermFormula = iterator.next().getFormula();
				
				if( parentTermFormula == null || !Formula.getFormula( parentTermFormula ).equals( formula ) )
				{
					iterator.remove();
				}
			}
			
			for( Iterator<ChebiTerm> iterator = parentTerms2.iterator(); iterator.hasNext(); )
			{
				final String parentTermFormula = iterator.next().getFormula();
				
				if( parentTermFormula == null || !Formula.getFormula( parentTermFormula ).equals( formula ) )
				{
					iterator.remove();
				}
			}
		}
		
		final Collection<ChebiTerm> intersection = (Collection<ChebiTerm>)CollectionUtils.getIntersection( Arrays.asList( parentTerms1, parentTerms2 ) );

		for( final ChebiTerm parentTerm : intersection )
		{
			return parentTerm;
		}
		
		parentTerms1.remove( ontologyTerm1 );
		
		for( final ChebiTerm parentTerm1 : parentTerms1 )
		{
			return getCommonAncestor( parentTerm1, ontologyTerm2, originalOntologyTerm1, formula );
		}
		
		parentTerms2.remove( ontologyTerm2 );
		
		for( final ChebiTerm parentTerm2 : parentTerms2 )
		{
			return getCommonAncestor( originalOntologyTerm1, parentTerm2, originalOntologyTerm1, formula );
		}
		
		return null;
	}
	
	/**
	 * 
	 * @param ontologyTerm1
	 * @param ontologyTerm2
	 * @param base 
	 * @return boolean
	 * @throws Exception 
	 */
	private boolean areConjugates( final ChebiTerm ontologyTerm1, final ChebiTerm ontologyTerm2, final boolean base ) throws Exception
	{
		final Collection<ChebiTerm> parentTerms = ontologyTerm1.getParents( Arrays.asList( base ? ChebiTerm.IS_CONJUGATE_BASE_OF : ChebiTerm.IS_CONJUGATE_ACID_OF ) );
		
		for( ChebiTerm parentTerm : parentTerms )
		{
			if( parentTerm.equals( ontologyTerm2 ) )
			{
				return true;
			}
		}
		
		for( ChebiTerm parentTerm : parentTerms )
		{
			return areConjugates( parentTerm, ontologyTerm2, base );
		}
		
		return false;
	}
	
	/**
	 * 
	 * @param matchCriteria
	 * @param ontologyTermIds
	 * @param calculatedEquivalence
	 */
	private void cacheEquivalence( final MatchCriteria matchCriteria, final Set<String> ontologyTermIds, final boolean calculatedEquivalence )
	{
		Map<Set<String>,Boolean> ontologyTermsToEquivalence = matchCriteriaToOntologyTermsToEquivalence.get( matchCriteria );
		
		if( ontologyTermsToEquivalence == null )
		{
			ontologyTermsToEquivalence = new HashMap<>();
			matchCriteriaToOntologyTermsToEquivalence.put( matchCriteria, ontologyTermsToEquivalence );
		}
		
		ontologyTermsToEquivalence.put( ontologyTermIds, Boolean.valueOf( calculatedEquivalence ) );
	}
	
	/**
	 * 
	 * @param ontologyTerm
	 * @return Collection<OntologyTerm>
	 * @throws Exception 
	 */
	private Map<OntologyTerm,Object[]> getXrefs( final OntologyTerm ontologyTerm ) throws Exception
	{
		final String uri = ontologyTerm.toUri();
		Map<OntologyTerm,Object[]> xrefs = ontologyTermUriToXrefs.get( uri );
		
		if( xrefs == null )
		{
			xrefs = ontologyTerm.getXrefs();
			ontologyTermUriToXrefs.put( uri, xrefs );
		}
		
		return xrefs;
	}
}