package co.s4ncampus.fpwithscala.users.domain

import cats.data._
import cats.Monad

class UserService[F[_]](repository: UserRepositoryAlgebra[F], validation: UserValidationAlgebra[F]) {
  def create(user: User)(implicit M: Monad[F]): EitherT[F, UserAlreadyExistsError, User] =
    for {
      _ <- validation.doesNotExist(user)
      saved <- EitherT.liftF(repository.create(user))
    } yield saved

  //falta validar si el usuario existe
  def upload(user: User)(implicit M: Monad[F]): EitherT[F, UserAlreadyExistsError, User] =
    for {
      saved <- EitherT.liftF(repository.upload(user))
    } yield saved

  def getUser(id: String)(implicit M: Monad[F]):EitherT[F, Option[User], LegalIDDoesntExistError] = {
    //var user = new User("", id, "", "", "", "")
    for {
      _ <- validation.doesNotExist2(id)
      saved <- EitherT.liftF(repository.getUser(id)) //:EitherT[F, LegalIDDoesntExistError, User]
      //saved <- repository.findByLegalId(id) //: : OptionT[F, User] Da Error
    } yield saved
  }
}

object UserService{
  def apply[F[_]](
                 repositoryAlgebra: UserRepositoryAlgebra[F],
                 validationAlgebra: UserValidationAlgebra[F],
                 ): UserService[F] =
    new UserService[F](repositoryAlgebra, validationAlgebra)
}