/**
 * 
 */
package com.SE.NettyServer;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @author Administrator
 *
 */
public class NettyServer {
	private static Logger logger = Logger.getLogger(NettyServer.class);

	private int port;

	public NettyServer(int port) {
		this.port = port;
		bind();
	}

	private void bind() {

		EventLoopGroup boss = new NioEventLoopGroup();
		EventLoopGroup worker = new NioEventLoopGroup();

		try {

			ServerBootstrap bootstrap = new ServerBootstrap();

			bootstrap.group(boss, worker);
			bootstrap.channel(NioServerSocketChannel.class);
			bootstrap.option(ChannelOption.SO_BACKLOG, 1024); //������
			bootstrap.option(ChannelOption.TCP_NODELAY, true);  //���ӳ٣���Ϣ��������
			bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true); //������
			bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel socketChannel)
						throws Exception {
					ChannelPipeline p = socketChannel.pipeline();
					//p.addLast(new NettyServerHandler());
				}
			});
			ChannelFuture f = bootstrap.bind(port).sync();
			if (f.isSuccess()) {
				logger.debug("����Netty����ɹ����˿ںţ�" + this.port);
			}
			// �ر�����
			f.channel().closeFuture().sync();

		} catch (Exception e) {
			logger.error("����Netty�����쳣���쳣��Ϣ��" + e.getMessage());
			e.printStackTrace();
		} finally {
			boss.shutdownGracefully();
			worker.shutdownGracefully();
		}
	}
}
