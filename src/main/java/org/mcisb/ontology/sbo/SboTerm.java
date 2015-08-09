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
	 */
	public String getFormula()
	{
		return JSBML.formulaToString( JSBML.readMathMLFromString( math ) );
	}
	
	/**
	 * 
	 * @return math
	 */
	public String getMath()
	{
		return math;
	}
	
	/**
	 * 
	 * @return math
	 */
	public String getRawMath()
	{
		return rawMath;
	}
	
	/**
	 *
	 * @param math
	 */
	public void setMath( final String math )
	{
		final String DUPLICATE_NAMESPACE = "xmlns=\"http://www.w3.org/1998/Math/MathML\" xmlns=\"http://www.w3.org/1998/Math/MathML\""; //$NON-NLS-1$
		final String SINGLE_NAMESPACE = "xmlns=\"http://www.w3.org/1998/Math/MathML\""; //$NON-NLS-1$
		this.rawMath = math.replaceAll( DUPLICATE_NAMESPACE, SINGLE_NAMESPACE );
		
		final String INVALID_CI_PATTERN = "<ci definitionURL=\"http://biomodels.net/SBO/#SBO:(\\d)+\">"; //$NON-NLS-1$
		final String VALID_CI_PATTERN = "<ci>"; //$NON-NLS-1$
		final String SEMANTICS_START_PATTERN = "<semantics xmlns:ns2=\"http://www.w3.org/1998/Math/MathML\" definitionURL=\"http://biomodels.net/SBO/#SBO:(\\d)+\">"; //$NON-NLS-1$
		final String SEMANTICS_END_PATTERN = "</semantics>"; //$NON-NLS-1$
		final String EMPTY_STRING = ""; //$NON-NLS-1$
		this.math = rawMath.replaceAll( INVALID_CI_PATTERN, VALID_CI_PATTERN ).replaceAll( SEMANTICS_START_PATTERN, EMPTY_STRING ).replaceAll( SEMANTICS_END_PATTERN, EMPTY_STRING );
	}
}