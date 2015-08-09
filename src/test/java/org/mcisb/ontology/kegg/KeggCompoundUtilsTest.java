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

/**
 *
 * @author Neil Swainston
 */
public class KeggCompoundUtilsTest
{
	/**
	 * 
	 */
	private KeggCompoundUtils utils = KeggCompoundUtils.getInstance();
	
	/**
	 *
	 * @throws Exception
	 */
	public KeggCompoundUtilsTest() throws Exception
	{
		// No implementation.
	}
	
	/**
	 *
	 * @throws Exception
	 */
	@Test
	public void getCompound() throws Exception
	{
		final OntologyTerm ontologyTerm = utils.getOntologyTerm( "C00469" ); //$NON-NLS-1$
		test( ontologyTerm );
	}
	
	/**
	 *
	 * @throws Exception
	 */
	@Test
	public void getCompound2() throws Exception
	{
		final KeggCompoundTerm ontologyTerm = (KeggCompoundTerm)utils.getOntologyTerm( "L-Lysine" ); //$NON-NLS-1$
		Assert.assertTrue( ontologyTerm.getId().equals( "C00047" ) ); //$NON-NLS-1$
		Assert.assertTrue( ontologyTerm.getSmiles() == null );
	}
	
	/**
	 * @throws Exception 
	 */
	/*
	@SuppressWarnings("static-method")
	@Test
	public void getSmiles() throws Exception
	{
		Assert.assertTrue( new KeggCompoundTerm( "C00031" ).getSmiles().equals( "OC[C@H]1OC(O)[C@H](O)[C@@H](O)[C@@H]1O" ) ); //$NON-NLS-1$ //$NON-NLS-2$
	}
	*/
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void getReactions() throws Exception
	{
		final KeggReactionParticipantTerm ontologyTerm = (KeggReactionParticipantTerm)utils.getOntologyTerm( "C00002" ); //$NON-NLS-1$
		Assert.assertTrue( ontologyTerm.getReactions().contains( "R09047" ) ); //$NON-NLS-1$
	}
	
	/**
	 *
	 * @param ontologyTerm
	 * @throws Exception
	 */
	public static void test( final OntologyTerm ontologyTerm ) throws Exception
	{
		final OntologyUtils ontologyUtils = OntologyUtils.getInstance();
		final OntologyTerm chebiTerm = ontologyUtils.getOntologyTerm( Ontology.CHEBI, "CHEBI:16236" ); //$NON-NLS-1$
		final OntologyTerm pubChemTerm = ontologyUtils.getOntologyTerm( Ontology.PUBCHEM_SUBSTANCE, "3752" ); //$NON-NLS-1$
		final Collection<String> synonyms = new HashSet<>( Arrays.asList( "Ethyl alcohol", "Methylcarbinol" ) ); //$NON-NLS-1$ //$NON-NLS-2$
		final String NAME = "Ethanol"; //$NON-NLS-1$
		Assert.assertTrue( ontologyTerm.getSynonyms().equals( synonyms ) );
		Assert.assertTrue( ontologyTerm.getName().equals( NAME ) );
		Assert.assertTrue( ontologyTerm.getXrefs().keySet().contains( chebiTerm ) );
		Assert.assertTrue( ontologyTerm.getXrefs().keySet().contains( pubChemTerm ) );
	}
}