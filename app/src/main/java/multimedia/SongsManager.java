package multimedia;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;

public class SongsManager {
	// SDCard Path
	final String MEDIA_PATH = new String("/sdcard/");
	private ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
	private final Activity context;
	// Constructor
	public SongsManager(Activity context){
		this.context = context;
	}
//
	/**
	 * Function to read all mp3 files from sdcard
	 * and store the details in ArrayList
	 * */
	public ArrayList<HashMap<String, String>> getPlayList(){
		File home = new File(MEDIA_PATH);

		if (home.listFiles(new FileExtensionFilter()).length > 0) {
			for (File file : home.listFiles(new FileExtensionFilter())) {
				HashMap<String, String> song = new HashMap<String, String>();
				song.put("songTitle", file.getName().substring(0, (file.getName().length() - 4)));
				song.put("songPath", file.getPath());
				// Adding each song to SongList
				songsList.add(song);
			}
		}
		// return songs list array
		return songsList;
	}

	/**
	 * Class to filter files which are having .mp3 extension
	 * */
	class FileExtensionFilter implements FilenameFilter {
		public boolean accept(File dir, String name) {
			return (name.endsWith(".mp3") || name.endsWith(".MP3"));
		}
	}
}
