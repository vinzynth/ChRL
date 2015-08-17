package at.chrl.nutils;

import java.net.InetAddress;

/**
 * @author KID, -Nemesiss-, ggadv2
 */
public class NetworkUtils {
	/**
	 * check if IP address match pattern
	 * 
	 * @param pattern
	 *            *.*.*.* , 192.168.1.0-255 , *
	 * @param address
	 *            - 192.168.1.1<BR>
	 *            <code>address = 10.2.88.12  pattern = *.*.*.*   result: true<BR>
	 *                address = 10.2.88.12  pattern = *   result: true<BR>
	 *                address = 10.2.88.12  pattern = 10.2.88.12-13   result: true<BR>
	 *                address = 10.2.88.12  pattern = 10.2.88.13-125   result: false<BR></code>
	 * @return true if address match pattern
	 */
	public static boolean checkIPMatching(String pattern, String address) {
		if (pattern.equals("*.*.*.*") || pattern.equals("*"))
			return true;

		InetAddress ia1;
		InetAddress ia2;
		String addressToCheck1 = null;
		String addressToCheck2 = null;
		try {
			ia1 = InetAddress.getByName(pattern);
			ia2 = InetAddress.getByName(address);
			addressToCheck1 = ia1.getHostAddress();
			addressToCheck2 = ia2.getHostAddress();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (addressToCheck1 == null || addressToCheck2 == null)
			return false;

		String[] mask = addressToCheck1.split("\\.");
		String[] ip_address = addressToCheck2.split("\\.");
		for (int i = 0; i < mask.length; i++) {
			if (mask[i].equals("*") || mask[i].equals(ip_address[i]))
				continue;
			else if (mask[i].contains("-")) {
				byte min = Byte.parseByte(mask[i].split("-")[0]);
				byte max = Byte.parseByte(mask[i].split("-")[1]);
				byte ip = Byte.parseByte(ip_address[i]);
				if (ip < min || ip > max)
					return false;
			} else
				return false;
		}
		return true;
	}
}
