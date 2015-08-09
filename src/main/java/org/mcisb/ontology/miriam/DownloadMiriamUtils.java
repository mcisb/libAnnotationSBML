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
package org.mcisb.ontology.miriam;

import java.io.*;
import java.net.*;
import java.nio.charset.*;
import java.util.*;
import javax.xml.namespace.*;
import javax.xml.stream.*;
import javax.xml.stream.events.*;
import org.mcisb.ontology.*;
import org.mcisb.util.io.*;

/**
 * 
 * @author Neil Swainston
 */
class DownloadMiriamUtils extends MiriamUtils
{
	/**
	 * 
	 */
	private final URL remoteUrl = new URL( "http://www.ebi.ac.uk/miriam/main/XMLExport" ); //$NON-NLS-1$

	/**
	 * 
	 */
	private final DownloadUtils downloadUtils;

	/**
	 * 
	 * @throws MalformedURLException
	 */
	DownloadMiriamUtils() throws MalformedURLException
	{
		final Map<String,Object> nameValuePairs = new HashMap<>();
		nameValuePairs.put( "fileName", "MiriamXML" ); //$NON-NLS-1$ //$NON-NLS-2$
		downloadUtils = new DownloadUtils( remoteUrl, nameValuePairs );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mcisb.miriam.MiriamUtils#getOntologiesMap()
	 */
	@Override
	protected Map<String,Ontology> getOntologiesMap() throws IOException, XMLStreamException
	{
		return read( new String( FileUtils.read( downloadUtils.getFileContents() ) ) );
	}

	/**
	 * 
	 * @param xml
	 * @return Map
	 * @throws XMLStreamException
	 * @throws IOException
	 */
	private static Map<String,Ontology> read( final String xml ) throws XMLStreamException, IOException
	{
		final Map<String,Ontology> ontologies = new HashMap<>();

		InputStream is = null;
		XMLEventReader reader = null;

		try
		{
			// & to &amp; encoding is sometimes required, as the EBI sometimes
			// delivers invalid XML.
			is = new ByteArrayInputStream( xml.replace( "& ", "&amp; " ).getBytes( Charset.defaultCharset() ) ); //$NON-NLS-1$ //$NON-NLS-2$
			reader = XMLInputFactory.newInstance().createXMLEventReader( is );

			final String NAMESPACE_URI = "http://www.biomodels.net/MIRIAM/"; //$NON-NLS-1$
			final QName datatypeQName = new QName( NAMESPACE_URI, "datatype" ); //$NON-NLS-1$
			final QName nameQName = new QName( NAMESPACE_URI, "name" ); //$NON-NLS-1$
			final QName patternQName = new QName( "pattern" ); //$NON-NLS-1$
			final QName uriQName = new QName( NAMESPACE_URI, "uri" ); //$NON-NLS-1$
			final QName typeQName = new QName( "type" ); //$NON-NLS-1$
			final QName deprecatedQName = new QName( "deprecated" ); //$NON-NLS-1$
			final QName dataEntryQName = new QName( NAMESPACE_URI, "dataEntry" ); //$NON-NLS-1$
			final String URL = "URL"; //$NON-NLS-1$
			final String URN = "URN"; //$NON-NLS-1$
			final StringBuffer characters = new StringBuffer();

			String name = null;
			String urlIdentifier = null;
			String urnIdentifier = null;
			Collection<String> uriIdentifiers = null;
			String linkTemplate = null;
			String regularExpression = null;
			boolean isDeprecated = false;
			boolean isUrl = false;
			boolean isUrn = false;

			while( reader.hasNext() )
			{
				final XMLEvent event = reader.nextEvent();

				if( event.isStartElement() )
				{
					characters.setLength( 0 );

					if( event.asStartElement().getAttributeByName( patternQName ) != null )
					{
						regularExpression = event.asStartElement().getAttributeByName( patternQName ).getValue();
					}
					else if( event.asStartElement().getName().equals( uriQName ) )
					{
						isDeprecated = event.asStartElement().getAttributeByName( deprecatedQName ) != null && Boolean.valueOf( event.asStartElement().getAttributeByName( deprecatedQName ).getValue() ).booleanValue();
						isUrl = event.asStartElement().getAttributeByName( typeQName ) != null && event.asStartElement().getAttributeByName( typeQName ).getValue().equals( URL );
						isUrn = event.asStartElement().getAttributeByName( typeQName ) != null && event.asStartElement().getAttributeByName( typeQName ).getValue().equals( URN );
					}
				}
				else if( event.isEndElement() )
				{
					final QName endElementName = event.asEndElement().getName();

					if( endElementName.equals( datatypeQName ) )
					{
						ontologies.put( name, new Ontology( name, urlIdentifier, urnIdentifier, uriIdentifiers, linkTemplate, regularExpression ) );
						urlIdentifier = null;
						urnIdentifier = null;
						uriIdentifiers = null;
						linkTemplate = null;
					}
					else if( endElementName.equals( nameQName ) )
					{
						name = characters.toString();
					}
					else if( endElementName.equals( uriQName ) )
					{
						if( urnIdentifier == null && isUrn && !isDeprecated )
						{
							urnIdentifier = characters.toString();
						}

						if( urlIdentifier == null && isUrl && !isDeprecated )
						{
							urlIdentifier = characters.toString();
						}

						if( uriIdentifiers == null )
						{
							uriIdentifiers = new HashSet<>();
						}

						uriIdentifiers.add( characters.toString() );
					}
					else if( endElementName.equals( dataEntryQName ) && linkTemplate == null )
					{
						linkTemplate = characters.toString();
					}
				}
				else if( event.isCharacters() )
				{
					characters.append( event.asCharacters().getData() );
				}
			}
		}
		finally
		{
			if( is != null )
			{
				is.close();
			}
			if( reader != null )
			{
				reader.close();
			}
		}

		return ontologies;
	}
}