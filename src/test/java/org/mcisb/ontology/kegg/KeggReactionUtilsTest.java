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

import java.util.*;
import org.junit.*;
import org.mcisb.ontology.*;
import org.mcisb.ontology.chebi.*;

/**
 * 
 * @author Neil Swainston
 */
public class KeggReactionUtilsTest
{
	/**
	 * 
	 */
	private KeggReactionUtils utils = KeggReactionUtils.getInstance();

	/**
	 * 
	 * @throws Exception
	 */
	public KeggReactionUtilsTest() throws Exception
	{
		// No implementation.
	}

	/**
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("static-method")
	@Test
	public void getReactionIdsByCompoundId() throws Exception
	{
		final Collection<String> reactionIds = Arrays.asList( KeggReactionUtils.getReactionIdsByCompoundId( "C00469" ) ); //$NON-NLS-1$
		Assert.assertTrue( reactionIds.containsAll( Arrays.asList( "rn:R00746", "rn:R00754", "rn:R02359", "rn:R02682", "rn:R04410", "rn:R05198" ) ) ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
	}

	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void getReactionsFromGeneId() throws Exception
	{
		final Collection<OntologyTerm> reactions = utils.getReactionsFromGeneId( "sce:YFR053C" ); //$NON-NLS-1$
		final Collection<KeggReactionTerm> expectedReactions = Arrays.asList( new KeggReactionTerm( "R01600" ), new KeggReactionTerm( "R01786" ), new KeggReactionTerm( "R01326" ) ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		Assert.assertTrue( reactions.containsAll( expectedReactions ) );
	}

	/**
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("static-method")
	@Test
	public void getPathways() throws Exception
	{
		final Collection<OntologyTerm> pathways = new KeggReactionTerm( "R00746" ).getPathways(); //$NON-NLS-1$
		final List<KeggPathwayTerm> expectedPathways = Arrays.asList( new KeggPathwayTerm( "rn00010" ), new KeggPathwayTerm( "rn01100" ), new KeggPathwayTerm( "rn01110" ) ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		Assert.assertTrue( pathways.containsAll( expectedPathways ) );
	}

	/**
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("static-method")
	@Test
	public void getProducts() throws Exception
	{
		final Collection<OntologyTerm> products = new KeggReactionTerm( "R05969" ).getProducts().keySet(); //$NON-NLS-1$
		Assert.assertTrue( products.contains( new KeggCompoundTerm( "C00105" ) ) ); //$NON-NLS-1$
		Assert.assertTrue( products.contains( new KeggGlycanTerm( "G00001" ) ) ); //$NON-NLS-1$

		final Collection<OntologyTerm> reactants = new KeggReactionTerm( "R04241" ).getSubstrates().keySet(); //$NON-NLS-1$
		Assert.assertTrue( reactants.contains( new KeggCompoundTerm( "C03541" ) ) ); //$NON-NLS-1$
	}

	/**
	 * 
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("static-method")
	@Test
	public void getReactionsBySubstrateAndProduct() throws Exception
	{
		final Collection<OntologyTerm> reactions1 = KeggReactionUtils.getReactionsFromSubstrateAndProduct( "C00041", "C00183" ); //$NON-NLS-1$ //$NON-NLS-2$
		final Collection<OntologyTerm> reactions2 = KeggReactionUtils.getReactionsFromSubstrateAndProduct( "C00183", "C00041" ); //$NON-NLS-1$ //$NON-NLS-2$

		final OntologySource chebiUtils = ChebiUtils.getInstance();
		final Collection<OntologyTerm> reactions3 = KeggReactionUtils.getReactionsFromSubstrateAndProduct( chebiUtils.getOntologyTerm( "CHEBI%3A16977" ), chebiUtils.getOntologyTerm( "CHEBI%3A16414" ) ); //$NON-NLS-1$ //$NON-NLS-2$
		final Collection<OntologyTerm> reactions4 = KeggReactionUtils.getReactionsFromSubstrateAndProduct( chebiUtils.getOntologyTerm( "CHEBI%3A16414" ), chebiUtils.getOntologyTerm( "CHEBI%3A16977" ) ); //$NON-NLS-1$ //$NON-NLS-2$

		final OntologyTerm keggReactionTerm = new KeggReactionTerm( "R01215" ); //$NON-NLS-1$

		Assert.assertTrue( reactions1.contains( keggReactionTerm ) );
		Assert.assertTrue( reactions2.contains( keggReactionTerm ) );
		Assert.assertTrue( reactions3.contains( keggReactionTerm ) );
		Assert.assertTrue( reactions4.contains( keggReactionTerm ) );
	}

	/**
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("static-method")
	@Test
	public void getSubstratesAndProducts() throws Exception
	{
		final OntologyTerm keggReactionTerm = new KeggReactionTerm( "R00382" ); //$NON-NLS-1$
		final Collection<OntologyTerm> keggReactionTermSubstrates = ( (KeggReactionTerm)keggReactionTerm ).getSubstrates().keySet();
		final Collection<OntologyTerm> keggReactionTermProducts = ( (KeggReactionTerm)keggReactionTerm ).getProducts().keySet();

		for( OntologyTerm keggReactionTermSubstrate : keggReactionTermSubstrates )
		{
			Assert.assertNotNull( keggReactionTermSubstrate.getId() );
		}

		for( OntologyTerm keggReactionTermProduct : keggReactionTermProducts )
		{
			Assert.assertNotNull( keggReactionTermProduct.getId() );
		}
	}
}