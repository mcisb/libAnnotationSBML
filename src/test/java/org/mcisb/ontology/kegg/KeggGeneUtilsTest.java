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
import org.mcisb.ontology.ec.*;

/**
 * 
 * @author Neil Swainston
 */
public class KeggGeneUtilsTest
{
	/**
	 * 
	 */
	private KeggGeneUtils utils = KeggGeneUtils.getInstance();

	/**
	 * 
	 * @throws Exception
	 */
	public KeggGeneUtilsTest() throws Exception
	{
		// No implementation.
	}

	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void getOntologyTerm() throws Exception
	{
		final OntologyTerm ontologyTerm = utils.getOntologyTerm( "sce:YFR053C" ); //$NON-NLS-1$
		test( ontologyTerm );
	}

	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void getOntologyTerm2() throws Exception
	{
		final OntologyTerm ontologyTerm = utils.getOntologyTerm( "sce:YLR262C-A" ); //$NON-NLS-1$
		test2( ontologyTerm );
	}

	/**
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("static-method")
	@Test
	public void getOntologyTerm3() throws Exception
	{
		final OntologyTerm ontologyTerm = OntologyUtils.getInstance().getOntologyTerm( "http://www.genome.jp/kegg/genes/#sce:YLR262C-A" ); //$NON-NLS-1$
		test2( ontologyTerm );
	}

	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void getOntologyTerm4() throws Exception
	{
		final OntologyTerm ontologyTerm = utils.getOntologyTerm( "hsa:6547" ); //$NON-NLS-1$
		test3( ontologyTerm );
	}

	/**
	 * 
	 * @param ontologyTerm
	 * @throws Exception
	 */
	public static void test( final OntologyTerm ontologyTerm ) throws Exception
	{
		final String NAME = "HXK1"; //$NON-NLS-1$
		Assert.assertTrue( ontologyTerm.getName().equals( NAME ) );
		Assert.assertTrue( ontologyTerm.getXrefs().keySet().contains( EcUtils.getInstance().getOntologyTerm( "2.7.1.1" ) ) ); //$NON-NLS-1$
	}

	/**
	 * 
	 * @param ontologyTerm
	 * @throws Exception
	 */
	public static void test2( final OntologyTerm ontologyTerm ) throws Exception
	{
		final String NAME = "TMA7"; //$NON-NLS-1$
		Assert.assertTrue( ontologyTerm.getName().equals( NAME ) );
	}

	/**
	 * 
	 * @param ontologyTerm
	 * @throws Exception
	 */
	public static void test3( final OntologyTerm ontologyTerm ) throws Exception
	{
		Assert.assertTrue( OntologyUtils.getInstance().getXrefs( Arrays.asList( ontologyTerm ), Ontology.UNIPROT ).size() > 0 );
	}
}