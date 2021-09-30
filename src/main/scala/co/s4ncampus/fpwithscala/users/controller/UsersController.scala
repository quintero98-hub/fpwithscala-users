package co.s4ncampus.fpwithscala.users.controller

import co.s4ncampus.fpwithscala.users.domain._
import cats.effect.Sync
import cats.syntax.all._
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.http4s.{EntityDecoder, HttpRoutes}
import co.s4ncampus.fpwithscala.users.domain.User

class UsersController[F[_]: Sync] extends Http4sDsl[F] {

    implicit val userDecoder: EntityDecoder[F, User] = jsonOf


    //curl -X POST -d '{"legalId":"103","firstName":"Carlos","lastName":"Mendoza","email":"algo@gmail.com","phone":"3158453256"}'
    //http://localhost:8000/users
    private def createUser(userService: UserService[F]): HttpRoutes[F] = 
        HttpRoutes.of[F] {
            case req @ POST -> Root =>
                val action = for {
                    user   <- req.as[User]
                    result <- userService.create(user).value
                } yield result
                action.flatMap {
                    case Right(saved) => Ok(saved.asJson)
                    case Left(UserAlreadyExistsError(existing)) => Conflict(s"The user with legal id ${existing.legalId} already exists")
                }
        }
    //curl -X PUT -d '{"legalId":"103","firstName":"Camilo", "lastName":"Zuluaga", "email":"carlos_zuluaga@epam.com", "phone":"3503247638"}'
    //http://localhost:8000/users/103
    private def updateUser(userService: UserService[F]): HttpRoutes[F] = {
        HttpRoutes.of[F] {
            case req@ PUT -> Root /legalId =>
                val action = for {
                    user   <- req.as[User]
                    result <- userService.upload(user, legalId)
                } yield result
                action.flatMap{
                    case true  => Ok(s"the user with id = $legalId has been modified")
                    case false => Conflict(s"Couldn't find the user with legal id $legalId")
                }
                /*
                val action = for {
                    user   <- req.as[User]
                    result <- userService.upload(user, legalId).value
                } yield result
              action.flatMap{
                  case Some(user) => Ok(user.asJson)
                  case None       => Conflict(s"Couldn't find the user with legal id $legalId")
              }
                action.flatMap {
                    case Right(saved) => Ok(saved.asJson)
                    case Left(UserDoesntExistError(existing)) => Conflict(s"The user with legal id ${existing.legalId} doesn't exist")
                }
                */
        }
    }

    private def getUser(userService: UserService[F]): HttpRoutes[F] = {
        HttpRoutes.of[F] {
            case GET -> Root /legalID =>
                userService.get(legalID).value.flatMap {
                    case Some(user) => Ok(user.asJson)
                    case None       => Conflict(s"Couldn't find the user with legal id $legalID")
                }
        }
    }

    /*
    private def getUsersByName(userService: UserService[F]): HttpRoutes[F] = {
        HttpRoutes.of[F] {
            case GET -> Root/name =>
                userService.getUsersByName(name).flatMap {
                    case list  => Ok(list.asJson)
                    case Nil   => Conflict(s"Couldn't find users with first name $name")
                }
        }
    }
     */

    private def getAll(userService: UserService[F]): HttpRoutes[F] = {
        HttpRoutes.of[F] {
            case GET -> Root =>
                userService.getAll().flatMap {
                    case Nil     => Conflict("There is no users created")
                    case list    => Ok(list.asJson)
                }
        }
    }

    private def deleteUser(userService: UserService[F]): HttpRoutes[F] ={
        HttpRoutes.of[F] {
            case DELETE -> Root /legalID =>
            userService.delete(legalID).flatMap{
                case true   => Ok("User deleted")
                case false  => Conflict(s"Couldn't find the user with legal id $legalID")
            }
        }
    }

    def endpoints(userService: UserService[F]): HttpRoutes[F] = {
        //To convine routes use the function `<+>`
        createUser(userService)<+>updateUser(userService)<+>getUser(userService)<+>deleteUser(userService)<+>getAll(userService)
        //
    }

}

object UsersController {
    def endpoints[F[_]: Sync](userService: UserService[F]): HttpRoutes[F] =
        new UsersController[F].endpoints(userService)
}