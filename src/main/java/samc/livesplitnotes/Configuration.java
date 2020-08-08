package samc.livesplitnotes;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class Configuration {

	private String file;

	private int backKey = 37;
	private int forwardKey = 39;

	private int pollRateInMillis = 100;

	private String address = "localhost";
	private int port = 16834;

	private String splitAt = "";
	
	private boolean alwaysOnTop = true;
	private boolean globalKeyHooks = false;

	public void load() {
		try (InputStream input = new FileInputStream("./config.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            
            this.file = prop.getProperty("file");
            this.backKey = Integer.valueOf(prop.getProperty("back"));
            this.forwardKey = Integer.valueOf(prop.getProperty("forward"));
            this.pollRateInMillis = Integer.valueOf(prop.getProperty("poll"));
            this.address = prop.getProperty("address");
            this.port = Integer.valueOf(prop.getProperty("port"));
            this.splitAt = prop.getProperty("splitAt");
            this.alwaysOnTop = Boolean.valueOf(prop.getProperty("onTop"));
            this.globalKeyHooks = Boolean.valueOf(prop.getProperty("hooks"));
            
		} catch (IOException | NumberFormatException | NullPointerException e) {
			e.printStackTrace();
		}
	}

	public void save() {
		try (OutputStream output = new FileOutputStream("./config.properties")) {

			Properties prop = new Properties();

			if (this.file != null) {
				prop.setProperty("file", this.file);
			}
			
			prop.setProperty("back", Integer.toString(this.backKey));
			prop.setProperty("forward", Integer.toString(this.forwardKey));
			prop.setProperty("poll", Integer.toString(this.pollRateInMillis));
			prop.setProperty("address", this.address);
			prop.setProperty("port", Integer.toString(this.port));
			prop.setProperty("splitAt", this.splitAt);
			prop.setProperty("onTop", Boolean.toString(this.isAlwaysOnTop()));
			prop.setProperty("hooks", Boolean.toString(this.globalKeyHooks));
			
			prop.store(output, null);

		} catch (IOException io) {
			io.printStackTrace();
		}
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public int getBackKey() {
		return backKey;
	}

	public void setBackKey(int backKey) {
		this.backKey = backKey;
	}

	public int getForwardKey() {
		return forwardKey;
	}

	public void setForwardKey(int forwardKey) {
		this.forwardKey = forwardKey;
	}

	public int getPollRateInMillis() {
		return pollRateInMillis;
	}

	public void setPollRateInMillis(int pollRateInMillis) {
		this.pollRateInMillis = pollRateInMillis;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getSplitAt() {
		return splitAt;
	}

	public void setSplitAt(String splitAt) {
		this.splitAt = splitAt;
	}
	
	public boolean isAlwaysOnTop() {
		return alwaysOnTop;
	}

	public void setAlwaysOnTop(boolean alwaysOnTop) {
		this.alwaysOnTop = alwaysOnTop;
	}
	
	public boolean isGlobalKeyHooks() {
		return globalKeyHooks;
	}

	public void setGlobalKeyHooks(boolean globalKeyHooks) {
		this.globalKeyHooks = globalKeyHooks;
	}

}
