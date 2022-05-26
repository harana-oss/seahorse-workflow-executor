package ai.deepsense.deeplang.params.validators

object ValidatorType extends Enumeration {

  type ValidatorType = Value

  val Range = Value("range")

  val Regex = Value("regex")

  val ArrayLength = Value("arrayLength")

  val ArrayComplex = Value("range")
// TODO DS-3225 Complex Array Validator serialization
//  val ArrayComplex = Value("arrayComplex")

}
