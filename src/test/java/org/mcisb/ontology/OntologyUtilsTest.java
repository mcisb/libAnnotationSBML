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

import java.util.*;
import org.junit.*;
import org.mcisb.ontology.OntologyUtils.MatchCriteria;
import org.mcisb.ontology.chebi.*;
import org.sbml.jsbml.*;

/**
 * 
 *
 * @author Neil Swainston
 */
public class OntologyUtilsTest
{
	/**
	 * 
	 */
	private final OntologyUtils utils = OntologyUtils.getInstance();
	
	/**
	 *
	 * @throws Exception
	 */
	public OntologyUtilsTest() throws Exception
	{
		// No implementation.
	}
	
	/**
	 *
	 * @throws Exception
	 */
	@Test
	public void getXrefs() throws Exception
	{
		final OntologyTerm chebiTerm = utils.getOntologyTerm( "http://www.ebi.ac.uk/chebi/#CHEBI:16236" ); //$NON-NLS-1$
		final OntologyTerm pubChemTerm = utils.getOntologyTerm( "http://www.pubchem.gov/substance/#3752" ); //$NON-NLS-1$
		final OntologyTerm keggTerm = utils.getOntologyTerm( "http://www.genome.jp/kegg/compound/#C00469" ); //$NON-NLS-1$
		final OntologyTerm inchiTerm = utils.getOntologyTerm( "http://identifiers.org/inchi/InChI=1S/C2H6O/c1-2-3/h3H,2H2,1H3" ); //$NON-NLS-1$
		
		final Map<OntologyTerm,Object[]> xrefs = new HashMap<>();
		xrefs.put( chebiTerm, new Object[] { CVTerm.Type.BIOLOGICAL_QUALIFIER, CVTerm.Qualifier.BQB_IS } );
		OntologyUtils.getInstance().getXrefs( xrefs );
		Assert.assertTrue( xrefs.keySet().contains( pubChemTerm ) );
		Assert.assertTrue( xrefs.keySet().contains( keggTerm ) );
		Assert.assertTrue( xrefs.keySet().contains( inchiTerm ) );
	}
	
	/**
	 *
	 * @throws Exception
	 */
	@Test
	public void getXref() throws Exception
	{
		final OntologyTerm chebiTerm = utils.getOntologyTerm( "http://www.ebi.ac.uk/chebi/#CHEBI:16236" ); //$NON-NLS-1$
		// final OntologyTerm pubChemTerm = utils.getOntologyTerm( "http://www.pubchem.gov/substance/#3752" ); //$NON-NLS-1$
		final OntologyTerm keggTerm = utils.getOntologyTerm( "http://www.genome.jp/kegg/compound/#C00469" ); //$NON-NLS-1$
		
		/*
		final Collection<OntologyTerm> keggXrefs = new ArrayList<OntologyTerm>();
		keggXrefs.add( pubChemTerm );
		Assert.assertTrue( OntologyUtils.getXrefs( pubChemXrefs, Ontology.CHEBI ).keySet().contains( chebiTerm ) );
		*/
		
		final Collection<OntologyTerm> keggXrefs = new ArrayList<>();
		keggXrefs.add( keggTerm );
		Assert.assertTrue( OntologyUtils.getInstance().getXrefs( keggXrefs, Ontology.CHEBI ).contains( chebiTerm ) );
	}
	
