package com.example.acidfiles;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



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

	 private List<String> items = null;
	    
	    @Override
	    public void onCreate(Bundle icicle) {
	        super.onCreate(icicle);
	        setContentView(R.layout.activity_view_tran);
	        getFiles(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/").listFiles());
	    }
	    @Override
	    protected void onListItemClick(ListView l, View v, int position, long id){
	    	String item1 = (String) getListAdapter().getItem(position);
    		File files = new File(item1);
    		Uri my = Uri.fromFile(files);
	    	
	        int selectedRow = (int)id;
	        if(selectedRow == 0){
	            getFiles(new File("/").listFiles());

	        }else{
	      		
        
        		
        		
        		if(item1.endsWith(".apk") || item1.endsWith(".APK")){
        			Intent intent = new Intent(); 
        			intent.setAction(android.content.Intent.ACTION_VIEW); 
        			intent.setDataAndType(Uri.fromFile(files),"application/vnd.android.package-archive"); 
        			startActivity(intent); 
        		
		                
	            	}else{
	            		if(mPlayer == null){
	            			mPlayer = MediaPlayer.create(this, my);
	            		}
	            		if(item1.endsWith(".mp3") || item1.endsWith(".MP3")){
	            			if(mPlayer.isPlaying()){
		            			mPlayer.pause();}else{
	            			if(mPlayer.isPlaying() == false){
	            		mPlayer.start();}  
	            			
	            		
	            				
		            			}
	            			 } else{
	            		
       			 File file = new File(items.get(selectedRow));
	         	            if(file.isDirectory()){
	         	                getFiles(file.listFiles());	 
	         	            }else{
	         	            	if(file.isFile()){
	         	            		
	         	            		 String item = (String) getListAdapter().getItem(position);
	         	            		
	         	            		
	         		               
	         		           
	         		                File f = new File(item);
	         		                

	         		                StringBuilder text = new StringBuilder();

	         		                try {
	         		                    BufferedReader br = new BufferedReader(new FileReader(f));
	         		                    String line;

	         		                    while ((line = br.readLine()) != null) {
	         		                        text.append(line);
	         		                        text.append('\n');
	         		                    } br.close();
	         		                }
	         		                catch (IOException e) {
	         		                	
	         		                }
	         	            	
	         		                new AlertDialog.Builder(this).setMessage(text).show();
	         	            	
	            		}else{
	                 new AlertDialog.Builder(this)
	                 .setTitle("This file is not a directory")
	                 .setNeutralButton("OK", new DialogInterface.OnClickListener(){
	                     public void onClick(DialogInterface dialog, int button){
	                         //do nothing
	                     }
	                 })
	                 .show();
	            }
	        }
	         	            }
	    }}
	            		}
	            		
	        
	    
	    
	    
	    
	  
	    
	    private void getFiles(File[] files){
	        items = new ArrayList<String>();
	        items.add(getString(R.string.ok));
	        for(File file : files){
	            items.add(file.getPath());
	        }
	        ArrayAdapter<String> fileList = new ArrayAdapter<String>(this,R.layout.textview, items);
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
