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
package org.mcisb.ontology.go;

import java.rmi.*;
import javax.xml.rpc.*;
import org.mcisb.ontology.*;

/**
 * 
 * @author Neil Swainston
 */
public class GoTerm extends OntologyTerm
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * 
	 * @param id
	 * @throws Exception
	 */
	public GoTerm( String id ) throws Exception
	{
		super( OntologyFactory.getOntology( Ontology.GO ), id );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mcisb.ontology.OntologyTerm#doInitialise()
	 */
	@Override
	protected void doInitialise() throws RemoteException, ServiceException
	{
		final String GO = "GO"; //$NON-NLS-1$
		name = OntologyLookupServiceClient.getTermById( id.replaceAll( OntologyTerm.ENCODED_COLON, OntologyTerm.COLON ), GO );
		addSynonym( name );
	}
}