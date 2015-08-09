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
public class DefaultOntologySourceTest
{
	/**
	 * 
	 */
	private final static String ONTOLOGY_NAME = Ontology.HMDB;
	
	/**
	 * 
	 */
	private final static String TERM_ID = "HMDB00122"; //$NON-NLS-1$
	
	/**
	 * 
	 */
	private final OntologyTerm ontologyTerm = new DefaultOntologySource( ONTOLOGY_NAME ).getOntologyTerm( TERM_ID );
	
	/**
	 * 
	 *
	 * @throws Exception
	 */
	public DefaultOntologySourceTest() throws Exception
	{
		// No implementation.
	}
	
	/**
	 *
	 * @throws Exception
	 */
	@Test 
	public void getOntologyName() throws Exception
	{
		Assert.assertEquals( ontologyTerm.getOntology(), OntologyFactory.getOntology( ONTOLOGY_NAME ) );
	}
	
	/**
	 *
	 */
	@Test 	
	public void getId()
	{
		Assert.assertEquals( ontologyTerm.getId(), TERM_ID );
	}
	
	/**
	 *
	 * @throws Exception
	 */
	@Test 	
	public void getName() throws Exception
	{
		Assert.assertEquals( ontologyTerm.getName(), null );
	}
}