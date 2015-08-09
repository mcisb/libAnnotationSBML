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

import java.io.*;
import java.net.*;
import java.util.*;
import javax.xml.stream.*;
import org.mcisb.util.xml.*;
import org.sbml.jsbml.*;

/**
 * 
 * @author neilswainston
 */
public class OntologyTermXmlWriter extends XmlWriter
{
	/**
	 * 
	 */
	private final static String TERM_URI = "uri"; //$NON-NLS-1$

	/**
	 * 
	 * @param os
	 * @throws Exception
	 */
	public OntologyTermXmlWriter( final OutputStream os ) throws Exception
	{
		super( XMLOutputFactory.newInstance().createXMLEventWriter( os ) );
	}

	/**
	 * 
	 * @param ontologyTerm
	 * @throws Exception
	 */
	public void write( final OntologyTerm ontologyTerm ) throws Exception
	{
		final URL schema = new URL( "http://www.mcisb.org/ontology/ontology.xsd" ); //$NON-NLS-1$
		final String PREFIX = "xsi"; //$NON-NLS-1$
		final String URI = "http://www.w3.org/2001/XMLSchema-instance"; //$NON-NLS-1$
		final String XMLNS = "xmlns"; //$NON-NLS-1$
		final String NAMESPACE = "http://org.mcisb.ontology"; //$NON-NLS-1$
		final String XSI_SCHEMA_LOCATION = "xsi:schemaLocation"; //$NON-NLS-1$
		final String SPACE = " "; //$NON-NLS-1$
		final String SCHEMA_LOCATION = NAMESPACE + SPACE + schema;
		final String ONTOLOGY_TERM = "OntologyTerm"; //$NON-NLS-1$

		writeStartDocument();
		writeStartElement( ONTOLOGY_TERM );
		writeNamespace( PREFIX, URI );
		writeAttribute( XMLNS, NAMESPACE );
		writeAttribute( XSI_SCHEMA_LOCATION, SCHEMA_LOCATION );

		writeAttribute( TERM_URI, ontologyTerm.toUri() );
		writeNames( ontologyTerm );
		writeLink( ontologyTerm.getLink() );
		writeXrefs( ontologyTerm.getXrefs() );

		writeEndElement( ONTOLOGY_TERM );
		writeEndDocument();

		close();
	}

	/**
	 * 
	 * @param ontologyTerm
	 * @throws Exception
	 */
	public void writeNames( final OntologyTerm ontologyTerm ) throws Exception
	{
		final String NAMES = "Names"; //$NON-NLS-1$
		final String NAME = "Name"; //$NON-NLS-1$
		final String TYPE = "type"; //$NON-NLS-1$
		final String RECOMMENDED = "recommended"; //$NON-NLS-1$
		final String SYNONYM = "synonym"; //$NON-NLS-1$

		writeStartElement( NAMES );

		final String name = ontologyTerm.getName();

		if( name != null )
		{
			writeStartElement( NAME );
			writeAttribute( TYPE, RECOMMENDED );
			writeCharacters( name );
			writeEndElement( NAME );
		}

		for( Iterator<String> iterator = ontologyTerm.getSynonyms().iterator(); iterator.hasNext(); )
		{
			writeStartElement( NAME );
			writeAttribute( TYPE, SYNONYM );
			writeCharacters( iterator.next() );
			writeEndElement( NAME );
		}

		writeEndElement( NAMES );
	}

	/**
	 * 
	 * @param link
	 * @throws Exception
	 */
	public void writeLink( final String link ) throws Exception
	{
		if( link != null )
		{
			final String LINK = "Link"; //$NON-NLS-1$
			writeStartElement( LINK );
			writeCharacters( link );
			writeEndElement( LINK );
		}
	}

	/**
	 * 
	 * @param xrefs
	 * @throws Exception
	 */
	public void writeXrefs( final Map<OntologyTerm,Object[]> xrefs ) throws Exception
	{
		final String XREFS = "XREFS"; //$NON-NLS-1$
		final String XREF = "XREF"; //$NON-NLS-1$
		final String BIOLOGICAL_QUALIFIER = "BIOLOGICAL_QUALIFIER"; //$NON-NLS-1$

		writeStartElement( XREFS );

		for( Iterator<Map.Entry<OntologyTerm,Object[]>> iterator = xrefs.entrySet().iterator(); iterator.hasNext(); )
		{
			final Map.Entry<OntologyTerm,Object[]> entry = iterator.next();
			final OntologyTerm ontologyTerm = entry.getKey();

			writeStartElement( XREF );
			writeAttribute( BIOLOGICAL_QUALIFIER, Integer.toString( ( (CVTerm.Qualifier)entry.getValue()[ 1 ] ).ordinal() ) );
			writeCharacters( ontologyTerm.toUri() );
			writeEndElement( XREF );
		}

		writeEndElement( XREFS );
	}
}