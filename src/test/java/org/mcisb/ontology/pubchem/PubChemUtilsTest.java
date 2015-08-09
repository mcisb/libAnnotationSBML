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
package org.mcisb.ontology.pubchem;

import org.junit.*;
import org.mcisb.ontology.*;

/**
 *
 * @author Neil Swainston
 */
public class PubChemUtilsTest
{
	/**
	 * 
	 */
	private final PubChemUtils compoundUtils = new PubChemUtils( Ontology.PUBCHEM_COMPOUND );
	
	/**
	 * 
	 */
	private final PubChemUtils substanceUtils = new PubChemUtils( Ontology.PUBCHEM_SUBSTANCE );
	
	/**
	 * 
	 */
	final PubChemTerm compoundATP = (PubChemTerm)compoundUtils.getOntologyTermFromId( "5957" ); //$NON-NLS-1$
	
	/**
	 * 
	 */
	final PubChemTerm substanceATP = (PubChemTerm)substanceUtils.getOntologyTermFromId( "53788912" ); //$NON-NLS-1$
	
	/**
	 * 
	 * @throws Exception
	 */
	public PubChemUtilsTest() throws Exception
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
		testCompoundATP( compoundATP ); 
		testSubstanceATP( substanceATP ); 
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void getFormula() throws Exception
	{
		Assert.assertTrue( ( (PubChemTerm)compoundUtils.getOntologyTermFromId( "46878370" ) ).getFormula().equals( "C20H21N7O7" ) ); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 *
	 * @throws Exception
	 */
	@Test
	public void search() throws Exception
	{
		final String ATP = "ATP"; //$NON-NLS-1$
		Assert.assertTrue( compoundUtils.search( ATP ).contains( compoundATP ) );
		Assert.assertTrue( substanceUtils.search( ATP ).contains( substanceATP ) );
		
		Assert.assertTrue( compoundUtils.search( "S-acetyldihydrolipoamide" ).size() == 1 ); //$NON-NLS-1$
		Assert.assertTrue( compoundUtils.search( "fructose-1,6-bisphosphate" ).size() > 0 ); //$NON-NLS-1$
		Assert.assertTrue( compoundUtils.search( "pyrimidine" ).size() > 1 ); //$NON-NLS-1$
	}
	
	/**
	 *
	 * @param ontologyTerm
	 * @throws Exception
	 */
	@SuppressWarnings("static-method")
	private void testCompoundATP( final PubChemTerm ontologyTerm ) throws Exception
	{
		final OntologyUtils ontologyUtils = OntologyUtils.getInstance();
		final OntologyTerm inchiTerm = ontologyUtils.getOntologyTerm( Ontology.INCHI, "InChI=1S/C10H16N5O13P3/c11-8-5-9(13-2-12-8)15(3-14-5)10-7(17)6(16)4(26-10)1-25-30(21,22)28-31(23,24)27-29(18,19)20/h2-4,6-7,10,16-17H,1H2,(H,21,22)(H,23,24)(H2,11,12,13)(H2,18,19,20)/t4-,6-,7-,10-/m1/s1" ); //$NON-NLS-1$
		final OntologyTerm chebiTerm = ontologyUtils.getOntologyTerm( Ontology.CHEBI, "CHEBI:15422" ); //$NON-NLS-1$
		Assert.assertTrue( ontologyTerm.getXrefs().keySet().contains( inchiTerm ) );
		Assert.assertTrue( ontologyTerm.getXrefs().keySet().contains( chebiTerm ) );
		Assert.assertTrue( ontologyTerm.getSynonyms().contains( "Adenosine, 5'-(tetrahydrogen triphosphate)" ) ); //$NON-NLS-1$
		Assert.assertTrue( ontologyTerm.getFormula().equals( "C10H16N5O13P3" ) ); //$NON-NLS-1$
		Assert.assertTrue( ontologyTerm.getCharge() == 0 );
		Assert.assertTrue( ontologyTerm.getSmiles().equals( "C1=NC2=C(C(=N1)N)N=CN2C3C(C(C(O3)COP(=O)(O)OP(=O)(O)OP(=O)(O)O)O)O" ) ); //$NON-NLS-1$
		Assert.assertTrue( ontologyTerm.getName().equals( "Adenosine triphosphate" ) ); //$NON-NLS-1$
	}
	
	/**
	 *
	 * @param ontologyTerm
	 * @throws Exception
	 */
	private void testSubstanceATP( final OntologyTerm ontologyTerm ) throws Exception
	{
		Assert.assertTrue( ontologyTerm.getSynonyms().contains( "Adenosine 5'-(tetrahydrogen triphosphate)" ) ); //$NON-NLS-1$
		Assert.assertTrue( ontologyTerm.getXrefs().keySet().contains( compoundATP ) );
		Assert.assertTrue( ontologyTerm.getName().equals( "Adenosine triphosphate" ) ); //$NON-NLS-1$
	}
}