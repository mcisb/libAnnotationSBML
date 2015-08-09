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
import java.util.*;
import javax.xml.stream.*;
import org.mcisb.ontology.*;
import org.mcisb.ontology.ec.*;
import org.mcisb.ontology.sbo.*;
import org.mcisb.sbml.*;
import org.mcisb.util.*;
import org.sbml.jsbml.*;

/**
 * 
 * @author Neil Swainston
 */
public class KeggReactionUtils extends KeggUtils
{
	/**
	 * 
	 */
	private static KeggReactionUtils utils = null;
	
	/**
	 * 
	 * @return ChebiUtils
	 * @throws Exception
	 */
	public synchronized static KeggReactionUtils getInstance() throws Exception
	{
		if( utils == null )
		{
			utils = new KeggReactionUtils();
		}
		
		return utils;
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	private KeggReactionUtils() throws Exception
	{
		super( Ontology.KEGG_REACTION );
	}
	
	/**
	 * 
	 * @param sbmlIn
	 * @param sbmlOut
	 * @throws FactoryConfigurationError 
	 * @throws Exception 
	 */
	public static void annotate( final File sbmlIn, final File sbmlOut ) throws FactoryConfigurationError, Exception
	{
		final SBMLDocument document = SBMLReader.read( sbmlIn );
		final Model model = document.getModel();
		
		for( int l = 0; l < model.getNumReactions(); l++ )
		{
			final Reaction reaction = model.getReaction( l );
			
			if( !reaction.isSetSBOTerm() || reaction.getSBOTerm() == SboUtils.BIOCHEMICAL_REACTION )
			{
				final OntologyTerm keggReactionTerm = SbmlUtils.getOntologyTerm( reaction, Ontology.KEGG_REACTION );
			
				if( keggReactionTerm == null )
				{
					final Collection<OntologyTerm> keggReactionTerms = getKeggReactionTerms( model, reaction );
					
					for( OntologyTerm suggestedKeggReactionTerm : keggReactionTerms )
					{
						if( suggestedKeggReactionTerm != null )
						{
							if( keggReactionTerms.size() == 1 )
							{
								SbmlUtils.addOntologyTerm( reaction, suggestedKeggReactionTerm, CVTerm.Type.BIOLOGICAL_QUALIFIER, CVTerm.Qualifier.BQB_IS );
							}
						}
					}
				}
			}
		}

		new SBMLWriter().write( document, sbmlOut );
	}
	
	/**
	 *
	 * @param compoundId
	 * @return String[]
	 * @throws Exception 
	 */
	public static String[] getReactionIdsByCompoundId( final String compoundId ) throws Exception
	{
		final String PREFIX = "cpd:"; //$NON-NLS-1$
		return getReactionsByCompound( PREFIX + compoundId );
	}
	
	/**
	 * 
	 * @param geneId
	 * @return Collection
	 * @throws Exception
	 */
	public Collection<OntologyTerm> getReactionsFromGeneId( final String geneId ) throws Exception
	{
		final String PREFIX = "ec:"; //$NON-NLS-1$
		final String[] enzymeIds = getEnzymesByGene( geneId );
		final List<OntologyTerm> reactions = new ArrayList<>();
		
		for( int i = 0; i < enzymeIds.length; i++ )
		{
			reactions.addAll( getReactionsFromEcTerm( PREFIX + enzymeIds[ i ] ) );
		}

		return reactions;
	}
	
	/**
	 * 
	 * @param substrateId
	 * @param productId
	 * @return Collection<OntologyTerm>
	 * @throws Exception
	 */
	public static Collection<OntologyTerm> getReactionsFromSubstrateAndProduct( final String substrateId, final String productId ) throws Exception
	{
		return getReactionsFromSubstratesAndProducts( Arrays.asList( substrateId ), Arrays.asList( productId ) );
	}
	
	/**
	 * 
	 * @param substrateIds
	 * @param productIds
	 * @return Collection<OntologyTerm>
	 * @throws Exception
	 */
	public static Collection<OntologyTerm> getReactionsFromSubstratesAndProducts( final Collection<String> substrateIds, final Collection<String> productIds ) throws Exception
	{
		final Collection<OntologyTerm> reactions = new HashSet<>();
		
		if( hasSufficientSpecificity( substrateIds, productIds ) )
		{
			final List<List<String>> substrateAndProductReactions = new ArrayList<>();
			
			for( String substrateId : substrateIds )
			{
				substrateAndProductReactions.add( Arrays.asList( getReactionIdsByCompoundId( substrateId ) ) );
			}
			
			for( String productId : productIds )
			{
				substrateAndProductReactions.add( Arrays.asList( getReactionIdsByCompoundId( productId ) ) );
			}
			
			final Collection<?> candidateReactions = CollectionUtils.getIntersection( substrateAndProductReactions );
			
			final List<OntologyTerm> substrates = new ArrayList<>();
			final List<OntologyTerm> products = new ArrayList<>();
			
			if( candidateReactions.size() > 0 )
			{
				for( String substrateId : substrateIds )
				{
					substrates.add( OntologyUtils.getInstance().getOntologyTerm( Ontology.KEGG_COMPOUND, substrateId ) );
				}
				
				for( String productId : productIds )
				{
					products.add( OntologyUtils.getInstance().getOntologyTerm( Ontology.KEGG_COMPOUND, productId ) );
				}
			}
			
			for( Object reactionId : candidateReactions )
			{
				final KeggReactionTerm candidateReaction = (KeggReactionTerm)OntologyUtils.getInstance().getOntologyTerm( Ontology.KEGG_REACTION, (String)reactionId );
				final Collection<OntologyTerm> reactionSubstrates = candidateReaction.getSubstrates().keySet();
				final Collection<OntologyTerm> reactionProducts = candidateReaction.getProducts().keySet();
				
				if( reactionSubstrates.containsAll( substrates ) && reactionProducts.containsAll( products ) || reactionSubstrates.containsAll( products ) && reactionProducts.containsAll( substrates ) )
				{
					reactions.add( candidateReaction );
				}
			}
		}
		
		return reactions;
	}

	/**
	 * 
	 * @param substrate
	 * @param product
	 * @return Collection<OntologyTerm>
	 * @throws Exception
	 */
	public static Collection<OntologyTerm> getReactionsFromSubstrateAndProduct( final OntologyTerm substrate, final OntologyTerm product ) throws Exception
	{
		final OntologyTerm keggSubstrateTerm = OntologyUtils.getInstance().getXref( substrate, Ontology.KEGG_COMPOUND );
		final OntologyTerm keggProductTerm = OntologyUtils.getInstance().getXref( product, Ontology.KEGG_COMPOUND );
		
		if( keggSubstrateTerm != null && keggProductTerm != null )
		{
			return getReactionsFromSubstrateAndProduct( keggSubstrateTerm.getId(), keggProductTerm.getId() );
		}
		
		return new ArrayList<>();
	}
	
	/**
	 *
	 * @param geneId
	 * @return String[]
	 * @throws IOException
	 */
	private static String[] getEnzymesByGene( final String geneId ) throws IOException
	{
		final URL url = new URL( "http://www.genome.jp/dbget-bin/www_bget?" + geneId ); //$NON-NLS-1$
		final Collection<String> enzymes = RegularExpressionUtils.getMatches( url, RegularExpressionUtils.EC_REGEX );
		return enzymes.toArray( new String[ enzymes.size() ] );
	}
	
	/**
	 *
	 * @param ecTerm
	 * @return Collection
	 * @throws Exception
	 */
	private Collection<OntologyTerm> getReactionsFromEcTerm( final String ecTerm ) throws Exception
	{
		final List<OntologyTerm> reactions = new ArrayList<>();
		final OntologySource ecTermUtils = EcUtils.getInstance();
		
		final Object ontologyTerm = ecTermUtils.getOntologyTerm( ecTerm );
		
		if( ontologyTerm instanceof KeggReactionParticipantTerm )
		{
			final KeggReactionParticipantTerm enzyme = (KeggReactionParticipantTerm)ontologyTerm;
			
			for( Iterator<String> iterator = enzyme.getReactions().iterator(); iterator.hasNext(); )
			{
				reactions.add( getOntologyTerm( iterator.next() ) );
			}
		}
		
		return reactions;
	}
	
	/**
	 * @param substrateIds
	 * @param productIds
	 * @return boolean
	 */
	private static boolean hasSufficientSpecificity( Collection<String> substrateIds, Collection<String> productIds )
	{
		// Filter out special cases:
		if( substrateIds.size() == 1 && productIds.size() == 1 )
		{
			final String substrateId = CollectionUtils.getFirst( substrateIds );
			final String productId = CollectionUtils.getFirst( productIds );
			
			// NAD+ / NADH
			if( substrateId.equals( "C00003" ) && productId.equals( "C00004" ) //$NON-NLS-1$ //$NON-NLS-2$
				|| substrateId.equals( "C00004" ) && productId.equals( "C00003" ) ) //$NON-NLS-1$ //$NON-NLS-2$
			{
				return false;
			}
			// NADP+ / NADPH
			else if( substrateId.equals( "C00005" ) && productId.equals( "C00006" ) //$NON-NLS-1$ //$NON-NLS-2$
				|| substrateId.equals( "C00006" ) && productId.equals( "C00005" ) ) //$NON-NLS-1$ //$NON-NLS-2$
			{
				return false;
			}
			// ADP / ATP
			else if( substrateId.equals( "C00002" ) && productId.equals( "C00008" ) //$NON-NLS-1$ //$NON-NLS-2$
				|| substrateId.equals( "C00008" ) && productId.equals( "C00002" ) ) //$NON-NLS-1$ //$NON-NLS-2$
			{
				return false;
			}
		}
		else if( substrateIds.size() == 1 && productIds.size() == 2 )
		{
			final String substrateId = CollectionUtils.getFirst( substrateIds );
			final List<String> productIdList = new ArrayList<>( productIds );
			final String productId1 = productIdList.get( 0 );
			final String productId2 = productIdList.get( 1 );
			
			if( substrateId.equals( "C00002" ) && productId1.equals( "C00008" ) && productId2.equals( "C00009" ) ) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			{
				return false;
			}
			else if( substrateId.equals( "C00006" ) && productId1.equals( "C00005" ) && productId2.equals( "C00007" ) ) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			{
				return false;
			}
		}
		else if( substrateIds.size() == 2 && productIds.size() == 1 )
		{
			final List<String> substrateIdList = new ArrayList<>( substrateIds );
			final String substrateId1 = substrateIdList.get( 0 );
			final String substrateId2 = substrateIdList.get( 1 );
			final String productId = CollectionUtils.getFirst( productIds );
			
			if( productId.equals( "C00002" ) && substrateId1.equals( "C00008" ) && substrateId2.equals( "C00009" ) ) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			{
				return false;
			}
			else if( productId.equals( "C00006" ) && substrateId1.equals( "C00005" ) && substrateId2.equals( "C00007" ) ) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			{
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * @param reaction
	 * @return Collection<OntologyTerm>
	 * @throws Exception 
	 */
	private static Collection<OntologyTerm> getKeggReactionTerms( final Model model, final Reaction reaction ) throws Exception
	{
		final Collection<OntologyTerm> ignoredCompoundTerms = new HashSet<>();
		final OntologyUtils ontologyUtils = OntologyUtils.getInstance();
		ignoredCompoundTerms.add( ontologyUtils.getOntologyTerm( Ontology.KEGG_COMPOUND, "C00001" ) ); //$NON-NLS-1$ // water
		ignoredCompoundTerms.add( ontologyUtils.getOntologyTerm( Ontology.KEGG_COMPOUND, "C00080" ) ); //$NON-NLS-1$ // H+
		
		final Collection<OntologyTerm> keggReactionTerms = new LinkedHashSet<>();
		OntologyTerm keggReactionTerm = SbmlUtils.getOntologyTerm( reaction, Ontology.KEGG_REACTION );
		
		if( keggReactionTerm == null )
		{
			final Collection<String> reactants = new HashSet<>();
			final Collection<String> products = new HashSet<>();
			
			for( int l = 0; l < reaction.getNumReactants(); l++ )
			{
				final Species species = SbmlUtils.getReactantSpecies( model, reaction.getId(), l );
				final Collection<OntologyTerm> ontologyTerms = SbmlUtils.getOntologyTerms( species, CVTerm.Type.BIOLOGICAL_QUALIFIER, CVTerm.Qualifier.BQB_IS );
				
				for( OntologyTerm ontologyTerm : ontologyTerms )
				{
					final OntologyTerm keggCompoundTerm = OntologyUtils.getInstance().getXref( ontologyTerm, Ontology.KEGG_COMPOUND );
					
					if( keggCompoundTerm != null && !ignoredCompoundTerms.contains( keggCompoundTerm ) )
					{
						reactants.add( keggCompoundTerm.getId() );
						break;
					}
				}
			}
			
			for( int l = 0; l < reaction.getNumProducts(); l++ )
			{
				final Species species = SbmlUtils.getProductSpecies( model, reaction.getId(), l );
				final Collection<OntologyTerm> ontologyTerms = SbmlUtils.getOntologyTerms( species, CVTerm.Type.BIOLOGICAL_QUALIFIER, CVTerm.Qualifier.BQB_IS );
				
				for( OntologyTerm ontologyTerm : ontologyTerms )
				{
					final OntologyTerm keggCompoundTerm = OntologyUtils.getInstance().getXref( ontologyTerm, Ontology.KEGG_COMPOUND );
					
					if( keggCompoundTerm != null && !ignoredCompoundTerms.contains( keggCompoundTerm ) )
					{
						products.add( keggCompoundTerm.getId() );
						break;
					}
				}
			}
			
			final Collection<OntologyTerm> forwardKeggReactionTerms = getReactionsFromSubstratesAndProducts( reactants, products );
			
			if( forwardKeggReactionTerms.size() != 0 )
			{
				final Collection<OntologyTerm> selectedKeggReactionTerm = selectKeggReactionTerms( reactants, products, forwardKeggReactionTerms );
				keggReactionTerms.addAll( selectedKeggReactionTerm );
			}
			else
			{
				// Flip reactants and products:
				final Collection<OntologyTerm> selectedKeggReactionTerm = selectKeggReactionTerms( products, reactants, getReactionsFromSubstratesAndProducts( products, reactants ) );
				keggReactionTerms.addAll( selectedKeggReactionTerm );
			}
		}
		else
		{
			keggReactionTerms.add( keggReactionTerm );
		}
		
		return keggReactionTerms;
	}
	
	/**
	 * 
	 * @param reactants
	 * @param products
	 * @param keggReactionTerms
	 * @return Collection<OntologyTerm>
	 * @throws Exception
	 */
	private final static Collection<OntologyTerm> selectKeggReactionTerms( final Collection<String> reactants, final Collection<String> products, final Collection<OntologyTerm> keggReactionTerms ) throws Exception
	{
		int bestScore = 0;
		OntologyTerm selectedKeggReactionTerm = null;
		
		for( OntologyTerm keggReactionTerm : keggReactionTerms )
		{
			final Collection<OntologyTerm> keggReactionTermSubstrates = ( (KeggReactionTerm)keggReactionTerm ).getSubstrates().keySet();
			final Collection<OntologyTerm> keggReactionTermProducts = ( (KeggReactionTerm)keggReactionTerm ).getProducts().keySet();
			final Collection<String> keggReactionTermSubstrateIds = new ArrayList<>();
			final Collection<String> keggReactionTermProductIds = new ArrayList<>();
			
			for( OntologyTerm keggReactionTermSubstrate : keggReactionTermSubstrates )
			{
				final String keggReactionTermSubstrateId = keggReactionTermSubstrate.getId();
				
				if( !keggReactionTermSubstrateId.equals( "C00001" ) && !keggReactionTermSubstrateId.equals( "C00080" ) ) //$NON-NLS-1$ //$NON-NLS-2$
				{
					keggReactionTermSubstrateIds.add( keggReactionTermSubstrateId );
				}
			}
			for( OntologyTerm keggReactionTermProduct : keggReactionTermProducts )
			{
				final String keggReactionTermProductId = keggReactionTermProduct.getId();
				
				if( !keggReactionTermProductId.equals( "C00001" ) && !keggReactionTermProductId.equals( "C00080" ) ) //$NON-NLS-1$ //$NON-NLS-2$
				{
					keggReactionTermProductIds.add( keggReactionTermProductId );
				}
			}
			
			int score = CollectionUtils.getIntersection( Arrays.asList( reactants, keggReactionTermSubstrateIds ) ).size() + CollectionUtils.getIntersection( Arrays.asList( products, keggReactionTermProductIds ) ).size();
			
			if( score > bestScore )
			{
				selectedKeggReactionTerm = keggReactionTerm;
				bestScore = score;
			}
			else if( score == bestScore )
			{
				selectedKeggReactionTerm = null;
			}
		}
		
		/*
		if( selectedKeggReactionTerm != null && keggReactionTerms.size() > 0 )
		{
			System.out.println();
			System.out.println( reactants + "\t" + products ); //$NON-NLS-1$
			
			for( OntologyTerm keggReactionTerm : keggReactionTerms )
			{
				System.out.println( keggReactionTerm.getId() + "\t" + keggReactionTerm ); //$NON-NLS-1$
				System.out.println( reaction.getId() + "\t" + sbmlUtils.toString( model, reaction.getId(), false ) ); //$NON-NLS-1$
			}
		}
		*/
		
		return selectedKeggReactionTerm == null ? keggReactionTerms : Arrays.asList( selectedKeggReactionTerm );
	}
	
	/**
	 * 
	 * @param args
	 * @throws Exception 
	 * @throws FactoryConfigurationError 
	 */
	public static void main( String[] args ) throws FactoryConfigurationError, Exception
	{
		KeggReactionUtils.annotate( new File( args[ 0 ] ), new File( args[ 1 ] ) );
	}
}