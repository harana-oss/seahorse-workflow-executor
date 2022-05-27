package ai.deepsense.deeplang.actions

import java.io.InputStream
import javax.mail.Message

import scala.concurrent.Future
import scala.util.Try

import akka.actor.ActorSystem
import org.mockito.ArgumentMatchers.{eq => eqMatcher, _}
import org.mockito.Mockito._
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentCaptor
import org.scalatest.matchers.should.Matchers
import org.scalatest.concurrent.Eventually
import org.scalatestplus.mockito.MockitoSugar
import org.scalatest.wordspec.AnyWordSpec

import ai.deepsense.commons.mail.EmailSender
import ai.deepsense.commons.rest.client.NotebookRestClient
import ai.deepsense.deeplang.actionobjects.dataframe.DataFrame
import ai.deepsense.deeplang.actions.Notebook.EmailAddressChoice
import ai.deepsense.deeplang.actions.Notebook.SendEmailChoice
import ai.deepsense.deeplang.ContextualDataFrameStorage
import ai.deepsense.deeplang.ExecutionContext

class NotebookSpec extends AnyWordSpec with Matchers with MockitoSugar with Eventually {

  def uutName(uut: Notebook): String = uut.getClass.getSimpleName.filterNot(_ == '$')

  def createUUTs(): Seq[Notebook] = Seq(new PythonNotebook, new RNotebook)

  trait Setup {

    val dataFrame: DataFrame = mock[DataFrame]

    val executionContext: ExecutionContext = mock[ExecutionContext]

    val notebookClient: NotebookRestClient = mock[NotebookRestClient]

    val actorSystem = ActorSystem()

    when(notebookClient.as).thenReturn(actorSystem)
    when(notebookClient.generateAndPollNbData(any()))
      .thenReturn(Future.successful("This should be notebook data".getBytes()))

    val dataFrameStorage: ContextualDataFrameStorage = mock[ContextualDataFrameStorage]

    val emailSender: EmailSender = mock[EmailSender]

    val message: Message = mock[Message]

    val messageWithAttachment: Message = mock[Message]

    when(executionContext.notebooksClient).thenReturn(Some(notebookClient))
    when(executionContext.dataFrameStorage).thenReturn(dataFrameStorage)
    when(executionContext.emailSender).thenReturn(Some(emailSender))

    when(emailSender.createPlainMessage(any(), any(), any())).thenReturn(message)
    when(emailSender.attachAttachment(eqMatcher(message), any(), any(), any())).thenReturn(messageWithAttachment)
    when(emailSender.sendEmail(any())).thenReturn(None)

    val sendEmailChoice: SendEmailChoice = SendEmailChoice

    val emailAddressChoice: EmailAddressChoice = EmailAddressChoice

    emailAddressChoice.setEmailAddress("john@example.com")
    sendEmailChoice.setSendEmail(Set(emailAddressChoice))

    def setFullEmailParams(uut: Notebook): Unit =
      uut.setShouldExecute(Set(sendEmailChoice))

    def setDontSendEmail(uut: Notebook): Unit = {
      uut.setShouldExecute(Set(sendEmailChoice))
      uut.getShouldExecute.head.setSendEmail(Set())
    }

    def runUut(uut: Notebook): Unit =
      uut.executeUntyped(Vector(dataFrame))(executionContext)

  }

  createUUTs().foreach { uut =>
    s"A ${uutName(uut)}" when {
      "all dependencies work correctly" should {
        "call the notebook server" in {
          new Setup {
            setFullEmailParams(uut)
            runUut(uut)

            verify(notebookClient).generateAndPollNbData(any())
          }
        }

        "try to send email" in {
          val uuts = createUUTs()
          new Setup {
            setFullEmailParams(uut)
            runUut(uut)

            verify(emailSender).sendEmail(messageWithAttachment)
          }
        }

        "include the attachment in email" in {
          val uuts = createUUTs()
          new Setup {
            setFullEmailParams(uut)
            runUut(uut)

            val dataStream = ArgumentCaptor.forClass(classOf[InputStream])

            verify(emailSender).attachAttachment(any(), dataStream.capture(), any(), any())

            val bytes  = collection.mutable.ArrayBuffer[Byte]()
            val stream = dataStream.getValue
            var byte   = stream.read()

            while (byte != -1) {
              bytes += byte.toByte
              byte = stream.read()
            }

            bytes shouldBe "This should be notebook data".getBytes()

          }
        }
      }

      "no email address given" should {
        "call the notebook server" in {
          val uuts: Seq[Notebook] = createUUTs()
          uuts.foreach { uut =>
            new Setup {
              setDontSendEmail(uut)
              uut.executeUntyped(Vector(dataFrame))(executionContext)

              verify(notebookClient).generateAndPollNbData(any())
            }
          }
        }

        "not try to send email" in {
          val uuts: Seq[Notebook] = createUUTs()
          uuts.foreach { uut =>
            new Setup {
              setDontSendEmail(uut)
              runUut(uut)

              verify(emailSender, times(0)).sendEmail(any())
            }
          }
        }
      }

      "notebook data generation went wrong" should {
        "fail with notebook server's exception" in {
          new Setup {
            val serverException = new RuntimeException("test exception")
            when(notebookClient.generateAndPollNbData(any())).thenReturn(Future.failed(serverException))

            setFullEmailParams(uut)
            (the[RuntimeException] thrownBy runUut(uut) should have).message(serverException.getMessage)
          }
        }

        "try to send email" in {
          new Setup {
            when(notebookClient.generateAndPollNbData(any()))
              .thenReturn(Future.failed(new RuntimeException("test exception")))

            setFullEmailParams(uut)
            Try {
              runUut(uut)
            }

            eventually {
              verify(emailSender, times(1)).sendEmail(any())
            }
          }
        }
      }
    }
  }

}
