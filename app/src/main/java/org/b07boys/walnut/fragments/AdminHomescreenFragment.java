package org.b07boys.walnut.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.b07boys.walnut.R;
import org.b07boys.walnut.courses.Course;
import org.b07boys.walnut.courses.CourseCatalogue;
import org.b07boys.walnut.courses.ModifyCourseType;
import org.b07boys.walnut.courses.OnChangeCourseListener;
import org.b07boys.walnut.courses.SessionType;
import org.b07boys.walnut.database.adapters.CourseListAdapter;
import org.b07boys.walnut.databinding.FragmentAdminHomescreenBinding;
import org.b07boys.walnut.main.MainActivity;

import java.util.ArrayList;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminHomescreenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminHomescreenFragment extends Fragment {

    private FragmentAdminHomescreenBinding binding;

    public AdminHomescreenFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     *
     * @return A new instance of fragment AdminHomescreenFragment.
     */
    public static AdminHomescreenFragment newInstance(String param1, String param2) {
        AdminHomescreenFragment fragment = new AdminHomescreenFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity)getActivity()).setActionBarTitles("Walnut", "Admin Page");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAdminHomescreenBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CourseCatalogue.getInstance().registerListener(new OnChangeCourseListener() {
            @Override
            public void onModify(Course course, ModifyCourseType modifyType) {
                ArrayList<Course> coursesFromDb = new ArrayList<>(CourseCatalogue.getInstance().getCourses());
                ArrayList<CourseListAdapter> adaptedCourses = new ArrayList<>();
                for (Course c : coursesFromDb) {
                    CourseListAdapter courseItem = new CourseListAdapter(c);
                    adaptedCourses.add(courseItem);
                }
                ArrayAdapter adapter = new ArrayAdapter<>(getActivity(),
                        R.layout.activity_listview, adaptedCourses);
                binding.courseList.setAdapter(adapter);
            }
        });

        Set<Course> courses = CourseCatalogue.getInstance().getCourses();

        String[] courseCodes = new String[courses.size()];

        int i = 0;
        for (Course course : courses) {
            courseCodes[i] = course.getCode();
            i++;
        }

        ArrayList<CourseListAdapter> courses1 = new ArrayList<>();
        for (Course c : courses) {
            CourseListAdapter temp2 = new CourseListAdapter(c);
            courses1.add(temp2);
        }

        ArrayAdapter adapter = new ArrayAdapter<CourseListAdapter>(getActivity(),
                R.layout.activity_listview, courses1);

        ListView listView = (ListView) binding.courseList;
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CourseListAdapter temp = ((CourseListAdapter)adapterView.getAdapter().getItem(i));
                String text = "";
                SessionType[] stuff = temp.getCourse().getOfferingSessions();
                String[] stuff2 = new String[temp.getCourse().getOfferingSessions().length];
                for (int ind = 0; ind < stuff2.length; ind++){
                    stuff2[ind] = stuff[ind].name();
                }
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment)
                        .navigate(AdminHomescreenFragmentDirections
                                .actionAdminHomescreenFragmentToCoursePopUpFragment("",null,null,null)
                                .setCourseCode(temp.getCourse().getCode()).setCourseTitle(temp.getCourse().getName())
                                .setPrerequisites(temp.getCourse().getPrerequisiteUIDS()).setCourseUID(temp.getCourse().getUID())
                                .setSessions(stuff2));
            }

        });


        binding.addCourseButton.setOnClickListener(viewButton -> {
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment)
                    .navigate(AdminHomescreenFragmentDirections
                            .actionAdminHomescreenFragmentToAddCoursePopUpFragment());
        });
        SearchView search = binding.searchBar;

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        MenuHost menuHost = requireActivity();
        menuHost.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.clear();
                menuInflater.inflate(R.menu.main_top_app_bar, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }
        });
    }
}


