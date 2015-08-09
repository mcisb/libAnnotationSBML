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
package org.mcisb.ontology.gene;

import java.io.*;
import java.net.*;
import javax.xml.stream.*;
import javax.xml.stream.events.*;
import org.mcisb.ontology.*;

/**
 * 
 * @author Neil Swainston
 */
public class GeneTerm extends OntologyTerm
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param id
	 * @throws Exception
	 */
	public GeneTerm( final String id ) throws Exception
	{
		super( OntologyFactory.getOntology( Ontology.NCBI_GENE ), id );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mcisb.ontology.OntologyTerm#doInitialise()
	 */
	@Override
	protected void doInitialise() throws IOException, XMLStreamException, FactoryConfigurationError
	{
		final URL url = new URL( "http://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=gene&id=" + id + "&retmode=xml" ); //$NON-NLS-1$ //$NON-NLS-2$

		try ( final InputStream is = url.openStream() )
		{
			parse( is );
			is.close();
		}
	}

	/**
	 * @param is
	 * @throws FactoryConfigurationError
	 * @throws XMLStreamException
	 */
	private void parse( final InputStream is ) throws XMLStreamException, FactoryConfigurationError
	{
		final String EMPTY_STRING = ""; //$NON-NLS-1$
		final String SPACE = " "; //$NON-NLS-1$

		final XMLEventReader reader = XMLInputFactory.newInstance().createXMLEventReader( new InputStreamReader( is ) );
		boolean inName = false;
		name = EMPTY_STRING;

		while( reader.hasNext() )
		{
			final XMLEvent event = reader.nextEvent();

			if( event.getEventType() == XMLStreamConstants.START_ELEMENT )
			{
				final String startElementName = event.asStartElement().getName().getLocalPart();

				inName = ( startElementName.equals( "Gene-ref_locus" ) || startElementName.equals( "Gene-ref_desc" ) ); //$NON-NLS-1$//$NON-NLS-2$
			}
			else if( event.getEventType() == XMLStreamConstants.CHARACTERS )
			{
				final String characters = event.asCharacters().getData().trim();

				if( inName )
				{
					name += characters + SPACE;
				}
			}
			else if( event.getEventType() == XMLStreamConstants.END_ELEMENT )
			{
				inName = false;
			}
		}

		name = name.trim();
	}
}