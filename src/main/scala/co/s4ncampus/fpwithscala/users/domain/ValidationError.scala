package co.s4ncampus.fpwithscala.users.domain

sealed trait ValidationError extends Product with Serializable
case class UserAlreadyExistsError(user: User) extends ValidationError
//case class LegalIDDoesntExistError(user: User) extends ValidationError
case class LegalIDDoesntExistError(id: User) extends ValidationError