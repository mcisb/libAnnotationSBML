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
package org.mcisb.ontology.ec;

import org.junit.*;

/**
 *
 * @author Neil Swainston
 */
public class EcUtilsTest
{
	/**
	 * 
	 */
	private EcUtils utils = EcUtils.getInstance();
	
	/**
	 *
	 * @throws Exception
	 */
	public EcUtilsTest() throws Exception
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
		Assert.assertNotNull( utils.getOntologyTerm( "1.-.-.-" ) ); //$NON-NLS-1$
		Assert.assertNotNull( utils.getOntologyTerm( "1.1.-.-" ) ); //$NON-NLS-1$
		Assert.assertNotNull( utils.getOntologyTerm( "1.1.1.-" ) ); //$NON-NLS-1$
		
		getOntologyTerm( "4.1.99.12" ); //$NON-NLS-1$
		getOntologyTerm( "ec:4.1.99.12" ); //$NON-NLS-1$
		// getOntologyTerm( "EC 4.1.99.12" ); //$NON-NLS-1$
		// getOntologyTerm( "noise noise noise 4.1.99.12 noise noise noise" ); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void matches() throws Exception
	{
		Assert.assertTrue( utils.matches( "10.1.1.-", "10.1.1.7" ) ); //$NON-NLS-1$ //$NON-NLS-2$
		Assert.assertFalse( utils.matches( "10.1.1.7", "10.1.1.-" ) ); //$NON-NLS-1$ //$NON-NLS-2$
		Assert.assertTrue( utils.matches( "10.-.-.-", "10.1.1.7" ) ); //$NON-NLS-1$ //$NON-NLS-2$
		Assert.assertFalse( utils.matches( "10.1.1.7", "10.1.1.8" ) ); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 *
	 * @param identifier
	 * @throws Exception
	 */
	private void getOntologyTerm( final String identifier ) throws Exception
	{
		final EcTerm ontologyTerm = (EcTerm)utils.getOntologyTerm( identifier );
		test( ontologyTerm );
	}
	
	/**
	 *
	 * @param ontologyTerm
	 * @throws Exception
	 */
	@SuppressWarnings("static-method")
	public void test( final EcTerm ontologyTerm ) throws Exception
	{
		final String NAME = "3,4-dihydroxy-2-butanone-4-phosphate synthase"; //$NON-NLS-1$
		Assert.assertTrue( ontologyTerm.getName().equals( NAME ) );
		Assert.assertTrue( ontologyTerm.getReactions().contains( "R07281" ) ); //$NON-NLS-1$
	}
}