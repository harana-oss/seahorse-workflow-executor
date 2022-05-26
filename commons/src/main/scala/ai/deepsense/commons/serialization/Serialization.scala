package ai.deepsense.commons.serialization

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

trait Serialization {

  def deserialize[T](bytes: Array[Byte]): T = {
    val bufferIn = new ByteArrayInputStream(bytes)
    val streamIn = new ObjectInputStream(bufferIn)
    try
      streamIn.readObject().asInstanceOf[T]
    finally
      streamIn.close()
  }

  def serialize[T](objectToSerialize: T): Array[Byte] = {
    val byteArrayOutputStream: ByteArrayOutputStream = new ByteArrayOutputStream()
    val oos                                          = new ObjectOutputStream(byteArrayOutputStream)
    try {
      oos.writeObject(objectToSerialize)
      oos.flush()
      byteArrayOutputStream.toByteArray
    } finally
      oos.close()
  }

  def serializeDeserialize[T](obj: T): T = deserialize[T](serialize[T](obj))

}

object Serialization extends Serialization
