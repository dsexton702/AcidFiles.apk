package com.example.acidfiles;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class AcidFiles extends ListActivity {
	public static MediaPlayer mPlayer = null;
	private String CURRENT_DIRECTORY;
	private List<String> items = null;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		/* We want to start out at the root of the sdcard */
		CURRENT_DIRECTORY = Environment.getExternalStorageDirectory()
				.getAbsolutePath();

		if (!CURRENT_DIRECTORY.endsWith("/"))
			CURRENT_DIRECTORY += "/";

		setContentView(R.layout.activity_view_tran);
		getFiles(new File(CURRENT_DIRECTORY).listFiles());
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		String item_path;
		File current_file;
		Uri uri;

		/* Always make sure we have a forward slash */
		if (!CURRENT_DIRECTORY.endsWith("/"))
			CURRENT_DIRECTORY += "/";

		item_path = (String) getListAdapter().getItem(position);
		item_path = CURRENT_DIRECTORY + item_path;
		current_file = new File(item_path);
		uri = Uri.fromFile(current_file);

		int selectedRow = (int) id;
		if (selectedRow == 0) {
			/* The "back" option */
			String target_path = current_file.getParentFile().getParent();
			if (target_path == null)
				target_path = "/";

			CURRENT_DIRECTORY = target_path;

			if (!(CURRENT_DIRECTORY == "/")) {
				/* Make sure that we're not trying to go back at the root dir */
				getFiles(new File(CURRENT_DIRECTORY).listFiles());
			}
		} else {
			if (item_path.toLowerCase(Locale.US).endsWith(".apk")) {
				Intent intent = new Intent();
				intent.setAction(android.content.Intent.ACTION_VIEW);
				intent.setDataAndType(Uri.fromFile(current_file),
						"application/vnd.android.package-archive");
				startActivity(intent);

			} else if (item_path.toLowerCase(Locale.US).endsWith(".mp3")) {
				if (mPlayer == null) {
					mPlayer = MediaPlayer.create(this, uri);
				}
				if (mPlayer.isPlaying()) {
					/* The mp3 is playing, so we pause it */
					mPlayer.pause();
				} else {
					/* If not we start it */
					mPlayer.start();
				}
			} else {
				if (current_file.isDirectory()) {
					/* We want to list the directory */
					if (current_file.listFiles() == null) {
						/*
						 * TODO: Write error handling for not being able to
						 * access the directory.
						 */
						System.out.println("Cannot access directory");
					} else {
						CURRENT_DIRECTORY = current_file.getPath();
						getFiles(current_file.listFiles());
					}
				} else {
					if (current_file.isFile()) {
						StringBuilder text = new StringBuilder();

						try {
							BufferedReader br = new BufferedReader(
									new FileReader(current_file));
							String line;

							while ((line = br.readLine()) != null) {
								text.append(line);
								text.append('\n');
							}
							br.close();
						} catch (IOException e) {
						}

						new AlertDialog.Builder(this).setMessage(text).show();

					} else {
						new AlertDialog.Builder(this)
								.setTitle("This file is not a directory")
								.setNeutralButton("OK",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int button) {
												// do nothing
											}
										}).show();
					}
				}
			}
		}
	}

	private void getFiles(File[] files) {
		String file_path = "";

		items = new ArrayList<String>();
		items.add(getString(R.string.ok));

		for (File file : files) {
			file_path = file.getPath();

			if (file_path.startsWith(CURRENT_DIRECTORY)) {
				/* Remove the path from the string */
				file_path = file_path.substring(CURRENT_DIRECTORY.length());
			}

			if (file_path.startsWith("/")) {
				/* Remove the leading forward slash */
				file_path = file_path.substring(1);
			}

			items.add(file_path);
		}
		ArrayAdapter<String> fileList = new ArrayAdapter<String>(this,
				R.layout.textview, items);
		setListAdapter(fileList);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_view_tran, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
