package com.example.newreaderapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private TextView txtTitle;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView=findViewById(R.id.recyclerView);
    }
    private class GetNew extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            InputStream inputStream=getInputStream();
            if(null !=inputStream){
                try {
                    initXMLPullParser(inputStream);
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        private void initXMLPullParser(InputStream inputStream) throws XmlPullParserException {
            Log.d(TAG, "initXMLPullParser: Initializing XML Pull Parser");
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES,false);
            parser.setInput(inputStream,null);
        }

        private InputStream getInputStream(){
            try {
                URL url=new URL("https://www.posttoday.com/rss/src/breakingnews.xml");
                HttpURLConnection connection= (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                return connection.getInputStream();
//                connection.setConnectTimeout(10);
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}