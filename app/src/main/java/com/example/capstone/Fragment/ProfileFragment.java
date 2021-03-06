package com.example.capstone.Fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.capstone.Model.Student;
import com.example.capstone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {

    private FirebaseUser student;
    private DatabaseReference reference;
    private String userID;
    private Button btnChapterOneProgress,btnChapterTwoProgress;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        btnChapterOneProgress=rootView.findViewById(R.id.btnChapterOneProgress);
        btnChapterTwoProgress=rootView.findViewById(R.id.btnChapterTwoProgress);

        student = FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance("https://capstoneproject-4b898-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Students");
        reference.keepSynced(true);

        userID=student.getUid();

        final TextView ProfileFName = rootView.findViewById(R.id.ProfileFName);
        final TextView ProfileLName = rootView.findViewById(R.id.ProfileLName);
        final TextView ProfileEmail = rootView.findViewById(R.id.ProfileEmail);
        final TextView ProfileSection = rootView.findViewById(R.id.ProfileSection);
        final TextView ChapterOneProgress = rootView.findViewById(R.id.tvChapterOnePercent);
        final TextView ChapterTwoProgress = rootView.findViewById(R.id.tvChapter2Percent);
        final ImageView civProfilePicture = rootView.findViewById(R.id.civProfilePicture);

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Student studentProfile= snapshot.getValue(Student.class);
                if(studentProfile!=null){
                    String sfname = studentProfile.getSFname();
                    String slname = studentProfile.getSLname();
                    String semail = studentProfile.getSEmail();
                    String ssection = studentProfile.getSsection();
                    String spicture = studentProfile.getSpicture();
                    int Chapter_1_Progress = studentProfile.getChapter_1_Progress();
                    int Chapter_2_Progress = studentProfile.getChapter_2_Progress();

                    if(spicture!=null){
                        Glide.with(requireActivity()).load(spicture).into(civProfilePicture);
                    }
                    if(Chapter_1_Progress!=0){
                        ChapterOneProgress.setText(Chapter_1_Progress+"%");
                    }
                    if(Chapter_2_Progress!=0){
                        ChapterTwoProgress.setText(Chapter_2_Progress+"%");
                    }
                    ProfileFName.setText(sfname);
                    ProfileLName.setText(slname);
                    ProfileEmail.setText(semail);
                    ProfileSection.setText(ssection);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Something wrong happen!", Toast.LENGTH_LONG).show();

                System.out.println("get "+error);

            }
        });

        btnChapterOneProgress.setOnClickListener(view -> sendToAnotherFragment("Plate Tectonics",1));
        btnChapterTwoProgress.setOnClickListener(view -> sendToAnotherFragment("Earth's Interior",2));

        return rootView;
    }

    public void sendToAnotherFragment(String ChapterName, int ChapterNumber){

        ProgressFragment progressFragment=new ProgressFragment();

        Bundle bundle = new Bundle();
        bundle.putString("ChapterName",ChapterName);
        bundle.putInt("ChapterNumber",ChapterNumber);
        progressFragment.setArguments(bundle);

                getFragmentManager()
                .beginTransaction()
                .replace(R.id.profileMainFragment,progressFragment)
                .addToBackStack(null)
                .commit();

    }
}
