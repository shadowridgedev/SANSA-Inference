package net.sansa_stack.inference.utils

/**
  * @author Lorenz Buehmann
  */
object EntailmentRegimes {

  sealed abstract class EntailmentRegime() {}

  case object RDFS extends EntailmentRegime()
  case object OWL extends EntailmentRegime()

}
