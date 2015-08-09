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
import org.junit.*;
import org.mcisb.ontology.*;

/**
 * 
 * @author Neil Swainston
 */
public class GoUtilsTest
{
	/**
	 *
	 */
	private final GoUtils utils = GoUtils.getInstance();

	/**
	 * 
	 */
	private final GoTerm mitochondriaGoTerm = new GoTerm( GoUtils.MITOCHONDRIA_GO_TERM_ID );

	/**
	 * 
	 * @throws Exception
	 */
	public GoUtilsTest() throws Exception
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
		final Set<OntologyTerm> ontologyTermsSet = new HashSet<>();
		final List<OntologyTerm> ontologyTermsList = new ArrayList<>();

		GoTerm ontologyTerm = (GoTerm)utils.getOntologyTerm( "cytoplasm" ); //$NON-NLS-1$
		ontologyTermsSet.add( ontologyTerm );
		ontologyTermsList.add( ontologyTerm );
		test1( ontologyTerm );

		ontologyTerm = (GoTerm)utils.getOntologyTerm( "GO:0005737" ); //$NON-NLS-1$
		ontologyTermsSet.add( ontologyTerm );
		ontologyTermsList.add( ontologyTerm );
		test1( ontologyTerm );

		ontologyTerm = (GoTerm)utils.getOntologyTerm( "GO%3A0005737" ); //$NON-NLS-1$
		ontologyTermsSet.add( ontologyTerm );
		ontologyTermsList.add( ontologyTerm );
		test1( ontologyTerm );

		Assert.assertTrue( ontologyTermsSet.size() == 1 );
		Assert.assertTrue( ontologyTermsList.size() == 3 );
	}

	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void getOntologyTerm2() throws Exception
	{
		Assert.assertTrue( mitochondriaGoTerm.getName().equals( "mitochondrion" ) ); //$NON-NLS-1$
	}

	/**
	 * 
	 * @param ontologyTerm
	 * @throws Exception
	 */
	@SuppressWarnings("static-method")
	public void test1( final GoTerm ontologyTerm ) throws Exception
	{
		final String NAME = "cytoplasm"; //$NON-NLS-1$
		Assert.assertTrue( ontologyTerm.getName().equals( NAME ) );
	}

	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void getParents() throws Exception
	{
		final GoTerm ontologyTerm = new GoTerm( "GO%3A0044429" ); //$NON-NLS-1$
		Assert.assertTrue( utils.getParents( ontologyTerm ).contains( new GoTerm( "GO%3A0044446" ) ) ); //$NON-NLS-1$
	}
}