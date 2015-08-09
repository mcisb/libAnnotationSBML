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

import org.junit.*;

/**
 * 
 *
 * @author Neil Swainston
 */
public class OntologyLookupServiceClientTest
{
	/**
	 * 
	 */
	private final static String ONTOLOGY_NAME = "CHEBI"; //$NON-NLS-1$
	
	/**
	 * 
	 */
	private final static String TERM_NAME = "glucose"; //$NON-NLS-1$
	
	/**
	 * 
	 */
	private final static String TERM_ID = "CHEBI:15903"; //$NON-NLS-1$
	
	/**
	 *
	 * @throws Exception
	 */
	@SuppressWarnings("static-method")
	@Test 
	public void getOntologyNames() throws Exception
	{
		Assert.assertTrue( OntologyLookupServiceClient.getOntologyNames().keySet().contains( ONTOLOGY_NAME ) );
	}
	
	/**
	 *
	 * @throws Exception
	 */
	@SuppressWarnings("static-method")
	@Test 	
	public void getTermsByName() throws Exception
	{
		Assert.assertTrue( OntologyLookupServiceClient.getTermsByName( TERM_NAME, ONTOLOGY_NAME, false ).keySet().contains( TERM_ID ) );
	}
	
	/**
	 *
	 * @throws Exception
	 */
	@SuppressWarnings("static-method")
	@Test 	
	public void getTermsById() throws Exception
	{
		Assert.assertTrue( OntologyLookupServiceClient.getTermById( TERM_ID, ONTOLOGY_NAME ).contains( TERM_NAME ) );
	}
}