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
package org.mcisb.sbml;

import java.io.*;
import java.net.*;
import java.nio.charset.*;
import java.util.*;
import org.junit.*;
import org.mcisb.ontology.*;
import org.mcisb.util.io.*;
import org.sbml.jsbml.*;

/**
 * 
 * @author Neil Swainston
 */
public class SbmlUtilsTest
{
	/**
	 * 
	 */
	private final String SBML = new String( StreamReader.read( new URL( "http://www.ebi.ac.uk/biomodels-main/download?mid=BIOMD0000000064" ).openStream() ), Charset.defaultCharset() ); //$NON-NLS-1$

	/**
	 * 
	 * @throws IOException
	 */
	public SbmlUtilsTest() throws IOException
	{
		// No implementation.
	}

	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void getSubModel() throws Exception
	{
		final SBMLDocument document = new SBMLReader().readSBMLFromString( SBML );
		final Model subModel = SbmlUtils.getSubDocument( document, "vPFK" ).getModel(); //$NON-NLS-1$

		final int NUM_MODIFIERS = 3;
		final int NUM_PARAMETERS = 1;
		Assert.assertTrue( subModel.getReaction( 0 ).getNumModifiers() == NUM_MODIFIERS );
		// This checks the kineticLaw is copied properly.
		Assert.assertTrue( subModel.getReaction( 0 ).getKineticLaw().getLocalParameterCount() == NUM_PARAMETERS );
	}

	/**
	 * 
	 * @throws Exception
	 */
	// @Test
	@SuppressWarnings("static-method")
	public void getCvTerms() throws Exception
	{
		final OntologySource ontologyUtils = OntologyUtils.getInstance();
		final Collection<OntologyTerm> ontologyTerms = new HashSet<>( Arrays.asList( ontologyUtils.getOntologyTerm( "http://www.ebi.ac.uk/chebi/#CHEBI:16236" ), ontologyUtils.getOntologyTerm( "http://www.pubchem.gov/substance/#3752" ) ) ); //$NON-NLS-1$ //$NON-NLS-2$

		final Species sbase = new Species();
		sbase.setNamespace( SbmlUtils.getDefaultSBMLNamespace() );
		sbase.setMetaId( "metaid" ); //$NON-NLS-1$

		SbmlUtils.addOntologyTerms( sbase, ontologyTerms, CVTerm.Type.BIOLOGICAL_QUALIFIER, CVTerm.Qualifier.BQB_IS );

		Assert.assertEquals( SbmlUtils.getOntologyTerms( sbase ).keySet(), ontologyTerms );
	}

	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void getSetNotes() throws Exception
	{
		final SBMLDocument document = new SBMLReader().readSBMLFromString( SBML );
		final SBase sbase = document.getModel().getSpecies( "F6P" ); //$NON-NLS-1$
		final Map<String,Object> notes1 = SbmlUtils.getNotes( sbase );
		Assert.assertTrue( notes1.size() == 0 );

		notes1.put( "FORMULA", "CH4" ); //$NON-NLS-1$ //$NON-NLS-2$
		notes1.put( "CHARGE", "0" ); //$NON-NLS-1$ //$NON-NLS-2$
		notes1.put( "Random free text", null ); //$NON-NLS-1$
		SbmlUtils.setNotes( sbase, notes1 );

		final Map<String,Object> notes2 = SbmlUtils.getNotes( sbase );
		Assert.assertTrue( notes2.size() == 3 );
		Assert.assertTrue( notes2.get( "FORMULA" ).equals( "CH4" ) ); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings({ "static-method" })
	@Test
	public void getCharge() throws Exception
	{
		final SBMLDocument document = new SBMLDocument();
		final Model model = document.createModel();
		final Species species = model.createSpecies();

		species.setName( "thiosulfate(1-)" ); //$NON-NLS-1$
		Assert.assertTrue( SbmlUtils.getCharge( model, species ) == -1 );

		species.setName( "thiosulfate(18+)" ); //$NON-NLS-1$
		Assert.assertTrue( SbmlUtils.getCharge( model, species ) == 18 );

		species.setName( "thiosulfate(1568-)" ); //$NON-NLS-1$
		Assert.assertTrue( SbmlUtils.getCharge( model, species ) == -1568 );
	}
}