	/**
	 *
	 * @throws Exception
	 */
	@Test
	public void areConjugates() throws Exception
	{
		final ChebiTerm ammonia = (ChebiTerm)utils.getOntologyTerm( "http://identifiers.org/obo.chebi/CHEBI:16134" ); // Ammonia //$NON-NLS-1$
		final ChebiTerm ammonium = (ChebiTerm)utils.getOntologyTerm( "http://www.ebi.ac.uk/chebi/#CHEBI:28938" ); // Ammonium //$NON-NLS-1$
		final ChebiTerm serinium = (ChebiTerm)utils.getOntologyTerm( "urn:miriam:obo.chebi:CHEBI%3A32837" ); // L-serinium //$NON-NLS-1$
		final ChebiTerm serinate = (ChebiTerm)utils.getOntologyTerm( "http://www.ebi.ac.uk/chebi/#CHEBI:32836" ); // L-serinate //$NON-NLS-1$
		final ChebiTerm alphaLglucose = (ChebiTerm)utils.getOntologyTerm( "http://www.ebi.ac.uk/chebi/#CHEBI:37630" ); // alpha-L-glucose //$NON-NLS-1$
		final ChebiTerm betaLglucose = (ChebiTerm)utils.getOntologyTerm( "urn:miriam:obo.chebi:CHEBI%3A37631" ); // beta-L-glucose //$NON-NLS-1$
		final ChebiTerm water = (ChebiTerm)utils.getOntologyTerm( "http://www.ebi.ac.uk/chebi/#CHEBI:15377" ); // water //$NON-NLS-1$
		final ChebiTerm atp = (ChebiTerm)utils.getOntologyTerm( "http://www.ebi.ac.uk/chebi/#CHEBI:15422" ); // ATP //$NON-NLS-1$
		final ChebiTerm atp3minus = (ChebiTerm)utils.getOntologyTerm( "http://www.ebi.ac.uk/chebi/#CHEBI:57299" ); // ATP 3- //$NON-NLS-1$
		final ChebiTerm atp4minus = (ChebiTerm)utils.getOntologyTerm( "http://www.ebi.ac.uk/chebi/#CHEBI:30616" ); // ATP 4- //$NON-NLS-1$
		Assert.assertTrue( utils.areConjugates( atp, atp3minus ) );
		Assert.assertTrue( utils.areConjugates( atp, atp4minus ) );
		Assert.assertTrue( utils.areConjugates( atp3minus, atp ) );
		Assert.assertTrue( utils.areConjugates( atp4minus, atp ) );
		Assert.assertTrue( utils.areConjugates( atp3minus, atp4minus ) );
		Assert.assertTrue( utils.areConjugates( atp4minus, atp3minus ) );
		
		Assert.assertTrue( utils.areConjugates( ammonia, ammonium ) );
		Assert.assertTrue( utils.areConjugates( ammonium, ammonia ) );
		Assert.assertTrue( utils.areConjugates( serinium, serinate ) );
		Assert.assertTrue( utils.areConjugates( serinate, serinium ) );
		Assert.assertFalse( utils.areConjugates( alphaLglucose, betaLglucose ) );
		Assert.assertFalse( utils.areConjugates( betaLglucose, alphaLglucose ) );
		Assert.assertFalse( utils.areConjugates( water, alphaLglucose ) );
		Assert.assertFalse( utils.areConjugates( alphaLglucose, water ) );
	}
	
