/**
 * (C) ChRL 2014 - chrl-utils - at.chrl.iryna - IrynaServer.java
 * Created: 29.07.2014 - 22:06:33
 */
package at.chrl.iryna;

import at.chrl.iryna.IrynaConfig.Iryna;
import at.chrl.iryna.implementation.IrynaConnectionFactoryImpl;
import at.chrl.nutils.JVMInfoUtil;
import at.chrl.nutils.network.NioServer;
import at.chrl.nutils.network.ServerCfg;

/**
 * @author Vinzynth
 *
 */
public final class IrynaServer {

	private IrynaServer() {}
	
	private static final class SingletonHolder{
		private final static IrynaServer instance;
		private final static NioServer nioInstance;
		
		static{
			ServerCfg config = new ServerCfg(
					IrynaConfig.BIND_ADDRESS,
					IrynaConfig.PORT,
					IrynaConfig.CONNECTION_NAME,
					new IrynaConnectionFactoryImpl()
			);
			nioInstance = new NioServer(IrynaConfig.DISPATCHER_THREAD_COUNT, config);
			instance = new IrynaServer();
		}
	}
	
	public static final IrynaServer getInstace(){
		return SingletonHolder.instance;
	}
	
	public NioServer getNioServerInstance(){
		return SingletonHolder.nioInstance;
	}
	
	/**
	 * {@inheritDoc}
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(512);
		sb.append(JVMInfoUtil.printSection("IRYNA Server"));
		sb.append(System.lineSeparator());
		sb.append(JVMInfoUtil.printSection(super.toString()));
		sb.append(System.lineSeparator());
		return sb.toString();
	}
	
	public static void main(String[] args) {
		Iryna.out.print(getInstace());
	}
}
