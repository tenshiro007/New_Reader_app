package com.example.newreaderapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private TextView txtTitle;
    private RecyclerView recyclerView;

    private NewsAdapter adapter;
    private ArrayList<NewsItem>news;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        news=new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerView);
        adapter=new NewsAdapter(this);

//        adapter.setNews(news);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        new GetNew().execute();
    }

    private class GetNew extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            InputStream inputStream = getInputStream();
            if (null != inputStream) {
                try {
                    initXMLPullParser(inputStream);
                } catch (XmlPullParserException | IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            Log.d(TAG, "onPostExecute: "+news.toString());
            adapter.setNews(news);
        }

        private void initXMLPullParser(InputStream inputStream) throws XmlPullParserException, IOException {
            Log.d(TAG, "initXMLPullParser: Initializing XML Pull Parser");
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(inputStream, null);

            parser.next();
            parser.require(XmlPullParser.START_TAG, null, "rss");
            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                parser.require(XmlPullParser.START_TAG, null, "channel");
                while (parser.next() != XmlPullParser.END_TAG) {
                    if (parser.getEventType() != XmlPullParser.START_TAG) {
                        continue;
                    }

                    if (parser.getName().equals("item")) {
                        String title = "";
                        String description = "";
                        String link = "";
                        String date = "";

                        parser.require(XmlPullParser.START_TAG, null, "item");
                        while (parser.next() != XmlPullParser.END_TAG) {
                            if (parser.getEventType() != XmlPullParser.START_TAG) {
                                continue;
                            }
                            String tagName = parser.getName();
                            if (tagName.equals("title")) {
                                title=getContext(parser,"title");

                            } else if (tagName.equals("description")) {
                                description=getContext(parser,"description");

                            } else if (tagName.equals("link")) {
                                link=getContext(parser,"link");

                            } else if (tagName.equals("pubdate")) {
                                date=getContext(parser,"pubdate");
                            }else{
                                skipTag(parser);
                            }
                        }
                        NewsItem item=new NewsItem(title,description,link,date );
                        news.add(item);

                    } else {
                        skipTag(parser);
                    }
                }
            }
        }

        private String getContext(XmlPullParser parser,String tag) throws IOException, XmlPullParserException {
            String content="";
            parser.require(XmlPullParser.START_TAG,null,tag);
            if(parser.next()==XmlPullParser.TEXT){
                content=parser.getText();
                parser.next();
            }
            return content;
        }

        private void skipTag(XmlPullParser parser) throws XmlPullParserException, IOException {
            if(parser.getEventType() !=XmlPullParser.START_TAG){
                throw new IllegalStateException();
            }
            int number=1;
            while (number !=0){
                switch (parser.next()){
                    case XmlPullParser.START_TAG:
                        number++;
                        break;
                    case XmlPullParser.END_TAG:
                        number--;
                        break;
                    default:
                        break;

                }
            }
        }

        private InputStream getInputStream() {
            try {
                URL url = new URL("https://www.posttoday.com/rss/src/breakingnews.xml");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
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