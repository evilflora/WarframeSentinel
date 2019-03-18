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

    final String CurrentFileName = "NewsFragment";
    List<NewsClass> newsList = new ArrayList<>(); // Liste des invasions
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

        hLoadNews.post(runnableLoadNews); // On rafraichis toutes les secondes les timers

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
            JSONArray news_update = MenuActivity.warframeWorldState.getNews(); // on récupère la liste des alertes

            boolean stop;
            for (int i = 0; i < news_update.length(); i++) { // on parcours la nouvelle liste (surement plus grande que l'ancienne)
                stop = false; // on remet à false
                NewsClass news_tmp = new NewsClass(news_update.getJSONObject(i));
                for(int j = 0; j < newsList.size(); j++) { // on compare à l'ancienne liste
                    if (news_tmp.get_id().compareTo(newsList.get(j).get_id()) == 0) { // Si la news n'existe pas encore dans notre liste
                        stop = true; // on indique que l'on en a trouvé une
                        break; // on casse la boucle car inutile de continuer
                    }
                }
                if (!stop && news_tmp.get_language_code().compareTo("en") == 0) { // si on n'a pas quitté la boucle alors c'est que cette alerte est nouvelle
                    Log.i(CurrentFileName,"Added new news id: " + news_tmp.get_id());
                    newsList.add(news_tmp); // on l'ajoute à la liste
                    Collections.sort(newsList,(o1, o2) -> Long.compare(o1.get_date_activation(),o2.get_date_activation()));
                    adapterNews.notifyDataSetChanged();

                }
            }
        } catch (Exception ex) {
            Log.e(CurrentFileName,"Cannot add new news | " + ex.getMessage());
        }
    }
}

