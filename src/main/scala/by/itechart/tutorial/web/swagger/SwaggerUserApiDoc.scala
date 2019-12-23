package by.itechart.tutorial.web.swagger

import by.itechart.tutorial.dao.User
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.media.{Content, Schema}
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.{Operation, Parameter}
import javax.ws.rs._;

@Path("/users")
@Tag(name = "User API")
trait SwaggerUserApiDoc {

  @GET
  @Path("/{id}")
  @Operation(
    summary = "Find a user by ID",
    description = "Returns a user based on ID",
    parameters = Array(
      new Parameter(
        name = "id", in = ParameterIn.PATH,
        required = true,
        description = "ID of user that needs to be fetched",
        schema = new Schema(implementation = classOf[Int])
      ),
      new Parameter(
        name = "mode", in = ParameterIn.QUERY,
        required = false,
        description = "It marks that full user info should be fetched (include m:m and 1:m links)",
        schema = new Schema(implementation = classOf[String], allowableValues = Array("full"))
      )
    ),
    responses = Array(
      new ApiResponse(
        responseCode = "200",
        description = "The user",
        content = Array(new Content(schema = new Schema(implementation = classOf[User])))
      ),
      new ApiResponse(
        responseCode = "404",
        description = "Requested user wasn't found"
      )
    )
  )
  def getUserById(): Unit = {}

  @POST
  @Path("/")
  @Operation(
    summary = "Add new user",
    description = "Create and save new user into service",
    requestBody = new RequestBody(
      description = "New user data",
      required = true,
      content = Array(new Content(schema = new Schema(implementation = classOf[User])))
    ),
    responses = Array(
      new ApiResponse(
        responseCode = "200",
        description = "The user successfully created"
      ),
      new ApiResponse(
        responseCode = "400",
        description = "Not enough data to create user"
      )
    )
  )
  def saveUser(): Unit = {}

  @DELETE
  @Path("/{id}")
  @Operation(
    summary = "Find a user by ID and delete it",
    description = "Returns the deleted user",
    parameters = Array(
      new Parameter(
        name = "id", in = ParameterIn.PATH,
        required = true,
        description = "ID of user that needs to be deleted",
        schema = new Schema(implementation = classOf[Int])
      )
    ),
    responses = Array(
      new ApiResponse(
        responseCode = "200",
        description = "The user successfully deleted",
        content = Array(new Content(schema = new Schema(implementation = classOf[User])))
      ),
      new ApiResponse(
        responseCode = "404",
        description = "Requested user wasn't found"
      )
    )
  )
  def deleteUserById(): Unit = {}

  @PUT
  @Path("/{id}")
  @Operation(
    summary = "Find a user by ID and update it",
    description = "Returns the updated user",
    parameters = Array(
      new Parameter(
        name = "id", in = ParameterIn.PATH,
        required = true,
        description = "ID of user that needs to be update",
        schema = new Schema(implementation = classOf[Int])
      )
    ),
    requestBody = new RequestBody(
      description = "New user data",
      required = true,
      content = Array(new Content(schema = new Schema(implementation = classOf[User])))
    ),
    responses = Array(
      new ApiResponse(
        responseCode = "200",
        description = "The user successfully updated",
        content = Array(new Content(schema = new Schema(implementation = classOf[User])))
      ),
      new ApiResponse(
        responseCode = "404",
        description = "Requested user wasn't found"
      )
    )
  )
  def updateUserById(): Unit = {}

  @GET
  @Path("")
  @Operation(
    summary = "Find limited quantity of users with offset",
    description = "[limited, offset] => Returns limited quantity of users with offset\n",
    parameters = Array(
      new Parameter(
        name = "offset", in = ParameterIn.QUERY,
        required = false,
        description = "Offset for users set",
        schema = new Schema(implementation = classOf[Int])
      ),
      new Parameter(
        name = "limit", in = ParameterIn.QUERY,
        required = false,
        description = "Max quantity fetched users",
        schema = new Schema(implementation = classOf[Int])
      )
    ),
    responses = Array(
      new ApiResponse(
        responseCode = "200",
        description = "Users fetched",
        content = Array(new Content(schema = new Schema(implementation = classOf[User])))
      ),
      new ApiResponse(
        responseCode = "404",
        description = "Requested users wasn't found"
      )
    )
  )
  def getUsersWitOffsetAndLimit(): Unit = {}

  @PATCH
  @Path("{id}")
  @Operation(
    summary = "Activation or deactivation user's account",
    parameters = Array(
      new Parameter(
        name = "activity", in = ParameterIn.QUERY,
        required = true,
        description = "false - to deactivate user's account, true - to active",
        schema = new Schema(implementation = classOf[Boolean])
      ),
      new Parameter(
        name = "id", in = ParameterIn.PATH,
        required = true,
        description = "ID of user which account status should be changed",
        schema = new Schema(implementation = classOf[Int])
      )
    ),
    responses = Array(
      new ApiResponse(
        responseCode = "200",
        description = "User's activity status was changed",
        content = Array(new Content(schema = new Schema(implementation = classOf[User])))
      ),
      new ApiResponse(
        responseCode = "404",
        description = "Requested users wasn't found"
      )
    )
  )
  def changeUserActivityStatus(): Unit = {}


}
