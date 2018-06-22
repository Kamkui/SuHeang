import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class Pinging extends Thread {
	private Object[] msg;
	private String ip;

	public Pinging(String ip) {
		this.ip = ip;
		msg = new Object[4];
	}

	public void run() {
		InputStream is = null;
		BufferedReader br = null;
		try {
			Runtime run = Runtime.getRuntime();
			Process p = run.exec("ping -a " + ip);
			msg[0] = ip;
			is = p.getInputStream();
			br = new BufferedReader(new InputStreamReader(is));
			String line = null;
			while ((line = br.readLine()) != null) {
				// System.out.println(line); // line�� �ӽ� ����غ���.
				if (line.indexOf("[") >= 0) {
					msg[3] = line.substring(5, line.indexOf("["));
				}
				if (line.indexOf("ms") >= 0) {
					// Pattern pattern =
					// Pattern.compile("(\\d+ms)(\\s+)(TTL=\\d+)",Pattern.CASE_INSENSTIVE);
					// Matcher matcher = pattern.matcher(line);
					// System.out.println(matcher.group(1));
					msg[1] = line.substring(line.indexOf("ms") - 1, line.indexOf("ms") + 2);
					msg[2] = line.substring(line.indexOf("TTL=") + 4, line.length());
					break;
				}
				if (line != null)

				{
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static Future<ScanResult> portIsOpen1(final ExecutorService es, final String ip, final int port,
			final int timeout) {
		return es.submit(new Callable<ScanResult>() {
			@Override
			public ScanResult call() {
				try {
					Socket socket = new Socket();
					socket.connect(new InetSocketAddress(ip, port), timeout);
					socket.close();
					return new ScanResult(port, true);
				} catch (Exception ex) {
					return new ScanResult(port, false);
				}
			}
		});
	}

	public static class ScanResult {
		private int port;

		private boolean isOpen;

		public ScanResult(int port, boolean isOpen) {
			super();
			this.port = port;
			this.isOpen = isOpen;
		}

		public int getPort() {
			return port;
		}

		public void setPort(int port) {
			this.port = port;
		}

		public boolean isOpen() {
			return isOpen;
		}
	}// if
	
	public Object[] getMsg() {
		try {
			join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return msg;
	}
	
	public static void main(String[] args) {
		
	}
}

