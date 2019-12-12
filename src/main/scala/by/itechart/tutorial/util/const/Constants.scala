package by.itechart.tutorial.util.const

object Constants {
  private val PostAndPutExceptionPattern = "not enough parameters to %s %s"
  private val DeleteModelExceptionPattern = "something went wrong during %s deleting"
  private val ModelNotFoundExceptionPattern = "requested %s wasn't found"

  val DefaultExceptionMessage: (String, String) = "message" -> "Ops! something went wrong!"

  val PostUserExceptionMessage: (String, String) = "message" -> PostAndPutExceptionPattern.format("create", "user")
  val PutUserExceptionMessage: (String, String) = "message" -> PostAndPutExceptionPattern.format("update", "user")
  val DeleteUserExceptionMessage: (String, String) = "message" -> DeleteModelExceptionPattern.format("user")
  val UserNotFoundExceptionMessage: (String, String) = "message" -> ModelNotFoundExceptionPattern.format("user")

  val PostGroupExceptionMessage: (String, String) = "message" -> PostAndPutExceptionPattern.format("create", "group")
  val PutGroupExceptionMessage: (String, String) = "message" -> PostAndPutExceptionPattern.format("update", "group")
  val DeleteGroupExceptionMessage: (String, String) = "message" -> DeleteModelExceptionPattern.format("group")
  val GroupNotFoundExceptionMessage: (String, String) = "message" -> ModelNotFoundExceptionPattern.format("group")

  val DateFormat = "EEE, MMM dd, yyyy h:mm a"
}
