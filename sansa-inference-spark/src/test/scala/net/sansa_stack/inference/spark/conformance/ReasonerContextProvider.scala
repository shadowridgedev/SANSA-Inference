package net.sansa_stack.inference.spark.conformance

import net.sansa_stack.inference.spark.forwardchaining.ForwardRuleReasoner

/**
  * Provides a reasoner.
  *
  * @author Lorenz Buehmann
  */
trait ReasonerContextProvider {

  def reasoner: ForwardRuleReasoner

}
