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
package org.mcisb.ontology.sbo;

import java.io.*;
import java.net.*;
import java.nio.charset.*;
import javax.xml.namespace.*;
import javax.xml.stream.*;
import javax.xml.stream.events.*;
import org.mcisb.ontology.*;
import org.sbml.jsbml.*;

/**
 * 
 * @author Neil Swainston
 */
public class SboTerm extends OntologyTerm
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private String rawMath = null;

	/**
	 * 
	 */
	private String math = null;

	/**
	 * 
	 * @param id
	 * @throws Exception
	 */
	public SboTerm( final String id ) throws Exception
	{
		super( OntologyFactory.getOntology( Ontology.SBO ), id );
	}

	/**
	 * 
	 * @return int
	 */
	public int getIntId()
	{
		return Integer.parseInt( id.substring( id.indexOf( OntologyTerm.COLON ) + OntologyTerm.COLON.length() ) );
	}

	/**
	 * 
	 * @return String
	 * @throws FactoryConfigurationError 
	 * @throws Exception 
	 */
	public String getFormula() throws FactoryConfigurationError, Exception
	{
		return JSBML.formulaToString( JSBML.readMathMLFromString( getMath() ) );
	}
	
	/**
	 * 
	 * @return math
	 * @throws FactoryConfigurationError 
	 * @throws Exception 
	 */
	public String getMath() throws FactoryConfigurationError, Exception
	{
		if( math == null )
		{
			init();
		}
		
		return math;
	}
	
	/**
	 * 
	 * @return math
	 * @throws FactoryConfigurationError 
	 * @throws Exception 
	 */
	public String getRawMath() throws FactoryConfigurationError, Exception
	{
		if( rawMath == null )
		{
			init();
		}
		return rawMath;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.mcisb.ontology.OntologyTerm#init()
	 */
	@Override
	protected synchronized void init() throws FactoryConfigurationError, Exception
	{
		super.init();
		
		final int ZERO = 0;
		final int CAPACITY = 2^8;
		final URL url = new URL( "http://www.ebi.ac.uk/sbo/exports/Main/SBO_XML.xml" ); //$NON-NLS-1$
		
		try( final InputStream is = url.openStream();
				final OutputStream os = new ByteArrayOutputStream( CAPACITY );
				final Reader reader = new InputStreamReader( is, Charset.defaultCharset().name() ) )
		{
			final XMLEventReader xmlEventReader = XMLInputFactory.newInstance().createXMLEventReader( reader );
			final XMLEventWriter xmlEventWriter = XMLOutputFactory.newInstance().createXMLEventWriter( os );
			
			final StringBuffer characters = new StringBuffer();
			String currentId = null;
			boolean inId = false;
			boolean inMath = false;
			
			while( xmlEventReader.peek() != null )
			{
				final XMLEvent event = (XMLEvent)xmlEventReader.next();
	
				switch( event.getEventType() )
				{
					case XMLStreamConstants.START_DOCUMENT:
					case XMLStreamConstants.END_DOCUMENT:
					{
						// Ignore.
						break;
					}
					case XMLStreamConstants.START_ELEMENT:
					{
						final StartElement startElement = event.asStartElement();
						final QName qname = startElement.getName();
	
						inId = ( qname.getLocalPart().equals( "id" ) ); //$NON-NLS-1$
						
						if( qname.getLocalPart().equals( "math" ) && id.equals( currentId ) ) //$NON-NLS-1$
						{
							inMath = true;
						}
						
						if( inMath )
						{
							xmlEventWriter.add( event );
						}
						
						break;
					}
					case XMLStreamConstants.END_ELEMENT:
					{
						final EndElement endElement = event.asEndElement();
						final QName qname = endElement.getName();
						
						if( inMath )
						{
							xmlEventWriter.add( event );
						}
						
						if( inId )
						{
							currentId = characters.toString().trim();
							characters.setLength( ZERO );
						}
						else if( qname.getLocalPart().equals( "math" ) && id.equals( currentId ) ) //$NON-NLS-1$
						{
							inMath = false;
							xmlEventWriter.flush();
							os.flush();
							setMath( os.toString() );
							return;
						}
	
						break;
					}
					case XMLStreamConstants.CHARACTERS:
					{
						if( inId )
						{
							characters.append( event.asCharacters().getData() );
						}
						else if( inMath )
						{
							xmlEventWriter.add( event );
						}
						
						break;
					}
					default:
					{
						// No action.
					}
				}
			}
		}
	}
	
	/**
	 * 
	 * @param math
	 */
	private void setMath( final String math )
	{
		this.rawMath = math;

		final String INVALID_CI_PATTERN = "<ci definitionURL=\"http://biomodels.net/SBO/#SBO:(\\d)+\">"; //$NON-NLS-1$
		final String VALID_CI_PATTERN = "<ci>"; //$NON-NLS-1$
		final String SEMANTICS_START_PATTERN = "<semantics definitionURL=\"http://biomodels.net/SBO/#SBO:(\\d)+\">"; //$NON-NLS-1$
		final String SEMANTICS_END_PATTERN = "</semantics>"; //$NON-NLS-1$
		final String EMPTY_STRING = ""; //$NON-NLS-1$
		this.math = rawMath.replaceAll( INVALID_CI_PATTERN, VALID_CI_PATTERN ).replaceAll( SEMANTICS_START_PATTERN, EMPTY_STRING ).replaceAll( SEMANTICS_END_PATTERN, EMPTY_STRING );
	}
}