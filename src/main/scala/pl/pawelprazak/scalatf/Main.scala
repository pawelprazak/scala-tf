package pl.pawelprazak.scalatf
import io.grpc.ManagedChannel
import tfplugin5.tfplugin5.`2`.{GetProviderSchema, ProviderGrpc}

import scala.concurrent.Future

object terraform {
  // Handshake MagicCookie used to configure clients and servers.
  object MagicCookie {
    // The magic cookie values should NEVER be changed.
    val Key = "TF_PLUGIN_MAGIC_COOKIE"
    val Value =
      "d602bf8f470bc67ca7faa0386276bbdd4330efaf76d1a219cb4d6991ca9872b2"
  }
}

object netty {
  object channel {

    import io.grpc.netty.NettyChannelBuilder
    import io.netty.channel.epoll.{
      Epoll,
      EpollDomainSocketChannel,
      EpollEventLoopGroup
    }
    import io.netty.channel.kqueue.{
      KQueue,
      KQueueDomainSocketChannel,
      KQueueEventLoopGroup
    }
    import io.netty.channel.unix.DomainSocketAddress

    import java.io.IOException

    @throws[IOException]
    def builder(url: String): NettyChannelBuilder =
      url match {
        case url if url.startsWith("unix:") =>
          val address = new DomainSocketAddress(url.replaceFirst("^unix:", ""))
          val builder =
            NettyChannelBuilder.forAddress(address).overrideAuthority(url)
          if (Epoll.isAvailable)
            builder
              .channelType(classOf[EpollDomainSocketChannel])
              .eventLoopGroup(new EpollEventLoopGroup)
          else if (KQueue.isAvailable)
            builder
              .channelType(classOf[KQueueDomainSocketChannel])
              .eventLoopGroup(new KQueueEventLoopGroup)
          else
            throw new IOException(
              "Unix domain sockets are unsupported on this platform"
            )
        case url => NettyChannelBuilder.forTarget(url)
      }

    @throws[IOException]
    def build(url: String): ManagedChannel = builder(url).build()
  }
}

object Hacks {
  import scala.concurrent.ExecutionContext.Implicits.global

  // TODO: run the provider using magic cookie and read the socket from JSON logs along with version etc.
  private val socket =
    "unix:/var/folders/nr/rqhf2hvn3wn8q_mjysz2gq6m0000gn/T/plugin802699758"
  val channel: ManagedChannel = netty.channel.build(socket)
  val blockingStub: ProviderGrpc.ProviderBlockingStub =
    ProviderGrpc.blockingStub(channel)
  val stub: ProviderGrpc.ProviderStub = ProviderGrpc.stub(channel)
  val request: GetProviderSchema.Request = GetProviderSchema.Request()
  val f: Future[GetProviderSchema.Response] = stub.getSchema(request)
  f.onComplete(println)
}

object Main {
  def main(args: Array[String]): Unit = {
    Hacks
  }
}
