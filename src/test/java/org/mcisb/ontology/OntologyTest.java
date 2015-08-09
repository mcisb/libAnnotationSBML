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

/**
 * 
 * 
 * @author Neil Swainston
 */
public class OntologyTest
{
	/**
	 * 
	 */
	private static final String NAME = "NAME"; //$NON-NLS-1$

	/**
	 * 
	 */
	private static final String URL_IDENTIFIER = "URL_IDENTIFIER"; //$NON-NLS-1$

	/**
	 * 
	 */
	private static final String URN_IDENTIFIER = "URN_IDENTIFIER"; //$NON-NLS-1$

	/**
	 * 
	 */
	private static final String LINK_TEMPLATE = "LINK_TEMPLATE"; //$NON-NLS-1$

	/**
	 * 
	 */
	private static final String REGULAR_EXPRESSION = "REGULAR_EXPRESSION"; //$NON-NLS-1$

	/**
	 * 
	 */
	private final Ontology ontology = new Ontology( NAME, URL_IDENTIFIER, URN_IDENTIFIER, Arrays.asList( URL_IDENTIFIER, URN_IDENTIFIER ), LINK_TEMPLATE, REGULAR_EXPRESSION );

	/**
	 *
	 */
	@Test
	public void getName()
	{
		Assert.assertEquals( ontology.getName(), NAME );
	}

	/**
	 *
	 */
	@Test
	public void getUrlIdentifier()
	{
		Assert.assertEquals( ontology.getUrlIdentifier(), URL_IDENTIFIER );
	}

	/**
	 *
	 */
	@Test
	public void getUrnIdentifier()
	{
		Assert.assertEquals( ontology.getUrnIdentifier(), URN_IDENTIFIER );
	}

	/**
	 *
	 */
	@Test
	public void getUriIdentifiers()
	{
		Assert.assertTrue( ontology.getUriIdentifiers().contains( URL_IDENTIFIER ) );
		Assert.assertTrue( ontology.getUriIdentifiers().contains( URN_IDENTIFIER ) );
	}

	/**
	 *
	 */
	@Test
	public void getLinkTemplate()
	{
		Assert.assertEquals( ontology.getLinkTemplate(), LINK_TEMPLATE );
	}

	/**
	 *
	 */
	@Test
	public void getRegularExpression()
	{
		Assert.assertEquals( ontology.getRegularExpression(), REGULAR_EXPRESSION );
	}

	/**
	 *
	 */
	@Test
	public void toStringTest()
	{
		Assert.assertEquals( ontology.toString(), NAME );
	}
}