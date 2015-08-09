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
package org.mcisb.ontology.uniprot;

import java.util.*;
import org.junit.*;
import org.mcisb.ontology.*;
import org.mcisb.util.*;

/**
 * 
 * @author Neil Swainston
 */
public class UniProtUtilsTest
{
	/**
	 * 
	 */
	private UniProtUtils utils = new UniProtUtils();

	/**
	 * 
	 * @throws Exception
	 */
	public UniProtUtilsTest() throws Exception
	{
		// No implementation.
	}

	/**
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("static-method")
	@Test
	public void searchRest() throws Exception
	{
		final Map<String,List<String>> results = UniProtUtils.searchRest( "S000003870&taxonomy:559292" ); //$NON-NLS-1$
		Assert.assertTrue( results.get( "P03965" ).contains( "Carbamoyl-phosphate synthase arginine-specific large chain" ) ); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("static-method")
	@Test
	public void getNames() throws Exception
	{
		final Collection<String> names = UniProtUtils.getNames( "P38286" ); //$NON-NLS-1$
		Assert.assertTrue( names.contains( "Very-long-chain 3-oxoacyl-CoA reductase" ) ); //$NON-NLS-1$
		Assert.assertTrue( names.contains( "EC 1.1.1.330" ) ); //$NON-NLS-1$
		Assert.assertTrue( names.contains( "3-ketoacyl-CoA reductase" ) ); //$NON-NLS-1$
		Assert.assertTrue( names.contains( "3-ketoreductase" ) ); //$NON-NLS-1$
		Assert.assertTrue( names.contains( "KAR" ) ); //$NON-NLS-1$
		Assert.assertTrue( names.contains( "Microsomal beta-keto-reductase" ) ); //$NON-NLS-1$
	}

	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void getOntologyTerm() throws Exception
	{
		test( utils.getOntologyTerm( "A2PYL6" ) ); //$NON-NLS-1$
	}

	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void search() throws Exception
	{
		Assert.assertTrue( utils.search( "Hexokinase-2" ).contains( utils.getOntologyTerm( "A2PYL6" ) ) ); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void getUniProtId() throws Exception
	{
		Assert.assertEquals( ( (UniProtTerm)utils.getOntologyTerm( "A2PYL6" ) ).getUniProtId(), "HXK2_HORSE" ); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void getSequence() throws Exception
	{
		Assert.assertNotNull( ( (UniProtTerm)utils.getOntologyTerm( "A2PYL6" ) ).getSequence() ); //$NON-NLS-1$
	}

	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void getSequence2() throws Exception
	{
		Assert.assertNotNull( ( (UniProtTerm)CollectionUtils.getFirst( utils.searchExactMatchIdentifier( "ENO2_YEAST" ) ) ).getSequence() ); //$NON-NLS-1$
	}

	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void searchGenename() throws Exception
	{
		Assert.assertTrue( utils.searchGeneName( "HK2" ).contains( utils.getOntologyTerm( "A2PYL6" ) ) ); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void getOntologyTermForB() throws Exception
	{
		Assert.assertNotNull( utils.getOntologyTerm( "B3LIX8" ) ); //$NON-NLS-1$
	}

	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void getGoTerms() throws Exception
	{
		final Collection<OntologyTerm> goTerms = ( (UniProtTerm)utils.getOntologyTerm( "P00925" ) ).getGoTerms(); //$NON-NLS-1$
		Assert.assertTrue( goTerms.contains( OntologyUtils.getInstance().getOntologyTerm( Ontology.GO, "GO:0009898" ) ) ); //$NON-NLS-1$
	}

	/**
	 * 
	 * @param ontologyTerm
	 * @throws Exception
	 */
	@SuppressWarnings("static-method")
	public void test( final OntologyTerm ontologyTerm ) throws Exception
	{
		Assert.assertEquals( ontologyTerm.getName(), "Hexokinase-2" ); //$NON-NLS-1$
		Assert.assertTrue( ontologyTerm.getSynonyms().containsAll( Arrays.asList( "Hexokinase type II", "HK II" ) ) ); //$NON-NLS-1$ //$NON-NLS-2$

		if( ontologyTerm instanceof UniProtTerm )
		{
			Assert.assertTrue( ( (UniProtTerm)ontologyTerm ).getOrganisms().containsAll( Arrays.asList( "Horse" ) ) ); //$NON-NLS-1$
			Assert.assertTrue( ( (UniProtTerm)ontologyTerm ).getSequence().equals( "MIASHLLAYFFTELNHDQVQKVDQYLYHMRLSDETLLEISKRFRKEMEKGLAATTHPTASVKMLPTFVRSTPDGTEHGEFLALDLGGTNFRVLRVRVTDNGLQKVEMENQIYAIPEDIMQGSGTQLFDHIAGCLANFMDKLQIKDKKLPLGFTFSFPCIQTKLDESFLVSWTKGFKSRGVEGRDVVTLIRKAIQRRGDFDIDIVAMVNDTVATMMTCGYDDQNCEIGLIVGMGSNACYMEEMRYIDTVEGDEGRMCINMEWGAFGDDGTLDDIRTEFDQEIDMGSLNPGQQLFEKMISGMYMGELVRLILVKMAKEELLFRGKLSPELLTTGRFETKDVSEIEGEKDGIQKAREVLVRLGMDPTQEDCVATHRICQIVSTRSASLCAATLAAVLQRIKENKGEERLRSTIGVDGSVYKKHPHFAKRLQKTVRRLVPNCDIRFLCSEDGSGKGAAMVTAVAYRLAYQHRARLKTLEPLKLSREQLLEVKRRMKVEMERGLSKETHASAPVKMLPTYVCATPDGTEKGDFLALDLGGTNFRVLLVRVRNGKRRGVEMHNKIYSIPQDIMHGTGDELFDHIVQCIADFLEYMGMKGVSLPLGFTFSFPCQQNRLDESILLKWTKGFKASGCEGEDVVTLLKEAIHRREEFDLDVVAVVNDTVGTMMTCGYEDPHCEVGLIVGTGSNACYMEEMRNVELVEGEEGRMCVNTEWGAFGDNGCLDDFCTEFDVAVDELSLNPGKQRFEKMMSGMYLGEIVRNILIDFTKRGLLFRGRISERLKTRGIFETKFLSQIESDCLALQQVRAILQHLGLESTCDDSIIVKEVCTVVAQRAAQLCGAGMAAVVDKIRENRGLDTLKVTVGVDGTLYKLHPHFAKVMRETVKDLAPKCDVSFLESEDGSGKGAALITAVACRIREAGQR" ) ); //$NON-NLS-1$
		}
		else
		{
			throw new Exception();
		}
	}
}