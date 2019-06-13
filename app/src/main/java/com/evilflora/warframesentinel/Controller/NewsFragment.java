package com.evilflora.warframesentinel.Controller;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.evilflora.warframesentinel.Modele.NewsClass;
import com.evilflora.warframesentinel.R;
import com.evilflora.warframesentinel.Vue.NewsListView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NewsFragment extends Fragment {

    private static String _currentFileName = "NewsFragment"; // filename
    private List<NewsClass> _newsList = new ArrayList<>();
    private NewsListView _adapterNews;
    private Handler _hLoadNews = new Handler();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.news_content, container, false);
        if (getActivity() != null) getActivity().setTitle(getString(R.string.news));

        ListView listViewNews = view.findViewById(R.id.listView_news);
        _adapterNews = new NewsListView(getContext(), _newsList);
        listViewNews.setAdapter(_adapterNews);

        _hLoadNews.post(runnableLoadNews);

        return view;
    }

    private Runnable runnableLoadNews = new Runnable() {
        @Override
        public void run() {
            try {
                JSONArray newsUpdate = MenuActivity.warframeWorldState.getNews();

                boolean stop;
                for (int i = 0; i < newsUpdate.length(); i++) {

                    stop = false;

                    NewsClass tmp = new NewsClass(getActivity(),newsUpdate.getJSONObject(i));

                    for(int j = 0; j < _newsList.size(); j++) { // we compare to the old list
                        if (tmp.getId().compareTo(_newsList.get(j).getId()) == 0) { // If the news exists in our old list
                            stop = true; // we indicate that we have found one
                            break; // we break the loop because it is useless to continue
                        }
                    }

                    if (!stop && tmp.getLanguageCode().compareTo("en") == 0) { // if we have not found news and the news corresponds to the desired language
                        Log.i(_currentFileName,"Ajout de la news ID : " + tmp.getId());
                        _newsList.add(tmp);
                        Collections.sort(_newsList,(o1, o2) -> Long.compare(o1.getDateActivation(),o2.getDateActivation())); // Sort by the most recent news
                        if (_adapterNews.getCount() >0) _adapterNews.notifyDataSetChanged(); // we update the view
                    }
                }
            } catch (Exception ex) {
                Log.e(_currentFileName,"Impossible d'ajouter la news | " + ex.getMessage());
            }
            _hLoadNews.postDelayed(this, 60000);
        }
    };

}

