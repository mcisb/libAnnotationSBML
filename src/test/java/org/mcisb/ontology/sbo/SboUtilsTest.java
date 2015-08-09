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
package org.mcisb.ontology.sbo;

import java.util.*;
import org.junit.*;
import org.mcisb.ontology.*;
import org.sbml.jsbml.*;

/**
 * 
 * @author Neil Swainston
 */
public class SboUtilsTest
{
	/**
	 * 
	 */
	private final SboUtils utils = SboUtils.getInstance();

	/**
	 * 
	 * @throws Exception
	 */
	public SboUtilsTest() throws Exception
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

		SboTerm ontologyTerm = (SboTerm)utils.getOntologyTerm( "Henri-Michaelis-Menten rate law" ); //$NON-NLS-1$
		ontologyTermsSet.add( ontologyTerm );
		ontologyTermsList.add( ontologyTerm );
		test( ontologyTerm );

		ontologyTerm = (SboTerm)utils.getOntologyTerm( "SBO:0000029" ); //$NON-NLS-1$
		ontologyTermsSet.add( ontologyTerm );
		ontologyTermsList.add( ontologyTerm );
		test( ontologyTerm );

		ontologyTerm = (SboTerm)utils.getOntologyTerm( 29 );
		ontologyTermsSet.add( ontologyTerm );
		ontologyTermsList.add( ontologyTerm );
		test( ontologyTerm );

		Assert.assertTrue( ontologyTermsSet.size() == 1 );
		Assert.assertTrue( ontologyTermsList.size() == 3 );
	}

	/**
	 * 
	 * @param ontologyTerm
	 * @throws Exception
	 */
	public void test( final SboTerm ontologyTerm ) throws Exception
	{
		Assert.assertTrue( ontologyTerm.getName().equals( "Henri-Michaelis-Menten rate law" ) ); //$NON-NLS-1$
		Assert.assertTrue( ontologyTerm.getFormula().equals( "lambda(kcat, Et, S, Ks, kcat*Et*S/(Ks+S))" ) ); //$NON-NLS-1$
		Assert.assertTrue( ontologyTerm.getIntId() == 29 );
		Assert.assertTrue( utils.getShortName( ontologyTerm.getRawMath(), 373 ).equals( "Ks" ) ); //$NON-NLS-1$

		final KineticLaw kineticLaw = new KineticLaw();
		kineticLaw.setMath( JSBML.readMathMLFromString( ontologyTerm.getMath() ) );
		Assert.assertTrue( JSBML.formulaToString( kineticLaw.getMath() ).equals( "lambda(kcat, Et, S, Ks, kcat*Et*S/(Ks+S))" ) ); //$NON-NLS-1$
	}
}