	/**
	 *
	 * @throws Exception
	 */
	@Test
	public void getCommonAncestor() throws Exception
	{
		final ChebiTerm melibiitol = (ChebiTerm)utils.getOntologyTerm( "http://www.ebi.ac.uk/chebi/#CHEBI:27527" ); // melibiitol //$NON-NLS-1$
		final ChebiTerm glycerol = (ChebiTerm)utils.getOntologyTerm( "http://www.ebi.ac.uk/chebi/#CHEBI:17754" ); // glycerol //$NON-NLS-1$
		final ChebiTerm alphaLglucose = (ChebiTerm)utils.getOntologyTerm( "http://www.ebi.ac.uk/chebi/#CHEBI:37630" ); // alpha-L-glucose //$NON-NLS-1$
		final ChebiTerm betaLglucose = (ChebiTerm)utils.getOntologyTerm( "http://www.ebi.ac.uk/chebi/#CHEBI:37631" ); // beta-L-glucose //$NON-NLS-1$
		final ChebiTerm lGlucopyranose = (ChebiTerm)utils.getOntologyTerm( "http://www.ebi.ac.uk/chebi/#CHEBI:37627" ); // L-glucopyranose //$NON-NLS-1$
		// final ChebiTerm aldehydoDglucose = (ChebiTerm)utils.getOntologyTerm( "http://www.ebi.ac.uk/chebi/#CHEBI:42758" ); // aldehydo-D-glucose //$NON-NLS-1$
		// final ChebiTerm glucose = (ChebiTerm)utils.getOntologyTerm( "http://www.ebi.ac.uk/chebi/#CHEBI:17234" ); // glucose //$NON-NLS-1$
		final ChebiTerm betaDglucose = (ChebiTerm)utils.getOntologyTerm( "http://www.ebi.ac.uk/chebi/#CHEBI:15903" ); // beta-D-glucose //$NON-NLS-1$
		final ChebiTerm glucopyranose = (ChebiTerm)utils.getOntologyTerm( "http://www.ebi.ac.uk/chebi/#CHEBI:37661" ); // glucopyranose //$NON-NLS-1$
		final ChebiTerm lipid = (ChebiTerm)utils.getOntologyTerm( "http://www.ebi.ac.uk/chebi/#CHEBI:25054" ); // lipid //$NON-NLS-1$
		final ChebiTerm cholesterol = (ChebiTerm)utils.getOntologyTerm( "http://www.ebi.ac.uk/chebi/#CHEBI:16113" ); // cholesterol //$NON-NLS-1$
		final ChebiTerm atp = (ChebiTerm)utils.getOntologyTerm( "http://www.ebi.ac.uk/chebi/#CHEBI:15422" ); // ATP //$NON-NLS-1$
		final ChebiTerm atp3minus = (ChebiTerm)utils.getOntologyTerm( "http://www.ebi.ac.uk/chebi/#CHEBI:57299" ); // ATP 3- //$NON-NLS-1$
		final ChebiTerm atp4minus = (ChebiTerm)utils.getOntologyTerm( "http://www.ebi.ac.uk/chebi/#CHEBI:30616" ); // ATP 4- //$NON-NLS-1$
		
		Assert.assertTrue( utils.getCommonAncestor( atp, atp3minus ) == null );
		Assert.assertTrue( utils.getCommonAncestor( atp, atp4minus ) == null );
		Assert.assertTrue( utils.getCommonAncestor( atp3minus, atp ) == null );
		Assert.assertTrue( utils.getCommonAncestor( atp4minus, atp ) == null );
		Assert.assertTrue( utils.getCommonAncestor( atp3minus, atp4minus ) == null );
		Assert.assertTrue( utils.getCommonAncestor( atp4minus, atp3minus ) == null );
		
		Assert.assertTrue( utils.getCommonAncestor( melibiitol, glycerol ) == null );
		Assert.assertTrue( utils.getCommonAncestor( glycerol, melibiitol ) == null );
		Assert.assertTrue( utils.getCommonAncestor( alphaLglucose, betaLglucose ).equals( lGlucopyranose ) );
		Assert.assertTrue( utils.getCommonAncestor( betaLglucose, alphaLglucose ).equals( lGlucopyranose ) );
		Assert.assertTrue( utils.getCommonAncestor( alphaLglucose, lGlucopyranose ).equals( lGlucopyranose ) );
		Assert.assertTrue( utils.getCommonAncestor( betaLglucose, lGlucopyranose ).equals( lGlucopyranose ) );
		// Assert.assertTrue( utils.getCommonAncestor( alphaLglucose, aldehydoDglucose ).equals( glucose ) );
		// Assert.assertTrue( utils.getCommonAncestor( aldehydoDglucose, alphaLglucose ).equals( glucose ) );
		Assert.assertTrue( utils.getCommonAncestor( betaDglucose, alphaLglucose ).equals( glucopyranose ) );
		Assert.assertTrue( utils.getCommonAncestor( alphaLglucose, betaDglucose ).equals( glucopyranose ) );
		Assert.assertTrue( utils.getCommonAncestor( lipid, cholesterol, false ).equals( lipid ) );
		Assert.assertTrue( utils.getCommonAncestor( cholesterol, lipid, false ).equals( lipid ) );
	}
	
