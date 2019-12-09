package model

import java.sql.{Date, Timestamp}

final case class User(
                       id: Option[Long],
                       name: String,
                       dateOfBirth: Date,
                       dateOfCreation: Timestamp,
                       isActive: Boolean
                     )
