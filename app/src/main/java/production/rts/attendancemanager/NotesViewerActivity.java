package production.rts.attendancemanager;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Console;
import java.util.ArrayList;

public class NotesViewerActivity extends AppCompatActivity {


    ArrayList<String> Noteslist = new ArrayList<>();
    ArrayList<String> keyList = new ArrayList<>();

    FloatingActionButton floatBtn;
    private FirebaseAuth mAuth;
    ListView listView;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_viewer);
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser muser = mAuth.getCurrentUser();
        String uid = muser.getUid();

        database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Notes").child(uid);

        floatBtn = findViewById(R.id.floatingActionButton);
        listView = findViewById(R.id.listView1);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Noteslist);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    Noteslist.add(String.valueOf(dsp.getValue())); //add result into array list
                    keyList.add(String.valueOf(dsp.getKey()));
                }
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        floatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoNotesEditor = new Intent(NotesViewerActivity.this, NotesActivity.class);
                startActivity(gotoNotesEditor);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                database = FirebaseDatabase.getInstance();
                String uid = muser.getUid();

                DatabaseReference keyRef = database.getReference("Notes").child(uid);
                keyRef.child(keyList.get(position)).removeValue();
                Noteslist.removeAll(Noteslist);
                Toast.makeText(NotesViewerActivity.this, "Note Deleted", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }
}
