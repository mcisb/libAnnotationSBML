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
import java.nio.charset.*;
import java.util.*;
import org.json.simple.*;
import org.json.simple.parser.*;


/**
 * 
 * @author Neil Swainston
 */
public class OntologyLookupServiceClient implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @return Map<String,String>
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws ParseException
	 */
	public static Map<String,String> getOntologyNames() throws MalformedURLException, IOException, ParseException
	{
		final String url = "http://www.ebi.ac.uk/ols/api/ontologies?page=0&size=500"; //$NON-NLS-1$
		final JSONObject json = read( url );
		final JSONObject response = (JSONObject)json.get( "_embedded" ); //$NON-NLS-1$
		final JSONArray ontologies = (JSONArray)response.get( "ontologies" ); //$NON-NLS-1$
		final Map<String,String> result = new TreeMap<>();
		
		for( Iterator<JSONObject> iterator = ontologies.iterator(); iterator.hasNext(); )
		{
			final JSONObject entry = iterator.next();
			result.put( entry.get( "ontologyId" ).toString(), entry.get( "ontologyId" ).toString() ); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		return result;
	}
	
	/**
	 * 
	 * @param term
	 * @param ontologyName
	 * @return Map<String,String>
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws ParseException
	 */
	public static Map<String,String> getTermsByName( final String term, final String ontologyName ) throws MalformedURLException, IOException, ParseException
	{
		final String url = "http://www.ebi.ac.uk/ols/api/search?q=" + term + "&ontology=" + ontologyName; //$NON-NLS-1$ //$NON-NLS-2$
		final JSONObject json = read( url );
		final JSONObject response = (JSONObject)json.get( "response" ); //$NON-NLS-1$
		final JSONArray docs = (JSONArray)response.get( "docs" ); //$NON-NLS-1$
		final Map<String,String> result = new TreeMap<>();
		
		for( Iterator<JSONObject> iterator = docs.iterator(); iterator.hasNext(); )
		{
			final JSONObject entry = iterator.next();
			result.put( entry.get( "obo_id" ).toString(), entry.get( "label" ).toString() ); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		return result;
	}

	/**
	 * 
	 * @param termId
	 * @return String
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws ParseException
	 */
	public static String getTermById( final String termId, final String ontologyName ) throws MalformedURLException, IOException, ParseException
	{
		final String encodedId = termId.replaceAll( OntologyTerm.ENCODED_COLON + "|" + OntologyTerm.COLON, "_" ); //$NON-NLS-1$ //$NON-NLS-2$
		final String url = "http://www.ebi.ac.uk/ols/api/ontologies/" + ontologyName + "/terms/http%253A%252F%252Fpurl.obolibrary.org%252Fobo%252F" + encodedId; //$NON-NLS-1$ //$NON-NLS-2$
		final JSONObject json = read( url );
		return (String)json.get( "label" ); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @param url
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws ParseException
	 */
	private static JSONObject read( final String url ) throws MalformedURLException, IOException, ParseException
	{
		final BufferedReader reader = new BufferedReader( new InputStreamReader( new URL( url ).openStream(), Charset.defaultCharset() ) );
		final StringBuffer buffer = new StringBuffer();
		String line = null;

		while( ( line = reader.readLine() ) != null )
		{
			buffer.append( line );
		}
		
		final JSONParser parser = new JSONParser();
		return (JSONObject)parser.parse( buffer.toString() );
	}
}