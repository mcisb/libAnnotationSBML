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
package org.mcisb.ontology.taxonomy;

import java.io.*;
import java.net.*;
import java.nio.charset.*;
import org.mcisb.ontology.*;
import org.mcisb.util.*;
import org.mcisb.util.io.*;

/**
 *
 * @author Neil Swainston
 */
public class TaxonomyTerm extends OntologyTerm
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *
	 * @param id
	 * @throws Exception
	 */
	public TaxonomyTerm( final String id ) throws Exception
	{
		super( OntologyFactory.getOntology( Ontology.TAXONOMY ), id );
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.mcisb.ontology.OntologyTerm#doInitialise()
	 */
	@Override
	protected void doInitialise()
	{
		final String NAME = "(?<=<title>Taxonomy browser \\().*(?=\\))"; //$NON-NLS-1$
		
		try( final InputStream is = new URL( getLink() ).openStream() )
		{
			final String html = new String( StreamReader.read( is ), Charset.defaultCharset() );
			name = CollectionUtils.getFirst( RegularExpressionUtils.getMatches( html, NAME ) );
		}
		catch( IOException e )
		{
			e.printStackTrace();
		}
	}
}