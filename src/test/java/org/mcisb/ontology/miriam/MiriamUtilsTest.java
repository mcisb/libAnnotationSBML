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

import org.junit.*;

/**
 * 
 *
 * @author Neil Swainston
 */
public class MiriamUtilsTest
{	
	/**
	 *
	 * @throws Exception
	 */
	@Test 	
	public void getOntologiesDownload() throws Exception
	{
		test( new DownloadMiriamUtils() );
	}
	
	/**
	 * 
	 *
	 * @param miriamUtils
	 * @throws Exception
	 */
	@SuppressWarnings("static-method")
	private void test( final MiriamUtils miriamUtils ) throws Exception
	{
		Assert.assertTrue( miriamUtils.getOntologiesMap().size() > 0 );
	}
}