	/**
	 *
	 * @throws Exception
	 */
	@Test
	public void areEquivalent() throws Exception
	{
		final ChebiTerm atp = (ChebiTerm)utils.getOntologyTerm( "http://www.ebi.ac.uk/chebi/#CHEBI:15422" ); // ATP //$NON-NLS-1$
		final ChebiTerm atp3minus = (ChebiTerm)utils.getOntologyTerm( "http://www.ebi.ac.uk/chebi/#CHEBI:57299" ); // ATP 3- //$NON-NLS-1$
		final ChebiTerm atp4minus = (ChebiTerm)utils.getOntologyTerm( "http://www.ebi.ac.uk/chebi/#CHEBI:30616" ); // ATP 4- //$NON-NLS-1$
		Assert.assertTrue( utils.areEquivalent( atp, atp3minus, MatchCriteria.CONJUGATES ) );
		Assert.assertTrue( utils.areEquivalent( atp, atp4minus, MatchCriteria.CONJUGATES ) );
		Assert.assertTrue( utils.areEquivalent( atp3minus, atp, MatchCriteria.CONJUGATES ) );
		Assert.assertTrue( utils.areEquivalent( atp4minus, atp, MatchCriteria.CONJUGATES ) );
		Assert.assertTrue( utils.areEquivalent( atp3minus, atp4minus, MatchCriteria.CONJUGATES ) );
		Assert.assertTrue( utils.areEquivalent( atp4minus, atp3minus, MatchCriteria.CONJUGATES ) );
		Assert.assertTrue( utils.areEquivalent( atp, atp3minus, MatchCriteria.ANY ) );
		Assert.assertTrue( utils.areEquivalent( atp, atp4minus, MatchCriteria.ANY ) );
		Assert.assertTrue( utils.areEquivalent( atp3minus, atp, MatchCriteria.ANY ) );
		Assert.assertTrue( utils.areEquivalent( atp4minus, atp, MatchCriteria.ANY ) );
		Assert.assertTrue( utils.areEquivalent( atp3minus, atp4minus, MatchCriteria.ANY ) );
		Assert.assertTrue( utils.areEquivalent( atp4minus, atp3minus, MatchCriteria.ANY ) );
		Assert.assertFalse( utils.areEquivalent( atp, atp3minus, MatchCriteria.COMMON_ANCESTOR ) );
		Assert.assertFalse( utils.areEquivalent( atp, atp4minus, MatchCriteria.COMMON_ANCESTOR ) );
		Assert.assertFalse( utils.areEquivalent( atp3minus, atp, MatchCriteria.COMMON_ANCESTOR ) );
		Assert.assertFalse( utils.areEquivalent( atp4minus, atp, MatchCriteria.COMMON_ANCESTOR ) );
		Assert.assertFalse( utils.areEquivalent( atp3minus, atp4minus, MatchCriteria.COMMON_ANCESTOR ) );
		Assert.assertFalse( utils.areEquivalent( atp4minus, atp3minus, MatchCriteria.COMMON_ANCESTOR ) );
		
		final ChebiTerm ammonia = (ChebiTerm)utils.getOntologyTerm( "http://www.ebi.ac.uk/chebi/#CHEBI:16134" ); // Ammonia //$NON-NLS-1$
		final ChebiTerm ammonium = (ChebiTerm)utils.getOntologyTerm( "http://www.ebi.ac.uk/chebi/#CHEBI:28938" ); // Ammonium //$NON-NLS-1$
		final ChebiTerm serinium = (ChebiTerm)utils.getOntologyTerm( "http://www.ebi.ac.uk/chebi/#CHEBI:32837" ); // L-serinium //$NON-NLS-1$
		final ChebiTerm serinate = (ChebiTerm)utils.getOntologyTerm( "http://www.ebi.ac.uk/chebi/#CHEBI:32836" ); // L-serinate //$NON-NLS-1$
		final ChebiTerm alphaLglucose = (ChebiTerm)utils.getOntologyTerm( "http://www.ebi.ac.uk/chebi/#CHEBI:37630" ); // alpha-L-glucose //$NON-NLS-1$
		final ChebiTerm betaLglucose = (ChebiTerm)utils.getOntologyTerm( "http://www.ebi.ac.uk/chebi/#CHEBI:37631" ); // beta-L-glucose //$NON-NLS-1$
		final ChebiTerm melibiitol = (ChebiTerm)utils.getOntologyTerm( "http://www.ebi.ac.uk/chebi/#CHEBI:27527" ); // melibiitol //$NON-NLS-1$
		final ChebiTerm glycerol = (ChebiTerm)utils.getOntologyTerm( "http://www.ebi.ac.uk/chebi/#CHEBI:17754" ); // glycerol //$NON-NLS-1$
		final ChebiTerm lGlucopyranose = (ChebiTerm)utils.getOntologyTerm( "http://www.ebi.ac.uk/chebi/#CHEBI:37627" ); // L-glucopyranose //$NON-NLS-1$
		final ChebiTerm aldehydoDglucose = (ChebiTerm)utils.getOntologyTerm( "http://www.ebi.ac.uk/chebi/#CHEBI:42758" ); // aldehydo-D-glucose //$NON-NLS-1$
		final ChebiTerm betaDglucose = (ChebiTerm)utils.getOntologyTerm( "http://www.ebi.ac.uk/chebi/#CHEBI:15903" ); // beta-D-glucose //$NON-NLS-1$
		final OntologyTerm term1 = utils.getOntologyTerm( "http://www.genome.jp/kegg/genes/#sce:YLR262C-A" ); //$NON-NLS-1$
		final OntologyTerm term2 = utils.getOntologyTerm( "http://www.genome.jp/kegg/genes/#sce:YLR262C" ); //$NON-NLS-1$
		
		
		Assert.assertFalse( utils.areEquivalent( ammonia, atp, MatchCriteria.CONJUGATES ) );
		Assert.assertFalse( utils.areEquivalent( ammonia, atp, MatchCriteria.COMMON_ANCESTOR ) );
		Assert.assertFalse( utils.areEquivalent( ammonia, atp, MatchCriteria.ANY ) );
		Assert.assertFalse( utils.areEquivalent( atp, ammonia, MatchCriteria.CONJUGATES ) );
		Assert.assertFalse( utils.areEquivalent( atp, ammonia, MatchCriteria.COMMON_ANCESTOR ) );
		Assert.assertFalse( utils.areEquivalent( atp, ammonia, MatchCriteria.ANY ) );
		
		Assert.assertTrue( utils.areEquivalent( ammonia, ammonium, MatchCriteria.CONJUGATES ) );
		Assert.assertTrue( utils.areEquivalent( ammonium, ammonia, MatchCriteria.ANY ) );
		Assert.assertTrue( utils.areEquivalent( serinium, serinate, MatchCriteria.ANY ) );
		Assert.assertTrue( utils.areEquivalent( serinate, serinium, MatchCriteria.CONJUGATES ) );
		Assert.assertFalse( utils.areEquivalent( melibiitol, glycerol, MatchCriteria.CONJUGATES ) );
		Assert.assertFalse( utils.areEquivalent( glycerol, melibiitol, MatchCriteria.CONJUGATES ) );
		Assert.assertFalse( utils.areEquivalent( alphaLglucose, betaLglucose, MatchCriteria.CONJUGATES ) );
		Assert.assertFalse( utils.areEquivalent( betaLglucose, alphaLglucose, MatchCriteria.CONJUGATES ) );
		Assert.assertFalse( utils.areEquivalent( alphaLglucose, lGlucopyranose, MatchCriteria.CONJUGATES ) );
		Assert.assertFalse( utils.areEquivalent( betaLglucose, lGlucopyranose, MatchCriteria.CONJUGATES ) );
		Assert.assertFalse( utils.areEquivalent( alphaLglucose, aldehydoDglucose, MatchCriteria.CONJUGATES ) );
		Assert.assertFalse( utils.areEquivalent( aldehydoDglucose, alphaLglucose, MatchCriteria.CONJUGATES ) );
		Assert.assertFalse( utils.areEquivalent( betaDglucose, alphaLglucose, MatchCriteria.CONJUGATES ) );
		Assert.assertFalse( utils.areEquivalent( alphaLglucose, betaDglucose, MatchCriteria.CONJUGATES ) );
		Assert.assertFalse( utils.areEquivalent( melibiitol, glycerol, MatchCriteria.ANY ) );
		Assert.assertFalse( utils.areEquivalent( glycerol, melibiitol, MatchCriteria.ANY ) );
		Assert.assertTrue( utils.areEquivalent( alphaLglucose, betaLglucose, MatchCriteria.ANY ) );
		Assert.assertTrue( utils.areEquivalent( betaLglucose, alphaLglucose, MatchCriteria.ANY ) );
		Assert.assertTrue( utils.areEquivalent( alphaLglucose, lGlucopyranose, MatchCriteria.ANY ) );
		Assert.assertTrue( utils.areEquivalent( betaLglucose, lGlucopyranose, MatchCriteria.ANY ) );
		// Assert.assertTrue( utils.areEquivalent( alphaLglucose, aldehydoDglucose, MatchCriteria.ANY ) );
		// Assert.assertTrue( utils.areEquivalent( aldehydoDglucose, alphaLglucose, MatchCriteria.ANY ) );
		Assert.assertTrue( utils.areEquivalent( betaDglucose, alphaLglucose, MatchCriteria.ANY ) );
		Assert.assertTrue( utils.areEquivalent( alphaLglucose, betaDglucose, MatchCriteria.ANY ) );
		Assert.assertFalse( utils.areEquivalent( melibiitol, glycerol, MatchCriteria.COMMON_ANCESTOR ) );
		Assert.assertFalse( utils.areEquivalent( glycerol, melibiitol, MatchCriteria.COMMON_ANCESTOR ) );
		Assert.assertTrue( utils.areEquivalent( alphaLglucose, betaLglucose, MatchCriteria.COMMON_ANCESTOR ) );
		Assert.assertTrue( utils.areEquivalent( betaLglucose, alphaLglucose, MatchCriteria.COMMON_ANCESTOR ) );
		Assert.assertTrue( utils.areEquivalent( alphaLglucose, lGlucopyranose, MatchCriteria.COMMON_ANCESTOR ) );
		Assert.assertTrue( utils.areEquivalent( betaLglucose, lGlucopyranose, MatchCriteria.COMMON_ANCESTOR ) );
		// Assert.assertTrue( utils.areEquivalent( alphaLglucose, aldehydoDglucose, MatchCriteria.COMMON_ANCESTOR ) );
		// Assert.assertTrue( utils.areEquivalent( aldehydoDglucose, alphaLglucose, MatchCriteria.COMMON_ANCESTOR ) );
		Assert.assertTrue( utils.areEquivalent( betaDglucose, alphaLglucose, MatchCriteria.COMMON_ANCESTOR ) );
		Assert.assertTrue( utils.areEquivalent( alphaLglucose, betaDglucose, MatchCriteria.COMMON_ANCESTOR ) );
		Assert.assertTrue( utils.areEquivalent( term1, term1, MatchCriteria.CONJUGATES ) );
		Assert.assertTrue( utils.areEquivalent( term2, term2, MatchCriteria.CONJUGATES ) );
		Assert.assertTrue( utils.areEquivalent( term1, term1, MatchCriteria.COMMON_ANCESTOR ) );
		Assert.assertTrue( utils.areEquivalent( term2, term2, MatchCriteria.COMMON_ANCESTOR ) );
		Assert.assertTrue( utils.areEquivalent( term1, term1, MatchCriteria.ANY ) );
		Assert.assertTrue( utils.areEquivalent( term2, term2, MatchCriteria.ANY ) );
		Assert.assertFalse( utils.areEquivalent( term1, term2, MatchCriteria.CONJUGATES ) );
		Assert.assertFalse( utils.areEquivalent( term1, term2, MatchCriteria.ANY ) );
	}
	
