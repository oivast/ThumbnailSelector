package com.example.thumbnailselector;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener{
	
	//Thumbnail Preferences
	private static final int OUTPUT_X = 200;
	private static final int OUTPUT_Y = 200;
	private static final int ASPECT_X = 1;
	private static final int ASPECT_Y = 1;
	private static final boolean SCALE = false;
	private static final boolean SCALE_UP_IF_NEEDED = true;	

	//Request Code
	private static final int SELECT_THUMBNAIL = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);		
	
		Button buttonChangeThumbnail = (Button) findViewById(R.id.button_change_thumbnail);
		buttonChangeThumbnail.setOnClickListener(this);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.button_change_thumbnail){
			startActivityForResult(CreateSelectPictureIntent(), SELECT_THUMBNAIL);
		}
	}
	
	//Create Intent to Select and Crop a Imagem from Gallery
	private Intent CreateSelectPictureIntent(){
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
		return selectPicture;
	}

}
