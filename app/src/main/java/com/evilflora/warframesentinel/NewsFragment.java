package com.evilflora.warframesentinel;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NewsFragment extends Fragment {

    private final String CurrentFileName = "NewsFragment"; // filename
    List<NewsClass> newsList = new ArrayList<>();
    NewsListView adapterNews;
    Handler hLoadNews = new Handler();
    ListView listViewNews;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.news_content, container, false);
        newsList.clear();
        getActivity().setTitle(getString(R.string.news));

        listViewNews = view.findViewById(R.id.listView_news);
        adapterNews = new NewsListView(getContext(), newsList);
        listViewNews.setAdapter(adapterNews);

        hLoadNews.post(runnableLoadNews);

        return view;
    }

    private Runnable runnableLoadNews = new Runnable() {
        @Override
        public void run() {
            load();
            hLoadNews.postDelayed(this, 60 * 1000);
        }
    };

    void load() {
        try {
            JSONArray news_update = MenuActivity.warframeWorldState.getNews();

            boolean stop;
            for (int i = 0; i < news_update.length(); i++) {

                stop = false;

                NewsClass news_tmp = new NewsClass(news_update.getJSONObject(i));

                for(int j = 0; j < newsList.size(); j++) { // we compare to the old list
                    if (news_tmp.get_id().compareTo(newsList.get(j).get_id()) == 0) { // If the news exists in our old list
                        stop = true; // we indicate that we have found one
                        break; // we break the loop because it is useless to continue
                    }
                }

                if (!stop && news_tmp.get_language_code().compareTo("en") == 0) { // if we have not found news and the news corresponds to the desired language
                    Log.i(CurrentFileName,"Ajout de la news ID : " + news_tmp.get_id());
                    newsList.add(news_tmp);
                    Collections.sort(newsList,(o1, o2) -> Long.compare(o1.get_date_activation(),o2.get_date_activation())); // Sort by the most recent news
                    adapterNews.notifyDataSetChanged(); // we update the view
                }
            }
        } catch (Exception ex) {
            Log.e(CurrentFileName,"Impossible d'ajouter la news | " + ex.getMessage());
        }
    }
}

