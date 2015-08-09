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
package org.mcisb.ontology.taxonomy;

import org.junit.*;
import org.mcisb.ontology.*;

/**
 * 
 * @author Neil Swainston
 */
public class TaxonomyUtilsTest
{
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void getOntologyTerm() throws Exception
	{
		test( TaxonomyUtils.getInstance().getOntologyTerm( "4932" ) ); //$NON-NLS-1$
	}

	/**
	 * 
	 * @param ontologyTerm
	 * @throws Exception
	 */
	@SuppressWarnings("static-method")
	public void test( final OntologyTerm ontologyTerm ) throws Exception
	{
		Assert.assertEquals( ontologyTerm.getName(), "Saccharomyces cerevisiae" ); //$NON-NLS-1$
	}
}