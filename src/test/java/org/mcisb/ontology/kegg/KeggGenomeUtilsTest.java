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
public class KeggGenomeUtilsTest
{
	/**
	 * 
	 */
	private KeggGenomeUtils utils = KeggGenomeUtils.getInstance();

	/**
	 * 
	 * @throws Exception
	 */
	public KeggGenomeUtilsTest() throws Exception
	{
		// No implementation.
	}

	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void getOrganism() throws Exception
	{
		final OntologyTerm ontologyTerm = utils.getOntologyTerm( "sce" ); //$NON-NLS-1$
		test( ontologyTerm );
	}

	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void getOrganismFromTaxonomyId() throws Exception
	{
		// Assert.assertTrue( utils.getOrganism( 4565 ).getId().equals( "etae" ) ); //$NON-NLS-1$
		Assert.assertTrue( utils.getOrganism( 559292 ).getId().equals( "sce" ) ); //$NON-NLS-1$
	}

	/**
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("static-method")
	@Test
	public void getOrganismIds() throws Exception
	{
		Assert.assertTrue( Arrays.binarySearch( KeggGenomeUtils.getOrganismIds(), "etae" ) != -1 ); //$NON-NLS-1$
	}

	/**
	 * 
	 * @param ontologyTerm
	 * @throws Exception
	 */
	public void test( final OntologyTerm ontologyTerm ) throws Exception
	{
		final Collection<String> synonyms = new HashSet<>( Arrays.asList( "YEAST", "559292" ) ); //$NON-NLS-1$ //$NON-NLS-2$
		final String NAME = "Saccharomyces cerevisiae S288c"; //$NON-NLS-1$
		final OntologyTerm taxonomyTerm = OntologyUtils.getInstance().getOntologyTerm( Ontology.TAXONOMY, "559292" ); //$NON-NLS-1$
		Assert.assertTrue( ontologyTerm.getSynonyms().equals( synonyms ) );
		Assert.assertTrue( ontologyTerm.getName().equals( NAME ) );
		Assert.assertTrue( utils.getOrganisms().contains( ontologyTerm ) );
		Assert.assertTrue( ontologyTerm.getXrefs().keySet().contains( taxonomyTerm ) );
	}
}