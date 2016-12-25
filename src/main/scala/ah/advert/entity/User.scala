package ah.advert.entity


import java.time.LocalDateTime
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import javax.xml.bind.DatatypeConverter


case class User(
                 id: Long,
                 name: String,
                 email: String,
                 password: String,
                 salt: String,
                 createdOn: LocalDateTime
               )

object User {

  def withRandomUUID(
                      name: String,
                      email: String,
                      plainPassword: String,
                      salt: String,
                      createdOn: LocalDateTime
                    )
  = User(0, name.toLowerCase, email, encryptPassword(plainPassword, salt), salt, createdOn)

  def encryptPassword(password: String, salt: String): String = {
    val keySpec = new PBEKeySpec(password.toCharArray, salt.getBytes, 10000, 128)
    val secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
    val bytes = secretKeyFactory.generateSecret(keySpec).getEncoded
    DatatypeConverter.printHexBinary(bytes)
  }

  def passwordsMatch(plainPassword: String, user: User): Boolean = {
    user.password.equals(encryptPassword(plainPassword, user.salt))
  }
}
