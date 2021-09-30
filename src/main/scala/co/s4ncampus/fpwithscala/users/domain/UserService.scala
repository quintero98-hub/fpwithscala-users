package co.s4ncampus.fpwithscala.users.domain

import cats.data._
import cats.Monad

class UserService[F[_]](repository: UserRepositoryAlgebra[F], validation: UserValidationAlgebra[F]) {
  def create(user: User)(implicit M: Monad[F]): EitherT[F, UserAlreadyExistsError, User] =
    for {
      _     <- validation.doesNotExist(user)
      saved <- EitherT.liftF(repository.create(user))
    } yield saved

  //falta validar si el usuario existe
  def upload(user: User, legalId: String)(implicit M: Monad[F]): EitherT[F, UserDoesntExistError, User] =
    for {
      saved <- EitherT.liftF(repository.upload(user, legalId))
      _     <- validation.doesAlreadyExist(user)
    } yield saved

  def get(legalId: String): OptionT[F, User] = repository.findByLegalId(legalId)

  def delete(legalId:String):F[Boolean] =repository.delete(legalId)

  def getAll():F[List[User]] = repository.getAll()
}

object UserService{
  def apply[F[_]](
                 repositoryAlgebra: UserRepositoryAlgebra[F],
                 validationAlgebra: UserValidationAlgebra[F],
                 ): UserService[F] =
    new UserService[F](repositoryAlgebra, validationAlgebra)
}