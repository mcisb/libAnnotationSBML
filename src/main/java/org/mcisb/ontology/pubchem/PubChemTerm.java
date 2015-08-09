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

import java.io.*;
import java.net.*;
import org.mcisb.ontology.*;
import org.mcisb.util.*;
import org.sbml.jsbml.*;

/**
 * 
 * @author Neil Swainston
 */
public class PubChemTerm extends OntologyTerm
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private String smiles = null;

	/**
	 * 
	 */
	private String formula = null;

	/**
	 * 
	 */
	private int charge = NumberUtils.UNDEFINED;

	/**
	 * 
	 * @param ontologyName
	 * @param id
	 * @throws Exception
	 */
	public PubChemTerm( final String ontologyName, final String id ) throws Exception
	{
		super( OntologyFactory.getOntology( ontologyName ), id );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mcisb.ontology.OntologyTerm#getName()
	 */
	@Override
	public String getName() throws Exception
	{
		init();
		return name;
	}

	/**
	 * 
	 * @return smiles
	 * @throws Exception
	 */
	public String getSmiles() throws Exception
	{
		init();
		return smiles;
	}

	/**
	 * 
	 * @return formula
	 * @throws Exception
	 */
	public String getFormula() throws Exception
	{
		init();
		return formula;
	}

	/**
	 * 
	 * @return charge
	 * @throws Exception
	 */
	public int getCharge() throws Exception
	{
		init();
		return charge;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mcisb.ontology.OntologyTerm#doInitialise()
	 */
	@Override
	protected void doInitialise() throws Exception
	{
		final String SEPARATOR = ","; //$NON-NLS-1$
		final String QUOTES = "\""; //$NON-NLS-1$
		final String EMPTY_STRING = ""; //$NON-NLS-1$

		final int FORMULA = 0;
		final int CHARGE = 1;
		final int SMILES = 2;
		final int INCHI = 3;

		// See
		// https://pubchem.ncbi.nlm.nih.gov/pug_rest/PUG_REST_Tutorial.html#_Toc409516681
		final String ID = ontology.getName().equals( Ontology.PUBCHEM_COMPOUND ) ? "compound/CID/" : "substance/SID/"; //$NON-NLS-1$ //$NON-NLS-2$
		final URL url = new URL( "https://pubchem.ncbi.nlm.nih.gov/rest/pug/" + ID + id + "/property/MolecularFormula,Charge,IsomericSMILES,InChI/csv" ); //$NON-NLS-1$ //$NON-NLS-2$

		try ( final BufferedReader reader = new BufferedReader( new InputStreamReader( url.openStream() ) ) )
		{
			reader.readLine();

			final String[] tokens = reader.readLine().split( SEPARATOR );
			formula = tokens[ FORMULA ].replaceAll( QUOTES, EMPTY_STRING );
			charge = Integer.parseInt( tokens[ CHARGE ].replaceAll( QUOTES, EMPTY_STRING ) );
			smiles = tokens[ SMILES ].replaceAll( QUOTES, EMPTY_STRING );

			final String inchiId = tokens[ INCHI ].replaceAll( QUOTES, EMPTY_STRING );

			if( inchiId != null )
			{
				addXref( OntologyUtils.getInstance().getOntologyTerm( Ontology.INCHI, inchiId ), CVTerm.Type.BIOLOGICAL_QUALIFIER, CVTerm.Qualifier.BQB_IS );
			}
		}

		final URL synonymsUrl = new URL( "https://pubchem.ncbi.nlm.nih.gov/rest/pug/compound/" + ID + "/" + id + "/synonyms/txt" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		try ( final BufferedReader reader = new BufferedReader( new InputStreamReader( synonymsUrl.openStream() ) ) )
		{
			addSynonym( reader.readLine() );
		}
	}
}