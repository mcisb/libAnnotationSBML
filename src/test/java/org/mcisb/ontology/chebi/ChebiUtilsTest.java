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
package org.mcisb.ontology.chebi;

import java.util.*;
import org.junit.*;
import org.mcisb.ontology.*;
import org.mcisb.util.*;

/**
 * 
 * @author Neil Swainston
 */
public class ChebiUtilsTest
{
	/**
	 * 
	 */
	private final ChebiUtils utils = ChebiUtils.getInstance();

	/**
	 * 
	 * @throws Exception
	 */
	public ChebiUtilsTest() throws Exception
	{
		// No implementation.
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void isValid() throws Exception
	{
		final ChebiTerm chebiTermValid = (ChebiTerm)utils.getOntologyTerm( "CHEBI:16908" ); //$NON-NLS-1$
		Assert.assertTrue( chebiTermValid.isValid() );
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void isValidFalse() throws Exception
	{
		final ChebiTerm chebiTermInvalid = (ChebiTerm)utils.getOntologyTerm( "CHEBI:1690169088" ); //$NON-NLS-1$
		Assert.assertFalse( chebiTermInvalid.isValid() );
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void getNADHName() throws Exception
	{
		final ChebiTerm nadh = (ChebiTerm)utils.getOntologyTerm( "CHEBI:16908" ); //$NON-NLS-1$
		Assert.assertTrue( nadh.getName().equals( "NADH" ) ); //$NON-NLS-1$
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void getSynonyms() throws Exception
	{
		final ChebiTerm term1 = (ChebiTerm)utils.getOntologyTerm( "CHEBI:16807" ); //$NON-NLS-1$
		final ChebiTerm term2 = (ChebiTerm)utils.getOntologyTerm( "CHEBI:8940" ); //$NON-NLS-1$
		final ChebiTerm term3 = (ChebiTerm)utils.getOntologyTerm( "CHEBI:12738" ); //$NON-NLS-1$
		final ChebiTerm term4 = (ChebiTerm)utils.getOntologyTerm( "CHEBI:22029" ); //$NON-NLS-1$
		Assert.assertTrue( term1.getSynonyms().contains( "S-Acetyldihydrolipoamide" ) ); //$NON-NLS-1$
		Assert.assertTrue( term2.getSynonyms().contains( "S-Acetyldihydrolipoamide" ) ); //$NON-NLS-1$
		Assert.assertTrue( term3.getSynonyms().contains( "S-Acetyldihydrolipoamide" ) ); //$NON-NLS-1$
		Assert.assertTrue( term4.getSynonyms().contains( "S-Acetyldihydrolipoamide" ) ); //$NON-NLS-1$
		Assert.assertTrue( term1.getSynonyms().contains( "S-[6-amino-6-oxo-1-(2-sulfanylethyl)hexyl] ethanethioate" ) ); //$NON-NLS-1$
		Assert.assertTrue( term2.getSynonyms().contains( "S-[6-amino-6-oxo-1-(2-sulfanylethyl)hexyl] ethanethioate" ) ); //$NON-NLS-1$
		Assert.assertTrue( term3.getSynonyms().contains( "S-[6-amino-6-oxo-1-(2-sulfanylethyl)hexyl] ethanethioate" ) ); //$NON-NLS-1$
		Assert.assertTrue( term4.getSynonyms().contains( "S-[6-amino-6-oxo-1-(2-sulfanylethyl)hexyl] ethanethioate" ) ); //$NON-NLS-1$
		Assert.assertTrue( term1.getSynonyms().contains( "6-S-Acetyldihydrolipoamide" ) ); //$NON-NLS-1$
		Assert.assertTrue( term2.getSynonyms().contains( "6-S-Acetyldihydrolipoamide" ) ); //$NON-NLS-1$
		Assert.assertTrue( term3.getSynonyms().contains( "6-S-Acetyldihydrolipoamide" ) ); //$NON-NLS-1$
		Assert.assertTrue( term4.getSynonyms().contains( "6-S-Acetyldihydrolipoamide" ) ); //$NON-NLS-1$
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void getUndefinedCharge() throws Exception
	{
		final ChebiTerm thiosulfate_1_minus = (ChebiTerm)utils.getOntologyTerm( "CHEBI:33541" ); //$NON-NLS-1$
		Assert.assertTrue( thiosulfate_1_minus.getCharge() == NumberUtils.UNDEFINED );
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

		ChebiTerm ontologyTerm = (ChebiTerm)utils.getOntologyTerm( "ethanol" ); //$NON-NLS-1$
		ontologyTermsSet.add( ontologyTerm );
		ontologyTermsList.add( ontologyTerm );
		test( ontologyTerm );

		ontologyTerm = (ChebiTerm)utils.getOntologyTerm( "16236" ); //$NON-NLS-1$
		ontologyTermsSet.add( ontologyTerm );
		ontologyTermsList.add( ontologyTerm );
		testFormula( ontologyTerm );
		test( ontologyTerm );

		ontologyTerm = (ChebiTerm)utils.getOntologyTerm( "CHEBI:16236" ); //$NON-NLS-1$
		ontologyTermsSet.add( ontologyTerm );
		ontologyTermsList.add( ontologyTerm );
		testCharge( ontologyTerm );
		test( ontologyTerm );

		ontologyTerm = (ChebiTerm)utils.getOntologyTerm( "CHEBI%3A16236" ); //$NON-NLS-1$
		ontologyTermsSet.add( ontologyTerm );
		ontologyTermsList.add( ontologyTerm );
		test( ontologyTerm );

		Assert.assertTrue( ontologyTermsSet.size() == 1 );
		Assert.assertTrue( ontologyTermsList.size() == 4 );
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void isA() throws Exception
	{
		final ChebiTerm query1 = (ChebiTerm)utils.getOntologyTerm( "CHEBI:36141" ); //$NON-NLS-1$
		final ChebiTerm query2 = (ChebiTerm)utils.getOntologyTerm( "CHEBI:51578" ); //$NON-NLS-1$
		final ChebiTerm query3 = (ChebiTerm)utils.getOntologyTerm( "CHEBI:15422" ); //$NON-NLS-1$
		final ChebiTerm target = (ChebiTerm)utils.getOntologyTerm( "CHEBI:36141" ); //$NON-NLS-1$
		final Collection<String> relations = Arrays.asList( "is_a", "has_functional_parent" ); //$NON-NLS-1$ //$NON-NLS-2$

		Assert.assertTrue( utils.hasParent( query1, target, relations ) );
		Assert.assertTrue( utils.hasParent( query2, target, relations ) );
		Assert.assertFalse( utils.hasParent( query3, target, relations ) );
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void getProtonFormula() throws Exception
	{
		final ChebiTerm proton = (ChebiTerm)utils.getOntologyTerm( "CHEBI:24636" ); //$NON-NLS-1$
		Assert.assertTrue( proton.getFormula().equals( "H" ) ); //$NON-NLS-1$
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void getFormula() throws Exception
	{
		final ChebiTerm term = (ChebiTerm)utils.getOntologyTerm( "CHEBI:15633" ); //$NON-NLS-1$
		Assert.assertTrue( term.getFormula().equals( "C19H21N7O6" ) ); //$NON-NLS-1$
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void search() throws Exception
	{
		final int MAX_SEARCH_TERM_LENGTH = 1000;
		final StringBuffer buffer = new StringBuffer();

		for( int i = 0; i < MAX_SEARCH_TERM_LENGTH; i++ )
		{
			utils.search( buffer.toString() );
			buffer.append( "a" ); //$NON-NLS-1$
		}
	}

	/**
	 * 
	 * @param ontologyTerm
	 * @throws Exception
	 */
	public static void test( final ChebiTerm ontologyTerm ) throws Exception
	{
		final int ZERO = 0;
		final OntologyUtils ontologyUtils = OntologyUtils.getInstance();
		final OntologyTerm keggTerm = ontologyUtils.getOntologyTerm( Ontology.KEGG_COMPOUND, "C00469" ); //$NON-NLS-1$
		final OntologyTerm inchiTerm = ontologyUtils.getOntologyTerm( Ontology.INCHI, "InChI=1S/C2H6O/c1-2-3/h3H,2H2,1H3" ); //$NON-NLS-1$
		final Collection<String> synonyms = new HashSet<>( Arrays.asList( "ethanol", "[CH2Me(OH)]", "ETHANOL", "EtOH", "alcohol", "Methylcarbinol", "Ethyl alcohol", "[OEtH]", "C2H5OH", "Ethanol" ) ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$
		final String formula = "C2H6O"; //$NON-NLS-1$
		Assert.assertTrue( ontologyTerm.getXrefs().keySet().contains( keggTerm ) );
		Assert.assertTrue( ontologyTerm.getXrefs().keySet().contains( inchiTerm ) );
		Assert.assertTrue( ontologyTerm.getSynonyms().containsAll( synonyms ) );
		Assert.assertTrue( ontologyTerm.getFormula().equals( formula ) );
		Assert.assertTrue( ontologyTerm.getCharge() == ZERO );
		Assert.assertTrue( ontologyTerm.getSmiles().equals( "CCO" ) ); //$NON-NLS-1$
		Assert.assertTrue( ontologyTerm.getInchi().equals( "InChI=1S/C2H6O/c1-2-3/h3H,2H2,1H3" ) ); //$NON-NLS-1$
		Assert.assertTrue( ontologyTerm.toUri().equals( "http://identifiers.org/chebi/CHEBI:16236" ) ); //$NON-NLS-1$
	}

	/**
	 * 
	 * @param ontologyTerm
	 * @throws Exception
	 */
	public static void testFormula( final ChebiTerm ontologyTerm ) throws Exception
	{
		final String formula = "C2H6O"; //$NON-NLS-1$
		Assert.assertTrue( ontologyTerm.getFormula().equals( formula ) );
	}

	/**
	 * 
	 * @param ontologyTerm
	 * @throws Exception
	 */
	public static void testCharge( final ChebiTerm ontologyTerm ) throws Exception
	{
		final int ZERO = 0;
		Assert.assertTrue( ontologyTerm.getCharge() == ZERO );
	}
}