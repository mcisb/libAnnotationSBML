/*******************************************************************************
. * Manchester Centre for Integrative Systems Biology
 * University of Manchester
 * Manchester M1 7ND
 * United Kingdom
 *
 * Copyright (C) 2007 University of Manchester
 *
 * This program is released under the Academic Free License ("AFL") v3.0.
 * (http://www.opensource.org/licenses/academic.php)
 *******************************************************************************/
package org.mcisb.ontology.gene;

import org.junit.*;
import org.mcisb.ontology.*;

/**
 *
 * @author Neil Swainston
 */
public class GeneUtilsTest
{
	/**
	 *
	 */
	private OntologySource utils = GeneUtils.getInstance();

	/**
	 *
	 * @throws Exception
	 */
	public GeneUtilsTest() throws Exception
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
		final GeneTerm ontologyTerm = (GeneTerm)utils.getOntologyTerm( "6547" ); //$NON-NLS-1$
		test( ontologyTerm );
	}

	/**
	 *
	 * @param ontologyTerm
	 * @throws Exception
	 */
	@SuppressWarnings("static-method")
	public void test( final GeneTerm ontologyTerm ) throws Exception
	{
		final String NAME = "SLC8A3 solute carrier family 8 (sodium/calcium exchanger), member 3"; //$NON-NLS-1$
		Assert.assertTrue( ontologyTerm.getName().equals( NAME ) );
	}
}