	/**
	 *
	 * @throws Exception
	 */
	@Test
	public void areTautomers() throws Exception
	{
		final ChebiTerm atp = (ChebiTerm)utils.getOntologyTerm( "http://www.ebi.ac.uk/chebi/#CHEBI:15422" ); // ATP //$NON-NLS-1$
		final ChebiTerm atp3minus = (ChebiTerm)utils.getOntologyTerm( "http://www.ebi.ac.uk/chebi/#CHEBI:57299" ); // ATP 3- //$NON-NLS-1$
		final ChebiTerm atp4minus = (ChebiTerm)utils.getOntologyTerm( "http://www.ebi.ac.uk/chebi/#CHEBI:30616" ); // ATP 4- //$NON-NLS-1$
		final ChebiTerm histidine = (ChebiTerm)utils.getOntologyTerm( "http://www.ebi.ac.uk/chebi/#CHEBI:15971" ); //$NON-NLS-1$
		final ChebiTerm histidineZwitterIon = (ChebiTerm)utils.getOntologyTerm( "http://www.ebi.ac.uk/chebi/#CHEBI:57595" ); // ATP 4- //$NON-NLS-1$
		final ChebiTerm valine = (ChebiTerm)utils.getOntologyTerm( "http://www.ebi.ac.uk/chebi/#CHEBI:16414" ); //$NON-NLS-1$
		final ChebiTerm valineZwitterIon = (ChebiTerm)utils.getOntologyTerm( "http://www.ebi.ac.uk/chebi/#CHEBI:57762" ); // ATP 4- //$NON-NLS-1$
		
		Assert.assertFalse( OntologyUtils.areTautomers( atp, atp3minus ) );
		Assert.assertFalse( OntologyUtils.areTautomers( atp, atp4minus ) );
		Assert.assertFalse( OntologyUtils.areTautomers( atp3minus, atp ) );
		Assert.assertFalse( OntologyUtils.areTautomers( atp4minus, atp ) );
		Assert.assertFalse( OntologyUtils.areTautomers( atp3minus, histidine ) );
		Assert.assertFalse( OntologyUtils.areTautomers( atp4minus, histidineZwitterIon ) );
		Assert.assertTrue( OntologyUtils.areTautomers( histidineZwitterIon, histidine ) );
		Assert.assertTrue( OntologyUtils.areTautomers( histidine, histidineZwitterIon ) );
		
		Assert.assertFalse( utils.areEquivalent( atp, atp3minus, MatchCriteria.TAUTOMERS ) );
		Assert.assertFalse( utils.areEquivalent( atp, atp4minus, MatchCriteria.TAUTOMERS ) );
		Assert.assertFalse( utils.areEquivalent( atp3minus, atp, MatchCriteria.TAUTOMERS ) );
		Assert.assertFalse( utils.areEquivalent( atp4minus, atp, MatchCriteria.TAUTOMERS ) );
		Assert.assertFalse( utils.areEquivalent( atp3minus, histidine, MatchCriteria.TAUTOMERS ) );
		Assert.assertFalse( utils.areEquivalent( atp4minus, histidineZwitterIon, MatchCriteria.TAUTOMERS ) );
		Assert.assertTrue( utils.areEquivalent( histidineZwitterIon, histidine, MatchCriteria.TAUTOMERS ) );
		Assert.assertTrue( utils.areEquivalent( histidine, histidineZwitterIon, MatchCriteria.TAUTOMERS ) );
		
		Assert.assertTrue( utils.areEquivalent( atp, atp3minus, MatchCriteria.CONJUGATES ) );
		Assert.assertTrue( utils.areEquivalent( atp, atp4minus, MatchCriteria.CONJUGATES ) );
		Assert.assertTrue( utils.areEquivalent( atp3minus, atp, MatchCriteria.CONJUGATES ) );
		Assert.assertTrue( utils.areEquivalent( atp4minus, atp, MatchCriteria.CONJUGATES ) );
		Assert.assertFalse( utils.areEquivalent( atp3minus, histidine, MatchCriteria.CONJUGATES ) );
		Assert.assertFalse( utils.areEquivalent( atp4minus, histidineZwitterIon, MatchCriteria.CONJUGATES ) );
		Assert.assertFalse( utils.areEquivalent( histidineZwitterIon, histidine, MatchCriteria.CONJUGATES ) );
		Assert.assertFalse( utils.areEquivalent( histidine, histidineZwitterIon, MatchCriteria.CONJUGATES ) );
		
		Assert.assertTrue( utils.areEquivalent( atp, atp3minus, MatchCriteria.ANY ) );
		Assert.assertTrue( utils.areEquivalent( atp, atp4minus, MatchCriteria.ANY ) );
		Assert.assertTrue( utils.areEquivalent( atp3minus, atp, MatchCriteria.ANY ) );
		Assert.assertTrue( utils.areEquivalent( atp4minus, atp, MatchCriteria.ANY ) );
		Assert.assertFalse( utils.areEquivalent( atp3minus, histidine, MatchCriteria.ANY ) );
		Assert.assertFalse( utils.areEquivalent( atp4minus, histidineZwitterIon, MatchCriteria.ANY ) );
		Assert.assertTrue( utils.areEquivalent( histidineZwitterIon, histidine, MatchCriteria.ANY ) );
		Assert.assertTrue( utils.areEquivalent( histidine, histidineZwitterIon, MatchCriteria.ANY ) );
		
		Assert.assertTrue( utils.areEquivalent( atp, atp3minus, Arrays.asList( new MatchCriteria[] { MatchCriteria.CONJUGATES, MatchCriteria.TAUTOMERS } ) ) );
		Assert.assertTrue( utils.areEquivalent( atp, atp4minus, Arrays.asList( new MatchCriteria[] { MatchCriteria.CONJUGATES, MatchCriteria.TAUTOMERS } ) ) );
		Assert.assertTrue( utils.areEquivalent( atp3minus, atp, Arrays.asList( new MatchCriteria[] { MatchCriteria.CONJUGATES, MatchCriteria.TAUTOMERS } ) ) );
		Assert.assertTrue( utils.areEquivalent( atp4minus, atp, Arrays.asList( new MatchCriteria[] { MatchCriteria.CONJUGATES, MatchCriteria.TAUTOMERS } ) ) );
		Assert.assertFalse( utils.areEquivalent( atp3minus, histidine, Arrays.asList( new MatchCriteria[] { MatchCriteria.CONJUGATES, MatchCriteria.TAUTOMERS } ) ) );
		Assert.assertFalse( utils.areEquivalent( atp4minus, histidineZwitterIon, Arrays.asList( new MatchCriteria[] { MatchCriteria.CONJUGATES, MatchCriteria.TAUTOMERS } ) ) );
		Assert.assertTrue( utils.areEquivalent( histidineZwitterIon, histidine, Arrays.asList( new MatchCriteria[] { MatchCriteria.CONJUGATES, MatchCriteria.TAUTOMERS } ) ) );
		Assert.assertTrue( utils.areEquivalent( histidine, histidineZwitterIon, Arrays.asList( new MatchCriteria[] { MatchCriteria.CONJUGATES, MatchCriteria.TAUTOMERS } ) ) );
		Assert.assertTrue( utils.areEquivalent( valineZwitterIon, valine, Arrays.asList( new MatchCriteria[] { MatchCriteria.CONJUGATES, MatchCriteria.TAUTOMERS } ) ) );
		Assert.assertTrue( utils.areEquivalent( valine, valineZwitterIon, Arrays.asList( new MatchCriteria[] { MatchCriteria.CONJUGATES, MatchCriteria.TAUTOMERS } ) ) );
	}
}