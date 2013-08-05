package com.example.thumbnailselector;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	// Thumbnail	
	private static final int OUTPUT_X = 200; // use small values
	private static final int OUTPUT_Y = 200; // use small values
	private static final int ASPECT_X = 1;
	private static final int ASPECT_Y = 1;
	private static final boolean SCALE = true;
	private static final boolean SCALE_UP_IF_NEEDED = true;
	private static final String FILE_NAME = "thumbnail.jpg";
	private ImageView thumbnailView;
	
	// Request Code
	private static final int SELECT_THUMBNAIL = 1;
	
	// Shared Preferences
	private static final String SHARED_PREFERENCES_HAS_THUMB = "shared_preferences.has_thumb";
	private SharedPreferences sharedPreferences;


	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		thumbnailView = (ImageView) findViewById(R.id.thumbnail_view);
		setImageViewWidth();

		sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
		if (sharedPreferences.getBoolean(SHARED_PREFERENCES_HAS_THUMB, false)) {
			updateThumnailView();
		}

		Button buttonChangeThumbnail = (Button) findViewById(R.id.button_change_thumbnail);
		buttonChangeThumbnail.setOnClickListener(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == SELECT_THUMBNAIL) {
			if (data != null) {
				final Bundle extras = data.getExtras();
				if (extras != null) {
					Bitmap photo = extras.getParcelable("data");
					if (saveThumbnail(photo)) {
						// Save status on sharedPreference
						Editor editor = sharedPreferences.edit();
						editor.putBoolean(SHARED_PREFERENCES_HAS_THUMB, true);
						editor.commit();
						updateThumnailView();
					}
				}
			}
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.button_change_thumbnail) {
			startActivityForResult(CreateSelectPictureIntent(),	SELECT_THUMBNAIL);
		}
	}

	/** Create Intent to Select and Crop a Image from Gallery
	 * @return A select picture intent
	 */
	private Intent CreateSelectPictureIntent() {
		Intent selectPicture = new Intent(Intent.ACTION_GET_CONTENT,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		selectPicture.setType("image/*");
		selectPicture.putExtra("crop", "true");
		selectPicture.putExtra("aspectX", ASPECT_X);
		selectPicture.putExtra("aspectY", ASPECT_Y);
		selectPicture.putExtra("outputX", OUTPUT_X);
		selectPicture.putExtra("outputY", OUTPUT_Y);
		selectPicture.putExtra("scale", SCALE);
		selectPicture.putExtra("scaleUpIfNeeded", SCALE_UP_IF_NEEDED);
		selectPicture.putExtra("return-data", true);
		return selectPicture;
	}
		
	/** Save thumbnail on Internal Storage
	 * @param thumbnail
	 * @return true if successful
	 */
	private boolean saveThumbnail(Bitmap thumbnail) {
		FileOutputStream outputStream;
		try {
			outputStream = openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
			thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
			outputStream.flush();
			outputStream.close();
		} catch (IOException e) {
			Toast.makeText(this, R.string.writeFileError, Toast.LENGTH_LONG)
					.show();
			return false;
		}
		return true;
	}

	/** Set ImageView width based on screen width */
	private void setImageViewWidth() {
		@SuppressWarnings("deprecation")
		// getSize(Point) needs API 13 and a use minimum API 8
		int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
		if (OUTPUT_X > screenWidth)
			thumbnailView.getLayoutParams().width = screenWidth;
		else
			thumbnailView.getLayoutParams().width = OUTPUT_X;
	}

	/** Set saved thumbnail image on thumbnailView */
	private void updateThumnailView() {
		File file = new File(getFilesDir(), FILE_NAME);
		thumbnailView.setImageBitmap(null); // makes image view update
		thumbnailView.setImageURI(Uri.fromFile(file));
	}

}
