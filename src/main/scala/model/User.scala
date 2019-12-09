package model

import java.sql.{Date, Timestamp}

final case class User(
                       id: Option[Long],
                       name: String,
                       surname: String,
                       email: String,
                       dateOfBirth: Date,
                       creationDate: Timestamp,
                       lastUpdateTime: Timestamp,
                       isActive: Boolean
                     )
