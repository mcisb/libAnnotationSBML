/*******************************************************************************
 * Manchester Centre for Integrative Systems Biology
 * University of Manchester
 * Manchester M1 7ND
 * United Kingdom
 * 
 * Copyright (C) 2008 University of Manchester
 * 
 * This program is released under the Academic Free License ("AFL") v3.0.
 * (http://www.opensource.org/licenses/academic.php)
 *******************************************************************************/
package org.mcisb.ontology.kegg;

import org.mcisb.ontology.*;

/**
 * 
 * @author Neil Swainston
 */
public class KeggPathwayTerm extends KeggTerm
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param id
	 * @throws Exception
	 */
	public KeggPathwayTerm( final String id ) throws Exception
	{
		super( Ontology.KEGG_PATHWAY, id, null );
	}
}