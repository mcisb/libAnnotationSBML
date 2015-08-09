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
public class OntologyFactoryTest
{
	/**
	 * 
	 */
	private final static String ONTOLOGY_NAME = Ontology.CHEBI;
	
	/**
	 * 
	 */
	private final static String ONTOLOGY_URI = "http://www.ebi.ac.uk/chebi/#ChEBI:12345"; //$NON-NLS-1$
	
	/**
	 *
	 * @throws Exception
	 */
	@SuppressWarnings("static-method")
	@Test 
	public void getOntologies() throws Exception
	{
		Assert.assertTrue( OntologyFactory.getOntologies().contains( OntologyFactory.getOntology( ONTOLOGY_NAME ) ) );
	}
	
	/**
	 *
	 * @throws Exception
	 */
	@SuppressWarnings("static-method")
	@Test 	
	public void getOntology() throws Exception
	{
		Assert.assertEquals( OntologyFactory.getOntology( ONTOLOGY_NAME ).getName(), ONTOLOGY_NAME );
	}
	
	/**
	 *
	 * @throws Exception
	 */
	@SuppressWarnings("static-method")
	@Test 	
	public void getOntologyFromURI() throws Exception
	{
		Assert.assertEquals( OntologyFactory.getOntologyFromUri( ONTOLOGY_URI ).getName(), ONTOLOGY_NAME );
	}
}