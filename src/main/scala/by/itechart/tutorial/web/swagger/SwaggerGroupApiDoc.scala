package by.itechart.tutorial.web.swagger

import by.itechart.tutorial.dao.Group
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.media.{Content, Schema}
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.{Operation, Parameter}
import javax.ws.rs._

@Path("/groups")
@Tag(name = "Group API")
trait SwaggerGroupApiDoc {

  @GET
  @Path("/{id}")
  @Operation(
    summary = "Find a group by ID",
    description = "Returns a group based on ID",
    parameters = Array(
      new Parameter(
        name = "id", in = ParameterIn.PATH,
        required = true,
        description = "ID of group that needs to be fetched",
        schema = new Schema(implementation = classOf[Int])
      )
    ),
    responses = Array(
      new ApiResponse(
        responseCode = "200",
        description = "The group",
        content = Array(new Content(schema = new Schema(implementation = classOf[Group])))
      ),
      new ApiResponse(
        responseCode = "404",
        description = "Requested group wasn't found"
      )
    )
  )
  def getGroupById(): Unit = {}

  @POST
  @Path("/")
  @Operation(
    summary = "Declare new group",
    description = "Create and save new group into service",
    requestBody = new RequestBody(
      description = "New group definition",
      required = true,
      content = Array(new Content(schema = new Schema(implementation = classOf[Group])))
    ),
    responses = Array(
      new ApiResponse(
        responseCode = "200",
        description = "The group successfully created"
      ),
      new ApiResponse(
        responseCode = "400",
        description = "Not enough data to create group"
      )
    )
  )
  def declareGroup(): Unit = {}

  @DELETE
  @Path("/{id}")
  @Operation(
    summary = "Find a group by ID and delete it",
    description = "Returns the deleted group",
    parameters = Array(
      new Parameter(
        name = "id", in = ParameterIn.PATH,
        required = true,
        description = "ID of group that needs to be deleted",
        schema = new Schema(implementation = classOf[Int])
      )
    ),
    responses = Array(
      new ApiResponse(
        responseCode = "200",
        description = "The group successfully deleted",
        content = Array(new Content(schema = new Schema(implementation = classOf[Group])))
      ),
      new ApiResponse(
        responseCode = "404",
        description = "Requested group wasn't found"
      )
    )
  )
  def deleteGroupById(): Unit = {}

  @PUT
  @Path("/{id}")
  @Operation(
    summary = "Find a group by ID and update it",
    description = "Returns the updated group",
    parameters = Array(
      new Parameter(
        name = "id", in = ParameterIn.PATH,
        required = true,
        description = "ID of group that needs to be update",
        schema = new Schema(implementation = classOf[Int])
      )
    ),
    requestBody = new RequestBody(
      description = "New group definition",
      required = true,
      content = Array(new Content(schema = new Schema(implementation = classOf[Group])))
    ),
    responses = Array(
      new ApiResponse(
        responseCode = "200",
        description = "The group successfully updated",
        content = Array(new Content(schema = new Schema(implementation = classOf[Group])))
      ),
      new ApiResponse(
        responseCode = "404",
        description = "Requested group wasn't found"
      )
    )
  )
  def updateGroupById(): Unit = {}

}
