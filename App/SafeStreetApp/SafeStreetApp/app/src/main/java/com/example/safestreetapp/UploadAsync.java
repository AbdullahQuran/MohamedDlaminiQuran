package com.example.safestreetapp;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.ref.WeakReference;

public class UploadAsync extends AsyncTask<Uri, String, Void> {

    private WeakReference<Report_Violation> uploadWeakReference;
    private boolean stop = false;

    UploadAsync(Report_Violation uploadViolationActivity){
        uploadWeakReference = new WeakReference<Report_Violation>(uploadViolationActivity);
    }

    @Override
    protected void onPreExecute() {

        Report_Violation uploadViolationActivity = uploadWeakReference.get();
        if(uploadViolationActivity == null || uploadViolationActivity.isFinishing()){
            return;
        }

        if(uploadViolationActivity.filepathlen < 39){
           // Toast.makeText(uploadViolationActivity.context, "For Security reason image must be uploaded from the Safestreet directory only - ", Toast.LENGTH_SHORT).show();
            stop = true;
        }
        else if(uploadViolationActivity.filepath == null) {
            Toast.makeText(uploadViolationActivity.context, "Please Select An Image of the Violation", Toast.LENGTH_SHORT).show();
            stop = true;

        }
        else if(!uploadViolationActivity.BMenc.equals(uploadViolationActivity.BMdec)){

            Toast.makeText(uploadViolationActivity.context, "You are trying to upload an image not taken by the App", Toast.LENGTH_SHORT).show();
            stop = true;
        }


        }



    @Override
    protected Void doInBackground(Uri... uris) {

        Report_Violation uploadViolationActivity = uploadWeakReference.get();
        if(uploadViolationActivity == null || uploadViolationActivity.isFinishing()){
            return null;
        }

        String result = "";
        Uri filepath = uris[0];

        if (!stop) {

            String encFileName = filepath.getPath().substring(filepath.getPath().length() - 39);
            File sdCard = Environment.getExternalStorageDirectory();

            File directory = new File(sdCard.getAbsolutePath() + "/Android/data/com.example.safestreetapp/files/Pictures");

            File imageFile = new File(directory, "JPEG_" + encFileName);

            AndroidNetworking.upload("http://10.0.2.2/SafeStreetMobile/upload.php")
                    .addMultipartFile("image", imageFile)
                    .setPriority(Priority.HIGH)
                    .build()
                    .setUploadProgressListener(new UploadProgressListener() {
                        @Override
                        public void onProgress(long bytesUploaded, long totalBytes) {

                        }
                    })
                    .getAsString(new StringRequestListener() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                int status = jsonObject.getInt("status");
                                String message = jsonObject.getString("message");
                                if (status == 0) {

                                    Toast.makeText(uploadViolationActivity.context, "Unable to Upload Image" + message, Toast.LENGTH_SHORT).show();

                                } else {
                                    //Toast.makeText(uploadViolationActivity.context, message, Toast.LENGTH_SHORT).show();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(uploadViolationActivity.context, "Parsing Error", Toast.LENGTH_SHORT).show();

                            }

                        }

                        @Override
                        public void onError(ANError anError) {
                            anError.printStackTrace();
                            Toast.makeText(uploadViolationActivity.context, "Error Uploading Image", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        return null;
    }
    @Override
    protected void onPostExecute(Void string) {


    }
}
