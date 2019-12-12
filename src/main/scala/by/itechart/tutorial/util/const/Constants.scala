package by.itechart.tutorial.util.const

object Constants {
  private val POST_PUT_EXCEPTION_PATTERN = "not enough parameters to %s %s"
  private val DELETE_MODEL_EXCEPTION_PATTERN = "something went wrong during %s deleting"
  private val MODEL_NOT_FOUND = "requested %s wasn't found"

  val POST_USER_EXCEPTION_MESSAGE: (String, String) = "message" -> POST_PUT_EXCEPTION_PATTERN.format("create", "user")
  val PUT_USER_EXCEPTION_MESSAGE: (String, String) = "message" -> POST_PUT_EXCEPTION_PATTERN.format("update", "user")
  val DELETE_USER_EXCEPTION_MESSAGE: (String, String) = "message" -> DELETE_MODEL_EXCEPTION_PATTERN.format("user")
  val USER_NOT_FOUND: (String, String) = "message" -> MODEL_NOT_FOUND.format("user")

  val POST_GROUP_EXCEPTION_MESSAGE: (String, String) = "message" -> POST_PUT_EXCEPTION_PATTERN.format("create", "group")
  val PUT_GROUP_EXCEPTION_MESSAGE: (String, String) = "message" -> POST_PUT_EXCEPTION_PATTERN.format("update", "group")
  val DELETE_GROUP_EXCEPTION_MESSAGE: (String, String) = "message" -> DELETE_MODEL_EXCEPTION_PATTERN.format("group")
  val GROUP_NOT_FOUND: (String, String) = "message" -> MODEL_NOT_FOUND.format("group")

  val DATE_FORMAT = "EEE, MMM dd, yyyy h:mm a"
}
