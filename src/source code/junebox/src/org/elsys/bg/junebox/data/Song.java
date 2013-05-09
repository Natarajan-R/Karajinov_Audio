package org.elsys.bg.junebox.data;

import java.net.MalformedURLException;
import java.net.URL;

public class Song {

	private String name;
	private String directory;
	private URL url;
		
	public Song(String name, String directory) {
		this.setName(name);
		this.setDirectory(directory);
		try {
			this.url = new URL("file:" + directory + "\\" + name);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	public Song(String fullpath) {
		try {
			this.url = new URL("file:" + fullpath);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	public String getName() {
		return name;
	}
	
	public String getDirectory() {
		return directory;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setDirectory(String directory) {
		this.directory = directory;
	}

	public void setURL(URL url) {
		this.url = url;
	}

	public URL getURL() {
		return url;
	}
	
	public boolean equals(Song song) {
		if(name.equals(song.getName()))
			if(directory.equals(song.getDirectory()))
				return true;
		return false;
	}
}
