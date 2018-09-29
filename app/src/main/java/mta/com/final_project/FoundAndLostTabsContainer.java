package mta.com.final_project;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class FoundAndLostTabsContainer extends Fragment {

    private final String FOUND = "found";
    private final String LOST = "lost";
    private View view;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private FoundAndLostAnimalsAdapter foundAndLostAnimalsAdapter;

    public FoundAndLostTabsContainer() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_found_and_lost_tabs_container, container, false);
        viewPager = view.findViewById(R.id.viewPager_foundAndLostTabsContainer);
        tabLayout = view.findViewById(R.id.tabsLayout_foundAndLostTabsContainer);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        foundAndLostAnimalsAdapter = new FoundAndLostAnimalsAdapter(getChildFragmentManager());
        foundAndLostAnimalsAdapter.addFragment(new AnimalsListFragment(FOUND), "Found");
        foundAndLostAnimalsAdapter.addFragment(new AnimalsListFragment(LOST), "Lost");

        viewPager.setAdapter(foundAndLostAnimalsAdapter);
    }
}
