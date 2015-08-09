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
import java.util.*;
import org.mcisb.ontology.*;
import org.mcisb.util.xml.*;

/**
 * 
 * @author Neil Swainston
 */
public class PubChemUtils extends DefaultOntologySource
{	
	/**
	 *
	 * @param ontologyName
	 * @throws Exception
	 */
	public PubChemUtils( final String ontologyName ) throws Exception
	{
		super( ontologyName );
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.mcisb.ontology.OntologySource#getOntologyTerm(java.lang.String)
	 */
	@Override
	public OntologyTerm getOntologyTermFromId( final String id ) throws Exception
	{
		return new PubChemTerm( ontology.getName(), id );
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.mcisb.ontology.OntologySource#search(java.lang.String)
	 */
	@Override
	public Collection<OntologyTerm> search( final String identifier ) throws Exception
	{
		final String ID = ontology.getName().equals( Ontology.PUBCHEM_COMPOUND ) ? "CID" : "SID"; //$NON-NLS-1$ //$NON-NLS-2$
		final Collection<OntologyTerm> ontologyTerms = new LinkedHashSet<>();
		final String database = ontology.getName().equals( Ontology.PUBCHEM_COMPOUND ) ? "compound" : "substance"; //$NON-NLS-1$ //$NON-NLS-2$
		final URL url = new URL( "https://pubchem.ncbi.nlm.nih.gov/rest/pug/" + database + "/name/" + identifier + "/cids/XML?name_type=word" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		
		try( final InputStream is = url.openStream() )
		{
			for( String id : XmlUtils.getElements( ID, is, true ) )
			{
				ontologyTerms.add( getOntologyTermFromId( id ) );
			}
		}
		
    	return ontologyTerms;
	}
}