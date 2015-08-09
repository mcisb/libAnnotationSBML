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

import java.util.*;
import org.mcisb.ontology.*;

/**
 *
 * @author Neil Swainston
 */
public abstract class MiriamUtils
{
	/**
	 *
	 * @return Map
	 * @throws Exception
	 */
	public static Map<String,Ontology> getOntologies() throws Exception
	{
		return new DownloadMiriamUtils().getOntologiesMap();
	}
	
	/**
	 *
	 * @return Map
	 * @throws Exception
	 */
	protected abstract Map<String,Ontology> getOntologiesMap() throws Exception;
}